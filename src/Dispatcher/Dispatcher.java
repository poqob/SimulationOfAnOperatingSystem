package Dispatcher;

import Devices.DeviceManager;
import Devices.EDevices;
import Dispatcher.FileOperations.FileOperations;
import Dispatcher.Ui.Ui;
import Process.Proces;
import Queues.RealTimeQueue.RealTimeQueueScheduler;
import Queues.UserJobQueue.MultilevelFeedbackQueueScheduler;
import Queues.UserJobQueue.UserJobQueue;
import Utils.Chronometer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Semaphore;

import Hardware.RAM;

public class Dispatcher {
    // User Job Queue
    UserJobQueue ujq;
    // Real Time Queue
    RealTimeQueueScheduler fcfs;
    // Define system resources
    DeviceManager deviceManager;
    RAM _ram;
    Chronometer chronometer;

    public Dispatcher() {
        ujq = new UserJobQueue();
        fcfs = new RealTimeQueueScheduler();
        chronometer = Chronometer.getInstance();
        deviceManager = DeviceManager.getInstance(); // device manager contains all static i/O devices
    }

    public void dispatchProcesses(LinkedList<Proces> processList, int numberOfProcesses) {
        System.out.println("Total number of processes: " + numberOfProcesses);
        long firstTime = -1;
        while (numberOfProcesses > _ram.getDoneProcessCount()) {
            if (chronometer.getElapsedTime() - firstTime == 1) {
                firstTime = chronometer.getElapsedTime();
                processList.forEach((proces -> {
                    // Check if process arrived
                    if (proces.getArrivalTime() == chronometer.getElapsedTime()) {
                        // Get the required devices
                        Map<EDevices, Integer> ioMap = proces.getIORequirements();
                        // Check the priority and if our system can meet the needed resources
                        if (proces.getPriority() == 0 && proces.getMemoryRequirement() <= RAM.getInstance().primary_memory_size && deviceManager.doesRecourcesMeetTheNeed(proces)) {
                            fcfs.addProcess(proces); // Add to Real Time queue
                        } else if (proces.getPriority() > 0 && proces.getMemoryRequirement() <= RAM.getInstance().secondary_memory_size && deviceManager.doesRecourcesMeetTheNeed(proces)) {
                            ujq.addProcess(proces); // Add to User Job Queue
                        } else
                            _ram.increaseDoneProcessCountRegardless();      // if it's an invalid process, increase count regardless
                    }
                }));
                //fcfs.printStatus();
                //ujq.printStatus();
                Ui.getInstance().write(fcfs.getQueueAsAList());
                Ui.getInstance().write(ujq.getQueueAsAList());
                // Trigger Schedulers
                ujq.trigger(fcfs.triggerScheduler());
            }
        }
    }


}
