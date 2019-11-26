package business;

import java.util.List;

import dataaccess.Auth;

public interface ControllerInterface {

	public Auth login(String id, String password) throws LoginException;

	boolean checkBookAvailability(String isbn) throws LibrarySystemException;

	public boolean checkoutBook(String memberId, String isbn) throws LibrarySystemException;

	public List<CheckoutRecordEntry> viewCheckoutHistory(String memberId) throws LibrarySystemException;

	public LibraryMember getMember(String id) throws LibrarySystemException;

	public BookCopy getBookCopy(String isbn) throws LibrarySystemException;

	public Book getBook(String isbn) throws LibrarySystemException;

	public List<String> allMemberIds();

	public List<String> allBookIds();

	public List<Book> getAllBooks();

	public String getBookCopies(Book book);

	public int numberOfMembers();

}
