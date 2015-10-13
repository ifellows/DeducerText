package edu.cens.text;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.deducer.Deducer;
import org.rosuda.deducer.toolkit.HelpButton;


public class DocumentTermMatrixViewer extends JDialog 
//JPanel
{
	private JTable table;
	private RObjectChooser corpusChooser;
	final JScrollPane scrollPane;
	private static final int FREQ_COLUMN_WIDTH = 50;
	FixedColumnTable fixedTable;
	private static final String HELP_URL = "index.php?n=Main.TextTermDocumentMatrix";

	public DocumentTermMatrixViewer(Frame parent) 
	{
		super(parent);
		this.setTitle("Term Document Matrix Viewer");
		
		scrollPane = new JScrollPane();

		//These 2 lines handle case when there's no corpora to look at.
		table = new JTable();
		doTableSetup();
		
		initTableDebug(); //TODO remove this line for official releases
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = getTopLevelConstraints();
		
		scrollPane.setAutoscrolls(true);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		corpusChooser = new RObjectChooser();
		corpusChooser.setClassFilter("Corpus");
		corpusChooser.refreshObjects();
		corpusChooser.getComboBox().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String corpus = corpusChooser.getSelectedObject();
				initTableFromCorpus(corpus);
				adjustForTableSize();
			}
		});

		if (corpusChooser.getItemCount() > 0)
		{
			corpusChooser.setSelectedIndex(0);
		} //else no corpora

		JPanel topMostPanel = new JPanel(new GridBagLayout());
		c.weightx = 1;
		topMostPanel.add(corpusChooser, c);
		c.gridx = 1;
		c.weightx = 0;
		c.ipadx = 15;
		c.ipady = 15;
		topMostPanel.add(new HelpButton(HELP_URL), c);
		
		
		c = new GridBagConstraints();
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		//topMostPanel.setBackground(Color.red);
		this.add(topMostPanel,c);
		
		c= new GridBagConstraints();


		JPanel tableHeaderPanel = new JPanel(new GridBagLayout());
		c.anchor = c.CENTER;
		c.insets = new Insets(0,5,0,0);
		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0;
		c.fill = c.NONE;
		JLabel lab1 = new JLabel("Terms:");
		lab1.setPreferredSize(new Dimension(120, lab1.getPreferredSize().height));
		tableHeaderPanel.add(lab1, c);
		
		c.gridx = 1;
		c.weightx =1;
		//c.fill = c.HORIZONTAL;
		c.anchor = c.WEST;
		c.insets = new Insets(0,0,0,0);;
		tableHeaderPanel.add(new JLabel("Documents:"), c);
		
		c = getTopLevelConstraints();
		c.insets = new Insets(0,15,0,15);;
		c.weightx = 1;
		c.gridy =1;
		c.fill = c.HORIZONTAL;
		this.add(tableHeaderPanel,c);

		c = getTopLevelConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 15, 15, 15);
		this.add(scrollPane, c);
	}

	public void doTableSetup()
	{
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(true);
		table.setGridColor(Color.LIGHT_GRAY);
		table.getTableHeader().setReorderingAllowed(false);		


		for (int i = 1; i < table.getColumnCount(); i++)
		{
			table.getColumnModel().getColumn(i).setPreferredWidth(FREQ_COLUMN_WIDTH);
		}

		if (table.getColumnCount() > 0)
		{
			table.getColumnModel().getColumn(0).setPreferredWidth(115);
			table.getColumnModel().getColumn(0).setMaxWidth(175);
			
			table.getColumnModel().getColumn(1).setPreferredWidth(75);
			table.getColumnModel().getColumn(1).setMaxWidth(75);
			
			scrollPane.setViewportView(table);				
			fixedTable = new FixedColumnTable(2, scrollPane);
		
			fixedTable.getFixedTable().setShowGrid(true);
			fixedTable.getFixedTable().setGridColor(Color.LIGHT_GRAY);
		}
	}
	
	public void initTableFromCorpus(String corpus)
	{		
		try
		{
			table = new JTable();
			String tdm = Deducer.getUniqueName("dtm");
			Deducer.eval(tdm + " <- TermDocumentMatrix(" + corpus + ", control = list(tolower=FALSE, minWordLength=1))");

			//Sort the matrix by total frequency
			//TODO Make option to switch between the doc and term frequency

			Deducer.eval(tdm + "<- " + tdm + "[order(apply(" + tdm + " > 0, 1, sum), rev("+tdm+"$dimnames$Terms), decreasing=TRUE),]");



			final String[] terms = Deducer.eval(tdm+"$dimnames$Terms").asStrings(); // new String[] {"rabid", "fricative", "lateral"};

			final String [] docNames = Deducer.eval(tdm+"$dimnames$Docs").asStrings(); 
			
			//new String[nDocs + 1];// {"term", "1", "2", "3"};

			int[] rowIndices = Deducer.eval(tdm+"$i").asIntegers();
			int[] colIndices = Deducer.eval(tdm+"$j").asIntegers();
			int[] nonZeroVals = Deducer.eval(tdm+"$v").asIntegers();
			final int [] freqTotals = Deducer.eval("unname(apply(" + tdm + ",1,sum))").asIntegers();


			final Map<Point, Integer> freqMap = new HashMap<Point, Integer>();

			for(int i = 0; i < rowIndices.length; i++)
			{
				//R indexing is 1-based
				freqMap.put(new Point(rowIndices[i] - 1 , colIndices[i] - 1), nonZeroVals[i]);
			}


			table.setModel(new AbstractTableModel()
			{
				public boolean isCellEditable(int rowIndex, int columnIndex)
				{
					return false;
				}

				public Object getValueAt(int rowIndex, int columnIndex)
				{
					if (columnIndex == 0)
					{
						return terms[rowIndex];
					}
					else if (columnIndex == 1)
					{
						return freqTotals[rowIndex];
					}
					else
					{
						return freqMap.get(new Point(rowIndex, columnIndex - 2));
					}
				}

				public int getRowCount()
				{
					return terms.length;
				}

				public String getColumnName(int columnIndex)
				{
					// TODO Auto-generated method stub
					if (columnIndex == 0)
					{
						return "Terms";
					}
					else if (columnIndex == 1)
					{
						return "Total";
					}
					else
					{
						//extra column for term strings:(-1) 
						return (columnIndex - 1) + "";
						//"doc_" + columnIndex + ": " + 
						//columnNames[columnIndex - 1];
					}
				}

				public int getColumnCount()
				{
					return docNames.length + 2;
				}
			});
			
			doTableSetup();

		}
		catch (REXPMismatchException e)
		{
			e.printStackTrace();
		}
	}

	//String[] terms = {"ergot", "escargot", "excommunicate"};
	public void initTableDebug()
	{		
			table = new JTable();
			
			final String[] terms =  {"ergot", "escargot", "excommunicate"};

			final String [] docNames = {"a", "modest", "proposal"};//Deducer.eval(dtm+"$dimnames$Docs").asStrings(); 
			//new String[nDocs + 1];// {"term", "1", "2", "3"};

			int nTerms = terms.length;

			int[] rowIndices = {1,2,2};//Deducer.eval(dtm+"$i").asIntegers();
			int[] colIndices = {1,2,1};//Deducer.eval(dtm+"$j").asIntegers();
			int[] nonZeroVals = {1000, 2000, 501};//Deducer.eval(dtm+"$v").asIntegers();


			final Map<Point, Integer> freqMap = new HashMap<Point, Integer>();


			for(int i = 0; i < rowIndices.length; i++)
			{
				//R indexing is 1-based
				freqMap.put(new Point(rowIndices[i] - 1 , colIndices[i] - 1), nonZeroVals[i]);
			}


			table.setModel(new AbstractTableModel()
			{
				public boolean isCellEditable(int rowIndex, int columnIndex)
				{
					return false;
				}

				public Object getValueAt(int rowIndex, int columnIndex)
				{
					if (columnIndex == 0)
					{
						return terms[rowIndex];
					}
					else if (columnIndex == 1)
					{
						return 101;
					}
					else
					{
						return freqMap.get(new Point(rowIndex, columnIndex - 1));
					}
				}

				public int getRowCount()
				{
					return terms.length;
				}

				public String getColumnName(int columnIndex)
				{
					// TODO Auto-generated method stub
					if (columnIndex == 0)
					{
						return "Terms";
					}
					else
					{
						//extra column for term strings:(-1) 
						return columnIndex + "";
						//"doc_" + columnIndex + ": " + 
						//docNames[columnIndex - 1];
					}
				}

				public int getColumnCount()
				{
					return docNames.length + 2;
				}
			});
			doTableSetup();
	}

	public void adjustForTableSize()
	{
		table.setPreferredScrollableViewportSize(new Dimension
				(
				Math.min(500, table.getPreferredSize().width),
				Math.min(400,table.getPreferredSize().height)
				)
				);
		this.pack();
		this.setMinimumSize(this.getSize());
	}

	public GridBagConstraints getTopLevelConstraints()
	{
		GridBagConstraints ret = new GridBagConstraints();
		//ret.insets = new Insets(10, 10, 10, 10);
		return ret;
	}

	public void setVisible(boolean show)
	{
		if (show)
		{
			adjustForTableSize();
		}
		super.setVisible(show);
	}

	public static void main(String[] args)
	{
		DocumentTermMatrixViewer f = new DocumentTermMatrixViewer(null);
		f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		f.setVisible(true);
		f.requestFocus();
	}
}

