package ui;

import java.net.URL;
import java.util.ResourceBundle;

import business.Author;
import business.LibrarySystemException;
import business.SystemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddNewAuthorController extends Stage implements Initializable {
	public static final AddNewAuthorController INSTANCE = new AddNewAuthorController();
	public static ViewAllBooks viewAllBooksInstance = new ViewAllBooks();

	private boolean isInitialized = false;
	private SystemController sc = new SystemController();
	private Stage primaryStage = new Stage();
	@FXML
	private Label fNameLabel;
	@FXML
	private Label lNameLabel;
	@FXML
	private Label mobileLabel;
	@FXML
	private Label streetLabel;

	@FXML
	private Label bioLabel;

	@FXML
	private TextArea tx;

	@FXML
	private Label cityLabel;
	@FXML
	private Label zipLabel;
	@FXML
	private Label stateLabel;

	@FXML
	private TextField fNameTxt;

	@FXML
	private TextField lNameTxt;
	@FXML
	private TextField mobileTxt;
	@FXML
	private TextField streetTxt;
	@FXML
	private TextField cityTxt;

	@FXML
	private TextField zipTxt;
	@FXML
	private TextField stateTxt;

	@FXML
	private Button addAuthorBtn;

	@FXML
	private Label result;

	private static String bookIsbn;

	public void setBookID(String bookID) {
		bookIsbn = bookID;
	}

	public void setBookView(ViewAllBooks vb) {
		viewAllBooksInstance = vb;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void isInitialized(boolean val) {
		isInitialized = val;
	}

	public void start() {

		System.out.println("AddNewAuthor class");

		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/ui/AddNewAuthor.fxml"));
			Scene scene = new Scene(root, 600, 600);
			scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@FXML
	private void AddNewAuthorButtonListener() throws LibrarySystemException {
		System.out.println("addAuthorBtn clicked");

		try {
			Author author = sc.addNewAuthor(fNameTxt.getText(), lNameTxt.getText(), mobileTxt.getText(), tx.getText(),
					streetTxt.getText(), cityTxt.getText(), stateTxt.getText(), zipTxt.getText(), bookIsbn);

			result.setText("author: " + author.getFirstName() + " " + author.getLastName() + " added");

			primaryStage.hide();
			
			viewAllBooksInstance.populateTable();

		} catch (LibrarySystemException e) {
			result.setText(e.getMessage());
		}

	}
}
