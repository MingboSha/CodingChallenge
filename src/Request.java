import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by minbosha on 01/04/2017.
 */
public class Request {
    private long index;
    String requestLine;
    private String host;
    private String dateTime;
    private String requestBody;
    private String resource;
    private String statusCode;
    private int bytes;

    public Request(long index, String requestLine) {
        this.index = index;
        this.requestLine = requestLine;
        parseLine();
    }

    public void parseLine() {
        String[] splitLine = this.requestLine.split(" ");
        this.host = splitLine[0];
        String dateString = splitLine[3]+" "+splitLine[4];
        //System.out.println(dateString);
        this.dateTime = dateString.substring(1, dateString.length() - 1);
        //System.out.println(dateTime);
        String byteString = splitLine[splitLine.length - 1];
        this.bytes = byteString.contains("-") ? -1 : Integer.parseInt(byteString);
        this.statusCode = splitLine[splitLine.length - 2];
        this.requestBody = this.requestLine.replace(host+" - - "+dateString+" ", "")
                .replace(" "+this.statusCode+" "+byteString, "");
        if (this.requestBody.split(" ").length > 1) {
            //System.out.println(this.requestBody.split(" ").length);
            this.resource = this.requestBody.split(" ")[1];
        } else {
            this.resource = null;
        }

    }

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

    public long getIndex() {
        return index;
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

    public String getStatusCode() {
        return statusCode;
    }

    public int getBytes() {
        return bytes;
    }

}
