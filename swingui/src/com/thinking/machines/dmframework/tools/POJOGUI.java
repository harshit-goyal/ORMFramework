package com.thinking.machines.dmframework.tools;
import com.thinking.machines.dmframework.pojo.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.math.*;
import javax.swing.table.*;
import java.nio.file.*;
public class POJOGUI extends JFrame implements DatabaseTreeNotificationListener
{
private static HashMap<EnvironmentVariable,Object> environmentVariables;
private static JFrame mainFrame;
private JToolBar mainToolBar;
private JButton saveButton;
private JButton javaButton;
private JButton createConfigurationXMLButton;
private DatabaseBean databaseBean;
private DatabaseTreeModel databaseTreeModel;
private DatabaseTree databaseTree;
private JScrollPane databaseTreeScrollPane;
private Container container;
private Dimension desktopSize;
private StatusBar statusBar;
private PropertiesPanel propertiesPanel;
private JTabbedPane tabbedPane;
private TMTable environmentVariablesTable;
private EnvironmentVariablesModel environmentVariablesModel;
private JPanel pojoSettingsPanel;
private JPanel environmentVariablesSettingsPanel;
static
{
environmentVariables=new HashMap<EnvironmentVariable,Object>();
environmentVariables.put(EnvironmentVariable.REPLACE_EXISTING_FILE,new Boolean(false));
environmentVariables.put(EnvironmentVariable.SOURCE_CODE_PATH,Paths.get("src"));
environmentVariables.put(EnvironmentVariable.XML_PATH,Paths.get("xml"));
environmentVariables.put(EnvironmentVariable.DEFAULT_PACKAGE,"");
}
public POJOGUI()
{
setTitle("Thinking Machines - ORM - POJO Tool");
mainFrame=this;
initComponents();
setDefaultCloseOperation(EXIT_ON_CLOSE);
System.out.println(Icons.LogoIcon);
setIconImage(Icons.LogoIcon.getImage());
addListeners();
}
private void initComponents()
{
UIManager.put("Tree.openIcon",Icons.ExpandIcon);
container=getContentPane();
container.setLayout(new BorderLayout());
container.setBackground(Color.white);
setBackground(Color.white);
statusBar=new StatusBar();
mainToolBar=new JToolBar();
saveButton=new JButton(Icons.SaveIcon);
javaButton=new JButton(Icons.JavaIcon);
createConfigurationXMLButton=new JButton(Icons.ConfigurationFileIcon);
mainToolBar.add(saveButton);
mainToolBar.add(javaButton);
mainToolBar.add(createConfigurationXMLButton);
databaseTreeModel=new DatabaseTreeModel();
databaseTree=new DatabaseTree(databaseTreeModel,this);
databaseTreeScrollPane=new JScrollPane(databaseTree,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
pojoSettingsPanel=new JPanel();
pojoSettingsPanel.setLayout(new GridLayout());
pojoSettingsPanel.add(databaseTreeScrollPane);
propertiesPanel=new PropertiesPanel();
pojoSettingsPanel.add(propertiesPanel);

environmentVariablesSettingsPanel=new JPanel();
environmentVariablesModel=new EnvironmentVariablesModel();
environmentVariablesTable=new TMTable(environmentVariablesModel);
environmentVariablesSettingsPanel.setLayout(new BorderLayout());
environmentVariablesSettingsPanel.add(environmentVariablesTable,BorderLayout.CENTER);

tabbedPane=new JTabbedPane();
tabbedPane.addTab("<html><h3 style='padding:1px;'>POJO Classes</h1></html>",pojoSettingsPanel);
tabbedPane.addTab("<html><h3 style='padding:1px;'>Environment Settings</h1></html>",environmentVariablesSettingsPanel);
container.add(mainToolBar,BorderLayout.NORTH);
container.add(tabbedPane,BorderLayout.CENTER);

container.add(statusBar,BorderLayout.SOUTH);
desktopSize=Toolkit.getDefaultToolkit().getScreenSize();
setSize(1000,650);
setLocation(desktopSize.width/2-500,(desktopSize.height-50)/2-325);
disableToolBar();
setVisible(true);
}
private void addListeners()
{
javaButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
databaseTree.generatePOJOClasses();
}
});
createConfigurationXMLButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
databaseTree.generateConfigurationXML();
}
});
}
public void updateStatus(String status,boolean connected)
{
if(connected)enableToolBar(); else disableToolBar();
statusBar.setStatus(status);
}
public void updatePropertiesModel(TMTableModel model)
{
propertiesPanel.setModel(model);
}
public void clearPropertiesModel()
{
propertiesPanel.setModel(null);
}
public static JFrame getMainFrame()
{
return mainFrame;
}
public void disableToolBar()
{
saveButton.setEnabled(false);
javaButton.setEnabled(false);
createConfigurationXMLButton.setEnabled(false);
}
public void enableToolBar()
{
saveButton.setEnabled(true);
javaButton.setEnabled(true);
createConfigurationXMLButton.setEnabled(true);
}
public Object environmentVariables(String variable)
{
return this.environmentVariables.get(variable);
}
static public Object environmentVariables(EnvironmentVariable environmentVariable)
{
return environmentVariables.get(environmentVariable);
}
static public void setEnvironmentVariable(EnvironmentVariable environmentVariable,Object value)
{
environmentVariables.remove(environmentVariable);
environmentVariables.put(environmentVariable,value);
}

public static void main(String gg[])
{
POJOGUI pojoGUI=new POJOGUI();
}
}
