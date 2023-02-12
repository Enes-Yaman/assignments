import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static StringBuffer print = new StringBuffer();
    public static Map<Integer, Players> playersmap;
    public static int dice = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader commands = new BufferedReader(new FileReader(args[0]));
        new PropertyJsonReader();
        new ListJsonReader();
        String line;
        Map<Integer, Players> playersMap = new HashMap<>();
        playersMap.put(1, new Players("Player 1"));
        playersMap.put(2, new Players("Player 2"));
        playersmap = playersMap;
        while ((line = commands.readLine()) != null && playersMap.get(1).getMoney() >= 0 && playersMap.get(2).getMoney() >= 0 && !Players.isGamefinished()) {
            if (line.equals("show()")) {
                show(playersMap);
            } else {
                String[] splittedline = line.split(";");
                dice = Integer.parseInt(splittedline[1]);
                if (Objects.equals(splittedline[0], "Player 1") && playersMap.get(1).getJail() == 0) {
                    replacee(playersMap,1,splittedline[1]);
                } else if ((Objects.equals(splittedline[0], "Player 2") && playersMap.get(2).getJail() == 0)) {
                    replacee(playersMap,2,splittedline[1]);
                } else if (Objects.equals(splittedline[0], "Player 1") && playersMap.get(1).getJail() != 0) {
                    playersMap.get(1).setJail(playersMap.get(1).getJail() - 1);
                    print.append(playersMap.get(1)).append("\t").append(splittedline[1]).append("\t").append(playersMap.get(1).getLocation()).append("\t").append(playersMap.get(1).getMoney()).append("\t").append(playersMap.get(2).getMoney()).append("\t");
                    print.append(playersMap.get(1)).append(" in jail (count=").append(3 - playersMap.get(1).getJail()).append(")\n");
                } else if (Objects.equals(splittedline[0], "Player 2") && playersMap.get(2).getJail() != 0){
                    playersMap.get(2).setJail(playersMap.get(2).getJail() - 1);
                    print.append(playersMap.get(2)).append("\t").append(splittedline[1]).append("\t").append(playersMap.get(2).getLocation()).append("\t").append(playersMap.get(1).getMoney()).append("\t").append(playersMap.get(2).getMoney()).append("\t");
                    print.append(playersMap.get(2)).append(" in jail (count=").append(3 - playersMap.get(2).getJail()).append(")\n");
                }

            }
        }
        show(playersMap);
        BufferedWriter writer = new BufferedWriter(new FileWriter("monitoring.txt"));
        writer.write(String.valueOf(print));
        writer.close();

    }
    public static void replacee(Map<Integer,Players> playersMap,int a,String dice){
        print.append(playersMap.get(a)).append("\t").append(dice).append("\t").append("Konum").append("\t").append("Para1").append("\t").append("Para2").append("\t");
        playersMap.get(a).move(Integer.parseInt(dice));
        print.replace(print.indexOf("Para1"), print.indexOf("Para1") + 5, String.valueOf(playersMap.get(1).getMoney()));
        print.replace(print.indexOf("Para2"), print.indexOf("Para2") + 5, String.valueOf(playersMap.get(2).getMoney()));
        print.replace(print.indexOf("Konum"), print.indexOf("Konum") + 5, String.valueOf(playersMap.get(a).getLocation()));
    }

    public static void show(Map<Integer, Players> playersMap) {
        print.append("-------------------------------------------------------------------------------------------------------------------------\n");
        print.append(playersMap.get(1)).append("\t").append(playersMap.get(1).getMoney()).append("\thave: ");
        for (Gamemap i : playersMap.get(1).getGamemaps()) {
            print.append(i).append(",");
        }
        print.deleteCharAt(print.length() - 1);
        print.append("\n");
        print.append(playersMap.get(2)).append("\t").append(playersMap.get(2).getMoney()).append("\thave: ");
        for (Gamemap i : playersMap.get(2).getGamemaps()) {
            print.append(i).append(",");
        }
        print.deleteCharAt(print.length() - 1);
        print.append("\n");
        print.append("Banker\t").append(130000 - (playersMap.get(1).getMoney() + playersMap.get(2).getMoney())).append("\n");
        if (playersMap.get(1).getMoney() > playersMap.get(2).getMoney()) {
            print.append("Winner\t").append(playersMap.get(1)).append("\n");
        } else {
            print.append("Winner\t").append(playersMap.get(2)).append("\n");
        }
        print.append("-------------------------------------------------------------------------------------------------------------------------\n");
    }
}
