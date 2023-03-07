

public class InventoryList {
    String title;
    int num;
    InventoryList next;

    public InventoryList(String title, int num) {
        this.title = title;
        this.num = num;
        next = null;
    }

    public void addNext(InventoryList n){
        next=n;
    }

    public void increaseCount(){
        num+=1;
    }

    public void decreaseCount(){
        num-=1;
    }

    public boolean isAvailable(){
        if(num>0) return true;
        return false;
    }

    public String toString(){
        return title + " " + Integer.toString(num) + "\n";
    }
}
