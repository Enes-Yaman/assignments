public class RailRoad extends Properties {
    public RailRoad(String name, int cost) {
        super(cost,0);
        this.Name = name;
        this.Cost = cost;
    }
    public int getRent(){
        return 25;
    }

    @Override
    public int isbuyable() {
        return 3;
    }
}
