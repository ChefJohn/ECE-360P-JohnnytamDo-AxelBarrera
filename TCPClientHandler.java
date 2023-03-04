import java.net.Socket;
import java.util.HashMap;

public class TCPClientHandler implements Runnable {
    HashMap<String,Integer> bookCountMap;
    HashMap<String,LibUser> userClassMap;
    HashMap<Integer,LibUser> loanClassMap;
    Socket u;
    BookServer server;

    public TCPClientHandler(HashMap<String, Integer> bookCountMap,
                            HashMap<String, LibUser> userClassMap,
                            HashMap<Integer, LibUser> loanClassMap,
                            Socket u,
                            BookServer server) {
        this.bookCountMap = bookCountMap;
        this.userClassMap = userClassMap;
        this.loanClassMap = loanClassMap;
        this.u = u;
        this.server = server;
    }

    @Override
    public void run() {

    }
}
