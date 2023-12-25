package Dispatcher;

import Devices.Browser;
import Devices.CD;
import Devices.Printer;
import Devices.Router;
import Process.Proces;
import Queues.RealTimeQueue.RealTimeQueueScheduler;
import Queues.UserJobQueue.MultilevelFeedbackQueueScheduler;
import Utils.Chronometer;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import Dispatcher.FileOperations.FileOperations;
import Hardware.RAM;

public class Dispatcher {
    MultilevelFeedbackQueueScheduler mfqs;
    RealTimeQueueScheduler fcfs;
    // Define system resources
    Printer printer;
    Browser browser;
    Router router;
    CD cd;
    RAM _ram;

    public Dispatcher() {
        mfqs = new MultilevelFeedbackQueueScheduler();
        fcfs = new RealTimeQueueScheduler();
        printer = Printer.getInstance();
        browser = Browser.getInstance();
        router = Router.getInstance();
        cd = CD.getInstance();
        _ram = RAM.getInstance();
    }

    public void dispatchProcesses(LinkedList<Proces> processList, Chronometer chronometer) {
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
                        if (proces.getPriority() == 0 && _ram.receiveMemory(proces)) {
                            // Add to Real Time queue
                            fcfs.addProcess(proces);
                        } else if (proces.getPriority() > 0 && proces.getMemoryRequirement() <= 960) {
                            // Add to MFQS queue
                            mfqs.addProcess(proces, proces.getPriority() - 1);
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

    public void printStatus() {
        System.out.println("Memory Available: " + _ram.getAvailableRam());
        System.out.println("Printers Available: " + printer.availableResources());
        System.out.println("Browser Available: " + browser.availableResources());
        System.out.println("Router Available: " + router.availableResources());
        System.out.println("CD Drivers Available: " + cd.availableResources());
    }
}
