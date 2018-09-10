import com.thinking.machines.dmframework.*;
import com.thinking.machines.dmframework.exceptions.*;
import java.math.*;
class addTest 
{
public static void main(String g[])
{
Student student=new Student();
student.setFirstName(g[0]);
student.setLastName(g[1]);
student.setPhoneNumber(g[2]);
student.setStdCode(Integer.parseInt(g[3]));
student.setRagistrationNumber(Integer.parseInt(g[4]));
student.setLibraryCardNumber(g[5]);
student.setFeeDecided(new BigDecimal(g[6]));
try
{
DataManager dm=new DataManager();
dm.begin();
dm.insert(student);
dm.end();
System.out.println(student.getRollNumber());
}catch(ValidatorException e)
{

e.printStackTrace();
System.out.println("-------------------------------------------------------------------");

ExceptionsIterator et=e.getIterator();
ExceptionIterator exceptionItrator;
while(et.hasNext())
{
exceptionItrator=et.next();
while(exceptionItrator.hasNext())
{
exceptionItrator.next();
System.out.println(exceptionItrator.index()+"    "+exceptionItrator.property()+"   "+exceptionItrator.exception());
}
}
}catch(DMFrameworkException e)
{
System.out.println(e.getDatabaseOperationException());
}
}
}