package business;

import java.io.Serializable;
import java.time.LocalDate;

final public class LibraryMember extends Person implements Serializable {

	private static final long serialVersionUID = -2226197306790714013L;
	private String memberId;
	private CheckoutRecord checkoutRec;

	public LibraryMember(String memberId, String fname, String lname, String tel, Address add) {
		super(fname, lname, tel, add);
		this.memberId = memberId;
	}

	public static void createCheckoutRecord(LibraryMember member) {
		member.checkoutRec = new CheckoutRecord();
	}

	public CheckoutRecord getCheckoutRec() {
		return checkoutRec;
	}

	public String getMemberId() {
		return memberId;
	}

	@Override
	public String toString() {
		return "Member Info: " + "ID: " + memberId + ", name: " + getFirstName() + " " + getLastName() + ", "
				+ getTelephone() + " " + getAddress();
	}

	public boolean addEntry(BookCopy bookCopy, LocalDate checkoutDate, LocalDate dueDate) {
		return checkoutRec.addEntry(bookCopy, checkoutDate, dueDate);
	}

}
