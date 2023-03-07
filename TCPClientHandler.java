import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TCPClientHandler implements Runnable {
    HashMap<String,Integer> bookCountMap;
    HashMap<String,LibUser> userClassMap;
    HashMap<Integer,LibUser> loanClassMap;
    Socket u;
    BookServer server;
    DataInputStream inm;
    DataOutputStream outm;
    boolean exit = false;

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
        try {
            inm = new DataInputStream(u.getInputStream());
            outm = new DataOutputStream(u.getOutputStream());
        }catch (IOException e){System.out.println(e);}
    }

    @Override
    public void run() {
        while(!exit){
            try {
                String message = receiveTCP();
                String[] tokens = tokenizeMessage(message);
                String response = executeCommand(tokens);
                sendTCP(response);
            }catch (Exception e){System.out.println(e);}
        }
    }

    private String executeCommand(String[] token) throws Exception{
        //TODO: Does the bulk of the work, handling all the commands
        String result = "";
        LibUser user;
        switch (token[0]){
            case "0":
                //set mode
                //REMEMBER TO ADDRESS NONE MESSAGE RECEPTION FOR CLIENT
                if(token[1].equals("u")){
                    result = "The communication mode is set to UDP\n";
                }else if(token[1].equals("t")){
                    result = "The communication mode is set to TCP\n";
                }else{
                    result = "NONE";
                }
                break;
            case "1":
                //begin loan
                if(!userClassMap.containsKey(token[1])){
                    user = new LibUser(token[1],bookCountMap);
                    userClassMap.put(token[1],user);
                }else{
                    user = userClassMap.get(token[1]);
                }
                //checking to see if requested book exists
                if (bookCountMap.get(token[2]) == null){
                    result = "Request Failed - We do not have this book\n";
                    return result;
                }
                //checking to see if requested book has avilability
                if (bookCountMap.get(token[2]) == 0){
                    result = "Request Failed - Book not available\n";
                    return result;
                }
                int loanID = server.getNewLoan();
                result = user.userBeginLoan(loanID,token[2]);
                loanClassMap.put(loanID,user);
                break;
            case "2":
                // end loan
                int endLoanID = Integer.parseInt(token[1]);
                user = loanClassMap.get(endLoanID);

                if (user == null){
                    user = new LibUser(token[1], bookCountMap);
                }

                result = user.userEndLoan(endLoanID);
                break;
            case "3":
                // get loans
                String username = token[1];
                user = userClassMap.get(username);

                if (user == null){
                    user = new LibUser(token[1], bookCountMap);
                }

                result = user.userGetLoan();
                break;
            case "4":
                // get inventory
                for (Map.Entry<String,Integer> mapElement : bookCountMap.entrySet()) {
                    result +=  mapElement.getKey() + " " + mapElement.getValue() + "\n";
                }
                break;
            case "5":
                // exit
                exit=true;
                printInventory();
                result = "NONE";
                break;
        }
        return result;
    }

    private String[] tokenizeMessage(String message){
        //TODO: Splits the message, 0th indx = command,
        // 1st indx = 1st parameter, 2nd indx = 2nd parameter
        String[] answer = message.split("\\|");
        return answer;
    }

    private void sendTCP(String message)throws Exception {
        outm.writeUTF(message);
    }

    private String receiveTCP() throws IOException{
        String message = inm.readUTF();
        return message;
    }

    private void printInventory()throws Exception{
        String result = "";
        for (Map.Entry<String,Integer> mapElement : bookCountMap.entrySet()) {
            result +=  mapElement.getKey() + " " + mapElement.getValue() + "\n";
        }
        FileWriter fileWriter = new FileWriter("inventory.txt");
        BufferedWriter file = new BufferedWriter(fileWriter);
        file.write(result);
        file.close();
    }
}
