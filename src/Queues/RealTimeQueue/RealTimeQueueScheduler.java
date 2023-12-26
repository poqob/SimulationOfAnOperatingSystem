package Queues.RealTimeQueue;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import Dispatcher.FileOperations.FileOperations;
import Process.Proces;
import Hardware.*;

public class RealTimeQueueScheduler {
    RAM _ram;
    private final LinkedList<Proces> realTimeQueue;

    public RealTimeQueueScheduler() {
        _ram = RAM.getInstance();
        realTimeQueue = new LinkedList<>();
    }

    public void addProcess(Proces task) {
        task.ready();
        realTimeQueue.add(task);
    }

    public void triggerScheduler(final Semaphore sem) {
        if (!realTimeQueue.isEmpty()) {
            runQueue(realTimeQueue, sem);
            return;
        }
        sem.release();
        System.out.println("RealTimeQueue Scheduler is Idle for this time interval!");
    }

    // TODO: _ram.receiveMemory(task); // memmory allocation needed. Can you please allocate memory @OzturkVedat.
    // NOTE: [Mustafa] i read run queue but my brain
    private void runQueue(LinkedList<Proces> fcfs, final Semaphore sem) {
        Proces task = fcfs.peek();    // Get the head
        task.run();
        // acquire needed resources here
        sem.release();
        // wait for one time interval
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task.execute();
        if (task.getExecutionTime() > 0) {
            fcfs.set(0, task);        // update the first process of the queue
        } else {
            task = fcfs.pollFirst();        // discard the first process from the queue
            RAM.getInstance().releaseMemory(task);
            task.done();
            FileOperations.doneProcessCount++;
            //cpu.releaseProcess(task, 3);
        }
    }

    public void printStatus() {
        System.out.println("//------------------------------------------");
        System.out.print("RealTime: ");
        for (Proces p : realTimeQueue) {
            System.out.print(p.getPid() + "(" + p.getExecutionTime() + "), ");
        }
        System.out.println("\n--------------------------------------------");
    }
}