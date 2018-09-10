package com.thinking.machines.dmframework.utilities;
import java.io.*;
import java.util.*;
import com.thinking.machines.dmframework.exceptions.*;
import java.util.*;
public class ConfigurationUtility
{
public static String configurationFilePath="TMDMFramework.xml";
private ConfigurationUtility(){}
private static HashMap<String,HashMap<String,String>> configurations;
private static List<Class> entities;
public static HashMap<String,String> getDatabaseConfiguration() throws DMFrameworkException,ClassNotFoundException
{
if(configurations==null) loadConfiguration();
HashMap<String,String> databaseConfiguration=configurations.get("database");
if(databaseConfiguration==null) throw new DMFrameworkException("Database configuration missing");
return databaseConfiguration;
}
private static void loadConfiguration() throws DMFrameworkException,ClassNotFoundException
{
try
{
HashMap<String,String> map;
File file=new File(configurationFilePath);
if(file.exists()==false) throw new DMFrameworkException("Configuration file "+configurationFilePath+" is missing");
configurations=new HashMap<String,HashMap<String,String>>();
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
String xml;
String tag;
String splits[];
String key,value;
entities=new ArrayList<Class>();
String entityClassName;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
xml=randomAccessFile.readLine();
if(xml.trim().length()==0) continue;
xml=xml.substring(1,xml.length()-2);
tag=xml.substring(0,xml.indexOf(" "));
xml=xml.substring(xml.indexOf(" ")+1);
xml=xml.replaceAll("'","\"");
splits=xml.trim().split("\"");
map=new HashMap<String,String>();
for(int i=0;i<splits.length;i+=2)
{
key=splits[i].trim().substring(0,splits[i].trim().length()-1);
value=splits[i+1].trim();
map.put(key.trim(),value.trim());
}
if(tag.equals("entity"))
{
entityClassName=map.get("class");
if(entityClassName==null) 
{
configurations=null;
throw new DMFrameworkException(configurationFilePath+" does not contain class attribute against entity");
}
entities.add(Class.forName(entityClassName));
continue;
}
configurations.put(tag,map);
}
randomAccessFile.close();
System.out.println("Configuration loaded successfully from : "+configurationFilePath); 
}catch(IOException ioException)
{
configurations=null;
throw new DMFrameworkException("Invalid configuration settings in  "+configurationFilePath+ioException.getMessage());
}
}
public static List<Class> getEntities() throws DMFrameworkException,ClassNotFoundException
{
if(entities==null) loadConfiguration();
return entities;
}
}