/**
 * A dialog for importing text files into a corpus, new or existing.
 * 
 * It consists of 
 * - A file browser
 * 	The user can select a document, or many documents
 * 
 * - A file filter combo box
 * 
 * - Radio buttons for making a new corpus, and appending to an existing corpus
 * 
 * - A text field for naming the new corpus
 * Visible only if the 'make new corpus' radio button is selected
 * 
 * - A combo box for selecting and existing corpus, to which the selected documents are appended
 * Visible only if the 'add to existing corpus' radio button is selected
 * 
 * - OK/Cancel buttons
 * 
 * TODO Add 'Help' button
 * TODO Add support for more file formats
 *  
 */
package edu.cens.text;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.rosuda.JGR.JGR;
import org.rosuda.deducer.Deducer;
import org.rosuda.deducer.toolkit.HelpButton;


public class TextFileChooser
{

	private static final String HELP_URL = "index.php?n=Main.TextImportFromFile";
	
	private JFileChooser fc;
	JDialog actualDialog;

	boolean useExistingCorpus = false;

	//String newCorpusName = null;
	final JTextField newNameField;
	String existingCorpus = null;

	private static String mostRecentPath = null;
	public TextFileChooser(JFrame parent)
	{
		newNameField = new JTextField();
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setApproveButtonText("Import");
		fc.setMultiSelectionEnabled(true); 

		//// Register Action to set corpus name on file change //////////////
		fc.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent e)
			{
				//If a file became selected, find out which one.
				if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(e.getPropertyName())) 
				{
					String fileName;
					if(e.getNewValue() instanceof File){
						File f = (File) e.getNewValue();
						fileName = f.getName();
					}else{
						return;
					}

					//remove extension
					//if(!f.isDirectory() && fileName.indexOf(".")>1)
					//	fileName = fileName.substring(0, fileName.lastIndexOf("."));

					newNameField.setText(Deducer.getUniqueName(fileName));
				}


			}
		});

		//// Actions for approve, or reject /////////////////////////////////
		//TODO add dialog for potential variable overwrite
		fc.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String command = e.getActionCommand();
				if (command.equals(JFileChooser.APPROVE_SELECTION)) 
				{
					//initialize an almost empty corpus
					//Deducer.execute(corpName + " <- Corpus(VectorSource(c('')))");
					//Build the corpus

					File[] pickedFiles = fc.getSelectedFiles();
					ArrayList<File> files = new ArrayList<File>();
					for(int i=0;i<pickedFiles.length;i++){
						if(pickedFiles[i].isDirectory()){
							File[] subFiles = pickedFiles[i].listFiles();
							if(subFiles!=null)
								for(int j=0;j<subFiles.length;j++)
									if(!subFiles[j].isDirectory() && !subFiles[j].getName().startsWith("."))
										files.add(subFiles[j]);
						}else
							files.add(pickedFiles[i]);
					}
					//System.out.println(pickedFiles.length + " " + fc.isMultiSelectionEnabled());

					String corpusCall = "Corpus(VectorSource(c(\n";

					String enclosingDir = fc.getCurrentDirectory().getAbsolutePath();//pickedFiles[0].getParent();
					mostRecentPath = enclosingDir;

					for (int i = 0; i < files.size(); i++)
					{
						String fileName = Deducer.addSlashes( files.get(i).getAbsolutePath() );

						corpusCall = corpusCall + "paste(readLines('" + fileName + "', warn=F), sep='', collapse='\\n')";
						if (i != files.size() - 1)
						{
							corpusCall = corpusCall + ",";
						}
						corpusCall = corpusCall + "\n";

					}

					corpusCall = corpusCall + ")))";

					//System.out.println(patternString);

					//////////////////////////////////////////////////////
					// Make new corpus
					//////////////////////////////////////////////////////
					if (! useExistingCorpus)
					{
						String newCorpusName = newNameField.getText();

						boolean isUnique = newCorpusName.equals(Deducer.getUniqueName(newCorpusName));

						boolean validCommand = true;

						if (newCorpusName == null || newCorpusName.equals("") || Character.isDigit(newCorpusName.charAt(0)))
						{
							validCommand = false;
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null,
									"You must give the corpus a name.",
									"Alert",
									JOptionPane.ERROR_MESSAGE);
						}
						else if (!isUnique) //not unique
						{

							int n = JOptionPane.showConfirmDialog(
									null,
									"The corpus name \"" + newCorpusName + "\" is already in use."
									+"\nWould you like to overwrite the existing variable?",
									"Warning",
									JOptionPane.YES_NO_OPTION);
							validCommand = n == 0;

						}

						if (validCommand)
						{
							//Windows uses back slashes for file paths.
							//R can handle it, but only if they're double backslashes.
							//Fixed by Deducer.addSlashes()?
							//							 if (File.separatorChar == '\\')
							//							 {
							//								 enclosingDir = enclosingDir.replace(""+File.separatorChar, "\\\\");
							//							 }

							Deducer.execute(newCorpusName + " <- " + corpusCall);

							actualDialog.dispose();

							//!!! How to import a single document as a corpus !!!
							//TODO use this instead of the file matching method currently employed
							//corp <- Corpus(VectorSource(paste(readLines("/Users/xander/Desktop/btchn10.txt"), sept="",collapse="\n")))
						}


					}
					else //>>>>>>>>>>>>>>>>>>>>>>>> use existing corpus >>>>>>>>>>>>
					{
						//The corpus to which we are adding the new documents
						String corpusAddee = existingCorpus; 
						String tempCorp = Deducer.getUniqueName("tempCorp");




						Deducer.execute
						//System.out.println
						(corpusAddee + "<- c(" + corpusAddee + ",\n" + corpusCall +"\n)");


						actualDialog.dispose();

					} //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
				} 
				else if (command.equals(JFileChooser.CANCEL_SELECTION)) 
				{
					actualDialog.dispose();
				}

			}
		});

		actualDialog = new JDialog(parent)
		{
			public void setVisible(boolean visible)
			{
				if (visible)
				{
					this.setLocationRelativeTo(this.getParent());
				}
				super.setVisible(visible);
			}
		};

		actualDialog.setLayout(new BorderLayout());

		actualDialog.add(fc, BorderLayout.CENTER);
		actualDialog.add(constructExtraOptionsPanel(), BorderLayout.SOUTH);

//		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		fc.addChoosableFileFilter(new FileFilter()
		{
			public String getDescription()
			{
				return "Text files (*.txt *.rtf)";
			}

			public boolean accept(File f)
			{
				String fname  = f.getName().toLowerCase();
				return fname.endsWith(".txt") || fname.endsWith(".rtf") || f.isDirectory();
			}
		});
		

		//		addChoosableFileFilter(new FileFilter()
		//		{
		//			public String getDescription()
		//			{
		//				return "CSV files";
		//			}
		//			
		//			public boolean accept(File f)
		//			{
		//				return f.getName().toLowerCase().endsWith(".csv");
		//			}
		//		});

	}


	public JPanel constructExtraOptionsPanel()
	{
		//////////////////////////////////////////////////
		// Construct the buttons /////////////////////////
		//////////////////////////////////////////////////

		final JRadioButton makeNewCorpusButton = new JRadioButton("Make New Corpus");
		JRadioButton addToCorpusButton = new JRadioButton("Add to Existing Corpus");

		final RObjectChooser corpusSelector = new RObjectChooser();

		ButtonGroup group = new ButtonGroup();
		group.add(makeNewCorpusButton);
		group.add(addToCorpusButton);

		final JPanel nameOrCorpusPanel = new JPanel(new CardLayout());
		final String makeNewCorpus = "make new corpus";
		final String chooseExistingCorpus = "choose existing corpus";

		makeNewCorpusButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				CardLayout cl = (CardLayout)(nameOrCorpusPanel.getLayout());
				cl.show(nameOrCorpusPanel, makeNewCorpus);
				useExistingCorpus = false;
			}
		});

		addToCorpusButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (corpusSelector.getItemCount() == 0)
				{
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null,
							"You do not yet have any corpuses.",
							"Alert",
							JOptionPane.ERROR_MESSAGE);
					makeNewCorpusButton.setSelected(true);
				}
				else
				{

					CardLayout cl = (CardLayout)(nameOrCorpusPanel.getLayout());
					cl.show(nameOrCorpusPanel, chooseExistingCorpus);

					useExistingCorpus = true;
				}
			}
		});

		////////////////////////////////////////////
		//  The extra options panel  ///////////////
		////////////////////////////////////////////

		JPanel retPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		////////////////////////////////////////////
		//  The radio button section  //////////////
		////////////////////////////////////////////

		JPanel radButPan = new JPanel();

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;

		radButPan.add(makeNewCorpusButton, c);

		c.gridx = 2;

		radButPan.add(addToCorpusButton, c);

		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		//JPanel coloredStuff = new JPanel();
		//coloredStuff.setBackground(Color.blue);
		//System.out.println(helpButton.getSize());
		c.ipadx = 25;
		c.ipady = 15;
		retPanel.add(new HelpButton(HELP_URL), c);
		
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		//radButPan.setBackground(Color.RED);
		retPanel.add(radButPan, c);

		////////////////////////////////////////////////////////
		//  The new-name/existing-corpus section  //////////////
		////////////////////////////////////////////////////////

		//-- New name panel --------------------------------------
		JPanel newNamePanel = new JPanel(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		newNamePanel.add(new JLabel("Save as: "), c);

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		newNameField.setText(Deducer.getUniqueName("untitled.corpus"));


		newNamePanel.add(newNameField, c);

		//-- Existing corpus panel --------------------------------------
		JPanel existingCorpusPanel = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();

		c.gridx = 0;
		c.weightx = 0;
		existingCorpusPanel.add(new JLabel("Add to: "),c);

		c.gridx = 1;
		c.weightx = 1;
		c.fill = c.HORIZONTAL;

		//JFrame f = new JFrame(); f.setVisible(true);

		//Important to add action listener *before* calling setSelected.
		corpusSelector.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				existingCorpus = (String) corpusSelector.getSelectedObject();
			}
		});
		
		corpusSelector.setClassFilter("Corpus");
		corpusSelector.refreshObjects();
		if (corpusSelector.getObjectCount() > 0)
		{
			corpusSelector.setSelectedIndex(0);
		}


		existingCorpusPanel.add(corpusSelector,c);

		//-- Panel that switches between new-name and existing-corpus panels -------

		nameOrCorpusPanel.add(newNamePanel, makeNewCorpus);
		nameOrCorpusPanel.add(existingCorpusPanel, chooseExistingCorpus);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 1;
		c.fill = c.HORIZONTAL;

		c.insets = new Insets(5, 15, 5, 15);

		//retPanel.setBackground(Color.green);
		retPanel.add(nameOrCorpusPanel, c);

		////////////////////////////////////////////
		//  A separator  ///////////////////////////
		////////////////////////////////////////////

		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(0, 5, 0, 5);

		//retPanel.add(new JSeparator(SwingConstants.HORIZONTAL),c);

		/////////////////////////////////////////////
		//  Default to making new corpus  ///////////
		/////////////////////////////////////////////
		makeNewCorpusButton.setSelected(true);

		return retPanel;
	}

	public void run()
	{	
		if (mostRecentPath != null)
		{
			fc.setCurrentDirectory(new File(mostRecentPath));
		}

		actualDialog.pack();
		actualDialog.setVisible(true);
	}

	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		f.setVisible(true);
		TextFileChooser tfc = new TextFileChooser(f)
		{

		};
		tfc.run();
	}
}
