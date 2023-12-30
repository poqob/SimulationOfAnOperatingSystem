import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Semaphore;

import Dispatcher.Ui.Ui;
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
        while (numberOfProcesses > RAM.getInstance().getDoneProcessCount()) {
            if (chronometer.getElapsedTime() - firstTime == 1) {
                firstTime = chronometer.getElapsedTime();
                a.forEach((proces -> {
                    // Check if process arrived
                    if (proces.getArrivalTime() == chronometer.getElapsedTime()) {
                        if (proces.getPriority() == 0 && proces.getMemoryRequirement() <= RAM.getInstance().primary_memory_size && DeviceManager.getInstance().isThereEnoughDeviceSource(proces)) {
                            fcfs.addProcess(proces); // Add to Real Time queue
                        } else if (proces.getPriority() > 0 && proces.getMemoryRequirement() <= RAM.getInstance().secondary_memory_size && DeviceManager.getInstance().isThereEnoughDeviceSource(proces)) {
                            ujq.addProcess(proces);// Add to User Job Queue queue
                        } else
                            RAM.getInstance().increaseDoneProcessCountRegardless();     // if it's an invalid process, increase count regardless
                    }
                }));
                Ui.getInstance().write(fcfs.getQueueAsAList());
                Ui.getInstance().write(ujq.getQueueAsAList());

                // Trigger Schedulers
                ujq.trigger(fcfs.triggerScheduler());
            }
        }
        System.out.println("No process left..");
    }
}
