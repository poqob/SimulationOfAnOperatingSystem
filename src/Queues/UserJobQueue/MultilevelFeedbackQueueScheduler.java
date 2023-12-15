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
import java.util.Timer;
import java.util.TimerTask;

class MultilevelFeedbackQueueScheduler {
    private final Queue<String>[] queues;
    private final int numberOfLevels = 3;
    private final int[] timeQuantums = { 1, 1, 1 };

    public MultilevelFeedbackQueueScheduler() {
        this.queues = new LinkedList[numberOfLevels];

        for (int i = 0; i < numberOfLevels; i++) {
            this.queues[i] = new LinkedList<>();
        }
    }
    
    public void addProccess (String task, int level) {
    	queues[level].add(task);
    }
    
    // This should be triggered by system timer on every interval (1 sec)
    public void triggerScheduler () {
    	// Printing purposes. Can be removed later.
    	boolean executed = false;
    	
        for (int i = 0; i < numberOfLevels; i++) {
            Queue<String> currentQueue = queues[i];
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
    
    public void runQueue (Queue<String> queue, int level) {
    	// Get the head
    	String task = queue.poll();
    	// check if ram and resource are available
    	/*
    	if (task.claimResource()) {
    		// run task
    		task.remainingTime--;
    		if (task.remainingTime > 0) {
    	    	if (level < 2) {
    				level++;
    			}
    			// Add to the next queue
    			addProccess(task, level);
    		}
    		else {
    			cpu.releaseProccess(task, DONE);
    		}
    	}
    	else {
    		cpu.releaseProccess(task, INTERRUPTED);
    	}
    	*/
    }
}