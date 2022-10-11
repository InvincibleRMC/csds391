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

        try {
            System.out.println(test());
        } catch (Exception e) {

            e.printStackTrace();
        }
        /*
         * String fileName = "input.txt";
         * fromFile(fileName);
         */
    }

    public static boolean test() throws Exception {
        String startingState = "012 345 678";
        // String startingState15="154 028 736";
        // startingState=startingState15;
        // bfs testing
        boolean test = true;
        for (int i = 0; i < 1; i++) {
            Puzzle random = Puzzle.createFromString(startingState);

            random.randomizeState(100);
            random.printState();

            Puzzle.maxNodes("10");
            System.out.println("Starting Solve\n\n\n");

            long startBFS = System.nanoTime();
            Puzzle bfs = random.bfs();
            long diffBFS = System.nanoTime() - startBFS;

            random.printState();
            bfs.printStateVerbose();
            // System.out.println(p.g);
            if (!bfs.solved()) {
                throw new Exception("Did not solve BFS");
            }
            System.out.println("Bfs done");

            System.out.println("A* h1");
            // A*

            System.out.println("Starting Solve\n\n\n ");

            long startAStarh1 = System.nanoTime();
            Puzzle astarh1 = random.aStar("h1");
            long diffAStarh1 = System.nanoTime() - startAStarh1;

            System.out.print("Starting State");
            random.printState();
            System.out.println("hopefully solved");
            astarh1.printStateVerbose();

            if (!astarh1.solved() || !astarh1.optimal(bfs)) {
                throw new Exception("Did not solve A*1");
            }

            System.out.println("A* h2");
            // A*

            // System.out.print(astarh1.pastPuzzle.toString());
            // astarh2.printState();

            System.out.println("Starting Solve\n\n\n ");

            long startAStarh2 = System.nanoTime();
            Puzzle astarh2 = random.aStar("h2");
            long diffAStarh2 = System.nanoTime() - startAStarh2;

            System.out.print("Starting State");
            random.printState();
            System.out.println("hopefully solved");
            astarh2.printStateVerbose();

            if (!astarh2.solved() || !astarh2.optimal(bfs)) {
                throw new Exception("Did not solve A*2");
            }
            System.out.println("A* h2");

            // beam
            System.out.println("Starting Solve\n\n\n ");

            long startBeam = System.nanoTime();
            Puzzle beam = random.beam("10");
            long diffBeam = System.nanoTime() - startBeam;

            System.out.print("Starting State");
            random.printState();
            System.out.println("hopefully solved");
            beam.printStateVerbose();

            if (!beam.solved()) {
                throw new Exception("Did not solve beam");
            }

            // beam
            System.out.println("Starting Solve\n\n\n ");

            long startNXM = System.nanoTime();
            Puzzle nxm = random.aStarDivideConquer("h2");
            long diffNXM = System.nanoTime() - startNXM;

            System.out.print("Starting State");
            random.printState();
            System.out.println("hopefully solved");
            nxm.printStateVerbose();

            if (!nxm.solved()) {
                throw new Exception("Did not solve customAStar");
            }

            System.out.println(diffBFS + " " + diffAStarh1 + " " + diffAStarh2 + " " + diffBeam + " " + diffNXM);
        }

        return test;
    }

    public static void fromFile(String filename) throws FileNotFoundException {
        // maybe try-catch could be more elegant

        File inputFile = new File(filename);
        FileInputStream inputStream = new FileInputStream(inputFile);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader input = new BufferedReader(inputStreamReader);
        input.lines().forEach(command -> commandHandler(command));
        try {
            input.close();
        } catch (IOException e) {
            // System.out.println("Finished Command File");
        }
        System.out.println(puzzle.solved());
        // System.out.println(puzzle.holeLocation()[0] + " " +
        // puzzle.holeLocation()[1]);

    }

    public static void commandHandler(String commandAndInput) {

        String[] commandInput = commandAndInput.split(" ");
        for (int i = 0; i < commandInput.length; i++) {
            System.out.print(commandInput[i] + " ");
        }
        System.out.println();
        switch (commandInput[0]) {
            case "setState": {
                System.out.println("Setting state");

                String state = "";
                for (int i = 1; i < commandInput.length; i++) {
                    state += commandInput[i];
                }

                puzzle = Puzzle.createFromString(state);
                return;
            }
            case "printState": {
                puzzle.printState();
                return;
            }
            case "move": {
                // System.out.println(commandInput[0]);
                // System.out.println("moving");
                puzzle.move(commandInput[1]);
                return;
            }
            case "randomizeState": {
                puzzle.randomizeState(commandInput[1],SEED);
                return;
            }
            case "solve": {
                switch (commandInput[1]) {
                    case "A-Star": {
                        puzzle.aStar(commandInput[2]);
                        return;
                    }
                    case "beam": {
                        puzzle.beam(commandInput[2]);
                        return;
                    }
                    case "bfs": {
                        puzzle.bfs();
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
            default: {
                System.out.println("Not valid command.");
                return;
            }
        }

    }

    // TODO:
    public static void CSV() {

    }

}
