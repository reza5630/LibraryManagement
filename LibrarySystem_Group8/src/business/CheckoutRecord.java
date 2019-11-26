package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckoutRecord implements Serializable {
	
	private static final long serialVersionUID = 2219991347225732081L;
	List<CheckoutRecordEntry> entries;

	CheckoutRecord() {
		entries = new ArrayList<CheckoutRecordEntry>();
	}

	public boolean addEntry(BookCopy bookCopy, LocalDate checkoutDate, LocalDate dueDate) {
		return entries.add(new CheckoutRecordEntry(bookCopy, checkoutDate, dueDate));
	}

	public List<CheckoutRecordEntry> getEntries() {
		return entries;
	}
}
