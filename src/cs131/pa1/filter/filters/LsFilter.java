package cs131.pa1.filter.filters;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.concurrent.ConcurrentFilter;
import cs131.pa1.filter.concurrent.ConcurrentREPL;

import java.io.File;

public class LsFilter extends ConcurrentFilter implements Runnable {
    private File[] all;

    @Override
    protected String processLine(String line) {
        return null;
    }

    public LsFilter() {
        this.all = new File(ConcurrentREPL.currentWorkingDirectory).listFiles();
    }

    @Override
    public void process() throws InterruptedException {
        for (int i = 0; i < this.all.length ; i++) {
            this.output.put(all[i].getName());
        }
        this.output.put(poisonPill);
    }

    @Override
    public void run()  {
        try {
          process();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setNextFilter(Filter nextFilter) {
        if (this.input != null) {
            throw new IllegalStateException(Message.CANNOT_HAVE_INPUT.with_parameter("ls"));
        }
        super.setNextFilter(nextFilter);
    }
}
