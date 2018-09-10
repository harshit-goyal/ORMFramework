package com.thinking.machines.dmframework.tools;
public class MySQL implements RDBMS
{
public String getArchitecture()
{
return "MySQL";
}
public String getDriverName()
{
return "com.mysql.jdbc.Driver";
}
public String getConnectionString()
{
return "jdbc:mysql://localhost:3306/daoframeworksampledbone";
}
public String toString()
{
return getArchitecture();
}
}