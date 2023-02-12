import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.TreeSet;


  // for  new add a new Jewel Type don't touch this .java and follow the instructions in Categorize.java
public class Main {
    public static String[][] Gamemap;
    public static int globalscore;
    public static int currentscore;
    public static String Leaderboard;
    public static String Monitoring;
    public static boolean isValid = true;

    public static void main(String[] args) throws IOException {
        // Readers / Writers
        BufferedReader command = new BufferedReader(new FileReader(args[1]));
        RandomAccessFile gridforlengts = new RandomAccessFile(new File(args[0]), "r");
        RandomAccessFile grid = new RandomAccessFile(new File(args[0]), "r");
        BufferedReader readedLeaderboard = new BufferedReader(new FileReader("leaderboard.txt"));
        BufferedWriter writerMonitoring = new BufferedWriter(new FileWriter("monitoring.txt"));

        // initializing the our board (I used 2d list because I think a[y][x] is easier to use)
        int collumn = gridforlengts.readLine().length();
        int row = (int) gridforlengts.length() / collumn;
        collumn = collumn / 2 + 1;
        String[][] a = new String[row][collumn];
        Main.Gamemap = a;

        String line;
        int counter = 0;
        while ((line = grid.readLine()) != null) {
            String[] splittedline = line.split(" ");
            System.arraycopy(splittedline, 0, Main.Gamemap[counter], 0, splittedline.length);
            counter++;
        }
        Main.Monitoring = "Game grid:\n\n";
        printgameMap();

        //reading command.txt and sending values to functions
        while (!Objects.equals(line = command.readLine(), "E")) {
            Main.Monitoring += "\nSelect coordinate or enter E to end the game: " + line + "\n\n";
            String[] splittedline = line.split(" ");
            int x = Integer.parseInt(splittedline[1]);
            int y = Integer.parseInt(splittedline[0]);
            try {
                Categorize.categorize(y, x);
            } catch (ArrayIndexOutOfBoundsException e) {
                isValid = false;
            }
            RefactorGameList.refactor(row, collumn);
            if (isValid) {
                printgameMap();
                Main.Monitoring += "\nScore: " + currentscore + " points\n";
            } else {
                Main.Monitoring += "Please enter a valid coordinate\n";
            }
            currentscore = 0;
        }
        Main.Monitoring += ("\nSelect coordinate or enter E to end the game: E\n\nTotal score: " + globalscore + " points\n\n");
        String name = command.readLine();
        Main.Monitoring += "Enter name: " + name + "\n";

        // initializing players and calculating
        TreeSet<Player> playerTreeSet = new TreeSet<>();
        Player ourplayer = new Player(name, globalscore);
        playerTreeSet.add(ourplayer);
        while ((line = readedLeaderboard.readLine()) != null) {
            String[] splittedline = line.split(" ");
            playerTreeSet.add(new Player(splittedline[0], Integer.parseInt(splittedline[1])));
        }
        if (playerTreeSet.headSet(ourplayer).size() == 0) {
            Player playerafter = (Player) playerTreeSet.toArray()[1];
            Main.Monitoring += "\nYour rank is " + (playerTreeSet.headSet(ourplayer).size() + 1 + "/" + playerTreeSet.size()) + ", your score is " + (ourplayer.Score - playerafter.Score) + " points higher than " + (playerafter.Name) + "\n";
        } else if (playerTreeSet.headSet(ourplayer).size() + 1 == playerTreeSet.size()) {
            Player playerbefore = (Player) playerTreeSet.toArray()[playerTreeSet.size() - 1];
            Main.Monitoring += "\nYour rank is " + (playerTreeSet.headSet(ourplayer).size() + 1 + "/" + playerTreeSet.size()) + ", your score is " + (playerbefore.Score - ourplayer.Score) + " points lower than " + (playerbefore.Name) + "\n";
        } else {
            Player playerafter = (Player) playerTreeSet.toArray()[playerTreeSet.headSet(ourplayer).size() + 1];
            Player playerbefore = (Player) playerTreeSet.toArray()[playerTreeSet.headSet(ourplayer).size() - 1];
            Main.Monitoring += "\nYour rank is " + (playerTreeSet.headSet(ourplayer).size() + 1 + "/" + playerTreeSet.size()) + ", your score is " + (playerbefore.Score - ourplayer.Score) + " points lower than " + (playerbefore.Name) + " and " + (ourplayer.Score - playerafter.Score) + " points higher than " + (playerafter.Name + "\n");
        }
        BufferedWriter writerLeaderboard = new BufferedWriter(new FileWriter("leaderboard.txt"));
        Main.Monitoring += "\nGood bye!";
        Main.Leaderboard = "";
        for (Player player : playerTreeSet) {
            Main.Leaderboard += player.Name + " " + player.Score + "\n";
        }
        writerMonitoring.write(Main.Monitoring);
        writerMonitoring.close();
        writerLeaderboard.write(Main.Leaderboard);
        writerLeaderboard.close();

    }

    public static void printgameMap() {
        for (String[] strings : Main.Gamemap) {
            for (String string : strings) {
                Main.Monitoring += string + " ";
            }
            Main.Monitoring += "\n";
        }
    }

}

class delet {
    public static boolean delete(int y, int x, int Ydirection, int Xdirection, boolean isMath) {
        try {
            // method for math symbols and
            String test1 = Main.Gamemap[y - Ydirection][x - Xdirection];
            String test2 = Main.Gamemap[y - 2 * Ydirection][x - 2 * Xdirection];
            String test3 = Main.Gamemap[y][x];
            ArrayList<String> cache = new ArrayList<>();
            cache.add(test1);
            cache.add(test2);
            cache.add(test3);
            if (isMath) {
                // checking is the any "nonMath" symbol
                if (cache.stream().anyMatch(n -> Objects.equals(n, "D") || Objects.equals(n, "T") || Objects.equals(n, "W") || Objects.equals(n, "S"))) {
                    return false;
                }
            } else if (Objects.equals(Main.Gamemap[y][x], "W")) {
                // for Wildcard the order can consist of at most 2 different letters
                HashSet<String> hashSet = new HashSet<>(cache);
                if (!(hashSet.size() < 3)) {
                    return false;
                }
            } else {
                HashSet<String> hashSet = new HashSet<>(cache);
                if (!(hashSet.size() < 2)) {
                    return false;
                }
            }
            Main.Gamemap[y - Ydirection][x - Xdirection] = " ";
            Main.Gamemap[y - 2 * Ydirection][x - 2 * Xdirection] = " ";
            Main.Gamemap[y][x] = " ";
            Categorize.lettertopint(cache);
            return true;

        } catch (ArrayIndexOutOfBoundsException exception) {
            return false;
        }
    }
}

class RefactorGameList {
    public static void refactor(int row, int collumn) {
        // x and y for selecting all variables in matrix and i for that delete may be at the bottom
        for (int i = 0; i < collumn; i++) {
            for (int X = 0; X < collumn; X++) {
                for (int Y = 1; Y < row; Y++) {
                    if (Objects.equals(Main.Gamemap[Y][X], " ")) {
                        String value1 = Main.Gamemap[Y][X];
                        Main.Gamemap[Y][X] = Main.Gamemap[Y - 1][X];
                        Main.Gamemap[Y - 1][X] = value1;
                    }
                }
            }
        }
    }
}

class Player implements Comparable<Player> {
    String Name;
    int Score;

    Player(String name, int score) {
        this.Name = name;
        this.Score = score;
    }

    @Override
    public int compareTo(Player o) {
        return o.Score - this.Score;
    }
}