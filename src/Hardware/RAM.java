package Hardware;

import Process.Proces;

// we will later update RAM class, for now we only simulate ram by whole numbers.
// TODO: add  a list of running process? may list of on memory processes? @Mustafa
public class RAM extends AHardware {


    // belleklerimiz
    // onceligi 0 olan bir proses ise primary memory'den alan ayir
    private static int available_primary_memory_area;
    // eger 0 degilse secondary memory'den alan ayir
    private static int available_secondary_memory_area;
    // RAM instance
    private static RAM _instance;

    public final int primary_memory_size = 64;
    public final int secondary_memory_size = 960;

    public static int doneProcessCount;

    // private constructor
    private RAM() {
        available_primary_memory_area = primary_memory_size;
        available_secondary_memory_area = secondary_memory_size;
        doneProcessCount = 0;
    }

    public static RAM getInstance() {
        if (_instance == null)
            _instance = new RAM();
        return _instance;
    }

    private boolean allocatePrimary(Proces process) {
        if (available_primary_memory_area >= process.getMemoryRequirement()) {
            available_primary_memory_area -= process.getMemoryRequirement();
            return true;
        } else
            return false;
    }

    private boolean allocateSecondary(Proces process) {
        if (available_secondary_memory_area >= process.getMemoryRequirement()) {
            available_secondary_memory_area -= process.getMemoryRequirement();
            return true;
        } else
            return false;
    }

    private boolean freePrimary(Proces process) {
        available_primary_memory_area += process.getMemoryRequirement();
        return true;
    }

    private boolean freeSecondary(Proces process) {
        available_secondary_memory_area += process.getMemoryRequirement();
        return true;
    }


    public boolean receiveMemory(Proces process) {
        if (process.getPriority() == 0)
            return allocatePrimary(process);
        else
            return allocateSecondary(process);
    }

    public boolean releaseMemory(Proces process) {
        doneProcessCount++;
        if (process.getPriority() == 0)
            return freePrimary(process);
        else
            return freeSecondary(process);
    }

    public int getDoneProcessCount() {
        return doneProcessCount;
    }

    public void increaseDoneProcessCountRegardless() {
        doneProcessCount++;
    }


    public int getAvailableRam() {
        return available_primary_memory_area + available_secondary_memory_area;
    }

    public int getTotalRam() {
        return primary_memory_size + secondary_memory_size;
    }
}
