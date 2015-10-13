package edu.cens.text;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.rosuda.deducer.Deducer;

public class WordCloudViewer extends AbstractTermFrequencyViewer
{
	
	public WordCloudViewer(TermFrequencyDialog tfDialog)
	{
		super(tfDialog);
	}

	// Word cloud settings ============================
	//This is a sign that word cloud should somehow have its own class
	//A little abstract class that has getters for the important options,
	//and an abstract method for generating an options panel.  Something similar
	//for the other view methods would be sensible as well.
	double WC_rotatePercentage;
	double WC_minFontSize;
	double WC_maxFontSize;
	String WC_colors;

	public JPanel getOptionsPanel()
	{
		Double [] defaultFontSizes = {.25, 1.0, 2.0, 4.0, 8.0};

		final JComboBox minFontSizeComboBox = new JComboBox(defaultFontSizes);
		final JComboBox maxFontSizeComboBox = new JComboBox(defaultFontSizes);
		
		InputVerifier positiveDoubleVerifier = new InputVerifier()
		{
			public boolean verify(JComponent input)
			{
				final JTextComponent source = (JTextComponent) input;
        String text = source.getText();
        
    		try
    		{
    			Double dub = Double.parseDouble(text);
    			return dub >= 0;
    		}
    		catch (NumberFormatException e)
    		{
    			return false;
    		}
        
			}
		};
		
		((JTextField)(minFontSizeComboBox.getEditor().getEditorComponent())).
		setInputVerifier(positiveDoubleVerifier); 
		
		((JTextField)(maxFontSizeComboBox.getEditor().getEditorComponent())).
		setInputVerifier(positiveDoubleVerifier); 

		minFontSizeComboBox.setEditable(true);
		minFontSizeComboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				WC_minFontSize = (Double) minFontSizeComboBox.getSelectedItem();//Double.parseDouble((String) minFontSizeComboBox.getSelectedItem());
				maxFontSizeComboBox.setSelectedItem(Math.max(WC_minFontSize, WC_maxFontSize));
			}
		});


		maxFontSizeComboBox.setEditable(true);
		maxFontSizeComboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				WC_maxFontSize = (Double) maxFontSizeComboBox.getSelectedItem();//Double.parseDouble((String) maxFontSizeComboBox.getSelectedItem());
				minFontSizeComboBox.setSelectedItem(Math.min(WC_minFontSize, WC_maxFontSize));
			}
		});
		minFontSizeComboBox.setSelectedItem(.25);
		maxFontSizeComboBox.setSelectedItem(4.0);
		
		String[] colorings = 
				{
				"Black", 
				"Black/White",
				"Spectral", 
				"Dark2",
				"Purple/Green",
				"Red/Cyan",
				"Blue/Gold"
				};
		final JComboBox coloringComboBox = new JComboBox(colorings);
		//Use these: 
		//http://www.oga-lab.net/RGM2/func.php?rd_id=RColorBrewer:ColorBrewer
		coloringComboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String colorItem = (String) coloringComboBox.getSelectedItem();
				WC_colors = "'black'";
				
				if (colorItem.equals("Black"))
				{
					WC_colors = "'black'";
				}
				else if (colorItem.equals("Black/White"))
				{
					WC_colors = "make.color.scale(c(.95,.95,.95),c(0,0,0),256,.4)";
				}
				else if (colorItem.equals("Spectral"))
				{
					WC_colors = "brewer.pal(11,'Spectral')";
				}
				else if (colorItem.equals("Dark2"))
				{
					WC_colors = "brewer.pal(8,'Dark2')";
				}
				else if (colorItem.equals("Purple/Green"))
				{
					WC_colors = "brewer.pal(11,'PRGn')";
				}
				else if (colorItem.equals("Red/Cyan"))
				{
					WC_colors  = "make.color.scale(c(0,1,1), c(1,0,0),256,.25)";
				}
				else if (colorItem.equals("Blue/Gold"))
				{
					WC_colors = "make.color.scale(c(1,1,0), c(0,0,.85),256,.5)";
				}
				else
				{
					throw new IllegalStateException("unrecognized color! '"+ colorItem +"' from combo box!");
				}
			}
		});
		coloringComboBox.setSelectedIndex(0);

		final JCheckBox rotateTermsCheckBox = new JCheckBox("Randomly Rotate Terms");
		rotateTermsCheckBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					WC_rotatePercentage = .25;
				} 
				else
				{
					WC_rotatePercentage = 0;
				}
			}
		});
		rotateTermsCheckBox.setSelected(true);

		JPanel retPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = getTopLevelLayoutDefaults();	

		c.ipadx = 0;
		c.ipady = 0;

		c.gridx = 0;
		c.anchor = c.EAST;
		c.fill = c.NONE;
		c.weightx = 0;
		retPanel.add(new JLabel("Min Font Size:"),c);


		c.gridx = 1;
		c.anchor = c.WEST;
		c.fill = c.NONE;
		c.weightx = 1;
		retPanel.add(minFontSizeComboBox,c);

		c.gridx = 2;
		c.gridy = 0;
		c.anchor = c.EAST;
		c.fill = c.NONE;
		c.weightx = 0;
		retPanel.add(new JLabel("Max Font Size:"),c);

		c.gridx = 3;
		c.gridy = 0;
		c.anchor = c.WEST;
		c.fill = c.NONE;
		c.weightx = 1;
		retPanel.add(maxFontSizeComboBox,c);

		c.gridx = 0;
		c.gridy = 1;
		c.anchor = c.EAST;
		c.fill = c.NONE;
		c.weightx = 0;
		c.gridwidth =1;
		retPanel.add(new JLabel("Coloring:"),c);

		c.gridx = 1;
		c.gridy = 1;
		c.anchor = c.WEST;
		c.fill = c.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth =3;
		retPanel.add(coloringComboBox,c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 3;
		c.anchor = c.WEST;
		c.fill = c.NONE;
		retPanel.add(rotateTermsCheckBox,c);
		
		minFontSizeComboBox.setPreferredSize(new Dimension(60, minFontSizeComboBox.getPreferredSize().height));
		maxFontSizeComboBox.setPreferredSize(new Dimension(60, maxFontSizeComboBox.getPreferredSize().height));

		
		return retPanel;
	}

	public String toString()
	{
		return "Word Cloud";
	}
	
	public void executeView()
	{
		String cmd = "";
		String tempFreq = Deducer.getUniqueName("tempFreq");
		
		cmd += (tempFreq + "<-" + tfDialog.getTermFreqCall(this.getExtraTermFreqArgs()));
		
		cmd+=("\nwordcloud(" +
				"names("+ tempFreq + "), " + 
				tempFreq +
				", min.freq=0 " +
				
				//given its penchant for not being able to display every word, 
				//it's probably best to display the most frequent words first.
				", random.order=FALSE" + 
				", scale = c(" + WC_maxFontSize + ", " + WC_minFontSize + ")" +
				", colors=" + WC_colors +
				", rot.per=" + WC_rotatePercentage +")");
		
		cmd+=("\nrm(" + tempFreq + ")");
		Deducer.execute(cmd);
		Deducer.execute("dev.set()", false); //give the plot focus
	}

	public static void main (String[] args)
	{
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new WordCloudViewer(null).getOptionsPanel());
		f.pack();
		f.setVisible(true);
	}
	
}
