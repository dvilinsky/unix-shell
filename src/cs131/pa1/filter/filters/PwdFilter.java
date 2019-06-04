package cs131.pa1.filter.filters;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.concurrent.ConcurrentFilter;
import cs131.pa1.filter.concurrent.ConcurrentREPL;

public class PwdFilter extends ConcurrentFilter implements Runnable {
    @Override
    public void process() throws InterruptedException {
        this.output.put(ConcurrentREPL.currentWorkingDirectory);
        this.output.put(poisonPill);
    }

    @Override
    protected String processLine(String line) {
        return null;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (InterruptedException e) {}
    }

    @Override
    public void setNextFilter(Filter nextFilter) {
        if (this.input != null) {
            throw new IllegalStateException(Message.CANNOT_HAVE_INPUT.with_parameter("pwd"));
        }
        super.setNextFilter(nextFilter);
    }
}
