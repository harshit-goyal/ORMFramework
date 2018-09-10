package com.thinking.machines.dmframework.tools;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import javax.swing.tree.*;
import java.awt.event.*;
import com.thinking.machines.dmframework.pojo.*;
import com.thinking.machines.dmframework.exceptions.*;
import javax.swing.table.*;
import java.nio.file.*;
public class DatabaseTree extends JTree implements ActionListener
{
private HashMap<Column,ColumnPropertiesModel> columnPropertiesModelHashMap;
private HashMap<Table,TablePropertiesModel> tablePropertiesModelHashMap;
private HashMap<View,ViewPropertiesModel> viewPropertiesModelHashMap;
private DatabaseBean databaseBean;
private DatabaseTreeModel databaseTreeModel;
private DatabaseTreeCellRenderer databaseTreeCellRenderer;
private JPopupMenu connectionPopup;
private JMenuItem connectPopupItem;
private boolean connected=false;
private DatabaseTreeNotificationListener databaseTreeNotificationListener;
public DatabaseTree(DatabaseTreeModel databaseTreeModel,DatabaseTreeNotificationListener databaseTreeNotificationListener)
{
super(databaseTreeModel);
this.columnPropertiesModelHashMap=new HashMap<Column,ColumnPropertiesModel>();
this.tablePropertiesModelHashMap=new HashMap<Table,TablePropertiesModel>();
this.viewPropertiesModelHashMap=new HashMap<View,ViewPropertiesModel>();

this.databaseTreeNotificationListener=databaseTreeNotificationListener;
this.databaseTreeNotificationListener.updateStatus("Not connected",false);
this.databaseBean=new DatabaseBean();
this.databaseTreeModel=databaseTreeModel;
databaseTreeCellRenderer=new DatabaseTreeCellRenderer();
setCellRenderer(databaseTreeCellRenderer);
connectionPopup=new JPopupMenu();
connectPopupItem=new JMenuItem("Connect");
connectPopupItem.setFont(new Font("Verdana",Font.PLAIN,22));
connectPopupItem.addActionListener(this);
connectionPopup.add(connectPopupItem);
addMouseListener(new MouseAdapter(){
public void mousePressed(MouseEvent mouseEvent)
{
int selectedRow=DatabaseTree.this.getRowForLocation(mouseEvent.getX(),mouseEvent.getY()+5);
if(selectedRow==-1) return;
if(SwingUtilities.isLeftMouseButton(mouseEvent))
{
DatabaseTree.this.setSelectionRow(selectedRow);
TreePath selectedPath=DatabaseTree.this.getPathForLocation(mouseEvent.getX(),mouseEvent.getY());
Object selectedNode=DatabaseTree.this.getLastSelectedPathComponent();
if(selectedNode instanceof Column)
{
Column column=(Column)selectedNode;
databaseTreeNotificationListener.updatePropertiesModel(columnPropertiesModelHashMap.get(column));
}else if(selectedNode instanceof Table)
{
Table table=(Table)selectedNode;
databaseTreeNotificationListener.updatePropertiesModel(tablePropertiesModelHashMap.get(table));
}else if(selectedNode instanceof View)
{
View view=(View)selectedNode;
databaseTreeNotificationListener.updatePropertiesModel(viewPropertiesModelHashMap.get(view));
}else
{
databaseTreeNotificationListener.clearPropertiesModel();
}
}

if(SwingUtilities.isRightMouseButton(mouseEvent))
{
if(selectedRow!=-1)
{
DatabaseTree.this.setSelectionRow(selectedRow);
TreePath selectedPath=DatabaseTree.this.getPathForLocation(mouseEvent.getX(),mouseEvent.getY());
Object selectedNode=DatabaseTree.this.getLastSelectedPathComponent();
if(selectedNode instanceof Database)
{
connectionPopup.show((JComponent)mouseEvent.getSource(),mouseEvent.getX(),mouseEvent.getY());
}
}
}
}
});
}
public void actionPerformed(ActionEvent ev)
{
if(ev.getSource()==connectPopupItem)
{
if(this.connected==true)
{
this.databaseTreeModel.setArchitecture(null);
this.databaseTreeModel.setDatabase(new Database());
this.databaseBean.setDatabaseConfiguration(null);
this.columnPropertiesModelHashMap.clear();
this.tablePropertiesModelHashMap.clear();
this.viewPropertiesModelHashMap.clear();
this.connected=false;
databaseTreeNotificationListener.updateStatus("Not connected",false);
return;
}
DatabaseConnectionDialog databaseConnectionDialog;
databaseConnectionDialog=new DatabaseConnectionDialog();
Database database=databaseConnectionDialog.getDatabase();
if(database!=null)
{
HashMap<String,String> databaseConfiguration=databaseConnectionDialog.getDatabaseConfiguration();
this.databaseTreeModel.setArchitecture(databaseConfiguration.get("architecture"));
this.databaseTreeModel.setDatabase(database);
this.databaseBean.setDatabaseConfiguration(databaseConfiguration);
TablePropertiesModel tablePropertiesModel;
ColumnPropertiesModel columnPropertiesModel;
ArrayList<ColumnPropertiesModel> columnPropertiesModelsForTable;
for(Table t:database.getTables())
{
columnPropertiesModelsForTable=new ArrayList<ColumnPropertiesModel>();
tablePropertiesModel=new TablePropertiesModel(t);
tablePropertiesModel.setDatabaseTree(this);
tablePropertiesModelHashMap.put(t,tablePropertiesModel);
for(Column c:t.getColumns())
{
columnPropertiesModel=new ColumnPropertiesModel(c);
columnPropertiesModel.setTablePropertiesModel(tablePropertiesModel);
columnPropertiesModelHashMap.put(c,columnPropertiesModel);
columnPropertiesModelsForTable.add(columnPropertiesModel);
}
tablePropertiesModel.setColumnPropertiesModels(columnPropertiesModelsForTable);
}
ViewPropertiesModel viewPropertiesModel;
ArrayList<ColumnPropertiesModel> columnPropertiesModelsForView;
for(View v:database.getViews())
{
columnPropertiesModelsForView=new ArrayList<ColumnPropertiesModel>();
viewPropertiesModel=new ViewPropertiesModel(v);
viewPropertiesModel.setDatabaseTree(this);
viewPropertiesModelHashMap.put(v,viewPropertiesModel);
for(Column c:v.getColumns())
{
columnPropertiesModel=new ColumnPropertiesModel(c);
columnPropertiesModel.setViewPropertiesModel(viewPropertiesModel);
columnPropertiesModelHashMap.put(c,columnPropertiesModel);
columnPropertiesModelsForView.add(columnPropertiesModel);
}
viewPropertiesModel.setColumnPropertiesModels(columnPropertiesModelsForView);
}
this.databaseTreeNotificationListener.updateStatus("Connected to "+databaseConfiguration.get("architecture")+" database using : "+databaseConfiguration.get("connectionString"),true);
this.connected=true;
connectPopupItem.setText("Disconnect");
}
databaseConnectionDialog.dispose();
}
}
private void raiseColumnSelectedEvent(Column column)
{
}
public boolean isJavaClassNameMappedWithOtherTable(Table sourceTable,String javaClassName)
{
if(sourceTable!=null && isJavaClassNameMappedWithOtherView(null,javaClassName))
{
return true;
}
Iterator<Map.Entry<Table,TablePropertiesModel>> iterator;
Map.Entry<Table,TablePropertiesModel> pair;
iterator=tablePropertiesModelHashMap.entrySet().iterator();
TablePropertiesModel tpm;
while(iterator.hasNext())
{
pair=iterator.next();
tpm=pair.getValue();
if(sourceTable==null || tpm.getTable().getName().equals(sourceTable.getName())==false)
{
if(tpm.getJavaClassName().equals(javaClassName)) return true;
}
}
return false;
}
public boolean isTableDisplayTextMappedWithOtherTable(Table sourceTable,String tableDisplayText)
{
if(sourceTable!=null && isViewDisplayTextMappedWithOtherView(null,tableDisplayText))
{
return true;
}
Iterator<Map.Entry<Table,TablePropertiesModel>> iterator;
Map.Entry<Table,TablePropertiesModel> pair;
iterator=tablePropertiesModelHashMap.entrySet().iterator();
TablePropertiesModel tpm;
while(iterator.hasNext())
{
pair=iterator.next();
tpm=pair.getValue();
if(sourceTable==null || tpm.getTable().getName().equals(sourceTable.getName())==false)
{
if(tpm.getTableDisplayText().equals(tableDisplayText)) return true;
}
}
return false;
}

public boolean isJavaClassNameMappedWithOtherView(View sourceView,String javaClassName)
{
if(sourceView!=null && isJavaClassNameMappedWithOtherTable(null,javaClassName))
{
return true;
}
Iterator<Map.Entry<View,ViewPropertiesModel>> iterator;
Map.Entry<View,ViewPropertiesModel> pair;
iterator=viewPropertiesModelHashMap.entrySet().iterator();
ViewPropertiesModel vpm;
while(iterator.hasNext())
{
pair=iterator.next();
vpm=pair.getValue();
if(sourceView==null || vpm.getView().getName().equals(sourceView.getName())==false)
{
if(vpm.getJavaClassName().equals(javaClassName)) return true;
}
}
return false;
}
public boolean isViewDisplayTextMappedWithOtherView(View sourceView,String viewDisplayText)
{
if(sourceView!=null && isTableDisplayTextMappedWithOtherTable(null,viewDisplayText))
{
return true;
}
Iterator<Map.Entry<View,ViewPropertiesModel>> iterator;
Map.Entry<View,ViewPropertiesModel> pair;
iterator=viewPropertiesModelHashMap.entrySet().iterator();
ViewPropertiesModel vpm;
while(iterator.hasNext())
{
pair=iterator.next();
vpm=pair.getValue();
if(sourceView==null || vpm.getView().getName().equals(sourceView.getName())==false)
{
if(vpm.getViewDisplayText().equals(viewDisplayText)) return true;
}
}
return false;
}
public void generateConfigurationXML() 
{
try
{
XMLGenerator.generate(databaseBean,tablePropertiesModelHashMap,viewPropertiesModelHashMap,(Path)POJOGUI.environmentVariables(EnvironmentVariable.XML_PATH));
}catch(DMFrameworkException dmFrameworkException)
{
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),dmFrameworkException.getMessage());
return;
}
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),"Configuration file : TMDMFramework.xml created.");
}
public void generatePOJOClasses()
{
Map.Entry<Table,TablePropertiesModel> tablePropertiesModelHashMapPair;
Iterator<Map.Entry<Table,TablePropertiesModel>> tablePropertiesModelIterator;
tablePropertiesModelIterator=tablePropertiesModelHashMap.entrySet().iterator();
while(tablePropertiesModelIterator.hasNext())
{
tablePropertiesModelHashMapPair=tablePropertiesModelIterator.next();
try
{
POJOGenerator.generate((TablePropertiesModel)tablePropertiesModelHashMapPair.getValue(),(Path)POJOGUI.environmentVariables(EnvironmentVariable.SOURCE_CODE_PATH));
}catch(DMFrameworkException dmFrameworkException)
{
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),dmFrameworkException.getMessage());
return;
}
}

Map.Entry<View,ViewPropertiesModel> viewPropertiesModelHashMapPair;
Iterator<Map.Entry<View,ViewPropertiesModel>> viewPropertiesModelIterator;
viewPropertiesModelIterator=viewPropertiesModelHashMap.entrySet().iterator();
while(viewPropertiesModelIterator.hasNext())
{
viewPropertiesModelHashMapPair=viewPropertiesModelIterator.next();
try
{
POJOGenerator.generate((ViewPropertiesModel)viewPropertiesModelHashMapPair.getValue(),(Path)POJOGUI.environmentVariables(EnvironmentVariable.SOURCE_CODE_PATH));
}catch(DMFrameworkException dmFrameworkException)
{
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),dmFrameworkException.getMessage());
return;
}
JOptionPane.showMessageDialog(POJOGUI.getMainFrame(),"Data Layer POJO Class generated.");
}
}
}