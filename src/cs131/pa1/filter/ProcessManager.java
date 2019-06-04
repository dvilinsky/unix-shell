package cs131.pa1.filter;

import cs131.pa1.filter.concurrent.ConcurrentCommandBuilder;
import cs131.pa1.filter.concurrent.ConcurrentFilter;
import cs131.pa1.filter.exceptions.InvalidCommandException;
import cs131.pa1.filter.exceptions.InvalidParameterException;
import cs131.pa1.filter.filters.StreamFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This class handles the storage and printing of background jobs, as well as of
 * executing threads
 */
public class ProcessManager {
    private static ConcurrentMap<Integer, String> processes = new ConcurrentHashMap<>();
    private static int pid = 0;
    private static final String BACKGROUND_JOB_INDICATOR = "&";
    private static final String REPL_JOBS = "repl_jobs";

    private String currentCommand;
    private boolean isBackground;

    public ProcessManager(String command) {
        this.isBackground = command.endsWith(BACKGROUND_JOB_INDICATOR);
        this.currentCommand = command.replaceAll(BACKGROUND_JOB_INDICATOR, "");
    }

    private void printBackgroundProcesses() {
        for (Integer pid : processes.keySet()) {
            System.out.println("\t" + pid + ". " + processes.get(pid) + "&");
        }
    }

    private List<ConcurrentFilter> makeFilters() throws InvalidCommandException,
        InvalidParameterException, RuntimeException {
        return ConcurrentCommandBuilder.createFiltersFromCommand(currentCommand);
    }

    public void executeCommand() throws InvalidCommandException, InvalidParameterException,
        RuntimeException {
        if (currentCommand.equals(REPL_JOBS)) {
            printBackgroundProcesses();
            return;
        }
        List<ConcurrentFilter> filters = makeFilters();
        if (isBackground) {
            processes.put(++pid, currentCommand);
        }
        setFinishedListener(filters);

        List<Thread> filterThreads = new ArrayList<>();
        for (ConcurrentFilter filter : filters) {
            filterThreads.add(new Thread(filter));
        }
        startThreads(filterThreads);
    }


    private void setFinishedListener(List<ConcurrentFilter> filters) {
        //Goodness gracious
        StreamFilter sFilter = (StreamFilter) filters.get(filters.size() - 1);
        sFilter.setId(pid);
        sFilter.setManager(this);
    }

    private void startThreads(List<Thread> filterThreads) {
        for (int i = 0; i < filterThreads.size(); i ++) {
            Thread t = filterThreads.get(i);
            try {
                t.start();
                if (i == filterThreads.size() - 1 && !isBackground) {
                    t.join();
                }
            } catch (IllegalStateException|InterruptedException e) {
                System.out.print(e.getMessage());
            }
        }
    }

    public void onFinish(int processId) {
        processes.remove(processId);
    }
}
