package Devices;

/// All devices will be derived from ADevice class. Override methods.
public abstract class ADevice {
    public abstract boolean toRequest();
    public  abstract boolean toRelease();
}
