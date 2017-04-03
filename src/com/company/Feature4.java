package com.company;

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
        //System.out.println("statusCode: "+statusCode);
        String host = request.getHost();
        Long secondTime = parseTime(request.getDateTime());
        if (blockMap.containsKey(host)) {
            if (blockMap.get(host) + 299 > secondTime) {
                resultList.add(request.requestLine+"\n");
                return;
            } else {
                blockMap.remove(host);
            }
        }

        if (statusCode.charAt(0) == '4' || statusCode.charAt(0) == '5') {
            if (!failureMap.containsKey(host)) {
                ArrayList<Long> failList = new ArrayList<>();
                failList.add(secondTime);
                //System.out.println("failList: "+failList);
                failureMap.put(host, failList);
            } else {
                ArrayList<Long> failList = failureMap.get(host);
                while (failList.size() > 0 && failList.get(0) + 20 < secondTime) {
                    failList.remove(0);
                }
                failList.add(secondTime);
                //System.out.println("failList: "+failList);
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

    public void generateResult() throws IOException {
        File outputFile = new File("./log_output/blocked.txt");
        outputFile.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(outputFile);

        for (int i = 0; i < resultList.size(); i++) {
            fw.write(resultList.get(i));
            System.out.print("Blocked: "+resultList.get(i));
        }
        fw.close();
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
