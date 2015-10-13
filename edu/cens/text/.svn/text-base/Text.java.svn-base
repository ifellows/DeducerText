package edu.cens.text;

import org.rosuda.JGR.JGR;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.deducer.Deducer;
import org.rosuda.deducer.WindowTracker;
import org.rosuda.deducer.data.DataViewerController;
import org.rosuda.deducer.data.DataViewerTab;
import org.rosuda.deducer.data.DataViewerTabFactory;
import org.rosuda.deducer.toolkit.HelpButton;
import org.rosuda.ibase.toolkit.EzMenuSwing;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by IntelliJ IDEA. User: Neal Date: 1/15/11 Time: 1:08 PM
 * 
 * Initialized DeducerText menubar, and GUI stuff for JGR
 */

public class Text
{

	private static TermFrequencyDialog viewOptionsDialog = new TermFrequencyDialog(JGR.MAINRCONSOLE);
	
	
public static String[] getCorpora()
{
	org.rosuda.REngine.REXP corpora = Deducer.eval("get.objects(\"Corpus\")");//.asList().keys();
	
	String[] v = {};
	try
	{
		v = corpora.asStrings();
	}
	catch (REXPMismatchException e1)
	{
		//e1.printStackTrace();
		v = new String[]{};
	}
	return v;
	
}
	public static class MenuTest extends JFrame
	{
		public MenuTest()
		{
			super();

			MenuListener listener = new MenuListener()
			{
				public void menuCanceled(MenuEvent e)
				{
					dumpInfo("Canceled", e);
				}

				public void menuDeselected(MenuEvent e)
				{
					dumpInfo("Deselected", e);
				}

				public void menuSelected(MenuEvent e)
				{
					dumpInfo("Selected", e);
				}

				private void dumpInfo(String s, MenuEvent e)
				{
					JMenu menu = (JMenu) e.getSource();
					System.out.println(s + ": " + menu.getText());
				}
			};

			JMenu fileMenu = new JMenu("File");
			fileMenu.addMenuListener(listener);
			fileMenu.add(new JMenuItem("Open"));
			fileMenu.add(new JMenuItem("Close"));
			fileMenu.add(new JMenuItem("Exit"));
			JMenu helpMenu = new JMenu("Help");
			helpMenu.addMenuListener(listener);
			helpMenu.add(new JMenuItem("About MenuTest"));
			helpMenu.add(new JMenuItem("Class Hierarchy"));
			helpMenu.addSeparator();
			helpMenu.add(new JCheckBoxMenuItem("Balloon Help"));
			final JMenu subMenu = new JMenu("Categories");
			final JMenuItem dynm = new JMenuItem("test");
			subMenu.addMenuListener(new MenuListener()
			{
				public void menuSelected(MenuEvent e)
				{
					subMenu.add(dynm);
				}

				public void menuDeselected(MenuEvent e)
				{
					subMenu.removeAll();
				}

				public void menuCanceled(MenuEvent e)
				{
					subMenu.removeAll();
				}
			});
			JRadioButtonMenuItem rb;
			ButtonGroup group = new ButtonGroup();
			subMenu.add(rb = new JRadioButtonMenuItem("A Little Help", true));
			group.add(rb);
			subMenu.add(rb = new JRadioButtonMenuItem("A Lot of Help"));
			group.add(rb);
			helpMenu.add(subMenu);
			JMenuBar mb = new JMenuBar();
			mb.add(fileMenu);
			mb.add(helpMenu);
			setJMenuBar(mb);
		}

	}

	public static void main(String args[])
	{
		JFrame frame = new MenuTest();
		frame.setSize(300, 300);
		frame.setVisible(true);
	}

	public void initJGR()
	{
		
		DataViewerController.init();
		DataViewerController.addDataType("VCorpus", "txt");
		DataViewerController.addTabFactory("VCorpus","Data View", new DataViewerTabFactory()
		{
			public DataViewerTab makeViewerTab(String dataName)
			{
				CorpusViewerPanel ret = new CorpusViewerPanel(false);
				ret.setData(dataName);
				return ret;
			}
		});

		String text = "Text";
		
		// EzMenuSwing.addMenu(JGR.MAINRCONSOLE, text);
		// EzMenuSwing.addMenuBefore(JGR.MAINRCONSOLE, text, "Help");

		// Add the menu item to left of "Help" menu
		// TODO : this should probably be folded into EzMenuSwing
		String toRightOfItem = "Packages & Data";
		JMenuBar mb = JGR.MAINRCONSOLE.getJMenuBar();
		int insertPos = 0;
		boolean found = false;
		for (; insertPos < mb.getMenuCount(); insertPos++)
		{
			if (mb.getMenu(insertPos).getText().equals(toRightOfItem))
			{
				found = true;
				break;
			}
		}
		if (!found)
		{
			throw new IllegalArgumentException("Could not find '"
					+ toRightOfItem + "' in the menu bar.");
		}
		mb.add(new JMenu(text), insertPos);

		JMenu textMenu = EzMenuSwing.getMenu(JGR.MAINRCONSOLE, text);

		final JMenuItem quickWordMenuItem = new JMenuItem("Quick Word Cloud");
		quickWordMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Deducer.threadedEval(".getDialog('Quick Word Cloud')$run()");
			}
		});
		textMenu.add(quickWordMenuItem);

		final JMenuItem textPlotMenuItem = new JMenuItem("Text Plot");
		textPlotMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Deducer.threadedEval(".getDialog('Text Plot')$run()");
			}
		});
		textMenu.add(textPlotMenuItem);
		
		textMenu.add(new JSeparator());
		
		final JMenuItem importTextMenuItem = new JMenuItem("Import Corpus From File");
		importTextMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TextFileChooser tfc = new TextFileChooser(JGR.MAINRCONSOLE);
				tfc.run();
			}
		});
		textMenu.add(importTextMenuItem);
		
		final JMenuItem extractCorpusMenuItem = new JMenuItem("Extract Corpus From Dataframe");
		
		extractCorpusMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				//JFrame f = new JFrame();
				// needsRLocked=true;
				ExtractCorpusDialog inst = new ExtractCorpusDialog( JGR.MAINRCONSOLE );
				inst.setVisible(true);
				WindowTracker.addWindow(inst);
			}
		});
		textMenu.add(extractCorpusMenuItem);
		
		textMenu.add(new JSeparator());
		
		// TODO accelerator keys
		
		JMenuItem preprocCorpMenuItem = new JMenuItem("Preprocess Corpus");
		
		preprocCorpMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new PreprocessingDialog(JGR.MAINRCONSOLE).setVisible(true);
			}
		});
		textMenu.add(preprocCorpMenuItem);
		
		textMenu.add(new JSeparator());
		
		JMenuItem viewCorpMenuItem = new JMenuItem("View Corpus");
		
		viewCorpMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//Deducer.eval("cens.viewer();");
				new CorpusViewer(JGR.MAINRCONSOLE).setVisible(true);
			}
		});
		textMenu.add(viewCorpMenuItem);
		
		JMenuItem DocTermMatrixMenuItem = new JMenuItem("View Term Document Matrix");
		DocTermMatrixMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//Deducer.eval("cens.viewer();");
				DocumentTermMatrixViewer f = new DocumentTermMatrixViewer(JGR.MAINRCONSOLE);
				f.setVisible(true);
			}
		});
		textMenu.add(DocTermMatrixMenuItem);
		
		textMenu.add(new JSeparator());
		
		JMenu viewFreqDataMenu = new JMenu("Word Frequencies");
		textMenu.add(viewFreqDataMenu);
		
		JMenuItem termFreqMenuItem = new JMenuItem("Frequency Totals List");
		
		termFreqMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				viewOptionsDialog.setViewMethod(TermFrequencyDialog.TOTAL_FREQUENCIES);
				viewOptionsDialog.setCorpora(getCorpora());
				viewOptionsDialog.setVisible(true);
				viewOptionsDialog.requestFocus();
			}
		});
		viewFreqDataMenu.add(termFreqMenuItem);
		
		JMenuItem barChartMenuItem = new JMenuItem("Bar Chart");
		
		barChartMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				viewOptionsDialog.setViewMethod(TermFrequencyDialog.BAR_CHART);
				viewOptionsDialog.setCorpora(getCorpora());
				viewOptionsDialog.setVisible(true);
				viewOptionsDialog.requestFocus();
			}
		});
		viewFreqDataMenu.add(barChartMenuItem);
		
		JMenuItem wordCloudMenuItem = new JMenuItem("Word Cloud");
		
		wordCloudMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				viewOptionsDialog.setViewMethod(TermFrequencyDialog.WORD_CLOUD);
				viewOptionsDialog.setCorpora(getCorpora());
				viewOptionsDialog.setVisible(true);
				viewOptionsDialog.requestFocus();
			}
		});
		viewFreqDataMenu.add(wordCloudMenuItem);
		
		EzMenuSwing.addJMenuItem(JGR.MAINRCONSOLE, "Help", "Deducer Add-on: Text", "dhelp",new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				HelpButton.showInBrowser(HelpButton.baseUrl+"pmwiki.php?n=Main.DeducerText");
			}
			
		});
		
	}

}
