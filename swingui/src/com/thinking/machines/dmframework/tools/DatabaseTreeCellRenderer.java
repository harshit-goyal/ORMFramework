package com.thinking.machines.dmframework.tools;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import javax.swing.border.*;
import com.thinking.machines.dmframework.pojo.*;
public class DatabaseTreeCellRenderer implements TreeCellRenderer 
{
private JLabel label;
public DatabaseTreeCellRenderer() 
{
label=new JLabel("");
label.setFont(new Font("Verdana",Font.PLAIN,18));
label.setOpaque(true);
}
public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,boolean leaf, int row, boolean hasFocus)
{
if(value instanceof Database) 
{
Database database=(Database)value;
String databaseName=database.getName();
if(databaseName==null || databaseName.length()==0)
{
databaseName="(Not connected)";
}
else
{
databaseName=databaseName+" ("+database.getArchitecture()+")";
}
label.setIcon(Icons.DatabaseIcon);
label.setText(databaseName);
}
if(value instanceof TableNode)
{
label.setIcon(Icons.TablesIcon);
System.out.println("<DatabaseTreeCellRenderer>--<getTreeCellRendererComponent>--(value.toString())"+value.toString());
label.setText(value.toString());
}
if(value instanceof ViewNode)
{
label.setIcon(Icons.ViewsIcon);
label.setText(value.toString());
}
if(value instanceof SequenceNode)
{
label.setIcon(null);
label.setText(value.toString());
}
if(value instanceof Sequence)
{
label.setIcon(null);
label.setText(value.toString());
}
if(value instanceof Table)
{
Table table=(Table)value;
label.setIcon(Icons.TableIcon);
System.out.println("<DatabaseTreeCellRenderer>--<getTreeCellRendererComponent>--(if-(value instanceof Table))-(table.getName())"+table.getName());
label.setText(table.getName());
}
if(value instanceof View)
{
View view=(View)value;
label.setIcon(Icons.ViewIcon);
label.setText(view.getName());
}
if(value instanceof Column)
{
Column column=(Column)value;
if(column.isInTable() && column.isPartOfPrimaryKey())
{
label.setIcon(Icons.PrimaryKeyIcon);
}
else
{
label.setIcon(Icons.FieldIcon);
}
System.out.println("<DatabaseTreeCellRenderer>--<getTreeCellRendererComponent>--(column.getName())"+column.getName());
label.setText(column.getName());
}
if(selected)
{
label.setBackground(new Color(225,225,225));
Border paddingBorder = BorderFactory.createEmptyBorder(2,10,2,75);
Border border = BorderFactory.createLineBorder(new Color(160,160,160));
label.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
}
else
{
label.setBackground(Color.white);
Border paddingBorder = BorderFactory.createEmptyBorder(2,10,2,75);
Border border = BorderFactory.createLineBorder(Color.white);
label.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
}
return label;
}
}