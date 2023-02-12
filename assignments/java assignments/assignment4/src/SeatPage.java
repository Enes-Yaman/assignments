import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class SeatPage {
    public static Group getGroup(String value) {
        ArrayList<Button> buttons = new ArrayList<>();
        Hall thishall = Main.halls.get(value);
        GridPane gridPane = new GridPane();
        gridPane.setVgap(thishall.row);
        gridPane.setHgap(thishall.collumn);
        for (Seat s : Main.seats.values()) {
            ImageView imageView;
            Button buttonview = new Button();
            if (Objects.equals(s.hallname, value)) {
                if (s.getOwner() == null) {
                    imageView = new ImageView(Seat.imagenotselled);
                } else if (s.getOwner() != LoginPage.currentuser && !LoginPage.currentuser.getAdmin()) {
                    imageView = new ImageView(Seat.imageselled);
                    buttonview.setDisable(true);
                } else {
                    imageView = new ImageView(Seat.imageselled);
                }
                buttonview.setGraphic(imageView);
                buttons.add(buttonview);
                imageView.setFitHeight(30);
                imageView.setFitWidth(30);
                GridPane.setConstraints(buttonview, s.collumn, s.row);
                gridPane.getChildren().addAll(buttonview);
            }
        }

        Label label = new Label(SelectFilmPage.selectedfilm.Name + " Hall: " + thishall.hallname);
        Label labele = new Label();
        ComboBox<String> comboBox = new ComboBox<>();
        for (User h : Main.users.values()) {
            comboBox.getItems().add(h.getName());
        }
        if (comboBox.getItems().size() != 0) {
            comboBox.setValue(comboBox.getItems().get(comboBox.getItems().indexOf(LoginPage.currentuser.Name)));
        } else {
            comboBox.setValue("");
        }
        Button back = new Button("◀ BACK");
        Label label1 = new Label();
        VBox vBox;
        if (LoginPage.currentuser.getAdmin()) {
            vBox = new VBox(label, gridPane, comboBox, labele, label1, back);
            VBox.setMargin(gridPane, new Insets(20));
            VBox.setMargin(labele, new Insets(20));
            VBox.setMargin(back, new Insets(10, 200, 0, 0));
        } else {
            vBox = new VBox(label, gridPane, label1, back);
            VBox.setMargin(gridPane, new Insets(20));
            VBox.setMargin(back, new Insets(10, 300, 0, 0));

        }
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(500);
        vBox.setPrefHeight(550);
        gridPane.setAlignment(Pos.CENTER);
        for (Button b : buttons) {
            b.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int row = GridPane.getRowIndex(b);
                    int collumn = GridPane.getColumnIndex(b);
                    if (Main.seats.get(thishall.hallname + "_" + row + "_" + collumn).getOwner() == null) {
                        labele.setText("Not bought yet!");
                    } else {
                        labele.setText("Bought by " + Main.seats.get(thishall.hallname + "_" + row + "_" + collumn).getOwner().getName() + " for " + Main.seats.get(thishall.hallname + "_" + row + "_" + collumn).getPrice() + "TL!");
                    }
                }
            });
            b.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int row = GridPane.getRowIndex(b);
                    int collumn = GridPane.getColumnIndex(b);
                    Seat s = Main.seats.get(thishall.hallname + "_" + row + "_" + collumn);
                    User u = Main.users.get(comboBox.getValue());
                    Film f = SelectFilmPage.selectedfilm;
                    if (LoginPage.currentuser.getAdmin()) {
                        if (s.getOwner() == null) {
                            ImageView imageView = new ImageView(Seat.imageselled);
                            imageView.setFitHeight(30);
                            imageView.setFitWidth(30);
                            b.setGraphic(imageView);
                            s.setOwner(u);
                            Double discount = 1.0;
                            if (u.getClup()) {
                                discount = Main.discountpercentage;
                            }
                            s.setPrice((int) (thishall.price * (discount)));
                            label1.setText("Seat at " + (row + 1) + "-" + (collumn + 1) + " is bought for " + u.getName() + " for " + (int) (thishall.price * (discount)) + " TL successfully");
                        } else {
                            User owner = s.getOwner();
                            ImageView imageView = new ImageView(Seat.imagenotselled);
                            imageView.setFitHeight(30);
                            imageView.setFitWidth(30);
                            b.setGraphic(imageView);
                            s.setOwner(null);
                            s.setPrice(0);
                            label1.setText("Seat at " + (row + 1) + "-" + (collumn + 1) + " is refunded for " + owner.getName() + " successfully");
                        }
                    } else {
                        if (s.getOwner() == null) {
                            Double discount = 1.0;
                            if (LoginPage.currentuser.getClup()) {
                                discount = Main.discountpercentage;
                            }
                            label1.setText("Seat at " + (row + 1) + "-" + (collumn + 1) + " is bought for " + (int) (discount * thishall.price) + " successfully!");
                            s.setPrice((int) (discount * thishall.price));
                            s.setOwner(LoginPage.currentuser);
                            ImageView imageView = new ImageView(Seat.imageselled);
                            imageView.setFitHeight(30);
                            imageView.setFitWidth(30);
                            b.setGraphic(imageView);
                        } else {
                            label1.setText("Seat at " + (row + 1) + "-" + (collumn + 1) + " is refunded successfully!");
                            s.setOwner(null);
                            s.setPrice(0);
                            ImageView imageView = new ImageView(Seat.imagenotselled);
                            imageView.setFitHeight(30);
                            imageView.setFitWidth(30);
                            b.setGraphic(imageView);
                        }
                    }
                }
            });
        }
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Group group = WatchFilmPage.getGroup();
                Stage stage = (Stage) back.getScene().getWindow();
                Scene scene = new Scene(group);
                stage.setScene(scene);
                stage.show();
            }
        });
        return new Group(vBox);
    }


}
