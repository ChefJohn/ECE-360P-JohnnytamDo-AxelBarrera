import java.util.concurrent.locks.ReentrantLock;

public class InventoryList {
    String title;
    int num;
    InventoryList next;
    ReentrantLock nodeLock;

    public InventoryList(String title, int num) {
        this.title = title;
        this.num = num;
        next = null;
        ReentrantLock nodeLock = new ReentrantLock(true);
    }

    public void addNext(InventoryList n){
        next=n;
    }

    public void increaseCount(){
        nodeLock.lock();
        num+=1;
        nodeLock.unlock();
    }

    public void decreaseCount(){
        nodeLock.lock();
        num-=1;
        nodeLock.unlock();
    }

    public boolean isAvailable(){
        boolean answer;
        nodeLock.lock();
        if(num>0) answer = true;
        else answer = false;
        nodeLock.unlock();
        return answer;
    }

    public String toString(){
        String answer = "";
        nodeLock.lock();
        answer = title + " " + Integer.toString(num) + "\n";
        nodeLock.unlock();
        return answer;
    }
}
