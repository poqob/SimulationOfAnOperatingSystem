package Process;

import java.net.InterfaceAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import Devices.EDevices;

public class Proces {
    // degiskenler
    int pid;
    int arrivalTime;
    int priority;
    int executionTime;
    final int memoryRequirement;
    final int printers;
    final int scanners;
    final int modems;
    final int cdDrives;

    // process durumu
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
        this.pid = pid;
        this.status = EProcessStatus.none;
    }

    public void interrupt() {
        this.status = EProcessStatus.interrupted;
        //System.out.println("(" + pid + ") is interrupted!");
    }

    public void ready() {
        this.status = EProcessStatus.ready;
        //System.out.println("(" + pid + ") is ready!");
    }

    public void done() {
        this.status = EProcessStatus.done;
        //System.out.println("(" + pid + ") is done!");
    }

    public void run() {
        this.status = EProcessStatus.running;
        //System.out.println("(" + pid + ") is running!");
    }

    public void execute() {
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

    // i/o ihtiyaclarini bir map veri yapisinda getir
    public Map<EDevices, Integer> getIORequirements() {
        Map<EDevices, Integer> map = new HashMap<EDevices, Integer>();
        map.put(EDevices.Browser, scanners);
        map.put(EDevices.CD, cdDrives);
        map.put(EDevices.Printer, printers);
        map.put(EDevices.Router, modems);
        return map;
    }


    @Override
    public String toString() {
        return "Proces{" +
                "pid=" + pid +
                ", arrivalTime=" + arrivalTime +
                ", priority=" + priority +
                ", executionTime=" + executionTime +
                ", memoryRequirement=" + memoryRequirement +
                ", printers=" + printers +
                ", scanners=" + scanners +
                ", modems=" + modems +
                ", cdDrives=" + cdDrives +
                ", status=" + status.name() +
                '}';
    }
}