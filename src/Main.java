import java.io.Console;
import java.util.LinkedList;

import Process.Proces;
import Dispatcher.FileOperations.FileOperations;
import Utils.Chronometer;
import Dispatcher.Dispatcher;

public class Main {
    public static void main(String[] args) {
        Chronometer chronometer = Chronometer.getInstance(); // <- its our time guys. to get program time, chronometer.getElapsedTime();
        FileOperations fileOperations = FileOperations.getInstance();
        LinkedList<Proces> processList;
        // program start
        fileOperations.readFile();
        processList = fileOperations.getParsedProcesses(); //@Mustafa: guys, our parsed processes are this.
        // create dispatcher
        Dispatcher dispatcher = new Dispatcher();
        chronometer.start();
        // Dispatch processes
        dispatcher.dispatchProcesses(processList);
        // program end
        chronometer.stop();
    }
}
