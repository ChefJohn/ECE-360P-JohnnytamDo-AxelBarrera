
import java.util.*;

public class LibUser {
    private String user;
    private HashMap<String,Integer> bookCountMap;       //book name: # of books available
    private HashMap<Integer,String> loanBookMap;        //loanID: book name

    public LibUser(String user, HashMap<String, Integer> bookCountMap) {
        this.user = user;
        this.bookCountMap = bookCountMap;
        this.loanBookMap = new HashMap<>();
    }

    public String userBeginLoan(int loanID, String bookName){
        /*TODO: fulfills the begin-loan command. If the request is valid,
            then this function will map the loan id with
            the book name to the respective user
        */

        String result = "";



        //if all checks out, then loan out the book to the respective user
        loanBookMap.put(loanID, bookName);
        bookCountMap.put(bookName, bookCountMap.get(bookName) - 1);
        result = "Your request has been approved, " + loanID + " " + this.user + " " + bookName;
        return result;
    }

    public String userEndLoan(int loanID){
        /*TODO: fulfills the end-loan command. If the requests is valid,
            then this function will unmap the loan id and
            the book associated with it from the respective user
        */

        String result = "";

        if (loanBookMap.get(loanID) == null){
            result = loanID + " not found, no such borrow record";
            return result;
        }

        //if valid, then give the loan book back
        loanBookMap.remove(loanID);
        String bookName = loanBookMap.get(loanID);
        bookCountMap.put(bookName, bookCountMap.get(bookName) + 1);
        result = loanID + " is returned";
        return result;
    }

    public String userGetLoan(){
        /*TODO: fulfills the get-loan command. If the requests is valid, then this function will just return all the loans
        that is associated with the respective user. If not, then just simply return ‘No record found for <user-name>’
        */

        String result = "";
        //checking if the respective user has any loaned books
        if (loanBookMap.isEmpty()){
            result = "No record found for " + this.user;
            return result;
        }

        //if valid then return array of all loans
        for (Map.Entry<Integer,String> mapElement : loanBookMap.entrySet()) {
            result +=  mapElement.getKey() + " " + mapElement.getValue() + "\n";
        }

        return result;
    }

}
