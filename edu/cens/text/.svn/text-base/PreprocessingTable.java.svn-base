package edu.cens.text;

/**
 * A table for containing text-corpus preprocessing actions.
 * 
 * Each row represents an action.  A row will contain:
 * - The action's name
 * 
 * - A check box for enabling/disabling the action
 * 
 * - A button for bringing up options specific to the action
 * 
 * - A pair of buttons for moving the position of an action-row up or down
 * This is for controlling the order in which the actions are applied
 * 
 * NOTE: this class is separate from the actual dialog for preprocessing text
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.rosuda.deducer.toolkit.IconButton;


import edu.cens.text.OptionsButtonPanel;

public class PreprocessingTable extends JTable
{
	protected static final int ENABLED_CHECKBOX_COLUMN = 0;
	protected static final int ACTION_COLUMN = 1;
	protected static final int OPTIONS_COLUMN = 2;
	protected static final int REORDER_COLUMN = 3;
	
	private Object [][] tableContents;
	int nActions;
	public PreprocessingTable(int nActions)
	{	
		super(nActions,4);
		
		JPopupMenu menu1 = new JPopupMenu();
		menu1.add(new JMenuItem("Stop!"));
		menu1.add(new JMenuItem("Meaningless"));
		menu1.add(new JMenuItem("Text"));
		
		JPopupMenu menu2 = new JPopupMenu();
		menu2.add(new JMenuItem("Stem"));
		menu2.add(new JMenuItem("Pointless"));
		menu2.add(new JMenuItem("Words"));
		this.nActions = nActions;
		
		tableContents = new Object [nActions][4];
		
		for (int i = 0; i < nActions; i++)
		{
			tableContents[i][REORDER_COLUMN] = new UpDownButtonPanel();
			tableContents[i][ENABLED_CHECKBOX_COLUMN] = new Boolean(true);
			tableContents[i][ACTION_COLUMN] = "Action " + i;
			tableContents[i][OPTIONS_COLUMN] = new OptionsButtonPanel(null);
		}
		
		setModel(new PreproListModel(tableContents));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.getColumnModel().getColumn(PreprocessingTable.OPTIONS_COLUMN).setCellRenderer(new PrepoCellRenderer());
		this.getColumnModel().getColumn(PreprocessingTable.OPTIONS_COLUMN).setCellEditor(new PrepoCellEditor());
		
		this.getColumnModel().getColumn(PreprocessingTable.REORDER_COLUMN).setCellRenderer(new PrepoCellRenderer());
		this.getColumnModel().getColumn(PreprocessingTable.REORDER_COLUMN).setCellEditor(new PrepoCellEditor());
		
		this.getColumnModel().getColumn(PreprocessingTable.ACTION_COLUMN).setCellRenderer(new PrepoCellRenderer());
		
		setRowHeight(30);
		this.setCellSelectionEnabled(false);
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(false);
		this.setFocusable(false);
	
		this.showHorizontalLines = true;
		this.showVerticalLines = true;
		this.setBorder(new LineBorder(Color.BLACK, 1));
		//(new LineBorder(Color.BLACK));
		this.setGridColor(Color.LIGHT_GRAY);
		this.setIntercellSpacing(new Dimension(4,4));
		this.getTableHeader().setReorderingAllowed(false);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		//checkbox width
		this.getColumnModel().getColumn(ENABLED_CHECKBOX_COLUMN).setPreferredWidth(35);
		
		//Reorder arrow buttons
		this.getColumnModel().getColumn(REORDER_COLUMN).setPreferredWidth(25);
		
		//Action name
		this.getColumnModel().getColumn(ACTION_COLUMN).setPreferredWidth(225);
		
		//Options button
		this.getColumnModel().getColumn(OPTIONS_COLUMN).setPreferredWidth(25);
		this.setShowVerticalLines(false);
	}
	
	public void setOptionsMenu(JPopupMenu menu, int row)
	{
		((OptionsButtonPanel)tableContents[row][OPTIONS_COLUMN]).setMenu(menu);
	}
	
	public void setAction(Object action, int row)
	{
		tableContents[row][ACTION_COLUMN] = action;
	}
	
	public void setHasOption(boolean hasOptions, int row)
	{
		((OptionsButtonPanel) tableContents[row][OPTIONS_COLUMN]).setButtonVisible(hasOptions);
	}
	
	public Object getAction(int row)
	{
		return tableContents[row][ACTION_COLUMN];
	}
	
	public boolean isEnabled(int row)
	{
		return (Boolean) tableContents[row][ENABLED_CHECKBOX_COLUMN];
	}
	public void setEnabled(int row, boolean enabled)
	{
		tableContents[row][ENABLED_CHECKBOX_COLUMN] = enabled;
	}
	
	
	public void tableChanged(TableModelEvent e)
	{
		super.tableChanged(e);
		repaint();
	}
	
	public void moveRowUp(int row)
	{
		if (row - 1 >= 0)
		{
			swapRows(row, row - 1);
		}
	}
	
	public void moveRowDown(int row)
	{
		if (row + 1 < this.getRowCount())
		{
			swapRows(row, row + 1);
		}
	} 
	
	public void swapRows(int a, int b)
	{
		Object [] rowA =  this.tableContents[a];
		Object [] rowB =  this.tableContents[b];
		tableContents[a] = rowB;
		tableContents[b] = rowA;
		//table.setModel(new PreproListModel(tableContents));
		this.repaint();
	}
	

	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.add(new PreprocessingTable(3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
}

class PrepoCellRenderer implements TableCellRenderer
{
	private UpDownButtonPanel reorderPanel;
	private JLabel lab;
	public PrepoCellRenderer() 
	{
		super();
		reorderPanel = new UpDownButtonPanel();
		lab = new JLabel();
	}
	
    public void setValue(Object value) 
    {
    }
	
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		//return JRadioButtonTableExample.panels[row];
		Component retComp;
		
		if (column == PreprocessingTable.REORDER_COLUMN)
		{
			retComp = reorderPanel;
		}
		else if (column == PreprocessingTable.ACTION_COLUMN)
		{
			//JLabel lab = new JLabel(value.toString());
			lab.setText(value.toString() + "  "); // Remove {+ "  "} if other type of alignment.
			boolean enabled = (Boolean)table.getModel().getValueAt(row, PreprocessingTable.ENABLED_CHECKBOX_COLUMN);
			
			//Color foreground = (enabled) ? SystemColor.textText : SystemColor.textInactiveText;
			//retComponent.setForeground(foreground);
			
			lab.setHorizontalAlignment(SwingConstants.RIGHT);
			lab.setEnabled(enabled);
			retComp = lab;
		}
		else
		{
			retComp = (Component) table.getModel().getValueAt(row, column);
		}
		
		retComp.setBackground(Color.WHITE);
		
		return retComp;
	}
}

class PrepoCellEditor extends AbstractCellEditor implements ItemListener, TableCellEditor
{
	// private JRadioButton button;
	private OptionsButtonPanel panel;
	private JButton button;
	
	public PrepoCellEditor()
	{
		super();
		 //panel = new OptionsButtonPanel2();
	}

	//public RadioButtonEditor(JCheckBox checkBox){super(checkBox);}

	
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column)
	{
		//change the panel according to the row
		//return panel;
		
		//return JRadioButtonTableExample.panels[row];
		//if (table.getColumnName(column).equalsIgnoreCase("Reorder"))
		if (column == PreprocessingTable.REORDER_COLUMN)
		{
			((UpDownButtonPanel) value).setRow(row);
			((UpDownButtonPanel) value).setTable((PreprocessingTable) table);
			
		}
		return (Component) table.getModel().getValueAt(row, column);
	}

	
	public Object getCellEditorValue()
	{
		return null;
	}

	
	public void itemStateChanged(ItemEvent e)
	{
		super.fireEditingStopped();
	}
}

class PreproListModel extends AbstractTableModel
{
	String[] columnNames;
	
	Object[][] tableContents;

	
	public PreproListModel(Object [][] tableContents)
	{
		this.tableContents = tableContents;
		
		columnNames = new String[4];
		columnNames[PreprocessingTable.REORDER_COLUMN] = "Reorder";
		columnNames[PreprocessingTable.ACTION_COLUMN] = "Action";
		columnNames[PreprocessingTable.OPTIONS_COLUMN] = "Options";
		columnNames[PreprocessingTable.ENABLED_CHECKBOX_COLUMN] = "Enable";
	}
	
    public int getColumnCount() 
    {
        return columnNames.length;
    }

    public int getRowCount() 
    {
        return tableContents.length;
    }

    public String getColumnName(int col) 
    {
        return columnNames[col];
    }
    
    public void setValueAt(Object value, int row, int col) 
    {
    	if (this.getColumnClass(col) == Boolean.class)
    	{
    		tableContents[row][col] = value;
    		fireTableCellUpdated(row, col);
    	}
    }


    public Object getValueAt(int row, int col) 
    {
        return tableContents[row][col];
    }
    
    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) 
    {
        return getValueAt(0, c).getClass();
    }
    
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) 
    {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
            return this.getColumnClass(col) != String.class;
    }	
}

class OptionsButtonPanel extends JPanel
{
	private JButton button;
	JPopupMenu men;
	OptionsButtonPanel thePanel;
	private static final boolean FACE_RIGHT = false;// if false, face down

	public OptionsButtonPanel(JPopupMenu menu)
	{
		super();
		thePanel = this;
		//this.setBackground(Color.WHITE);
		
		//Add a label saying "options"
		//this.add(new JLabel("options"), BorderLayout.CENTER);

		men = menu;

		
		if (FACE_RIGHT)
		{
			//button = new BasicArrowButton(SwingConstants.EAST);
			button = new IconButton("/icons/advanced_21.png", "options", null, "options");
			button.setContentAreaFilled(false);
			button.setMinimumSize(new Dimension(21, 21));
			button.setPreferredSize(new Dimension(21, 21));
		}
		else
		{
			button = new IconButton("/icons/advanced_21.png", "options", null, "options");
			//button = new BasicArrowButton(SwingConstants.SOUTH);
			button.setContentAreaFilled(false);
			button.setMinimumSize(new Dimension(21, 21));
			button.setPreferredSize(new Dimension(21, 21));
		}
		
		this.add(button, BorderLayout.NORTH);
		ActionListener al = new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{

				Point p = button.getLocationOnScreen();
				
				if (FACE_RIGHT)
				{
					men.setLocation((int) p.getX() + button.getWidth(), (int) p.getY());
				}
				else
				{
					men.setLocation((int) p.getX(), (int) p.getY() + + button.getHeight());
				}
				
				men.setInvoker(thePanel);
				men.setVisible(true);
			}
		};
		button.addActionListener(al);
	}
	
	public void setButtonVisible(boolean visible)
	{
		this.button.setVisible(visible);
	}
	
	public void setMenu(JPopupMenu menu)
	{
		this.men = menu;
	}
}

