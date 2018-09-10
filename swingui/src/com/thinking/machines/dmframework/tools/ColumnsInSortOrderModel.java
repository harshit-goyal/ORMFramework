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
public class ColumnsInSortOrderModel extends TMTableModel implements MouseListener
{
private TablePropertiesModel tablePropertiesModel;
private ViewPropertiesModel viewPropertiesModel;
private ColumnsNotInSortOrderModel columnsNotInSortOrderModel;
private TMTable tmTable;
private Table table;
private Font font=new Font("Verdana",Font.PLAIN,20);
public ColumnsInSortOrderModel(TablePropertiesModel tablePropertiesModel)
{
this.tablePropertiesModel=tablePropertiesModel;
}
public ColumnsInSortOrderModel(ViewPropertiesModel viewPropertiesModel)
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
return this.tablePropertiesModel.getSortOrder().size();
else
return this.viewPropertiesModel.getSortOrder().size();
}
public int getColumnCount()
{
if(tablePropertiesModel!=null)
{
if(tablePropertiesModel.getSortOrder().size()<=1) return 3;
return 5;
}
else
{
if(viewPropertiesModel.getSortOrder().size()<=1) return 3;
return 5;
}
}
public String getColumnName(int col)
{
if(col==1) return "Properties in default sort order"; else return " ";
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
if(tablePropertiesModel!=null)
{
if(col==1) return tablePropertiesModel.getColumnPropertiesModelByColumn(tablePropertiesModel.getSortOrder().get(row)).getValueAt(2,1);
}
else
{
if(col==1) return viewPropertiesModel.getColumnPropertiesModelByColumn(viewPropertiesModel.getSortOrder().get(row)).getValueAt(2,1);
}
if(col==2 && getRowCount()==1)
{
return Icons.RemoveIcon;
}
if(col==2) 
{
if(row==0) return " ";
return Icons.UpIcon;
}
if(col==3) 
{
if(row==getRowCount()-1) return " ";
return Icons.DownIcon;
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
if(getRowCount()==1)
{
if(col==2)   removeRow(row);  
}
else
{
if(col==4)  removeRow(row); 
if(col==2)  swapRows(row-1,row); 
if(col==3)  swapRows(row,row+1); 
}
}
private void updateTable()
{
fireTableStructureChanged();
fireTableDataChanged();
tmTable.updateColumnWidths();
columnsNotInSortOrderModel.fireTableDataChanged();
}
private void removeRow(int row)
{
if(tablePropertiesModel!=null)
{
tablePropertiesModel.getSortOrder().remove(row);
}
else
{
viewPropertiesModel.getSortOrder().remove(row);
}
updateTable();
}
private void swapRows(int eRow,int fRow)
{
ArrayList<Column> sortOrder;
if(tablePropertiesModel!=null)
{
sortOrder=tablePropertiesModel.getSortOrder();
}
else
{
sortOrder=viewPropertiesModel.getSortOrder();
}
Column fColumn=sortOrder.get(fRow);
sortOrder.remove(fRow);
sortOrder.add(eRow,fColumn);
updateTable();
}
public void setColumnsNotInSortOrderModel(ColumnsNotInSortOrderModel columnsNotInSortOrderModel)
{
this.columnsNotInSortOrderModel=columnsNotInSortOrderModel;
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
if(tablePropertiesModel!=null && tablePropertiesModel.getSortOrder().get(row).isPartOfPrimaryKey())
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
