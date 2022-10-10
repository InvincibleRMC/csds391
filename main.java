import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;


public class main {

    static Puzzle puzzle;
    public static void main(String[] args) throws FileNotFoundException {

        try {
            System.out.println(test());
        } catch (Exception e) {
        
            e.printStackTrace();
        }
        /* 
        String fileName = "input.txt";
        fromFile(fileName);
        */
    }

    
    public static boolean test() throws Exception{
        String startingState ="012 345 678";
        String startingState15="154 028 736"; 
        //startingState=startingState15;
        //bfs testing
        boolean bfsTest = true;
        for(int i =0;i<1;i++){
            Puzzle random = new Puzzle(startingState);

            //p.printState();
            // System.out.println(p.solved());

            random.randomizeState(100);
            random = new Puzzle(startingState15);

            Puzzle bfs = new Puzzle(random);
            Puzzle astarh1 = new Puzzle(random);
            Puzzle astarh2 = new Puzzle(random);
            Puzzle beam = new Puzzle(random);

            System.out.println("Starting Solve\n\n\n");

            long startBFS = System.nanoTime();
            bfs.bfs();
            long diffBFS = System.nanoTime()-startBFS;

            random.printState();
            bfs.printStateVerbose();
            //System.out.println(p.g);
            if(!bfs.solved()){
                throw new Exception();
            }
            System.out.println("Bfs done");

            System.out.println("A* h1");
            //A*
            
           // System.out.print(astarh1.pastPuzzle.toString());
            astarh1.printState();
            // System.out.println(p.solved());
            //Puzzle random = new Puzzle(p);

            System.out.println("Starting Solve\n\n\n ");

            long startAStarh1 =System.nanoTime();
            astarh1.aStar("h1");
            long diffAStarh1 = System.nanoTime()-startAStarh1;
            
            System.out.print("Starting State");
            random.printState();
            System.out.println("hopefully solved");
            astarh1.printStateVerbose();
            
            if(!astarh1.solved() || !astarh1.optimal(bfs)){
                throw new Exception();
            }

            System.out.println("A* h2");
            //A*
            
           // System.out.print(astarh1.pastPuzzle.toString());
            astarh2.printState();

            System.out.println("Starting Solve\n\n\n ");

            long startAStarh2 = System.nanoTime();
            astarh2.aStar("h2");
            long diffAStarh2 = System.nanoTime()-startAStarh2;

            System.out.print("Starting State");
            random.printState();
            System.out.println("hopefully solved");
            astarh2.printStateVerbose();
            
            if(!astarh2.solved() || !astarh2.optimal(bfs)){
                throw new Exception();
            }
            System.out.println("A* h2");
            //A*
            
           // System.out.print(astarh1.pastPuzzle.toString());
           beam.printState();

            System.out.println("Starting Solve\n\n\n ");

            long startBeam = System.nanoTime();
            beam.beam("5");
            long diffBeam = System.nanoTime()-startBeam;

            System.out.print("Starting State");
            random.printState();
            System.out.println("hopefully solved");
            beam.printStateVerbose();
            
            if(!beam.solved()){
                throw new Exception();
            }





            System.out.println(diffBFS + " " + diffAStarh1 + " " + diffAStarh2 + " " + diffBeam);
        }
        

        return bfsTest;
    }


    public static void fromFile(String filename) throws FileNotFoundException{
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
        //System.out.println(puzzle.holeLocation()[0] + " " + puzzle.holeLocation()[1]);
        
    }

    public static void commandHandler(String commandAndInput){

        String[] commandInput = commandAndInput.split(" ");
        for(int i =0;i<commandInput.length;i++){
            System.out.print(commandInput[i] + " ");
        }
       System.out.println();
        switch(commandInput[0]){
            case "setState":{
                System.out.println("Setting state");

                String state ="";
                for(int i=1;i<commandInput.length;i++){
                    state += commandInput[i];
                }
                
                puzzle = new Puzzle(state);
                return;
            }
            case "printState":{
                puzzle.printState();
                return;
            }
            case "move":{
                //System.out.println(commandInput[0]);
                //System.out.println("moving");
                puzzle.move(commandInput[1]);
                return;
            }
            case "randomizeState":{
                puzzle.randomizeState(commandInput[1]);
                return;
            }
            case "solve":{
                switch(commandInput[1]){
                    case "A-Star":{
                    puzzle.aStar(commandInput[2]);
                    return;
                    }
                    case "beam":{
                    puzzle.beam(commandInput[2]);
                    return;
                    }
                    case "bfs":{
                        puzzle.bfs();
                    return;
                    }
                    default:{
                        System.out.println("Solve command given incorrect parameter.");
                        return;
                    }
                }
            }
            case "maxNodes":{
                puzzle.maxNodes(commandInput[1]);
                return;
            }
            default:{
                System.out.println("Not valid command.");
                return;
            } 
        }

    }

}


