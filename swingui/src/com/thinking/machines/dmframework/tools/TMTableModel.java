package com.thinking.machines.dmframework.tools;
import javax.swing.table.*;
import java.awt.*;
abstract public class TMTableModel extends AbstractTableModel
{
public abstract boolean hasCellRenderer(int row,int col);
abstract public boolean hasCellEditor(int row,int col);
abstract public TableCellEditor getCellEditor(int row,int col);
abstract public TableCellRenderer getCellRenderer(int row,int col);
abstract public void setTMTable(TMTable tmTable);
public Font getFont()
{
Font font=new Font("Verdana",Font.PLAIN,20);
return font;
}
public Font getHeaderFont()
{
Font font=new Font("Verdana",Font.BOLD,18);
return font;
}
public int getColumnWidth(int columnIndex)
{
return 100;
}
public int getRowHeight()
{
return 30;
}
}