package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static HashMap<String, Integer> hostMap = new HashMap<>();
    static HashMap<String, Long> resourceMap = new HashMap<>();
    static HashMap<String, Long> timeSumMap = new HashMap<>();
    static ArrayList<String> timeList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ReadFile("./log_input/log.txt");
        Feature1 f1 = new Feature1(hostMap);
        Feature2 f2 = new Feature2(resourceMap);
        Feature3 f3 = new Feature3(timeSumMap, timeList);
        f1.feature1();
        f2.feature2();
        f3.feature3();
    }

    public static void ReadFile(String fileName) {
        //HashMap<Long, Request> result = new HashMap<>();
        BufferedReader br;
        long index = 0;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                Request req = new Request(index, line);
                //result.put(index, req);
                String host = req.getHost();
                if (host == null) {
                    System.out.println("Null line at "+index+": "+line);
                    host = "null";
                }
                if (!hostMap.containsKey(host)) {
                    hostMap.put(host, 1);
                } else {
                    hostMap.put(host, hostMap.get(host)+1);
                }
                String resource = req.getResource();
                int bytes = req.getBytes();
                if (bytes != -1 && resource != null) {
                    if (!resourceMap.containsKey(resource)) {
                        resourceMap.put(resource, (long) bytes);
                    } else {
                        resourceMap.put(resource, resourceMap.get(resource) + bytes);
                    }
                }
                String dateTime = req.getDateTime();
                long requestCount = index + 1;
                if (!timeSumMap.containsKey(dateTime)) {
                    timeList.add(dateTime);
                }
                timeSumMap.put(dateTime, requestCount);
                index++;
                //System.out.println("Progress: "+index*100/4400644+"%");
            }
            System.out.println("# of lines: "+index);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return result;

    }
}
