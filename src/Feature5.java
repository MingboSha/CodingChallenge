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
public class Feature5 {
    private HashMap<String, Long> weekdayMap;
    private Long totalIndex;
    private final static String[] weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public Feature5() {
        this.weekdayMap = new HashMap<>();
        this.totalIndex = 0L;
    }

    public void scan(Request req) {
        String weekday = parseWeekday(req.getDateTime());
        totalIndex++;
        if (!weekdayMap.containsKey(weekday)) {
            weekdayMap.put(weekday, 1L);
        } else {
            weekdayMap.put(weekday, weekdayMap.get(weekday) + 1);
        }
    }

    public void generateResult(String outputPath) throws IOException {
        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (weekdayMap.containsKey(weekdays[i])) {
                int percent = (int) (weekdayMap.get(weekdays[i]) * 100 / totalIndex);
                resultList.add(weekdays[i] + ": " + percent +"%"+ "\n");
                //System.out.println(weekdays[i] + ": " + percent+"%");
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

    private String parseWeekday(String dateTime) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        String weekday = null;
        try {
            Date date = df.parse(dateTime);
            SimpleDateFormat df2 = new SimpleDateFormat("EEEE");
            weekday = df2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weekday;
    }
}
