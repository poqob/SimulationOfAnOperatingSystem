import java.io.Console;

import Utils.Chronometer;

public class Main {
    public static void main(String[] args) {
        Chronometer chronometer = new Chronometer(); // <- its our time guys. to get program time, chronometer.getElapsedTime();
        chronometer.start();
        while (true) {
            try {
                Thread.sleep(2000); // Sleep for 1 second
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(chronometer.getElapsedTime());
    }
}
