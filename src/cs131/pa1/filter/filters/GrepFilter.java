package cs131.pa1.filter.filters;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.concurrent.ConcurrentFilter;

public class GrepFilter extends ConcurrentFilter {
    private String searchString;

    public GrepFilter(String args) {
        this.searchString = args.trim();
    }

    @Override
    protected String processLine(String line) {
        return line.contains(this.searchString) ? line : null;
    }

    @Override
    public void process()  {
        try {
            super.process();
            this.output.put(poisonPill);
        } catch (InterruptedException e) {}
    }

    @Override
    public void run() {
        process();
    }

    @Override
    public void setNextFilter(Filter nextFilter) {
        if (this.input == null) {
            throw new IllegalStateException(Message.REQUIRES_INPUT.with_parameter("grep " + searchString));
        }
        super.setNextFilter(nextFilter);
    }
}
