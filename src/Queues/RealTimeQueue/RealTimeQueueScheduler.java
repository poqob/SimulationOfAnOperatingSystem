package Queues.RealTimeQueue;

import java.util.LinkedList;
import java.util.List;

import Dispatcher.FileOperations.FileOperations;
import Process.Proces;
import Hardware.*;

public class RealTimeQueueScheduler {
    RAM _ram;
    Processor _processor;
    private final LinkedList<Proces> realTimeQueue;

    public RealTimeQueueScheduler() {
        _ram = RAM.getInstance();
        _processor = Processor.getInstance();
        realTimeQueue = new LinkedList<>();
    }

    public void addProcess(Proces task) {
        task.ready();
        realTimeQueue.add(task);
    }

    public boolean triggerScheduler() {
        // Check if there is any tasks
        if (!realTimeQueue.isEmpty()) {
            // Run the queue
            runQueue(realTimeQueue);
            return true;
        }
        System.out.println("RealTimeQueue Scheduler is Idle for this time interval!");
        return false;
    }


    private void runQueue(LinkedList<Proces> fcfs) {
        Proces task = fcfs.peek();    // Get the head
        _ram.receiveMemory(task); // Receive the needed memory (if hasn't already)
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
            // Task is done
            task = fcfs.pollFirst();        // discard the first process from the queue
            _processor.process(task);
        }
    }

    @Deprecated
    public void printStatus() {
        System.out.println("//------------------------------------------");
        System.out.print("RealTime: ");
        for (Proces p : realTimeQueue) {
            System.out.print(p.getPid() + "(" + p.getExecutionTime() + "), ");
        }
        System.out.println("\n--------------------------------------------");
    }


    public List<Proces> getQueueAsAList() {
        return realTimeQueue.stream().toList();
    }
}