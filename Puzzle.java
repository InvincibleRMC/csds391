import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Puzzle {

    private int width;
    private int length;
    private int[][] data;
    public HashSet<Puzzle> pastPuzzle;
    private String moveMadeTo ="";
    private int g =0;

    public Puzzle(int width, int length) {
        this.width = width;
        this.length = length;
        data = new int[width][length];
        pastPuzzle = new HashSet<Puzzle>(factorial(width * length) / 2);
    }

    public Puzzle(int n) {
        width = n;
        length = n;
        data = new int[n][n];
        pastPuzzle = new HashSet<Puzzle>(factorial(width * length) / 2);
    }

    public Puzzle() {
    }

    public Puzzle(Puzzle p) {
        this.width = p.width;
        this.length = p.length;
        data = new int[p.data.length][];
        for(int i = 0; i < p.data.length; i++){
            data[i] = p.data[i].clone();
        }
        //this.data = p.data.clone();
        this.pastPuzzle = p.pastPuzzle;
        this.g = p.g;
        this.moveMadeTo = p.moveMadeTo;
    }

    public Puzzle(String stringState) {

        String[] commandInput = stringState.split(" ");
        for (int i = 0; i < commandInput.length; i++) {
            System.out.print(commandInput[i] + " ");
        }

        int[][] state = new int[commandInput.length][commandInput[1].length()];
        for (int i = 0; i < state.length; i++) {

            String[] stateLine = (commandInput[i].replace('b', '0')).split("");
            for (int j = 0; j < state[0].length; j++) {
                state[i][j] = Integer.parseInt(stateLine[j]);
            }
        }
        this.setState(state);
    }

    // for now assuming it will be given legal state
    public void setState(int[][] state) {
        length = state.length;
        width = state[0].length;
        data = state;
        pastPuzzle = new HashSet<Puzzle>(factorial(width * length) / 2);
    }
    public void setState(Puzzle p) {
        int[][] state = p.data;
        length = state.length;
        width = state[0].length;
        data = state;
        pastPuzzle.clear();
        g=p.g;
        moveMadeTo = p.moveMadeTo;
    }

    public void resetHashSet(){
        pastPuzzle = new HashSet<Puzzle>(factorial(width * length) / 2);
    }

    public int factorial(int n) {
        int num = 1;
        for (int i = 1; i < n; i++) {
            num *= i;
        }
        return num;
    }

    // TODO:
    public boolean checkRight() {
        boolean check = true;
        for (int i = 0; i < length; i++) {

        }
        return check;
    }

    public boolean checkBottom() {
        return false;
    }
    // solved
    // b12
    // 345
    // 678

    public void printState() {
        System.out.println(toString());
    }

    public void printStateVerbose() {
        System.out.println(toString() + "Path length= " + g + " Path= " + moveMadeTo);
    }

    public String toString() {
        String stringPuzzle = "\n";

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                stringPuzzle = stringPuzzle + data[i][j];
            }
            stringPuzzle = stringPuzzle + "\n";
        }
        return stringPuzzle.replace('0', 'b');
    }

    public void move(String direction) {

        int x = holeLocationX();
        int y = holeLocationY();
        switch (direction) {
            case "up": {
                x--;
                break;
            }
            case "left": {
                y--;
                break;
            }
            case "right": {
                y++;
                break;
            }
            case "down": {
                x++;
                break;
            }
            default: {
                System.out.println("Not a valid move direction");
                return;
            }
        }
        int[] swapLocation = new int[] { x, y };
        if (validSwap(swapLocation)) {
            swap(swapLocation);
        } else {
            System.out.println("Cannot move edge in the way.");
        }
    }

    public boolean validSwap(int[] swapLocation) {
        int x = swapLocation[0];
        int y = swapLocation[1];
        if (x < 0 || y < 0) {
            return false;
        }
        if (x >= width || y >= length) {
            return false;
        }
        return true;
    }

    public void swap(int[] swapLocation) {
        // System.out.println("swapping");
        int temp = data[holeLocationX()][holeLocationY()];
        data[holeLocationX()][holeLocationY()] = data[swapLocation[0]][swapLocation[1]];
        data[swapLocation[0]][swapLocation[1]] = temp;
    }

    public int holeLocationX() {
        return holeLocation()[0];
    }

    public int holeLocationY() {
        return holeLocation()[1];
    }

    public int[] holeLocation() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (data[i][j] == 0) {
                    return new int[] { i, j };
                }
            }
        }
        // Somehow doesn't find blank spot
        // should probably throw an error
        System.out.println("COULDN'T find hole");
        return new int[] { -10, -10 };
    }

    public void randomizeState(String times) {
        randomizeState(Integer.parseInt(times));
    }

    public void randomizeState(int n){
        String[] moveOptions = new String[] { "up", "left", "right", "down" };
        //todo add seeding
        Random r = new Random();
        for (int i = 0; i <= n; i++) {
            //r.randInt()
            move(moveOptions[ThreadLocalRandom.current().nextInt(0, moveOptions.length)]);
        }
    }

    /*
     * public Puzzle[] generatePuzzle(){
     * 
     * String[] moveOptions = new String[] {"up","left","right","down"};
     * Puzzle[] puzzles = new Puzzle[moveOptions.length];
     * for(int i =0; i< puzzles.length;i++){
     * 
     * Puzzle p = getPuzzle();
     * p.move(moveOptions[i]);
     * puzzles[i] = p;
     * 
     * }
     * 
     * return puzzles;
     * }
     */

     //TODO Delete
    public boolean visted() {
        return pastPuzzle.contains(this);
    }

    /*
     * public void ezMethod(){
     * //Queue q = new Queue<>();
     * }
     */

    public Puzzle[] childrenPuzzles() {
       // System.out.println("getting Children");
        String[] moveOptions = new String[] { "up", "left", "right", "down" };
        LinkedList<Puzzle> puzzles = new LinkedList<Puzzle>();
        // Puzzle[] puzzles = new Puzzle[moveOptions.length];
        for (int i = 0; i < moveOptions.length; i++) {

            Puzzle movedP = new Puzzle(this);
           
            movedP.move(moveOptions[i]);
           
            if (!this.equals(movedP)) {
                movedP.moveMadeTo = movedP.moveMadeTo + " " + moveOptions[i];
                movedP.g++;
                puzzles.add(movedP);
               
            }
        }
       // System.out.println(puzzles.toString());
        return puzzles.toArray(new Puzzle[puzzles.size()]);
    }

    public void bfs() {
        Queue<Puzzle> q = new LinkedList<>();
        q.add(this);
        while (q.size() > 0) {
            Puzzle p = q.poll();
            //System.out.println("Current puzzle is" + p.toString());
            if (p.solved()) {
                this.setState(p);
                return;
            }
            Puzzle[] puzzles = p.childrenPuzzles();
            for (int i = 0; i < puzzles.length; i++) {
                //puzzles[i].printStateVerbose();
                if (!pastPuzzle.contains(puzzles[i])) {
                    q.add(puzzles[i]);
                    pastPuzzle.add(puzzles[i]);
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

    
        Comparator<Puzzle> comparatorh1 = new Comparator<Puzzle>(){

            @Override
            public int compare(Puzzle p1, Puzzle p2) {
                return p1.g+p1.h1() <= p2.g+p2.h1() ? -1:1;
            }
        };
        Comparator<Puzzle> comparatorh2 = new Comparator<Puzzle>(){

            @Override
            public int compare(Puzzle p1, Puzzle p2) {
                return p1.g+p1.h2() <= p2.g+p2.h2() ? -1:1;
            }
        };
        

    public int h1(){
        int misplaceTiles = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if(data[i][j] == 0) continue;
                if (data[i][j] != i * length + j) {
                    misplaceTiles++;
                }
            }
        }
        return misplaceTiles;
    }
    public int h2(){
        return -1;
    }

    public void aStar(String heuristic) {
        Comparator<Puzzle> comparator;
        switch(heuristic){
            case "h1":{
                comparator = comparatorh1;
                break;
            }
            case "h2":{
                comparator = comparatorh2;
                break;
            }
            default:{
                System.out.println("No heuristic fiven defaulting to h1");
                comparator = comparatorh1;
                break;
            }
        }
        PriorityQueue<Puzzle> q = new PriorityQueue<Puzzle>(comparator);
        q.add(this);
        while(q.size()>0){
       
            Puzzle p = q.poll();
            //System.out.println("Current puzzle is");
           // p.printStateVerbose();
            if (p.solved()) {
                //System.out.println("found " + p.toString());
                this.setState(p);
                return;
            }
            Puzzle[] puzzles = p.childrenPuzzles();
          //  p.printState();
           // System.out.println(puzzles.length);
            //System.out.println(puzzles[0].toString());
            for (int i = 0; i < puzzles.length; i++) {
                //puzzles[i].printState();
               // System.out.println(pastPuzzle.toString());
                if (!pastPuzzle.contains(puzzles[i])) {
                    q.add(puzzles[i]);
                    pastPuzzle.add(puzzles[i]);
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

    public void beam(String string) {
    }

    public void maxNodes(String string) {
    }

    @Override
    public int hashCode(){
        if (data == null) {
            return 0;
        }
        int result = 1;
        int h = data.length;
        int w = data[0].length;
        int value = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                value = data[i][j];
                int elementHash = (value ^ (value >>> 32));
                result = 31 * result + elementHash;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this){
            return true;
        }
        if(o == null || o.getClass() != this.getClass()){
            return false;
        }

        Puzzle p = (Puzzle) o;
        if(length!=p.length){
            return false;
        }
        if(width!=p.width){
            return false;
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (this.data[i][j] != p.data[i][j]) {
                    return false;
                }
            }
        }
        return true ;
    }

    // could be special case of equals
    public boolean solved() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (data[i][j] != i * length + j) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean optimal(Puzzle bfs) {
       // random.bfs();
        return bfs.moveMadeTo.equals(this.moveMadeTo) && bfs.g == this.g;
    }

}
