import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Semaphore;

import Utils.Chronometer;
import Process.Proces;
import Devices.*;
import Dispatcher.FileOperations.FileOperations;
import Queues.UserJobQueue.*;
import Queues.RealTimeQueue.*;
import Hardware.*;

/// Tests will be written here. @everyone
public class Test {
    public static void main(String[] args) {
        // tests for devices.
        FileOperations fileOperations = FileOperations.getInstance();
        fileOperations.readFile();
        LinkedList<Proces> a = fileOperations.getParsedProcesses();
        a.forEach((proces -> System.out.println(proces.getArrivalTime() + " (pid: " + proces.getPid() + ")")));
        // Scheduler Test
        UserJobQueue ujq = new UserJobQueue();
        RealTimeQueueScheduler fcfs = new RealTimeQueueScheduler();

        Chronometer chronometer = Chronometer.getInstance();
        int numberOfProcesses = fileOperations.numberOfProcesses();
        System.out.println("Total number of processes: " + numberOfProcesses);

        chronometer.start();
        long firstTime = -1;
        while (numberOfProcesses > FileOperations.doneProcessCount) {
            if (chronometer.getElapsedTime() - firstTime == 1) {
                firstTime = chronometer.getElapsedTime();
                a.forEach((proces -> {
                    // Check if process arrived
                    if (proces.getArrivalTime() == chronometer.getElapsedTime()) {
                    	Map<EDevices, Integer> ioMap = proces.getIORequirements();
                        if (proces.getPriority() == 0 && proces.getMemoryRequirement() <= RAM.getInstance().primary_memory_size &&
                        		ioMap.get(EDevices.Printer) <= 2 &&
                        		ioMap.get(EDevices.Browser) <= 1 &&
                        		ioMap.get(EDevices.Router) <= 1 &&
                        		ioMap.get(EDevices.CD) <= 2) {
                            fcfs.addProcess(proces); // Add to Real Time queue
                        } else if (proces.getPriority() > 0 && proces.getMemoryRequirement() <= RAM.getInstance().secondary_memory_size &&
                        		ioMap.get(EDevices.Printer) <= 2 &&
                        		ioMap.get(EDevices.Browser) <= 1 &&
                        		ioMap.get(EDevices.Router) <= 1 &&
                        		ioMap.get(EDevices.CD) <= 2) {
                        	ujq.addProcess(proces);// Add to User Job Queue queue
                        } else
                            FileOperations.doneProcessCount++;      // if it's an invalid process, increase count regardless
                    }
                }));
                fcfs.printStatus();
                ujq.printStatus();
                // Trigger Schedulers
                ujq.trigger(fcfs.triggerScheduler());
            }
        }
        System.out.println("No process left..");
    }
}
