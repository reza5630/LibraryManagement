package ui;

import business.CheckoutRecordEntry;
import business.LibrarySystemException;
import business.SystemController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

public class CheckoutHistory extends Stage {
	public static final CheckoutHistory INSTANCE = new CheckoutHistory();

	private TableView<CheckoutRecordEntry> tableView;
	private TableColumn<CheckoutRecordEntry, String> bookName;
	private TableColumn<CheckoutRecordEntry, String> checkoutDate;
	private TableColumn<CheckoutRecordEntry, String> dueDate;
	private ObservableList<CheckoutRecordEntry> data;
	private String memberId = "";
	private boolean isInitialized = false;
	
	public boolean isInitialized() {
		return isInitialized;
	}
	void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void start() {
		Stage primaryStage = new Stage();
		primaryStage.setTitle("View Checkout History");

		// Label
		Label label = new Label("Checkout History");
		label.setTextFill(Color.DARKBLUE);
		label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
		HBox labelHb = new HBox();
		labelHb.setAlignment(Pos.CENTER);
		labelHb.getChildren().add(label);
		
		tableView = new TableView<>();
		data = getInitialTableData();
		tableView.setItems(data);
		
		populateTable();
		tableView.setPrefWidth(450);
		tableView.setPrefHeight(300);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		// Vbox
		VBox vbox = new VBox(20);
		vbox.setPadding(new Insets(25, 25, 25, 25));
		vbox.getChildren().addAll(tableView, label);

		// Scene
		Scene scene = new Scene(vbox, 500, 550); // w x h
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	private ObservableList<CheckoutRecordEntry> getInitialTableData() {
		SystemController sc = new SystemController();
		ObservableList<CheckoutRecordEntry> data = null;
		try {
			data = FXCollections.observableArrayList(sc.viewCheckoutHistory(memberId));
		} catch (LibrarySystemException e) {
			System.err.println(e.getMessage());
		}
		return data;
	}
	@SuppressWarnings("unchecked")
	private void populateTable() {
		bookName = new TableColumn<CheckoutRecordEntry, String>("Book Name");
		bookName.setCellValueFactory(new PropertyValueFactory<CheckoutRecordEntry, String>("bookTitle"));
		bookName.setCellFactory(TextFieldTableCell.forTableColumn());

		checkoutDate = new TableColumn<CheckoutRecordEntry, String>("Checkout Date");
		checkoutDate.setCellValueFactory(new PropertyValueFactory<CheckoutRecordEntry, String>("checkoutDateProp"));
		checkoutDate.setCellFactory(TextFieldTableCell.forTableColumn());

		dueDate = new TableColumn<CheckoutRecordEntry, String>("Due Date");
		dueDate.setCellValueFactory(new PropertyValueFactory<CheckoutRecordEntry, String>("dueDateProp"));
		dueDate.setCellFactory(TextFieldTableCell.forTableColumn());
		
		tableView.getColumns().setAll(bookName, checkoutDate, dueDate);
	}
}
