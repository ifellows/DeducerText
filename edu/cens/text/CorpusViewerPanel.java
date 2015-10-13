/**
 * A panel containing a corpus viewer.
 * 
 * It consists of: 
 * - a table of all documents, 
 * 
 * - a text area which shows the full text of the document selected in the table 
 * 
 * - a text field that allows you to select/go to a specific doc. in the table
 * 
 * - an optional combobox used to select the active corpus.  
 * This can be replaced with a label displaying the name of the active corpus.
 * 
 */
package edu.cens.text;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.rosuda.JGR.JGR;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.deducer.Deducer;
import org.rosuda.deducer.data.DataViewerTab;
import org.rosuda.deducer.toolkit.HelpButton;
import org.rosuda.deducer.widgets.ObjectChooserWidget;

public class CorpusViewerPanel extends DataViewerTab //TODO extend JDialog instead?
{
	JTextArea documentTextArea;
	JTable documentTable;
	RObjectChooser corpusSelector; //TODO replace with a friendlier corpus selection device, probably a scrollable list.
	JFormattedTextField goToField;
	boolean showCorpusSelector = false;
	private JLabel corpusNameLabel;
	private static final String HELP_URL = "index.php?n=Main.ViewCorpus";

	/**
	 * 
	 * @param showCorpusSelector if true, display the corpus selecting combobox,
	 * else display a JLabel of the currently active corpus
	 */
	public CorpusViewerPanel(boolean showCorpusSelector)
	{
		
		this.showCorpusSelector = showCorpusSelector;

		corpusNameLabel = new JLabel("");
		
		corpusSelector = new RObjectChooser(this);
		corpusSelector.setClassFilter("Corpus");
		corpusSelector.refreshObjects();

		corpusSelector.getComboBox().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String corpus = corpusSelector.getSelectedObject();
				if (corpus != null)
				{
					setData(corpus);
				}
			}
		});

		documentTextArea = new JTextArea();
		documentTextArea.setLineWrap(true);
		documentTextArea.setWrapStyleWord(true);
		documentTextArea.setEditable(false); //TODO It would be nice, but it would be difficult to make this work

		goToField = new JFormattedTextField(5);
		goToField.setText("0");
		goToField.setColumns(4);
		//		goToField.setInputVerifier(new InputVerifier()
		//		{
		//			public boolean verify(JComponent input)
		//			{
		//				//TODO perhaps it should be 1 based?
		//				return verifyInt(goToField.getText(), 0, documentTable.getRowCount() - 1);
		//			}
		//		});
		goToField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Integer goTo = intFromString(goToField.getText(), 1, documentTable.getRowCount());
				if (goTo != null)
				{
					documentTable.setRowSelectionInterval(goTo-1, goTo-1);
					documentTable.scrollRectToVisible(new Rectangle(documentTable.getCellRect(goTo-1, 0, true)));

				}
				else
				{
					Toolkit.getDefaultToolkit().beep();
					//goToField.requestFocus();
				}
			}
		});

		String[] columnNames = {"Doc #", "Text"};
		Object [][] tableData = {};

		//////////////////////////////////////////////////////////////
//		int tableH = 300;
//		tableData = new Object[tableH][2];
//		//Populate the table with fake entries to test
//		Random r = new Random();
//		for (int i = 1; i <= tableH; i++)
//		{
//			String fakeWord = "";
//			int wordLength = r.nextInt(10) + 3;
//
//			if (i%9 == 0)
//			{
//				wordLength *= 400; //just to test long strings
//			}
//
//			for (int j = 0; j < wordLength; j++)
//			{
//				fakeWord += (char) (r.nextInt('z' - 'a') + 'a');
//				int spaceo = r.nextInt(9) + 3;
//				if (j%spaceo == spaceo - 1)
//				{
//					fakeWord += " ";
//				}
//			}
//			tableData[i - 1][0] = new Integer(i);
//			tableData[i - 1][1] = fakeWord;
//		}
		/////////////////////////////////////////////////////////////////

		documentTable = new JTable(tableData, columnNames)
		{
			{ //Irritating, but necessary to get grid to display in JGR
				showHorizontalLines = true;
				showVerticalLines = true;
			}
			//Disable editing... for now.
			public boolean isCellEditable(int rowIndex, int vColIndex) 
			{
				return false;
			}
		};
	
		setupTable();
		
		documentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			int lastIndex = -1;
			public void valueChanged(ListSelectionEvent e)
			{
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				int i = lsm.getMinSelectionIndex(); 
				
				if (lastIndex!=i && i > -1)
				{
					lastIndex=i;
					documentTextArea.setText((String)documentTable.getModel().getValueAt(i, 1));
					goToField.setText("" + (i + 1));
				}

			}
		});
		assembleGui();

		if (corpusSelector.getObjectCount() > 0)
		{
			corpusSelector.getComboBox().setSelectedIndex(0);
		}
	}

	private void setupTable()
	{
		documentTable.setShowVerticalLines(false);
		//documentTable.setBorder(new LineBorder(Color.BLACK, 1));
		documentTable.setGridColor(Color.LIGHT_GRAY);
		documentTable.setRowHeight(20);
		documentTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		documentTable.getColumnModel().getColumn(0).setMaxWidth(50);
		documentTable.getColumnModel().getColumn(0).setMinWidth(50);
		documentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		documentTable.getTableHeader().setResizingAllowed(false);
		documentTable.getTableHeader().setReorderingAllowed(false);


		documentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		documentTable.setRowSelectionAllowed(true);
		documentTable.setColumnSelectionAllowed(false);
	}
	
	private Integer intFromString(String val, int min, int max)
	{
		try
		{
			int goTo = Integer.parseInt(val);
			if (goTo >= min && goTo <= max)
			{
				return new Integer(goTo);
			}
		}
		catch (NumberFormatException e)
		{
		}
		return null;
	}

	private GridBagConstraints getTopLevelConstraints()
	{
		GridBagConstraints c =  new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(6, 6, 6, 6);
		//c.ipadx =10;
		//c.ipady =10;
		return c;
	}

	private void assembleGui()
	{
		this.setLayout(new GridBagLayout());
		JPanel everythingPanel = new JPanel(new GridBagLayout());

		//put space along outside edges of dialog.
		GridBagConstraints c = getTopLevelConstraints();
		c.insets = new Insets(5, 10, 10, 10);
		this.add(everythingPanel, c);

		//Build the document table
		//------------------------------------------------------------------------
		// Corpus Selector
		//------------------------------------------------------------------------
		c = getTopLevelConstraints();
		c.gridx = 0;
		c.weightx = 0;
		JPanel corpusSelectorPanel = new JPanel(new GridBagLayout());
		corpusSelectorPanel.add(new JLabel("Corpus:"), c);
		c.gridx = 1;
		c.weightx = 1;
		if (showCorpusSelector)
		{
			corpusSelectorPanel.add(corpusSelector, c);
		}
		else
		{
			corpusSelectorPanel.add(corpusNameLabel, c);
		}

		c = getTopLevelConstraints();
		c.gridx = 0;
		c.weighty = 0;
		everythingPanel.add(corpusSelectorPanel, c);
		//-------------------------------------------------------------------------
		// Goto selection
		//------------------------------------------------------------------------
		c = getTopLevelConstraints();
		c.gridx = 1;
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel gotoPanel = new JPanel();
		gotoPanel.add(new HelpButton(HELP_URL), BorderLayout.EAST);
		gotoPanel.add(new JLabel("Go To: "),BorderLayout.WEST);
		gotoPanel.add(goToField,BorderLayout.CENTER);
		everythingPanel.add(gotoPanel,c);

		//------------------------------------------------------------------------
		// Document text area / document selector
		//------------------------------------------------------------------------
		JSplitPane docStuffPane = new JSplitPane();
		JScrollPane docTableScrollPane = new JScrollPane(documentTable);
		docTableScrollPane.setPreferredSize(new Dimension(200, 200));

		JScrollPane docTextAreaScrollPane = new JScrollPane(documentTextArea);

		JPanel textAreaPanelWithLabel = new JPanel(new GridBagLayout());
		c = getTopLevelConstraints();
		c.insets = new Insets(0, 0, 0, 0);
		c.weighty = 0;
		c.anchor = GridBagConstraints.WEST;
		textAreaPanelWithLabel.add(new JLabel("Document Text:"),c);

		c.gridy = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		textAreaPanelWithLabel.add(docTextAreaScrollPane,c);



		docTextAreaScrollPane.setPreferredSize(new Dimension(250, 200));

		docStuffPane.setLeftComponent(docTableScrollPane);
		//docStuffPane.setRightComponent(docTextAreaScrollPane);
		docStuffPane.setRightComponent(textAreaPanelWithLabel);
		docStuffPane.setBorder(new LineBorder(Color.BLACK, 0));

		c = getTopLevelConstraints();
		c.gridy = 1;
		c.gridwidth = 2;
		everythingPanel.add(docStuffPane, c);

	}

	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CorpusViewerPanel cv2 =  new CorpusViewerPanel(false);
		f.add(cv2);
		f.setVisible(true);
	}

	@Override
	public void setData(String d)
	{
		final String corpus = d;
		
		new Thread(new Runnable(){

			public void run() {
				try
				{
					final int  nDocs = Deducer.timedEval(String.format("length(%s)", corpus)).asInteger();
					Object [][] data = new Object[nDocs][2];
					final String [] columnNames = {"Doc #", "Text"};

					for (int i = 1; i <= nDocs; i++)
					{
						String docBody = Deducer.timedEval("paste(" + corpus + "[[" + i  + "]]$content,sep='',collapse='\n')").asString(); 
							//Deducer.eval(corpus + "[[" + i + "]]").asString();//corpus + "[" + i + "][[1]]").asString();
						data[i-1][0] = i;
						data[i-1][1] = docBody;
					}
					final Object[][] dat = data;
					try {
						SwingUtilities.invokeAndWait(new Runnable(){

							public void run() {
								
								corpusNameLabel.setText(corpus);
								documentTable.setModel(new DefaultTableModel(dat, columnNames));
								setupTable();
							}
							
						});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (nDocs > 0)
					{
						documentTable.setRowSelectionInterval(0, 0);
					}
					
					
				}
				catch (REXPMismatchException e2)
				{
					e2.printStackTrace();
				}
			}
			
		}).start();

	}

	@Override
	public void refresh()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public JMenuBar generateMenuBar()
	{
		return new JMenuBar(); //null?
	}

	@Override
	public void cleanUp()
	{
		// TODO Auto-generated method stub
		
	}

}
