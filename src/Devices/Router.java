package Devices;

import Process.Proces;

public class Router extends ADevice {

    // kaynak bostaysa false(0)
    // kaynak doluysa true(1)
    boolean sources[];

    Proces proces[];
    private static Router _instance;
    public final int maximum_source = 1;
    

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
