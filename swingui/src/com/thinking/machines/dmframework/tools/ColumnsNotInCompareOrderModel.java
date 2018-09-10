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
public class ColumnsNotInCompareOrderModel extends TMTableModel implements MouseListener
{
private ViewPropertiesModel viewPropertiesModel;
private ColumnsInCompareOrderModel columnsInCompareOrderModel;
private TMTable tmTable;
private TMTable columnsInCompareOrderTMTable;
private Table table;
private Font font=new Font("Verdana",Font.PLAIN,20);
public ColumnsNotInCompareOrderModel(ViewPropertiesModel viewPropertiesModel)
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
return this.viewPropertiesModel.getColumnsNotInCompareOrder().size();
}
public int getColumnCount()
{
return 3;
}
public String getColumnName(int col)
{
if(col==1) return "Properties not in unique identifier "; else return " ";
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
if(col==1) return viewPropertiesModel.getColumnPropertiesModelByColumn(viewPropertiesModel.getColumnsNotInCompareOrder().get(row)).getValueAt(2,1);
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
columnsInCompareOrderModel.fireTableStructureChanged();
columnsInCompareOrderModel.fireTableDataChanged();
columnsInCompareOrderTMTable.updateColumnWidths();
}
private void removeRow(int row)
{
viewPropertiesModel.getCompareOrder().add(viewPropertiesModel.getColumnsNotInCompareOrder().get(row));
updateTable();
}
public void setColumnsInCompareOrderModel(ColumnsInCompareOrderModel columnsInCompareOrderModel)
{
this.columnsInCompareOrderModel=columnsInCompareOrderModel;
}
public void setColumnsInCompareOrderTMTable(TMTable columnsInCompareOrderTMTable)
{
this.columnsInCompareOrderTMTable=columnsInCompareOrderTMTable;
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
/*if(tablePropertiesModel!=null && tablePropertiesModel.getColumnsNotInCompareOrder().get(row).isPartOfPrimaryKey())
{
label.setIcon(Icons.PrimaryKeyIcon);
}*/
}
}
return label;
}
}
// inner class ends (Renderer)
}
