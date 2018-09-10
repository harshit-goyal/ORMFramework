import java.sql.*;
import java.util.*;

import com.thinking.machines.chat.dl.*;
import com.thinking.machines.dmframework.*;
import com.thinking.machines.dmframework.exceptions.*;
class SelectAccount{
public static void main(String g[])
{
try
{ResultSet r;
DataManager dm=new DataManager();
dm.begin();
List<Account> a=dm.<Account>select(Account.class).where("userName").eq("deepesh").query();
dm.end();
Iterator<Account> i=a.iterator();
Account aa;
System.out.println(a.size());
while(i.hasNext())
{
aa=i.next();
System.out.println(aa.getUserName()+")");
}
}catch(Exception e)
{
e.printStackTrace();
System.out.println("----------------------------------------------------------");
System.out.println(e);

}

}


}