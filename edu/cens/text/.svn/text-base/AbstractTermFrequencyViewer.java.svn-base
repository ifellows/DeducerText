package edu.cens.text;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public abstract class AbstractTermFrequencyViewer
{
	JComboBox sortMethodSelector;
	private boolean decreasing;
	protected TermFrequencyDialog tfDialog;
	
	public abstract JPanel getOptionsPanel();
	public abstract String toString();
	public abstract void executeView();
	
	public AbstractTermFrequencyViewer(TermFrequencyDialog tfDialog)
	{
		this.tfDialog = tfDialog;
		decreasing = true;
	}
	
	public void onCorpusChange(String corpus)
	{
	}
	
	public String getExtraTermFreqArgs()
	{
		return "sorted=\"" + this.getSortMethod() + "\", " +
		"decreasing=" + ("" + this.getAsc()).toUpperCase();
	}
	
	protected GridBagConstraints getTopLevelLayoutDefaults()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(3, 3, 3, 3);
		return c;
	}
	
	public String getSortMethod()
	{
		// "alpha" : "freq";
		if (sortMethodSelector == null)
		{
			return "freq";
		}
		else if (sortMethodSelector.getSelectedItem().equals("by frequency"))
		{
			return "freq";
		}
		else if (sortMethodSelector.getSelectedItem().equals("alphanumerically"))
		{
			return "alpha";
		}
		else
		{
			throw new IllegalStateException(
					"Unrecognized value '"+ sortMethodSelector.getSelectedItem() +"'  from combo box!");
		}
		// return ((SortOptions) _sortedCMB.getSelectedItem()).getType();
	}
	
	public boolean getAsc()
	{
		return decreasing;
		// return ((SortOptions) _sortedCMB.getSelectedItem()).getSortAsc();
	}
	
	protected JPanel constructSortPanel()
	{
		GridBagConstraints c = getTopLevelLayoutDefaults();
		JPanel sortPanel = new JPanel(new GridBagLayout());

		//sortPanel.setBorder(BorderFactory.createTitledBorder("Sort: "));

		JRadioButton descendingButton = new JRadioButton("descending");
		JRadioButton ascendingButton = new JRadioButton("ascending");

		ButtonGroup sortGroup = new ButtonGroup();
		sortGroup.add(descendingButton);
		sortGroup.add(ascendingButton);

		ascendingButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				decreasing = false;
			}
		});

		descendingButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				decreasing = true;
			}
		});

		descendingButton.setSelected(true);

		c.anchor =c.EAST;
		c.weighty = 0;

		sortPanel.add(new JLabel("Sort:"),c);

		sortMethodSelector = new JComboBox();
		sortMethodSelector.addItem("by frequency");
		sortMethodSelector.addItem("alphanumerically");
		sortMethodSelector.setSelectedIndex(0);
		
		// add combo box
		c.weighty = 0;
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor =c.WEST;
		sortPanel.add(sortMethodSelector, c);

		JPanel radButtonPanel = new JPanel(new GridBagLayout());

		// add descending radio button
		c.anchor = GridBagConstraints.LINE_START;
		// c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		//sortPanel.add(descendingButton, c);

		// add ascending radio button
		c.gridx = 1;
		c.gridy = 2;
		//sortPanel.add(ascendingButton, c);

		//Put radio buttons on separate panel
		c = getTopLevelLayoutDefaults();
		radButtonPanel.add(descendingButton, c);
		c.gridx = 1;
		radButtonPanel.add(ascendingButton, c);

		c = getTopLevelLayoutDefaults();
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 1;
		c.fill = c.HORIZONTAL;
		sortPanel.add(radButtonPanel, c);

		return sortPanel;
	}
}
