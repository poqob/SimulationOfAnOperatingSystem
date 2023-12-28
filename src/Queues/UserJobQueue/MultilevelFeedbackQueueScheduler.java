/*
 * It is important to note that a process that is in a lower priority queue
 * can only execute only when the higher priority queues are empty.
 *
 * Any running process in the lower priority queue
 * can be interrupted by a process arriving in the higher priority queue.
 */
package Queues.UserJobQueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import Devices.DeviceManager;
import Dispatcher.FileOperations.FileOperations;
import Process.Proces;
import Utils.Chronometer;
import Hardware.*;

public class MultilevelFeedbackQueueScheduler {
    private final int numberOfLevels;
    private final int[] timeQuantums;
    private final Queue<Proces>[] queues;
    // Round Robin Queue
    private final RoundRobin RRQ;

    public MultilevelFeedbackQueueScheduler() {
        numberOfLevels = 3;
        timeQuantums = new int[]{1, 1, 1};
        queues = new LinkedList[numberOfLevels];
        RRQ = new RoundRobin(timeQuantums[numberOfLevels - 1]);
        
        for (int i = 0; i < numberOfLevels; i++) {
            this.queues[i] = new LinkedList<>();
        }
    }

    public void addProcess(Proces task, int level) {
        task.ready();
        queues[level].add(task);
    }

    // This should be triggered by system timer on every interval (1 sec)
    public void triggerScheduler(boolean realTimeStatus) {
        for (int i = 0; i < numberOfLevels; i++) {
        	// Check if the queue has any tasks
            if (!queues[i].isEmpty()) {
            	// check if process hasn't exceeded 20 seconds limit
            	int count = 0;
                for(Proces proces : queues[i])
                    count++;
                while (count > 0) {
                	Proces process = queues[i].peek();
            		if (Chronometer.getInstance().getElapsedTime() - process.getArrivalTime() >= 20) {
            			// Terminate the task
            	    	RAM.getInstance().releaseMemory(process); // Release taken memory
            	    	DeviceManager.getInstance().releaseDevices(process); // Release the taken devices
            			process.done();
            			FileOperations.doneProcessCount++;
            			System.out.println("Couldn't be finished within 20 seconds!");
            			queues[i].poll();
            		}
            		count--;
                }
                // Check the survived tasks
            	if (!queues[i].isEmpty()) {
            		// Check if Real Time scheduler is idle
            		if (!realTimeStatus) {
            			if (i < 2) {
            				runQueue(i);
            			}
            			else {
            				// Run in Round Robin mode
            				queues[i] = RRQ.runScheduler(queues[i]);
            			}
            		}
            		else {
            			// Interrupt the user process
            			queues[i].peek().interrupt();
            		}
            	return; // A higher level task got executed. So ignore the rest of the queues.
            	}
            }
        }
        System.out.println("MFQS is Idle for this interval!");
    }

    private void runQueue(int level) {
        // Get the head
        Proces task = queues[level].poll();
        task.run();
        // wait for the quantum of the current level
        try {
            Thread.sleep(timeQuantums[level] * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task.execute();
        if (task.getExecutionTime() > 0) {
            level++;
            // Add to the next queue
            addProcess(task, level);
        } else {
        	// Task is done
            RAM.getInstance().releaseMemory(task); // Release taken memory
            DeviceManager.getInstance().releaseDevices(task); // Release the taken devices
            task.done();
            FileOperations.doneProcessCount++;
            // DONE (3)
            //cpu.releaseProcess(task, 3);
        }
    }

    public void printStatus() {
        System.out.println("--------------------------------------------");
        for (int i = 0; i < numberOfLevels; i++) {
            System.out.print(i + ": ");
            for (Proces p : queues[i]) {
                System.out.print(p.getPid() + "(" + p.getExecutionTime() + "), ");
            }
            System.out.print('\n');
        }
        System.out.println("--------------------------------------------");
    }
}