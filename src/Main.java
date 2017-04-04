import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    private static Feature1 f1;
    private static Feature2 f2;
    private static Feature3 f3;
    private static Feature4 f4;
    private static Feature5 f5;

    public static void main(String[] args) throws IOException {
        String inputPath;
        String outputPath1;
        String outputPath2;
        String outputPath3;
        String outputPath4;

        if (args.length == 5) {
            inputPath = args[0];
            outputPath1 = args[1];
            outputPath2 = args[2];
            outputPath3 = args[3];
            outputPath4 = args[4];
        } else {
            throw new IllegalArgumentException("No enough input arguments");
        }
        f1 = new Feature1();
        f2 = new Feature2();
        f3 = new Feature3();
        f4 = new Feature4();
        f5 = new Feature5();

        ReadFile(inputPath);
        f1.generateResult(outputPath1);
        f2.generateResult(outputPath2);
        f3.generateResult(outputPath3);
        f4.generateResult(outputPath4);
        f5.generateResult("./log_output/weekdays.txt");
    }

    private static void ReadFile(String fileName) {

        BufferedReader br;
        long index = 0;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                Request req = new Request(index, line);
                //result.put(index, req);
                f1.scan(req);
                f2.scan(req);
                f3.scan(req);
                f4.scan(req);
                f5.scan(req);

                index++;
                //System.out.println("Progress: "+index*100/4400644+"%");
            }
            System.out.println("# of lines: "+index);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
