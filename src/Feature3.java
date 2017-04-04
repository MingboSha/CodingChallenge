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
    private HashMap<String, Long> timeSumMap;
    private ArrayList<String> timeList;
    private HashMap<String, Long> windowSumMap = new HashMap<>();
    private long startTime;
    private long endTime;

    public Feature3(HashMap<String, Long> timeSumMap, ArrayList<String> timeList) {
        this.timeSumMap = timeSumMap;
        this.timeList = timeList;
    }

    public void feature3(String outputPath3) throws IOException {
        int p1 = 0;
        int p2 = 0;
        this.startTime = parseTime(timeList.get(0));
        //System.out.println("StartTime: "+startTime);
        this.endTime = parseTime(timeList.get(timeList.size() - 1));
        //System.out.println("EndTime: "+endTime);

        PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return compareDateTime(o1, o2);
            }
        });

        for (long pTime = startTime; pTime <= endTime; pTime++) {
            p1 = moveP1(pTime, p1);
            p2 = moveP2(pTime, p2);
            long windowSum = windowSum(p1, p2);
            String dateTime = revertTime(pTime);
            windowSumMap.put(dateTime, windowSum);
            //System.out.println("Put in pq: "+dateTime+"  "+windowSum);
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
            System.out.println("DateTime: "+dateTime+", Requests: "+windowSumMap.get(dateTime));
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

    private int moveP1(long pTime, int p1) {
        while (parseTime(timeList.get(p1)) < pTime) {
            p1++;
        }
        return p1;
    }

    private int moveP2(long pTime, int p2) {
        long p2Time = pTime + 3659;
        if (p2Time >= parseTime(timeList.get(timeList.size() - 1))) {
            return timeList.size() - 1;
        }
        while (parseTime(timeList.get(p2)) < p2Time) {
            p2++;
            //System.out.println("p2: "+p2+"/"+(timeList.size()-1));
        }
        return p2;
    }

    private long windowSum(int p1, int p2) {
        long windowSum = 0;
        if (p1 > 0) {
            windowSum = timeSumMap.get(timeList.get(p2)) - timeSumMap.get(timeList.get(p1 - 1));
        } else {
            windowSum = timeSumMap.get(timeList.get(p2));
        }
        return windowSum;
    }

    private long parseTime(String dateTime) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        long timeStamp = 0;
        try {
            Date date = df.parse(dateTime);
            //System.out.println(date);
            timeStamp = date.getTime()/1000;
            //System.out.println(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    private String revertTime(long secondTime) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        Date date = new Date(secondTime*1000);
        return df.format(date);
    }

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
