import java.io.File;
import java.util.LinkedList;
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
		MultilevelFeedbackQueueScheduler mfqs = new MultilevelFeedbackQueueScheduler();
		RealTimeQueueScheduler fcfs = new RealTimeQueueScheduler();
        Thread triggerFCFS;
        Thread triggerMFQS;
		// Semaphore for concurrency
		Semaphore sem = new Semaphore(1);
		Chronometer chronometer = new Chronometer();
		int maxChronometerTime = fileOperations.getMaxOverallTime() + 1;
		System.out.println("Program life time:" + maxChronometerTime);

		chronometer.start();
		long firstTime = -1;
		//while (chronometer.getElapsedTime() <= maxChronometerTime) {
		while (true) {
			if (chronometer.getElapsedTime() - firstTime == 1) {
				firstTime = chronometer.getElapsedTime();
				a.forEach((proces -> {
					// Check if process arrived
					if (proces.getArrivalTime() == chronometer.getElapsedTime()) {
						if (proces.getPriority() == 0 && RAM.getInstance().receiveMemory(proces)) {
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
				// Trigger RealTime Scheduler in a new thread
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
		//System.out.println("No process left..");
	}
}
