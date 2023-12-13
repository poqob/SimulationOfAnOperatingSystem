package Process;

// i made copy paste from sevda.
public class Process {
    // fields
    int arrivalTime;
    int priority;
    int executionTime;
    int memoryRequirement;
    int printers;
    int scanners;
    int modems;
    int cdDrives;

    // process status
    EProcessStatus status;


    public int getMemoryRequirement() {
        return memoryRequirement;
    }

    public Process(int arrivalTime, int priority, int executionTime, int memoryRequirement,
                   int printers, int scanners, int modems, int cdDrives) {
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.executionTime = executionTime;
        this.memoryRequirement = memoryRequirement;
        this.printers = printers;
        this.scanners = scanners;
        this.modems = modems;
        this.cdDrives = cdDrives;
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

    public EProcessStatus getStatus() {
        return status;
    }


}