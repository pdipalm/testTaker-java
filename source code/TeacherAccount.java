
public class TeacherAccount {
	private String teacherName;
	private String userName;
	private long acctNum;
	
	TeacherAccount(long num, String name, String un){
		this.setTeacherName(name);
		this.setAcctNum(num);
		this.setUserName(un);
	}

	public long getAcctNum() {
		return acctNum;
	}

	public void setAcctNum(long acctNum) {
		this.acctNum = acctNum;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
