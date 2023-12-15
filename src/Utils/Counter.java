package Utils;

import java.util.Timer;
import java.util.TimerTask;

public class Counter {

    private int seconds;
    private final Object lock = new Object();

    public Counter() {
        this.seconds = 0;
    }

    public void start() {
        Timer timer = new Timer(true);

        // Schedule a task to increment the seconds every second
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (lock) {
                    incrementSeconds();
                    //System.out.println("Seconds: " + getSeconds());
                }
            }
        }, 0, 1000); // Initial delay: 0 milliseconds, Repeat every 1000 milliseconds (1 second)
    }

    public int getSeconds() {
        synchronized (lock) {
            return seconds;
        }
    }

    private void incrementSeconds() {
        synchronized (lock) {
            seconds++;
        }
    }
}
