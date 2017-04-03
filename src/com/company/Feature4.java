package com.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by minbosha on 01/04/2017.
 */
public class Feature4 {

    HashMap<String, ArrayList<Long>> failureMap;
    HashMap<String, Long> blockMap;
    ArrayList<String> resultList;
    public Feature4() {
        this.failureMap = new HashMap<>();
        this.blockMap = new HashMap<>();
        this.resultList = new ArrayList<>();
    }
    public void scan(Request request) {
        String statusCode = request.getStatusCode();
        String host = request.getHost();
        Long secondTime = parseTime(request.getDateTime());
        if (statusCode.charAt(0) == 4 || statusCode.charAt(0) == 5) {
            if (!failureMap.containsKey(host)) {
                ArrayList<Long> failList = new ArrayList<>();
                failList.add(secondTime);
                failureMap.put(host, failList);
            } else {
                ArrayList<Long> failList = failureMap.get(host);
                while (failList.size() > 0 && failList.get(0) + 20 < secondTime) {
                    failList.remove(0);
                }
                failList.add(secondTime);
                if (failList.size() >= 3) {
                    blockMap.put(host, secondTime);
                }
            }
        } else {
            if (failureMap.containsKey(host)) {
                failureMap.remove(host);
            }
        }


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
}
