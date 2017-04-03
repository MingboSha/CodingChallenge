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

    class FailureTrack {
        int recentFailConut;
        long firstFailTime;

        public int getRecentFailConut() {
            return recentFailConut;
        }

        public void setRecentFailConut(int recentFailConut) {
            this.recentFailConut = recentFailConut;
        }

        public long getFirstFailTime() {
            return firstFailTime;
        }

        public void setFirstFailTime(long firstFailTime) {
            this.firstFailTime = firstFailTime;
        }
    }

    class BlockTrack {

    }

    HashMap<String, FailureTrack> failureMap;
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
        if (statusCode.charAt(0) == 4 || statusCode.charAt(0) == 5) {
            if (!failureMap.containsKey(host)) {
                FailureTrack failure = new FailureTrack();
                failure.setFirstFailTime(parseTime(request.getDateTime()));
                failure.setRecentFailConut(1);
                failureMap.put(host, failure);
            } else {
                int recentFailCount = failureMap.get(host).getRecentFailConut();
                if (recentFailCount < 2) {
                    failureMap.get(host).setRecentFailConut();
                }
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
