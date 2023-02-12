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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class LoginPage {
    public static User currentuser;

    public static String hashPassword(String password) {
        byte[] bytesOfPassword = password.getBytes(StandardCharsets.UTF_8);
        byte[] md5Digest = new byte[0];
        try {
            md5Digest = MessageDigest.getInstance("MD5").digest(bytesOfPassword);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return Base64.getEncoder().encodeToString(md5Digest);
    }

    public static Group getGroup() {
        Label Errormessage = new Label();
        Label WelcomeMessage = new Label();
        WelcomeMessage.setFont(Font.font(15));
        WelcomeMessage.setText("Welcome to the HUCS Cinema Reservation System!\nPlease enter your credentials below and click LOGIN.\nYou can create a new account by clicking SIGN UP button");
        Label usernametext = new Label();
        usernametext.setText("Username:");
        Label passwordtext = new Label();
        passwordtext.setText("Password:");
        PasswordField password = new PasswordField();
        TextField username = new TextField();
        Button Signupbutton = new Button();
        Signupbutton.setText("SIGN UP");
        Button LoginButton = new Button();
        LoginButton.setText("LOG IN");
        HBox hBox1 = new HBox(usernametext, username);
        HBox hBox2 = new HBox(passwordtext, password);
        HBox hBox3 = new HBox(Signupbutton, LoginButton);
        Button Forgotbutton = new Button("Forgot password?");
        if (Main.extraenabled){
            hBox3.getChildren().add(Forgotbutton);
            HBox.setMargin(Forgotbutton,new Insets(0,0,0,100));
        }
        WelcomeMessage.setAlignment(Pos.CENTER);
        WelcomeMessage.setTextAlignment(TextAlignment.CENTER);
        hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.CENTER);
        hBox3.setAlignment(Pos.CENTER);
        hBox1.setPadding(new Insets(60, 20, 50, 20));
        hBox3.setPadding(new Insets(60, 20, 20, 20));
        HBox.setMargin(Signupbutton, new Insets(0, 100, 0, 0));
        HBox.setMargin(usernametext, new Insets(0, 20, 0, 0));
        HBox.setMargin(passwordtext, new Insets(0, 20, 0, 0));
        VBox vBox = new VBox(WelcomeMessage, hBox1, hBox2, hBox3, Errormessage);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefHeight(400.0);
        vBox.setPrefWidth(600.0);
        Group root0 = new Group(vBox);
        final int[] counter = {1};
        Forgotbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Group group = ForgatPage.getGroup();
                Stage stage = (Stage) Forgotbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        Signupbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Group group = SignupPage.getGroup();
                Stage stage = (Stage) Signupbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        LoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (counter[0] < Main.maximum_error_without_getting_blocked) {
                    String usernamee = username.getText();
                    String passwordd = password.getText();
                    username.setText("");
                    password.setText("");
                    try {
                        if (Objects.equals(usernamee, Main.users.get(usernamee).getName()) && (Objects.equals(hashPassword(passwordd), Main.users.get(usernamee).getPassword()))) {
                            currentuser = Main.users.get(usernamee);
                            switchToScene1(LoginButton);
                        } else {
                            counter[0]++;
                            Main.errorsound.play();
                            Main.errorsound.seek(Duration.ZERO);
                            Errormessage.setText("There is no such a credential!");
                        }
                    } catch (NullPointerException e) {
                        counter[0]++;
                        Main.errorsound.play();
                        Main.errorsound.seek(Duration.ZERO);
                        Errormessage.setText("There is no such a credential!");
                    }
                } else {
                    Timer timer = new Timer();

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            counter[0] = 1;
                        }
                    }, Main.blocktime * 1000);
                    Errormessage.setText("ERROR: Please wait until end of the 5 seconds to make a new operation!");
                    username.setText("");
                    password.setText("");
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);
                }
            }

            private void switchToScene1(Button loginButton) {
                Group group = SelectFilmPage.getGroup();
                Stage stage = (Stage) LoginButton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }


        });

        return root0;
    }

}
