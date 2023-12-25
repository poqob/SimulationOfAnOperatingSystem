package Devices;

import Process.Proces;

/// All devices will be derived from ADevice class. Override methods.
public abstract class ADevice {
    public final EDevices deviceType;

    protected ADevice(EDevices deviceType) {
        this.deviceType = deviceType;
    }

    public abstract boolean allocate(Proces process);

    public abstract boolean release(Proces process);

    public abstract int availableResources();

    public abstract Proces[] getSourceOwners();
}
