package cs131.pa1.filter;

/**
 * This interface indicates that a filter will be notifying someone of
 * its completion
 * */
public interface NotifyingFilter {
    void setId(int id);
    void setManager(ProcessManager manager);
    void notifyManager();
}
