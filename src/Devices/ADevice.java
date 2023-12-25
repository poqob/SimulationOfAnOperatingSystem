package Devices;

import Process.Proces;

/// All devices will be derived from ADevice class. Override methods.
public abstract class ADevice {
    public abstract boolean toRequest(Proces process);

    public abstract boolean toRelease(Proces process);

    public abstract int availableResources();

    public abstract Proces[] getSourceOwners();
}
