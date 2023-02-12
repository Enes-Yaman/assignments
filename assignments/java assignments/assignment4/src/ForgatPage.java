import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class ForgatPage {

    public static Group getGroup() {
        final User[] user = new User[1];
        Label Usernametext = new Label("Username: ");
        TextField Username = new TextField();
        Button seequestion = new Button("See Question");
        HBox hBox = new HBox(Usernametext,Username);
        hBox.setAlignment(Pos.CENTER);
        VBox vBox1 = new VBox(hBox,seequestion);

        Label Questiontext = new Label();
        TextField Answer = new TextField();
        Button Checkanswer = new Button("Check Answer");
        HBox hBox2 = new HBox(Questiontext,Answer);
        HBox.setMargin(Questiontext,new Insets(0,20,0,0));
        hBox2.setAlignment(Pos.CENTER);
        VBox vBox2 = new VBox(hBox2,Checkanswer);

        PasswordField passwordField = new PasswordField();
        PasswordField passwordField2 = new PasswordField();
        passwordField2.setPromptText("Password");
        passwordField.setPromptText("Password");
        Button submit = new Button("Change Password");
        VBox vBox3 = new VBox(passwordField,passwordField2,submit);



        Label Errormessage = new Label();
        Button back = new Button("Back");
        VBox vBox = new VBox(vBox1,Errormessage,back);
        VBox.setMargin(seequestion,new Insets(20));
        VBox.setMargin(back,new Insets(10,200,10,0));
        vBox.setAlignment(Pos.CENTER);
        vBox1.setAlignment(Pos.CENTER);
        vBox2.setAlignment(Pos.CENTER);
        vBox3.setAlignment(Pos.CENTER);
        vBox.setPrefHeight(230);
        vBox.setPrefWidth(300);
        VBox.setMargin(Checkanswer,new Insets(20));
        VBox.setMargin(passwordField2,new Insets(20));
        VBox.setMargin(passwordField,new Insets(20));
        Group group = new Group(vBox);
        seequestion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                if (Objects.equals(Username.getText(), Main.users.get(Username.getText()).getName()) && Main.users.get(Username.getText()).getQuestion() != null){
                    user[0] = Main.users.get(Username.getText());
                    Questiontext.setText(user[0].getQuestion());
                    vBox.getChildren().remove(0);
                    vBox.getChildren().add(0,vBox2);
                    Errormessage.setText("");
                }else {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);
                    Errormessage.setText("This user didn't save a password recovery");
                }}catch (NullPointerException e) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);
                    Errormessage.setText("There is no such a credential!");
                }
            }
        });
        Checkanswer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (Objects.equals(LoginPage.hashPassword(Answer.getText()), user[0].Answer)){
                    vBox.getChildren().remove(0);
                    vBox.getChildren().add(0,vBox3);
                    Errormessage.setText("");
                }else {
                    Errormessage.setText("Answer is not true!");
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);
                }
            }
        });
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String pass1 = passwordField.getText();
                String pass2 = passwordField2.getText();
                passwordField.setText("");
                passwordField2.setText("");
                Errormessage.setText("");
                if (pass1.isEmpty() || pass2.isEmpty()) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);
                    Errormessage.setText("ERROR: Password cannot be empty!");
                    Errormessage.setVisible(true);
                } else if (!pass1.equals(pass2)) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);
                    Errormessage.setText("ERROR: Passwords do not match!");
                    Errormessage.setVisible(true);
                } else {
                    String hashedpass = LoginPage.hashPassword(pass1);
                    user[0].setPassword(hashedpass);
                    Errormessage.setText("SUCCESS: You password has been updated!");
                    Errormessage.setVisible(true);
                }
            }
        });
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Group group = LoginPage.getGroup();
                Stage stage = (Stage) back.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        return group;
    }
}
