import java.util.LinkedList;

import Process.Proces;
import Dispatcher.FileOperations.FileOperations;
import Dispatcher.Dispatcher;

public class Main {
    public static void main(String[] args) {
        FileOperations fileOperations = FileOperations.getInstance();
        LinkedList<Proces> processList;
        // program baslasin
        fileOperations.readFile();
        processList = fileOperations.getParsedProcesses(); //@Mustafa: guys, our parsed processes are this.
        // gorevlendirici yaratilsin
        Dispatcher dispatcher = new Dispatcher();
        // gorevlendirici calissin
        dispatcher.dispatchProcesses(processList, fileOperations.numberOfProcesses());
        // program sonlandi
        System.out.println("No process left..");
    }
}
