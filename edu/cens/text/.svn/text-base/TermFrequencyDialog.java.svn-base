/**
 * Dialog for viewing and manipulating term frequency in a variety of ways. 
 * You can currently view term frequencies as:
 * - Word clouds:
 * Controls for font size, color, and rotation are provided
 * 
 * - Simple lists:
 * Controls for sorting, and saving the list as a dataframe are provided
 * 
 * - Bar plots:
 * Controls for sorting are provided
 * 
 * TODO Too much functionality is crammed into this class. The particulars of 
 * each viewing method should be factored into its own class.
 * 
 * TODO it would make sense to view the term-document matrix from this dialog as well
 */
package edu.cens.text;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.rosuda.deducer.Deducer;
import org.rosuda.deducer.toolkit.HelpButton;

public class TermFrequencyDialog extends JFrame
{
	private static final String HELP_URL = "index.php?n=Main.WordCount";
	public static final int BAR_CHART = 1;
	//public static final int DOCUMENT_TERM_MATRIX = -1;
	public static final int TOTAL_FREQUENCIES = 0;
	public static final int WORD_CLOUD = 2;
	
	private boolean useDocumentFrequency;

	JTextField topPercentField;
	JTextField minFreqField;
	JTextField absoluteNTermsField;

	JComboBox corpusSelector;
	JComboBox viewMethodSelector;

	JRadioButton useAllButton = new JRadioButton();
	JRadioButton useTopNButton = new JRadioButton();
	JRadioButton useTopPercentButton = new JRadioButton();

	JPanel optionsPanel;

	GridBagConstraints optionsPanelConstraints;

	
	private AbstractTermFrequencyViewer[] viewers;


	public boolean getUseDocumentFrequency()
	{
		return useDocumentFrequency;
	}
	
	public TermFrequencyDialog(JFrame parent)
	{
		super("Term Frequency");
		useDocumentFrequency = false;

		//TODO : should probably separate construction / action listener setting from GUI arrangement.
		
		viewers = new AbstractTermFrequencyViewer[]  { 
					new FrequencyTotalViewer(this), 
					new FrequencyBarChartViewer(this), 
					new WordCloudViewer(this) 
				};

		constructGui();
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

	public void setCorpora(String[] newCorpora)
	{
		if (newCorpora != null && corpusSelector != null)
		{
			corpusSelector.removeAllItems();
			for (String s : newCorpora)
			{
				corpusSelector.addItem(s);
			}
		}
	}

	public void setViewMethod(int method)
	{
		this.viewMethodSelector.setSelectedIndex(method);
	}

	public void constructGui()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints c = getTopLevelLayoutDefaults();

		//////////////////////////////////////////////////////
		// Source data selection panel //////////////////////
		//////////////////////////////////////////////////////
		JPanel sourceDataSelectionPanel = constructSourceDataSelectionPanel();


		//////////////////////////////////////////////////////
		// OK/Cancel/Help panel /////////////////////////////
		//////////////////////////////////////////////////////
		JPanel okPanel = constructOkHelpPanel();

		//////////////////////////////////////////////////////
		// Document/Term Frequency Panel /////////////////////
		//////////////////////////////////////////////////////




		// --------------------------------------------------------------------------
		// +++++++++ Add data selector to toplevel dialog +++++++++++++
		c = getTopLevelLayoutDefaults();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;

		//c.insets = DIALOG_INSETS;
		this.add(sourceDataSelectionPanel, c);

		// +++++++++ Add min freq panel to toplevel dialog +++++++++++++
		c = getTopLevelLayoutDefaults();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.weighty = 0;
		c.weightx = 0;
		c.anchor = c.NORTH;
		//c.insets = DIALOG_INSETS;
		this.add(constructFilteringPanel(), c);


		// +++++++++ Add View mode selection panel to toplevel dialog
		// +++++++++++++
		c = getTopLevelLayoutDefaults();
		c.fill = GridBagConstraints.BOTH;
		//c.anchor = GridBagConstraints.NORTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		//c.insets = DIALOG_INSETS;
		this.add(constructViewMethodPanel(), c);

		// +++++++++ Add OkCancel panel to toplevel dialog +++++++++++++
		c = getTopLevelLayoutDefaults();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;

		//c.insets = DIALOG_INSETS;
		this.add(okPanel, c);

		this.pack();
		this.setMinimumSize(this.getSize());
		this.setResizable(false);// MAYBE not a good idea, but normal for dialogs.
	}

	public int getMinFrequency()
	{
		return Integer.parseInt(minFreqField.getText());
	}

	public int getAbsoluteNTerms()
	{
		if (this.useTopNButton.isSelected())
		{
			return Integer.parseInt(this.absoluteNTermsField.getText());
		}
		else
		{
			return 0;
		}
	}

	public int getPercent()
	{
		if (this.useTopPercentButton.isSelected())
		{
			return Integer.parseInt(this.topPercentField.getText());
		}
		else
		{
			return 0;
		}
		// return _thresholdCMB.getSelectedItem().hashCode();
	}

	public String getCorpus()
	{
		if (this.corpusSelector.getSelectedItem() != null)
		{
			return this.corpusSelector.getSelectedItem().toString();
		} 
		else
		{
			return null;
		}
	}

	private GridBagConstraints getTopLevelLayoutDefaults()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(3, 3, 3, 3);
		return c;
	}

	private JPanel constructOkHelpPanel()
	{
		JPanel okPanel = new JPanel(new GridBagLayout());
		JButton okButton = new JButton("View");
		JButton cancelButton = new JButton("Close");
		getRootPane().setDefaultButton(okButton);

		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(false);
			}
		});

		ActionListener okAction = new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				doVisualization();
			}

		};

		okButton.addActionListener(okAction);

		GridBagConstraints c = getTopLevelLayoutDefaults();
		okPanel.add(new JLabel(""), c);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.ipadx = 10;
		c.ipady = 10;
		okPanel.add(new HelpButton(HELP_URL), c);
		
		c = getTopLevelLayoutDefaults();

		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		okPanel.add(cancelButton, c);
		c.gridx = 2;
		okPanel.add(okButton, c);
		return okPanel;
	}

	private JPanel constructSourceDataSelectionPanel()
	{
		JPanel sourceDataSelectionPanel = new JPanel(new GridBagLayout());

		GridBagConstraints c = getTopLevelLayoutDefaults();
		// c.ipadx = 5;
		c.ipady = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 0;

		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;

		JLabel sourceDatLab = new JLabel("Source Data:");


		sourceDataSelectionPanel.add(sourceDatLab, c);


		corpusSelector = new JComboBox();
		corpusSelector.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for (AbstractTermFrequencyViewer viewer : viewers)
				{
					viewer.onCorpusChange(getCorpus());
				}
			}
		});

		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;

		sourceDataSelectionPanel.add(corpusSelector, c);
		return sourceDataSelectionPanel;
	}

	private JPanel constructFrequencyMethodPanel()
	{
		JPanel frequencyMethodPanel = new JPanel(new GridBagLayout());

		JRadioButton termFreqRadBut = new JRadioButton("Term Frequency");
		JRadioButton docFreqRadBut = new JRadioButton("Document Frequency");
		termFreqRadBut.setSelected(true);

		ButtonGroup freqButGroup = new ButtonGroup();
		freqButGroup.add(termFreqRadBut);
		freqButGroup.add(docFreqRadBut);

		termFreqRadBut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				useDocumentFrequency = false;
			}
		});

		docFreqRadBut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				useDocumentFrequency = true;
			}
		});

		GridBagConstraints c = getTopLevelLayoutDefaults();
		frequencyMethodPanel.add(termFreqRadBut, c);
		c.gridx = 1;
		frequencyMethodPanel.add(docFreqRadBut, c);
		return frequencyMethodPanel;
	}

	private JPanel constructFilteringPanel()
	{
		GridBagConstraints c = getTopLevelLayoutDefaults();
		//c.insets = new Insets(0, 0, 4, 0);
		c.ipadx = 0;
		c.ipady = 0;

		JPanel usePanelInner = new JPanel(new GridBagLayout());

		useAllButton = new JRadioButton();
		useTopNButton = new JRadioButton();
		useTopPercentButton = new JRadioButton();

		useTopNButton.setSelected(true);
		//This line ensures each this radio button row has same height as others.
		useAllButton.setPreferredSize
		(
				new Dimension
				(
						useAllButton.getPreferredSize().width, 
						new JTextField().getPreferredSize().height
				)
		);

		ButtonGroup useGroup = new ButtonGroup();
		useGroup.add(useAllButton);
		useGroup.add(useTopNButton);
		useGroup.add(useTopPercentButton);

		c.anchor = c.WEST;
		c.fill = c.NONE;
		c.weighty = 0;
		c.ipady = 3;
		c.ipadx = 7;
		// ======== 'Use all' radio button row =======================
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		usePanelInner.add(useAllButton, c);
		// add text for use all
		c.gridx = 1;
		c.gridwidth = 3;
		c.weightx = 0;
		usePanelInner.add(new JLabel("All Terms"), c);

		// ======= 'Use top #' radio button row =======================
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		usePanelInner.add(useTopNButton, c);

		// add text for Top # terms
		c.gridx = 1;
		c.gridwidth = 1;
		usePanelInner.add(new JLabel("Top"), c);

		c.gridx = 2;
		c.gridwidth = 2;
		c.fill = c.HORIZONTAL;
		this.absoluteNTermsField = new JTextField("100",3);

		usePanelInner.add(absoluteNTermsField, c);

		// ======= 'Use top N %' radio button row =======================
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		usePanelInner.add(useTopPercentButton, c);

		// add text for Top # terms
		c.gridx = 1;
		c.gridwidth = 1;
		usePanelInner.add(new JLabel("Top"), c);

		c.gridx = 2;
		
		this.topPercentField = new JTextField("20", 3);
		//		topPercentField.setPreferredSize(new Dimension(50, topPercentField
		//				.getPreferredSize().height));
		usePanelInner.add(topPercentField, c);

		c.gridx = 3;
		c.weightx = 0;
		usePanelInner.add(new JLabel("%"), c);

		Border usePanelBorder = new TitledBorder("Use:")
		{
//			public Insets getBorderInsets(Component c)
//			{
//				return new Insets(20, 10, 10, 10);
//			}
		};//("Use:");
		usePanelInner.setBorder(usePanelBorder);

		///////////////////////////////////////////////////
		// Min Frequency Stuff ///////////////////////////////////
		/////////////////////////////////////////////////

		JPanel minFreqPanel = new JPanel(new GridBagLayout());

		c = getTopLevelLayoutDefaults();

		c.fill = c.NONE;
		c.weighty = 0;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0;
		minFreqPanel.add(new JLabel("Frequency :"), c);
		c.gridx = 1;
		c.weightx = 1;

		//c.fill = c.HORIZONTAL;
		minFreqField = new JTextField(4);
		minFreqField.setText("0");
		minFreqPanel.add(minFreqField,c);

		//p.setBorder(BorderFactory.createEtchedBorder());
		minFreqPanel.setBorder(BorderFactory.createTitledBorder("Filtering:"));
		//minFreqPanel.setMinimumSize(minFreqPanel.getPreferredSize());

		c.gridy = 1;
		c.weighty = 1;
		minFreqPanel.add(new JLabel(""),c);

		// ==================================================
		JPanel allP = new JPanel(new GridBagLayout());
		c = getTopLevelLayoutDefaults();

		c.weighty = 0;
		c.gridwidth = 2;
		allP.add(constructFrequencyMethodPanel(), c);

		c.gridy = 1;
		c.gridx = 0;
		c.weighty = 1;
		c.gridwidth = 1;
		c.fill = c.BOTH;
		allP.add(usePanelInner,c);

		c.gridx = 1;
		allP.add(minFreqPanel, c);

		return allP;
	}

	private JPanel constructViewOptionsPanel()
	{
		JPanel ret = new JPanel(new GridBagLayout());
		ret.setBorder(BorderFactory.createTitledBorder("View Options:"));

		JPanel allOptionsPanel = getSelectedViewer().getOptionsPanel();

		GridBagConstraints c = getTopLevelLayoutDefaults();
		
		c = getTopLevelLayoutDefaults();
		c.gridy = 1;
		c.anchor = c.NORTHWEST;
		c.fill = c.BOTH;

		ret.add(allOptionsPanel,c);
		return ret;
	}

	private JPanel constructViewMethodPanel()
	{
		final JPanel p = new JPanel(new GridBagLayout());

		GridBagConstraints c = getTopLevelLayoutDefaults();
		c.gridx = 0;
		c.gridy = 0;
		//c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.anchor = c.EAST;

		p.add(new JLabel("View As:"), c);

		c.gridx = 1;
		c.weightx = 1;
		c.anchor = c.WEST;

		viewMethodSelector = new JComboBox();
		p.add(viewMethodSelector, c);

		final TermFrequencyDialog thisPtr = this;

		viewMethodSelector.addActionListener(new ActionListener()
		{		
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				//optionsPanel = getViewModeOptionsPanel();
				if (optionsPanel != null)
				{
					p.remove(optionsPanel);
					optionsPanel = //getSelectedViewer().getOptionsPanel();  
						constructViewOptionsPanel();
					p.add(optionsPanel, optionsPanelConstraints);
					thisPtr.setMinimumSize(null);
					thisPtr.pack();
					thisPtr.setMinimumSize(thisPtr.getSize());
					p.validate();
					thisPtr.repaint();

				}
			}
		});
		
		for (AbstractTermFrequencyViewer v : viewers)
		{
			viewMethodSelector.addItem(v);
		}



		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		optionsPanel = this.constructViewOptionsPanel();
		optionsPanel.setEnabled(false);
		c.anchor = c.NORTH;
		c.fill = c.BOTH;
		c.weighty = 1;


		optionsPanelConstraints = c;
		p.add(optionsPanel, c);
		// p.setBorder(BorderFactory.createTitledBorder("View:"));

		return p;
	}

	
	public void setVisible(boolean show)
	{
		if (show)
		{
			this.setLocationRelativeTo(this.getParent());
		}
		super.setVisible(show);
	}

	public String getTermFreqCall(String extraArgs)
	{
		int percentage = getPercent();
		int absoluteNTerms = getAbsoluteNTerms();
		int minFreq = getMinFrequency();
		//String sortOptions = sortArg == null ? "" : "sorted=\"" + sortArg + "\", "; 
		//getSorted();
		//boolean ascending = getAsc();

		String termFreqCall = "term.freq(" + 
		"d=" + getCorpus() + ", " + 
		"percent=" + percentage + ", " +
		"topN=" + absoluteNTerms + ", " +
		"minFreq=" + minFreq + "," +
		"useDocFreq=" + (""+useDocumentFrequency).toUpperCase() + "," +
		
		extraArgs +
		//sortOptions +
		//"decreasing=" + (""+ascending).toUpperCase() + ", " +
		")";
		return termFreqCall;
	}

//	private String getTermFreqCall()
//	{
//		return getTermFreqCall("");
//	}
	
	private void doVisualization()
	{
		// TODO validate selected name
		// TODO save matrix name somewhere?
		// String generateDTM = saveNameField.getText() +
		// " <-  DocumentTermMatrix(" + getSourceCorpus() + ")";
		// System.out.println(generateDTM);
		// Deducer.execute(generateDTM);
		// cens.txt_barplot(cens.term_freq(get(" + getCorpus() + "),
		// 100, sorted, decreasing));

		if (this.getCorpus() == null)
		{
			JOptionPane.showMessageDialog(getContentPane(),
					"You do not have any corpuses to visualize!",
					"Warning",
					JOptionPane.WARNING_MESSAGE);
		} 
		else
		{
			getSelectedViewer().executeView();
		}
	}

	private AbstractTermFrequencyViewer getSelectedViewer()
	{
		return ((AbstractTermFrequencyViewer) this.viewMethodSelector.getSelectedItem());
	}
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TermFrequencyDialog dlg = new TermFrequencyDialog(f)
		{
			@Override
			public void setVisible(boolean b)
			{
				super.setVisible(b);
				if (!b)
				{
					System.exit(0);
				}
			}
		};
		dlg.setCorpora(new String[] { 
				//"AAAAAAA", "BBBBBBBBBBB", "^^^^^^^^^^^^^^^" 
		});
		dlg.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				System.exit(0);
			}



		});

		dlg.setVisible(true);
		dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

}
