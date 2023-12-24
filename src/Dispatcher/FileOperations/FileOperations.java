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

    private static FileOperations _instance;

    // methods
    private FileOperations() {
        processList = new LinkedList<Proces>();
        _randList = new ArrayList<Integer>();
        random = new Random();
    }

    private void _parseProcesses(String line) {
        Proces proces;

        // Split the string into an array of substrings using ","
        String[] parts = line.replaceAll("\\s+","").split(",");
        // Create an array to store the parsed integers
        int[] integers = new int[parts.length];

        // Parse each substring into an integer
        for (int i = 0; i < parts.length; i++)
            integers[i] = Integer.parseInt(parts[i]);

        // create and add process
        proces = new Proces(pickRandom(), integers[0], integers[1], integers[2], integers[3], integers[4], integers[5], integers[6], integers[7]);
        processList.add(proces);

    }

    private int pickRandom() {
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


}
