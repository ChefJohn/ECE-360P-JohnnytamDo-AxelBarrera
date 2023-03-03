import java.io.*;
import java.util.*;

public class BookServer {
    public static void main(String[] args) {
        int tcpPort;
        int udpPort;
        if (args.length != 1) {
            System.out.println("ERROR: Provide 1 argument: input file containing initial inventory");
            System.exit(-1);
        }
        String fileName = args[0];
        tcpPort = 7000;
        udpPort = 8000;

        HashMap<String, Integer> bookCountMap = new HashMap<>();

        // parse the inventory file
        File file = new File(fileName);

        try {
            Scanner sc = new Scanner(file);

            while(sc.hasNextLine()){
                
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not found!");
        }

        // TODO: handle request from clients
    }
}
