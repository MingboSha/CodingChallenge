import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by minbosha on 01/04/2017.
 */

public class Feature2 {
    private HashMap<String, Long> resourceMap;
    public Feature2() {

        this.resourceMap = new HashMap<>();
    }

    public void scan(Request req) {
        String resource = req.getResource();
        int bytes = req.getBytes();
        if (bytes != -1 && resource != null) {
            if (!resourceMap.containsKey(resource)) {
                resourceMap.put(resource, (long) bytes);
            } else {
                resourceMap.put(resource, resourceMap.get(resource) + bytes);
            }
        }
    }

    public void generateResult (String outputPath2) throws IOException {
        PriorityQueue<String> resourceQueue = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return compareHost(o1, o2);
            }
        });

        for (String key : resourceMap.keySet()) {
            //System.out.println("resource: "+key);
            if (resourceQueue.size() == 10) {
                if (compareHost(key, resourceQueue.peek()) > 0) {
                    resourceQueue.poll();
                    resourceQueue.offer(key);
                }
            } else {
                resourceQueue.offer(key);
            }
        }

        ArrayList<String> resultList = new ArrayList<>();

        while (!resourceQueue.isEmpty()) {
            String resource = resourceQueue.poll();
            System.out.println("Resource: "+resource+", Bytes: "+resourceMap.get(resource));
            String resultLine = resource+"\n";
            resultList.add(resultLine);
        }

        File outputFile = new File(outputPath2);
        outputFile.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(outputFile);

        for (int i = resultList.size() - 1; i >= 0; i--) {
            fw.write(resultList.get(i));
        }
        fw.close();

    }


    private int compareHost (String resource1, String resource2) {
        if (resourceMap.get(resource1) > resourceMap.get(resource2)) {
            return 1;
        } else if (resourceMap.get(resource1).equals(resourceMap.get(resource2))) {
            if (resource1.compareTo(resource2) > 0) {
                return -1;
            } else if (resource1.compareTo(resource2) == 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return -1;
        }
    }


}