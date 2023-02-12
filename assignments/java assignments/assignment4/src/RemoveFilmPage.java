import javafx.collections.ObservableList;
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
import javafx.stage.Stage;

import java.util.Objects;

public class RemoveFilmPage {
    public static Group getGroup() {
        Label removepagetext = new Label("Select the film that you desire to remove and then click OK");
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
        Button backbutton = new Button("◀ BACK");
        HBox hBox = new HBox(backbutton, okbutton);
        VBox vBox = new VBox(removepagetext, comboBox, hBox);
        hBox.setAlignment(Pos.CENTER);
        removepagetext.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        VBox.setMargin(comboBox, new Insets(40, 40, 40, 40));
        HBox.setMargin(okbutton, new Insets(0, 0, 0, 50));
        vBox.setPrefHeight(200.0);
        vBox.setPrefWidth(500.0);
        okbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String deletefilm = comboBox.getValue();
                ObservableList<String> films = comboBox.getItems();
                films.remove(comboBox.getValue());
                comboBox.setItems(films);
                if (comboBox.getItems().size() != 0) {
                    comboBox.setValue(comboBox.getItems().get(0));
                } else {
                    comboBox.setValue("");
                }
                Main.films.remove(deletefilm);
                for (Hall hall: Main.halls.values()) {
                    if (Objects.equals(hall.filmname, deletefilm)){
                        Hall.seatdeleter(Main.halls.get(hall.hallname));
                    }
                }
                Main.halls.values().removeIf(i -> i.filmname.equals(deletefilm));

            }
        });
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
        return new Group(vBox);

    }
}
