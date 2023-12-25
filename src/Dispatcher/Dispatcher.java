package Dispatcher;

import Devices.DeviceManager;
import Process.Proces;
import Queues.RealTimeQueue.RealTimeQueueScheduler;
import Queues.UserJobQueue.MultilevelFeedbackQueueScheduler;
import Utils.Chronometer;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import Hardware.RAM;

public class Dispatcher {
    MultilevelFeedbackQueueScheduler mfqs;
    RealTimeQueueScheduler fcfs;
    // Define system resources
    DeviceManager deviceManager;
    RAM _ram;
    Chronometer chronometer;

    public Dispatcher() {
        mfqs = new MultilevelFeedbackQueueScheduler();
        fcfs = new RealTimeQueueScheduler();
        chronometer = Chronometer.getInstance();
        deviceManager = DeviceManager.getInstance(); // device manager contains all static i/O devices
    }

    public void dispatchProcesses(LinkedList<Proces> processList) {
        Thread triggerFCFS;
        Thread triggerMFQS;

        // Semaphore for concurrency
        Semaphore sem = new Semaphore(1);
        long firstTime = -1;
        while (true) {
            if (chronometer.getElapsedTime() - firstTime == 1) {
                firstTime = chronometer.getElapsedTime();
                processList.forEach((proces -> {
                    // Check if process arrived
                    if (proces.getArrivalTime() == chronometer.getElapsedTime()) {
                        if (proces.getPriority() == 0 && proces.getMemoryRequirement() <= _ram.primary_memory_size) {
                            fcfs.addProcess(proces); // Add to Real Time queue
                        } else if (proces.getPriority() > 0 && proces.getMemoryRequirement() <= _ram.secondary_memory_size) {
                            mfqs.addProcess(proces, proces.getPriority() - 1);// Add to MFQS queue
                        }
                    }
                }));
                fcfs.printStatus();
                mfqs.printStatus();
                // Lock the semaphore
                try {
                    sem.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Trigger Real Time Scheduler in a new thread
                triggerFCFS = new Thread() {
                    @Override
                    public void run() {
                        fcfs.triggerScheduler(sem);
                    }
                };
                triggerFCFS.start();
                // Trigger MFQS in a new thread
                triggerMFQS = new Thread() {
                    @Override
                    public void run() {
                        mfqs.triggerScheduler(sem);
                    }
                };
                triggerMFQS.start();
                // Wait for threads to finish
                try {
                    triggerFCFS.join();
                    triggerMFQS.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
