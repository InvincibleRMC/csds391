import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

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
        width = p.width;
        length = p.length;
        data = p.data;
        pastPuzzle = p.pastPuzzle;
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
    public boolean checkPuzzle(){
        for(int i=0;i<length;i++){
            for(int j=0;j<width;j++){
                if(data[i][j] != i*length+j){
                    return false;
                }
            }
        }
        return true;
    }
    

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
        if(validSwap(swapLocation)){
            swap(holeLocation(), swapLocation);
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
        int temp = data[firstSpot[0]][firstSpot[1]];
        data[firstSpot[0]][firstSpot[1]] = data[secondSpot[0]][secondSpot[1]];
        data[secondSpot[0]][secondSpot[1]] = temp;
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
        return new int[] {-10,-10};
    }
    public void randomizeState(String times) {
        String[] moveOptions = new String[] {"up","left","right","down"};
        int n = Integer.parseInt(times);
        for(int i=0;i<=n;i++){
           move(moveOptions[(int) (Math.random()*4)]);
        }
    }

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

    public boolean visted(Puzzle p){
        return pastPuzzle.contains(p);
    }
    

    public void ezMethod(){
        //Queue q = new Queue<>();
    }
    public void aStar(String string) {
    }

    public void beam(String string) {
    }

    public void maxNodes(String string) {
    }
    
   

}
