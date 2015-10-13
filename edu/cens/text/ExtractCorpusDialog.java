/**
 * A dialog for extracting a corpus from an R data frame.
 * 
 * Consists of:
 * 
 * - A widget for selecting a dataframe, and a variable
 * - A text field for naming the new corpus
 * - an OK/Cancel/Help panel for executing the command
 * 
 */

package edu.cens.text;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.rosuda.deducer.Deducer;
import org.rosuda.deducer.toolkit.HelpButton;
import org.rosuda.deducer.toolkit.OkayCancelPanel;
import org.rosuda.deducer.toolkit.VariableSelector;
import org.rosuda.deducer.widgets.TextFieldWidget;



// TODO add help button

public class ExtractCorpusDialog extends JDialog implements ActionListener
{
	private VariableSelector variableSelector;
	//private SingletonDJList factor;
	//private SingletonAddRemoveButton addTextButton;
	private TextFieldWidget newCorpusNameField;
	private OkayCancelPanel okayCancelPanel;

	private boolean userHasNamed;
	private static final String HELP_URL = "index.php?n=Main.CreateACorpus";

	public ExtractCorpusDialog(JFrame parent)
	{
		super(parent, "Extract Corpus");

		///////////////////////////////////////
		////// ASSEMBLE THE GUI ///////////////
		///////////////////////////////////////
		if (parent != null) 
		{
			Dimension parentSize = parent.getSize(); 
			Point p = parent.getLocation(); 
			setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		GridBagConstraints c = new GridBagConstraints();

		////////////////////////////////////////
		////// The Variable selection panel ////
		////////////////////////////////////////
		JPanel messagePane = new JPanel(new GridBagLayout());
		int insetVal = 7;
		c.insets = new Insets(insetVal, insetVal, insetVal, insetVal);
		// ======= The variable selector list ==========
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.8;
		c.weighty = 1;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.LINE_START;
		//messagePane.add(new JButton("Button 1"), c);
		variableSelector = new VariableSelector();
		messagePane.add(variableSelector, c);
		variableSelector.setPreferredSize(new java.awt.Dimension(100, 100));
		c = new GridBagConstraints(); //reset;
		c.insets = new Insets(insetVal, insetVal, insetVal, insetVal);

		// ====== Preprocessing options =======================

//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.ipadx = 0;
//		c.ipady = 2;
//		c.gridx = 0;
//		c.gridy = 1;
//		c.weightx = .2;
//		c.weighty = 0;
//		c.anchor = GridBagConstraints.CENTER;
//		
//		///////////////////////////////////////////////////////
//		/////////// Build the table of actions ////////////////
//		///////////////////////////////////////////////////////
//		
//		int nActions = ProcessCmd.values().length;
//
//		PreprocessingTable _table = new PreprocessingTable(nActions);
//		
//		for (int i = 0; i < nActions; i++)
//		{
//			//Set the action's name
//			//new ProcessActionPanel(ProcessCmd.values()[i]);
//			//_table.setActionName(ProcessCmd.values()[i].getLabel(), i);
//			_table.setAction(ProcessCmd.values()[i], i);
//			
//			//Add any relevant options to the action
//			JPopupMenu ithMenu =  new JPopupMenu();
//			JMenuItem [] items = ProcessCmd.values()[i].getExtraOptions();
//			
//			for (JMenuItem item : items)
//			{
//				ithMenu.add(item);
//			}
//			_table.setHasOption(items.length > 0, i);
//			_table.setOptionsMenu(ithMenu, i);
//		}
//		
//		JPanel p = new JPanel();
//		p.setBackground(Color.WHITE);
//		p.setBorder(BorderFactory.createTitledBorder("Actions:"));
//		p.add(_table);
//		
//		messagePane.add(p, c); 
//		
		//messagePane.add(new javax.swing.JLabel("Prepro"), c);
		
		// ====== The new corpus name text field =======================

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = .2;
		c.weighty = 0;
		c.anchor = GridBagConstraints.CENTER;
		newCorpusNameField = new TextFieldWidget("Save Corpus As:");
		//newCorpusNameField.setSize(new Dimension(100,25));
		newCorpusNameField.setPreferredSize(new Dimension(100,50));
		messagePane.add(newCorpusNameField, c);

		c = new GridBagConstraints(); //reset;
		c.insets = new Insets(insetVal, insetVal, insetVal, insetVal);

		getContentPane().add(messagePane,BorderLayout.CENTER);

		//======  The OK/Cance/help button panel =======
		JPanel footerPanel = new JPanel(new GridBagLayout());
		okayCancelPanel = new OkayCancelPanel(false,true,this);
		okayCancelPanel.setPreferredSize(new Dimension(250,25));
		okayCancelPanel.getApproveButton().setText("Save");
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		//messagePane.add(okayCancelPanel, c);
		//messagePane.add(new JButton("VERY BOTTOM"), c);

		
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;
		footerPanel.add(new HelpButton(HELP_URL), c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx = 0;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;

		footerPanel.add(okayCancelPanel, c);
		c = new GridBagConstraints(); //reset;
		c.insets = new Insets(insetVal, insetVal, insetVal, insetVal);

		getContentPane().add(footerPanel, BorderLayout.SOUTH);

		c = new GridBagConstraints(); //reset;
		c.insets = new Insets(insetVal, insetVal, insetVal, insetVal);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		
		
		////////////////////////////////////////
		///// Register action listeners ////////
		////////////////////////////////////////
//		addTextButton.addActionListener(new ActionListener() 
//		{	
//			public void actionPerformed(ActionEvent e) 
//			{
//				if (variableSelector.getJList().getSelectedValue() != null && factor.getModel().getSize() == 0)
//				//(factor.getModel().getSize() > 0)
//				{
//					//newCorpusNameField.getTextField().setText("something there: " + factor.getModel().getElementAt(0));
//					newCorpusNameField.getTextField().setText(variableSelector.getJList().getSelectedValue()+".corpus");
//				}
//			}
//		});
		
		variableSelector.getJList().addListSelectionListener(new ListSelectionListener()
		{
			
			
			public void valueChanged(ListSelectionEvent arg0)
			{
				if (variableSelector.getJList().getSelectedValue() != null)
				{
					newCorpusNameField.getTextField().setText(variableSelector.getJList().getSelectedValue()+".corpus");
				}
			}
		});
				
		ActionListener runAction = new ActionListener()
		{
			
			
			public void actionPerformed(ActionEvent e)
			{
				String newCorpusName = getNewCorpusName();
				String dataFrame = getSelectedDataFrame();
				String variable = getSelectedVariable();
				String cmd = newCorpusName + "<- Corpus(VectorSource("+dataFrame+"$"+variable+"))";
				//System.out.println(cmd);
				
				//Possible problems:
				// - No variable selected
				// - Corpus name is empty
				// - Corpus name already defined
				boolean validCommand = true;
				if (variable == null)
				{
					validCommand = false;
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(getContentPane(),
						    "You must select a text variable before you can extract a corpus.",
						    "Alert",
						    JOptionPane.ERROR_MESSAGE);
				}
				else if (newCorpusName == null)
				{
					validCommand = false;
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(getContentPane(),
						    "You must give the corpus a name.",
						    "Alert",
						    JOptionPane.ERROR_MESSAGE);
				}
				else if (! newCorpusName.equals(Deducer.getUniqueName(newCorpusName))) //not unique
				{
					
					int n = JOptionPane.showConfirmDialog(
						    getContentPane(),
						    "The corpus name \"" + newCorpusName + "\" is already in use."
						    +"\nWould you like to overwrite the existing variable?",
						    "Warning",
						    JOptionPane.YES_NO_OPTION);
					validCommand = n == 0;

				}
				
				if (validCommand)
				{
					dispose();
					Deducer.execute(cmd);
				}
			}
		};	
		
		ActionListener cancelAction = new ActionListener() 
		{

			
			public void actionPerformed(ActionEvent arg0)
			{
				dispose();
			}
		
		};
		
		okayCancelPanel.getApproveButton().addActionListener(runAction);
		okayCancelPanel.getCancelButton().addActionListener(cancelAction);
		
		//factor.setMo
		//((DefaultListModel) factor.getModel()).add(0, "1thing");
		setMinimumSize(new Dimension(300, 350));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
	}

	public String getNewCorpusName()
	{
		String proposedName = newCorpusNameField.getTextField().getText();
		if (!proposedName.equals(""))
		{
			return proposedName;
		}
		else
		{
			return null;
		}
	}
	
	public String getSelectedVariable()
	{
		return (String) variableSelector.getJList().getSelectedValue();//this.variableSelector.getSelectedData();
//		if (factor.getModel().getSize() > 0)
//		{
//			return factor.getModel().getElementAt(0).toString();
//		}
//		else
//		{
//			return null;
//		}
		//variableSelector.getJList().getSelectedValue().toString();
	}
	
	public String getSelectedDataFrame()
	{
		// TODO might crash if no data frames are present
		if (variableSelector.getSelectedData() != null)
		{
			return variableSelector.getSelectedData().toString();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Test the dialog
	 * @param args unused
	 */
	public static void main(String[] args) 
	{

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ExtractCorpusDialog dlg = new ExtractCorpusDialog(f);
		f.setVisible(true);
		dlg.addWindowListener(new WindowAdapter()
		{
			public void windowClosed(WindowEvent e)
			{
				System.exit(0);
			}

		});
		
		dlg.setVisible(true);
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public void setVisible(boolean show)
	{
		if (show)
		{
			this.setLocationRelativeTo(this.getParent());
		}
		super.setVisible(show);
	}
	
	public void actionPerformed(ActionEvent arg0) 
	{
		// TODO Auto-generated method stub

	}
}
