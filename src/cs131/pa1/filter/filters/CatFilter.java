package cs131.pa1.filter.filters;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.concurrent.ConcurrentFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class CatFilter extends ConcurrentFilter {
    private List<Scanner> fileScanners;
    private String files;

    public CatFilter(String filenames) {
        this.files = filenames;
        List<String> fileNames = Arrays.asList(filenames.split(" "));
        fileScanners = new ArrayList<>();
        for (String fileName : fileNames) {
            try {
                File f = new File(fileName);
                Scanner scanner = new Scanner(f);
                fileScanners.add(scanner);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(Message.FILE_NOT_FOUND.with_parameter("cat " + filenames));
            }
        }
    }

    @Override
    public void run() {
        try {
            process();
        } catch (InterruptedException e) {}
    }

    @Override
    protected String processLine(String line) {
        return null;
    }

    @Override
    public void process() throws InterruptedException {
        for (Scanner s : fileScanners) {
            while(s.hasNextLine()) {
                this.output.put(s.nextLine());
            }
        }
        this.output.put(poisonPill);
    }

    @Override
    public void setNextFilter(Filter nextFilter) {
        if (this.input != null) {
            throw new IllegalStateException(Message.CANNOT_HAVE_INPUT.with_parameter("cat " + this.files));
        }
        super.setNextFilter(nextFilter);
    }
}
