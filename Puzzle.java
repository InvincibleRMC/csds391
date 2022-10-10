    import java.util.Comparator;
    import java.util.HashSet;
    import java.util.LinkedList;
    import java.util.PriorityQueue;
    import java.util.Queue;
    import java.util.Random;
    import java.util.concurrent.ThreadLocalRandom;

    final class Puzzle {

        private final int width;
        private final int length;
        private int[][] data;
        private HashSet<Puzzle> pastPuzzle;
        private String moveMadeTo ="";
        private int g = 0;
        private int maxNode = Integer.MAX_VALUE;
        private int nodeCount = 0;

        // Copy Constructor of a Puzzle
        public Puzzle(Puzzle p) {
            this.width = p.width;
            this.length = p.length;
            data = new int[p.data.length][];
            for(int i = 0; i < p.data.length; i++){
                data[i] = p.data[i].clone();
            }
            this.pastPuzzle = p.pastPuzzle;
            this.g = p.g;
            this.moveMadeTo = p.moveMadeTo;
        }

        // Copy Constructor of a Puzzle
        public Puzzle(String stringState) {

            String[] commandInput = stringState.split(" ");
            int[][] state = new int[commandInput.length][commandInput[1].length()];
            for (int i = 0; i < state.length; i++) {

                String[] stateLine = (commandInput[i].replace('b', '0')).split("");
                for (int j = 0; j < state[0].length; j++) {
                    state[i][j] = Integer.parseInt(stateLine[j]);
                }
            }
            length = state.length;
            width = state[0].length;
            data = state;
            pastPuzzle = new HashSet<Puzzle>(factorial(width * length) / 2);
        
        }

        public void setState(Puzzle p) {
            int[][] state = p.data;
            data = state;
            pastPuzzle.clear();
            g=p.g;
            moveMadeTo = p.moveMadeTo;
        }

        //Calculates the factorial of a number
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
            String[] moveOptions = new String[] { "up", "left", "right", "down" };
            //todo add seeding
            Random r = new Random();
            for (int i = 0; i <= n; i++) {
                //r.randInt()
                move(moveOptions[ThreadLocalRandom.current().nextInt(0, moveOptions.length)]);
            }
        }

        // Checks if a move is valid
        public boolean validMove(String direction){
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
                    return false;
                }
            }
            int[] swapLocation = new int[] { x, y };
            return validSwap(swapLocation);
        }

        public Puzzle[] childrenPuzzles() {

            if(this.maxNode == this.nodeCount && this.maxNode!=-1){
                System.out.println( "Exceeded maximum node count!");
            }

            String[] moveOptions = new String[] { "up", "left", "right", "down" };
            LinkedList<Puzzle> puzzles = new LinkedList<Puzzle>();
            
            for (int i = 0; i < moveOptions.length; i++) {

                Puzzle movedP = new Puzzle(this);
            
                if(validMove(moveOptions[i])){
                    movedP.move(moveOptions[i]);
                    movedP.moveMadeTo = movedP.moveMadeTo + " " + moveOptions[i];
                    movedP.g++;
                    puzzles.add(movedP);
                }
            }
        // System.out.println(puzzles.toString());
            return puzzles.toArray(new Puzzle[puzzles.size()]);
            }

        // Breadth First Search
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
                    if (!pastPuzzle.contains(puzzles[i])) {
                        q.add(puzzles[i]);
                        pastPuzzle.add(puzzles[i]);
                    }
                }
            }

            try {
                throw new Exception();
            } catch (Exception e) {
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
                    System.out.println("No heuristic given defaulting to h1");
                    comparator = comparatorh1;
                    break;
                }
            }
            PriorityQueue<Puzzle> q = new PriorityQueue<Puzzle>(comparator);
            q.add(this);
            while(q.size()>0){
        
                Puzzle p = q.poll();
                
                if (p.solved()) {
                    this.setState(p);
                    return;
                }
                Puzzle[] puzzles = p.childrenPuzzles();
           
                for (int i = 0; i < puzzles.length; i++) {
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
            // Normally start with k states
            int k = Integer.parseInt(string);

            PriorityQueue<Puzzle> q = new PriorityQueue<Puzzle>(comparatorh2);
            q.add(this);
            while(q.size()>0){
        
                int queueSize = q.size();

                Puzzle[] nextPuzzle = new Puzzle[k];
                for(int i = 0;(i<k) && (i<queueSize);i++){
                    nextPuzzle[i] = q.poll();

                    System.out.println(i);
                    if(nextPuzzle[i].solved()){
                        this.setState(nextPuzzle[i]);
                        return;
                    }
                }

                for(int j =0;j< k&& (j<queueSize);j++){
                    Puzzle[] puzzles = nextPuzzle[j].childrenPuzzles();
                    for (int i = 0; i < puzzles.length; i++) {
                    
                        if (!pastPuzzle.contains(puzzles[i])) {
                            q.add(puzzles[i]);
                            pastPuzzle.add(puzzles[i]);
                        }
                    }
                }
            
                
        }
    }

    //Sets the maximum amount of nodes an algorithm can search
    public void maxNodes(String string) {
        maxNode = Integer.parseInt(string);
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

        // Given a 

        public boolean optimal(Puzzle bfs) {
            return  bfs.g == this.g;
        }

    }
