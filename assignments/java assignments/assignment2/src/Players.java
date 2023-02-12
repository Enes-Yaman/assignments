import java.util.ArrayList;
import java.util.List;

public class Players {
    private static boolean gamefinished;
    private int location;
    private int money;
    private int jail;
    private List<RailRoad> arr = new ArrayList<>();
    private final ArrayList<Gamemap> gamemaps;
    private final String name;

    public List<RailRoad> getArr() {
        return arr;
    }

    public Players(String name) {
        this.name = name;
        this.location = 1;
        this.money = 15000;
        this.gamemaps = new ArrayList<>();
    }

    public static boolean isGamefinished() {
        return gamefinished;
    }

    public int getMoney() {
        return money;
    }

    public Players setMoney(int money) {
        this.money = this.money + money;
        return this;
    }

    public int getJail() {
        return jail;
    }

    public Players setJail(int jail) {
        this.jail = jail;
        return this;
    }

    public ArrayList<Gamemap> getGamemaps() {
        return gamemaps;
    }

    public void move(int a) {
        if (a + location > 40) {
            money += 200;
            location = ((a + location) % 40);
        } else {
            location += a;
        }
        buyordo(PropertyJsonReader.properties.get(location));
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void buyordo(Gamemap object) {
        if (object.isbuyable() == 0 && this.money >= object.getCost() || object.isbuyable() == 3 && object.getOwner() == null) {
            if (object.isbuyable()==3){
                if (!arr.contains(object)){
                    arr.add((RailRoad) object);
                }
            }
            Properties properties = (Properties) object;
            properties.setOwner(this);
            gamemaps.add(object);
            this.money = this.money - object.getCost();
        } else if (object.isbuyable() == 0 && this.money < object.getCost()) {
            Main.print.append(this).append(" goes bankrupt\n");
            gamefinished = true;
        } else if (object.isbuyable() == 1 || object.isbuyable() == 3) {
            if (object.getOwner() == this) {
                Main.print.append(this).append(" has ").append(object).append("\n");
            } else {
                if (object.isbuyable()==3){
                    this.setMoney(-object.getOwner().getArr().size()*object.getRent());
                    object.getOwner().setMoney(-object.getOwner().getArr().size()*object.getRent());
                }else {
                object.getOwner().setMoney(object.getRent());
                this.setMoney(-object.getRent());}
                Main.print.append(this).append(" paid rent for ").append(object).append("\n");
            }
        } else {
            object.doit(this);
        }
    }

    public String toString() {
        return name;
    }
}
