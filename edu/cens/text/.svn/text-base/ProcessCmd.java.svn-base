package edu.cens.text;

import org.rosuda.deducer.Deducer;

import javax.swing.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by IntelliJ IDEA. User: Neal Date: 2/22/11 Time: 10:00 PM
 * 
 * An enum that encapsulates each text-preprocessing action in R.
 * 
 */

public enum ProcessCmd
{
	tolower("To Lower Case", "tm_map(%s, content_transformer(tolower))"), 
	depunct("Remove Punctuation", "tm_map(%s, removePunctuation)"),
	denumber("Remove Numbers", "tm_map(%s, removeNumbers)"),
	deword(	"Remove Stopwords (default list)", "tm_map(%s, removeWords, stopwords())")
	{
		String stopwordFile = null;
		
		public String getRCmd(String... args)
		{

			return String.format("tm_map(%s, removeWords, stopwords("+getStopwordString()+"))", args);
		}
		
		private String getStopwordString()
		{
			if (stopwordFile != null)
			{
				return "'" + stopwordFile + "'";
			}
			else
			{
				return "";
			}
		}
		
		public JMenuItem[] getExtraOptions()
		{
			return new JMenuItem[] { 
					//= BEGIN ARRAY CONTENTS ================================
					new JMenuItem("Print Stopwords")
					{
						{
							addActionListener(new ActionListener()
							{

								public void actionPerformed(ActionEvent e)
								{
									if (stopwordFile == null)
									{
										Deducer.execute("stopwords();");
									}
									else
									{
										Deducer.execute("readLines(" + getStopwordString() + ",warn=F)");
									}
									//readLines("/Users/xander/Desktop/stop.txt")
								}
							});
						}
					},
					
					new JMenuItem("Use Custom Stopword File")
					{
						//JRadioButtonMenuItem theMenuItemItself = this;
						{
							//defaultOrCustomStopwordButtonGroup.add(this);
							addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									final JFileChooser fc = new JFileChooser();
									//In response to a button click:
									int returnVal = fc.showOpenDialog(null);

									if (returnVal == JFileChooser.APPROVE_OPTION) 
									{
										stopwordFile = fc.getSelectedFile().toString();
										File fff = new File(stopwordFile);
										//System.out.println(stopwordFile);
										_label = "Remove Stopwords (" + fff.getName() + ")";
										//theMenuItemItself.setSelected(true);
										
										if(_parent != null)
										{
											_parent.repaint();
										}
										
										//SwingUtilities.windowForComponent(theMenuItemItself).invalidate();
										//SwingUtilities.windowForComponent(theMenuItemItself).repaint();
									}
									else
									{
									}
									//readLines("/Users/xander/Desktop/stop.txt")
								}
							});
						}
					},
					
					new JMenuItem("Use Default Stopwords")
					{
						{
							//final JRadioButtonMenuItem thisPtr = this;
							//defaultOrCustomStopwordButtonGroup.add(this);
							this.setSelected(true);
							addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									stopwordFile = null;
									//thisPtr.setSelected(true);
									_label = "Remove Stopwords (default list)";
								}
							});
						}
					}
					//= END ARRAY CONTENTS ===================================
			};
		}
	},
	strip("Remove Whitespace", "tm_map(%s, stripWhitespace)"), 
	stem("Stem Words", "tm_map(%s, stemDocument)"), ;

	/////////////////////////////////////////////////////
	// METHODS //////////////////////////////////////////
	/////////////////////////////////////////////////////

	ProcessCmd(String label, String function)
	{
		_label = label;
		_function = function;
	}

	public String getRCmd(String... args)
	{
		return String.format(_function, args);
	}

	public JMenuItem[] getExtraOptions()
	{
		return new JMenuItem[0];
	};

	public String _label;
	public final String	_function;
	protected  Component _parent;
	protected static ButtonGroup defaultOrCustomStopwordButtonGroup = new ButtonGroup();

	public String toString()
	{
		return _label;
	}

	public String getLabel()
	{
		return _label;
	}
	
	public void setParentComponent(Component parent)
	{
		this._parent = parent;
	}
}
