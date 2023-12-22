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
import Process.Proces;
import Hardware.*;

public class MultilevelFeedbackQueueScheduler {
    private final int numberOfLevels;
    private final int[] timeQuantums;
    private final Queue<Proces>[] queues;
    public boolean isBusy;
    
    public MultilevelFeedbackQueueScheduler() {
        numberOfLevels = 3;
        timeQuantums = new int[] { 1, 1, 1 };
        queues = new LinkedList[numberOfLevels];
        isBusy = false;
        
        for (int i = 0; i < numberOfLevels; i++) {
            this.queues[i] = new LinkedList<>();
        }
    }
    
    public void addProcess (Proces task, int level) {
    	task.ready();
    	queues[level].add(task);
    }
    
    // This should be triggered by system timer on every interval (1 sec)
    public void triggerScheduler (final Semaphore sem) {
        for (Queue<Proces> queue : queues) {
            if (!queue.isEmpty()) {
            	isBusy = true;
            	// Run queue in new thread
            	new Thread(() -> {
                	runQueue(queue, sem);
            	}).start();
                // A higher queue has tasks in it. So do not check the rest of the queues.
                return;
            }
        }
        System.out.println("[Yetalit]: My Scheduler is Idle for this interval!");
    }
    
    private void runQueue (Queue<Proces> queue, final Semaphore sem) {
    	// Get the head
    	Proces task = queue.poll();
    	int level = task.getPriority() - 1;
    	// check if ram is available
    	if (RAM.getInstance().receiveMemory(task)) {
    		task.run();
    		try {
        		// Wait for the realtime scheduler to acquire resources
				sem.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
    		sem.release();
    		// if needed sources are not available
    		/*
    		 *  // INTERRUPTED (2)
                //cpu.releaseProcess(task, 2);
            	isBusy = false;
            	return;
    		 */
    		// wait for the quantum of the current level
	    	try {
	    	    Thread.sleep(timeQuantums[level] * 1000);
	    	} catch (InterruptedException e) {
	    	    e.printStackTrace();
	    	}
            task.execute();
    		if (task.getExecutionTime() > 0) {
    	    	if (level < 2) {
    				level++;
    			}
    			// Add to the next queue
    			addProcess(task, level);
    		}
    		else {
    			RAM.getInstance().releaseMemory(task);
    			// DONE (3)
    			//cpu.releaseProcess(task, 3);
    		}
    	}
    	else {
    		// INTERRUPTED (2)
    		//cpu.releaseProcess(task, 2);
    	}
    	isBusy = false;
    }
    
    public void printStatus () {
    	System.out.println("//------------------------------------------"); 
        for (int i = 0; i < numberOfLevels; i++) {
        	System.out.print(i + ": ");
        	for(Proces p : queues[i]) { 
        		System.out.print(p.getPid() + "(" + p.getExecutionTime() + "), ");
        	}
        	System.out.print('\n');
        }
        System.out.println("--------------------------------------------"); 
    }
}