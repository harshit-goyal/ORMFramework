package com.thinking.machines.dmframework.tools;
public class Oracle11g implements RDBMS
{
public String getArchitecture()
{
return "Oracle 11g";
}
public String getDriverName()
{
return "oracle.jdbc.driver.OracleDriver";
}
public String getConnectionString()
{
return "jdbc:oracle:thin:@192.168.1.253:1521:xe";
}
public String toString()
{
return getArchitecture();
}
}