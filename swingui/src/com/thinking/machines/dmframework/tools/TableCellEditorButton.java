package com.thinking.machines.dmframework.tools;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
public class TableCellEditorButton extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener
{
private JTable table;
private Action action;
private int mnemonic;
private Border originalBorder;
private Border focusBorder;
private JButton renderButton;
private JButton editButton;
private Object editorValue;
private boolean isTableCellEditorButton;
public TableCellEditorButton(Action action)
{
this.action = action;
renderButton = new JButton();
editButton = new JButton();
editButton.setFocusPainted( false );
editButton.addActionListener( this );
originalBorder = editButton.getBorder();
setFocusBorder( new LineBorder(Color.BLUE) );
}
public void addMouseListener(JTable table)
{
this.table=table;
table.addMouseListener(this);
}
public Border getFocusBorder()
{
return focusBorder;
}
public void setFocusBorder(Border focusBorder)
{
this.focusBorder = focusBorder;
editButton.setBorder( focusBorder );
}
public int getMnemonic()
{
return mnemonic;
}
public void setMnemonic(int mnemonic)
{
this.mnemonic = mnemonic;
renderButton.setMnemonic(mnemonic);
editButton.setMnemonic(mnemonic);
}
public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
{
if (value == null)
{
editButton.setText( "" );
editButton.setIcon( null );
}
else if (value instanceof Icon)
{
editButton.setText( "" );
editButton.setIcon( (Icon)value );
}
else
{
editButton.setText( value.toString() );
editButton.setIcon( null );
}
this.editorValue = value;
return editButton;
}
public Object getCellEditorValue()
{
return editorValue;
}
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
{
if (isSelected)
{
renderButton.setForeground(table.getSelectionForeground());
renderButton.setBackground(table.getSelectionBackground());
}
else
{
renderButton.setForeground(table.getForeground());
renderButton.setBackground(UIManager.getColor("Button.background"));
}
if (hasFocus)
{
renderButton.setBorder( focusBorder );
}
else
{
renderButton.setBorder( originalBorder );
}
renderButton.setText( (value == null) ? "" : value.toString() );
if(value == null)
{
renderButton.setText( "" );
renderButton.setIcon( null );
}
else if (value instanceof Icon)
{
renderButton.setText( "" );
renderButton.setIcon( (Icon)value );
}
else
{
System.out.println("<TableCellEditorButton>--<getTableCellRendererComponent>--<else(value instaceof Icon)>--(value)"+value.toString());
renderButton.setText( value.toString() );
renderButton.setIcon( null );
}
return renderButton;
}
public void actionPerformed(ActionEvent e)
{
int row = table.convertRowIndexToModel( table.getEditingRow() );
fireEditingStopped();
ActionEvent event = new ActionEvent(table,ActionEvent.ACTION_PERFORMED,"" + row);
action.actionPerformed(event);
}
public void mousePressed(MouseEvent e)
{
if (table.isEditing() &&  table.getCellEditor() == this)
isTableCellEditorButton = true;
}
public void mouseReleased(MouseEvent e)
{
if (isTableCellEditorButton &&  table.isEditing())
table.getCellEditor().stopCellEditing();
isTableCellEditorButton = false;
}
public void mouseClicked(MouseEvent e) {}
public void mouseEntered(MouseEvent e) {}
public void mouseExited(MouseEvent e) {}
}
