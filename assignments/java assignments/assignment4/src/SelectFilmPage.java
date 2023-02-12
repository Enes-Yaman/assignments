import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SelectFilmPage {
    public static Film selectedfilm;

    public static Group getGroup() {
        Label welcome = new Label("");
        ComboBox<String> comboBox = new ComboBox<>();
        for (Film f : Main.films.values()) {
            comboBox.getItems().add(f.Name);
        }
        if (comboBox.getItems().size() != 0) {
            comboBox.setValue(comboBox.getItems().get(0));
        } else {
            comboBox.setValue("");
        }
        Button okbutton = new Button("OK");
        HBox hBox1 = new HBox(comboBox, okbutton);
        Button addbutton = new Button("Add Film");
        Button removebutton = new Button("Remove Film");
        Button usersbutton = new Button("Edit Users");
        Button logoutbutton = new Button("LOG OUT");
        HBox hBox2 = new HBox(addbutton, removebutton, usersbutton);
        VBox vBox;
        Label errorlabel = new Label("ERROR: There is no selectable movie! Please add new one!");
        welcome.setAlignment(Pos.CENTER);
        welcome.setTextAlignment(TextAlignment.CENTER);
        if (LoginPage.currentuser.getAdmin() && LoginPage.currentuser.getClup()) {
            welcome.setText("Welcome " + LoginPage.currentuser.getName() + "(Admin - Club Member)!\nYou can either select film below or do edits");
            vBox = new VBox(welcome, hBox1, hBox2, logoutbutton);
        } else if (LoginPage.currentuser.getAdmin()) {
            welcome.setText("Welcome " + LoginPage.currentuser.getName() + "(Admin)!\nYou can either select film below or do edits");
            vBox = new VBox(welcome, hBox1, hBox2, logoutbutton);

        } else if (LoginPage.currentuser.getClup()) {
            welcome.setText("Welcome " + LoginPage.currentuser.getName() + "(Club Member)!\nSelect a film and then click OK to continue");
            vBox = new VBox(welcome, hBox1, logoutbutton);
        } else {
            welcome.setText("Welcome " + LoginPage.currentuser.getName() + "!\nSelect a film and then click OK to continue");
            vBox = new VBox(welcome, hBox1, logoutbutton);

        }
        vBox.setPrefHeight(200.0);
        vBox.setPrefWidth(500.0);
        vBox.setAlignment(Pos.CENTER);
        hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.CENTER);
        hBox1.setPadding(new Insets(20, 20, 30, 20));
        HBox.setMargin(removebutton, new Insets(0, 20, 0, 20));
        HBox.setMargin(okbutton, new Insets(0, 0, 0, 30));
        VBox.setMargin(logoutbutton, new Insets(20, 0, 0, 280));
        okbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectedfilm = Main.films.get(comboBox.getValue());
                if (selectedfilm != null) {
                    Group group = WatchFilmPage.getGroup();
                    Stage stage = (Stage) okbutton.getScene().getWindow();
                    Scene scene = new Scene(group);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    if (!vBox.getChildren().contains(errorlabel)) {
                        vBox.getChildren().add(errorlabel);
                    }
                }
            }
        });
        addbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Group group = AddFilmPage.getGroup();
                Stage stage = (Stage) okbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        removebutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Group group = RemoveFilmPage.getGroup();
                Stage stage = (Stage) okbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        logoutbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LoginPage.currentuser = null;
                Group group = LoginPage.getGroup();
                Stage stage = (Stage) okbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        usersbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Group group = EditUsersPage.getGroup();
                Stage stage = (Stage) okbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });

        Group group = new Group(vBox);
        return group;
    }

}