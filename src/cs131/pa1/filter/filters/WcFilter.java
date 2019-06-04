package cs131.pa1.filter.filters;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.concurrent.ConcurrentFilter;

public class WcFilter extends ConcurrentFilter implements Runnable {
    private int words;
    private int lines;
    private int characters;

    public WcFilter() {
        this.words = this.lines = this.characters = 0;
    }

    @Override
    protected String processLine(String line) {
        this.words += line.split(" ").length;
        this.lines++;
        this.characters += line.length();
        return null; //ugh.....
    }

    @Override
    public void process() throws InterruptedException {
        super.process();
        this.output.put(this.lines + " " + this.words + " " + this.characters);
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
            throw new IllegalStateException(Message.REQUIRES_INPUT.with_parameter("wc"));
        }
        super.setNextFilter(nextFilter);
    }
}
