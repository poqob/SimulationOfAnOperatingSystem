package Utils;

public class Chronometer {

    private long startTime;
    private long elapsedTime;

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
