import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by minbosha on 01/04/2017.
 */

public class Feature1 {
    private HashMap<String, Integer> hostMap;

    public Feature1() {

        this.hostMap = new HashMap<>();
    }

    public void scan(Request req) {
        String host = req.getHost();
        if (host == null) {
            String line = req.requestLine;
            System.out.println("Null line at: "+line);
            host = "null";
        }
        if (!hostMap.containsKey(host)) {
            hostMap.put(host, 1);
        } else {
            hostMap.put(host, hostMap.get(host)+1);
        }
    }

    public void generateResult (String outputPath1) throws IOException {
        PriorityQueue<String> hostQueue = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return compareHost(o1, o2);
            }
        });

        for (String key : hostMap.keySet()) {
            //System.out.println("key: "+key);
            if (hostQueue.size() == 10) {
                if (compareHost(key, hostQueue.peek()) > 0) {
                    hostQueue.poll();
                    hostQueue.offer(key);
                }
            } else {
                hostQueue.offer(key);
            }
        }

        ArrayList<String> resultList = new ArrayList<>();

        while (!hostQueue.isEmpty()) {
            String host = hostQueue.poll();
            System.out.println("Host: "+host+", Feq: "+hostMap.get(host));
            String resultLine = host+","+hostMap.get(host)+"\n";
            resultList.add(resultLine);
        }

        File outputFile = new File(outputPath1);
        outputFile.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(outputFile);

        for (int i = resultList.size() - 1; i >= 0; i--) {
            fw.write(resultList.get(i));
        }
        fw.close();

    }


    private int compareHost (String host1, String host2) {
        if (hostMap.get(host1) > hostMap.get(host2)) {
            return 1;
        } else if (hostMap.get(host1).equals(hostMap.get(host2))) {
            if (host1.compareTo(host2) > 0) {
                return -1;
            } else if (host1.compareTo(host2) == 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return -1;
        }
    }


}
