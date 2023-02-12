import java.util.Arrays;

public class Stack{
    int[] integerList = new int[50];
    Stack(){
        Arrays.fill(this.integerList, -1);
        this.Size();
    }
    public void Push(int Number){
        this.integerList[this.Size()] = Number;
        this.Size();
    }
    public int Pop(){
        this.integerList[this.Size()] = -1;
        this.Size();
        return Top();
    }
    public int Top(){
        this.Size();
        return this.integerList[this.Size()-1];
    }
    public boolean isFull(){
        if (this.Size() == 50){
            return true;
        }else{
            return false;
        }
    }
    public boolean isEmpty(){
        if (this.Size() == 0){
            return true;
        }else {
            return false;
        }
    }
    public int Size(){
        int sizeholder = 0;
        for (int i: this.integerList) {
            if (i != -1){
                sizeholder ++;
            }
        }
        return sizeholder;
    }
}