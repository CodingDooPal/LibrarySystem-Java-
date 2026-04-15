import java.util.*;
import java.io.*;

//Book 객체(책에 대한 속성)
class Book {
	Book() {}
	Book(final String name, final String author, final int count) {
		this.bookName = name;
		this.bookAuthor = author;
		this.availableCount = count;
	}
	
	String getBookName() {
		return this.bookName;
	}
	
	String getBookAuthor() {
		return this.bookAuthor;
	}
	
	int getAvailableCount() {
		return this.availableCount;
	}
	
	void addAvailableCount() {
		++this.availableCount;
	}
	
	void sumAvailableCount() {
		--this.availableCount;
	}
	
	//boolean isLoaned() {}
	private	String bookName;
	private String bookAuthor;
	private int availableCount;
	//boolean m_isLoaned{ false }; // 대출 가능한 수량이 없으면 true로 변경
};

class BorrowBook {
	BorrowBook(final String name, final String author) {
		this.bookName = name;
		this.bookAuthor = author;
	}
	
	String bookName;
	String bookAuthor;
};

//Borrow 객체(하나의 유저가 빌린 책들 저장)
class Borrow {
	Borrow() {}
	Borrow(final String name, final String author, final int idx) {
		BorrowBook borrowBook = new BorrowBook(name, author);
		this.borrowBookList.add(borrowBook);
		this.userIdx = idx;
	}
	
	int getUserIdx() {
		return this.userIdx;
	}
	
	Vector<BorrowBook> getBorrowBookList() {
		return this.borrowBookList;
	}
	
	private int userIdx;
    private Vector<BorrowBook> borrowBookList = new Vector<>();
};

//도서 관리
public class LibraryManager {
	void insertNewBook(final Book book) {
		this.bookList.add(book);
	}
	
	Vector<Book> getBookList() {
		return this.bookList;
	}
	
	Vector<Borrow> getBorrowList() {
		return this.borrowList;
	}
	
	Book getBook(int idx) {
		return this.bookList.get(idx);
	}
	
	Borrow getBorrow(int borrowListIdx) {
		return this.borrowList.get(borrowListIdx);
	}
	
	// 빌린 책이 있는지 확인
	int hasBorrowedBook(final int userIdx) {
		int idx = 0;
		for (Borrow borrow : this.borrowList) {
			if(borrow.getUserIdx() == userIdx) {
				return idx;
			}
			++idx;
		}
		return -1;
	}
	private Vector<Book> bookList = new Vector<>();
	private Vector<Borrow> borrowList = new Vector<>();
};
