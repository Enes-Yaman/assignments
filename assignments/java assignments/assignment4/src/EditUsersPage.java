import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditUsersPage {
    public static Group getGroup() {
        TableColumn<User, String> usernamecol = new TableColumn<>("Username");
        TableColumn<User, Boolean> clupMembercol = new TableColumn<>("Clup Member");
        TableColumn<User, Boolean> admincol = new TableColumn<>("Admin");
        TableView<User> tableView = new TableView<>();

        usernamecol.setCellValueFactory(new PropertyValueFactory<User, String>("Name"));
        clupMembercol.setCellValueFactory(new PropertyValueFactory<User, Boolean>("Clup"));
        admincol.setCellValueFactory(new PropertyValueFactory<User, Boolean>("Admin"));

        tableView.getColumns().add(usernamecol);
        tableView.getColumns().add(clupMembercol);
        tableView.getColumns().add(admincol);
        for (User user : Main.users.values()) {
            if (user != LoginPage.currentuser) {
                tableView.getItems().add(user);
            }
        }

        tableView.getSelectionModel().selectFirst();
        Button back = new Button("◀ BACK");
        Button clupmem = new Button("Promote/Demote Clup Member");
        Button admin = new Button("Promote/Demote Admin");
        admin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User user = tableView.getSelectionModel().getSelectedItem();
                user.setAdmin(!user.getAdmin());
                tableView.refresh();
            }
        });
        clupmem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User user = tableView.getSelectionModel().getSelectedItem();
                tableView.getSelectionModel().getSelectedCells();
                user.setClup(!user.getClup());
                tableView.refresh();
            }
        });
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Group group = SelectFilmPage.getGroup();
                Stage stage = (Stage) back.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        HBox hBox = new HBox(back, clupmem, admin);
        VBox vBox = new VBox(tableView, hBox);
        vBox.setPrefHeight(500);
        vBox.setPrefWidth(600);
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        VBox.setMargin(tableView, new Insets(0, 20, 30, 20));
        HBox.setMargin(clupmem, new Insets(0, 50, 0, 50));
        Group group = new Group(vBox);
        return group;
    }
}
