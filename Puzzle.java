import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

public class Puzzle {
    
    private int width;
    private int length;
    private int[][] data;
    private HashSet pastPuzzle;
    private String moveMadeTo;
    
    public Puzzle(int width,int length){
        this.width = width;
        this.length = length;
        data = new int[width][length];
        pastPuzzle = new HashSet<>(factorial(width*length)/2);
    }

    public Puzzle(int n){
    width = n;
    length = n;
    data = new int[n][n];
    pastPuzzle = new HashSet<>(factorial(width*length)/2);
    }

    public Puzzle(){
    }

    public Puzzle(Puzzle p){
        this.width = p.width;
        this.length = p.length;
        this.data = p.data;
        this.pastPuzzle = p.pastPuzzle;
    }
    public Puzzle(String stringState){
        
        String[] commandInput = stringState.split(" ");
        for(int i =0;i<commandInput.length;i++){
            System.out.print(commandInput[i] + " ");
        }

        int[][] state = new int[commandInput.length][commandInput[1].length()];
                for(int i =0;i<state.length;i++){

                    String[] stateLine = (commandInput[i].replace('b', '0')).split("");
                    for (int j=0;j<state[0].length;j++){
                        state[i][j] =  Integer.parseInt(stateLine[j]);
                    }
                }
        this.setState(state);
    }

    // for now assuming it will be given legal state
    public void setState(int[][] state){
        length = state.length;
        width = state[0].length;
        data = state;
        pastPuzzle = new HashSet<>(factorial(width*length)/2);
    }

    public int factorial(int n){
        int num= 1;
        for(int i=1;i<n;i++){
            num *= i;
        }
        return num;
    }

    //TODO:
    public boolean checkRight(){
        boolean check = true;
        for(int i=0;i<length;i++){

        }
        return check;
    }
    public boolean checkBottom(){
        return false;
    }
    // solved
    // b12
    // 345
    // 678
    
    

    public void printState(){
        System.out.println(toString());
    }

    public String toString(){
        String stringPuzzle ="\n";

        for(int i =0; i< length;i++){
            for(int j=0; j< width;j++){
                stringPuzzle = stringPuzzle +data[i][j];
            }
            stringPuzzle = stringPuzzle +"\n";
        }
        return stringPuzzle.replace('0', 'b');
    }

    public void move(String direction) {
        int[] swapLocation = new int[] {-1,-1};
        int x = holeLocationX();
        int y = holeLocationY();
        switch (direction){
            case "up":{
                swapLocation = new int[] {x-1,y};
                break;
            }
            case "left":{
                swapLocation = new int[] {x,y-1};
                break;
            }
            case "right":{
                swapLocation = new int[] {x,y+1};
                break;
            }
            case "down":{
                swapLocation = new int[] {x+1,y};
                break;
            }
            default:{
                System.out.println("Not a valid move direction");
                return;
            }
        }
        if(this.validSwap(swapLocation)){
            this.swap(holeLocation(), swapLocation);
        }
        else{
            System.out.println("Cannot move edge in the way.");
        }
    }
    public boolean validSwap(int[] swapLocation){
        int x = swapLocation[0];
        int y = swapLocation[1];
        if(x < 0 || y < 0){
            return false;
        }
        if(x >= width || y >= length){
            return false;
        }
        return true;
    }

    public void swap(int[] firstSpot,int[] secondSpot){
        //System.out.println("swapping");
        int temp = this.data[firstSpot[0]][firstSpot[1]];
        this.data[firstSpot[0]][firstSpot[1]] = this.data[secondSpot[0]][secondSpot[1]];
        this.data[secondSpot[0]][secondSpot[1]] = temp;
    }

    public int holeLocationX(){
        return holeLocation()[0];
    }
    public int holeLocationY(){
        return holeLocation()[1];
    }

    public int[] holeLocation(){
        for(int i=0;i<length;i++){
            for(int j=0; j<width;j++){
                if(data[i][j]==0){
                    return new int[] {i,j};
                }
            }
        }
        //Somehow doesn't find blank spot
        // should probably throw an error
        System.out.println("COULDN'T find hole");
        return new int[] {-10,-10};
    }
    public void randomizeState(String times) {
        String[] moveOptions = new String[] {"up","left","right","down"};
        int n = Integer.parseInt(times);
        for(int i=0;i<=n;i++){
           move(moveOptions[(int) (Math.random()*4)]);
        }
    }

    /* 
    public Puzzle[] generatePuzzle(){
        
        String[] moveOptions = new String[] {"up","left","right","down"};
        Puzzle[] puzzles = new Puzzle[moveOptions.length];
        for(int i =0; i< puzzles.length;i++){
            
                Puzzle p = getPuzzle();
                p.move(moveOptions[i]);
                puzzles[i] = p;
            
        }
       
        return puzzles;
    }
*/
    public boolean visted(){
        return pastPuzzle.contains(this);
    }
    
    /* 
    public void ezMethod(){
        //Queue q = new Queue<>();
    }
    */

    public Puzzle[] childrenPuzzles(){
        System.out.println("getting Children");
        String[] moveOptions = new String[] {"up","left","right","down"};
        LinkedList<Puzzle> puzzles = new LinkedList<Puzzle>();
       // Puzzle[] puzzles = new Puzzle[moveOptions.length];
        for(int i =0; i < moveOptions.length ;i++){
            
                Puzzle movedP = new Puzzle(this);
                Puzzle p = new Puzzle(movedP);
                p.printState();
                movedP.printState();
                movedP.move(moveOptions[i]);
                System.out.println("Moving " + moveOptions[i]);
                p.printState();
                movedP.printState();
                if(!p.equals(movedP) ){
                    puzzles.add(movedP);
                    System.out.println("added");
                }
                //p=null;
                
        }
        System.out.println(puzzles.toString());
        return puzzles.toArray(new Puzzle[puzzles.size()]);
    }
    public void bfs(){
        Queue<Puzzle> q = new LinkedList<>();
        q.add(this);
        while(q.size()>0){
            Puzzle p =q.poll();
            if(p.solved()){
                return;
            }
            Puzzle[] puzzles = p.childrenPuzzles();
            
            System.out.println(puzzles.length);
            System.out.println(puzzles[0].toString());
            for(int i =0;i<puzzles.length;i++){
                if(!puzzles[i].visted()){
                     q.add(puzzles[i]);
                }
            }
        }


        try {
            throw new Exception();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void aStar(String string) {
    }

    public void beam(String string) {
    }

    public void maxNodes(String string) {
    }

    public boolean equals(Puzzle p){
        for(int i=0;i<length;i++){
            for(int j=0;j<width;j++){
                if(this.data[i][j] != p.data[i][j]){
                    return false;
                }
            }
        }
        return true;
    }
    
    // could be special case of equals
    public boolean solved(){
        for(int i=0;i<length;i++){
            for(int j=0;j<width;j++){
                if(data[i][j] != i*length+j){
                    return false;
                }
            }
        }
        return true;
    }

   public boolean optimal(){
    return true;
   }

}
