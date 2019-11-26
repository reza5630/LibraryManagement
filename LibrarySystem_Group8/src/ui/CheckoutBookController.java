package ui;

import java.net.URL;
import java.util.ResourceBundle;

import business.LibrarySystemException;
import business.SystemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CheckoutBookController implements Initializable {
	public static final CheckoutBookController INSTANCE = new CheckoutBookController();
	private boolean isInitialized = false;
	private static SystemController sc;

	@FXML
	private TextField txt_memberId;
	@FXML
	private TextField txt_bookIsbn;
	@FXML
	private Button btn_checkAvailablity;
	@FXML
	private Button btn_checkout;
	@FXML
	private Label lbl_status;

	@FXML
	public void checkAvailablity(ActionEvent evt) {
		sc = new SystemController();
		String bookIsbn = txt_bookIsbn.getText();
		try {
			if (bookIsbn == null || bookIsbn.isEmpty())
				throw new LibrarySystemException("Book ISBN cannot be empty");
			if (sc.checkBookAvailability(bookIsbn))
				showStatus("Available!", true);
			else
				showStatus("Not Availabe!", false);
		} catch (LibrarySystemException exp) {
			showStatus(exp.getMessage(), false);
		}
	}

	@FXML
	public void checkoutBook(ActionEvent evt) {
		sc = new SystemController();
		String memberId = txt_memberId.getText();
		String bookIsbn = txt_bookIsbn.getText();
		try {
			if (memberId == null || memberId.isEmpty())
				throw new LibrarySystemException("Member ID cannot be empty");
			if (bookIsbn == null || bookIsbn.isEmpty())
				throw new LibrarySystemException("Book ISBN cannot be empty");
			if (sc.checkoutBook(memberId, bookIsbn))
				showStatus("Checkout Successful!", true);
			else
				showStatus("Checkout Unsuccessful!", false);
		} catch (LibrarySystemException exp) {
			showStatus(exp.getMessage(), false);
		}
	}

	private void showStatus(String status, boolean success) {
		lbl_status.setText(status);
		if (!success)
			lbl_status.setTextFill(Color.web("#800000"));
		if (success)
			lbl_status.setTextFill(Color.web("#006400"));
		lbl_status.setVisible(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert btn_checkAvailablity != null : "fx:id=\"btn_checkAvailablity\" was not injected: check your FXML file 'CheckoutBook.fxml'.";
		assert btn_checkout != null : "fx:id=\"btn_checkout\" was not injected: check your FXML file 'CheckoutBook.fxml'.";
		assert lbl_status != null : "fx:id=\"lbl_status\" was not injected: check your FXML file 'CheckoutBook.fxml'.";
		assert txt_bookIsbn != null : "fx:id=\"txt_bookIsbn\" was not injected: check your FXML file 'CheckoutBook.fxml'.";
		assert txt_memberId != null : "fx:id=\"txt_memberId\" was not injected: check your FXML file 'CheckoutBook.fxml'.";

	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void start() {
		Stage primaryStage = new Stage();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/ui/CheckoutBook.fxml"));
			Scene scene = new Scene(root, 550, 350);
			scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
