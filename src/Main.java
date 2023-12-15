import java.io.Console;
import java.util.LinkedList;

import Process.Proces;
import Dispatcher.FileOperations.FileOperations;
import Utils.Chronometer;

public class Main {
    public static void main(String[] args) {
        Chronometer chronometer = new Chronometer(); // <- its our time guys. to get program time, chronometer.getElapsedTime();
        chronometer.start();
        // program start
        FileOperations fileOperations = FileOperations.getInstance();
        fileOperations.readFile();
        LinkedList<Proces> processList = fileOperations.getParsedProcesses(); //@Mustafa: guys, our parsed processes are this.

        // program end
        chronometer.stop();
    }
}
