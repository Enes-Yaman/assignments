import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader fiksturinput = new BufferedReader(new FileReader(args[0]));
        BufferedWriter f = new BufferedWriter(new FileWriter("Football.txt"));
        BufferedWriter v = new BufferedWriter(new FileWriter("Volleyball.txt"));
        BufferedWriter h = new BufferedWriter(new FileWriter("Handball.txt"));
        BufferedWriter b = new BufferedWriter(new FileWriter("Basketball.txt"));
        String line;
        ArrayList<Football> footballs = new ArrayList<>();
        ArrayList<Handball> handballs = new ArrayList<>();
        ArrayList<Basketball> basketballs = new ArrayList<>();
        ArrayList<Volleyball> volleyballs = new ArrayList<>();
        ArrayList<String> footballteams = new ArrayList<>();
        ArrayList<String> handballteams = new ArrayList<>();
        ArrayList<String> basketballteams = new ArrayList<>();
        ArrayList<String> volleyballteams = new ArrayList<>();

        while ((line = fiksturinput.readLine()) != null) {
            String[] linee = line.split("\t");
            ArrayList<Integer> intlist = new ArrayList<>();
            for (String s : linee[3].split(":")) {
                intlist.add(Integer.parseInt(s));
            }
            String e = Arrays.toString(linee);
            Object[] scorelist = intlist.toArray();
            if (Objects.equals(linee[0], "F")) {
                if (!footballteams.contains(linee[1])) {
                    footballteams.add(linee[1]);
                    footballs.add(new Football(linee[1], (Integer) scorelist[0], (Integer) scorelist[1]));
                } else {
                    footballs.get(footballteams.indexOf(linee[1])).setTotalgoalconceded((Integer) scorelist[1]);
                    footballs.get(footballteams.indexOf(linee[1])).setTotalgoalscored((Integer) scorelist[0]);
                    if ((Integer) scorelist[0] > (Integer) scorelist[1]) {
                        footballs.get(footballteams.indexOf(linee[1])).setTotalscore(3);
                        footballs.get(footballteams.indexOf(linee[1])).setWinnedmatches();

                    }
                    if (scorelist[0] == scorelist[1]) {
                        footballs.get(footballteams.indexOf(linee[1])).setTotalscore(1);
                        footballs.get(footballteams.indexOf(linee[1])).setTiematches();
                    }
                    else{
                        footballs.get(footballteams.indexOf(linee[1])).setLosedmatches();
                    }
                }
                if (!footballteams.contains(linee[2])) {
                    footballteams.add(linee[2]);
                    footballs.add(new Football(linee[2], (Integer) scorelist[1], (Integer) scorelist[0]));
                } else {
                    footballs.get(footballteams.indexOf(linee[2])).setTotalgoalconceded((Integer) scorelist[0]);
                    footballs.get(footballteams.indexOf(linee[2])).setTotalgoalscored((Integer) scorelist[1]);
                    if ((Integer) scorelist[0] < (Integer) scorelist[1]) {
                        footballs.get(footballteams.indexOf(linee[2])).setTotalscore(3);
                        footballs.get(footballteams.indexOf(linee[2])).setWinnedmatches();
                    }
                    else if (scorelist[0] == scorelist[1]) {
                        footballs.get(footballteams.indexOf(linee[2])).setTotalscore(1);
                        footballs.get(footballteams.indexOf(linee[2])).setTiematches();
                    }
                    else{
                        footballs.get(footballteams.indexOf(linee[2])).setLosedmatches();
                    }
                }
            }
            else if (Objects.equals(linee[0], "H")) {
                if (!handballteams.contains(linee[1])) {
                    handballteams.add(linee[1]);
                    handballs.add(new Handball(linee[1], (Integer) scorelist[0], (Integer) scorelist[1]));
                } else {
                    handballs.get(handballteams.indexOf(linee[1])).setTotalgoalconceded((Integer) scorelist[1]);
                    handballs.get(handballteams.indexOf(linee[1])).setTotalgoalscored((Integer) scorelist[0]);
                    if ((Integer) scorelist[0] > (Integer) scorelist[1]) {
                        handballs.get(handballteams.indexOf(linee[1])).setTotalscore(2);
                        handballs.get(handballteams.indexOf(linee[1])).setWinnedmatches();

                    }
                    if (scorelist[0] == scorelist[1]) {
                        handballs.get(handballteams.indexOf(linee[1])).setTotalscore(1);
                        handballs.get(handballteams.indexOf(linee[1])).setTiematches();
                    }
                    else{
                        handballs.get(handballteams.indexOf(linee[1])).setLosedmatches();
                    }
                }
                if (!handballteams.contains(linee[2])) {
                    handballteams.add(linee[2]);
                    handballs.add(new Handball(linee[2], (Integer) scorelist[1], (Integer) scorelist[0]));
                } else {
                    handballs.get(handballteams.indexOf(linee[2])).setTotalgoalconceded((Integer) scorelist[0]);
                    handballs.get(handballteams.indexOf(linee[2])).setTotalgoalscored((Integer) scorelist[1]);
                    if ((Integer) scorelist[0] < (Integer) scorelist[1]) {
                        handballs.get(handballteams.indexOf(linee[2])).setTotalscore(2);
                        handballs.get(handballteams.indexOf(linee[2])).setWinnedmatches();
                    }
                    if (scorelist[0] == scorelist[1]) {
                        handballs.get(handballteams.indexOf(linee[2])).setTotalscore(1);
                        handballs.get(handballteams.indexOf(linee[2])).setTiematches();
                    }
                    else{
                        handballs.get(handballteams.indexOf(linee[2])).setLosedmatches();
                    }
                }
            }
            else if (Objects.equals(linee[0], "B")) {
                if (!basketballteams.contains(linee[1])) {
                    basketballteams.add(linee[1]);
                    basketballs.add(new Basketball(linee[1], (Integer) scorelist[0], (Integer) scorelist[1]));
                } else {
                    basketballs.get(basketballteams.indexOf(linee[1])).setTotalgoalconceded((Integer) scorelist[1]);
                    basketballs.get(basketballteams.indexOf(linee[1])).setTotalgoalscored((Integer) scorelist[0]);
                    if ((Integer) scorelist[0] > (Integer) scorelist[1]) {
                        basketballs.get(basketballteams.indexOf(linee[1])).setTotalscore(2);
                        basketballs.get(basketballteams.indexOf(linee[1])).setWinnedmatches();

                    }
                    else{
                        basketballs.get(basketballteams.indexOf(linee[1])).setLosedmatches();
                        basketballs.get(basketballteams.indexOf(linee[1])).setTotalscore(1);
                    }
                }
                if (!basketballteams.contains(linee[2])) {
                    basketballteams.add(linee[2]);
                    basketballs.add(new Basketball(linee[2], (Integer) scorelist[1], (Integer) scorelist[0]));
                } else {
                    basketballs.get(basketballteams.indexOf(linee[2])).setTotalgoalconceded((Integer) scorelist[0]);
                    basketballs.get(basketballteams.indexOf(linee[2])).setTotalgoalscored((Integer) scorelist[1]);
                    if ((Integer) scorelist[0] < (Integer) scorelist[1]) {
                        basketballs.get(basketballteams.indexOf(linee[2])).setTotalscore(2);
                        basketballs.get(basketballteams.indexOf(linee[2])).setWinnedmatches();
                    }
                    else{
                        basketballs.get(basketballteams.indexOf(linee[2])).setLosedmatches();
                        basketballs.get(basketballteams.indexOf(linee[2])).setTotalscore(1);
                    }
                }
            }
            else if (Objects.equals(linee[0], "V")) {
                if (!volleyballteams.contains(linee[1])) {
                    volleyballteams.add(linee[1]);
                    volleyballs.add(new Volleyball(linee[1], (Integer) scorelist[0], (Integer) scorelist[1]));
                } else {
                    volleyballs.get(volleyballteams.indexOf(linee[1])).setTotalgoalconceded((Integer) scorelist[1]);
                    volleyballs.get(volleyballteams.indexOf(linee[1])).setTotalgoalscored((Integer) scorelist[0]);
                    if ((Integer) scorelist[0] > (Integer) scorelist[1] + 1) {
                        volleyballs.get(volleyballteams.indexOf(linee[1])).setTotalscore(3);
                        volleyballs.get(volleyballteams.indexOf(linee[1])).setWinnedmatches();
                    }
                    else if ((Integer)scorelist[0] == (Integer) scorelist[1] + 1 ) {
                        volleyballs.get(volleyballteams.indexOf(linee[1])).setTotalscore(2);
                        volleyballs.get(volleyballteams.indexOf(linee[1])).setWinnedmatches();
                    }
                    else if ((Integer)scorelist[0] == (Integer) scorelist[1] - 1){
                        volleyballs.get(volleyballteams.indexOf(linee[1])).setLosedmatches();
                        volleyballs.get(volleyballteams.indexOf(linee[1])).setTotalscore(1);
                    }else{
                        volleyballs.get(volleyballteams.indexOf(linee[1])).setLosedmatches();
                    }
                }
                if (!volleyballteams.contains(linee[2])) {
                    volleyballteams.add(linee[2]);
                    volleyballs.add(new Volleyball(linee[2], (Integer) scorelist[1], (Integer) scorelist[0]));
                } else {
                    volleyballs.get(volleyballteams.indexOf(linee[2])).setTotalgoalconceded((Integer) scorelist[0]);
                    volleyballs.get(volleyballteams.indexOf(linee[2])).setTotalgoalscored((Integer) scorelist[1]);
                    if ((Integer) scorelist[1] > (Integer) scorelist[0] + 1) {
                        volleyballs.get(volleyballteams.indexOf(linee[2])).setTotalscore(3);
                        volleyballs.get(volleyballteams.indexOf(linee[2])).setWinnedmatches();
                    }
                    else if ((Integer)scorelist[1] == (Integer) scorelist[0] + 1 ) {
                        volleyballs.get(volleyballteams.indexOf(linee[2])).setTotalscore(2);
                        volleyballs.get(volleyballteams.indexOf(linee[2])).setWinnedmatches();
                    }
                    else if ((Integer)scorelist[1] == (Integer) scorelist[0] - 1){
                        volleyballs.get(volleyballteams.indexOf(linee[2])).setLosedmatches();
                        volleyballs.get(volleyballteams.indexOf(linee[2])).setTotalscore(1);
                    }else{
                        volleyballs.get(volleyballteams.indexOf(linee[2])).setLosedmatches();
                    }
                }
            }
        }
        ArrayList<Object[]> footballarray = new ArrayList<>();
        footballs.sort(new Comparator<Football>() {
            @Override
            public int compare(Football o1, Football o2) {
                if (o2.getTotalscore() != o1.getTotalscore()){
                    return Integer.compare(o2.getTotalscore(), o1.getTotalscore());}
                else if (o2.getTotalgoalscored() - o2.getTotalgoalconceded() != o1.getTotalgoalscored() - o1.getTotalgoalconceded()){
                    return Integer.compare(o2.getTotalgoalscored()-o2.getTotalgoalconceded(), o1.getTotalgoalscored()-o1.getTotalgoalconceded());
                }
                else{
                    return String.CASE_INSENSITIVE_ORDER.compare(o1.getTeamname(), o2.getTeamname());
                }
            }
        });
        volleyballs.sort(new Comparator<Volleyball>() {
            @Override
            public int compare(Volleyball o1, Volleyball o2) {
                if (o2.getTotalscore() != o1.getTotalscore()){
                    return Integer.compare(o2.getTotalscore(), o1.getTotalscore());}
                else if (o2.getTotalgoalscored() - o2.getTotalgoalconceded() != o1.getTotalgoalscored() - o1.getTotalgoalconceded()){
                    return Integer.compare(o2.getTotalgoalscored()-o2.getTotalgoalconceded(), o1.getTotalgoalscored()-o1.getTotalgoalconceded());
                }
                else{
                    return String.CASE_INSENSITIVE_ORDER.compare(o1.getTeamname(), o2.getTeamname());
                }
            }
        });
        basketballs.sort(new Comparator<Basketball>() {
            @Override
            public int compare(Basketball o1, Basketball o2) {
                if (o2.getTotalscore() != o1.getTotalscore()){
                    return Integer.compare(o2.getTotalscore(), o1.getTotalscore());}
                else if (o2.getTotalgoalscored() - o2.getTotalgoalconceded() != o1.getTotalgoalscored() - o1.getTotalgoalconceded()){
                    return Integer.compare(o2.getTotalgoalscored()-o2.getTotalgoalconceded(), o1.getTotalgoalscored()-o1.getTotalgoalconceded());
                }
                else{
                    return String.CASE_INSENSITIVE_ORDER.compare(o1.getTeamname(), o2.getTeamname());
                }
            }
        });
        handballs.sort(new Comparator<Handball>() {
            @Override
            public int compare(Handball o1, Handball o2) {
                if (o2.getTotalscore() != o1.getTotalscore()){
                    return Integer.compare(o2.getTotalscore(), o1.getTotalscore());}
                else if (o2.getTotalgoalscored() - o2.getTotalgoalconceded() != o1.getTotalgoalscored() - o1.getTotalgoalconceded()){
                    return Integer.compare(o2.getTotalgoalscored()-o2.getTotalgoalconceded(), o1.getTotalgoalscored()-o1.getTotalgoalconceded());
                }
                else{
                    return String.CASE_INSENSITIVE_ORDER.compare(o1.getTeamname(), o2.getTeamname());
                }
            }
        });
        for (int i = 0 ; i<footballteams.size(); i++){
            Football a = footballs.get(i);
            int atotal = a.getLosedmatches() + a.getWinnedmatches() + a.getTiematches();
            f.write(i+1 + "\t" + a.getTeamname() + "\t" + atotal + "\t" + a.getWinnedmatches() + "\t" + a.getTiematches() + "\t" + a.getLosedmatches() + "\t" + a.getTotalgoalscored() + ":" + a.getTotalgoalconceded() + "\t" + a.getTotalscore() + "\n");
        }
        for (int i = 0 ; i<basketballs.size(); i++){
            Basketball a = basketballs.get(i);
            int atotal = a.getLosedmatches() + a.getWinnedmatches();
            b.write(i+1 + "\t" + a.getTeamname() + "\t" + atotal + "\t" + a.getWinnedmatches() + "\t" + "0" + "\t" + a.getLosedmatches() + "\t" + a.getTotalgoalscored() + ":" + a.getTotalgoalconceded() + "\t" + a.getTotalscore() +"\n");
        }
        for (int i = 0 ; i<volleyballs.size(); i++){
            Volleyball a = volleyballs.get(i);
            int atotal = a.getLosedmatches() + a.getWinnedmatches();
            v.write(i+1 + "\t" + a.getTeamname() + "\t" + atotal + "\t" + a.getWinnedmatches() + "\t" + "0" + "\t" + a.getLosedmatches() + "\t" + a.getTotalgoalscored() + ":" + a.getTotalgoalconceded() + "\t" + a.getTotalscore() + "\n");
        }
        for (int i = 0 ; i<handballs.size(); i++){
            Handball a = handballs.get(i);
            int atotal = a.getLosedmatches() + a.getWinnedmatches() + a.getTiematches();
            h.write(i+1 + "\t" + a.getTeamname() + "\t" + atotal + "\t" + a.getWinnedmatches() + "\t" + a.getTiematches() + "\t" + a.getLosedmatches() + "\t" + a.getTotalgoalscored() + ":" + a.getTotalgoalconceded() + "\t" + a.getTotalscore() + "\n");
        }
        f.close();
        b.close();
        v.close();
        h.close();
    }
}
class Football {
    private int totalscore, totalgoalscored, totalgoalconceded,winnedmatches,losedmatches,tiematches;
    private final String teamname;

    public Football(String teamname, int goalscored, int goalconceded) {
        this.totalgoalscored = goalscored + this.totalgoalscored;
        this.totalgoalconceded = goalconceded + this.totalgoalconceded;
        if (goalscored > goalconceded) {
            this.totalscore = this.totalscore + 3;
            setWinnedmatches();
        } else if (goalscored == goalconceded) {
            this.totalscore = this.totalscore + 1;
            setTiematches();
        }
        else{
            setLosedmatches();
        }
        this.teamname = teamname;

    }

    public int getWinnedmatches() {
        return winnedmatches;
    }

    public void setWinnedmatches() {
        this.winnedmatches ++;
    }

    public int getLosedmatches() {
        return losedmatches;
    }

    public void setLosedmatches() {
        this.losedmatches ++;
    }

    public int getTiematches() {
        return tiematches;
    }

    public void setTiematches() {
        this.tiematches ++;
    }

    public String getTeamname() {
        return teamname;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore + this.totalscore;
    }

    public int getTotalgoalscored() {
        return totalgoalscored;
    }

    public void setTotalgoalscored(int totalgoalscored) {
        this.totalgoalscored = totalgoalscored + this.totalgoalscored;
    }

    public int getTotalgoalconceded() {
        return totalgoalconceded;
    }

    public void setTotalgoalconceded(int totalgoalconceded) {
        this.totalgoalconceded = totalgoalconceded + this.totalgoalconceded;
    }
}
class Handball {
    private int totalscore, totalgoalscored, totalgoalconceded,winnedmatches,losedmatches,tiematches;
    private final String teamname;

    public Handball(String teamname, int goalscored, int goalconceded) {
        this.totalgoalscored = goalscored + this.totalgoalscored;
        this.totalgoalconceded = goalconceded + this.totalgoalconceded;
        if (goalscored > goalconceded) {
            this.totalscore = this.totalscore + 2;
            setWinnedmatches();
        } else if (goalscored == goalconceded) {
            this.totalscore = this.totalscore + 1;
            setTiematches();
        }
        else{
            setLosedmatches();
        }
        this.teamname = teamname;

    }

    public int getWinnedmatches() {
        return winnedmatches;
    }

    public void setWinnedmatches() {
        this.winnedmatches ++;
    }

    public int getLosedmatches() {
        return losedmatches;
    }

    public void setLosedmatches() {
        this.losedmatches ++;
    }

    public int getTiematches() {
        return tiematches;
    }

    public void setTiematches() {
        this.tiematches ++;
    }

    public String getTeamname() {
        return teamname;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore + this.totalscore;
    }

    public int getTotalgoalscored() {
        return totalgoalscored;
    }

    public void setTotalgoalscored(int totalgoalscored) {
        this.totalgoalscored = totalgoalscored + this.totalgoalscored;
    }

    public int getTotalgoalconceded() {
        return totalgoalconceded;
    }

    public void setTotalgoalconceded(int totalgoalconceded) {
        this.totalgoalconceded = totalgoalconceded + this.totalgoalconceded;
    }
}
class Basketball {
    private int totalscore, totalgoalscored, totalgoalconceded,winnedmatches,losedmatches;
    private final String teamname;

    public Basketball(String teamname, int goalscored, int goalconceded) {
        this.totalgoalscored = goalscored + this.totalgoalscored;
        this.totalgoalconceded = goalconceded + this.totalgoalconceded;
        if (goalscored > goalconceded) {
            this.totalscore = this.totalscore + 2;
            setWinnedmatches();
        }else{
            this.totalscore = this.totalscore + 1;
            setLosedmatches();
        }

        this.teamname = teamname;

    }

    public int getWinnedmatches() {
        return winnedmatches;
    }

    public void setWinnedmatches() {
        this.winnedmatches ++;
    }

    public int getLosedmatches() {
        return losedmatches;
    }

    public void setLosedmatches() {
        this.losedmatches ++;
    }

    public String getTeamname() {
        return teamname;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore + this.totalscore;
    }

    public int getTotalgoalscored() {
        return totalgoalscored;
    }

    public void setTotalgoalscored(int totalgoalscored) {
        this.totalgoalscored = totalgoalscored + this.totalgoalscored;
    }

    public int getTotalgoalconceded() {
        return totalgoalconceded;
    }

    public void setTotalgoalconceded(int totalgoalconceded) {
        this.totalgoalconceded = totalgoalconceded + this.totalgoalconceded;
    }
}
class Volleyball {
    private int totalscore, totalgoalscored, totalgoalconceded,winnedmatches,losedmatches;
    private final String teamname;

    public Volleyball(String teamname, int goalscored, int goalconceded) {
        this.totalgoalscored = goalscored + this.totalgoalscored;
        this.totalgoalconceded = goalconceded + this.totalgoalconceded;
        if (goalscored > goalconceded + 1) {
            this.totalscore = this.totalscore + 3;
            setWinnedmatches();
        }else if (goalscored == goalconceded + 1){
            this.totalscore = this.totalscore + 2;
            setWinnedmatches();
        }else if (goalscored == goalconceded -1){
            this.totalscore = this.totalscore + 1;
            setLosedmatches();
        }else {
            setLosedmatches();
        }
        this.teamname = teamname;

    }

    public int getWinnedmatches() {
        return winnedmatches;
    }

    public void setWinnedmatches() {
        this.winnedmatches ++;
    }

    public int getLosedmatches() {
        return losedmatches;
    }

    public void setLosedmatches() {
        this.losedmatches ++;
    }

    public String getTeamname() {
        return teamname;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore + this.totalscore;
    }

    public int getTotalgoalscored() {
        return totalgoalscored;
    }

    public void setTotalgoalscored(int totalgoalscored) {
        this.totalgoalscored = totalgoalscored + this.totalgoalscored;
    }

    public int getTotalgoalconceded() {
        return totalgoalconceded;
    }

    public void setTotalgoalconceded(int totalgoalconceded) {
        this.totalgoalconceded = totalgoalconceded + this.totalgoalconceded;
    }
}
