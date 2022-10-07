import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class main {

    static Puzzle puzzle = new Puzzle();
    public static void main(String[] args) throws FileNotFoundException {

        try {
            System.out.println(test());
        } catch (Exception e) {
            // TODO Auto-generated catch block
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
            System.out.print(random.pastPuzzle.toString());
            // System.out.println(p.solved());

            random.randomizeState(100);
            random = new Puzzle(startingState15);

            Puzzle bfs = new Puzzle(random);
            Puzzle astarh1 = new Puzzle(random);


            System.out.println("Starting Solve\n\n\n");
            bfs.bfs();

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
            astarh1.aStar("h1");
            System.out.print("Starting State");
            random.printState();
            System.out.println("hopefully solved");
            astarh1.printStateVerbose();
            
            if(!astarh1.solved() || !astarh1.optimal(bfs)){
                throw new Exception();
            }

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

                int[][] state = new int[commandInput.length-1][commandInput[1].length()];
                for(int i =0;i<state.length;i++){

                    String[] stateLine = (commandInput[i+1].replace('b', '0')).split("");
                    for (int j=0;j<state[0].length;j++){
                       
                        state[i][j] =  Integer.parseInt(stateLine[j]);
                        //System.out.println(state[i][j]);
                    }
                }
                puzzle.setState(state);
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


