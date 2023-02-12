public abstract class Gamemap {
    protected String Name;

    public abstract String toString();

    public abstract int getCost();

    public abstract int getRent();

    public abstract int isbuyable();

    public abstract Players getOwner();

    public abstract void doit(Players players);
}

class NotBuyablePlaces extends Gamemap {
    private static final ListJsonReader listJsonReader = new ListJsonReader();
    private static int chance;
    private static int comchest;
    private final int doingcategory;

    public NotBuyablePlaces(String name, int a) {
        this.Name = name;
        this.doingcategory = a;
    }

    public String toString() {
        return Name;
    }

    public int isbuyable() {
        return 2;
    }

    public int getCost() {
        return 0;
    }

    public int getRent() {
        return 0;
    }


    public Players getOwner() {
        return null;
    }

    public void doit(Players players) {
        switch (doingcategory) {
            case 0: {
                Main.print.append(players).append(" is in GO square\n");
                players.setLocation(1);
		break;
            }
            case 1: {
                Main.print.append(players).append(" draw ").append(listJsonReader.getCommunity().get(comchest)).append("\n");
                if (listJsonReader.getCommunity().get(comchest).equals("Advance to Go (Collect $200)")) {
                    players.setLocation(1);
                    players.setMoney(200);
                } else if (listJsonReader.getCommunity().get(comchest).equals("Bank error in your favor - collect $75")) {
                    players.setMoney(75);
                } else if (listJsonReader.getCommunity().get(comchest).equals("Doctor's fees - Pay $50")) {
                    players.setMoney(-50);
                } else if (listJsonReader.getCommunity().get(comchest).equals("It is your birthday Collect $10 from each player")) {
                    players.setMoney(+10);
                    if (Main.playersmap.get(1) == players) {
                        Main.playersmap.get(2).setMoney(-10);
                    } else {
                        Main.playersmap.get(1).setMoney(-10);
                    }
                } else if (listJsonReader.getCommunity().get(comchest).equals("Grand Opera Night - collect $50 from every player for opening night seats")) {
                    players.setMoney(+50);
                    if (Main.playersmap.get(1) == players) {
                        Main.playersmap.get(2).setMoney(-50);
                    } else {
                        Main.playersmap.get(1).setMoney(-50);
                    }
                } else if (listJsonReader.getCommunity().get(comchest).equals("Income Tax refund - collect $20")) {
                    players.setMoney(20);
                } else if (listJsonReader.getCommunity().get(comchest).equals("Life Insurance Matures - collect $100")) {
                    players.setMoney(100);
                } else if (listJsonReader.getCommunity().get(comchest).equals("Pay Hospital Fees of $100")) {
                    players.setMoney(-100);
                } else if (listJsonReader.getCommunity().get(comchest).equals("Pay School Fees of $50")) {
                    players.setMoney(-50);
                } else if (listJsonReader.getCommunity().get(comchest).equals("You inherit $100")) {
                    players.setMoney(100);
                } else if (listJsonReader.getCommunity().get(comchest).equals("From sale of stock you get $50")) {
                    players.setMoney(50);
                }
                comchest++;
                if (comchest == 11){
                    comchest = 0;
                }
		break;
            }
            case 2: {
                Main.print.append(players).append(" paid Tax\n");
                players.setMoney(-100);
		break;

            }
            case 3: {
                Main.print.append(players).append(" draw ").append(listJsonReader.getChance().get(chance)).append(" ");
                if (listJsonReader.getChance().get(chance).equals("Advance to Go (Collect $200)")) {
                    Main.print.append("\n");
                    players.setLocation(1);
                    players.setMoney(200);
                } else if (listJsonReader.getChance().get(chance).equals("Advance to Leicester Square")) {
                    players.setLocation(27);
                    players.buyordo(PropertyJsonReader.properties.get(27));
                } else if (listJsonReader.getChance().get(chance).equals("Go back 3 spaces")) {
                    players.setLocation(players.getLocation() - 3);
                    players.buyordo(PropertyJsonReader.properties.get(players.getLocation()));
                } else if (listJsonReader.getChance().get(chance).equals("Pay poor tax of $15")) {
                    players.setMoney(-15);
                    Main.print.append("\n");
                } else if (listJsonReader.getChance().get(chance).equals("Your building loan matures - collect $150")) {
                    Main.print.append("\n");
                    players.setMoney(150);
                } else if (listJsonReader.getChance().get(chance).equals("You have won a crossword competition - collect $100 ")) {
                    players.setMoney(100);
                    Main.print.append("\n");
                }
                chance++;
                if (chance == 6){
                    chance = 0;
                }
		break;
            }
            case 4: {
                Main.print.append(players).append(" went to jail\n");
                players.setLocation(11);
                players.setJail(3);
		break;
            }
        }
    }

}

class Properties extends Gamemap {
    protected int Cost;
    protected int Rent;
    private Players owner;

    public Properties(int cost, int rent) {
        this.Cost = cost;
        this.Rent = rent;
    }

    @Override
    public String toString() {
        return Name;
    }

    public int getCost() {
        return Cost;
    }

    public int getRent() {
        return Rent;
    }


    public int isbuyable() {
        if (this.owner == null) {
            return 0;
        } else {
            return 1;
        }
    }

    public Players getOwner() {
        return owner;
    }

    public void setOwner(Players player) {
        this.owner = player;
        Main.print.append(player.toString()).append(" bought ").append(this).append("\n");
    }

    public void doit(Players players) {
    }
}
