import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Paths;

public class AddFilmPage {
    public static Group getGroup() {
        Label Addfilmtext = new Label("Please give name, relative path of the trailer and duration of the film.");
        Label Nametext = new Label("Name:");
        TextField name = new TextField();
        TextField path = new TextField();
        TextField duration = new TextField();
        Label Pathtext = new Label("Trailer (Path):");
        Label Durationtext = new Label("Duration (m):");
        Button backbutton = new Button("◀ BACK");
        Button okbutton = new Button("OK");
        Label error = new Label();
        error.setVisible(false);
        HBox hBox1 = new HBox(Nametext, name);
        HBox hBox2 = new HBox(Pathtext, path);
        HBox hBox3 = new HBox(Durationtext, duration);
        HBox hBox4 = new HBox(backbutton, okbutton);
        VBox vBox = new VBox(Addfilmtext, hBox1, hBox2, hBox3, hBox4, error);
        vBox.setPrefHeight(200.0);
        vBox.setPrefWidth(500.0);
        vBox.setAlignment(Pos.CENTER);
        hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.CENTER);
        hBox3.setAlignment(Pos.CENTER);
        hBox4.setAlignment(Pos.CENTER);
        Addfilmtext.setAlignment(Pos.CENTER);
        Addfilmtext.setTextAlignment(TextAlignment.CENTER);
        error.setTextAlignment(TextAlignment.CENTER);
        error.setAlignment(Pos.CENTER);
        VBox.setMargin(Addfilmtext, new Insets(20));
        HBox.setMargin(Nametext, new Insets(0, 70, 0, 0));
        HBox.setMargin(Durationtext, new Insets(0, 20, 0, 0));
        HBox.setMargin(Pathtext, new Insets(0, 20, 0, 0));
        HBox.setMargin(backbutton, new Insets(0, 175, 0, 0));
        hBox1.setPadding(new Insets(20, 20, 20, 20));
        hBox2.setPadding(new Insets(20, 20, 20, 20));
        hBox3.setPadding(new Insets(20, 20, 20, 20));
        hBox4.setPadding(new Insets(20, 20, 20, 20));
        Group group = new Group(vBox);
        backbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Group group = SelectFilmPage.getGroup();
                Stage stage = (Stage) backbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        okbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (name.getText().isEmpty()) {
                        Main.errorsound.play();
                        Main.errorsound.seek(Duration.ZERO);
                        error.setText("ERROR: Film name could not empty!");
                        error.setVisible(true);

                    } else if (path.getText().isEmpty()) {
                        Main.errorsound.play();
                        Main.errorsound.seek(Duration.ZERO);

                        error.setText("ERROR: Trailer path could not be empty!");
                        error.setVisible(true);

                    } else if (Integer.parseInt(duration.getText()) <= 0) {
                        Main.errorsound.play();
                        Main.errorsound.seek(Duration.ZERO);

                        error.setText("Duration has to be a positive integer!");
                        error.setVisible(true);

                    }
                    File file = new File(Main.directorypath + "/assets/trailers/" + path.getText());
                    File filee = new File(path.getText());
                    String fileurl = file.toURI().toString();
                    Media media = new Media(fileurl);
                    Main.films.put(name.getText(), new Film(name.getText(), Paths.get(filee.toString()), Integer.parseInt(duration.getText())));
                    error.setText("SUCCESS: Film added successfully!");
                    error.setVisible(true);
                } catch (NumberFormatException numberFormatException) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);

                    error.setText("Duration has to be a positive integer!");
                    error.setVisible(true);
                } catch (IllegalArgumentException illegalArgumentException) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);

                    error.setText("Invalid path");
                    error.setVisible(true);
                } catch (MediaException mediaException) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);

                    error.setText("Invaid path");
                    error.setVisible(true);
                }
            }
        });

        return group;
    }
}
