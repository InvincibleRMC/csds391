import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class main {

    static Puzzle puzzle = new Puzzle();
    public static void main(String[] args) throws FileNotFoundException {

       // Puzzle p = new Puzzle();
        // Generate Random?

        // maybe try-catch could be more elegant
        String fileName = "input.txt";
        File inputFile = new File(fileName);
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


    public static void commandGenerator(){

    }

}


