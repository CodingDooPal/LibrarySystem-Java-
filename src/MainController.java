package qwer2345;

import java.util.*;
import java.io.*;

class LibraryController
{
	private LibraryView libraryView;
	private LibraryManager libraryManager;
	private UserManager userManager;
	private final int booksPerPage = 5;
	private final int maxBorrowBookCount = 3;
	private int currentPage = 1;
	private int totalPage = 0;

	LibraryController(LibraryView libraryView, LibraryManager libraryManager,
			UserManager userManager) {
		this.libraryView = libraryView;
		this.libraryManager = libraryManager;
		this.userManager = userManager;	
		this.totalPage = carculateTotalPage();
	}
	
	int getCurrentPage() {
		return this.currentPage;
	}
	
	int getTotalPage() {
		return this.totalPage;
	}
	
	void displayBookList(int currentPage, int totalPage) {
		libraryView.printHeader();
		int num = this.booksPerPage * (currentPage - 1);
		Vector<Book> bookList = this.libraryManager.getBookList();
		for(int i = 0; i < this.booksPerPage; ++i) {
			if(num + i >= bookList.size()) {
				break;
			}
			this.libraryView.printBookName(i + 1, bookList.get(num + i).getBookName());
		}
		this.libraryView.printBookListMenu(currentPage, totalPage);
	}
	
	int carculateTotalPage() {
		int size = this.libraryManager.getBookList().size();
		if (size % this.booksPerPage == 0) {
			return size / this.booksPerPage;
		}
		return (size / this.booksPerPage) + 1;
	}
	
	void startLibraryMenu(final int userIdx) {
		this.libraryView.setCurrentViewState(ViewState.Library);
		this.totalPage = carculateTotalPage();
		this.currentPage = 1;
		while (true) {
			this.displayBookList(this.currentPage, this.totalPage);
			int input = this.libraryView.input();
			switch (input) {
			case 1: // 이전 페이지
			{
				if (this.currentPage > 1) {
					--this.currentPage;
				}
				else {
					// 이전 페이지가 없다는 메시지 출력
					this.libraryView.printNoPage(PageState.Prev);
				}
				break;
			}
			case 2: // 다음 페이지
			{
				if (this.currentPage < this.totalPage) {
					++this.currentPage;
				}
				else {
					// 다음 페이지가 없다는 메시지 출력
					this.libraryView.printNoPage(PageState.Next);
				}
				break;
			}
			case 3: // 원하는 페이지 이동
			{
				int inputPage = 0;
				this.libraryView.printInputpage();
				inputPage = this.libraryView.input();
				if (inputPage > 0 && inputPage <= this.totalPage) {
					this.currentPage = inputPage;
				}
				else {
					// 존재하지 않는 페이지라는 메시지 출력
					this.libraryView.printNoPage(PageState.None);
				}
				break;
			}
			case 4: // 도서 선택
			{
				int bookIdx = selectBook();
				if(bookIdx == -1) {
					// 존재하지 않는 책 오류
					break;
				}
				int userInput = -1;
				while (userInput != 0 && userInput != 1) {
					if(userIdx == -1) {
						this.libraryView.printModifyBook();
						userInput = this.libraryView.input();
						modifyBook(userInput, bookIdx);
					}
					else {
						this.libraryView.printReserveBook();
						userInput = this.libraryView.input();
						reserveBook(userInput, userIdx, bookIdx);
					}
				}
				break;
			}
			case 5: // 도서 검색(나중에 몇 페이지에 있는 지도 함께 출력하도록 변경)
			{
				// 도서 검색 view 띄우고 string input 받는다.
				this.libraryView.printSearchBook();
				String searchBookName = this.libraryView.inputStr();
				Vector<Book> searchBookList = searchBook(searchBookName);
				// 객체를 벡터로 받아서 empty이면 찾는 책이 없고
				// 데이터가 1이상이면 출력
				if(searchBookList.isEmpty()) {
					// 해당 제목의 책이 없다
					this.libraryView.printNotFoundBook();
				}
				else {
					// 찾은 책 목록 출력
					this.libraryView.printBookInfo(searchBookList);
				}
				
				break;
			}
			case 0: // 나가기
			{
				
				return;
			}
			default:
			{
				// 에러 메시지 출력
				this.libraryView.printInputError();
				break;
			}
			}
		}
	}
	
	int selectBook() {
		this.libraryView.printSelectBook();
		int input = this.libraryView.input();
		int num = this.libraryManager.getBookList().size();
		boolean isLastPage = (this.currentPage == this.totalPage);
		int bookIdx = (this.currentPage - 1) * this.booksPerPage + (input - 1);
		
		if(input <= 0 || input > this.booksPerPage) {
			// 잘못된 입력 에러 메시지
			this.libraryView.printInputError();
			return -1;
		}
		if (isLastPage && num % this.booksPerPage != 0) {
			if (input > num % this.booksPerPage) {
				// 잘못된 입력 에러 메시지
				this.libraryView.printInputError();
				return -1;
			}
		}
		
		Book selectbook = this.libraryManager.getBook(bookIdx);
		this.libraryView.printBookInfo(selectbook.getBookName(), selectbook.getBookAuthor(),
			selectbook.getAvailableCount());

		return bookIdx;
	}
	
	void reserveBook(final int input, final int userIdx, final int bookIdx) {
		switch (input)
		{
		case 1: // 대출 예약
		{
			Book book = this.libraryManager.getBookList().get(bookIdx);

			// 빌릴 수 있는 책이 없다면
			if (book.getAvailableCount() == 0) {
				// 빌릴 수 있는 책이 없습니다. 출력
				this.libraryView.printNoAvailableBook();
				break;
			}
			// 대출 한도가 초과라면
			if (this.userManager.getCurrentUser().getBorrowedBookCount() == this.maxBorrowBookCount) {
				this.libraryView.printMaxBorrowedCount(this.userManager.getCurrentUser().getUserName());
				break;
			}
			// 빌린 유저 리스트에 현재 유저가 있는 지 확인
			if (this.libraryManager.hasBorrowedBook(userIdx) >= 0) {
				int borrowIdx = this.libraryManager.hasBorrowedBook(userIdx);
				Borrow userBorrowed = this.libraryManager.getBorrowList().get(borrowIdx);
				userBorrowed.getBorrowBookList().add(book);
				
			}
			else {
				Borrow borrow = new Borrow(book, userIdx);
				this.libraryManager.getBorrowList().add(borrow);
			}
			// 해당하는 책의 대출 가능 수를 하나 줄임.
			book.sumAvailableCount();
			// 유저의 빌린 책의 수를 하나 늘림.
			this.userManager.getCurrentUser().addBorrowedBookCount();
			break;
		}
		case 0: // 나가기
		{

			break;
		}
		default:
		{
			this.libraryView.printInputError();
			break;
		}
		}
	}
	
	void displayBorrowedBookList() {
		int borrowListIdx = this.libraryManager.hasBorrowedBook(this.userManager.getCurrentUserIdx());
		if (borrowListIdx >= 0){
			Borrow userBorrowedBook = this.libraryManager.getBorrow(borrowListIdx);
			this.libraryView.printBookInfo(userBorrowedBook.getBorrowBookList());
		}
		else {
			this.libraryView.printNoBorrowedBook();
		}
	}
	
	Vector<Book> searchBook(String searchBookName) {
		Vector<Book> searchBookList = new Vector<>();
		for(Book book : this.libraryManager.getBookList()) {
			if(book.getBookName().equals(searchBookName)) {
				searchBookList.add(book);
			}
		}
		return searchBookList;		
	}
	
	void modifyBook(int num, int bookIdx) {
		Book book = this.libraryManager.getBookList().get(bookIdx);
		switch (num) {
		case 1:
			int userInput = -1;
			while(userInput != 0) {
				this.libraryView.printModifyBookMenu();
				userInput = this.libraryView.input();
				switch(userInput) {
				case 1: {
					String inputStr = this.libraryView.inputStr();
					book.setBookName(inputStr);
					break;
				}
				case 2: {
					String inputStr = this.libraryView.inputStr();
					book.setBookAuthor(inputStr);
					break;
				}
				case 3: { 
					int inputNum = this.libraryView.input();
					book.setAvailableCount(inputNum);
					break;
				}
				case 0: {
					// 이전 화면으로 돌아갑니다.
					break;
				}
				default: {
					// 유효하지 않은 입력 에러 출력
					this.libraryView.printInputError();
					break;
				}
				}
			}
			
			break;
		case 0:
			// 이전 화면으로 돌아갑니다.
			break;
		default:
			// 유효하지 않은 입력 에러 출력
			this.libraryView.printInputError();
			break;
		}
	}
	
	// 관리자가 책 추가 또는 삭제
	void bookInsertOrDelete() {
		this.libraryView.printBookInsertOrDeleteMenu();
		int inputNum = this.libraryView.input();
		switch(inputNum) {
		case 1:
		{
			this.libraryView.printInputBookName();
			String name = this.libraryView.inputStr();
			this.libraryView.printInputBookAuthor();
			String author = this.libraryView.inputStr();
			this.libraryView.printInputBookAvilableCount();
			int avilableCount = this.libraryView.input();
			
			Book book = new Book(name, author, avilableCount);
			this.libraryManager.getBookList().add(book);
			
			this.libraryView.printSuccessBookInsert();
			
			break;
		}
		case 2:
		{
			// 삭제할 책 이름 입력
			this.libraryView.printInputBookName();
			String searchBookName = this.libraryView.inputStr();
			Vector<Book> searchBookList = searchBook(searchBookName); // bookList 객체는 삭제가 안 됨.
			// 해결하기 위해서 bookList에 인덱스로 직접 접근해야 함. 따라서 searchBook을 하면서 bookList에 어디에 위치하는지에 대한 index도 함께 추출할
			// 방법을 생각해야 됨.
			if(searchBookList.isEmpty()) {
				// 해당 제목의 책이 없다
				this.libraryView.printNotFoundBook();
			}
			else {
				// 찾은 책 목록 출력
				this.libraryView.printBookInfo(searchBookList);
			}
			this.libraryView.printSelectBook();
			// 잘못 진입했을 때를 고려해서 -1을 입력하면 나가도록 설정
			this.libraryView.printExitDeleteBookMenu();
			int inputNum2 = this.libraryView.input();
			if(inputNum2 == -1) {
				break;
			}
			searchBookList.remove(inputNum2 - 1);
			this.libraryView.printSuccessDeleteBook();
			
			break;
		}
		case 0:
		{
			// 돌아가기
			break;
		}
		default:
			// 유효하지 않는 입력 에러 출력
			this.libraryView.printInputError();
		}
	}
}

class UserController
{
	UserController(UserView userView, UserManager userManager,
		LibraryController libraryController) {
		this.userView = userView;
		this.userManager = userManager;
		this.libraryController = libraryController;
	}
	
	boolean login(final String id, final String pw) {
		if (this.userManager.login(id, pw)) {
			return true;
		}
		return false;
	}
	
	boolean signUp(final User user) {
		// ID 중복 여부 확인 추가
		if (this.userManager.searchUserId(user.getUserId()))
			return false;
		this.userManager.insertNewUser(user);
		return true;
	}
	
	String getName() {
		String name = this.userManager.getCurrentUser().getUserName();
		return name;
	}
	
	void startUserMenu() {
		int input = 0;
		this.userView.setCurrentViewState(ViewState.User);
		while(true) {
			this.userView.printMenu();
			input = this.userView.input();
			switch (input) {
			case 1: // 도서 목록, 검색 및 대출 예약
			{
				this.libraryController.startLibraryMenu(this.userManager.getCurrentUserIdx());
				break;
			}
			case 2: // 대출 받은 책 목록 확인
			{
				this.libraryController.displayBorrowedBookList();
				break;
			}
			case 3: // 사용자 계정 정보 변경
			{
				this.userView.printUserAccountModify();
				int num = this.userView.input();
				switch (num) {
				case 1: // id, pw, 이름 변경
				case 2:
				case 3:
					this.userView.printModify(num);
					String inputStr = this.userView.inputStr();
					this.userManager.modifyUserInfo(inputStr, num);
					this.userView.printModifySuccess(num);
					break;
				case 4: // 학번 변경
					this.userView.printModify(num);
					int inputNum = this.userView.input();
					this.userManager.modifyUserInfo(inputNum, num);
					this.userView.printModifySuccess(num);
					break;
				default:
					// 에러코드 출력
					this.userView.printInputError();
					break;
				}
				
				break;
			}
			case 0: // 계정 로그아웃
			{
				this.userView.printEndMassage();
				return;
			}
			default:
			{
				// 올바르지 않은 입력값 에러 출력
				break;
			}
			}
		}
	}
	private	UserView userView;
	private UserManager userManager;
	private LibraryController libraryController;
}

class AdminController
{
	AdminController(AdminView adminView, AdminManager adminManager,
		LibraryController libraryController) {
		this.adminView = adminView;
		this.adminManager = adminManager;
		this.libraryController = libraryController;
	}
	
	boolean login(final String id, final String pw) {
		if (this.adminManager.login(id, pw)) {
			return true;
		}
		return false;
	}
	
	void startAdminMenu() {
		int input = 0;
		this.adminView.setCurrentViewState(ViewState.Admin);
		while(true) {
			this.adminView.printMenu();
			input = this.adminView.input();
			switch (input) {
			case 1: // 도서 검색 및 수정
			{
				this.libraryController.startLibraryMenu(-1);
				break;
			}
			case 2: // 도서 추가 및 삭제
			{
				this.libraryController.bookInsertOrDelete();
				break;
			}
			case 3: // 사용자 관리
			{

				break;
			}
			case 4: // 관리자 계정 정보 변경
			{

				break;
			}
			case 0: // 계정 로그아웃
			{
				this.adminView.printEndMassage();
				return;
			}
			default:
			{
				// 올바르지 않은 입력값 에러 출력
				break;
			}
			}
		}
	}
private
	AdminView adminView;
	AdminManager adminManager;
	LibraryController libraryController;
}

// 메인 화면에 대한 컨트롤러
class MainController
{
	// main에서 참조로 has-a에 연결된 클래스에 동일한 객체 전달
	private final MainView mainView = new MainView();
	private final UserView userView = new UserView();
	private final UserManager userManager = new UserManager();
	private final AdminView adminView = new AdminView();
	private final AdminManager adminManager = new AdminManager();
	private final LibraryView libraryView = new LibraryView();
	private final LibraryManager libraryManager = new LibraryManager();

	private final LibraryController libraryController;
	private final UserController userController;
	private final AdminController adminController;
	
	MainController() {
		this.libraryController = new LibraryController(libraryView, libraryManager, userManager);
		this.userController = new UserController(userView, userManager, libraryController);
		this.adminController = new AdminController(adminView, adminManager, libraryController);
	}

	void startMainMenu() {
		int input = 0;
		testCode(); // 테스트용 유저 및 book 인풋
		this.mainView.setCurrentViewState(ViewState.Main);
		while (true) {
			this.mainView.printMenu();
			input = this.mainView.input();
			switch (input) {
			case 1: // user 로그인
			{
				String id = userView.inputId();
				String pw = userView.inputPw();
				if (this.userController.login(id, pw)) {
					this.userView.printLoginSuccess(this.userController.getName());
				}
				else {
					this.userView.printLoginFail();
					break;
				}
				this.userController.startUserMenu();
				break;
			}
			case 2: // admin 로그인
			{
				String id = adminView.inputId();
				String pw = adminView.inputPw();
				if (this.adminController.login(id, pw)) {
					this.adminView.printLoginSuccess();
				}
				else {
					this.adminView.printLoginFail();
					break;
				}
				// AdminController 실행 추가
				this.adminController.startAdminMenu();
				break;
			}
			case 3: // 회원가입
			{
				User user = this.mainView.inputSignUp();
				// UserManager 직접 호출x, UserController 호출 후 사용하도록 변경
				if (this.userController.signUp(user)) {
					this.userView.printSignUpSuccess();
				}
				else {
					this.userView.printSignUpFail();
				}
				break;
			}
			case 0:
			{
				this.mainView.printEndMassage();
				return;
			}
			default:
			{
				// 올바르지 않은 입력값 에러 출력
				this.mainView.printInputError();
				break;
			}
			}
		}
	}
	
	void testCode() {
		User user = new User("user", "password", "박종혁", 20223668);
		this.userManager.insertNewUser(user);
		for (int i = 0; i < 52; ++i) {
				this.libraryManager.insertNewBook(new Book("test book " + Integer.toString(i + 1), "jpark", 1));
		}
	}
}
