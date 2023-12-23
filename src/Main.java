import java.io.Console;
import java.util.LinkedList;
import Process.Proces;
import Dispatcher.FileOperations.FileOperations;
import Utils.Chronometer;
import Dispatcher.Dispatcher;

public class Main {
    public static void main(String[] args) {
        Chronometer chronometer = new Chronometer(); // <- its our time guys. to get program time, chronometer.getElapsedTime();
        chronometer.start();
        // program start
        FileOperations fileOperations = FileOperations.getInstance();
        fileOperations.readFile();
        LinkedList<Proces> processList = fileOperations.getParsedProcesses(); //@Mustafa: guys, our parsed processes are this.

		Dispatcher dispatcher = new Dispatcher();
        // Dispatch processes
        dispatcher.dispatchProcesses(processList, chronometer);
		
        // program end
        chronometer.stop();
    }
}
