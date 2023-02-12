import java.util.Objects;

public class Hall {
    String filmname, hallname;
    int price, row, collumn;

    public Hall(String filmname, String hallname, int price, int row, int collumn) {
        this.collumn = collumn;
        this.filmname = filmname;
        this.hallname = hallname;
        this.price = price;
        this.row = row;
    }

    public static void seatbuilder(Hall h) {
        for (int i = 0; i < h.row; i++) {
            for (int j = 0; j < h.collumn; j++) {
                Main.seats.put(h.hallname + "_" + i + "_" + j, new Seat(h.hallname, i, j, null, 0, h.filmname));
            }
        }
    }

    public static void seatdeleter(Hall h) {
        Main.seats.values().removeIf(s -> Objects.equals(s.hallname, h.hallname));
    }

}
