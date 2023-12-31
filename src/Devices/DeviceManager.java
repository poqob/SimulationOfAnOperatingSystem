package Devices;

import java.util.LinkedList;
import java.util.Map;

import Hardware.RAM;
import Process.Proces;

public class DeviceManager {
    // kaynaklar
    Printer printer;
    Browser browser;
    Router router;
    CD cd;
    RAM _ram;
    // kaynak listesi
    LinkedList<ADevice> devices;

    // singleton mimari
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

    // proses icin kaynaklarin uygunlugunu kontroler eden method. Kaynaklar musaitse true dondurur
    public boolean isThereEnoughDeviceSource(Proces proces) {
        Map<EDevices, Integer> _map = proces.getIORequirements();
        // prosesin kaynak ihtiyaclarini tek tek kontrol et
        for (EDevices key : _map.keySet()) {
            int request = _map.get(key);
            switch (key) {
                case Printer:
                    if (printer.availableResources() < request) {
                        return false;
                    }
                    break;
                case Browser:
                    if (browser.availableResources() < request) {
                        return false;
                    }
                    break;
                case CD:
                    if (cd.availableResources() < request) {
                        return false;
                    }
                    break;
                case Router:
                    if (router.availableResources() < request) {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        return true;        // istenen her kaynak musaitse true dondur
    } 


    public boolean doesRecourcesMeetTheNeed(Proces proces) {
        Map<EDevices, Integer> _map = proces.getIORequirements();
        // prosesin kaynak ihtiyaclarini tek tek kontrol et
        for (EDevices key : _map.keySet()) {
            int request = _map.get(key);
            switch (key) {
                case Printer:
                    if (printer.maximum_source < request) {
                        return false;
                    }
                    break;
                case Browser:
                    if (browser.maximum_source < request) {
                        return false;
                    }
                    break;
                case CD:
                    if (cd.maximum_source < request) {
                        return false;
                    }
                    break;
                case Router:
                    if (router.maximum_source < request) {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        return true;   // istenen her kaynak musaitse true dondur
    }

    // kaynaklar uygunsa istenen kaynaklari prosese teslim et
    public void allocateDevices(Proces proces) {
        Map<EDevices, Integer> _map = proces.getIORequirements();
        // prosesin kaynak ihtiyaclarini tek tek kontrol et
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


    // kaynaklari iade et
    public void releaseDevices(Proces proces) {
        Map<EDevices, Integer> _map = proces.getIORequirements();
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
