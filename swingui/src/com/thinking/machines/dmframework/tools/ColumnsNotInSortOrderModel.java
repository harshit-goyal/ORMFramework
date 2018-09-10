package com.thinking.machines.dmframework.tools;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.table.*;
import com.thinking.machines.dmframework.pojo.*;
import com.thinking.machines.dmframework.utilities.*;
import com.thinking.machines.dmframework.exceptions.*;
import javax.swing.*;
import java.awt.*;
public class ColumnsNotInSortOrderModel extends TMTableModel implements MouseListener
{
private TablePropertiesModel tablePropertiesModel;
private ViewPropertiesModel viewPropertiesModel;
private ColumnsInSortOrderModel columnsInSortOrderModel;
private TMTable tmTable;
private TMTable columnsInSortOrderTMTable;
private Table table;
private Font font=new Font("Verdana",Font.PLAIN,20);
public ColumnsNotInSortOrderModel(TablePropertiesModel tablePropertiesModel)
{
this.tablePropertiesModel=tablePropertiesModel;
}
public ColumnsNotInSortOrderModel(ViewPropertiesModel viewPropertiesModel)
{
this.viewPropertiesModel=viewPropertiesModel;
}
public void setTMTable(TMTable tmTable)
{
this.tmTable=tmTable;
this.tmTable.addMouseListener(this);
}
public TMTable getTMTable()
{
return this.tmTable;
}
public Font getHeaderFont()
{
Font font=new Font("Verdana",Font.BOLD,16);
return font;
}
public int getRowCount()
{
if(this.tablePropertiesModel!=null)
return this.tablePropertiesModel.getColumnsNotInSortOrder().size();
else
return this.viewPropertiesModel.getColumnsNotInSortOrder().size();
}
public int getColumnCount()
{
return 3;
}
public String getColumnName(int col)
{
if(col==1) return "Properties not in sort order "; else return " ";
}
public int getColumnWidth(int col)
{
if(col==1) return 400;
else
return 70;
}
public boolean isCellEditable(int row,int col)
{
return false;
}
public Object getValueAt(int row,int col)
{
if(col==0) return new Integer(row+1);
if(this.tablePropertiesModel!=null)
{
if(col==1) return tablePropertiesModel.getColumnPropertiesModelByColumn(tablePropertiesModel.getColumnsNotInSortOrder().get(row)).getValueAt(2,1);
}
else
{
if(col==1) return viewPropertiesModel.getColumnPropertiesModelByColumn(viewPropertiesModel.getColumnsNotInSortOrder().get(row)).getValueAt(2,1);
}
return Icons.RemoveIcon;
}
public void setValueAt(Object value,int row,int col)
{
// do nothing
}
public TableCellEditor getEditorComponent(int row,int cell)
{
return null;
}
public Table getTable()
{
return table;
}
public boolean hasCellRenderer(int row,int col)
{
return true;
}
public boolean hasCellEditor(int row,int col)
{
return false;
}
public TableCellRenderer getCellRenderer(int row,int col)
{
return new CellRenderer();
}
public TableCellEditor getCellEditor(int row,int col)
{
return null;
}
public  void mouseClicked(MouseEvent event)
{
int row=tmTable.rowAtPoint(event.getPoint());
int col=tmTable.columnAtPoint(event.getPoint());
if(col==2)   removeRow(row);  
}
private void updateTable()
{
fireTableDataChanged();
columnsInSortOrderModel.fireTableStructureChanged();
columnsInSortOrderModel.fireTableDataChanged();
columnsInSortOrderTMTable.updateColumnWidths();
}
private void removeRow(int row)
{
if(tablePropertiesModel!=null)
tablePropertiesModel.getSortOrder().add(tablePropertiesModel.getColumnsNotInSortOrder().get(row));
else
viewPropertiesModel.getSortOrder().add(viewPropertiesModel.getColumnsNotInSortOrder().get(row));
updateTable();
}
public void setColumnsInSortOrderModel(ColumnsInSortOrderModel columnsInSortOrderModel)
{
this.columnsInSortOrderModel=columnsInSortOrderModel;
}
public void setColumnsInSortOrderTMTable(TMTable columnsInSortOrderTMTable)
{
this.columnsInSortOrderTMTable=columnsInSortOrderTMTable;
}
public  void mousePressed(MouseEvent event){}
public  void mouseReleased(MouseEvent event){}
public  void mouseEntered(MouseEvent event){}
public  void mouseExited(MouseEvent event){}

// inner class starts (Renderer)
public class CellRenderer implements TableCellRenderer
{
public Component getTableCellRendererComponent(JTable table,Object object,boolean isSelected,boolean hasFocus,int row,int col)
{
JLabel label=new JLabel();
label.setFont(font);
if(object instanceof ImageIcon)
{
ImageIcon imageIcon=(ImageIcon)object;
label.setIcon(imageIcon);
label.setHorizontalAlignment(SwingConstants.CENTER);
return label;
}
label.setText(object.toString());
if(col==0)
{
label.setHorizontalAlignment(SwingConstants.RIGHT);
}
else
{
label.setHorizontalAlignment(SwingConstants.LEFT);
if(col==1)
{
if(tablePropertiesModel!=null && tablePropertiesModel.getColumnsNotInSortOrder().get(row).isPartOfPrimaryKey())
{
label.setIcon(Icons.PrimaryKeyIcon);
}
}
}
return label;
}
}
// inner class ends (Renderer)
}
