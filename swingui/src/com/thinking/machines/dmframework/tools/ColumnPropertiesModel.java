package com.thinking.machines.dmframework.tools;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.*;
import java.util.*;
import javax.swing.table.*;
import com.thinking.machines.dmframework.pojo.*;
import com.thinking.machines.dmframework.utilities.*;
import com.thinking.machines.dmframework.exceptions.*;
import javax.swing.*;
import java.awt.*;
public class ColumnPropertiesModel extends TMTableModel
{
private Column column;
private ArrayList<Pair<String,Object>> properties;
private HashMap<Integer,TableCellEditor> editors=new HashMap<Integer,TableCellEditor>();
private ArrayList<Boolean> editable;
private Font font=new Font("Verdana",Font.PLAIN,20);
private TMTable tmTable;
private TablePropertiesModel tablePropertiesModel;
private ViewPropertiesModel viewPropertiesModel;
public ColumnPropertiesModel(Column column)
{
this.column=column;
this.editable=new ArrayList<Boolean>();
this.properties=new ArrayList<Pair<String,Object>>();
this.properties.add(new Pair<String,Object>("Name",column.getName())); 
editable.add(false);
try
{
this.properties.add(new Pair<String,Object>("Data type",Types.getDataType(column.getType()).getPrimitiveType().getSimpleName())); 
editable.add(false);
}catch(DMFrameworkException dmFrameworkException)
{
this.properties.add(new Pair<String,Object>("Java type",column.getProperty().getType().getName())); 
editable.add(false);
}
this.properties.add(new Pair<String,Object>("Java property name",column.getProperty().getName())); 
editable.add(true);
JTextField textField=new JTextField();
textField.setFont(font);
editors.put(properties.size()-1,new DefaultCellEditor(textField));
String displayText=WordUtils.uncapitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(column.getProperty().getName())," "));
System.out.println("<ColumnPropertiesModel>--(displayText)@"+displayText);
this.properties.add(new Pair<String,Object>("Display",displayText)); 
editable.add(true);
editors.put(properties.size()-1,new DefaultCellEditor(textField));
this.properties.add(new Pair<String,Object>("Java type",column.getProperty().getType().getName())); 
editable.add(false);
if(column.isInTable())
{
if(column.getArchitecture().equalsIgnoreCase("mysql"))
{
this.properties.add(new Pair<String,Object>("Auto generated",column.getIsAutoIncrementEnabled())); 
editable.add(false);
}
if(column.getArchitecture().equalsIgnoreCase("oracle 11g"))
{
if(Types.isNumeric(column.getProperty().getType()))
{
this.properties.add(new Pair<String,Object>("Sequence","")); 
editable.add(true);
JComboBox comboBox=new JComboBox();
comboBox.setFont(font);
for(String sequenceName:column.getTable().getDatabase().getSequences())
{
comboBox.addItem(sequenceName);
}
editors.put(properties.size()-1,new DefaultCellEditor(comboBox));
}
}
this.properties.add(new Pair<String,Object>("Primary key",column.isPartOfPrimaryKey())); 
editable.add(false);
this.properties.add(new Pair<String,Object>("Unique",column.getIsUnique())); 
editable.add(false);
this.properties.add(new Pair<String,Object>("Allow null",column.getIsNullable())); 
editable.add(false);
this.properties.add(new Pair<String,Object>("Default value",column.getDefaultValue())); 
editable.add(false);
}
this.properties.add(new Pair<String,Object>("Width",column.getWidth())); 
editable.add(false);
if(column.getProperty().getType().equals(java.math.BigDecimal.class))
{
this.properties.add(new Pair<String,Object>("Decimal precision",column.getDecimalPrecision())); 
editable.add(false);
}
}
public Font getFont()
{
return font;
}
public int getRowHeight()
{
return 30;
}
public void setTablePropertiesModel(TablePropertiesModel tablePropertiesModel)
{
this.tablePropertiesModel=tablePropertiesModel;
}
public TablePropertiesModel getTablePropertiesModel()
{
return tablePropertiesModel;
}
public void setViewPropertiesModel(ViewPropertiesModel viewPropertiesModel)
{
this.viewPropertiesModel=viewPropertiesModel;
}
public ViewPropertiesModel getViewPropertiesModel()
{
return viewPropertiesModel;
}
public String getPropertyName()
{
return (String)properties.get(2).getSecond();
}
public void setPropertyName(String propertyName)
{
System.out.println("<ColumnPropertiesModel>--<setPropertyName>--(propertyName)"+propertyName);
properties.get(2).setSecond(propertyName);
column.getProperty().setName(propertyName);
tablePropertiesModel.getTable().getColumn(column.getName()).getProperty().setName(propertyName);
}
public String getPropertyDisplayText()
{
return (String)properties.get(3).getSecond();
}
public void setPropertyDisplayText(String propertyDisplayText)
{
System.out.println("<ColumnPropertiesModel>--<setPropertyDisplayName>--(propertyDisplayName)"+propertyDisplayText);
properties.get(3).setSecond(propertyDisplayText);
column.setDisplayName(propertyDisplayText);
tablePropertiesModel.getTable().getColumn(column.getName()).setDisplayName(propertyDisplayText);
}
public void setTMTable(TMTable tmTable)
{
this.tmTable=tmTable;
}
public int getRowCount()
{
return this.properties.size();
}
public String getDisplayName()
{
for(Pair<String,Object> pair:properties)
{
if(pair.getFirst().equals("Display")) return (String)pair.getSecond();
}
return null;
}
public String getSequenceName()
{
for(Pair<String,Object> pair:properties)
{
if(pair.getFirst().equals("Sequence")) return (String)pair.getSecond();
}
return null;
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
if(col==0) return this.properties.get(row).getFirst();
return this.properties.get(row).getSecond();
}
public void setValueAt(Object value,int row,int col)
{
System.out.println("<columnPropertiesMOdel>--(setValueAt)--(value)"+value);
if(col==1 && this.tablePropertiesModel!=null) 
{
if(row==2)
{
if(tablePropertiesModel.isPropertyNameMappedWithOtherColumn(this.column,(String)value))
{
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),((String)value)+" exists against another column as Java Property");
return;
}
if(col==1 && row==2) this.setPropertyName((String)value);
}
if(row==3)
{
if(tablePropertiesModel.isPropertyDisplayTextMappedWithOtherColumn(this.column,(String)value))
{
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),((String)value)+" exists against another column as Display Text");
return;
}
if(col==1 && row==3) this.setPropertyDisplayText((String)value);
}
}
if(col==1 && this.viewPropertiesModel!=null) 
{
if(row==2)
{
if(viewPropertiesModel.isPropertyNameMappedWithOtherColumn(this.column,(String)value))
{
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),((String)value)+" exists against another column as Java Property");
return;
}
}
if(row==3)
{
if(viewPropertiesModel.isPropertyDisplayTextMappedWithOtherColumn(this.column,(String)value))
{
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),((String)value)+" exists against another column as Display Text");
return;
}
}
}
this.properties.get(row).setSecond(value);
}
public Column getColumn()
{
return this.column;
}
public boolean hasCellEditor(int row,int col)
{
if(col==0) return false;
return editable.get(row);
}
public boolean hasCellRenderer(int row,int col)
{
return true;
}
public TableCellRenderer getCellRenderer(int row,int cell)
{
return new CellRenderer();
}
public TableCellEditor getCellEditor(int row,int cell)
{
return editors.get(row);
}
// inner class starts (Renderer)
public class CellRenderer implements TableCellRenderer
{
public Component getTableCellRendererComponent(JTable table,Object object,boolean isSelected,boolean hasFocus,int row,int col)
{
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
{
label.setText(object.toString());
System.out.println("<Column	PropertiesModel>--<inner<CellRenerer>>--(getColumnCellEditorComponent)--(object) at column "+col+" "+object.toString());

}else
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
