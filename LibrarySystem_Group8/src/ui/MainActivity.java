package ui;

import java.util.ArrayList;
import java.util.List;

import dataaccess.Auth;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainActivity extends Stage {
	public static final MainActivity INSTANCE = new MainActivity();
	private boolean isInitialized = false;

	public boolean isInitialized() {
		return isInitialized;
	}

	public void isInitialized(boolean val) {
		isInitialized = val;
	}

	private MainActivity() {
	}

	public void init(Auth userLevel) {
		GridPane grid = new GridPane();
		grid.setId("top-container");
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setMinSize(400, 400);
		grid.setVgap(40);
		MenuBar mainMenu = new MenuBar();

		grid.add(mainMenu, 0, 0);

		HBox splashBox = new HBox();
		String accessLevel = "" + (userLevel.name().equals("BOTH") ? "SUPERUSER" : userLevel);
		Label splashLabel = new Label("Access Level: " + accessLevel);
		splashLabel.setFont(Font.font("System Regular", FontWeight.BOLD, 18));
		splashBox.setAlignment(Pos.CENTER);
		splashBox.getChildren().add(splashLabel);
		grid.add(splashBox, 1, 1);

		Menu optionsMenu = new Menu("Options");
		List<MenuItem> items = new ArrayList<MenuItem>();

		MenuItem members = new MenuItem("Members");
		items.add(members);
		members.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!ShowMembersController.INSTANCE.isInitialized()) {
					ShowMembersController.INSTANCE.setUserLevel(accessLevel);
					ShowMembersController.INSTANCE.init();
				}
				ShowMembersController.INSTANCE.show();
			}
		});

		if(userLevel!=Auth.LIBRARIAN) {
			
			MenuItem books = new MenuItem("Books");
			items.add(books);
			books.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					//System.out.println("books clicked");
					if (!ViewAllBooks.INSTANCE.isInitialized()) {
						ViewAllBooks.INSTANCE.start();
					}
				}
			});
		}
		if (userLevel != Auth.ADMIN) {

			MenuItem checkout = new MenuItem("Checkout");
			items.add(checkout);
			checkout.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					//System.out.println("books clicked");
					if (!CheckoutBookController.INSTANCE.isInitialized()) {
						CheckoutBookController.INSTANCE.start();
					}
				}
			});
		}

		MenuItem logout = new MenuItem("Logout");
		items.add(logout);
		logout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.hideAllWindows();
				Start.primStage().show();
				close();
			}
		});

		optionsMenu.getItems().addAll(items);
		mainMenu.getMenus().addAll(optionsMenu);

		Scene scene = new Scene(grid);
		scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
		setScene(scene);

	}

}
