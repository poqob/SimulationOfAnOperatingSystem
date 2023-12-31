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
import java.util.concurrent.Semaphore;

import Devices.DeviceManager;
import Dispatcher.FileOperations.FileOperations;
import Process.Proces;
import Utils.Chronometer;
import Hardware.*;

public class MultilevelFeedbackQueueScheduler {
    private final int numberOfLevels;
    private final int[] timeQuantums;
    private final Queue<Proces>[] queues;
    private final RoundRobin RRQ;

    public MultilevelFeedbackQueueScheduler() {
        numberOfLevels = 3;
        timeQuantums = new int[]{1, 1, 1};
        queues = new LinkedList[numberOfLevels];
        // Round Robin Queue
        RRQ = new RoundRobin(timeQuantums[numberOfLevels - 1]);

        for (int i = 0; i < numberOfLevels; i++) {
            this.queues[i] = new LinkedList<>();
        }
    }

    public void addProcess(Proces task, int level) {
        task.ready();
        queues[level].add(task);
    }

    // Zamanlayici tarafindan her zaman araliginda(1sn) tetiklenmeli
    public void triggerScheduler(boolean realTimeStatus) {
        for (int i = 0; i < numberOfLevels; i++) {
            if (!queues[i].isEmpty()) {
                // proses 20sn limitini asiyor mu kontrol et
                int count = 0;
                for (Proces proces : queues[i])
                    count++;
                while (count > 0) {
                    Proces process = queues[i].peek();
                    if (Chronometer.getInstance().getElapsedTime() - process.getArrivalTime() >= 20) {
                        Processor.process(process);
                        System.out.println("Couldn't be finished within 20 seconds!");
                        queues[i].poll();
                    }
                    count--;
                }
                if (!queues[i].isEmpty()) {
                    if (!realTimeStatus) {
                        if (i < 2) {
                            runQueue(i);
                        } else {
                            // Round Robin olarak yurut
                            queues[i] = RRQ.runScheduler(queues[i]);
                        }
                    } else {
                        queues[i].peek().interrupt();
                    }
                    return;
                }
            }
        }
        System.out.println("MFQS is Idle for this interval!");
    }

    private void runQueue(int level) {
        // kuyrugun basindakini getir
        Proces task = queues[level].poll();
        task.run();
        // kuyruk seviyesinin kuantum suresince bekle
        try {
            Thread.sleep(timeQuantums[level] * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task.execute();
        if (task.getExecutionTime() > 0) {
            level++;
            // Alt kuyruga aktar
            addProcess(task, level);
        } else {
            Processor.process(task);
        }
    }

    public void printStatus() {
        System.out.println("--------------------------------------------");
        for (int i = 0; i < numberOfLevels; i++) {
            System.out.print(i + ": ");
            for (Proces p : queues[i]) {
                System.out.print(p.getPid() + "(" + p.getExecutionTime() + "), ");
            }
            System.out.print('\n');
        }
        System.out.println("--------------------------------------------");
    }
}