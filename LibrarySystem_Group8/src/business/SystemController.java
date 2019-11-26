package business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController implements ControllerInterface {
	public static Auth currentAuth = null;
	private static DataAccess da;
	private static HashMap<String, User> userMap;
	private static HashMap<String, LibraryMember> memberMap;
	private static HashMap<String, Book> bookMap;

	@Override
	public Auth login(String id, String password) throws LoginException {
		createAccess();
		if (!userMap.containsKey(id))
			throw new LoginException("ID " + id + " not found");
		String passwordFound = userMap.get(id).getPassword();
		if (!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		currentAuth = userMap.get(id).getAuthorization();
		return currentAuth;
	}

	@Override
	public List<String> allMemberIds() {
		createAccess();
		List<String> retval = new ArrayList<>();
		retval.addAll(memberMap.keySet());
		Collections.sort(retval);
		return retval;
	}

	@Override
	public List<String> allBookIds() {
		createAccess();
		List<String> retval = new ArrayList<>();
		retval.addAll(bookMap.keySet());
		Collections.sort(retval);
		return retval;
	}

	// Dalia
	@Override
	public boolean checkBookAvailability(String isbn) throws LibrarySystemException {
		createAccess();
		if (currentAuth != Auth.LIBRARIAN && currentAuth != Auth.BOTH)
			throw new LibrarySystemException("You are not authorized!");
		Book book = getBook(isbn);
		if (book == null)
			return false;
		System.out.println(book.isAvailable());
		return book.isAvailable();
	}

	// Dalia
	@Override
	public boolean checkoutBook(String memberId, String isbn) throws LibrarySystemException {
		createAccess();
		System.out.println("hello");
		if (currentAuth != Auth.LIBRARIAN && currentAuth != Auth.BOTH)
			throw new LibrarySystemException("You are not authorized!");
		// Get Member and Book
		LibraryMember member = getMember(memberId);
		Book book = getBook(isbn);
		// Check on nulls
		if (member == null || book == null)
			return false;
		BookCopy bookCopy = book.getNextAvailableCopy();
		// Create new checkout record
		if (bookCopy == null)
			throw new LibrarySystemException("No Available copies for book with '" + isbn + "' ISBN.");
		if (member.getCheckoutRec() == null)
			LibraryMember.createCheckoutRecord(member);
		member.addEntry(bookCopy, LocalDate.now(), LocalDate.now().plusDays(bookCopy.getBook().getMaxCheckoutLength()));
		bookCopy.changeAvailability();
		da.updateMember(member);
		da.updateBook(book);
		return true;
	}

	// Dalia
	@Override
	public Book getBook(String isbn) throws LibrarySystemException {
		// Search for Book with ISBN
		if (!allBookIds().contains(isbn))
			throw new LibrarySystemException("Book ISBN '" + isbn + "' was not found!");
		// Get Book
		return bookMap.get(isbn);
	}

	// Dalia
	@Override
	public LibraryMember getMember(String id) throws LibrarySystemException {
		// Search for Member with ID
		if (!allMemberIds().contains(id))
			throw new LibrarySystemException("Member ID '" + id + "' was not found!");
		LibraryMember member = null;
		// Get member
		member = memberMap.get(id);
		return member;
	}

	// Dalia
	@Override
	public BookCopy getBookCopy(String isbn) throws LibrarySystemException {
		// Search for Book with ISBN
		if (!allBookIds().contains(isbn))
			throw new LibrarySystemException("Book ISBN '" + isbn + "' was not found!");
		// Get Book
		Book book = bookMap.get(isbn);
		// Check Book availability
		if (!book.isAvailable())
			throw new LibrarySystemException("Book with ISBN '" + isbn + "' is not available for check out!");
		// Get next available copy & change availability
		BookCopy copy = book.getNextAvailableCopy();
		return copy;
	}

	// Dalia
	@Override
	public List<CheckoutRecordEntry> viewCheckoutHistory(String memberId) throws LibrarySystemException {
		createAccess();
		if (memberId == null)
			throw new LibrarySystemException("Please enter member id!");
		// Get Member
		if (!allMemberIds().contains(memberId))
			throw new LibrarySystemException("Member ID '" + memberId + "' was not found!");
		LibraryMember member = getMember(memberId);
		// Check if has checkout record
		if (member.getCheckoutRec() == null || member.getCheckoutRec().getEntries().isEmpty())
			throw new LibrarySystemException("Member '" + member.getFullName() + "' has no prior checkout record");
		return member.getCheckoutRec().getEntries();
	}

	// AYA
	@Override
	public List<Book> getAllBooks() {
		createAccess();
		List<Book> booksList = new ArrayList<Book>();
		Set<String> keys = bookMap.keySet();
		for (String key : keys) {
			booksList.add(bookMap.get(key));
		}
		return booksList;
	}

	// AYA
	@Override
	public String getBookCopies(Book book) {
		BookCopy[] bookCopy = book.getCopies();
		return String.valueOf(bookCopy.length);
	}

	// Aya
	public void updateBook(Book book) {
		createAccess();
		da.updateBook(book);
	}

	// Aya
	public void saveNewBook(Book book) throws LibrarySystemException {
		createAccess();
		if (book == null)
			throw new LibrarySystemException("book is empty");
		da.saveNewBook(book);
	}

	// AYA
	public Author getAuthorList(String name) throws LibrarySystemException {
		if (name == null)
			throw new LibrarySystemException("please enter first name of author");
		return new Author(name, "", "", new Address("", "", "", ""), "");

	}

	// AYA
	public Author addNewAuthor(String fName, String lName, String mobile, String bio, String street, String city,
			String state, String zip, String bookIsbn) throws LibrarySystemException {
		Address address = null;
		Author author = null;
		if (!street.equals("") && !city.equals("") && !state.equals("") && !zip.equals("")) {
			address = new Address(street, city, state, zip);
		}
		if (!fName.equals("") && !lName.equals("") && !mobile.equals("") && !bio.equals("")) {
			author = new Author(fName, lName, mobile, address, bio);
		} else {
			throw new LibrarySystemException("You should fill all fields");
		}

		Book book = getBook(bookIsbn);
		book.getAuthors().add(author);
		saveNewBook(book);

		System.out.println("+++   " + book.getAuthors().size());

		return new Author(fName, lName, mobile, address, bio);

	}

	// Reza
	public boolean addMember(String memberID, String firstname, String lastname, String tel, String street, String city,
			String state, String zip) {
		Address address = new Address(street, city, state, zip);
		LibraryMember member = new LibraryMember(memberID, firstname, lastname, tel, address);
		da.saveNewMember(member);
		return true;
	}

	// Reza
	public boolean deleteMember(String id) throws LibrarySystemException {
		memberMap = da.readMemberMap();
		// Search for Member with ID
		if (!memberMap.containsKey(id))
			throw new LibrarySystemException("Member ID '" + id + "' was not found!");
		da.deleteMember(id);
		return true;
	}

	// Reza
	@Override
	public int numberOfMembers() {
		List<String> list = allMemberIds();
		return list.size();
	}

	// Dalia
	private static void createAccess() {
		da = new DataAccessFacade();
		userMap = da.readUserMap();
		memberMap = da.readMemberMap();
		bookMap = da.readBooksMap();
	}
}
