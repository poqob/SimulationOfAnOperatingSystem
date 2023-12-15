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
import Process.Proces;

class MultilevelFeedbackQueueScheduler {
    private final int numberOfLevels;
    private final float[] timeQuantums;
    private final Queue<Proces>[] queues;
    
    public MultilevelFeedbackQueueScheduler() {
        this.numberOfLevels = 3;
        this.timeQuantums = new float[] { 0.98f, 0.98f, 0.98f }; // Approximate values entered for a better result
        this.queues = new LinkedList[numberOfLevels];
        
        for (int i = 0; i < numberOfLevels; i++) {
            this.queues[i] = new LinkedList<>();
        }
    }
    
    public void addProccess (Proces task, int level) {
    	task.ready();
    	queues[level].add(task);
    }
    
    // This should be triggered by system timer on every interval (1 sec)
    public void triggerScheduler () {
    	// Printing purposes. Can be removed later.
    	boolean executed = false;
    	
        for (int i = 0; i < numberOfLevels; i++) {
            Queue<Proces> currentQueue = queues[i];
            if (!currentQueue.isEmpty()) {
            	executed = true;
            	runQueue(currentQueue, i);
                // A higher queue has tasks in it. So do not check the rest of the queues.
                break;
            }
        }
        if (!executed) {
        	System.out.println("[Yetalit]: My Scheduler is Idle for this interval!");
        }
    }
    
    private void runQueue (Queue<Proces> queue, int level) {
    	// Get the head
    	Proces task = queue.poll();
    	// check if ram and resources are available
    	if (task.claimResource()) {
    		task.run();
    		// delay for the quantum of the current level
	    	try {
	    	    Thread.sleep((long)(timeQuantums[level] * 1000));
	    	} catch (InterruptedException e) {
	    	    e.printStackTrace();
	    	}
    		if (task.getExecutionTime() > 0) {
    	    	if (level < 2) {
    				level++;
    			}
    			// Add to the next queue
    			addProccess(task, level);
    		}
    		else {
    			// DONE (3)
    			//cpu.releaseProccess(task, 3);
    		}
    	}
    	else {
    		// INTERRUPTED (2)
    		//cpu.releaseProccess(task, 2);
    	}
    }
}