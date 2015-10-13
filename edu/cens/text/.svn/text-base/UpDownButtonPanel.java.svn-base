/**
 * A panel containing an up button and a down button.  Used in PreprocessingTable
 * for reordering preprocessing actions. 
 */

package edu.cens.text;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

import org.rosuda.deducer.toolkit.IconButton;


public class UpDownButtonPanel extends JPanel
{
	PreprocessingTable table;
	int row;
	public UpDownButtonPanel()
	//(PrepocessingDialog prepocessingDialog)
	{
		super();
		row = -1; //will be intialized later by "setRow"
		this.table = null;// Also, set later// prepocessingDialog;
		//this.add(new JLabel("up"), BorderLayout.CENTER); 
		this.setLayout(new GridLayout(2,0));
		//JButton upButton = new IconButton("/icons/sort-up.gif", "up", null, "up");// 
		JButton upButton = new BasicArrowButton(SwingConstants.NORTH);
		upButton.setBackground(Color.white);
		//upButton.setContentAreaFilled(false);
		JButton downButton = new BasicArrowButton(SwingConstants.SOUTH);
		downButton.setBackground(Color.white);
		//downButton.setContentAreaFilled(false);
		
		this.add(upButton);
		this.add(downButton);
		
		upButton.setSize(30, 17);
		downButton.setSize(30, 17);
		
		
		ActionListener upAction = new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				table.moveRowUp(row);
			}
		};
		
		ActionListener downAction = new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				table.moveRowDown(row);
			}
		};
		
		upButton.addActionListener(upAction);
		downButton.addActionListener(downAction);
	}
	
	public void setRow(int row)
	{
		this.row = row;
	}

	public void setTable(PreprocessingTable table)
	{
		this.table = table;
	}
}
