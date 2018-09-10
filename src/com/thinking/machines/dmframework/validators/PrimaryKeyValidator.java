package com.thinking.machines.dmframework.validators;
import org.apache.log4j.*;
import com.thinking.machines.dmframework.exceptions.*;
import com.thinking.machines.dmframework.pojo.*;
import com.thinking.machines.dmframework.utilities.*;
import java.util.*;
import java.lang.reflect.*;
import java.sql.*;
public class PrimaryKeyValidator implements KeyValidator
{
private final static Logger logger=Logger.getLogger(PrimaryKeyValidator.class);
private ArrayList<MethodWrapper> getterMethods;
private ArrayList<Method> preparedStatementSetterMethods;
private String exceptionMessage;
private String sqlStatement;
public PrimaryKeyValidator(String sqlStatement,ArrayList<MethodWrapper> getterMethods,ArrayList<Method> preparedStatementSetterMethods,String exceptionMessage)
{
this.getterMethods=getterMethods;
this.sqlStatement=sqlStatement;
this.preparedStatementSetterMethods=preparedStatementSetterMethods;
this.exceptionMessage=exceptionMessage;
}
public void validate(Connection connection,Object object,boolean throwExceptionIfExists) throws DMFrameworkException,ValidatorException
{
ValidatorException validatorException=new ValidatorException();
PreparedStatement preparedStatement=null;
ResultSet resultSet=null;
try
{
boolean exists=false;
preparedStatement=connection.prepareStatement(sqlStatement);
int questionMarkPosition=1;
MethodWrapper methodWrapper=null;
Method preparedStatementSetterMethod;
for(int k=0;k<getterMethods.size();k++)
{
methodWrapper=getterMethods.get(k);
preparedStatementSetterMethod=preparedStatementSetterMethods.get(k);
try
{
preparedStatementSetterMethod.invoke(preparedStatement,questionMarkPosition,methodWrapper.invoke(object));
}catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException commonException)
{
logger.error(commonException.getMessage());
throw new DMFrameworkException(commonException.getMessage());
}
questionMarkPosition++;
}
resultSet=preparedStatement.executeQuery();
exists=resultSet.next();
resultSet.close();
preparedStatement.close();
if(throwExceptionIfExists)
{
if(exists)
{
if(getterMethods.size()>1)
{
validatorException.add("generic",exceptionMessage+" exists.");
}
else
{
validatorException.add(methodWrapper.getColumn().getProperty().getName(),exceptionMessage+" exists.");
}
//throw new DMFrameworkException(exceptionMessage+" exists.");
}
}
else
{
if(!exists)
{
if(getterMethods.size()>1)
{
validatorException.add("generic",exceptionMessage+" does not exist.");
}
else
{
validatorException.add(methodWrapper.getColumn().getProperty().getName(),exceptionMessage+" does not exist.");
}
//throw new DMFrameworkException(exceptionMessage+" does not exist.");
}
}
}catch(SQLException sqlException)
{
try
{
if(resultSet!=null && resultSet.isClosed()==false) resultSet.close();
}catch(SQLException se){}
try
{
if(preparedStatement!=null && preparedStatement.isClosed()==false) preparedStatement.close();
}catch(SQLException se){}
logger.error(sqlException.getMessage());
throw new DMFrameworkException(sqlException.getMessage());
}
if(validatorException.hasExceptions()) throw validatorException;
}
public void validateBeforeInsertion(Connection connection,Object object,boolean throwExceptionIfExists) throws DMFrameworkException,ValidatorException
{
ValidatorException validatorException=new ValidatorException();
// even if one column of the composite key is autoincrement 
// then no need to perform duplicacy check
MethodWrapper methodWrapper=null;
for(int k=0;k<getterMethods.size();k++)
{
methodWrapper=getterMethods.get(k);
if(methodWrapper.getColumn().getIsAutoIncrementEnabled()) return;
}
PreparedStatement preparedStatement=null;
ResultSet resultSet=null;
try
{
boolean exists=false;
preparedStatement=connection.prepareStatement(sqlStatement);
int questionMarkPosition=1;
Method preparedStatementSetterMethod;
for(int k=0;k<getterMethods.size();k++)
{
methodWrapper=getterMethods.get(k);
preparedStatementSetterMethod=preparedStatementSetterMethods.get(k);
try
{
preparedStatementSetterMethod.invoke(preparedStatement,questionMarkPosition,methodWrapper.invoke(object));
}catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException commonException)
{
logger.error(commonException.getMessage());
throw new DMFrameworkException(commonException.getMessage());
}
questionMarkPosition++;
}
resultSet=preparedStatement.executeQuery();
exists=resultSet.next();
resultSet.close();
preparedStatement.close();
if(throwExceptionIfExists)
{
if(exists)
{
if(getterMethods.size()>1)
{
validatorException.add("generic",exceptionMessage+" exists.");
}
else
{
validatorException.add(methodWrapper.getColumn().getProperty().getName(),exceptionMessage+" exists.");
}
//throw new DMFrameworkException(exceptionMessage+" exists.");
}
}
else
{
if(!exists)
{
if(getterMethods.size()>1)
{
validatorException.add("generic",exceptionMessage+" does not exist.");
}
else
{
validatorException.add(methodWrapper.getColumn().getProperty().getName(),exceptionMessage+" does not exist.");
}
//throw new DMFrameworkException(exceptionMessage+" does not exist.");
}
}
}catch(SQLException sqlException)
{
try
{
if(resultSet!=null && resultSet.isClosed()==false) resultSet.close();
}catch(SQLException se){}
try
{
if(preparedStatement!=null && preparedStatement.isClosed()==false) preparedStatement.close();
}catch(SQLException se){}
logger.error(sqlException.getMessage());
throw new DMFrameworkException(sqlException.getMessage());
}
if(validatorException.hasExceptions()) throw validatorException;
}
public void validateByPrimaryKey(Connection connection,boolean throwExceptionIfExists,Object ...primaryKey) throws DMFrameworkException,ValidatorException
{
ValidatorException validatorException=new ValidatorException();
PreparedStatement preparedStatement=null;
ResultSet resultSet=null;
try
{
boolean exists=false;
preparedStatement=connection.prepareStatement(sqlStatement);
int questionMarkPosition=1;
Method preparedStatementSetterMethod;
for(int k=0;k<getterMethods.size();k++)
{
preparedStatementSetterMethod=preparedStatementSetterMethods.get(k);
try
{
preparedStatementSetterMethod.invoke(preparedStatement,questionMarkPosition,primaryKey[k]);
}catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException commonException)
{
logger.error(commonException.getMessage());
throw new DMFrameworkException(commonException.getMessage());
}
questionMarkPosition++;
}
resultSet=preparedStatement.executeQuery();
exists=resultSet.next();
resultSet.close();
preparedStatement.close();
if(throwExceptionIfExists)
{
if(exists)
{
if(getterMethods.size()>1)
{
validatorException.add("generic",exceptionMessage+" exists.");
}
else
{
validatorException.add(getterMethods.get(0).getColumn().getProperty().getName(),exceptionMessage+" exists.");
}
//throw new DMFrameworkException(exceptionMessage+" exists.");
}
}
else
{
if(!exists)
{
if(getterMethods.size()>1)
{
validatorException.add("generic",exceptionMessage+" does not exist.");
}
else
{
validatorException.add(getterMethods.get(0).getColumn().getProperty().getName(),exceptionMessage+" does not exist.");
}
//throw new DMFrameworkException(exceptionMessage+" does not exist.");
}
}
}catch(SQLException sqlException)
{
try
{
if(resultSet!=null && resultSet.isClosed()==false) resultSet.close();
}catch(SQLException se){}
try
{
if(preparedStatement!=null && preparedStatement.isClosed()==false) preparedStatement.close();
}catch(SQLException se){}
logger.error(sqlException.getMessage());
throw new DMFrameworkException(sqlException.getMessage());
}
if(validatorException.hasExceptions()) throw validatorException;
}
}