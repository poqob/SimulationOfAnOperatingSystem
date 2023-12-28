package Dispatcher.Ui;

import java.util.List;

import Process.Proces;
///future work: ADVANCED UI PROGRAM, in early stages of project i only do what is wanted.
// Listen queues with time
// call second by second
// split console into three parts.

// part0: contains arriving processes.

// part1: contains queued processes.

// part2: contains processing processes.
// part3: which is in the same column with part2 contains done processes.

public class Ui {

    private String res;

    private Ui() {
    }

    private static Ui _instance;

    public static Ui getInstance() {
        if (_instance == null)
            _instance = new Ui();
        return _instance;
    }

    public void write(Proces proces) {
        res = Color.getColor(proces.getPid()) + proces.toString();
        // future updates.
        System.out.println(res);
    }

    public void write(List<Proces> proceses) {
        for (Proces proces :
                proceses) {
            write(proces);
        }
    }

    // TODO:
    public void system_status() {
    }


}
