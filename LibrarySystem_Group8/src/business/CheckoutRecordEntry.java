package business;

import java.io.Serializable;
import java.time.LocalDate;

public class CheckoutRecordEntry implements Serializable {

	@Override
	public String toString() {
		return "CheckoutRecordEntry [bookCopy=" + bookCopy.getBook().getTitle() + ", checkoutDate=" + checkoutDate + ", dueDate=" + dueDate
				+ "]\n";
	}

	private static final long serialVersionUID = 4621170859333777209L;
	private BookCopy bookCopy;
	private LocalDate checkoutDate;
	private LocalDate dueDate;

	CheckoutRecordEntry(BookCopy bookCopy, LocalDate checkoutDate, LocalDate dueDate) {
		this.bookCopy = bookCopy;
		this.checkoutDate = checkoutDate;
		this.dueDate = dueDate;
	}

	public BookCopy getBookCopy() {
		return bookCopy;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public String getCheckoutDateProp() {
		return checkoutDate.toString();
	}

	public String getDueDateProp() {
		return dueDate.toString();
	}

	public String getBookTitle() {
		return bookCopy.getBook().getTitle();
	}
}
