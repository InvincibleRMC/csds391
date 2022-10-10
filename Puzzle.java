    import java.util.Comparator;
    import java.util.HashSet;
    import java.util.LinkedList;
    import java.util.PriorityQueue;
    import java.util.Queue;
    import java.util.Random;
    import java.util.concurrent.ThreadLocalRandom;

    final class Puzzle {

       
        private static final String MOVE_UP="up";
        private static final String MOVE_LEFT="left";
        private static final String MOVE_RIGHT="right";
        private static final String MOVE_DOWN="down";
        private static final String[] moveOptions = new String[] { MOVE_UP, MOVE_LEFT, MOVE_RIGHT, MOVE_DOWN };
        
        private static int maxNodes = Integer.MAX_VALUE;

        private final int width;
        private final int length;
        private final int[][] data;
        // moveMadeTo is a String list of moves from the starting state the solved state.
        private final String moveMadeTo;
        // Path Cost
        private final int g;
        private final int nodeCount;

        // Creates a Puzzle based off a given string state
        public static Puzzle createFromString(String stringState) {

            String[] commandInput = stringState.split(" ");
            int[][] state = new int[commandInput.length][commandInput[1].length()];
            for (int i = 0; i < state.length; i++) {

                String[] stateLine = (commandInput[i].replace('b', '0')).split("");
                for (int j = 0; j < state[0].length; j++) {
                    state[i][j] = Integer.parseInt(stateLine[j]);
                }
            }
            int length = state.length;
            int width = state[0].length;
            int g=0;
            int nodeCount = 0;
            String moveMadeTo="";
            return new Puzzle(width, length, state, moveMadeTo, g, nodeCount);
        }

        // Copy Constructor of a Puzzle with 
        public static Puzzle move(Puzzle p, String direction) {
            String moveMadeTo = p.moveMadeTo + " " + direction;
            int pathCost =p.g+1;
            Puzzle puzzle = new Puzzle(p.width,p.length,copyData(p),moveMadeTo,pathCost,p.nodeCount);
            puzzle.move(direction);
            return puzzle;
        }

        // Copy Data 2d array helper
        private static int[][] copyData(Puzzle p){
            int[][] data = new int[p.data.length][];
            for(int i = 0; i < p.data.length; i++){
                data[i] = p.data[i].clone();
            }
            return data;
        }

        // Private generic constructor
        private Puzzle(int width,int length, int[][] data, String moveMadeTo, int g,int nodeCount){
            this.width=width;
            this.length=length;
            this.data = data;
            this.moveMadeTo = moveMadeTo;
            this.g=g;
            this.nodeCount=nodeCount;
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

        //Prints the state as described in project 1
        public void printState() {
            System.out.println(toString());
        }

        //Prints the state as described in project 1 with the path and path cost
        public void printStateVerbose() {
            System.out.println(toString() + "Path cost= " + g + " Path= " + moveMadeTo);
        }

        // Returns a string form of the nxn puzzle
        public String toString() {
            StringBuffer stringPuzzle = new StringBuffer("\n");
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    int val = data[i][j];
                    stringPuzzle.append(val == 0 ? 'b':val);
                }
                stringPuzzle.append("\n");
            }
            return stringPuzzle.toString() ;
        }

        // moves a given direction if valid
        public void move(String direction) {
            int[] swapLocation = calcSwapLocation(direction);
            if (validSwap(swapLocation)) {
                swap(swapLocation);
            } else {
                System.out.println("Cannot move edge in the way.");
            }
        }

        // Checks if a swap is valid
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

        // Performs a swap
        public void swap(int[] swapLocation) {
            int temp = data[holeLocationX()][holeLocationY()];
            data[holeLocationX()][holeLocationY()] = data[swapLocation[0]][swapLocation[1]];
            data[swapLocation[0]][swapLocation[1]] = temp;
        }

        // X cordinator of the blank Tile
        public int holeLocationX() {
            return holeLocation()[0];
        }
        // Y cordinator of the blank Tile
        public int holeLocationY() {
            return holeLocation()[1];
        }

        //Finds the blank tile
        public int[] holeLocation() {
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if (data[i][j] == 0) {
                        return new int[] { i, j };
                    }
                }
            }
           
            // Error if hole is not found
            // Returns an Int to not throw error like if return null
            System.out.println("COULDN'T find hole");
            return new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE };
        }

        // Randomizes a state from txt
        public void randomizeState(String times) {
            randomizeState(Integer.parseInt(times));
        }

        // Randomizes a state n times
        public void randomizeState(int n){
           
            //todo add seeding
            //final Random r = new Random();
            for (int i = 0; i <= n; i++) {
                //r.randInt()
                move(moveOptions[ThreadLocalRandom.current().nextInt(0, moveOptions.length)]);
            }
        }

        // Checks if a move is valid
        public boolean validMove(String direction){
            return validSwap(calcSwapLocation(direction));
        }

        // Calculate the swap Location
        private int[] calcSwapLocation(String direction){
            int x = holeLocationX();
            int y = holeLocationY();
            switch (direction) {
                case MOVE_UP: {
                    x--;
                    break;
                }
                case MOVE_LEFT: {
                    y--;
                    break;
                }
                case MOVE_RIGHT: {
                    y++;
                    break;
                }
                case MOVE_DOWN: {
                    x++;
                    break;
                }
                default: {
                    System.out.println("Not a valid move direction");
                    x=Integer.MIN_VALUE;
                    y=Integer.MIN_VALUE;
                }
            }
            return new int[] { x, y };
        }

        public Puzzle[] childrenPuzzles() {

            if(this.maxNodes == this.nodeCount && this.maxNodes!=-1){
                System.out.println( "Exceeded maximum node count!");
            }

           // String[] moveOptions = new String[] { "up", "left", "right", "down" };
            LinkedList<Puzzle> puzzles = new LinkedList<Puzzle>();
            
            for (int i = 0; i < moveOptions.length; i++) {

                if(validMove(moveOptions[i])){
                    puzzles.add(Puzzle.move(this,moveOptions[i]));
                }
            }
        // System.out.println(puzzles.toString());
            return puzzles.toArray(new Puzzle[puzzles.size()]);
        }


        // returns the stateSpaceSize of a puzzle
        private int stateSpaceSize(){
            return factorial(width*length)/2;
        }

        //Calculates the factorial of a number
        public static int factorial(int n) {
            int num = 1;
            for (int i = 1; i < n; i++) {
                num *= i;
            }
            return num;
        }

        // Breadth First Search
        public Puzzle bfs() {
            HashSet<Puzzle> pastPuzzles = new HashSet<Puzzle>(stateSpaceSize());
            Queue<Puzzle> q = new LinkedList<>();
            q.add(this);
            while (q.size() > 0) {
                Puzzle p = q.poll();
                //p.printStateVerbose();
                if (p.solved()) {
                   return p;
                }
                Puzzle[] puzzles = p.childrenPuzzles();

                for (int i = 0; i < puzzles.length; i++) {
                    if (!pastPuzzles.contains(puzzles[i])) {
                        q.add(puzzles[i]);
                        pastPuzzles.add(puzzles[i]);
                    }
                }
            }

            System.out.println("Given an invalid starting state");
            return this;
        }

        
        private static final Comparator<Puzzle> comparatorh1 = new Comparator<Puzzle>(){
            @Override
            public int compare(Puzzle p1, Puzzle p2) {
                return p1.g+p1.h1() <= p2.g+p2.h1() ? -1:1;
            }
        };
        
        private static final Comparator<Puzzle> comparatorh2 = new Comparator<Puzzle>(){
            @Override
            public int compare(Puzzle p1, Puzzle p2) {
                return p1.g+p1.h2() <= p2.g+p2.h2() ? -1:1;
            }
        };        

        // h1 is the number of misplaced tiles
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

        // h2 is the manhattan distance of the tiles to their goal locations
        public int h2(){
            int manhattan = 0;
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if(data[i][j] == 0) continue;
                    int val = data[i][j];
                    
                    int y = val%data[i].length;
                    int x =  (val - y)/data.length;
                    manhattan += Math.abs(i-x) + Math.abs(j-y) ;
                }
            }
            return manhattan;
        }

        public Puzzle aStar(String heuristic) {
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
                    System.out.println("No heuristic given defaulting to h1");
                    comparator = comparatorh1;
                    break;
                }
            }
            HashSet<Puzzle> pastPuzzles = new HashSet<Puzzle>(stateSpaceSize());
            PriorityQueue<Puzzle> q = new PriorityQueue<Puzzle>(comparator);
            q.add(this);
            while(q.size()>0){
        
                Puzzle p = q.poll();
                
                if (p.solved()) {
                    return p;
                }
                Puzzle[] puzzles = p.childrenPuzzles();
           
                for (int i = 0; i < puzzles.length; i++) {
                    if (!pastPuzzles.contains(puzzles[i])) {
                        q.add(puzzles[i]);
                        pastPuzzles.add(puzzles[i]);
                    }
                }
            }

            System.out.println("Given an invalid starting state");
            return this;
        }

        public Puzzle beam(String string) {
            // Normally start with k states
            int k = Integer.parseInt(string);

            HashSet<Puzzle> pastPuzzles = new HashSet<Puzzle>(stateSpaceSize());
            PriorityQueue<Puzzle> q = new PriorityQueue<Puzzle>(comparatorh2);
            q.add(this);
            while(q.size()>0){
        
                int queueSize = q.size();

                Puzzle[] nextPuzzle = new Puzzle[k];
                for(int i = 0;(i<k) && (i<queueSize);i++){
                    nextPuzzle[i] = q.poll();

                    System.out.println(i);
                    if(nextPuzzle[i].solved()){
                        return nextPuzzle[i];
                    }
                }

                for(int j =0;j< k && (j<queueSize);j++){
                    Puzzle[] puzzles = nextPuzzle[j].childrenPuzzles();
                    for (int i = 0; i < puzzles.length; i++) {
                    
                        if (!pastPuzzles.contains(puzzles[i])) {
                            q.add(puzzles[i]);
                            pastPuzzles.add(puzzles[i]);
                        }
                    }
                }
            }
            System.out.println("Given an invalid starting state");
            return this;
        }      
        
    

    //Sets the maximum amount of nodes an algorithm can search
    public static void maxNodes(String string) {
        maxNodes = Integer.parseInt(string);
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

    // Given a bfs puzzle checks if a puzzle is solved optimally
    public boolean optimal(Puzzle bfs) {
        return  bfs.g == this.g;
    }

}
