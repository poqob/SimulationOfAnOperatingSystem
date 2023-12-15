package Devices;

import Process.Proces;

public class CD extends ADevice {
    // false(0) for available source.
    // true(1) for busy source.
    boolean sources[];

    Proces proces[];
    private static CD _instance;

    private CD() {
        sources = new boolean[]{false, false};
        proces = new Proces[]{null, null};
    }

    public CD getInstance() {
        if (_instance == null)
            _instance = new CD();
        return _instance;
    }

    // @return true if request success, else false.
    @Override
    public boolean toRequest(Proces process) {
        if (proces[0] == null) {
            proces[0] = process;
            sources[0] = true;
            return true;
        } else if (proces[1] == null) {
            proces[1] = process;
            sources[1] = true;
            return true;
        } else return false;
    }

    // @return true if request success, else false.
    @Override
    public boolean toRelease(Proces process) {
        if (proces[0] == process) {
            proces[0] = null;
            sources[0] = false;
            return true;
        } else if (proces[1] == process) {
            proces[1] = null;
            sources[1] = false;
            return true;
        } else return false;
    }
}
