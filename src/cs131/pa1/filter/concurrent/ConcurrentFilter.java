package cs131.pa1.filter.concurrent;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa1.filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable {
	
	protected BlockingQueue<String> input;
	protected BlockingQueue<String> output;
	protected static String poisonPill = UUID.randomUUID().toString();

	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	public void process() throws InterruptedException {
        while (true) {
            try {
                String line = input.take();
                if (line.equals(poisonPill)) {
                    break;
                }
                String processedLine = processLine(line);
                if (processedLine != null) {
                    output.put(processedLine);
                }
            } catch (InterruptedException e) {}
        }
	}

    @Override
    public void run() {

    }

    //Does the real work in each filter
	protected abstract String processLine(String line);
	
}
