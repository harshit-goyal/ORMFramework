package com.thinking.machines.dmframework.tools;
import java.util.*;
public class DatabaseBean
{
private HashMap<String,String> databaseConfiguration;
public DatabaseBean()
{
this.databaseConfiguration=null;
}
public void setDatabaseConfiguration(HashMap<String,String> databaseConfiguration)
{
this.databaseConfiguration=databaseConfiguration;
}
public HashMap<String,String> getDatabaseConfiguration()
{
return this.databaseConfiguration;
}
public String getArchitecture()
{
return databaseConfiguration.get("architecture");
}
public String getUsername()
{
return databaseConfiguration.get("username");
}
public String getPassword()
{
return databaseConfiguration.get("password");
}
public String getConnectionString()
{
return databaseConfiguration.get("connectionString");
}
public String getDriver()
{
return databaseConfiguration.get("driver");
}
}