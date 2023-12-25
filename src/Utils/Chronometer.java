package Utils;

public class Chronometer {


    private Chronometer() {
    }

    private static Chronometer _instance;

    public static Chronometer getInstance() {
        if (_instance == null)
            _instance = new Chronometer();
        return _instance;
    }

    private long startTime = 0;
    private long elapsedTime = 0;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        long currentTime = System.currentTimeMillis();
        elapsedTime = (currentTime - startTime) / 1000; // Convert milliseconds to seconds
    }

    public void reset() {
        startTime = 0;
        elapsedTime = 0;
    }

    public long getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }


}
