import java.io.*;
import java.util.*;

enum ViewState { Main, User, Admin, Library };
enum PageState { Prev, Next, None };

public class MainView {
	void printHeader() {
		System.out.println("\n=== 000 도서관 시스템 " + this.printCurrentState(getCurrentViewState()) + 
				" Menu ===");
	}
	
	void printMenu() {
		printHeader();
		System.out.println("1. 사용자 로그인");
		System.out.println("2. 관리자 로그인");
		System.out.println("3. 회원가입");
		System.out.println("0. 시스템 종료");
	}
	
	void setCurrentViewState(ViewState state) {
		this.currentViewState = state;
	}
	
	ViewState getCurrentViewState() {
		return this.currentViewState;
	}
	
	String printCurrentState(final ViewState state) {
		switch (state) {
		case ViewState.Main:
			return "Main";
		case ViewState.User:
			return "User";
		case ViewState.Admin:
			return "Admin";
		case ViewState.Library:
			return "Library";
		default:
			// 에러 코드 삽입
			return "Error";
		}
	}
	
	void printErrorMassage(final int errorCode) {
		// 에러 코드별로 출력 설정
	}
	
	void printEndMassage() {
		printSystemNotice();
		System.out.println("프로그램 종료");
	}

	int input() {
		System.out.print(">> ");
		try {
			int num = Integer.parseInt(scanner.nextLine());
			return num;
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	String inputStr() {
		System.out.print(">> ");
		String str = scanner.nextLine();
		return str;
	}
	
	void printInputError() {
		printSystemNotice();
		System.out.println("잘못된 입력입니다.");
	}

	User inputSignUp() {
		System.out.println("\n사용할 id를 입력하세요: ");
		String id = scanner.nextLine();
		System.out.println("사용할 password를 입력하세요: ");
		String pw = scanner.nextLine();
		System.out.println("이름을 입력하세요: ");
		String name = scanner.nextLine();
		System.out.println("학번을 입력하세요: ");
		int studentNum = Integer.parseInt(scanner.nextLine());
		
		return new User(id, pw, name, studentNum);
	}
	
	String inputId() {
		System.out.print("\nid를 입력하세요.\n>>");
		String id = scanner.nextLine();
		return id;
	}
	
	String inputPw() {
		System.out.print("\npassword를 입력하세요.\n>>");
		String pw = scanner.nextLine();
		return pw;
	}
	
	void printLoginFail() {
		printSystemNotice();
		System.out.println("일치하는 정보가 없습니다. id와 password를 다시 입력해주세요.");
	}
	
	void printSystemNotice() {
		System.out.println("\n\n***System Notice***");
	}

	// 인터페이스가 현재 Main, User, Admin 중 어느 것인지 알려주는 녀석
	protected ViewState currentViewState = ViewState.Main;
	protected Scanner scanner = new Scanner(System.in);
}

//User로 접속 시 보여지는 User 화면 관련
class UserView extends MainView
{	
	@Override
	void printMenu() {
		printHeader();
		System.out.println("1. 도서 목록, 검색 및 대출 예약");
		System.out.println("2. 대출 목록");
		System.out.println("3. 사용자 계정 정보 변경");
		System.out.println("0. 계정 로그아웃");
	}

	void printLoginSuccess(final String name) {
		printSystemNotice();
		System.out.println(name + "님 환영합니다!");
	}
	
	void printSignUpSuccess() {
		printSystemNotice();
		System.out.println("회원가입에 성공했습니다.");
	}
	
	void printSignUpFail() {
		printSystemNotice();
		System.out.println("회원가입에 실패했습니다.");
	}
	
	void printUserAccountModify() {
		printSystemNotice();
		System.out.println("수정할 정보를 선택하세요.");
		System.out.println("1. ID 변경");
		System.out.println("2. Password 변경");
		System.out.println("3. 이름 변경");
		System.out.println("4. 학번 변경");
	}
	
	void printModify(int num) {
		printSystemNotice();
		switch(num) {
		case 1:
			System.out.println("변경할 ID를 입력하세요.");
			break;
		case 2:
			System.out.println("변경할 Password를 입력하세요.");
			break;
		case 3: 
			System.out.println("변경할 이름을 입력하세요.");
			break;
		case 4:
			System.out.println("변경할 학번을 입력하세요.");
			break;
		}
	}
	
	void printModifySuccess(int num) {
		printSystemNotice();
		switch(num) {
		case 1:
			System.out.println("Id가 변경되었습니다!");
			break;
		case 2:
			System.out.println("Password가 변경되었습니다!");
			break;
		case 3: 
			System.out.println("이름이 변경되었습니다!");
			break;
		case 4:
			System.out.println("학번이 변경되었습니다!");
			break;
		}
	}
	
	@Override
	void printErrorMassage(final int errorCode) {
		// 에러 메시지 만들자.
	}
}

//Admin으로 접속 시 보여지는 Admin 화면 관련
class AdminView extends MainView
{
	@Override
	void printMenu() {
		printHeader();
		System.out.println("1. 도서 검색 및 수정");
		System.out.println("2. 도서 추가 및 삭제");
		System.out.println("3. 사용자 관리");
		System.out.println("4. 관리자 계정 정보 변경");
		System.out.println("0. 계정 로그아웃");
	}
	
	void printLoginSuccess() {
		printSystemNotice();
		System.out.println("관리자님 환영합니다!");
	}
	
	@Override
	void printErrorMassage(final int errorCode) {
		// 에러 메시지 만들자.
	}
}

class LibraryView extends MainView
{
	// printHeader();
	
	@Override
	void printErrorMassage(final int errorCode) {
		// 에러 메시지 만들자.
	}
	
	void printBookName(final int num, final String name) {
		System.out.println(num + ". " + name);
	}
	
	void printBookListMenu(int currentPage, int totalPage) {
		System.out.println("=====" + currentPage + "/" + totalPage + "=====");
		System.out.println("1. 이전 페이지");
		System.out.println("2. 다음 페이지");
		System.out.println("3. 원하는 페이지 이동");
		System.out.println("4. 도서 선택");
		System.out.println("5. 도서 검색");
		System.out.println("0. 나가기");
	}
	
	void printInputpage() {
		printSystemNotice();
		System.out.println("원하는 페이지를 입력하세요.");
	}
	
	void printSelectBook() {
		printSystemNotice();
		System.out.println("원하는 책의 번호를 입력하세요.");
	}
	
	void printBookInfo(final String name, final String author, final int count) {
		System.out.println("=====> " + name + " <=====");
		System.out.println("저자: " + author);
		System.out.println("남은 권수: " + count);
	}
	
	void printReserveBook() {
		System.out.println("====================");
		System.out.println("1. 대출 예약하기");
		System.out.println("0. 돌아가기");
	}

	void printNoPage(PageState pageState) {
		printSystemNotice();
		switch(pageState) {
		case PageState.Prev:
			System.out.println("이전 페이지가 없습니다.");
			break;
		case PageState.Next:
			System.out.println("다음 페이지가 없습니다.");
			break;
		case PageState.None:
			System.out.println("해당 페이지는 존재하지 않습니다.");
			break;
		default:
			System.out.println("pageState 값에 문제 발생"); // Error Code에 삽입
			break;
		}
	}
	
	void printNoAvailableBook() {
		printSystemNotice();
		System.out.println("현재 예약 가능한 책이 없습니다.");
	}
	
	void printMaxBorrowedCount(final String name) {
		printSystemNotice();
		System.out.println(name + "님의 대출 한도가 초과되어 대출이 불가능합니다.");
	}
	
	void printBookInfo(Vector<Book> bookList) {
		int num = 1;
		for(Book book : bookList) {
			System.out.println("\n" + num + "번째 책");
			System.out.println("책 이름: " + book.getBookName());
			System.out.println("책 저자: " + book.getBookAuthor());
			System.out.println("====================");
			++num;
		}
	}
	
	void printNoBorrowedBook() {
		printSystemNotice();
		System.out.println("현재 대출받은 책이 없습니다.");
	}
	
	void printSearchBook() {
		printSystemNotice();
		System.out.println("검색할 책의 이름을 입력하세요.");
	}
	
	void printNotFoundBook() {
		printSystemNotice();
		System.out.println("해당 제목의 책을 찾을 수 없습니다.");
	}
	
	void printModifyBook() {
		System.out.println("====================");
		System.out.println("1. 도서 정보 변경하기");
		System.out.println("0. 돌아가기");
	}
	
	void printModifyBookMenu() {
		System.out.println("수정할 정보를 선택하세요.");
		System.out.println("1. 책 이름 변경하기");
		System.out.println("2. 책 저자 변경하기");
		System.out.println("3. 책 수량 변경하기");
		System.out.println("0. 돌아가기");
	}
	
	void printBookInsertOrDeleteMenu() {
		System.out.println("\n1. 책 추가하기");
		System.out.println("2. 책 삭제하기");
		System.out.println("0. 돌아가기");
	}
	
	void printInputBookName() {
		System.out.println("책 이름을 입력하세요.");
	}
	
	void printInputBookAuthor() {
		System.out.println("책 저자를 입력하세요.");
	}
	
	void printInputBookAvilableCount() {
		System.out.println("이용 가능한 수량을 입력하세요.");
	}
	
	void printSuccessBookInsert() {
		System.out.println("새로운 책이 추가되었습니다.");
	}
	
	void printExitDeleteBookMenu() {
		System.out.println("삭제하고 싶지 않거나 삭제할 책 이름을 잘못 입력했을 경우 -1을 입력하세요.");
	}
	
	void printSuccessDeleteBook() {
		System.out.println("삭제에 성공했습니다.");
	}
}
