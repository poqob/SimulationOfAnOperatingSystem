package Hardware;

import Devices.DeviceManager;
import Process.Proces;

// TODO: processing(Proces process, Time time): waits while given time then sends proceed process to dispatcher.
public class Processor extends AHardware {
    private static RAM _ram;


    private static Processor _instance;

    private Processor() {
        _ram = RAM.getInstance();
    }

    public static Processor getInstance() {
        if (_instance == null)
            _instance = new Processor();
        return _instance;
    }


    public static Proces process(Proces proces) {
        // think we proceed process then release memory.
        proces.done();
        DeviceManager.getInstance().releaseDevices(proces);
        _ram.releaseMemory(proces);
        return proces;
    }


}
