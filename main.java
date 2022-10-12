import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    static Puzzle puzzle;
    // Randomization seed
    private static final long SEED = 123456789;

    public static void main(String[] args) {
        // testXY();
        expirement();
        // expirement(4);
        /*
         * String folder = "Code_Correctness/";
         * String setStateTest = "setStateTest.txt";
         * String impossible = "impossible.txt";
         * String astarh1 = "astarh1.txt";
         * String astarh2 = "astarh2.txt";
         * String beam = "beam.txt";
         * String divideAndConquer = "divideAndConquer.txt";
         * String fileName = divideAndConquer;
         * try {
         * fromFile(folder + fileName);
         * } catch (FileNotFoundException e) {
         * e.printStackTrace();
         * }
         */
        // Puzzle p = Puzzle.createFromString("325 167 84b");
        // System.out.println(heuristic(p));

    }

    public static void assertEquals(int e, int r) {
        if (e != r) {
            throw new AssertionError("Expeceted " + e + " recieved " + r);
        }
    }

    public static void testXY() {
        // 2x3
        // b12
        // 345
        // b 0 0
        // 1 0 1
        // 2 0 2
        // 3 1 0
        // 4 1 1
        // 5 1 2
        int width = 3;

        assertEquals(0, Puzzle.valFromXY(width, 0, 0));
        assertEquals(1, Puzzle.valFromXY(width, 0, 1));
        assertEquals(2, Puzzle.valFromXY(width, 0, 2));
        assertEquals(3, Puzzle.valFromXY(width, 1, 0));
        assertEquals(4, Puzzle.valFromXY(width, 1, 1));
        assertEquals(5, Puzzle.valFromXY(width, 1, 2));

        assertEquals(0, Puzzle.xFromVal(width, 0));
        assertEquals(0, Puzzle.yFromVal(width, 0));

        assertEquals(0, Puzzle.xFromVal(width, 1));
        assertEquals(1, Puzzle.yFromVal(width, 1));

        assertEquals(0, Puzzle.xFromVal(width, 2));
        assertEquals(2, Puzzle.yFromVal(width, 2));

        assertEquals(1, Puzzle.xFromVal(width, 3));
        assertEquals(0, Puzzle.yFromVal(width, 3));

        assertEquals(1, Puzzle.xFromVal(width, 4));
        assertEquals(1, Puzzle.yFromVal(width, 4));

        assertEquals(1, Puzzle.xFromVal(width, 5));
        assertEquals(2, Puzzle.yFromVal(width, 5));
    }

    public static void expirement() {
        String startingState = "012 345 678";
        int dataSample = 100;

        for (int i = 0; i < dataSample; i++) {

            Puzzle random = Puzzle.createFromString(startingState);
            random.randomizeState(100);

            Puzzle.maxNodes(String.valueOf(100 * i));
            Puzzle.disableMaxNodeError();

            Puzzle astarh1 = random.aStar("h1");
            Puzzle astarh2 = random.aStar("h2");
            Puzzle beam = random.beam("5");
            Puzzle divideAndConquer = random.aStarDivideConquer("h2");

            Puzzle[] algorithms = new Puzzle[] { astarh1, astarh2, beam, divideAndConquer };
            for (int j = 0; j < algorithms.length; j++) {
                System.out.print(algorithms[j].getResult() + " ");
            }
            System.out.println();

        }

    }

    public static void expirement(int n) {

        int dataSample = 10;

        for (int i = 0; i < dataSample; i++) {

            Puzzle random = Puzzle.createFromDimension(n);
            random.randomizeState(1000);

            Puzzle divideAndConquer = random.aStarDivideConquer("h2");
            System.out.print(divideAndConquer.getResult() + " ");

            System.out.println();
        }

    }

    public static void fromFile(String filename) throws FileNotFoundException {
        File inputFile = new File(filename);
        FileInputStream inputStream = new FileInputStream(inputFile);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader input = new BufferedReader(inputStreamReader);
        input.lines().forEach(command -> commandHandler(command));
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void commandHandler(String commandAndInput) {
        Puzzle.enableMaxNodeError();
        String[] commandInput = commandAndInput.split(" ");
        for (int i = 0; i < commandInput.length; i++) {
            System.out.print(commandInput[i] + " ");
        }
        System.out.println();
        switch (commandInput[0]) {
            case "setState": {
                System.out.println("Setting state");

                if ((commandInput[1].equals("n="))) {
                    puzzle = Puzzle.createFromDimension(Integer.parseInt(commandInput[2]));
                    return;
                }

                String state = "";
                for (int i = 1; i < commandInput.length; i++) {
                    state += commandInput[i] + " ";
                }
                puzzle = Puzzle.createFromString(state);
                return;
            }
            case "printState": {
                puzzle.printState();
                return;
            }
            case "move": {
                puzzle.move(commandInput[1]);
                return;
            }
            case "randomizeState": {
                puzzle.randomizeState(commandInput[1], SEED);
                return;
            }
            case "solve": {
                switch (commandInput[1]) {
                    case "A-star": {
                        puzzle = puzzle.aStar(commandInput[2]);
                        return;
                    }
                    case "beam": {
                        puzzle = puzzle.beam(commandInput[2]);
                        return;
                    }
                    case "bfs": {
                        puzzle = puzzle.bfs();
                        return;
                    }
                    case "d&c": {
                        puzzle = puzzle.aStarDivideConquer("h2");
                        return;
                    }
                    default: {
                        System.out.println("Solve command given incorrect parameter.");
                        return;
                    }
                }
            }
            case "maxNodes": {
                Puzzle.maxNodes(commandInput[1]);
                return;
            }
            case "//": {
                return;
            }
            default: {
                System.out.println("Not valid command.");
                return;
            }
        }

    }

}
