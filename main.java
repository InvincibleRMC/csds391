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

    static Puzzle puzzle;
    public static void main(String[] args) throws FileNotFoundException {

        

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
                puzzle.setState(commandInput[1]);
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


