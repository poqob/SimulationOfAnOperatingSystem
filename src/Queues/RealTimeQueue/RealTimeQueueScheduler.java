/*
 * It is important to note that a process that is in a lower priority queue
 * can only execute only when the higher priority queues are empty.
 * 
 * Any running process in the lower priority queue
 * can be interrupted by a process arriving in the higher priority queue.
 */
package Queues.RealTimeQueue;
import java.util.LinkedList;
import Process.Proces;
import Hardware.*;

public class RealTimeQueueScheduler {
    private final float timeQuantum;
    private final LinkedList<Proces> realTimeQueue;
    
    public RealTimeQueueScheduler() {
        this.timeQuantum =  0.97f ; 
        this.realTimeQueue = new LinkedList<>();
    }
    
    public void addProcess (Proces task) {
    	task.ready();
    	realTimeQueue.add(task);
    }
    
    public void triggerScheduler () {
            if (!realTimeQueue.isEmpty()) {
            	// Run queue in new thread
            	new Thread(() -> {
                	runQueue(realTimeQueue);
            	}).start();
                return;
            }
        
        System.out.println("RealTimeQueue Scheduler is Idle for this time interval!");
    }
    
    private void runQueue (LinkedList<Proces> fcfs) {
    	Proces task = fcfs.peek();	// Get the head
    	// check if ram is available
		if(RAM.receiveMemory(task)){
			task.run();
    		// delay for the quantum of the current level
	    	try {
	    	    Thread.sleep((long)(timeQuantum * 1000));
	    	} catch (InterruptedException e) {
	    	    e.printStackTrace();
	    	}
    		if (task.getExecutionTime() > 0) {
    	    	fcfs.set(0,task);		// update the first process of the queue
    		}
    		else {
				task=fcfs.pollFirst();		// discard the first process from the queue
				task.done();				// giving the status "done" to the process
				//cpu.releaseProcess(task, 3);
    		}
			RAM.releaseMemory(task);
		}
    	else {
    		// error: no RAM available for real time process
    	}
    } 
    public void printStatus () {
    	System.out.println("//------------------------------------------"); 
        
        	for(Proces p : realTimeQueue) { 
        		System.out.print(p.getPid() + "(" + p.getExecutionTime() + "), ");
        	}
        	System.out.print('\n');   
    }
}