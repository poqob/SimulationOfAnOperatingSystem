package Devices;

import java.util.LinkedList;
import java.util.Map;

import Hardware.RAM;
import Process.Proces;

public class DeviceManager {
    // devices
    Printer printer;
    Browser browser;
    Router router;
    CD cd;
    RAM _ram;
    // device list
    LinkedList<ADevice> devices;

    // singleton architecture
    private static DeviceManager _instance;

    // private constructor.
    private DeviceManager() {
        devices = new LinkedList<ADevice>();
        printer = Printer.getInstance();
        browser = Browser.getInstance();
        router = Router.getInstance();
        cd = CD.getInstance();
        _ram = RAM.getInstance();

        devices.add(printer);
        devices.add(browser);
        devices.add(router);
        devices.add(cd);
    }

    // factory constructor.
    public static DeviceManager getInstance() {
        if (_instance == null)
            _instance = new DeviceManager();
        return _instance;
    }

    // it is device source controller method. that takes process as parameter and turns source availability situation.
    public boolean isThereEnoughDeviceSource(Proces proces) {
        Map<EDevices, Integer> _map = proces.getIORequirements();
        // iterate over process source requirements.
        for (EDevices key : _map.keySet()) {
            int request = _map.get(key);
            switch (key) {
                case Printer:
                    if (printer.availableResources() < request)
                        return false;
                    break;
                case Browser:
                    if (browser.availableResources() < request)
                        return false;
                    break;
                case CD:
                    if (cd.availableResources() < request)
                        return false;
                    break;
                case Router:
                    if (router.availableResources() < request)
                        return false;
                    break;
                default:
                    break;
            }
        }
        return true; // if all wanted sources were met then return true.
    }

    // if availability okay then allocate devices.
    public void allocateDevices(Proces proces) {
        Map<EDevices, Integer> _map = proces.getIORequirements();
        // iterate over process source requirements.
        for (EDevices key : _map.keySet()) {
            switch (key) {
                case Printer:
                    printer.allocate(proces);
                    break;
                case Browser:
                    browser.allocate(proces);
                    break;
                case CD:
                    cd.allocate(proces);
                    break;
                case Router:
                    router.allocate(proces);
                    break;
                default:
                    break;
            }
        }
    }


    // release devices.
    public void releaseDevices(Proces proces) {
        Map<EDevices, Integer> _map = proces.getIORequirements();
        // iterate over process source requirements.
        for (EDevices key : _map.keySet()) {
            switch (key) {
                case Printer:
                    printer.release(proces);
                    break;
                case Browser:
                    browser.release(proces);
                    break;
                case CD:
                    cd.release(proces);
                    break;
                case Router:
                    router.release(proces);
                    break;
                default:
                    break;
            }
        }
    }

    // TODO: its only test method, delete before release version of the program.
    public void printStatus() {
        System.out.println("Memory Available: " + _ram.getAvailableRam());
        System.out.println("Printers Available: " + printer.availableResources());
        System.out.println("Browser Available: " + browser.availableResources());
        System.out.println("Router Available: " + router.availableResources());
        System.out.println("CD Drivers Available: " + cd.availableResources());
    }
}