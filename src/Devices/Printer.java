package Devices;

import Process.Proces;

public class Printer extends ADevice {

    // kaynak bostaysa false(0)
    // kaynak doluysa true(1)
    boolean sources[];

    Proces proces[];
    private static Printer _instance;
    public final int maximum_source = 2;

    private Printer() {
        super(EDevices.Printer);
        sources = new boolean[] { false, false };
        proces = new Proces[] { null, null };
    }

    public static Printer getInstance() {
        if (_instance == null)
            _instance = new Printer();
        return _instance;
    }

    // istek basariliysa true dondur
    @Override
    public boolean allocate(Proces process) {
        if (proces[0] == null) {
            proces[0] = process;
            sources[0] = true;
            return true;
        } else if (proces[1] == null) {
            proces[1] = process;
            sources[1] = true;
            return true;
        } else
            return false;
    }

    // istek basariliysa true dondur
    @Override
    public boolean release(Proces process) {
        if (proces[0] == process) {
            proces[0] = null;
            sources[0] = false;
            return true;
        } else if (proces[1] == process) {
            proces[1] = null;
            sources[1] = false;
            return true;
        } else
            return false;
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
