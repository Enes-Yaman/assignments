import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

public class Main extends Application {
    public static Path directorypath = Paths.get("").toAbsolutePath().getParent();
    public static HashMap<String, User> users = new HashMap();
    public static HashMap<String, Hall> halls = new HashMap();
    public static HashMap<String, Film> films = new HashMap();
    public static HashMap<String, Seat> seats = new HashMap();
    public static MediaPlayer errorsound;
    public static int maximum_error_without_getting_blocked;
    public static String title;
    public static double discountpercentage;
    public static int blocktime;
    public static boolean extraenabled = false;

    public static void main(String[] args) throws IOException {
        readbackup();
        readextra();
        launch();
        writebackup();
        writeextra();
    }

    private static void writeextra() throws IOException {
        if(extraenabled){
        BufferedWriter writer = new BufferedWriter(new FileWriter(directorypath+"/assets/data/extra.dat"));
        String extra = "";
        for (User u : users.values()) {
            extra += (u.getName() + "\t" + u.getQuestion() + "\t" + u.getAnswer() + "\n");
        }
        writer.write(extra);
        writer.close();
    }}

    private static void readextra() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(directorypath+"/assets/data/extra.dat"));
            extraenabled = true;
            String line;
            while ((line = reader.readLine()) != null){
                String[] lines = line.split("\t");
                if (lines[1] != null && !lines[1].equals("null")) {
                    users.get(lines[0]).setQuestion(lines[1]);
                    users.get(lines[0]).setAnswer(lines[2]);
                }
            }
        } catch (IOException exception) {
            extraenabled = false;
        }
    }

    public static void readbackup() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(directorypath + "/assets/data/backup.dat"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strings = line.split("\t");
                if (Objects.equals(strings[0], "user")) {
                    boolean clup = false;
                    boolean admin = false;
                    if (Objects.equals(strings[3], "true")) {
                        clup = true;
                    }
                    if (Objects.equals(strings[4], "true")) {
                        admin = true;
                    }
                    users.put(strings[1], new User(strings[1], strings[2], clup, admin));
                } else if (Objects.equals(strings[0], "film")) {
                    films.put(strings[1], new Film(strings[1], Paths.get((strings[2])), Integer.parseInt(strings[3])));
                } else if (Objects.equals(strings[0], "hall")) {
                    halls.put(strings[2], new Hall(strings[1], strings[2], Integer.parseInt(strings[3]), Integer.parseInt(strings[4]), Integer.parseInt(strings[5])));
                } else if (Objects.equals(strings[0], "seat")) {
                    if (Objects.equals(strings[5], "null")) {
                        seats.put(strings[2] + "_" + strings[3] + "_" + strings[4], new Seat(strings[2], Integer.parseInt(strings[3]), Integer.parseInt(strings[4]), strings[1]));
                    } else {
                        seats.put(strings[2] + "_" + strings[3] + "_" + strings[4], new Seat(strings[2], Integer.parseInt(strings[3]), Integer.parseInt(strings[4]), users.get(strings[5]), Integer.parseInt(strings[6]), strings[1]));
                    }
                }
            }
        }catch (IOException exception){
            users.put("admin",new User("admin",LoginPage.hashPassword("password"),true,true));
        }finally {
            properties();
        }

    }

    public static void writebackup() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(Main.directorypath + "/assets/data/backup.dat"));
        String backup = "";
        for (User u : users.values()) {
            backup += ("user\t" + u.getName() + "\t" + u.getPassword() + "\t" + u.getClup() + "\t" + u.getAdmin() + "\n");
        }
        for (Hall h : halls.values()) {
            backup += ("hall\t" + h.filmname + "\t" + h.hallname + "\t" + h.price + "\t" + h.row + "\t" + h.collumn + "\n");
        }
        for (Film f : films.values()) {
            backup += ("film\t" + f.Name + "\t" + f.TrailerPath + "\t" + f.Duration + "\n");
        }
        for (Seat s : seats.values()) {
            String k = null;
            if (s.owner != null) {
                k = s.owner.getName();
            }
            backup += ("seat\t" + s.filmname + "\t" + s.hallname + "\t" + s.row + "\t" + s.collumn + "\t" + k + "\t" + s.price + "\n");
        }
        writer.write(backup);
        writer.close();
    }

    public static void properties() throws IOException {
        File file = new File(Main.directorypath + "/assets/effects/error.mp3");
        String fileurl = file.toURI().toString();
        javafx.scene.media.Media media = new javafx.scene.media.Media(fileurl);
        errorsound = new MediaPlayer(media);
        BufferedReader reader = new BufferedReader(new FileReader(Main.directorypath + "/assets/data/properties.dat"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("maximum-error-without-getting-blocked=")) {
                line = line.replace("maximum-error-without-getting-blocked=", "");
                maximum_error_without_getting_blocked = Integer.parseInt(line);
            } else if (line.contains("title=")) {
                line = line.replace("title=", "");
                title = line;
            } else if (line.contains("discount-percentage=")) {
                line = line.replace("discount-percentage=", "");
                discountpercentage = (100.0 - Integer.parseInt(line)) / 100.0;
            } else if (line.contains("block-time=")) {
                line = line.replace("block-time=", "");
                blocktime = Integer.parseInt(line);
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Group group = LoginPage.getGroup();
        Scene scene = new Scene(group, 600, 400);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.getIcons().add(new Image(new File(directorypath + "/assets/icons/logo.png").toURI().toString()));
        stage.show();
    }
}

