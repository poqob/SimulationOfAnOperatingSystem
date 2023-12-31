package Devices;

import Process.Proces;

public class Browser extends ADevice {

    // kaynak bostaysa false(0)
    // kaynak doluysa true(1)
    public final int maximum_source = 1;
    boolean sources[];

    Proces proces[];
    private static Browser _instance;

    private Browser() {
        super(EDevices.Browser);
        sources = new boolean[]{false};
        proces = new Proces[]{null};
    }

    public static Browser getInstance() {
        if (_instance == null)
            _instance = new Browser();
        return _instance;
    }

    // istek basariliysa true dondur
    @Override
    public boolean allocate(Proces process) {
        if (proces[0] == null) {
            proces[0] = process;
            sources[0] = true;
            return true;
        } else return false;
    }

    // istek basariliysa true dondur
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
            if (!source)
                count++;
        return count;
    }

    @Override
    public Proces[] getSourceOwners() {
        return proces;
    }


}
