import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader food = new BufferedReader(new FileReader("food.txt"));
        BufferedReader people = new BufferedReader(new FileReader("people.txt"));
        BufferedReader sport = new BufferedReader(new FileReader("sport.txt"));
        BufferedReader command = new BufferedReader(new FileReader(args[0]));
        BufferedWriter writer = new BufferedWriter(new FileWriter("monitoring.txt"));
        String line;
        Map<Integer, Food> foodidtobject = new HashMap<>();
        Map<Integer, People> peopleidtobject = new HashMap<>();
        Map<Integer, Sport> sportidtobject = new HashMap<>();
        while ((line = food.readLine()) != null) {
            String[] splittedline = line.split("\t");
            foodidtobject.put(Integer.valueOf(splittedline[0]), new Food(splittedline[1], Integer.valueOf(splittedline[2])));
        }
        while ((line = people.readLine()) != null) {
            String[] splittedline = line.split("\t");
            peopleidtobject.put(Integer.valueOf(splittedline[0]), new People(splittedline[1], splittedline[2], Integer.valueOf(splittedline[3]), Integer.valueOf(splittedline[4]), Integer.valueOf(splittedline[5]), Integer.valueOf(splittedline[0]), writer));
        }
        while ((line = sport.readLine()) != null) {
            String[] splittedline = line.split("\t");
            sportidtobject.put(Integer.valueOf(splittedline[0]), new Sport(splittedline[1], Integer.valueOf(splittedline[2])));
        }
        ArrayList<People> commandedpeoples = new ArrayList<>();
        while ((line = command.readLine()) != null) {
            if (line.contains("print")) {
                if (line.contains("List")) {
                    for (People commandedpeople : commandedpeoples) {
                        printline.print(commandedpeople, writer);
                    }
                    writer.write("***************\n");
                } else if (line.contains("Warn")) {
                    int i = 0;
                    for (People commandedpeople : commandedpeoples) {
                        if ((commandedpeople.getAddedcal() - commandedpeople.getInterestedcal()) - commandedpeople.getNeeded() > 0) {
                            printline.print(commandedpeople, writer);
                            i++;
                        }
                    }
                    if (i == 0) {
                        writer.write("there\tis\tno\tsuch\tperson\n");
                    }
                    writer.write("***************\n");
                } else {
                    line = line.replaceAll("\\D+", "");
                    People commandedpeople = peopleidtobject.get(Integer.parseInt(line));
                    printline.print(commandedpeople, writer);
                    writer.write("***************\n");
                }
            } else {
                String[] splittedline = line.split("\t");
                if (foodidtobject.containsKey(Integer.valueOf(splittedline[1]))) {
                    peopleidtobject.get(Integer.parseInt(splittedline[0])).addcalori(foodidtobject.get(Integer.parseInt(splittedline[1])).getKcal(), Integer.parseInt(splittedline[2]), foodidtobject.get(Integer.parseInt(splittedline[1])).getName());
                } else {
                    peopleidtobject.get(Integer.parseInt(splittedline[0])).interestcalori(sportidtobject.get(Integer.parseInt(splittedline[1])).getKcal(), Integer.parseInt(splittedline[2]), sportidtobject.get(Integer.parseInt(splittedline[1])).getName());
                }
                if (!commandedpeoples.contains(peopleidtobject.get(Integer.parseInt(splittedline[0])))) {
                    commandedpeoples.add(peopleidtobject.get(Integer.parseInt(splittedline[0])));
                }
            }
        }
        writer.close();
        RandomAccessFile monitoring = new RandomAccessFile(new File("monitoring.txt"), "rw");
        monitoring.setLength(monitoring.length() - 17);
        monitoring.close();
    }
}

class Food {
    private final String Name;
    private final int Kcal;

    public Food(String name, Integer kcal) {
        this.Name = name;
        this.Kcal = kcal;
    }

    public String getName() {
        return Name;
    }

    public int getKcal() {
        return Kcal;
    }
}

class Sport {
    private final String Name;
    private final int Kcal;

    public Sport(String name, Integer kcal) {
        this.Name = name;
        this.Kcal = kcal;
    }

    public String getName() {
        return Name;
    }

    public int getKcal() {
        return Kcal;
    }
}

class People {
    private static BufferedWriter Writer;
    private final String Name, Gender;
    private final int Weight, Height, Birthdate, Index;
    private int nowcalori = 0, addedcal, interestedcal;

    public People(String name, String gender, Integer weight, Integer height, Integer birthdate, Integer index, BufferedWriter writer) {
        this.Name = name;
        this.Weight = weight;
        this.Height = height;
        this.Birthdate = birthdate;
        this.Gender = gender;
        this.Index = index;
        Writer = writer;
    }

    public String getName() {
        return Name;
    }

    public int getAge() {
        return 2022 - Birthdate;
    }

    public int getNeeded() {
        double a;
        if (Objects.equals(Gender, "male")) {
            a = (66 + (13.75 * Weight + (5 * Height - (6.8 * getAge()))));
        } else {
            a = (665 + (9.6 * Weight + (1.7 * Height - (4.7 * getAge()))));
        }
        return (int) Math.round(a);
    }

    public void addcalori(int calori, int portion, String name) throws IOException {
        nowcalori = nowcalori + (calori * portion);
        Writer.write(Index + "\thas\ttaken\t" + calori * portion + "kcal\tfrom\t" + name + "\n");
        Writer.write("***************\n");
        addedcal = addedcal + (calori * portion);
    }

    public void interestcalori(int calori, int duration, String name) throws IOException {
        nowcalori = nowcalori - (calori * duration / 60);
        Writer.write(Index + "\thas\tburned\t" + (calori * duration / 60) + "kcal\tthank\tto\t" + name + "\n");
        Writer.write("***************\n");
        interestedcal = interestedcal + (calori * duration / 60);
    }

    public int getAddedcal() {
        return addedcal;
    }

    public int getInterestedcal() {
        return interestedcal;
    }
}

class printline {
    public static void print(People a, BufferedWriter writer) throws IOException {
        if (((a.getAddedcal() - a.getInterestedcal()) - a.getNeeded()) > 0) {
            writer.write(a.getName() + "\t" + a.getAge() + "\t" + a.getNeeded() + "kcal\t" + a.getAddedcal() + "kcal\t" + a.getInterestedcal() + "kcal\t+" + ((a.getAddedcal() - a.getInterestedcal()) - a.getNeeded()) + "kcal\n");
        } else {
            writer.write(a.getName() + "\t" + a.getAge() + "\t" + a.getNeeded() + "kcal\t" + a.getAddedcal() + "kcal\t" + a.getInterestedcal() + "kcal\t" + ((a.getAddedcal() - a.getInterestedcal()) - a.getNeeded()) + "kcal\n");
        }
    }
}
