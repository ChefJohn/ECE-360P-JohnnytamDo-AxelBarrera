
import java.util.*;

public class LibUser {
    private String user;
    private HashMap<String,Integer> bookCountMap;
    private HashMap<Integer,String> loanBookMap;

    public LibUser(String user, HashMap<String, Integer> bookCountMap) {
        this.user = user;
        this.bookCountMap = bookCountMap;
        this.loanBookMap = new HashMap<>();
    }

    public void userBeginLoan(){
        /*TODO: fulfills the begin-loan command. If the request is valid, then this function will map the loan id with
        the book name to the respective user
        */
    }

    public void userEndLoan(){
        /*TODO: fulfills the end-loan command. If the requests is valid, then this function will unmap the loan id and 
        the book associated with it from the respective user
        */
    }

    public void userGetLoan(){
        /*TODO: fulfills the get-loan command. If the requests is valid, then this function will just return all the loans
        that is associated with the respective user. If not, then just simply return ‘No record found for <user-name>’
        */
    }

}
