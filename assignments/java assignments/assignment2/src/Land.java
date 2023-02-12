public class Land extends Properties {

    public Land(String name, int cost) {
        super(0, cost);
        this.Cost = cost;
        if (cost<2001){
            this.Rent=cost/10*4;
        }else if (cost<3001){
            this.Rent=cost/10*3;
        }else{
            this.Rent=cost/100*35;
        }this.Name = name;
    }
}
