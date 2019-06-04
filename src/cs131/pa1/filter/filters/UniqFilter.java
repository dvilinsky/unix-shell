package cs131.pa1.filter.filters;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.concurrent.ConcurrentFilter;

import java.util.HashSet;
import java.util.Set;

public class UniqFilter extends ConcurrentFilter implements Runnable {
    private Set<String> uniques = new HashSet<>();

    @Override
    protected String processLine(String line) {
        if (!uniques.contains(line)) {
            uniques.add(line);
            return line;
        } else {
            return null;
        }
    }

    @Override
    public void process() throws InterruptedException {
        super.process();
        this.output.put(poisonPill);
    }

    @Override
    public void run() {
        try {
            process();
        } catch (InterruptedException e) {}
    }

    @Override
    public void setNextFilter(Filter nextFilter) {
        if (this.input == null) {
            throw new IllegalStateException(Message.REQUIRES_INPUT.with_parameter("uniq"));
        }
        super.setNextFilter(nextFilter);
    }
}
