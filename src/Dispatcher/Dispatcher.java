package Dispatcher;
import Process.Proces;
import Queues.RealTimeQueue.RealTimeQueueScheduler;
import Queues.UserJobQueue.MultilevelFeedbackQueueScheduler;
import Utils.Chronometer;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

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
    	// Semaphore for concurrency
        Semaphore sem = new Semaphore(1);
        long firstTime = 0;
        while (true) {
        	if (chronometer.getElapsedTime() - firstTime == 1 && !fcfs.isBusy && !mfqs.isBusy) {
        		firstTime = chronometer.getElapsedTime();
        		processList.forEach((proces -> {
        			// Check if process arrived
        			if (proces.getArrivalTime() == chronometer.getElapsedTime()) {
        				if (proces.getPriority() == 0) {
        					// Add to Real Time queue
        					fcfs.addProcess(proces);
        				}
        				else {
            				// Add to MFQS queue
            				mfqs.addProcess(proces, proces.getPriority() - 1);
        				}
        			}
        		}));
        		fcfs.printStatus();
          		mfqs.printStatus();
          		// Trigger Real Time Scheduler
          		fcfs.triggerScheduler(sem);
        		// Trigger MFQS
        		mfqs.triggerScheduler(sem);
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
