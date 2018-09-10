package com.thinking.machines.dmframework.tools;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.nio.file.*;
import com.thinking.machines.dmframework.exceptions.*;
public class PropertiesPanel extends JPanel
{
private TMTable table;
private JScrollPane tableScrollPane;
private StatusBar statusBar;
private TMTableModel model;
private JToolBar toolBar;
private JLabel panelTitle;
private JPanel headerPanel;
private JButton javaButton;
public PropertiesPanel()
{
Font font=new Font("Verdana",Font.PLAIN,20);
Font titleFont=new Font("Verdana",Font.BOLD,22);
this.model=new EmptyModel();
statusBar=new StatusBar();
table=new TMTable(this.model);
tableScrollPane=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
table.setFont(font);
table.setRowHeight(30);
setLayout(new BorderLayout());
add(tableScrollPane,BorderLayout.CENTER);
add(statusBar,BorderLayout.SOUTH);
panelTitle=new JLabel(" ");
panelTitle.setIcon(null);
panelTitle.setFont(titleFont);
toolBar=new JToolBar();
javaButton=new JButton(Icons.JavaIcon);
javaButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent event)
{
if(model instanceof TablePropertiesModel)
{
try
{
POJOGenerator.generate((TablePropertiesModel)model,(Path)POJOGUI.environmentVariables(EnvironmentVariable.SOURCE_CODE_PATH));
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),"Data Layer POJO Class generated.");
}catch(DMFrameworkException dmFrameworkException)
{
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),dmFrameworkException.getMessage());
}
}

if(model instanceof ViewPropertiesModel)
{
try
{
POJOGenerator.generate((ViewPropertiesModel)model,(Path)POJOGUI.environmentVariables(EnvironmentVariable.SOURCE_CODE_PATH));
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),"Data Layer POJO Class generated.");
}catch(DMFrameworkException dmFrameworkException)
{
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),dmFrameworkException.getMessage());
}
}

}
});
toolBar.add(javaButton);
headerPanel=new JPanel();
headerPanel.setLayout(new GridLayout(2,1));
headerPanel.add(toolBar);
headerPanel.add(panelTitle);
add(headerPanel,BorderLayout.NORTH);
this.setVisible(false);
}
public void setModel(TMTableModel model)
{
if(model==null) this.model=new EmptyModel();
else  this.model=model;
this.model.setTMTable(this.table);
this.table.setModel(this.model);
this.model.fireTableDataChanged();
if(model instanceof TablePropertiesModel)
{
this.javaButton.setVisible(true);
}
if(model instanceof ColumnPropertiesModel)
{
this.javaButton.setVisible(false);
if(model.getRowCount()>0)
{
panelTitle.setText("Column");
panelTitle.setIcon(Icons.FieldIcon);
this.setVisible(true);
}
else
{
panelTitle.setText(" ");
panelTitle.setIcon(null);
this.setVisible(false);
}
}else  if(model instanceof TablePropertiesModel)
{
if(model.getRowCount()>0)
{
panelTitle.setText("Table");
panelTitle.setIcon(Icons.TableIcon);
this.setVisible(true);
}
else
{
panelTitle.setText(" ");
panelTitle.setIcon(null);
this.setVisible(false);
}
}else if(model instanceof ViewPropertiesModel)
{
this.javaButton.setVisible(true);
if(model.getRowCount()>0)
{
panelTitle.setText("View");
panelTitle.setIcon(Icons.ViewIcon);
this.setVisible(true);
}
else
{
panelTitle.setText(" ");
panelTitle.setIcon(null);
this.setVisible(false);
}
} else
{
this.setVisible(false);
}
}

class EmptyModel extends TMTableModel
{
TMTable tmTable;
public int getRowCount(){ return 0; }
public int getColumnCount() { return 0; }
public int getRowHeight() { return 20; };
public Font getFont(){ return new Font("Verdana",Font.PLAIN,20); }
public void setTMTable(TMTable tmTable){
this.tmTable=tmTable;
}
public  boolean hasCellRenderer(int row,int col) { return false; }
public boolean hasCellEditor(int row,int col) { return false; }
public TableCellEditor getCellEditor(int row,int col) { return null; }
public TableCellRenderer getCellRenderer(int row,int col) { return null;}
public Object getValueAt(int row,int col) {
return model.getValueAt(row,col);
}
}
}