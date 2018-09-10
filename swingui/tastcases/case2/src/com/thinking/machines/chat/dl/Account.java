package com.thinking.machines.chat.dl;
import com.thinking.machines.dmframework.annotations.*;
import java.math.*;
@Display(value="Account")
@Table(name="ACCOUNT")
public class Account implements java.io.Serializable,Comparable<Account>
{
@Sort(priority=1)
@Display(value="user id")
@Column(name="USER_ID")
private BigDecimal userId;
@Display(value="user name")
@Column(name="USER_NAME")
private String userName;
@Display(value="email id")
@Column(name="EMAIL_ID")
private String emailId;
@Display(value="password")
@Column(name="PASSWORD")
private String password;
@Display(value="possword key")
@Column(name="POSSWORD_KEY")
private String posswordKey;
@Display(value="first name")
@Column(name="FIRST_NAME")
private String firstName;
@Display(value="last name")
@Column(name="LAST_NAME")
private String lastName;
@Display(value="gender")
@Column(name="GENDER")
private String gender;
@Display(value="status")
@Column(name="STATUS")
private String status;
public Account()
{
this.userId=null;
this.userName=null;
this.emailId=null;
this.password=null;
this.posswordKey=null;
this.firstName=null;
this.lastName=null;
this.gender=null;
this.status=null;
}
public void setUserId(BigDecimal userId)
{
this.userId=userId;
}
public BigDecimal getUserId()
{
return this.userId;
}
public void setUserName(String userName)
{
this.userName=userName;
}
public String getUserName()
{
return this.userName;
}
public void setEmailId(String emailId)
{
this.emailId=emailId;
}
public String getEmailId()
{
return this.emailId;
}
public void setPassword(String password)
{
this.password=password;
}
public String getPassword()
{
return this.password;
}
public void setPosswordKey(String posswordKey)
{
this.posswordKey=posswordKey;
}
public String getPosswordKey()
{
return this.posswordKey;
}
public void setFirstName(String firstName)
{
this.firstName=firstName;
}
public String getFirstName()
{
return this.firstName;
}
public void setLastName(String lastName)
{
this.lastName=lastName;
}
public String getLastName()
{
return this.lastName;
}
public void setGender(String gender)
{
this.gender=gender;
}
public String getGender()
{
return this.gender;
}
public void setStatus(String status)
{
this.status=status;
}
public String getStatus()
{
return this.status;
}
public boolean equals(Object object)
{
if(object==null) return false;
if(!(object instanceof Account)) return false;
Account anotherAccount=(Account)object;
if(this.userId==null && anotherAccount.userId==null) return true;
if(this.userId==null || anotherAccount.userId==null) return false;
return this.userId.equals(anotherAccount.userId);
}
public int compareTo(Account anotherAccount)
{
if(anotherAccount==null) return 1;
if(this.userId==null && anotherAccount.userId==null) return 0;
int difference;
if(this.userId==null && anotherAccount.userId!=null) return 1;
if(this.userId!=null && anotherAccount.userId==null) return -1;
difference=this.userId.compareTo(anotherAccount.userId);
return difference;
}
public int hashCode()
{
if(this.userId==null) return 0;
return this.userId.hashCode();
}
}
