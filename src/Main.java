import java.io.Console;

import Utils.Counter;

public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();
        counter.start();

        // Run the main thread indefinitely to observe the changing seconds value
        while (true) {
            int currentSeconds = counter.getSeconds();
            System.out.println("Main thread observes seconds: " + currentSeconds);
            if (currentSeconds == 5)
                break;

            // Introduce a delay to avoid excessive printing
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
