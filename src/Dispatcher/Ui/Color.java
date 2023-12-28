package Dispatcher.Ui;

enum Colors {
    RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE,
}


public class Color {
    private static Colors colour = Colors.WHITE;

    public static String getColor(int number) {
        colour = Colors.values()[number % Colors.values().length];
        switch (colour) {
            case RED:
                return "\u001B[31m";
            case GREEN:
                return "\u001B[32m";
            case YELLOW:
                return "\u001B[33m";
            case BLUE:
                return "\u001B[34m";
            case MAGENTA:
                return "\u001B[35m";
            case CYAN:
                return "\u001B[36m";
            case WHITE:
                return "\u001B[37m";
            default:
                return "\u001B[37m";
        }
    }
}
