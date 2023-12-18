package Queues.RealTimeQueue;
import java.util.LinkedList;
import Process.Proces;
import Hardware.*;

public class RealTimeQueueScheduler {
    private final LinkedList<Proces> realTimeQueue;
    public boolean isBusy;
    
    public RealTimeQueueScheduler() {
        realTimeQueue = new LinkedList<>();
        isBusy = false;
    }
    
    public void addProcess (Proces task) {
    	task.ready();
    	realTimeQueue.add(task);
    }
    
    public void triggerScheduler () {
        if (!realTimeQueue.isEmpty()) {
            isBusy = true;
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
		if(RAM.getInstance().receiveMemory(task)){
			task.run();
			// accrue needed resources
			task.claimResource(true);
			// wait for one time interval
	    	try {
	    	    Thread.sleep(1000);
	    	} catch (InterruptedException e) {
	    	    e.printStackTrace();
	    	}
	    	task.execute();
    		if (task.getExecutionTime() > 0) {
    	    	fcfs.set(0,task);		// update the first process of the queue
    		}
    		else {
				task=fcfs.pollFirst();		// discard the first process from the queue
				RAM.getInstance().releaseMemory(task);
				//cpu.releaseProcess(task, 3);
    		}
		}
    	else {
    		// error: no RAM available for real time process
    	}
		isBusy = false;
    } 
    public void printStatus () {
    	System.out.println("//------------------------------------------");
    	System.out.print("Real Time: ");
        for(Proces p : realTimeQueue) { 
        	System.out.print(p.getPid() + "(" + p.getExecutionTime() + "), ");
        }
        System.out.println("\n--------------------------------------------");   
    }
}