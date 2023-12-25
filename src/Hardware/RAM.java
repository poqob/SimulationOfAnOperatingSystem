package Hardware;

import Process.Proces;

// we will later update RAM class, for now we only simulate ram by whole numbers.
// TODO: add  a list of running process? may list of on memory processes? @Mustafa
public class RAM extends AHardware {


    // available memories.
    // if incoming process's priority level is 0 then allocate primary memory area.
    private static int available_primary_memory_area;
    // if incoming process's priority level isn't 0 then allocate secondary memory area.
    private static int available_secondary_memory_area;
    // RAM instance
    private static RAM _instance;

    public final int primary_memory_size = 64;
    public final int secondary_memory_size = 960;

    // private constructor
    private RAM() {
        available_primary_memory_area = primary_memory_size;
        available_secondary_memory_area = secondary_memory_size;
    }

    public static RAM getInstance() {
        if (_instance == null)
            _instance = new RAM();
        return _instance;
    }

    private boolean allocatePrimary(Proces process) {
        if (available_primary_memory_area >= process.getMemoryRequirement()) {
            available_primary_memory_area -= process.getMemoryRequirement();
            process.setAllocated(true);
            return true;
        } else
            return false;
    }

    private boolean allocateSecondary(Proces process) {
        if (available_secondary_memory_area >= process.getMemoryRequirement()) {
            available_secondary_memory_area -= process.getMemoryRequirement();
            process.setAllocated(true);
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
        if (process.isAllocated()) {
            return true;
        }
        if (process.getPriority() == 0)
            return allocatePrimary(process);
        else
            return allocateSecondary(process);
    }

    public boolean releaseMemory(Proces process) {
        process.setAllocated(false);
        if (process.getPriority() == 0)
            return freePrimary(process);
        else
            return freeSecondary(process);
    }


    public int getAvailableRam() {
        return available_primary_memory_area + available_secondary_memory_area;
    }

    public int getTotalRam() {
        return primary_memory_size + secondary_memory_size;
    }
}
