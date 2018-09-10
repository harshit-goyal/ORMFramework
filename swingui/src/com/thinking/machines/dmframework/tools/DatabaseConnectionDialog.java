package com.thinking.machines.dmframework.tools;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.sql.*;
import com.thinking.machines.dmframework.pojo.*;
import com.thinking.machines.dmframework.utilities.*;
import com.thinking.machines.dmframework.exceptions.*;
import java.util.*;
public class DatabaseConnectionDialog extends JDialog implements DocumentListener
{
private HashMap<String,String> databaseConfiguration;
private Database database;
private Color errorColor=new Color(66,0,0);
private Color progressColor=new Color(0,108,54);
private Vector<RDBMS> listOfArchitectures;
private JComboBox architecture;
private JTextField connectionString;
private JTextField username;
private JPasswordField password;
private JTextField driver;
private JLabel architectureLabel;
private JLabel connectionStringLabel;
private JLabel usernameLabel;
private JLabel passwordLabel;
private JLabel driverLabel;
private JLabel statusBar;
private JButton connectButton;
public DatabaseConnectionDialog()
{
setIconImage(Icons.LogoIcon.getImage());
initComponents();
addListeners();
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
setSize(700,335);
setLocation(d.width/2-getWidth()/2,(d.height-50)/2-getHeight()/2);
setModal(true);
setAlwaysOnTop(true);
setVisible(true);
}
private void initComponents()
{
setTitle("Database connection");
listOfArchitectures=new Vector<RDBMS>();
listOfArchitectures.add(new MySQL());
listOfArchitectures.add(new Oracle11g());
architectureLabel=new JLabel("RDBMS");
architecture=new JComboBox(listOfArchitectures);
usernameLabel=new JLabel("Username");
username=new JTextField("chat"); // for oracle chat
passwordLabel=new JLabel("Password");
password=new JPasswordField("chat"); // for oracle chat
driverLabel=new JLabel("JDBC Driver");
driver=new JTextField(listOfArchitectures.get(0).getDriverName());
connectionStringLabel=new JLabel("Connection string");
connectionString=new JTextField(listOfArchitectures.get(0).getConnectionString());
connectButton=new JButton("Connect");
connectButton.setEnabled(false);
statusBar=new JLabel("");
Container c=getContentPane();
c.setLayout(null);
int lm,tm;
lm=5;
tm=10;
Font statusBarFont=new Font("Verdana",Font.BOLD,16);
statusBar.setFont(statusBarFont);
statusBar.setForeground(errorColor);
Font font=new Font("Verdana",Font.PLAIN,16);
usernameLabel.setFont(font);
username.setFont(font);
password.setFont(font);
passwordLabel.setFont(font);
driver.setFont(font);
driverLabel.setFont(font);
connectionString.setFont(font);
connectionStringLabel.setFont(font);
architectureLabel.setFont(font);
architectureLabel.setBounds(lm+5,tm+5,150,30);
architecture.setBounds(lm+5+150,tm+5,150,30);
usernameLabel.setBounds(lm+5,tm+5+30+5,150,30);
username.setBounds(lm+5+150,tm+5+30+5,150,30);
passwordLabel.setBounds(lm+5,tm+5+30+5+30+5,150,30);
password.setBounds(lm+5+150,tm+5+30+5+30+5,150,30);
driverLabel.setBounds(lm+5,tm+5+30+5+30+5+30+5,150,30);
driver.setBounds(lm+5+150,tm+5+30+5+30+5+30+5,500,30);
connectionStringLabel.setBounds(lm+5,tm+5+30+5+30+5+30+5+30+5,150,30);
connectionString.setBounds(lm+5+150,tm+5+30+5+30+5+30+5+30+5,500,30);
connectButton.setBounds(lm+5+150+500-100,tm+5+30+5+30+5+30+5+30+15+30+5,100,30);
statusBar.setBounds(1,335-31-40,700-1,30);
c.add(architectureLabel);
c.add(architecture);
c.add(usernameLabel);
c.add(username);
c.add(passwordLabel);
c.add(password);
c.add(connectionStringLabel);
c.add(connectionString);
c.add(driverLabel);
c.add(driver);
c.add(connectButton);
c.add(statusBar);
}
private void disableAll()
{
username.setEnabled(false);
password.setEnabled(false);
connectionString.setEnabled(false);
driver.setEnabled(false);
connectButton.setEnabled(false);
}
private void enableAll()
{
username.setEnabled(true);
password.setEnabled(true);
connectionString.setEnabled(true);
driver.setEnabled(true);
connectButton.setEnabled(true);
}
private void addListeners()
{
username.getDocument().addDocumentListener(this);
connectionString.getDocument().addDocumentListener(this);
password.getDocument().addDocumentListener(this);
driver.getDocument().addDocumentListener(this);
architecture.addItemListener(new ItemListener(){
public void itemStateChanged(ItemEvent ev)
{
driver.setText(((RDBMS)architecture.getSelectedItem()).getDriverName());
connectionString.setText(((RDBMS)architecture.getSelectedItem()).getConnectionString());

}
});


connectButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
DatabaseConnectionDialog.this.disableAll();
try
{
DatabaseConnectionDialog.this.statusBar.setText("");
String username=DatabaseConnectionDialog.this.username.getText().trim();
String password=new String(DatabaseConnectionDialog.this.password.getPassword());
String connectionString=DatabaseConnectionDialog.this.connectionString.getText().trim();
String driver=DatabaseConnectionDialog.this.driver.getText().trim();
String architecture=DatabaseConnectionDialog.this.architecture.getSelectedItem().toString();
Class.forName(driver);
Connection connection;
if(username!=null && username.length()>0)
{
connection=DriverManager.getConnection(connectionString,username,password);
}
else
{
connection=DriverManager.getConnection(connectionString);
}
connection.close();
statusBar.setForeground(progressColor);
statusBar.setText("Connected and collecting database information, please wait...");
javax.swing.Timer timer=null;
timer=new javax.swing.Timer(1000,new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
try
{
javax.swing.Timer t=(javax.swing.Timer)ev.getSource();
t.stop();
HashMap<String,String> configurationMap;
configurationMap=new HashMap<String,String>();
configurationMap.put("architecture",architecture);
configurationMap.put("username",username);
configurationMap.put("password",password);
configurationMap.put("connectionString",connectionString);
configurationMap.put("driver",driver);
database=DatabaseUtility.getDatabase(configurationMap);
DatabaseConnectionDialog.this.databaseConfiguration=new HashMap<String,String>();
DatabaseConnectionDialog.this.databaseConfiguration=configurationMap;
DatabaseConnectionDialog.this.hide();
}catch(DMFrameworkException dmFrameworkException)
{
JOptionPane.showMessageDialog(DatabaseConnectionDialog.this,dmFrameworkException.toString());
statusBar.setForeground(errorColor);
}
}
});
timer.start();
}catch(ClassNotFoundException classNotFoundException)
{
System.out.println(classNotFoundException.toString());
DatabaseConnectionDialog.this.statusBar.setText(classNotFoundException.toString());
}
catch(SQLException sqlException)
{
System.out.println(sqlException.getMessage());
DatabaseConnectionDialog.this.statusBar.setText(sqlException.getMessage());
}
DatabaseConnectionDialog.this.enableAll();
}
});
}
private void enableDisableConnectButton()
{
String username=this.username.getText().trim();
String password=new String(this.password.getPassword()).trim();
String driver=this.driver.getText().trim();
String connectionString=this.connectionString.getText().trim();
if(username.length()>0 || password.length()>0)
{
if(username.length()==0 || password.length()==0)
{
connectButton.setEnabled(false); 
return;
}
}
if(connectionString.length()==0 || driver.length()==0)
{
connectButton.setEnabled(false); 
return;
}
connectButton.setEnabled(true);
}
public void insertUpdate(DocumentEvent documentEvent)
{
enableDisableConnectButton();
}
public void removeUpdate(DocumentEvent documentEvent)
{
enableDisableConnectButton();
}
public void changedUpdate(DocumentEvent documentEvent)
{
enableDisableConnectButton();
}
public Database getDatabase()
{
return this.database;
}
public HashMap<String,String> getDatabaseConfiguration()
{
return this.databaseConfiguration;
}
}