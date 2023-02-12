import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader inputfile = new BufferedReader(new FileReader(args[0]));
        BufferedWriter list = new BufferedWriter(new FileWriter(args[1]));
        String line;
        ArrayList<String> Armstrong = new ArrayList<>();
        ArrayList<Integer> Ascending = new ArrayList<Integer>();
        ArrayList<Integer> Descending = new ArrayList<Integer>();
        ArrayList<Integer> armstronglist = new ArrayList<Integer>();
        while (!Objects.equals(line = inputfile.readLine(), "Ascending order sorting")) {
            Armstrong.add(line);
        }
        ArrayList<Integer> Armstrongs = new ArrayList<Integer>();
        int limit = Integer.parseInt(Armstrong.get(1));
        for(int i = 100; i< limit;i++){
            int lenisit = String.valueOf(i).length();
            int repo, repo2, result = 0;
            repo = i;
            while (repo != 0)
            {
                repo2 = repo % 10;
                result += Math.pow(repo2, lenisit);
                repo /= 10;
            }
            if(result == i){
                Armstrongs.add(i);
            }
        }
        list.write("Calculating Armstrong numbers\n");
        for(int number : Armstrongs){
            list.write(number+" ");
        }
        list.write("\n");
        list.write("Ascending order sorting\n");

        while (!Objects.equals(line = inputfile.readLine(), "-1")) {
            Ascending.add(Integer.parseInt(line));
            Collections.sort(Ascending);
            for(int numberAs : Ascending){
                list.write(numberAs+" ");
            }
            list.write("\n");
        }
        list.write("Descending order sorting\n");
        inputfile.readLine();
        while (!Objects.equals(line = inputfile.readLine(), "-1")) {
            Descending.add(Integer.parseInt(line));
            Collections.sort(Descending);
            Collections.reverse(Descending);
            for(int numberDes : Descending){
                list.write(numberDes+" ");
            }
            list.write("\n");
        }
        while (Objects.equals(line = inputfile.readLine(), "Exit")){
            list.write("Terminated..");
            break;
        }
        inputfile.close();
        list.close();

    }

}
