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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class RemoveHallPage {
    public static Group getGroup(String movieinfo) {
        Label label = new Label("Select the hall that you desire to remove from " + SelectFilmPage.selectedfilm.Name + " and then click OK.");
        ComboBox<String> comboBox = new ComboBox<>();
        for (Hall h : Main.halls.values()) {
            if (h.filmname.equals(SelectFilmPage.selectedfilm.Name)) {
                comboBox.getItems().add(h.hallname);
            }
        }
        comboBox.setValue(comboBox.getItems().get(0));
        Button backbutton = new Button("◀ BACK");
        Button okbutton = new Button("OK");
        HBox hBox = new HBox(backbutton, okbutton);
        VBox vBox = new VBox(label, comboBox, hBox);
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        vBox.setPrefHeight(300);
        vBox.setPrefWidth(700);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        VBox.setMargin(comboBox, new Insets(40, 0, 40, 0));
        HBox.setMargin(backbutton, new Insets(0, 40, 0, 0));

        Group group = new Group(vBox);
        backbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Group group = WatchFilmPage.getGroup();
                Stage stage = (Stage) backbutton.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        okbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String deletehall = comboBox.getValue();
                ObservableList<String> halls = comboBox.getItems();
                halls.remove(comboBox.getValue());
                comboBox.setItems(halls);
                if (comboBox.getItems().size() != 0) {
                    comboBox.setValue(comboBox.getItems().get(0));
                } else {
                    comboBox.setValue("");
                }
                Hall.seatdeleter(Main.halls.get(deletehall));
                Main.halls.remove(deletehall);
            }
        });
        return group;
    }
}
