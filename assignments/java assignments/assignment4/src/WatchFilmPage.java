import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Objects;

public class WatchFilmPage {
    public static Group getGroup() {
        File file = new File(Main.directorypath + "/assets/trailers/" + SelectFilmPage.selectedfilm.TrailerPath);
        String fileurl = file.toURI().toString();
        Media media = new Media(fileurl);
        String movieinfo = SelectFilmPage.selectedfilm.Name + " (" + SelectFilmPage.selectedfilm.Duration + " minutes)";
        Label info = new Label(movieinfo);
        info.setFont(Font.font(20));
        MediaPlayer player = new MediaPlayer(media);
        MediaView mediaView = new MediaView(player);
        Button button = new Button("   ▶  ");
        Button videoback = new Button("  << ");
        Button videoforward = new Button("  >> ");
        Button videoreplay = new Button(" |<< ");

        Slider slider = new Slider(0, 1, 1);
        slider.orientationProperty().set(Orientation.VERTICAL);
        VBox vBox1 = new VBox(button, videoback, videoforward, videoreplay, slider);
        VBox.setMargin(button, new Insets(20));
        VBox.setMargin(videoback, new Insets(20));
        VBox.setMargin(videoforward, new Insets(20));
        VBox.setMargin(videoreplay, new Insets(20));
        VBox.setMargin(slider, new Insets(20));
        slider.setValue(0.5);
        player.setVolume(0.5);
        HBox hBox1 = new HBox(mediaView, vBox1);
        HBox.setMargin(mediaView, new Insets(0, 0, 0, 10));
        Button backbutton = new Button("◀ BACK");
        Button add_hallbutton = new Button("Add Hall");
        Button removehallbutton = new Button("Remove Hall");
        ComboBox<String> comboBox = new ComboBox<>();
        for (Hall h : Main.halls.values()) {
            if (h.filmname.equals(SelectFilmPage.selectedfilm.Name)) {
                comboBox.getItems().add(h.hallname);
            }
        }
        if (comboBox.getItems().size() != 0) {
            comboBox.setValue(comboBox.getItems().get(0));
        } else {
            comboBox.setValue("");
        }
        HBox hBox2;
        Button okbutton = new Button("OK");
        if (!LoginPage.currentuser.getAdmin()) {
            hBox2 = new HBox(backbutton, comboBox, okbutton);
            HBox.setMargin(comboBox, new Insets(0, 20, 0, 20));

        } else {
            hBox2 = new HBox(backbutton, add_hallbutton, removehallbutton, comboBox, okbutton);
            HBox.setMargin(comboBox, new Insets(0, 20, 0, 20));
            HBox.setMargin(add_hallbutton, new Insets(0, 20, 0, 20));
        }
        VBox vBox = new VBox(info, hBox1, hBox2);
        vBox.setPrefWidth(1450);
        vBox.setPrefHeight(900);
        vBox.setAlignment(Pos.CENTER);
        hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.CENTER);
        vBox1.setAlignment(Pos.CENTER);
        VBox.setMargin(hBox1, new Insets(20, 0, 20, 0));
        HBox.setMargin(vBox1, new Insets(0, 0, 0, 20));
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                player.stop();
                button.setText("   ▶  ");
            }
        });
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                player.setVolume((Double) newValue);
            }
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (Objects.equals(button.getText(), "   ▶  ")) {
                    player.play();
                    button.setText("   ||  ");
                } else {
                    player.pause();
                    button.setText("   ▶  ");
                }
            }
        });
        videoback.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player.seek(player.getCurrentTime().add(new Duration(-5000)));
            }
        });
        videoforward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player.seek(player.getCurrentTime().add(new Duration(5000)));
            }
        });
        videoreplay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (Objects.equals(button.getText(), "▶")) {
                    player.stop();
                } else {
                    player.stop();
                    player.play();
                }

            }
        });
        backbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player.seek(Duration.ZERO);
                player.stop();
                Group group = SelectFilmPage.getGroup();
                Stage stage = (Stage) backbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        add_hallbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player.seek(Duration.ZERO);
                player.stop();
                Group group = AddHallPage.getGroup(movieinfo);
                Stage stage = (Stage) add_hallbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        removehallbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player.seek(Duration.ZERO);
                player.stop();
                Group group = RemoveHallPage.getGroup(movieinfo);
                Stage stage = (Stage) removehallbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        okbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player.seek(Duration.ZERO);
                player.stop();
                Group group = SeatPage.getGroup(comboBox.getValue());
                Stage stage = (Stage) okbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        return new Group(vBox);
    }
}
