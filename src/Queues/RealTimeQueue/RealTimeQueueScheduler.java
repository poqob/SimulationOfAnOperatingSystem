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
        if(task.getExecutionTime() < 20){     // proses 20sn limitini asiyor mu kontrol et
        task.ready();
        realTimeQueue.add(task);
    }else
        System.out.println("Process execution time exceeds 20sec limit!");
    }

    public boolean triggerScheduler() {
        // kuyrukta proses var mi kontrol et
        if (!realTimeQueue.isEmpty()) {
            // kuyrugu yurut
            runQueue(realTimeQueue);
            return true;
        }
        System.out.println("RealTimeQueue Scheduler is Idle for this time interval!");
        return false;
    }


    private void runQueue(LinkedList<Proces> fcfs) {
        Proces task = fcfs.peek();    // kuyrugun basindaki prosesi getir
        _ram.receiveMemory(task);   // gerekli bellekte alan ayir
        task.run();
        // bir zaman araliginca bekle
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task.execute();
        if (task.getExecutionTime() > 0) {
            fcfs.set(0, task);        // kuyrugun basindaki prosesi guncelle
        } else {
            // Task is done
            task = fcfs.pollFirst();        // proses icrasi bittiyse kuyruktan kaldir, bellegi iade et
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