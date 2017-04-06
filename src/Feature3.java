import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by minbosha on 01/04/2017.
 */

public class Feature3 {

    private HashMap <String, Long> timeSumMap;
    private ArrayList <String> timeList;
    private HashMap <String, Long> windowSumMap = new HashMap <> ( );

    public Feature3 ( ) {
        this.timeSumMap = new HashMap <> ( );
        this.timeList = new ArrayList <> ( );
    }

    /**
     * Scan one line of request, and keep the time stamp and cumulative sum in HashMap
     * and also keep all the time stamps with request in ArrayList as index for the HashMap
     * @param req object for a request line
     */
    public void scan(Request req) {
        String dateTime = req.getDateTime();
        long requestCount = req.getIndex() + 1;
        if (!timeSumMap.containsKey(dateTime)) {
            timeList.add(dateTime);
        }
        timeSumMap.put(dateTime, requestCount);
    }

    /**
     * Move the window from beginning to the end, move the two pointers with the window and in the mean time put the
     * window sums in a minHeap with size 10.
     * Write the output to file
     * @param outputPath3 The output path for this feature
     * @throws IOException
     */
    public void generateResult(String outputPath3) throws IOException {
        int p1 = 0;
        int p2 = 0;
        long windowSum = 0;
        long startTime = parseTime(timeList.get(0));
        long endTime = parseTime(timeList.get(timeList.size() - 1));

        PriorityQueue<String> pq = new PriorityQueue<>(this::compareDateTime);

        for (long pTime = startTime; pTime <= endTime; pTime++) {
            p1 = moveP1(pTime, p1);
            p2 = moveP2(pTime, p2);
            windowSum = windowSum(p1, p2);
            String dateTime = revertTime(pTime);
            windowSumMap.put(dateTime, windowSum);
            if (pq.size() == 10) {
                if (compareDateTime(dateTime, pq.peek()) > 0) {
                    pq.poll();
                    pq.offer(dateTime);
                }
            } else {
                pq.offer(dateTime);
            }
        }

        ArrayList<String> resultList = new ArrayList<>();

        while (!pq.isEmpty()) {
            String dateTime = pq.poll();
            //System.out.println("DateTime: "+dateTime+", Requests: "+windowSumMap.get(dateTime));
            String resultLine = dateTime+","+windowSumMap.get(dateTime)+"\n";
            resultList.add(resultLine);
        }

        File outputFile = new File(outputPath3);
        outputFile.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(outputFile);

        for (int i = resultList.size() - 1; i >= 0; i--) {
            fw.write(resultList.get(i));
        }
        fw.close();

    }

    // 00 01 02 03 04 05 07 09 13
    // 2  2  1  3  0  1  2  3  1
    // 2  4  5  8  8  9  11 14 15

    /**
     * Based on the current window location, move pointer 1
     * @param pTime Start time for current window
     * @param p1 Current pointer 1 location
     * @return p1 location after move
     */
    private int moveP1(long pTime, int p1) {
        while (parseTime(timeList.get(p1)) < pTime) {
            p1++;
        }
        return p1;
    }

    /**
     * Based on the current window location, move pointer 2
     * @param pTime Start time for current window
     * @param p2 Current pointer 2 location
     * @return p2 location after move
     */
    private int moveP2(long pTime, int p2) {
        long p2Time = pTime + 3599;
        if (p2Time >= parseTime(timeList.get(timeList.size() - 1))) {
            return timeList.size() - 1;
        }
        while (parseTime(timeList.get(p2)) < p2Time) {
            p2++;
        }
        return p2;
    }

    /**
     * Based on the location of two pointers, calculate the sum in the window by subtraction
     * @param p1 Location of pointer 1
     * @param p2 Location of pointer 2
     * @return Sum in the window
     */
    private long windowSum(int p1, int p2) {
        long windowSum;
        if (p1 > 0) {
            windowSum = timeSumMap.get(timeList.get(p2)) - timeSumMap.get(timeList.get(p1 - 1));
        } else {
            windowSum = timeSumMap.get(timeList.get(p2));
        }
        return windowSum;
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

    /**
     * Revert the second time stamp back to date time in String
     * @param secondTime The input second time
     * @return The date time after revert
     */
    private String revertTime(long secondTime) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        Date date = new Date(secondTime*1000);
        return df.format(date);
    }

    /**
     * A comparator function to used by minHeap, the window with less sum has higher priority
     * If two window have same sum than compare the lexicographical order
     * @param dt1 The start location for the first window
     * @param dt2 The start location for the second window
     * @return The priority
     */
    private int compareDateTime (String dt1, String dt2) {
        if (windowSumMap.get(dt1) > windowSumMap.get(dt2)) {
            return 1;
        } else if (windowSumMap.get(dt1).equals(windowSumMap.get(dt2))) {
            if (dt1.compareTo(dt2) > 0) {
                return -1;
            } else if (dt1.compareTo(dt2) == 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return -1;
        }
    }

}
