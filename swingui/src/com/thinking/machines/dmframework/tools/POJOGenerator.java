package com.thinking.machines.dmframework.tools;
import java.nio.file.*;
import com.thinking.machines.dmframework.utilities.*;
import com.thinking.machines.dmframework.pojo.*;
import com.thinking.machines.dmframework.exceptions.*;
import org.apache.commons.lang3.*;
import java.util.*;
import java.io.*;
import org.apache.commons.io.*;
import javax.swing.*;
public class POJOGenerator
{
public static void generate(TablePropertiesModel tablePropertiesModel,Path path)  throws DMFrameworkException
{
try
{
String packageName=tablePropertiesModel.getPackageName();
boolean packageIt=true;
if(packageName==null || packageName.length()==0)
{
packageIt=false;
}
if(packageIt==false)
{
String defaultPackageName=((String)POJOGUI.environmentVariables(EnvironmentVariable.DEFAULT_PACKAGE)).toString();
if(defaultPackageName!=null && defaultPackageName.length()>0)
{
packageName=defaultPackageName;
packageIt=true;
}
}
File packagePath;
if(packageIt)
{
packagePath=new File(path.toAbsolutePath()+File.separator+packageName.replace(".",File.separator));
}
else
{
packagePath=new File(path.toAbsolutePath().toString());
}
if(packagePath.exists()==false)
{
FileUtils.forceMkdir(packagePath);
}
File file;
int i;
boolean applyAnd;
boolean applyComma;
String className;
Property property;
HashSet<Package> imports=new HashSet<Package>();
Iterator<Package> iterator;
Package pkg;
List<String> primaryKeys;
String primaryKey;
List<String> primaryKeyProperties;
Table table=null;
table=tablePropertiesModel.getTable();
//System.out.println("Creating pojo class java for : "+table.getClassName());
primaryKeys=table.getPrimaryKeys();
if(primaryKeys.size()==0)
{
throw new DMFrameworkException("Table : "+table.getName()+" has no primary key(s)");
}
primaryKeyProperties=new ArrayList<String>();
for(Column column:table.getColumns())
{
pkg=column.getProperty().getType().getPackage();
if(pkg.getName().equals("java.lang")==false)
{
imports.add(pkg);
}
}
iterator=imports.iterator();
className=table.getClassName();
file=new File(packagePath+File.separator+className+".java");
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
ColumnPropertiesModel columnPropertiesModel;
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(packageIt)
{
randomAccessFile.writeBytes("package "+packageName+";\r\n");
}
randomAccessFile.writeBytes("import com.thinking.machines.dmframework.annotations.*;\r\n");
DataType dataType;
while(iterator.hasNext())
{
randomAccessFile.writeBytes("import "+iterator.next().getName()+".*;\r\n");
}
String sequenceName;
String displayName=tablePropertiesModel.getDisplayName();
if(displayName!=null && displayName.trim().length()!=0)
{
randomAccessFile.writeBytes("@Display(value=\""+displayName+"\")\r\n");
}
randomAccessFile.writeBytes("@Table(name=\""+table.getName()+"\")\r\n");
randomAccessFile.writeBytes("public class "+className+" implements java.io.Serializable,Comparable<"+className+">\r\n");
randomAccessFile.writeBytes("{\r\n");
for(Column column:table.getColumns())
{
property=column.getProperty();
for(i=0;i<primaryKeys.size();i++)
{
if(primaryKeys.get(i).equals(column.getName()))
{
randomAccessFile.writeBytes("@Sort(priority="+(i+1)+")\r\n");
primaryKeyProperties.add(property.getName());
}
}
columnPropertiesModel=tablePropertiesModel.getColumnPropertiesModelByColumn(column);
displayName=columnPropertiesModel.getDisplayName();
if(displayName!=null && displayName.trim().length()!=0)
{
randomAccessFile.writeBytes("@Display(value=\""+displayName+"\")\r\n");
}
randomAccessFile.writeBytes("@Column(name=\""+column.getName()+"\")\r\n");
sequenceName=columnPropertiesModel.getSequenceName();
if(sequenceName!=null && sequenceName.trim().length()!=0)
{
randomAccessFile.writeBytes("@Sequence(name=\""+sequenceName+"\")\r\n");
}
System.out.println("<POJOGenarator>()--(property.getName)"+property.getName());
randomAccessFile.writeBytes("private "+property.getType().getSimpleName()+" "+property.getName()+";\r\n");
}
// building default constructor
randomAccessFile.writeBytes("public "+className+"()\r\n");
randomAccessFile.writeBytes("{\r\n");
for(Column column:table.getColumns())
{
property=column.getProperty();
randomAccessFile.writeBytes("this."+property.getName()+"=null;\r\n");
}
randomAccessFile.writeBytes("}\r\n");
// building setter/getters
for(Column column:table.getColumns())
{
property=column.getProperty();
// setter
randomAccessFile.writeBytes("public void set"+StringUtils.capitalize(property.getName())+"("+property.getType().getSimpleName()+" "+property.getName()+")\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("this."+property.getName()+"="+property.getName()+";\r\n");
randomAccessFile.writeBytes("}\r\n");
// getter
randomAccessFile.writeBytes("public "+property.getType().getSimpleName()+" get"+StringUtils.capitalize(property.getName())+"()\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("return this."+property.getName()+";\r\n");
randomAccessFile.writeBytes("}\r\n");
}
// equals
randomAccessFile.writeBytes("public boolean equals(Object object)\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("if(object==null) return false;\r\n");
randomAccessFile.writeBytes("if(!(object instanceof "+className+")) return false;\r\n");
randomAccessFile.writeBytes(className+" another"+className+"=("+className+")object;\r\n");
// both this and that have null value
randomAccessFile.writeBytes("if(");
applyAnd=false;
for(i=0;i<primaryKeyProperties.size();i++)
{
if(applyAnd) randomAccessFile.writeBytes(" && ");
primaryKey=primaryKeyProperties.get(i);
randomAccessFile.writeBytes("this."+primaryKey+"==null && another"+className+"."+primaryKey+"==null");
applyAnd=true;
}
randomAccessFile.writeBytes(") return true;\r\n");

// either this or that have null value
randomAccessFile.writeBytes("if(");
applyAnd=false;
for(i=0;i<primaryKeyProperties.size();i++)
{
if(applyAnd) randomAccessFile.writeBytes(" || ");
primaryKey=primaryKeyProperties.get(i);
randomAccessFile.writeBytes("this."+primaryKey+"==null || another"+className+"."+primaryKey+"==null");
applyAnd=true;
}
randomAccessFile.writeBytes(") return false;\r\n");

// none of them are null
applyAnd=false;
randomAccessFile.writeBytes("return ");
for(i=0;i<primaryKeyProperties.size();i++)
{
if(applyAnd) randomAccessFile.writeBytes(" && ");
primaryKey=primaryKeyProperties.get(i);
randomAccessFile.writeBytes("this."+primaryKey+".equals(another"+className+"."+primaryKey+")");
applyAnd=true;
}
randomAccessFile.writeBytes(";\r\n");
randomAccessFile.writeBytes("}\r\n");
// compareTo
randomAccessFile.writeBytes("public int compareTo("+className+" another"+className+")\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("if(another"+className+"==null) return 1;\r\n");
// both this and that have null value
randomAccessFile.writeBytes("if(");
applyAnd=false;
for(i=0;i<primaryKeyProperties.size();i++)
{
if(applyAnd) randomAccessFile.writeBytes(" && ");
primaryKey=primaryKeyProperties.get(i);
randomAccessFile.writeBytes("this."+primaryKey+"==null && another"+className+"."+primaryKey+"==null");
applyAnd=true;
}
randomAccessFile.writeBytes(") return 0;\r\n");
randomAccessFile.writeBytes("int difference;\r\n");
for(i=0;i<primaryKeyProperties.size();i++)
{
primaryKey=primaryKeyProperties.get(i);
randomAccessFile.writeBytes("if(this."+primaryKey+"==null && another"+className+"."+primaryKey+"!=null) return 1;\r\n");
randomAccessFile.writeBytes("if(this."+primaryKey+"!=null && another"+className+"."+primaryKey+"==null) return -1;\r\n");
randomAccessFile.writeBytes("difference=this."+primaryKey+".compareTo(another"+className+"."+primaryKey+");\r\n");
if(i<primaryKeyProperties.size()-1)
randomAccessFile.writeBytes("if(difference!=0) return difference;\r\n");
else
randomAccessFile.writeBytes("return difference;\r\n");
}
randomAccessFile.writeBytes("}\r\n");
// hashCode 
randomAccessFile.writeBytes("public int hashCode()\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("if(");
applyAnd=false;
for(i=0;i<primaryKeyProperties.size();i++)
{
if(applyAnd) randomAccessFile.writeBytes(" && ");
primaryKey=primaryKeyProperties.get(i);
randomAccessFile.writeBytes("this."+primaryKey+"==null");
applyAnd=true;
}
randomAccessFile.writeBytes(") return 0;\r\n");
if(primaryKeyProperties.size()==1)
{
randomAccessFile.writeBytes("return this."+primaryKeyProperties.get(0)+".hashCode();\r\n");
}
else
{
randomAccessFile.writeBytes("return java.util.Objects.hash(");
applyComma=false;
for(i=0;i<primaryKeyProperties.size();i++)
{
primaryKey=primaryKeyProperties.get(i);
if(applyComma) randomAccessFile.writeBytes(",");
randomAccessFile.writeBytes("this."+primaryKey);
applyComma=true;
}
randomAccessFile.writeBytes(");\r\n");
}
randomAccessFile.writeBytes("}\r\n");
randomAccessFile.writeBytes("}\r\n");
randomAccessFile.close();
}catch(IOException ioException)
{
throw new DMFrameworkException(ioException.toString());
}
}
public static void generate(ViewPropertiesModel viewPropertiesModel,Path path)  throws DMFrameworkException
{
try
{
String packageName=viewPropertiesModel.getPackageName();
boolean packageIt=true;
if(packageName==null || packageName.length()==0)
{
packageIt=false;
}
if(packageIt==false)
{
String defaultPackageName=((String)POJOGUI.environmentVariables(EnvironmentVariable.DEFAULT_PACKAGE)).toString();
if(defaultPackageName!=null && defaultPackageName.length()>0)
{
packageName=defaultPackageName;
packageIt=true;
}
}
File packagePath;
if(packageIt)
{
packagePath=new File(path.toAbsolutePath()+File.separator+packageName.replace(".",File.separator));
}
else
{
packagePath=new File(path.toAbsolutePath().toString());
}
if(packagePath.exists()==false)
{
FileUtils.forceMkdir(packagePath);
}
File file;
int i;
boolean applyAnd;
boolean applyComma;
String className;
Property property;
HashSet<Package> imports=new HashSet<Package>();
Iterator<Package> iterator;
Package pkg;
List<String> primaryKeys;
String primaryKey;
List<String> primaryKeyProperties;
View view=null;
view=viewPropertiesModel.getView();
//System.out.println("Creating pojo class java for : "+view.getClassName());
iterator=imports.iterator();
className=view.getClassName();
file=new File(packagePath+File.separator+className+".java");
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
ColumnPropertiesModel columnPropertiesModel;
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(packageIt)
{
randomAccessFile.writeBytes("package "+packageName+";\r\n");
}
randomAccessFile.writeBytes("import com.thinking.machines.dmframework.annotations.*;\r\n");
DataType dataType;
while(iterator.hasNext())
{
randomAccessFile.writeBytes("import "+iterator.next().getName()+".*;\r\n");
}
String sequenceName;
String displayName=viewPropertiesModel.getDisplayName();
if(displayName!=null && displayName.trim().length()!=0)
{
randomAccessFile.writeBytes("@Display(value=\""+displayName+"\")\r\n");
}
randomAccessFile.writeBytes("@View(name=\""+view.getName()+"\")\r\n");
ArrayList<Column> compareOrder=viewPropertiesModel.getCompareOrder();
if(compareOrder!=null && compareOrder.size()>0)
{
randomAccessFile.writeBytes("public class "+className+" implements java.io.Serializable,Comparable<"+className+">\r\n");
}
else
{
randomAccessFile.writeBytes("public class "+className+" implements java.io.Serializable\r\n");
}
randomAccessFile.writeBytes("{\r\n");

for(Column column:view.getColumns())
{
property=column.getProperty();
columnPropertiesModel=viewPropertiesModel.getColumnPropertiesModelByColumn(column);
displayName=columnPropertiesModel.getDisplayName();
if(displayName!=null && displayName.trim().length()!=0)
{
randomAccessFile.writeBytes("@Display(value=\""+displayName+"\")\r\n");
}
randomAccessFile.writeBytes("@Column(name=\""+column.getName()+"\")\r\n");
randomAccessFile.writeBytes("private "+property.getType().getSimpleName()+" "+property.getName()+";\r\n");
}
// building default constructor
randomAccessFile.writeBytes("public "+className+"()\r\n");
randomAccessFile.writeBytes("{\r\n");
for(Column column:view.getColumns())
{
property=column.getProperty();
randomAccessFile.writeBytes("this."+property.getName()+"=null;\r\n");
}
randomAccessFile.writeBytes("}\r\n");
// building setter/getters
for(Column column:view.getColumns())
{
property=column.getProperty();
// setter
randomAccessFile.writeBytes("public void set"+StringUtils.capitalize(property.getName())+"("+property.getType().getSimpleName()+" "+property.getName()+")\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("this."+property.getName()+"="+property.getName()+";\r\n");
randomAccessFile.writeBytes("}\r\n");
// getter
randomAccessFile.writeBytes("public "+property.getType().getSimpleName()+" get"+StringUtils.capitalize(property.getName())+"()\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("return this."+property.getName()+";\r\n");
randomAccessFile.writeBytes("}\r\n");
}
randomAccessFile.writeBytes("}\r\n");



if(compareOrder!=null && compareOrder.size()>0)
{
// equals
Column compareOrderColumn;
Property compareOrderProperty;
randomAccessFile.writeBytes("public boolean equals(Object object)\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("if(!(object instanceof "+className+")) return false;\r\n");
randomAccessFile.writeBytes(className+" another"+className+"=("+className+")object;\r\n");
applyAnd=false;
randomAccessFile.writeBytes("return ");
for(i=0;i<compareOrder.size();i++)
{
compareOrderColumn=compareOrder.get(i);
compareOrderProperty=compareOrderColumn.getProperty();
if(applyAnd) randomAccessFile.writeBytes(" && ");
randomAccessFile.writeBytes("this."+compareOrderProperty.getName()+".equals(another"+className+"."+compareOrderProperty.getName()+")");
applyAnd=true;
}
randomAccessFile.writeBytes(";\r\n");
randomAccessFile.writeBytes("}\r\n");
// compareTo
randomAccessFile.writeBytes("public int compareTo("+className+" another"+className+")\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("int difference;\r\n");
for(i=0;i<compareOrder.size();i++)
{
compareOrderColumn=compareOrder.get(i);
compareOrderProperty=compareOrderColumn.getProperty();
randomAccessFile.writeBytes("difference=this."+compareOrderProperty.getName()+".compareTo(another"+className+"."+compareOrderProperty.getName()+");\r\n");
randomAccessFile.writeBytes("if(difference!=0) return difference;\r\n");
}
randomAccessFile.writeBytes("return 0;\r\n");
randomAccessFile.writeBytes("}\r\n");
// hashCode 
randomAccessFile.writeBytes("public int hashCode()\r\n");
randomAccessFile.writeBytes("{\r\n");
if(compareOrder.size()==1)
{
compareOrderColumn=compareOrder.get(0);
compareOrderProperty=compareOrderColumn.getProperty();
randomAccessFile.writeBytes("return this."+compareOrderProperty.getName()+".hashCode();\r\n");
}
else
{
randomAccessFile.writeBytes("return java.util.Objects.hash(");
applyComma=false;
for(i=0;i<compareOrder.size();i++)
{
compareOrderColumn=compareOrder.get(0);
compareOrderProperty=compareOrderColumn.getProperty();
if(applyComma) randomAccessFile.writeBytes(",");
randomAccessFile.writeBytes("this."+compareOrderProperty.getName());
applyComma=true;
}
randomAccessFile.writeBytes(");\r\n");
}
randomAccessFile.writeBytes("}\r\n");
}//compareOrder if condition ends
randomAccessFile.close();
}catch(IOException ioException)
{
throw new DMFrameworkException(ioException.toString());
}
}


}