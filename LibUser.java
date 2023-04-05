
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class LibUser {
    private String user;
    private HashMap<String,InventoryList> bookCountMap;       //book name: # of books available
    private HashMap<Integer,String> loanBookMap;        //loanID: book name
    ReentrantLock loanMapLock;

    public LibUser(String user, HashMap<String, InventoryList> bookCountMap) {
        this.user = user;
        this.bookCountMap = bookCountMap;
        this.loanBookMap = new HashMap<>();
        ReentrantLock loanMapLock = new ReentrantLock(true);
    }

    public String userBeginLoan(int loanID, String bookName){
        /*TODO: fulfills the begin-loan command. If the request is valid,
            then this function will map the loan id with
            the book name to the respective user
        */

        String result = "";

        //if all checks out, then loan out the book to the respective user
        loanMapLock.lock();
        loanBookMap.put(loanID, bookName);
        loanMapLock.unlock();
        /**
         * Writes to book Count
         */
        bookCountMap.get(bookName).decreaseCount();
        result = "Your request has been approved, " + loanID + " " + this.user + " " + bookName + "\n";
        return result;
    }

    public String userEndLoan(int loanID){
        /*TODO: fulfills the end-loan command. If the requests is valid,
            then this function will unmap the loan id and
            the book associated with it from the respective user
        */

        String result = "";
        loanMapLock.lock();
        if (loanBookMap.get(loanID) == null){
            result = loanID + " not found, no such borrow record\n";
            return result;
        }

        //if valid, then give the loan book back
        String bookName = loanBookMap.get(loanID);
        loanBookMap.remove(loanID);
        loanMapLock.unlock();

        bookCountMap.get(bookName).increaseCount();
        result = loanID + " is returned\n";
        return result;
    }

    public String userGetLoan(){
        /*TODO: fulfills the get-loan command. If the requests is valid, then this function will just return all the loans
        that is associated with the respective user. If not, then just simply return ‘No record found for <user-name>’
        */

        String result = "";
        //checking if the respective user has any loaned books
        loanMapLock.lock();
        if (loanBookMap.isEmpty()){
            result = "No record found for " + this.user + "\n";
            return result;
        }

        //if valid then return array of all loans
        for (Map.Entry<Integer,String> mapElement : loanBookMap.entrySet()) {
            result +=  mapElement.getKey() + " " + mapElement.getValue() + "\n";
        }
        loanMapLock.unlock();
        return result;
    }

}
