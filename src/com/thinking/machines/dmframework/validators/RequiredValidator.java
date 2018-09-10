package com.thinking.machines.dmframework.validators;
import org.apache.log4j.*;
import com.thinking.machines.dmframework.exceptions.*;
import com.thinking.machines.dmframework.pojo.*;
import com.thinking.machines.dmframework.utilities.*;
import java.util.*;
import java.lang.reflect.*;
public class RequiredValidator implements Validator
{
private final static Logger logger=Logger.getLogger(RequiredValidator.class);
private ArrayList<MethodWrapper> getterMethods;
private ArrayList<MethodWrapper> setterMethods;
public RequiredValidator(ArrayList<MethodWrapper> setterMethods,ArrayList<MethodWrapper> getterMethods)
{
this.setterMethods=setterMethods;
this.getterMethods=getterMethods;
}
public void validate(Object object) throws DMFrameworkException,ValidatorException
{
ValidatorException validatorException=new ValidatorException();
MethodWrapper setterMethodWrapper,getterMethodWrapper;
for(int i=0;i<getterMethods.size();i++)
{
getterMethodWrapper=getterMethods.get(i);
try
{
if(isEmpty(getterMethodWrapper.invoke(object))) 
{
if(getterMethodWrapper.getColumn().getDefaultValue()==null)
{
validatorException.add(getterMethodWrapper.getColumn().getProperty().getName(),getterMethodWrapper.getCapitalizedSpacedProperty()+" required.");
//throw new DMFrameworkException(getterMethodWrapper.getCapitalizedSpacedProperty()+" required.");
}
else
{
setterMethodWrapper=setterMethods.get(i);
try
{
setterMethodWrapper.invoke(object,setterMethodWrapper.getColumn().getDefaultValue());
}catch(Exception exception)
{
validatorException.add(getterMethodWrapper.getColumn().getProperty().getName(),getterMethodWrapper.getCapitalizedSpacedProperty()+" required.");
//throw new DMFrameworkException(getterMethodWrapper.getCapitalizedSpacedProperty()+" required.");
}
}
}
}catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException commonException)
{
logger.error(commonException.getMessage());
throw new DMFrameworkException(commonException.getMessage());
}
}
if(validatorException.hasExceptions()) throw validatorException;
}
public void validateBeforeInsertion(Object object) throws DMFrameworkException,ValidatorException
{
ValidatorException validatorException=new ValidatorException();
MethodWrapper setterMethodWrapper,getterMethodWrapper;
for(int i=0;i<getterMethods.size();i++)
{
getterMethodWrapper=getterMethods.get(i);
if(getterMethodWrapper.getColumn().getIsAutoIncrementEnabled()) continue;
try
{
if(isEmpty(getterMethodWrapper.invoke(object))) 
{
if(getterMethodWrapper.getColumn().getDefaultValue()==null)
{
validatorException.add(getterMethodWrapper.getColumn().getProperty().getName(),getterMethodWrapper.getCapitalizedSpacedProperty()+" required.");
//throw new DMFrameworkException(getterMethodWrapper.getCapitalizedSpacedProperty()+" required.");
}
else
{
setterMethodWrapper=setterMethods.get(i);
try
{
setterMethodWrapper.invoke(object,setterMethodWrapper.getColumn().getDefaultValue());
}catch(Exception exception)
{
validatorException.add(getterMethodWrapper.getColumn().getProperty().getName(),getterMethodWrapper.getCapitalizedSpacedProperty()+" required.");
//throw new DMFrameworkException(getterMethodWrapper.getCapitalizedSpacedProperty()+" required.");
}
}
}
}catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException commonException)
{
logger.error(commonException.getMessage());
throw new DMFrameworkException(commonException.getMessage());
}
}
if(validatorException.hasExceptions()) throw validatorException;
}
private boolean isEmpty(Object object)
{
if(object==null) return true;
if(object instanceof String) 
{
if(((String)object).trim().length()==0) return true;
return false;
}
return false;
}
}