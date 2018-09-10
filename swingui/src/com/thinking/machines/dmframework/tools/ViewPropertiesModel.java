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
public class ViewPropertiesModel extends TMTableModel
{
private DatabaseTree databaseTree;
private TMTable tmTable;
private View view;
private ArrayList<Column> sortOrder;
private ArrayList<Column> compareOrder;

private ArrayList<Pair<String,Object>> properties;
private HashMap<Integer,TableCellEditor> editors=new HashMap<Integer,TableCellEditor>();
private ArrayList<Boolean> editable;
private Font font=new Font("Verdana",Font.PLAIN,20);
private TableCellEditorButton defaultSortOrderTableCellEditorButton;
private TableCellEditorButton uniqueIdentifierTableCellEditorButton;
private ArrayList<ColumnPropertiesModel> columnPropertiesModels;
public ViewPropertiesModel(View view)
{
this.view=view;
sortOrder=new ArrayList<Column>();
compareOrder=new ArrayList<Column>();
this.editable=new ArrayList<Boolean>();
this.properties=new ArrayList<Pair<String,Object>>();
this.properties.add(new Pair<String,Object>("Name",view.getName())); 
editable.add(false);
this.properties.add(new Pair<String,Object>("Java class name",view.getClassName())); 
editable.add(true);
JTextField textField=new JTextField();
textField.setFont(font);
editors.put(properties.size()-1,new DefaultCellEditor(textField));
String displayText=WordUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(view.getClassName())," "));
System.out.println("<ViewPropertiesModel>()--(displayText)"+displayText);

this.properties.add(new Pair<String,Object>("Display",displayText)); 
editable.add(true);
editors.put(properties.size()-1,new DefaultCellEditor(textField));

this.properties.add(new Pair<String,Object>("Uniqie identifier",Icons.ViewAndConfigureIcon)); 
editable.add(true);
uniqueIdentifierTableCellEditorButton=new TableCellEditorButton(new AbstractAction(){
public void actionPerformed(ActionEvent ev)
{
DefaultCompareOrderDialog defaultCompareOrderDialog;
defaultCompareOrderDialog=new DefaultCompareOrderDialog(POJOGUI.getMainFrame(),true,ViewPropertiesModel.this);
defaultCompareOrderDialog.setVisible(true);
}
});
editors.put(properties.size()-1,uniqueIdentifierTableCellEditorButton);

this.properties.add(new Pair<String,Object>("Default sort order",Icons.ViewAndConfigureIcon)); 
editable.add(true);
defaultSortOrderTableCellEditorButton=new TableCellEditorButton(new AbstractAction(){
public void actionPerformed(ActionEvent ev)
{
DefaultSortOrderDialog defaultSortOrderDialog;
defaultSortOrderDialog=new DefaultSortOrderDialog(POJOGUI.getMainFrame(),true,ViewPropertiesModel.this);
defaultSortOrderDialog.setVisible(true);
}
});
editors.put(properties.size()-1,defaultSortOrderTableCellEditorButton);

String packageNameText="";
this.properties.add(new Pair<String,Object>("Package name",packageNameText)); 
editable.add(true);
editors.put(properties.size()-1,new DefaultCellEditor(textField));
}
public void setColumnPropertiesModels(ArrayList<ColumnPropertiesModel> columnPropertiesModels)
{
this.columnPropertiesModels=columnPropertiesModels;
}
public Font getFont()
{
return font;
}
public int getRowHeight()
{
return 30;
}
public String getJavaClassName()
{
return (String)properties.get(1).getSecond();
}
public void setJavaClassName(String javaClassName)
{
properties.get(1).setSecond(javaClassName);
}
public String getViewDisplayText()
{
return (String)properties.get(2).getSecond();
}
public void setViewDisplayText(String tableDisplayText)
{
properties.get(2).setSecond(tableDisplayText);
}
public void setTMTable(TMTable tmTable)
{
this.tmTable=tmTable;
this.defaultSortOrderTableCellEditorButton.addMouseListener(tmTable);
this.uniqueIdentifierTableCellEditorButton.addMouseListener(tmTable);
}
public TMTable getTMTable()
{
return this.tmTable;
}
public void setDatabaseTree(DatabaseTree databaseTree)
{
this.databaseTree=databaseTree;
}
public DatabaseTree getDatabaseTree()
{
return this.databaseTree;
}
public int getRowCount()
{
if(properties==null) return 0;
return this.properties.size();
}
public int getColumnCount()
{
return 2;
}
public String getColumnName(int col)
{
if(col==0) return "Property";
return "Value";
}
public boolean isCellEditable(int row,int col)
{
if(col==0) return false;
return this.editable.get(row);
}
public Object getValueAt(int row,int col)
{
if(properties==null) return null;
if(col==0) return this.properties.get(row).getFirst();
return this.properties.get(row).getSecond();
}
public void setValueAt(Object value,int row,int col)
{
if(col==1) 
{
if(row==1)
{
if(databaseTree.isJavaClassNameMappedWithOtherView(this.view,(String)value))
{
JOptionPane.showMessageDialog(tmTable,((String)value)+" exists against another view/table as Java Class Name");
return;
}
}
if(row==2)
{
// At application level multiple tables may represent part of one entity
// hence multiple tables/view can  have same display text
/*if(databaseTree.isViewDisplayTextMappedWithOtherView(this.view,(String)value))
{
JOptionPane.showMessageDialog(tmTable,((String)value)+" exists against another view/table as Display Text");
return;
}*/
}
this.properties.get(row).setSecond(value);
}
}
public TableCellEditor getEditorComponent(int row,int cell)
{
return editors.get(row);
}
public ArrayList<Column> getSortOrder()
{
return this.sortOrder;
}
public void setSortOrder(ArrayList<Column> sortOrder)
{
this.sortOrder=sortOrder;
}
public ArrayList<Column> getCompareOrder()
{
return this.compareOrder;
}
public void setCompareOrder(ArrayList<Column> compareOrder)
{
this.compareOrder=compareOrder;
}
public java.util.List<Column> getColumnsNotInSortOrder()
{
ArrayList<Column> columnsNotInSortOrder=new ArrayList<Column>();
boolean found;
for(Column c:view.getColumns())
{
found=false;
for(Column soc:sortOrder)
{
if(c.getName().equals(soc.getName()))
{
found=true;
break;
}
}
if(!found) columnsNotInSortOrder.add(c);
}
return columnsNotInSortOrder;
}

public java.util.List<Column> getColumnsNotInCompareOrder()
{
ArrayList<Column> columnsNotInCompareOrder=new ArrayList<Column>();
boolean found;
for(Column c:view.getColumns())
{
found=false;
for(Column soc:compareOrder)
{
if(c.getName().equals(soc.getName()))
{
found=true;
break;
}
}
if(!found) columnsNotInCompareOrder.add(c);
}
return columnsNotInCompareOrder;
}
public boolean isPropertyNameMappedWithOtherColumn(Column sourceColumn,String propertyName)
{
for(ColumnPropertiesModel cpm:columnPropertiesModels)
{
if(cpm.getColumn().getName().equals(sourceColumn.getName())==false)
{
if(cpm.getPropertyName().equals(propertyName)) return true;
}
}
return false;
}
public boolean isPropertyDisplayTextMappedWithOtherColumn(Column sourceColumn,String propertyDisplayText)
{
for(ColumnPropertiesModel cpm:columnPropertiesModels)
{
if(cpm.getColumn().getName().equals(sourceColumn.getName())==false)
{
if(cpm.getPropertyDisplayText().equals(propertyDisplayText)) return true;
}
}
return false;
}
public String getDisplayName()
{
for(Pair<String,Object> pair:properties)
{
if(pair.getFirst().equals("Display")) return (String)pair.getSecond();
}
return null;
}

public View getView()
{
return view;
}
public boolean hasCellRenderer(int row,int col)
{
return true;
}
public boolean hasCellEditor(int row,int col)
{
if(col==0) return false;
return editable.get(row);
}
public TableCellRenderer getCellRenderer(int row,int col)
{
return new CellRenderer();
}
public TableCellEditor getCellEditor(int row,int col)
{
return editors.get(row);
}
public ColumnPropertiesModel getColumnPropertiesModel(int columnIndex)
{
return columnPropertiesModels.get(columnIndex);
}
public ColumnPropertiesModel getColumnPropertiesModelByColumn(Column column)
{
for(ColumnPropertiesModel cpm:columnPropertiesModels)
{
if(column.getName().equals(cpm.getColumn().getName())) return cpm;
}
return null; // this case will never arise
}
public String getPackageName()
{
for(Pair<String,Object> pair:properties)
{
if(pair.getFirst().equals("Package name")) return (String)pair.getSecond();
}
return null;
}

// inner class starts (Renderer)
public class CellRenderer implements TableCellRenderer
{
public Component getTableCellRendererComponent(JTable table,Object object,boolean isSelected,boolean hasFocus,int row,int col)
{
if((row==3 || row==4) && col==1)
{
return getCellEditor(row,col).getTableCellEditorComponent(table,object,hasFocus,row,col);
}
JLabel label=new JLabel();
if(object instanceof Boolean)
{
if(((Boolean)object))
label.setIcon(Icons.CheckedCheckBoxIcon);
else
label.setIcon(Icons.UncheckedCheckBoxIcon);
label.setHorizontalAlignment(SwingConstants.CENTER);
}
else
{
if(object!=null)
label.setText(object.toString());
else
label.setText("null");
label.setHorizontalAlignment(SwingConstants.LEFT);
}
label.setFont(font);
if(isCellEditable(row,col)) label.setIcon(Icons.EditableIcon);
return label;
}
}
// inner class ends (Renderer)
}
