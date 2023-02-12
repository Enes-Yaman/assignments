import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AddHallPage {
    public static Group getGroup(String moviinfo) {
        Label label = new Label(moviinfo);
        Label label1 = new Label("Row:");
        Label label2 = new Label("Column:");
        ComboBox<Integer> row = new ComboBox<>();
        ComboBox<Integer> collumn = new ComboBox<>();
        for (int i = 3; i < 11; i++) {
            row.getItems().add(i);
            collumn.getItems().add(i);

        }
        row.setValue(row.getItems().get(0));
        collumn.setValue(collumn.getItems().get(0));
        Label label3 = new Label("Name:");
        Label label4 = new Label("Price:");
        TextField textField = new TextField();
        TextField textField1 = new TextField();
        Button backbutton = new Button("◀ BACK");
        Button okbutton = new Button("OK");
        Label errorlabel = new Label();
        HBox hBox = new HBox(label1, row);
        HBox hBox1 = new HBox(label2, collumn);
        HBox hBox2 = new HBox(label3, textField);
        HBox hBox3 = new HBox(label4, textField1);
        HBox hBox4 = new HBox(backbutton, okbutton);
        VBox vBox = new VBox(label, hBox, hBox1, hBox2, hBox3, hBox4, errorlabel);
        vBox.setPrefWidth(500);
        vBox.setPrefHeight(300);
        vBox.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.CENTER);
        hBox3.setAlignment(Pos.CENTER);
        hBox4.setAlignment(Pos.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label1.setAlignment(Pos.CENTER);
        label1.setTextAlignment(TextAlignment.CENTER);
        label2.setAlignment(Pos.CENTER);
        label2.setTextAlignment(TextAlignment.CENTER);
        label3.setAlignment(Pos.CENTER);
        label3.setTextAlignment(TextAlignment.CENTER);
        label4.setAlignment(Pos.CENTER);
        label4.setTextAlignment(TextAlignment.CENTER);
        errorlabel.setAlignment(Pos.CENTER);
        errorlabel.setTextAlignment(TextAlignment.CENTER);
        VBox.setMargin(hBox, new Insets(20, 0, 20, 0));
        VBox.setMargin(hBox2, new Insets(20, 0, 20, 0));
        VBox.setMargin(hBox4, new Insets(20, 0, 20, 0));
        HBox.setMargin(label1, new Insets(0, 150, 0, 0));
        HBox.setMargin(label2, new Insets(0, 130, 0, 0));
        HBox.setMargin(label3, new Insets(0, 35, 0, 0));
        HBox.setMargin(label4, new Insets(0, 40, 0, 0));
        HBox.setMargin(backbutton, new Insets(0, 150, 0, 0));
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
                try {
                    if (textField.getText().isEmpty()) {
                        Main.errorsound.play();
                        Main.errorsound.seek(Duration.ZERO);

                        errorlabel.setText("ERROR: Hall name could not be empty");
                    } else if (textField1.getText().isEmpty()) {
                        Main.errorsound.play();
                        Main.errorsound.seek(Duration.ZERO);

                        errorlabel.setText("ERROR: Price could not be empty");
                    } else {
                        String name = textField.getText();
                        int price = Integer.parseInt(textField1.getText());

                        if (price < 0) {
                            Main.errorsound.play();
                            Main.errorsound.seek(Duration.ZERO);

                            errorlabel.setText("ERROR: Price must be bigger than 0");
                        } else if (Main.halls.get(name) != null) {
                            Main.errorsound.play();
                            Main.errorsound.seek(Duration.ZERO);

                            errorlabel.setText("ERROR: There are already a hall with this name");
                        } else {
                            Main.halls.put(name, new Hall(SelectFilmPage.selectedfilm.Name, name, price, row.getValue(), collumn.getValue()));
                            Hall.seatbuilder(Main.halls.get(name));
                            errorlabel.setText("SUCCESS: Hall successfully created!");
                        }
                    }
                } catch (NumberFormatException e) {
                    Main.errorsound.play();
                    Main.errorsound.seek(Duration.ZERO);

                    errorlabel.setText("ERROR: Price must be bigger than 0");
                }


            }
        });
        return group;
    }
}
