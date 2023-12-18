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

    private static RAM _instance;

    private RAM() {
        available_primary_memory_area = 64;
        available_secondary_memory_area = 960;
    }

    public static RAM getInstance() {
        if (_instance == null)
            _instance = new RAM();
        return _instance;
    }

    private static boolean allocatePrimary(Proces process) {
        if (available_primary_memory_area >= process.getMemoryRequirement()) {
            available_primary_memory_area -= process.getMemoryRequirement();
            process.setAllocated(true);
            return true;
        } else
            return false;
    }

    private static boolean allocateSecondary(Proces process) {
        if (available_secondary_memory_area >= process.getMemoryRequirement()) {
            available_secondary_memory_area -= process.getMemoryRequirement();
            process.setAllocated(true);
            return true;
        } else
            return false;
    }

    private static boolean freePrimary(Proces process) {
        available_primary_memory_area += process.getMemoryRequirement();
        return true;
    }

    private static boolean freeSecondary(Proces process) {
        available_secondary_memory_area += process.getMemoryRequirement();
        return true;
    }


    public static boolean receiveMemory(Proces process) {
    	if (process.isAllocated()) {
    		return true;
    	}
        if (process.getPriority() == 0)
            return allocatePrimary(process);
        else
            return allocateSecondary(process);
    }

    public static boolean releaseMemory(Proces process) {
    	process.setAllocated(false);
        if (process.getPriority() == 0)
            return freePrimary(process);
        else
            return freeSecondary(process);
    }


    public static int getAvailableRam() {
        return available_primary_memory_area + available_secondary_memory_area;
    }


}
