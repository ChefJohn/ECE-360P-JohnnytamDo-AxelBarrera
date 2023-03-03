
import java.util.*;

public class LibUser {
    private String user;
    private HashMap<String,Integer> bookCountMap;
    private HashMap<Integer,String> loanBookMap;

    public LibUser(String user, HashMap<String, Integer> bookCountMap) {
        this.user = user;
        this.bookCountMap = bookCountMap;
    }


}
