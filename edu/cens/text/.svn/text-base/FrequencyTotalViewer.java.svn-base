package edu.cens.text;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.rosuda.deducer.Deducer;

public class FrequencyTotalViewer extends AbstractTermFrequencyViewer
{
	JTextField saveTotalsField;
	
	public FrequencyTotalViewer(TermFrequencyDialog tfDialog)
	{
		super(tfDialog);
		saveTotalsField = new JTextField();
	}
	
	public JPanel getOptionsPanel()
	{
		JPanel retPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = getTopLevelLayoutDefaults();
		c.anchor = c.CENTER;
		c.gridwidth = 2;
		c.weighty = 0;
		c.insets = new Insets(0, 5, 5, 5);
		retPanel.add(constructSortPanel(), c);

		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = c.HORIZONTAL;
		c.gridwidth = 2;
		retPanel.add(new JSeparator(JSeparator.HORIZONTAL), c);

		c.weightx = 0;
		c.weighty = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		retPanel.add(new JLabel("Save Frequencies as Variable:"), c);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 3;
		retPanel.add(saveTotalsField,c);

		c.gridy = 3;
		c.gridx = 1;
		c.weightx = 0;
		c.fill = c.NONE;
		c.anchor = c.NORTHEAST;

		JButton saveTotalsButton = new JButton("Save");
		retPanel.add(saveTotalsButton, c);

		saveTotalsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveTotalsAsDataFrame(saveTotalsField.getText());

			}
		});
		
		return retPanel;
	}

	protected void saveTotalsAsDataFrame(String saveName)
	{
		// TODO Implement some basic redundancy avoidance.
		String cmd = "";
		//Save the frequency totals to a temporary variable
		String tempTotals = Deducer.getUniqueName("freqTotals");
		//Deducer.execute(tempTotals + " <- " + tfDialog.getTermFreqCall(this.getExtraTermFreqArgs()));
		cmd += tempTotals + " <- " + tfDialog.getTermFreqCall(this.getExtraTermFreqArgs())+ "\n";
		String totalType = this.tfDialog.getUseDocumentFrequency() ? "doc_freq" : "term_freq";
		
		//Save the totals 
		//Deducer.execute(saveName + " <- d(term=names(" + tempTotals + "), " + totalType + "=" + tempTotals +")");
		cmd +=saveName + " <- d(term=names(" + tempTotals + "), " + totalType + "=" + tempTotals +")\n";
		//delete the temporary variable
		//Deducer.execute("rm(" + tempTotals + ")");
		cmd +="rm(" + tempTotals + ")";
		Deducer.execute(cmd);

	}
	
	public void executeView()
	{
		Deducer.execute("print(" + tfDialog.getTermFreqCall(this.getExtraTermFreqArgs()) + ");");
	}

	public String toString()
	{
		return "Frequency Totals List";
	}
	
	public void onCorpusChange(String selectedCorpus)
	{
		if (selectedCorpus != null)
		{
			saveTotalsField.setText(selectedCorpus + ".term_freq");
		}
	}

}
