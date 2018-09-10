package com.thinking.machines.dmframework.tools;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
public class TMTable extends JTable
{
private TMTableModel model;
public TMTable(TMTableModel model)
{
this.model=model;
this.model.setTMTable(this);
this.setModel(model);
getTableHeader().setFont(model.getHeaderFont());
setFont(model.getFont());
setRowHeight(model.getRowHeight());
getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
updateColumnWidths();
}
public void setModel(TMTableModel model)
{
this.model=model;
super.setModel(this.model);
}
public TableCellEditor getCellEditor(int row,int col)
{
if(model.hasCellEditor(row,col))
{
return model.getCellEditor(row,col);
}
return super.getCellEditor(row,col);
}
public TableCellRenderer getCellRenderer(int row,int col)
{
if(model.hasCellRenderer(row,col))
{
return model.getCellRenderer(row,col);
}
return super.getCellRenderer(row,col);
}
public void updateColumnWidths()
{
for(int i=0;i<model.getColumnCount();i++)
{
getColumnModel().getColumn(i).setPreferredWidth(model.getColumnWidth(i));
}
}
}