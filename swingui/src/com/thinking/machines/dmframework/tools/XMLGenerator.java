package com.thinking.machines.dmframework.tools;
import java.nio.file.*;
import com.thinking.machines.dmframework.pojo.*;
import com.thinking.machines.dmframework.exceptions.*;
import java.util.*;
import java.io.*;
import org.apache.commons.io.*;
import javax.swing.*;
public class XMLGenerator
{
public static void generate(DatabaseBean databaseBean,HashMap<Table,TablePropertiesModel> tablePropertiesModelHashMap,HashMap<View,ViewPropertiesModel> viewPropertiesModelHashMap,Path xmlPath)  throws DMFrameworkException
{
ArrayList<String> xmlLines=new ArrayList<String>();
Map.Entry<Table,TablePropertiesModel> tablePropertiesModelHashMapPair;
Iterator<Map.Entry<Table,TablePropertiesModel>> tablePropertiesModelIterator;
tablePropertiesModelIterator=tablePropertiesModelHashMap.entrySet().iterator();
TablePropertiesModel tablePropertiesModel;
Table table;
String packageName;
boolean packageIt;
String defaultPackageName;
String className;
if(databaseBean.getUsername()!=null && databaseBean.getUsername().trim().length()>0)
{
xmlLines.add("<database architecture=\""+databaseBean.getArchitecture()+"\" connectionString=\""+databaseBean.getConnectionString()+"\" username=\""+databaseBean.getUsername()+"\" password=\""+databaseBean.getPassword()+"\" driver=\""+databaseBean.getDriver()+"\" />");
}
else
{
xmlLines.add("<database architecture=\""+databaseBean.getArchitecture()+"\" connectionString=\""+databaseBean.getConnectionString()+"\"  driver=\""+databaseBean.getDriver()+"\" />");
}
while(tablePropertiesModelIterator.hasNext())
{
tablePropertiesModel=tablePropertiesModelIterator.next().getValue();
packageName=tablePropertiesModel.getPackageName();
packageIt=true;
if(packageName==null || packageName.length()==0)
{
packageIt=false;
}
if(packageIt==false)
{
defaultPackageName=((String)POJOGUI.environmentVariables(EnvironmentVariable.DEFAULT_PACKAGE)).toString();
if(defaultPackageName!=null && defaultPackageName.length()>0)
{
packageName=defaultPackageName;
packageIt=true;
}
}
table=tablePropertiesModel.getTable();
className=table.getClassName();

if(packageIt)
{
xmlLines.add("<entity class=\""+packageName+"."+className+"\" />");
}
else
{
xmlLines.add("<entity class=\""+className+"\" />");
}
} // table iterator ends

Map.Entry<View,ViewPropertiesModel> viewPropertiesModelHashMapPair;
Iterator<Map.Entry<View,ViewPropertiesModel>> viewPropertiesModelIterator;
viewPropertiesModelIterator=viewPropertiesModelHashMap.entrySet().iterator();
ViewPropertiesModel viewPropertiesModel;
View view;
while(viewPropertiesModelIterator.hasNext())
{
viewPropertiesModel=viewPropertiesModelIterator.next().getValue();
packageName=viewPropertiesModel.getPackageName();
packageIt=true;
if(packageName==null || packageName.length()==0)
{
packageIt=false;
}
if(packageIt==false)
{
defaultPackageName=((String)POJOGUI.environmentVariables(EnvironmentVariable.DEFAULT_PACKAGE)).toString();
if(defaultPackageName!=null && defaultPackageName.length()>0)
{
packageName=defaultPackageName;
packageIt=true;
}
}
view=viewPropertiesModel.getView();
className=view.getClassName();

if(packageIt)
{
xmlLines.add("<entity class=\""+packageName+"."+className+"\" />");
}
else
{
xmlLines.add("<entity class=\""+className+"\" />");
}
}  // view iterator ends
File xmlFolder=new File(xmlPath.toAbsolutePath().toString());
if(xmlFolder.exists()==false)
{
try
{
FileUtils.forceMkdir(xmlFolder);
}catch(IOException ioException)
{
throw new DMFrameworkException(ioException.toString());
}
}
File file;
file=new File(xmlPath.toAbsolutePath()+File.separator+"TMDMFramework.xml");
if(file.exists())
{
if(((Boolean)POJOGUI.environmentVariables(EnvironmentVariable.REPLACE_EXISTING_FILE))==false)
{
JCheckBox replaceCheckBox = new JCheckBox("Do the same for rest");
String message = file.getAbsolutePath()+" exists, replace it ?";
Object[] elements = {message, replaceCheckBox};
int selected=JOptionPane.showConfirmDialog(POJOGUI.getMainFrame(), elements, "Confirmation", JOptionPane.YES_NO_OPTION);
boolean replace = replaceCheckBox.isSelected();
POJOGUI.setEnvironmentVariable(EnvironmentVariable.REPLACE_EXISTING_FILE,replace);
if(!(selected==JOptionPane.YES_OPTION)) return;
}
file.delete();
}

try
{
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
for(String line:xmlLines)
{
randomAccessFile.writeBytes(line+"\r\n");
}
randomAccessFile.close();
}catch(IOException ioException)
{
throw new DMFrameworkException(ioException.toString());
}
}
}