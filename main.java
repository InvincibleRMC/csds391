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
            test();
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

        boolean bfsTest = true;
        String startingState ="012 345 678";
        for(int i =0;i<10;i++){

            Puzzle p = new Puzzle(startingState);
            p.printState();
           // System.out.println(p.solved());
            p.randomizeState("20");
            System.out.println("\n\n\n Starting Solve");
            p.bfs();
            p.printState();
            if(!p.solved()){
                throw new Exception();
            }
        }



/*
        File inputFile = new File("test.txt");


       





        
        FileInputStream inputStream = new FileInputStream(inputFile);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader input = new BufferedReader(inputStreamReader);
        input.lines().forEach(command -> commandHandler(command));
        try {
            input.close();
        } catch (IOException e) {
           // System.out.println("Finished Command File");
        }
        System.out.println(puzzle.checkPuzzle());
        //System.out.println(puzzle.holeLocation()[0] + " " + puzzle.holeLocation()[1]);
 */
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

    /* 
    public static void commandGenerator(File inputFile) throws IOException{
        FileWriter writer = new FileWriter(inputFile);
        BufferedWriter bWriter = new BufferedWriter(writer);
        String startingState = "setState b12 345 678";
        bWriter.write(startingState, 0, startingState.length());
        int random = 70;
        String randomizeState = "randomizeState " + random;
        bWriter.write(randomizeState, 0, randomizeState.length());


        bWriter.close();

    }
*/
}


