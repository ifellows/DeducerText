/**
 * A combo box containing the all defined R-objects of a particular class.
 * 
 * TODO a combo box is not good for large numbers of variables.
 * Change to something equally compact, but more efficient, and powerful
 */

package edu.cens.text;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.rosuda.JGR.layout.AnchorConstraint;
import org.rosuda.JGR.layout.AnchorLayout;
import org.rosuda.deducer.Deducer;

public class RObjectChooser extends JComboBox
{

	String initialModel = null;
	String lastModel = null;
	String clss = null;
	
	public RObjectChooser(Component parentWindow)
	{
		super();
		
		if( parentWindow != null )
		{
			parentWindow.addComponentListener(new ComponentAdapter()
			{
				public void componentShown(ComponentEvent arg0)
				{
					refreshObjects();
				}
			});
		}
	}
	
	public RObjectChooser()
	{
		this(null);
	}

	public String getSelectedObject()
	{
		return (String) this.getComboBox().getModel().getSelectedItem();
	}

	public int getObjectCount()
	{
		return this.getItemCount();
	}


	public JComboBox getComboBox()
	{
		return this;
	}

	public void refreshObjects()
	{

			String[] objs;
			boolean isIncludeInherited = true;
			try
			{
				String call = "get.objects(" + getClassFilter()
						+ ",includeInherited="
						+ (isIncludeInherited ? "TRUE" : "FALSE") + ")";
				objs = Deducer.eval(call).asStrings();
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				objs = new String[] {};
			}
			
			this.setModel(new DefaultComboBoxModel(objs));
	}

	/**
	 * only show R objects of class
	 * 
	 * @param className
	 *            R class name
	 */
	public void setClassFilter(String className)
	{
		if (className.equals("NULL"))
		{
			this.clss = "NULL";
		}
		else
		{
			this.clss = "\"" + className + "\"";
		}
	}

	public String getClassFilter()
	{
		return clss;
	}

	/*
	 * Start DeducerWidget methods
	 */
}
