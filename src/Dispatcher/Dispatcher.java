package Dispatcher;

import Devices.DeviceManager;
import Devices.EDevices;
import Dispatcher.FileOperations.FileOperations;
import Dispatcher.Ui.Ui;
import Process.Proces;
import Queues.RealTimeQueue.RealTimeQueueScheduler;
import Queues.UserJobQueue.MultilevelFeedbackQueueScheduler;
import Queues.UserJobQueue.UserJobQueue;
import Utils.Chronometer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Semaphore;

import Hardware.RAM;

public class Dispatcher {
    // User Job Queue(kuyrugu)
    UserJobQueue ujq;
    // Real Time Queue(kuyrugu)
    RealTimeQueueScheduler fcfs;
    // sistem kaynaklarini tanimla
    DeviceManager deviceManager;
    RAM _ram;
    Chronometer chronometer;

    public Dispatcher() {
        ujq = new UserJobQueue();
        fcfs = new RealTimeQueueScheduler();
        chronometer = Chronometer.getInstance();
        _ram = RAM.getInstance();
        deviceManager = DeviceManager.getInstance(); // device manager tum static i/o kaynaklarini kapsar
    }

    public void dispatchProcesses(LinkedList<Proces> processList, int numberOfProcesses) {
        chronometer.start();
        System.out.println("Total number of processes: " + numberOfProcesses);
        long firstTime = -1;
        while (numberOfProcesses > _ram.getDoneProcessCount()) {
            if (chronometer.getElapsedTime() - firstTime == 1) {
                firstTime = chronometer.getElapsedTime();
                processList.forEach((proces -> {
                    // proses geldi mi diye bak(arrivalTime)
                    if (proces.getArrivalTime() == chronometer.getElapsedTime()) {
                        // gerekli kaynaklari isgal et
                        Map<EDevices, Integer> ioMap = proces.getIORequirements();
                        // onceliği ve kaynaklarin uygunlugunu kontrol et
                        if (proces.getPriority() == 0 && proces.getMemoryRequirement() <= _ram.primary_memory_size && deviceManager.doesRecourcesMeetTheNeed(proces)) {
                            fcfs.addProcess(proces); // Real Time queue ekle
                        } else if (proces.getPriority() > 0 && proces.getMemoryRequirement() <= _ram.secondary_memory_size && deviceManager.doesRecourcesMeetTheNeed(proces)) {
                            ujq.addProcess(proces); // User Job Queue ekle
                        } else
                            _ram.increaseDoneProcessCountRegardless();      // process gecersizse bile sayaci arttir
                    }
                }));
                Ui.getInstance().write(fcfs.getQueueAsAList());
                Ui.getInstance().write(ujq.getQueueAsAList());
                // cizelgeliyicileri tetikle
                ujq.trigger(fcfs.triggerScheduler());
            }
        }
    }


}
