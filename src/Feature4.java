import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by minbosha on 01/04/2017.
 */

public class Feature4 {

    private HashMap<String, ArrayList<Long>> failureMap;
    private HashMap<String, Long> blockMap;
    private ArrayList<String> resultList;

    public Feature4() {
        this.failureMap = new HashMap<>();
        this.blockMap = new HashMap<>();
        this.resultList = new ArrayList<>();
    }

    /**
     * Scan the request line and put the blocked request in a result list
     * @param request Request object of the line
     */
    public void scan(Request request) {
        String statusCode = request.getStatusCode();
        String host = request.getHost();
        Long secondTime = parseTime(request.getDateTime());
        /*
         Tell if the request should be blocked
         If the request was blocked but already passed the block time, remove from the block list
         */
        if (blockMap.containsKey(host)) {
            if (blockMap.get(host) + 300 >= secondTime) {
                resultList.add(request.requestLine+"\n");
                return;
            } else {
                blockMap.remove(host);
            }
        }

        /*
         If the request shouldn't be blocked, then tell if it failed or not
         If it failed, update the it failing status
         If satisfy the block condition, put it into the block list
         If success, clear the failing status
         */
        if (statusCode.charAt(0) == '4' || statusCode.charAt(0) == '5') {
            if (!failureMap.containsKey(host)) {
                ArrayList<Long> failList = new ArrayList<>();
                failList.add(secondTime);
                failureMap.put(host, failList);
            } else {
                ArrayList<Long> failList = failureMap.get(host);
                while (failList.size() > 0 && failList.get(0) + 19 < secondTime) {
                    failList.remove(0);
                }
                failList.add(secondTime);
                if (failList.size() >= 3) {
                    blockMap.put(host, secondTime);
                    failureMap.remove(host);
                } else {
                    failureMap.put(host, failList);
                }
            }
        } else {
            if (failureMap.containsKey(host)) {
                failureMap.remove(host);
            }
        }

    }

    /**
     * Write the output to file
     * @param outputPath4 The output path for this feature
     * @throws IOException
     */
    public void generateResult(String outputPath4) throws IOException {
        File outputFile = new File(outputPath4);
        outputFile.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(outputFile);

        for (String result : resultList) {
            fw.write(result);
            System.out.print("Blocked: " + result);
        }
        fw.close();
    }

    /**
     * Parse the date time in String to a time stamp in second
     * @param dateTime The input date time
     * @return The time stamp after parse
     */
    private long parseTime(String dateTime) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        long timeStamp = 0;
        try {
            Date date = df.parse(dateTime);
            timeStamp = date.getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }
}
