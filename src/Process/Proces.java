package Process;

import java.util.Random;

// i made copy paste from sevda.
public class Proces {
    // fields
    int pid;
    int arrivalTime;
    int priority;
    int executionTime;
    final int memoryRequirement;
    final int printers;
    final int scanners;
    final int modems;
    final int cdDrives;
    boolean allocated;

    // process status
    EProcessStatus status;


    public int getMemoryRequirement() {
        return memoryRequirement;
    }
    
    public int getExecutionTime() {
        return executionTime;
    }

    public Proces(int pid, int arrivalTime, int priority, int executionTime, int memoryRequirement,
                  int printers, int scanners, int modems, int cdDrives) {
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.executionTime = executionTime;
        this.memoryRequirement = memoryRequirement;
        this.printers = printers;
        this.scanners = scanners;
        this.modems = modems;
        this.cdDrives = cdDrives;
        this.allocated = false;
        this.pid = pid;
    }

    public void interrupt() {
        // implement process works.
        this.status = EProcessStatus.interrupted;
    }

    public void ready() {
        // implement process works.
        this.status = EProcessStatus.ready;
    }

    public void done() {
        // implement process works.
        this.status = EProcessStatus.done;
    }

    public void run() {
        // implement process works.
        this.status = EProcessStatus.running;
    }

    public void execute () {
    	executionTime--;
    }
    
    public EProcessStatus getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }
    
    public int getArrivalTime() {
        return arrivalTime;
    }
    
    public int getPid() {
        return pid;
    }
    
    public boolean isAllocated () {
    	return allocated;
    }
    
    public void setAllocated (boolean value) {
    	allocated = value;
    }
    
    public boolean claimResource (boolean test) {
    	// can be removed later
    	if (test) {
    		return true;
    	}
    	if (priority == 0) {
    		// accrue resources
    		return true;
    	}
  	  	/* if (neededDevices.isAvailable) {
  	  		return true;
  	  	} */
    	return false;
    }
}