package edu.cens.text;

import org.rosuda.JGR.JGR;
import org.rosuda.deducer.Deducer;
import org.rosuda.deducer.toolkit.HelpButton;
import org.rosuda.deducer.widgets.ObjectChooserWidget;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

/**
 * PreprocessingDialog is a dialog for creating a processed version of an
 * exisitng text-corpus.
 * 
 * It consists of:
 * 
 * - A combo box for selecting the corpus to be processed
 * 
 * - A PreprocessingTable for the configuring the preprocessing actions to be applied
 * More information can be found in PreprocessingTable.java
 * 
 * - A text field for naming the new, processed corpus
 * 
 * - OK/Cancel/Help buttons
 * The help button provides no useful help at the moment.
 * 
 */

public class PreprocessingDialog extends JDialog
{

	PreprocessingTable _table;
	
	//NOTE: infuriating quirk of HelpButton, is that in the constructor, it puts in a base URL,
	//but in the setUrl function it does not.  And so... sometimes you need to specify the whole
	//URL, and sometimes not.  If using the DeducerOkCancelPanel, you must specify the whole URL
	private static final String HELP_URL = "http://www.deducer.org/pmwiki/index.php?n=Main.PreprocessCorpus";
	
	protected ObjectChooserWidget _source = new ObjectChooserWidget("Source Corpus:", this)
	{
		{
			setClassFilter("Corpus");
			refreshObjects();
		}
	};

	// private JLabel _source = new JLabel("Source");

	private JTextField saveAsNameField = new JTextField(15);

	private DefaultListModel _model = new DefaultListModel();
	{
		int n = ProcessCmd.values().length;
		for (int i = 0; i < n; i++)
		{
			_model.addElement(
					ProcessCmd.values()[i]
					//new ProcessActionPanel(ProcessCmd.values()[i])
					);
		}
	}


	public PreprocessingDialog(JFrame parent)
	{
		super(parent);
		setTitle("Preprocess Corpus...");
		
		_source.getComboBox().addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if (saveAsNameField != null) 
				{
					String uniqueName = Deducer.getUniqueName(_source.getComboBox().getSelectedItem() + ".processed");
					saveAsNameField.setText(uniqueName);
				}
			}
		});
		if (_source.getComboBox().getModel().getSize() > 0)
		{
			_source.getComboBox().setSelectedIndex(0);
		}
		//getComboBox().addItem("Porcus");
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 10, 10);
		this.setLayout(new GridBagLayout());
		add(new JPanel()
		{
			{

				setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.insets = new Insets(5, 5, 5, 5);
				c.gridy = 0;
				c.gridwidth = 2;

				add(_source, c);

				c.gridy++;

				
				///////////////////////////////////////////////////////
				/////////// Build the table of actions ////////////////
				///////////////////////////////////////////////////////
				
				int nActions = ProcessCmd.values().length;
		
				_table = new PreprocessingTable(nActions);
				
				for (int i = 0; i < nActions; i++)
				{
					//Set the action's name
					//_table.setActionName(ProcessCmd.values()[i].getLabel(), i);
					_table.setAction(ProcessCmd.values()[i], i
							//new ProcessActionPanel(ProcessCmd.values()[i]), i
							);
					
					//Add any relevant options to the action
					JPopupMenu ithMenu =  new JPopupMenu();
					JMenuItem [] items = ProcessCmd.values()[i].getExtraOptions();
					ProcessCmd.values()[i].setParentComponent(_table);
					
					for (JMenuItem item : items)
					{
						ithMenu.add(item);
					}
					_table.setHasOption(items.length > 0, i);
					_table.setOptionsMenu(ithMenu, i);
				}
				_table.setEnabled(4, false);
				_table.setEnabled(5, false);
				
				JPanel p = new JPanel();
				p.setBorder(BorderFactory.createTitledBorder("Actions:"));
				p.add(_table);
				
				add(p, c); //this is the new prepocessing table
				//add(_list, c); // this is the old one

				c.weighty = 1;
				c.weightx = 1;
				c.gridy++;
				c.gridwidth = 2;
				c.fill = GridBagConstraints.BOTH;
				add(new JLabel(), c); // spacing blah
				
				c.gridy++;
				c.gridx = 0;
				c.gridwidth = 1;
				c.weighty = 0;
				c.weightx = 0;
				c.insets = new Insets(0, 15, 0, 0);
				c.ipadx = 10;
				
				add(new JLabel("Save Corpus As:"), c);
				
				c.gridx = 1;
				c.gridwidth = 1;
				c.weighty = 0;
				c.weightx = 1;
				c.insets = new Insets(0, 0, 0, 0);
				
				add(saveAsNameField, c);

				c.gridx = 0;
				c.gridy++;
				c.gridwidth = 2;
				c.weightx = 1;
				c.weighty = 0;
				//c.anchor = GridBagConstraints.SOUTH;
				c.fill = GridBagConstraints.HORIZONTAL;
				//c.insets = new Insets(10, 10, 10, 10);
				add(new DeducerOkCancelPanel(this.getRootPane(), "Save as:", "Cancel", HELP_URL)
				{
					
					protected void ok()
					{
						doOK();
					}
					
					
					protected void cancel()
					{
						doCancel();
					}
				}, c);
			}
		}, c);

		pack();
		setResizable(false);
		setMinimumSize(this.getSize());

	}

	public boolean doCancel()
	{
		dispose();
		return true;
	}

	public boolean doOK()
	{
		if (_source.getComboBox().getSelectedItem() == null)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(getContentPane(),
				    "You do not have any corpuses to preprocess.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;	
		}
		else if (saveAsNameField.getText() == null || saveAsNameField.getText().equals(""))
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(getContentPane(),
				    "No name given for the processed corpus.\n" +
				    "Please choose a valid name.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;	
		}

		
		String s = _source.getComboBox().getSelectedItem().toString();
		//getModel().toString();
		
		
		String t = //_source.getModel().toString(); //Just overwrite the unprocessed  corpus
		saveAsNameField.getText(); //use the new name
	
		int nEnabled = 0;
		String cmd = "";
		for (int i = 0; i < ProcessCmd.values().length ; i++)
		{
			if (_table.isEnabled(i))
			{
				nEnabled ++;
				ProcessCmd command = (ProcessCmd) _table.getAction(i);
				cmd +=(t + "<- " + command.getRCmd(s) + "\n");
				//System.out.println(t + "<- " + p._command.getRCmd(s) + ";\n");
				s = t;
			}
			
		}
		Deducer.execute(cmd);
	
		if (nEnabled == 0) //
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(getContentPane(),
				    "No preprocessing actions were selected!" +
				    "\nYou can enable an action by checking the box next to its name.",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
			return false;
		}
		else
		{
			//dispose(); //TODO restore this line
			setVisible(false);
			return true;
		}
	}

	private boolean debugForceShow = false;
	
	
	public void setVisible(boolean visible)
	{
		
		if (visible)
		{
			this.setLocationRelativeTo(this.getParent());
		}
		
		
		if (visible == true && _source.getModel() == null && !debugForceShow)
		{
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(getContentPane(),
			    "You have not yet created any corpuses."
				+
				"\nCreate a corpus with \"Extract Corpus\" in the Text menu.",
			    "Warning",
			    JOptionPane.WARNING_MESSAGE);
		}

		super.setVisible(visible);
	}
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PreprocessingDialog dlg = new PreprocessingDialog(null);
		f.setVisible(true);
		dlg.addWindowListener(new WindowAdapter()
		{
			public void windowClosed(WindowEvent e)
			{
				System.exit(0);
			}

		});
		dlg.debugForceShow = true;
		dlg.setVisible(true);
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

}
