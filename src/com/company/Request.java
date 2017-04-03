package com.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.StringTokenizer;

/**
 * Created by minbosha on 01/04/2017.
 */
public class Request {
    long id;
    String requestLine;
    String host;
    String dateTime;
    //long timeStamp;
    String requestBody;
    String resource;
    int statusCode;
    int bytes;

    public Request(long id, String requestLine) {
        this.id = id;
        this.requestLine = requestLine;
        parseLine();
    }

    public void parseLine() {
        this.requestBody = "\""+this.requestLine.split("\"")[1]+"\"";
        //System.out.println("Line: "+this.requestLine);
        if (this.requestBody.split(" ").length > 1) {
            //System.out.println(this.requestBody.split(" ").length);
            this.resource = this.requestBody.split(" ")[1];
        } else {
            this.resource = null;
        }
        //System.out.println("Resource: "+this.resource);
        //System.out.println("RequestBody: "+this.requestBody);
        String requestLineWithoutBody = this.requestLine.replace(" "+requestBody,"");
        //System.out.println("RequestLineWithoutBody: "+requestLineWithoutBody);
        String pattern = " []";
        StringTokenizer tokenizer = new StringTokenizer(requestLineWithoutBody,pattern);


        if(tokenizer.countTokens()==7){
            host = tokenizer.nextToken();
            tokenizer.nextToken();
            tokenizer.nextToken();
            dateTime = tokenizer.nextToken();
            dateTime += " " + tokenizer.nextToken();
            //this.timeStamp = parseTime(dateTime);
            statusCode = Integer.parseInt(tokenizer.nextToken());
            String byte_size = tokenizer.nextToken();
            if(!byte_size.contains("-"))
                bytes = Integer.parseInt(byte_size);
            else
                bytes = -1;
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

    public long getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getResource() {
        return resource;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getBytes() {
        return bytes;
    }

}
