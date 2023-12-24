package Dispatcher.FileOperations;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import Process.Proces;

import java.util.Random;

/*
12,0,1,64,0,0,0,0
arival time, priority, process time, Mbayt, printer, browser, router, cd;
 * */
public class FileOperations {
    // fields
    final private String directory = "";
    final private String fileName = "entry.txt";
    private LinkedList<Proces> processList;

    private ArrayList<Integer> _randList;

    private Random random;
    public static int doneProcessCount;
    private static FileOperations _instance;

    // methods
    private FileOperations() {
        processList = new LinkedList<Proces>();
        _randList = new ArrayList<Integer>();
        random = new Random();
        doneProcessCount=0;
    }

    private void _parseProcesses(String line) {
        Proces proces;

        // Split the string into an array of substrings using ","
        String[] parts = line.replaceAll("\\s+","").split(",");
        // Create an array to store the parsed integers
        int[] processAttributes = new int[parts.length];

        // Parse each substring into an integer
        for (int i = 0; i < parts.length; i++)
            processAttributes[i] = Integer.parseInt(parts[i]);

        // create and add process
        proces = new Proces(pickRandom(), processAttributes[0], processAttributes[1], processAttributes[2],
         processAttributes[3], processAttributes[4], processAttributes[5], processAttributes[6], processAttributes[7]);
        processList.add(proces);

    }

    private int pickRandom() {      // for picking a random pid
        int val = random.nextInt();
        if (_randList.contains(val))
            return pickRandom();
        _randList.add(val);
        return val;
    }

    public static FileOperations getInstance() {
        if (_instance == null)
            _instance = new FileOperations();
        return _instance;
    }

    public void readFile() {
        String line;
        Path filePath = Paths.get(directory, fileName);

        // Check if the file exists before attempting to read it
        if (Files.exists(filePath)) {
            try {
                // Open the file using BufferedReader
                BufferedReader reader = Files.newBufferedReader(filePath);
                // Read the file line by line
                while ((line = reader.readLine()) != null) {
                    _parseProcesses(line); // parse each line.
                }
                // Close the BufferedReader
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found: " + filePath);
        }


    }

    public LinkedList<Proces> getParsedProcesses() {
        return processList;
    }

    /* public int getMaxOverallTime() {
        int maxArrivalTime = 0;
        int timerMax = 0;       // max time for the processes to be executed
    
        for (Proces process : processList) {
            int arrivalTime = process.getArrivalTime();
            int executionTime = process.getExecutionTime();
    
            if (arrivalTime >= maxArrivalTime) {        // get the max arrival time from the list
                maxArrivalTime = arrivalTime;
    
                if (executionTime + arrivalTime >= timerMax)       // get the max overall time from the processes
                    timerMax = executionTime + arrivalTime;
                
            }
        }
        return timerMax;
    } */
    public int numberOfProcesses(){
        int count=0;
        for(Proces proces : processList)
            count++;
        return count;     
    }
}
