package cs131.pa1.filter.filters;

import cs131.pa1.filter.NotifyingFilter;
import cs131.pa1.filter.ProcessManager;
import cs131.pa1.filter.concurrent.ConcurrentFilter;
import cs131.pa1.filter.concurrent.ConcurrentREPL;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Generically handles prints to either files or standard out
 */
public class StreamFilter extends ConcurrentFilter implements NotifyingFilter {
    private PrintStream out;
    private int id = 0;
    private ProcessManager myManager;

    public StreamFilter() {
        out = new PrintStream(System.out);
    }

    public StreamFilter(String fileName) {
        File f = new File(ConcurrentREPL.currentWorkingDirectory + "\\" + fileName);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            this.out = new PrintStream(f);
        } catch (IOException e) {
            e.printStackTrace(); //Should never happen
        }
    }


    @Override
    protected String processLine(String line) {
        this.out.println(line);
        return null;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (InterruptedException e) {}
        notifyManager();
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setManager(ProcessManager manager) {
        this.myManager = manager;
    }

    @Override
    public void notifyManager() {
        this.myManager.onFinish(this.id);
    }
}
