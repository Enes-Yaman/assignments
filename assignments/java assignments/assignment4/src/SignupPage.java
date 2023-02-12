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
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SignupPage {
    public static Group getGroup() {
        Label Errormessage = new Label();
        Label WelcomeMessage = new Label();
        WelcomeMessage.setText("Wrlcome to the HUCS Cinema Reservation System!\nFill the form below to create a new account\nYou can go to Log IN page by clicking LOG IN Button");
        WelcomeMessage.setFont(Font.font(15));
        Label usernametext = new Label("Username:");
        TextField username = new TextField();

        Label passwordtext = new Label("Password:");
        PasswordField password = new PasswordField();
        Label passwordtext2 = new Label("Password:");
        PasswordField password2 = new PasswordField();

        Button Signupbutton = new Button();
        Signupbutton.setText("SIGN UP");
        Button LoginButton = new Button();
        LoginButton.setText("LOG IN");

        HBox hBox1 = new HBox(usernametext, username);
        HBox hBox2 = new HBox(passwordtext, password);
        HBox hBox3 = new HBox(passwordtext2, password2);
        HBox hBox5 = new HBox();
        TextField Answer = new TextField();
        TextField Question = new TextField();
        if (Main.extraenabled){
            Question.setPromptText("Question");
            Answer.setPromptText("Answer");
            hBox5.getChildren().add(Question);
            hBox5.getChildren().add(Answer);
            hBox5.setAlignment(Pos.CENTER);
            HBox.setMargin(Answer,new Insets(20));
        }
        HBox hBox4 = new HBox(LoginButton, Signupbutton);
        WelcomeMessage.setAlignment(Pos.CENTER);
        WelcomeMessage.setTextAlignment(TextAlignment.CENTER);
        hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.CENTER);
        hBox3.setAlignment(Pos.CENTER);
        hBox4.setAlignment(Pos.CENTER);
        hBox1.setPadding(new Insets(30, 20, 20, 20));
        hBox2.setPadding(new Insets(30, 20, 20, 20));
        hBox3.setPadding(new Insets(30, 20, 20, 20));
        hBox4.setPadding(new Insets(30, 20, 20, 20));

        HBox.setMargin(LoginButton, new Insets(0, 100, 0, 0));
        HBox.setMargin(usernametext, new Insets(0, 20, 0, 0));
        HBox.setMargin(passwordtext, new Insets(0, 20, 0, 0));
        HBox.setMargin(passwordtext2, new Insets(0, 20, 0, 0));
        VBox vBox = new VBox(WelcomeMessage, hBox1, hBox2, hBox3, hBox5, hBox4, Errormessage);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefHeight(400.0);
        vBox.setPrefWidth(600.0);
        Group group = new Group(vBox);
        LoginButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                Group group = LoginPage.getGroup();
                Stage stage = (Stage) Signupbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        Signupbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String usernamee = username.getText();
                String pass1 = password.getText();
                String pass2 = password2.getText();
                username.setText("");
                password.setText("");
                password2.setText("");
                if (Main.users.containsKey(usernamee)) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);

                    Errormessage.setText("ERROR: This username already exist!");
                    Errormessage.setVisible(true);
                } else if (usernamee.isEmpty()) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);

                    Errormessage.setText("ERROR: Username cannot be empty!");
                    Errormessage.setVisible(true);
                } else if (pass1.isEmpty() || pass2.isEmpty()) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);

                    Errormessage.setText("ERROR: Password cannot be empty!");
                    Errormessage.setVisible(true);
                } else if (!pass1.equals(pass2)) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);

                    Errormessage.setText("ERROR: Passwords do not match!");
                    Errormessage.setVisible(true);
                } else if (Main.extraenabled){
                    if (Question.getText().isEmpty() || Answer.getText().isEmpty()){
                        Main.errorsound.play();
                        Main.errorsound.seek(Duration.ZERO);
                        Errormessage.setText("ERROR: Answer or Question cannot be empty!");
                        Errormessage.setVisible(true);
                    }else{
                        String hashedpass = LoginPage.hashPassword(pass1);
                        Main.users.put(usernamee, new User(usernamee, hashedpass, false, false));
                        Main.users.get(usernamee).setQuestion(Question.getText());
                        Main.users.get(usernamee).setAnswer(LoginPage.hashPassword(Answer.getText()));
                        Errormessage.setText("SUCCESS: You have successfully registered with your new credentials");
                        Errormessage.setVisible(true);
                    }
                }
                else {
                    String hashedpass = LoginPage.hashPassword(pass1);
                    Main.users.put(usernamee, new User(usernamee, hashedpass, false, false));
                    Errormessage.setText("SUCCESS: You have successfully registered with your new credentials");
                    Errormessage.setVisible(true);
                }
            }
        });
        return group;
    }
}
