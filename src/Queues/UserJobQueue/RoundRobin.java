package Queues.UserJobQueue;

import Hardware.Processor;
import Process.Proces;

import java.util.Queue;

import Devices.DeviceManager;
import Dispatcher.FileOperations.FileOperations;
import Hardware.RAM;

public class RoundRobin {
    private final int timeQuantum;

    public RoundRobin(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

        // Round Robin olarak yurut
        public Queue<Proces> runScheduler(Queue<Proces> rrq) {
        // kuyrugun basindakini getir
        Proces task = rrq.poll();
        task.run();
        // kuyruk seviyesinin kuantum suresince bekle
        try {
            Thread.sleep(timeQuantum * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task.execute();
        if (task.getExecutionTime() > 0) {
            // kuyruga ekle
            task.ready();
            rrq.add(task);
        } else {
            Processor.process(task);
        }
        return rrq;
    }
}
