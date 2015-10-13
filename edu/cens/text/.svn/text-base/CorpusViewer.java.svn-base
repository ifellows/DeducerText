/**
 * Dialog for viewing corpuses.  
 * 
 * Just a wrapper for CorpusViewerPanel
 * 
 * See CorpusViewerPanel.java for more information
 */
package edu.cens.text;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.rosuda.JGR.JGR;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.deducer.Deducer;
import org.rosuda.deducer.widgets.ObjectChooserWidget;

public class CorpusViewer extends JDialog //TODO extend JDialog instead?
{

	public CorpusViewer(JFrame parent)
	{
		super(parent);
		this.add(new CorpusViewerPanel(true));
		this.setTitle("View Corpus");

	}

	
	
	public void setVisible(boolean visible)
	{
		if (visible)
		{
			this.pack();
			this.setMinimumSize(this.getSize());
			this.setLocationRelativeTo(this.getParent());
		}
		super.setVisible(visible);
	}



	public static void main(String[] args)
	{
		CorpusViewer cv2 =  new CorpusViewer(null)
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

		cv2.setVisible(true);
	}

}
