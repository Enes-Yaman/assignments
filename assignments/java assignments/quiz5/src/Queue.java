import java.util.LinkedList;

public class Queue extends LinkedList<Integer> {
    public void enqueueFront(int val){
        add(0,val);
    }
    public void enqueueBack(int val){
        add(size(),val);
    }public void enqueueMiddle(int val){
        add(size()/2,val);
    }public int dequeueFront(){
        try {
            return remove(0);
        }catch (Exception e){
            return -1;
        }
    }public int dequeueLast(){
        try {
            return remove(size()-1);
        }catch (Exception e){
            return -1;
        }
    }public int dequeueMiddle(){
        try {
            return remove((size()-1)/2);
        }catch (Exception e){
            return -1;
        }
    }
}
