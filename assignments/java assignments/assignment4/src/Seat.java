import javafx.scene.image.Image;

import java.io.File;

public class Seat {
    static Image imagenotselled;
    static Image imageselled;
    int row, collumn;
    int price;
    User owner;
    Image image;
    String hallname, filmname;

    Seat(String hallname, int row, int collumn, User owner, int price, String filmname) {
        this.collumn = collumn;
        this.row = row;
        this.owner = owner;
        this.price = price;
        this.hallname = hallname;
        this.filmname = filmname;
        if (imageselled == null) {
            getImage();
        }
    }

    Seat(String hallname, int row, int collumn, String filmname) {
        this.price = 0;
        this.row = row;
        this.collumn = collumn;
        this.filmname = filmname;
        this.hallname = hallname;
        if (imagenotselled == null) {
            getImage();
        }
    }

    public int getRow() {
        return row;
    }

    public Seat setRow(int row) {
        this.row = row;
        return this;
    }

    public int getCollumn() {
        return collumn;
    }

    public Seat setCollumn(int collumn) {
        this.collumn = collumn;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Image getImage() {
        File file = new File(Main.directorypath + "/assets/icons/empty_seat.png");
        File filereserved = new File(Main.directorypath + "/assets/icons/reserved_seat.png");
        String fileurl = file.toURI().toString();
        String fileurlreserved = filereserved.toURI().toString();
        Image image = new Image(fileurl);
        Image imagereserved = new Image(fileurlreserved);
        if (owner == null) {
            this.image = image;
            imagenotselled = image;
            return image;
        } else {
            imageselled = imagereserved;
            this.image = imagereserved;
            return imagereserved;
        }
    }
}
