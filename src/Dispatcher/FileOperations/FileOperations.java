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
    // alanlar
    final private String directory = "";
    final private String fileName = "entry.txt";
    private LinkedList<Proces> processList;

    private ArrayList<Integer> _randList;

    private Random random;
    private static FileOperations _instance;

    // metotlar
    private FileOperations() {
        processList = new LinkedList<Proces>();
        _randList = new ArrayList<Integer>();
        random = new Random();
    }

    private void _parseProcesses(String line) {
        Proces proces;

        // string'leri substring'lere bolup bir array'e at
        String[] parts = line.replaceAll("\\s+", "").split(",");
        // proseslerin degerlerini atacagimiz array
        int[] processAttributes = new int[parts.length];

        // her substring'i integer'a cevir
        for (int i = 0; i < parts.length; i++)
            processAttributes[i] = Integer.parseInt(parts[i]);

        // process olustur ve listeye ekle
        proces = new Proces(pickRandom(), processAttributes[0], processAttributes[1], processAttributes[2],
                processAttributes[3], processAttributes[4], processAttributes[5], processAttributes[6], processAttributes[7]);
        processList.add(proces);

    }

    private int pickRandom() {      // rastgele bir pid almak icin
        int val = Math.abs(random.nextInt());
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

        // Dosya var mi kontrol et
        if (Files.exists(filePath)) {
            try {
                // BufferedReader ile dosyayi ac
                BufferedReader reader = Files.newBufferedReader(filePath);
                // dosyayi satir satir oku
                while ((line = reader.readLine()) != null) {
                    _parseProcesses(line); // her satiri prosese cevir
                }
                // BufferedReader'i kapat
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

    public int numberOfProcesses() {
        int count = 0;
        for (Proces proces : processList)
            count++;
        return count;
    }
}
