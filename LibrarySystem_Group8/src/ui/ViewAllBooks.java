package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import business.Book;
import business.SystemController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ViewAllBooks extends Stage implements Initializable {

	public static final ViewAllBooks INSTANCE = new ViewAllBooks();
	private TableView<Book> table;
	TableColumn<Book, String> titleCol;
	TableColumn<Book, String> numberOfCopiesCol;
	TableColumn<Book, String> numberOfAuthors;
	private ObservableList<Book> data;
	private Text actionStatus;
	private boolean isInitialized = false;
	Parent root;

	public boolean isInitialized() {
		return isInitialized;
	}

	public void start() {
		Stage primaryStage = new Stage();
		primaryStage.setTitle("View All Books");

		// Books label
		Label label = new Label("All Books");
		label.setTextFill(Color.DARKBLUE);
		label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
		HBox labelHb = new HBox();
		labelHb.setAlignment(Pos.CENTER);
		labelHb.getChildren().add(label);

		// Table view, data, columns and properties

		table = new TableView<>();
		data = getInitialTableData();
		table.setItems(data);

		populateTable();
		table.setPrefWidth(450);
		table.setPrefHeight(300);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		table.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());

		// Add and delete buttons
		Button addNewBookCopybtn = new Button("Add New Copy");
		addNewBookCopybtn.setOnAction(new AddNewBookCopyButtonListener());
		Button addBook = new Button("Add New Book");
		addBook.setOnAction(new AddNewBookButtonListener());

		Button addAuthor = new Button("Add Other Author");
		addAuthor.setOnAction(new AddOtherAuthorButtonListener());

		HBox buttonHb = new HBox(10);
		buttonHb.setAlignment(Pos.CENTER);
		buttonHb.getChildren().addAll(addNewBookCopybtn, addBook, addAuthor);

		// Status message text
		actionStatus = new Text();
		actionStatus.setFill(Color.FIREBRICK);

		// Vbox
		VBox vbox = new VBox(20);
		vbox.setPadding(new Insets(25, 25, 25, 25));

		vbox.getChildren().addAll(labelHb, table, buttonHb, actionStatus);

		// Scene
		Scene scene = new Scene(vbox, 500, 550); // w x h
		primaryStage.setScene(scene);
		primaryStage.show();

		// Select the first row
		table.getSelectionModel().select(0);
		Book book = table.getSelectionModel().getSelectedItem();
		actionStatus.setText(book.toString());

	} // start()

	// to select certain book
	Book book;

	private class RowSelectChangeListener implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {

			int ix = newVal.intValue();

			if ((ix < 0) || (ix >= data.size())) {

				return; // invalid data
			}

			book = data.get(ix);

			actionStatus.setText(book.toString());
		}
	}

	private ObservableList<Book> getInitialTableData() {

		List<Book> list = new ArrayList<>(viewAllBooks());

		ObservableList<Book> data = FXCollections.observableList(list);

		return data;
	}

	private class AddNewBookCopyButtonListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {

			book.addCopy();

//			DataAccessFacade dc = new DataAccessFacade();
			SystemController sc = new SystemController();

			sc.updateBook(book); // to save the added copy to thier book

			populateTable();

			// Select the new row
			table.requestFocus();

			actionStatus.setText("you added new copy from: " + book.getTitle() + " length now becomes: "
					+ String.valueOf(book.getCopies().length));
		}
	}

	private class AddNewBookButtonListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			System.out.println("books clicked");
			if (!AddNewBookController.INSTANCE.isInitialized()) {

				AddNewBookController.INSTANCE.start();
			}

		}
	}

	private class AddOtherAuthorButtonListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			System.out.println("AddNewAuthorController clicked");
			if (!AddNewAuthorController.INSTANCE.isInitialized()) {
				AddNewAuthorController.INSTANCE.setBookID(book.getIsbn());
				AddNewAuthorController.INSTANCE.setBookView(INSTANCE);
				AddNewAuthorController.INSTANCE.start();
			}

		}
	}

	@SuppressWarnings("unchecked")
	public void populateTable() {

		titleCol = new TableColumn<Book, String>("All Book Titles");
		titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		titleCol.setCellFactory(TextFieldTableCell.forTableColumn());

		numberOfCopiesCol = new TableColumn<Book, String>("Number Of Copies");
		numberOfCopiesCol.setCellValueFactory(new PropertyValueFactory<Book, String>("count"));
		numberOfCopiesCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		numberOfAuthors= new TableColumn<Book, String>("Number Of Authors");
		numberOfAuthors.setCellValueFactory(new PropertyValueFactory<Book, String>("numberOfAuthors"));
		numberOfAuthors.setCellFactory(TextFieldTableCell.forTableColumn());

		table.getColumns().setAll(titleCol, numberOfCopiesCol,numberOfAuthors);
	}

	public List<Book> viewAllBooks() {
		StringBuilder sb = new StringBuilder();
		SystemController sc = new SystemController();
		List<Book> books = sc.getAllBooks();

		String numberOfCopies = null;
		for (Book book : books) {
			numberOfCopies = sc.getBookCopies(book);
			sb.append(book.getTitle() + numberOfCopies + "\n");
		}
		System.out.println(" viewAllBooks : " + sb.toString());
		return books;
	}

	public void init() {
		try {
			root = FXMLLoader.load(getClass().getResource("/ui/AddNewBook.fxml"));
			Scene scene = new Scene(root, 600, 600);
			scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
			setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
