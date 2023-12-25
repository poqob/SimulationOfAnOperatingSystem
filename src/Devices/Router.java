package Devices;

import Process.Proces;

public class Router extends ADevice {


    // false(0) for available source.
    // true(1) for busy source.
    boolean sources[];

    Proces proces[];
    private static Router _instance;

    private Router() {
        super(EDevices.Router);
        sources = new boolean[]{false};
        proces = new Proces[]{null};
    }

    public static Router getInstance() {
        if (_instance == null)
            _instance = new Router();
        return _instance;
    }

    // @return true if request success, else false.
    @Override
    public boolean allocate(Proces process) {
        if (proces[0] == null) {
            proces[0] = process;
            sources[0] = true;
            return true;
        } else return false;
    }

    // @return true if request success, else false.
    @Override
    public boolean release(Proces process) {
        if (proces[0] == process) {
            proces[0] = null;
            sources[0] = false;
            return true;
        } else return false;
    }


    @Override
    public int availableResources() {
        int count = 0;
        for (boolean source : sources)
            if (source)
                count++;
        return count;
    }

    @Override
    public Proces[] getSourceOwners() {
        return proces;
    }
}
