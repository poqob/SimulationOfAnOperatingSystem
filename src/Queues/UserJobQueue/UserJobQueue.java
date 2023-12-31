package Queues.UserJobQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Devices.DeviceManager;
import Dispatcher.FileOperations.FileOperations;
import Hardware.Processor;
import Hardware.RAM;
import Process.Proces;
import Utils.Chronometer;

public class UserJobQueue {
    private final Queue<Proces> queue;
    private MultilevelFeedbackQueueScheduler mfqs;

    public UserJobQueue() {
        queue = new LinkedList<>();
        mfqs = new MultilevelFeedbackQueueScheduler();
    }

    public void addProcess(Proces task) {
        queue.add(task);
    }

    public void trigger(boolean realTimeStatus) {
        int count = 0;
        for (Proces proces : queue)
            count++;
        while (count > 0) {
            Proces process = queue.peek();
            // proses 20sn limitini asiyor mu kontrol et
            if (Chronometer.getInstance().getElapsedTime() - process.getArrivalTime() >= 20) {
                // prosesi yok et
            	process.done();
                RAM.getInstance().doneProcessCount++;
                System.out.println("Couldn't be finished within 20 seconds!");
                queue.poll();
            } else {
                // eger kaynaklar musaitse
                if (DeviceManager.getInstance().isThereEnoughDeviceSource(process) && RAM.getInstance().receiveMemory(process)) {
                    DeviceManager.getInstance().allocateDevices(process); // gerekli kaynaklari isgal et
                    // prosesi MFQS'e ekle
                    mfqs.addProcess(process, process.getPriority() - 1);
                    queue.poll();
                }
            }
            count--;
        }
        // MFQS'yi tetikle
        mfqs.triggerScheduler(realTimeStatus);
    }

    @Deprecated
    public void printStatus() {
        System.out.println("//------------------------------------------");
        System.out.print("User Job: ");
        for (Proces p : queue) {
            System.out.print(p.getPid() + "(" + p.getExecutionTime() + "), ");
        }
        System.out.println("\n--------------------------------------------");
        mfqs.printStatus();
    }


    public List<Proces> getQueueAsAList() {
        return queue.stream().toList();
    }
}
