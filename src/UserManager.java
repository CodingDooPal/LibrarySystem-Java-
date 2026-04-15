import java.io.*;
import java.util.*;

class User {
	User() {}
	User(final String id, final String pw, final String name, final int studentNum) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.studentNum = studentNum;
	}
	
	String getUserId() {
		return this.id;
	}
	
	String getUserPw() {
		return this.pw;
	}
	
	String getUserName() {
		return this.name;
	}
	
	void setUserId(String id) {
		this.id = id;
	}
	
	void setUserPw(String pw) {
		this.pw = pw;
	}
	
	void setUserName(String name) {
		this.name = name;
	}
	
	void setUserStudentNum(int num) {
		this.studentNum = num;
	}
	
	int getBorrowedBookCount() {
		return this.borrowedBookCount;
	}
	
	void addBorrowedBookCount() {
		++this.borrowedBookCount;
	}
	
	void sumBorrowedBookCount() {
		--this.borrowedBookCount;
	}
private
	String id;
	String pw;
	String name;
	int studentNum;
	int borrowedBookCount; // 빌린 책의 개수(최대 3권)
}

class Admin
{
	String getAdminId() {
		return this.id;
	}

	String getAdminPw() {
		return this.pw;		
	}
	
	private String id = "admin";
	private String pw = "password"; // 나중에 숨기거나 암호화 가능할지 생각해봐야함
	
};

public class UserManager {
	User getCurrentUser() {
		return this.userInfoList.get(currentUserIdx);
	}
	
	int getCurrentUserIdx() {
		return this.currentUserIdx;
	}

	boolean login(final String id, final String pw) {
		int idx = 0;
		for(User user : this.userInfoList) {
			if(id.equals(user.getUserId()) && pw.equals(user.getUserPw())) {
				this.currentUserIdx = idx;
				return true;
			}
			++idx;
		}
		return false;
	}
	
	boolean searchUserId(final String id) {
		for(User user : this.userInfoList) {
			if(id.equals(user.getUserId())) {
				return true;
			}
		}
		return false;
	}
	
	void insertNewUser(User user) {
		this.userInfoList.add(user);
	}
	
	void modifyUserInfo(String input, int num) {
		switch(num) {
		case 1:
			userInfoList.get(currentUserIdx).setUserId(input);
			break;
		case 2: 
			userInfoList.get(currentUserIdx).setUserPw(input);
			break;
		case 3:
			userInfoList.get(currentUserIdx).setUserName(input);
			break;
		}
	}
	
	void modifyUserInfo(int input, int num) {
		userInfoList.get(currentUserIdx).setUserStudentNum(num);
	}
	
	private Vector<User> userInfoList = new Vector<>();
	private	int currentUserIdx = -1;
}

class AdminManager
{
	boolean login(final String id, final String pw) {
		if(id.equals(admin.getAdminId()) && pw.equals(admin.getAdminPw())) {
			return true;
		}
		return false;
	}

	private	Admin admin = new Admin();
}
