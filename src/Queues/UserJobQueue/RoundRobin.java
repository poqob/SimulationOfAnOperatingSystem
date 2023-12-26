package Queues.UserJobQueue;
import Process.Proces;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import Dispatcher.FileOperations.FileOperations;
import Hardware.RAM;

public class RoundRobin {
    private final int timeQuantum;

    public RoundRobin(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }
    
    // Run in Round Robin mode
    public Queue<Proces> runScheduler(Queue<Proces> rrq) {
        // Get the head
        Proces task = rrq.poll();
        // check if ram is available
        // TODO: Do we controll device availability here??? DeviceManager.getInstance().isThereEnoughDeviceSource(task) -> boolean
        if (RAM.getInstance().receiveMemory(task)) {
            task.run();
            // if needed sources are not available
    		/*
    		 *  // INTERRUPTED (2)
                //cpu.releaseProcess(task, 2);
            	return rrq;
    		 */
            // wait for the quantum of the current level
            try {
                Thread.sleep(timeQuantum * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.execute();
            if (task.getExecutionTime() > 0) {
                // Add to the queue
                task.ready();
                rrq.add(task);
            } else {
                RAM.getInstance().releaseMemory(task);
                task.done();
                FileOperations.doneProcessCount++;
                // DONE (3)
                //cpu.releaseProcess(task, 3);
            }
        } else {
            task.interrupt();
            // Add to the queue
            task.ready();
            rrq.add(task);
            // INTERRUPTED (2)
            //cpu.releaseProcess(task, 2);
        }
        return rrq;
    }
}
