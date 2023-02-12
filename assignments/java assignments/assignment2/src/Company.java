public class Company extends Properties {
    public Company(String name, int cost) {
        super(0, cost);
        this.Name = name;
        this.Cost = cost;
    }

    public int getRent() {
        return Main.dice * 4;
    }

}
