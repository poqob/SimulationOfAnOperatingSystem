import java.io.File;
import java.util.LinkedList;

import Utils.Chronometer;
import Process.Proces;
import Devices.*;
import Dispatcher.FileOperations.FileOperations;
import Queues.UserJobQueue.*;

/// Tests will be written here. @everyone
public class Test {
    public static void main(String[] args) {
        // tests for devices.
        FileOperations fileOperations = FileOperations.getInstance();
        fileOperations.readFile();
        LinkedList<Proces> a = fileOperations.getParsedProcesses();
        a.forEach((proces -> System.out.println(proces.getArrivalTime() + " (pid: " + proces.getPid() + ")")));
        // MFQS Test
        MultilevelFeedbackQueueScheduler mfqs = new MultilevelFeedbackQueueScheduler();
        Chronometer chronometer = new Chronometer();
        chronometer.start();
        long firstTime = 0;
        while (true) {
        	if (chronometer.getElapsedTime() - firstTime == 1) {
        		firstTime = chronometer.getElapsedTime();
        		a.forEach((proces -> {
        			// Check if process arrived
        			if (proces.getArrivalTime() == chronometer.getElapsedTime()) {
        				// Add to queue
        				mfqs.addProcess(proces, proces.getPriority() - 1);
        			}
        		}));
          		mfqs.printStatus();
        		// Trigger Scheduler
        		mfqs.triggerScheduler();
        	}
        }
    }
}
