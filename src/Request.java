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
        //Split the request line by space and get everything except request body
        String[] splitLine = this.requestLine.split(" ");
        this.host = splitLine[0];
        String dateString = splitLine[3]+" "+splitLine[4];
        this.dateTime = dateString.substring(1, dateString.length() - 1);
        String byteString = splitLine[splitLine.length - 1];
        this.bytes = byteString.contains("-") ? -1 : Integer.parseInt(byteString);
        this.statusCode = splitLine[splitLine.length - 2];
        //Get requestBody by remove everything else
        this.requestBody = this.requestLine.replace(host+" - - "+dateString+" ", "")
                .replace(" "+this.statusCode+" "+byteString, "");
        if (this.requestBody.split(" ").length > 1) {
            this.resource = this.requestBody.split(" ")[1];
        } else {
            this.resource = null;
        }

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
