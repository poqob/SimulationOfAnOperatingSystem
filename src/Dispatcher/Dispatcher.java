package Dispatcher;
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
    int printersAvailable;
    int scannersAvailable;
    int modemsAvailable;
    int cdDrivesAvailable;
    int memoryAvailable;

    public Dispatcher() {
        mfqs = new MultilevelFeedbackQueueScheduler();
        fcfs = new RealTimeQueueScheduler();
        printersAvailable = 2;
        scannersAvailable = 1;
        modemsAvailable = 1;
        cdDrivesAvailable = 2;
        memoryAvailable = 1024;
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
        				if (proces.getPriority() == 0 && RAM.getInstance().receiveMemory(proces)) {
        					// Add to Real Time queue
        					fcfs.addProcess(proces);
        				}
        				else if (proces.getPriority() > 0 && proces.getMemoryRequirement() <= 960) {
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
        System.out.println("Memory Available: " + memoryAvailable);
        System.out.println("Printers Available: " + printersAvailable);
        System.out.println("Scanners Available: " + scannersAvailable);
        System.out.println("Modems Available: " + modemsAvailable);
        System.out.println("CD Drives Available: " + cdDrivesAvailable);
    }
}
