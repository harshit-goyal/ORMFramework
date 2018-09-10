import com.thinking.machines.dmframework.annotations.*;
import java.math.*;
@Display(value="Student")
@Table(name="student")
public class Student implements java.io.Serializable,Comparable<Student>
{
@Sort(priority=1)
@Display(value="roll number")
@Column(name="roll_number")
private Integer rollNumber;
@Display(value="first name")
@Column(name="first_name")
private String firstName;
@Display(value="last name")
@Column(name="lst_name")
private String lastName;
@Display(value="phone number")
@Column(name="phone_number")
private String phoneNumber;
@Display(value="ragistration number")
@Column(name="rgs_num")
private Integer ragistrationNumber;
@Display(value="library card number")
@Column(name="lbr_crd_num")
private String libraryCardNumber;
@Display(value="std code")
@Column(name="std_code")
private Integer stdCode;
@Display(value="fee decided")
@Column(name="fee_dcd")
private BigDecimal feeDecided;
public Student()
{
this.rollNumber=null;
this.firstName=null;
this.lastName=null;
this.phoneNumber=null;
this.ragistrationNumber=null;
this.libraryCardNumber=null;
this.stdCode=null;
this.feeDecided=null;
}
public void setRollNumber(Integer rollNumber)
{
this.rollNumber=rollNumber;
}
public Integer getRollNumber()
{
return this.rollNumber;
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
public void setPhoneNumber(String phoneNumber)
{
this.phoneNumber=phoneNumber;
}
public String getPhoneNumber()
{
return this.phoneNumber;
}
public void setRagistrationNumber(Integer ragistrationNumber)
{
this.ragistrationNumber=ragistrationNumber;
}
public Integer getRagistrationNumber()
{
return this.ragistrationNumber;
}
public void setLibraryCardNumber(String libraryCardNumber)
{
this.libraryCardNumber=libraryCardNumber;
}
public String getLibraryCardNumber()
{
return this.libraryCardNumber;
}
public void setStdCode(Integer stdCode)
{
this.stdCode=stdCode;
}
public Integer getStdCode()
{
return this.stdCode;
}
public void setFeeDecided(BigDecimal feeDecided)
{
this.feeDecided=feeDecided;
}
public BigDecimal getFeeDecided()
{
return this.feeDecided;
}
public boolean equals(Object object)
{
if(object==null) return false;
if(!(object instanceof Student)) return false;
Student anotherStudent=(Student)object;
if(this.rollNumber==null && anotherStudent.rollNumber==null) return true;
if(this.rollNumber==null || anotherStudent.rollNumber==null) return false;
return this.rollNumber.equals(anotherStudent.rollNumber);
}
public int compareTo(Student anotherStudent)
{
if(anotherStudent==null) return 1;
if(this.rollNumber==null && anotherStudent.rollNumber==null) return 0;
int difference;
if(this.rollNumber==null && anotherStudent.rollNumber!=null) return 1;
if(this.rollNumber!=null && anotherStudent.rollNumber==null) return -1;
difference=this.rollNumber.compareTo(anotherStudent.rollNumber);
return difference;
}
public int hashCode()
{
if(this.rollNumber==null) return 0;
return this.rollNumber.hashCode();
}
}
