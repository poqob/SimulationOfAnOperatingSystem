package Hardware;

import Devices.DeviceManager;
import Process.Proces;

// islemci simulasyon sinifi.
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
        // proses kaynaklarinin iadesini sagla
        proces.done();
        DeviceManager.getInstance().releaseDevices(proces);
        _ram.releaseMemory(proces);
        return proces;
    }


}
