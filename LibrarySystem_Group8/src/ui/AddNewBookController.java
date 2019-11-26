package ui;

import java.net.URL;
import java.util.ResourceBundle;

import business.Author;
import business.Book;
import business.LibrarySystemException;
import business.SystemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddNewBookController extends Stage implements Initializable {

	public static final AddNewBookController INSTANCE = new AddNewBookController();

	private boolean isInitialized = false;

	private SystemController sc = new SystemController();

	@FXML
	Label pageTitle;

	@FXML
	Label bookTitle;

	@FXML
	TextField bookTitleTxt;

	@FXML
	Label maxCheckout;

	@FXML
	TextField maxCheckoutTxt;

	@FXML
	Label numberOfCopies;

	@FXML
	TextField numberOfCopiestxt;

	@FXML
	Label Author1;

	@FXML
	TextField Authortxt1;

//	@FXML
//	Label Author2;
//
//	@FXML
//	TextField Authortxt2;

	@FXML
	Button addbtn;

	@FXML
	Label result;

	@FXML
	public void addNewBook() {
		System.out.println("addNewBook");

		Author author = null;
		try {
			author = sc.getAuthorList(Authortxt1.getText());
		} catch (LibrarySystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Book book = new Book("5677", bookTitleTxt.getText(), Integer.parseInt(maxCheckoutTxt.getText()), author);
		try {
			sc.saveNewBook(book);
			result.setText("bookTitle: " + book.getTitle() + " added");
		} catch (LibrarySystemException ex) {
			result.setText(ex.getMessage());
		}
	
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void isInitialized(boolean val) {
		isInitialized = val;
	}

	public void start() {
		Stage primaryStage = new Stage();
		Parent root;

		try {
			root = FXMLLoader.load(getClass().getResource("/ui/AddNewBook.fxml"));
			Scene scene = new Scene(root, 550, 350);
			scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

//	private int getLastBookID() {
//		List<String> ids = sc.allBookIds();//
//		System.out.println("ids:  " + ids);
//
//		ids.get(ids.size() - 1).replace("-", "");
//		return Integer.parseInt(ids.get(ids.size() - 1));
//	}
}
