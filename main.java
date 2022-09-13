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
        } catch (IOException e) {}
    }

    public static void commandHandler(String commandAndInput){

        String[] commandInput = commandAndInput.split(" ");
        
        switch(commandInput[0]){
            case "setState":{
                System.out.println("Setstating");

                int[][] state = new int[commandInput.length-1][commandInput[1].length()];
                
                for(int i =0;i<state.length;i++){

                    String[] stateLine = (commandInput[i+1].replace('b', '0')).split("");
                    for (int j=0;j<state[0].length;j++){
                       
                        state[i][j] =  Integer.parseInt(stateLine[j]);
                        System.out.println(state[i][j]);
                    }
                }
                
                puzzle.setState(state);
            }
            case "printState":{
                puzzle.printState();
            }
            case "move":{
                puzzle.move(commandInput[1]);
            }
            case "randomizeState":{
                puzzle.randomizeState(commandInput[1]);
            }
            case "solve":{
                switch(commandInput[1]){
                    case "A-Star":{
                    puzzle.aStar(commandInput[2]);
                    }
                    case "beam":{
                    puzzle.beam(commandInput[2]);
                    }
                    default:{
                        throw new InputMismatchException();
                    }
                }
            }
            case "maxNodes":{
                puzzle.maxNodes(commandInput[1]);
            }
            default:{
                throw new InputMismatchException(); 
            } 
        }

    }


    public static void commandGenerator(){

    }

}


