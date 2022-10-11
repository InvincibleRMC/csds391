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

        //expirement();
        //expirement(4);
        String folder = "Code_Correctness/";
        String fileName = "setStateTest.txt";
        try {
            fromFile(folder + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         
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
            Puzzle beam = random.beam("25");
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
        Puzzle.enableMaxNodeError();
        String[] commandInput = commandAndInput.split(" ");
        for (int i = 0; i < commandInput.length; i++) {
            System.out.print(commandInput[i] + " ");
        }
        System.out.println();
        switch (commandInput[0]) {
            case "setState": {
                System.out.println("Setting state");
               
                if((commandInput[1].equals("n="))){
                    puzzle= Puzzle.createFromDimension(Integer.parseInt(commandInput[2]));
                    return;
                }    

                String state = "";
                for (int i = 1; i < commandInput.length; i++) {
                    state += commandInput[i]+ " ";
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
                puzzle.randomizeState(commandInput[1], SEED);
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

}
