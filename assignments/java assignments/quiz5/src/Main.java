import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    static boolean isprinted = false;
    static String output ="";
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        int[] list = new int[50];
        String line;
        String[] linelist;
        ArrayList<Integer> arrayList = new ArrayList<>();
        Queue queue = new Queue();
        while ((line = reader.readLine()) != null){
            if (line.length()<7){
            arrayList.add(Integer.valueOf(line));
            } else {
                if (!Main.isprinted){
                    a(arrayList);
                }
                linelist = line.split(" ");
                if (linelist.length>1){
                    if (Objects.equals(linelist[0], "enqueFront")){
                        queue.enqueueFront(Integer.parseInt(linelist[1]));
                    }else if (Objects.equals(linelist[0], "enqueBack")){
                        queue.enqueueBack(Integer.parseInt(linelist[1]));
                    }else {
                        queue.enqueueMiddle(Integer.parseInt(linelist[1]));
                    }Main.output+=(queue+"\n");
                }else {
                    try {
                    if (Objects.equals(linelist[0], "dequeFront")){
                        queue.dequeueFront();
                    }else if(Objects.equals(linelist[0], "dequeMiddle")){
                        queue.dequeueMiddle();
                    }else {
                        queue.dequeueLast();
                    }Main.output+=(queue+"\n");
                    }catch (IndexOutOfBoundsException e){
                        Main.output+=("[]\n");
                    }
                }
            }

        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        writer.write(Main.output);
        writer.close();
    }
    public static void a(ArrayList<Integer> arrayList){
        ArrayList<Stack> stackArrayList = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            stackArrayList.add(new Stack());
            stackArrayList.get(i).Push(arrayList.get(i));
            decitooctal(stackArrayList.get(i));
        }
    }
    public static void decitooctal(Stack stack){
        StringBuffer sbr = new StringBuffer("");
        while (stack.Top()!=0){
            sbr.append(stack.Top()%8);
            stack.Push(stack.Top() / 8);
        }
        Main.output+=(sbr.reverse()+"\n");
        Main.isprinted = true;
    }

}