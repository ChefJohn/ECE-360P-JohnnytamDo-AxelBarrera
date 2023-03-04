import java.io.*;
import java.util.*;

//IMPORTANT NOTE: UNCOMMENT OUT THE ARGS SHIT WHEN DONE TESTING

public class BookServer {
    public static void main(String[] args) {
        int tcpPort;
        int udpPort;
        // if (args.length != 1) {
        //     System.out.println("ERROR: Provide 1 argument: input file containing initial inventory");
        //     System.exit(-1);
        // }
        //String fileName = args[0];
        tcpPort = 7000;
        udpPort = 8000;

        HashMap<String, Integer> bookCountMap = new HashMap<>();
        HashMap<String,LibUser> userClassMap = new HashMap<>();
        HashMap<String,LibUser> loanClassMap = new HashMap<>();

        // parse the inventory file (also change this to the filename variable when done testing)
        File file = new File("/Users/johnnydo/Documents/School/Concurrent/Project3/ECE-360P-JohnnytamDo-AxelBarrera/input_file.txt");

        try {
            Scanner sc = new Scanner(file);

            //for each line, map the number of books to corresponding book name
            while(sc.hasNextLine()){
                String[] line = sc.nextLine().split("\" ");
                bookCountMap.put(line[0] + "\"", Integer.parseInt(line[1]));
            }

            sc.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File not found!");
        }

        //TESTING PARSING
        for (Map.Entry<String,Integer> mapElement : bookCountMap.entrySet()) {
            System.out.println(mapElement.getKey());
            System.out.println(mapElement.getValue());
        }

        // TODO: handle request from clients
    }
}
