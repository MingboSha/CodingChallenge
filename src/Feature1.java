import java.io.*;
import java.util.ArrayList;
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

    /**
     * Scan one line of request, and keep the host and frequency information in HashMap
     * @param req object for a request line
     */
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

    /**
     * Loop over the HashMap and maintain a minHeap with size 10
     * Write the output in file
     * @param outputPath1 The output path for this feature
     * @throws IOException
     */
    public void generateResult (String outputPath1) throws IOException {
        PriorityQueue<String> hostQueue = new PriorityQueue<>(this::compareHost);

        for (String key : hostMap.keySet()) {
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
            //System.out.println("Host: "+host+", Feq: "+hostMap.get(host));
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

    /**
     * A comparator function to used by minHeap, the host with lower frequency has higher priority
     * If two host have same frequency than compare the lexicographical order
     * @param host1 the first host
     * @param host2 the second host
     * @return priority
     */
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
