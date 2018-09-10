package com.thinking.machines.dmframework.tools;
import java.util.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import com.thinking.machines.dmframework.pojo.*;
public class EnvironmentVariablesModel extends TMTableModel
{
private HashMap<Integer,TableCellEditor> editors=new HashMap<Integer,TableCellEditor>();
private ArrayList<Boolean> editable;
private Font font=new Font("Verdana",Font.PLAIN,20);
private TMTable tmTable;
private ArrayList<Pair<String,Object>> properties;
public EnvironmentVariablesModel()
{
/*
REPLACE_EXISTING_FILE
SOURCE_CODE_PATH
DEFAULT_PACKAGE
*/
this.editable=new ArrayList<Boolean>();
this.properties=new ArrayList<Pair<String,Object>>();
JTextField textField;
this.properties.add(new Pair<String,Object>("Default package name",POJOGUI.environmentVariables(EnvironmentVariable.DEFAULT_PACKAGE))); 
editable.add(true);
textField=new JTextField();
textField.setFont(font);
editors.put(properties.size()-1,new DefaultCellEditor(textField));


this.properties.add(new Pair<String,Object>("Source code path",POJOGUI.environmentVariables(EnvironmentVariable.SOURCE_CODE_PATH))); 
editable.add(true);
textField=new JTextField();
textField.setFont(font);
editors.put(properties.size()-1,new DefaultCellEditor(textField));

this.properties.add(new Pair<String,Object>("Configuration xml path",POJOGUI.environmentVariables(EnvironmentVariable.XML_PATH))); 
editable.add(true);
textField=new JTextField();
textField.setFont(font);
editors.put(properties.size()-1,new DefaultCellEditor(textField));

this.properties.add(new Pair<String,Object>("Replace existing class file",POJOGUI.environmentVariables(EnvironmentVariable.REPLACE_EXISTING_FILE))); 
editable.add(true);
JCheckBox checkBox=new JCheckBox();
checkBox.setHorizontalAlignment(SwingConstants.LEFT);
checkBox.setFont(font);
editors.put(properties.size()-1,new DefaultCellEditor(checkBox));

}
public Font getFont()
{
return font;
}
public int getRowHeight()
{
return 30;
}
public void setTMTable(TMTable tmTable)
{
this.tmTable=tmTable;
}
public int getRowCount()
{
return this.properties.size();
}
public int getColumnCount()
{
return 2;
}
public String getColumnName(int col)
{
if(col==0) return "Variable";
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
if(col==1)
{
if(row==0)  // DEFAULT_PACKAGE
{
String data=(String)value;
String pattern="^(?:\\w+|\\w+\\.\\w+)+$";
if(data.matches(pattern)==false)
{
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),data+" is not a valid package name.");
return;
}
properties.get(row).setSecond((String)value);
POJOGUI.setEnvironmentVariable(EnvironmentVariable.DEFAULT_PACKAGE,(String)value);
}
if(row==1)  // SOURCE_CODE_PATH
{
properties.get(row).setSecond(Paths.get((String)value));
POJOGUI.setEnvironmentVariable(EnvironmentVariable.SOURCE_CODE_PATH,Paths.get((String)value));
}
if(row==2)  // XML_PATH
{
properties.get(row).setSecond(Paths.get((String)value));
POJOGUI.setEnvironmentVariable(EnvironmentVariable.XML_PATH,Paths.get((String)value));
}
if(row==3)  // REPLACE_EXISTING_FILE
{
properties.get(row).setSecond((Boolean)value);
POJOGUI.setEnvironmentVariable(EnvironmentVariable.REPLACE_EXISTING_FILE,(Boolean)value);
}
}
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
JCheckBox checkBox=new JCheckBox();
checkBox.setHorizontalAlignment(SwingConstants.LEFT);
if(((Boolean)object)) checkBox.setSelected(true);
return checkBox;
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
