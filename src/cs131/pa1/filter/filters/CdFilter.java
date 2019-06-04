package cs131.pa1.filter.filters;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.concurrent.ConcurrentFilter;
import cs131.pa1.filter.concurrent.ConcurrentREPL;

import java.io.File;

public class CdFilter extends ConcurrentFilter {
    private String pathChanger;
    private String attemptedPath;

    public CdFilter(String path) {
        this.pathChanger = path;
        buildPathAttempt();
        File f = new File(attemptedPath);
        if (!(f.exists() && f.isDirectory())) {
            throw new RuntimeException(Message.DIRECTORY_NOT_FOUND.with_parameter("cd " + pathChanger));
        }
    }

    @Override
    protected String processLine(String line) {
        return null;
    }

    @Override
    public void process() {
        ConcurrentREPL.currentWorkingDirectory = attemptedPath;
        try {
            this.output.put(poisonPill);
        } catch (InterruptedException e) {}
    }

    public void buildPathAttempt() {
        this.attemptedPath = ConcurrentREPL.currentWorkingDirectory;
        if (pathChanger.equals("..")) {
            attemptedPath = attemptedPath.substring(0, attemptedPath.lastIndexOf(FILE_SEPARATOR));
        } else if (pathChanger.equals(".")) {
            //do nothing;
        } else {
            attemptedPath += FILE_SEPARATOR + pathChanger;
        }
    }

    @Override
    public void setNextFilter(Filter nextFilter) {
        if (this.input != null) {
            throw new IllegalStateException(Message.CANNOT_HAVE_INPUT.with_parameter("cd " + pathChanger));
        }
        if (!(nextFilter instanceof StreamFilter)) {
            throw new RuntimeException(Message.CANNOT_HAVE_OUTPUT.with_parameter("cd " + pathChanger));
        } else {
            super.setNextFilter(nextFilter);
        }
    }

    @Override
    public void run() {
        process();
    }
}
