package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final public class Book implements Serializable {
	private static final long serialVersionUID = 6110690276685962829L;
	private BookCopy[] copies;
	private List<Author> authors;
	private String isbn;
	private String title;
	private int maxCheckoutLength;

	public int getNumCopies() {
		return copies.length;
	}

	public String getTitle() {
		return title;
	}

	public BookCopy[] getCopies() {
		return copies;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void addAuthor(Author author) {
		

		authors.add(author);
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getCount() {
		return String.valueOf(getNumCopies());
	}

	public String getNumberOfAuthors() {
		return String.valueOf(getAuthors().size());
	}

	public Book(String isbn, String title, int maxCheckoutLength, Author author) {
		this.isbn = isbn;
		this.title = title;
		this.maxCheckoutLength = maxCheckoutLength;
		// this.authors = Collections.unmodifiableList(authors);
		authors = new ArrayList<>();
		authors.add(author);
		copies = new BookCopy[] { new BookCopy(this, 1, true) };

	}

	public void updateCopies(BookCopy copy) {
		if (this.equals(copy.getBook())) {
			for (int i = 0; i < copies.length; i++) {
				if (copies[i].getCopyNum() == copy.getCopyNum())
					copies[i] = copy;
			}
		}
	}

	public List<Integer> getCopyNums() {
		List<Integer> copyNums = new ArrayList<>();
		for (BookCopy c : copies)
			copyNums.add(c.getCopyNum());
		return copyNums;
	}

	public void addCopy() {
		BookCopy[] newArr = new BookCopy[copies.length + 1];
		System.arraycopy(copies, 0, newArr, 0, copies.length);
		newArr[copies.length] = new BookCopy(this, copies.length + 1, true);
		copies = newArr;
	}

	public boolean isAvailable() {
		if (copies.length <= 0)
			return false;
		for (BookCopy copy : copies) {
			if (!copy.isAvailable())
				return false;
		}
		return true;
	}

	public BookCopy getNextAvailableCopy() {
		if (getNumCopies() == 0)
			return null;
		for (int i = 0; i < copies.length; i++) {
			if (copies[i].isAvailable())
				return copies[i];
		}
		return null;
	}

	public BookCopy getCopy(int copyNum) {
		if (getNumCopies() == 0 || copyNum >= getNumCopies())
			return null;
		return copies[copyNum];
	}

	public int getMaxCheckoutLength() {
		return maxCheckoutLength;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	@Override
	public boolean equals(Object ob) {
		if (ob == null)
			return false;
		if (ob.getClass() != getClass())
			return false;
		Book b = (Book) ob;
		return b.isbn.equals(isbn);
	}

	@Override
	public String toString() {
		return "isbn: " + isbn + ", maxLength: " + maxCheckoutLength + ", available: " + isAvailable();
	}

}
