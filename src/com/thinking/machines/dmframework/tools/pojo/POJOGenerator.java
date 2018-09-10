package com.thinking.machines.dmframework.tools.pojo;
import com.thinking.machines.dmframework.utilities.*;
import com.thinking.machines.dmframework.pojo.*;
import com.thinking.machines.dmframework.exceptions.*;
import org.apache.commons.lang3.*;
import java.util.*;
import java.io.*;
import org.apache.commons.io.*;
public class POJOGenerator
{
public static void main(String gg[]) 
{
try
{
String configurationFilePath=gg[2];
ConfigurationUtility.configurationFilePath=configurationFilePath;
String packageName=gg[1];
File packagePath=new File(gg[0]+File.separator+packageName.replace(".",File.separator));
System.out.println(packagePath);
FileUtils.deleteDirectory(packagePath);
FileUtils.forceMkdir(packagePath);
Database database;
database=DatabaseUtility.getDatabase(ConfigurationUtility.getDatabaseConfiguration());
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
for(Table table:database.getTables())
{
System.out.println("Creating pojo class java for : "+table.getName());
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
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
randomAccessFile.writeBytes("package "+packageName+";\r\n");
randomAccessFile.writeBytes("import com.thinking.machines.dmframework.annotations.*;\r\n");
DataType dataType;
while(iterator.hasNext())
{
randomAccessFile.writeBytes("import "+iterator.next().getName()+".*;\r\n");
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
randomAccessFile.writeBytes("@Column(name=\""+column.getName()+"\")\r\n");
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
randomAccessFile.writeBytes("if(!(object instanceof "+className+")) return false;\r\n");
randomAccessFile.writeBytes(className+" another"+className+"=("+className+")object;\r\n");
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
randomAccessFile.writeBytes("int difference;\r\n");
for(i=0;i<primaryKeyProperties.size();i++)
{
primaryKey=primaryKeyProperties.get(i);
randomAccessFile.writeBytes("difference=this."+primaryKey+".compareTo(another"+className+"."+primaryKey+");\r\n");
randomAccessFile.writeBytes("if(difference!=0) return difference;\r\n");
}
randomAccessFile.writeBytes("return 0;\r\n");
randomAccessFile.writeBytes("}\r\n");
// hashCode 
randomAccessFile.writeBytes("public int hashCode()\r\n");
randomAccessFile.writeBytes("{\r\n");
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
}

System.out.println("Entity classes created in "+packagePath.getAbsolutePath());
}catch(Exception exception)
{
System.out.println(exception);
}
}
}