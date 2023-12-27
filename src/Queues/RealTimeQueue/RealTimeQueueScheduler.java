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

    public boolean triggerScheduler() {
        if (!realTimeQueue.isEmpty()) {
            runQueue(realTimeQueue);
            return true;
        }
        System.out.println("RealTimeQueue Scheduler is Idle for this time interval!");
        return false;
    }

    // NOTE: [Mustafa] i read run queue but my brain
    private void runQueue(LinkedList<Proces> fcfs) {
        Proces task = fcfs.peek();    // Get the head
        _ram.receiveMemory(task);
        task.run();
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
            _ram.releaseMemory(task);
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