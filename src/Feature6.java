import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by minbosha on 04/04/2017.
 */
public class Feature6 {
    private HashMap<String, Long> dayHourMap;
    private Long totalIndex;

    public Feature6() {
        this.dayHourMap = new HashMap<>();
        this.totalIndex = 0L;
    }

    public void scan(Request req) {
        String hour = parseDayHour(req.getDateTime());
        totalIndex++;
        if (!dayHourMap.containsKey(hour)) {
            dayHourMap.put(hour, 1L);
        } else {
            dayHourMap.put(hour, dayHourMap.get(hour) + 1);
        }
    }

    public void generateResult(String outputPath) throws IOException {
        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String hour = (i < 10 ? "0" : "") + i;
            String hour1 = (i+1 < 10 ? "0" : "") + (i+1);
            if (hour1.equals("24")) hour1 = "00";
            if (dayHourMap.containsKey(hour)) {
                int percent = (int) (dayHourMap.get(hour) * 100 / totalIndex);
                resultList.add(hour + ":00 - "+hour1+":00 " + percent +"%"+ "\n");
                //System.out.println(hour + ":00 - "+hour1+":00 " + percent +"%");
            } else {
                resultList.add(hour + ":00 - "+hour1+":00 " + 0 +"%"+ "\n");
                //System.out.println(hour + ":00 - "+hour1+":00 " + 0 +"%");
            }
        }
        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(outputFile);

        for (String result : resultList) {
            fw.write(result);
        }
        fw.close();

    }

    private String parseDayHour(String dateTime) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        String hour = null;
        try {
            Date date = df.parse(dateTime);
            SimpleDateFormat df2 = new SimpleDateFormat("HH");
            hour = df2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hour;
    }
}
