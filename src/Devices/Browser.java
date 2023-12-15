package Devices;

import Process.Proces;

public class Browser extends ADevice {

    // false(0) for available source.
    // true(1) for busy source.
    boolean sources[];

    Proces proces[];
    private static Browser _instance;

    private Browser() {
        sources = new boolean[]{false};
        proces = new Proces[]{null};
    }

    public Browser getInstance() {
        if (_instance == null)
            _instance = new Browser();
        return _instance;
    }

    // @return true if request success, else false.
    @Override
    public boolean toRequest(Proces process) {
        if (proces[0] == null) {
            proces[0] = process;
            sources[0] = true;
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
        } else return false;
    }
}
