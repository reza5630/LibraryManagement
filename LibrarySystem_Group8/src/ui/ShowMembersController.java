package ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import business.LibraryMember;
import business.LibrarySystemException;
import business.SystemController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ShowMembersController extends Stage implements Initializable {
	public static final ShowMembersController INSTANCE = new ShowMembersController();

	private SystemController sc = new SystemController();
	private boolean isInitialized = false;
	private String currentMember = "";
	private static String currentUserLevel = "";
	Parent root;

	public boolean isInitialized() {
		return isInitialized;
	}

	public void isInitialized(boolean val) {
		isInitialized = val;
	}

	public void setUserLevel(String userLevel) {
		currentUserLevel = userLevel;
		//System.out.println(currentUserLevel);
	}

	public void init() {

		try {
			root = FXMLLoader.load(getClass().getResource("/ui/ShowMembers.fxml"));
			Scene scene = new Scene(root, 600, 600);
			scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
			setScene(scene);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Label labelUserID = new Label();
	@FXML
	private Label userID = new Label();
	@FXML
	private Label labelAccess = new Label();
	@FXML
	private Text messageBar = new Text();

	@FXML
	private Button btnAdd = new Button("Add Member");

	@FXML
	private Button btnUpdate = new Button("Update Member");

	@FXML
	private Button btnCheckout = new Button("CheckoutList");

	@FXML
	private Button btnDelete = new Button("Delete Member");

	@FXML
	private MenuButton menuButton = new MenuButton("View MemberList");

	@FXML
	EventHandler<ActionEvent> action = changeTabPlacement();

	@FXML
	AnchorPane userInfo = new AnchorPane();
	@FXML
	private TextField fname = new TextField();
	@FXML
	private TextField lname = new TextField();
	@FXML
	private TextField tel = new TextField();
	@FXML
	private TextField city = new TextField();
	@FXML
	private TextField state = new TextField();
	@FXML
	private TextField street = new TextField();
	@FXML
	private TextField zip = new TextField();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//System.out.println("loading member View");
		assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert btnCheckout != null : "fx:id=\"btnCheckout\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert btnUpdate != null : "fx:id=\"btnUpdate\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert city != null : "fx:id=\"city\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert fname != null : "fx:id=\"fname\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert labelAccess != null : "fx:id=\"labelAccess\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert labelUserID != null : "fx:id=\"labelUserID\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert lname != null : "fx:id=\"lname\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert menuButton != null : "fx:id=\"menuButton\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert messageBar != null : "fx:id=\"messageBar\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert state != null : "fx:id=\"state\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert street != null : "fx:id=\"street\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert tel != null : "fx:id=\"tel\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert userID != null : "fx:id=\"userID\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert userInfo != null : "fx:id=\"userInfo\" was not injected: check your FXML file 'ShowMembers.fxml'.";
		assert zip != null : "fx:id=\"zip\" was not injected: check your FXML file 'ShowMembers.fxml'.";

		//System.out.println("Access Level : " + currentUserLevel);
		labelAccess.setText(currentUserLevel);

		createMenuItems();

		btnAdd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				//System.out.println("add clicked");
				setAddMemberScreen();
			}
		});

		btnCheckout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				//System.out.println("Checkout list");
				if (!CheckoutHistory.INSTANCE.isInitialized()) {
					CheckoutHistory.INSTANCE.setMemberId(currentMember);
					CheckoutHistory.INSTANCE.start();
					try {
						System.out.println("\n\n---Checkout History of " + sc.getMember(currentMember).getFullName() + ": ---\n"
								+ sc.viewCheckoutHistory(currentMember).toString());
					} catch (LibrarySystemException e1) {
						// TODO Auto-generated catch block
						//System.out.println(e1.getMessage());
					}
				}
			}
		});

		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					sc.deleteMember(currentMember);
					//System.out.println("delete clicked");
					createMenuItems();
					messageBar.setFill(Start.Colors.green);
					messageBar.setVisible(true);
					messageBar.setText("Member Deleted");
				} catch (LibrarySystemException e1) {
					// TODO Auto-generated catch block
					messageBar.setVisible(true);
					messageBar.setFill(Start.Colors.red);
					messageBar.setText("Error! " + e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (fname.getText().equals("") || lname.getText().equals("") || tel.getText().equals("")
						|| street.getText().equals("") || city.getText().equals("") || state.getText().equals("")
						|| zip.getText().equals("")) {
					messageBar.setFill(Start.Colors.red);
					messageBar.setText("Member Fields Empty");
					messageBar.setVisible(true);
				} else {
					sc.addMember(currentMember, fname.getText(), lname.getText(), tel.getText(), street.getText(),
							city.getText(), state.getText(), zip.getText());
					createMenuItems();
					//System.out.println("update clicked");
					messageBar.setFill(Start.Colors.green);
					messageBar.setText("Member Info Added");
					messageBar.setVisible(true);
				}
			}
		});

	}

	protected void setAddMemberScreen() {
		// TODO Auto-generated method stub
		labelUserID.setVisible(true);
		userID.setVisible(true);
		messageBar.setVisible(false);
		btnDelete.setVisible(false);
		btnCheckout.setVisible(false);
		btnUpdate.setText("Add");
		currentMember = Integer.toString(getLastMemberID() + 1);
		userID.setText("" + currentMember);
		fname.setText("");
		lname.setText("");
		tel.setText("");
		street.setText("");
		city.setText("");
		state.setText("");
		zip.setText("");
		userInfo.setVisible(true);
	}

	private int getLastMemberID() {
		List<String> ids = sc.allMemberIds();
		return Integer.parseInt(ids.get(ids.size() - 1));
	}

	private void createMenuItems() {
		// TODO Auto-generated method stub
		if (currentUserLevel.equals("LIBRARIAN")) {
			btnAdd.setVisible(false);
		}
		userInfo.setVisible(false);
		messageBar.setVisible(false);
		menuButton.getItems().clear();
		List<String> ids = sc.allMemberIds();
		for (String s : ids) {
			MenuItem temp = new MenuItem(s);
			temp.setOnAction(action);
			menuButton.getItems().add(temp);
		}
	}

	private void setMenuForMemberInfo() {
		if (currentUserLevel.equals("LIBRARIAN")) {
			btnDelete.setVisible(false);
			btnUpdate.setVisible(false);
			btnCheckout.setVisible(true);
		} else if (currentUserLevel.equals("ADMIN")) {
			btnDelete.setVisible(true);
			btnUpdate.setVisible(true);
			btnCheckout.setVisible(false);
		} else {
			btnDelete.setVisible(true);
			btnUpdate.setVisible(true);
			btnCheckout.setVisible(true);
		}
		btnUpdate.setText("Update");
		messageBar.setVisible(false);
		labelUserID.setVisible(false);
		userID.setVisible(false);
	}

	private EventHandler<ActionEvent> changeTabPlacement() {
		// TODO Auto-generated method stub
		return new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				////System.out.println(userInfo.isVisible());
				MenuItem mItem = (MenuItem) event.getSource();
				currentMember = mItem.getText();
				//System.out.println(currentMember);
				userInfo.setVisible(true);
				try {
					LibraryMember member = sc.getMember(currentMember);
					fname.setText(member.getFirstName());
					lname.setText(member.getLastName());
					tel.setText(member.getTelephone());
					street.setText(member.getAddress().getStreet());
					city.setText(member.getAddress().getCity());
					state.setText(member.getAddress().getState());
					zip.setText(member.getAddress().getZip());
					setMenuForMemberInfo();
				} catch (LibrarySystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

}
