import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<Contact> contacts = new ArrayList<>();
        HashMap<String,Contact> hashMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        TreeSet<LastNameComparator> sort = new TreeSet();
        String line;
        int index = 0;
        while ((line = reader.readLine()) != null){
            String[] strings = line.split(" ");
            contacts.add(new Contact(strings[3],strings[1],strings[2],strings[0]));
            sort.add(new LastNameComparator(strings[3],strings[1],strings[2],strings[0]));
            hashMap.put(strings[0],contacts.get(index));
            index++;
        }

        BufferedWriter Arraylistwriter = new BufferedWriter(new FileWriter("contactsArrayList.txt"));
        for (Contact c : contacts) {
            Arraylistwriter.write(c+"\n");
        } Arraylistwriter.close();

        LastNameComparator.compare(contacts);
        BufferedWriter lastnamewriter = new BufferedWriter(new FileWriter("contactsArrayListOrderByLastName.txt"));
        for (Contact c : contacts) {
            lastnamewriter.write(c+"\n");
        } lastnamewriter.close();

        BufferedWriter hashsetwriter = new BufferedWriter(new FileWriter("contactsHashSet.txt"));
        HashSet<Contact> contactHashSet = new HashSet<>(contacts);
        for (Contact c : contactHashSet) {
            hashsetwriter.write(c+"\n");
        } hashsetwriter.close();

        BufferedWriter treeSetwriter = new BufferedWriter(new FileWriter("contactsTreeSet.txt"));
        TreeSet contactTreeSet = new TreeSet(contacts);
        for (Object c : contactTreeSet) {
            treeSetwriter.write(c+"\n");
        } treeSetwriter.close();

        BufferedWriter sortedtreeSetwriter = new BufferedWriter(new FileWriter("contactsTreeSetOrderByLastName.txt"));
        for (Object i : sort) {
            sortedtreeSetwriter.write(i+"\n");
        }sortedtreeSetwriter.close();

        BufferedWriter hashmapwriter = new BufferedWriter(new FileWriter("file.txt"));
        hashMap.forEach((k,v) -> {
            try {
                hashmapwriter.write(v+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });hashmapwriter.close();

    }
}
class Contact implements Comparable<Contact>{
    private String socialSecurityNumber;
    private String firstName;

    public String getLastName() {
        return lastName;
    }

    private String lastName;
    private String phonenumber;
    @Override
    public int compareTo(Contact o) {
        return this.phonenumber.compareTo(o.phonenumber);
    }
    Contact(String socialSecurityNumber, String firstName, String lastName, String phonenumber){
        this.firstName = firstName;
        this.socialSecurityNumber = socialSecurityNumber;
        this.lastName = lastName;
        this.phonenumber = phonenumber;
    }

    @Override
    public String toString() {
        return (this.phonenumber+" "+this.firstName+" "+this.lastName+" "+this.socialSecurityNumber);
    }
}
class LastNameComparator extends Contact implements Comparable<Contact> {
    LastNameComparator(String socialSecurityNumber, String firstName, String lastName, String phonenumber) {
        super(socialSecurityNumber, firstName, lastName, phonenumber);
    }
    public static void compare(List<Contact> contacts){
        contacts.sort((o1, o2) -> CharSequence.compare(o1.getLastName(), o2.getLastName()));}

    public int compareTo(Contact o) {
        return this.getLastName().compareTo(o.getLastName());
    }
}