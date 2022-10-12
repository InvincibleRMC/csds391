import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

final class Puzzle {

    private static final String MOVE_UP = "up";
    private static final String MOVE_LEFT = "left";
    private static final String MOVE_RIGHT = "right";
    private static final String MOVE_DOWN = "down";
    private static final String[] moveOptions = new String[] { MOVE_UP, MOVE_LEFT, MOVE_RIGHT, MOVE_DOWN };

    private static int maxNodes = Integer.MAX_VALUE;
    private static boolean printMaxNodeError = true;

    private final int width;
    private final int length;
    private final int[][] data;
    private long diffTime;
    private int nodeCount;
    // moveMadeTo is a String list of moves from the starting state the solved
    // state.
    private final String moveMadeTo;
    // Path Cost
    private final int g;

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
        int g = 0;
        String moveMadeTo = "";
        long diffTime = Long.MIN_VALUE;
        int nodeCount = 0;
        return new Puzzle(width, length, state, moveMadeTo, g, diffTime, nodeCount);
    }

    // Creates a starting Puzzle of n by m
    public static Puzzle createFromDimension(int n, int m) {

        int width = m;
        int length = n;

        int[][] data = new int[length][width];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                data[i][j] = i * width + j;
            }
        }

        int g = 0;
        String moveMadeTo = "";
        long diffTime = Long.MIN_VALUE;
        int nodeCount = 0;
        return new Puzzle(width, length, data, moveMadeTo, g, diffTime, nodeCount);
    }

    // Creates a starting Puzzle of n by n
    public static Puzzle createFromDimension(int n) {
        return createFromDimension(n, n);
    }

    // Creates a resets a Puzzles dimensions
    public static Puzzle resetDimenstion(Puzzle p) {
        return new Puzzle(p.data[0].length, p.data.length, copyData(p), p.moveMadeTo, p.g, p.diffTime, p.nodeCount);
    }

    // Puzzle move
    public static Puzzle move(Puzzle p, String direction) {
        String moveMadeTo = p.moveMadeTo + " " + direction;
        int pathCost = p.g + 1;
        Puzzle puzzle = new Puzzle(p.width, p.length, copyData(p), moveMadeTo, pathCost, p.diffTime, p.nodeCount);
        puzzle.move(direction);
        return puzzle;
    }

    // Puzzle Divide and conquer
    public static Puzzle DivideAndConquer(Puzzle p) {
        int width = p.width;
        int length = p.length;
        if (p.bottomSolved()) {
            length--;
        } else {
            width--;
        }
        return new Puzzle(width, length, copyData(p), p.moveMadeTo, p.g, p.diffTime, p.nodeCount);
    }

    // Copy Data 2d array helper
    private static int[][] copyData(Puzzle p) {
        int[][] data = new int[p.data.length][];
        for (int i = 0; i < p.data.length; i++) {
            data[i] = p.data[i].clone();
        }
        return data;
    }

    // Private generic constructor
    private Puzzle(int width, int length, int[][] data, String moveMadeTo, int g, long diffTime, int nodeCount) {
        this.width = width;
        this.length = length;
        this.data = data;
        this.moveMadeTo = moveMadeTo;
        this.g = g;
        this.diffTime = diffTime;
        this.nodeCount = nodeCount;
    }

    // Getters
    public int getPathCost() {
        return g;
    }

    public String getPath() {
        return moveMadeTo;
    }

    public long getSolveTime() {
        return diffTime;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getMaxNodeCount() {
        return maxNodes;
    }

    public String getResult() {
        return new String(g + " " + diffTime + " " + nodeCount + " " + maxNodes);
    }

    public void setNodeCount(int n) {
        nodeCount = n;
    }

    // Setter
    public void addNode() {
        if ((nodeCount == maxNodes) && (printMaxNodeError)) {
            System.out.println("Maxnode Count exceed");
        }
        nodeCount++;
    }

    // Sets the maximum amount of nodes an algorithm can search
    public static void maxNodes(String string) {
        maxNodes = Integer.parseInt(string);
    }

    public static void disableMaxNodeError() {
        printMaxNodeError = false;
    }

    public static void enableMaxNodeError() {
        printMaxNodeError = true;
    }

    // Returns a string form of the nxn puzzle
    public String toString() {
        StringBuffer stringPuzzle = new StringBuffer("\n");
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                int val = data[i][j];
                stringPuzzle.append(val == 0 ? "b" : val);
                stringPuzzle.append(" ");
            }
            stringPuzzle.append("\n");
        }
        return stringPuzzle.toString();
    }

    // Prints the state as described in project 1
    public void printState() {
        System.out.println(toString());
    }

    // Prints the state as described in project 1 with the path and path cost
    public void printStateVerbose() {
        System.out.println(toString() + "Path cost= " + g + " Path= " + moveMadeTo);
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

    // Checks if a move is valid
    public boolean validMove(String direction) {
        return validSwap(calcSwapLocation(direction));
    }

    // Calculate the swap Location
    private int[] calcSwapLocation(String direction) {
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
                x = Integer.MIN_VALUE;
                y = Integer.MIN_VALUE;
            }
        }
        return new int[] { x, y };
    }

    // Checks if a swap is valid
    public boolean validSwap(int[] swapLocation) {

        int x = swapLocation[0];
        int y = swapLocation[1];

        if (x < 0 || y < 0) {
            return false;
        }
        if (x >= length || y >= width) {
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

    // Finds the blank tile
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
    public void randomizeState(String times, long seed) {
        randomizeState(Integer.parseInt(times), seed);
    }

    // Randomizes a state n times with a given seed
    public void randomizeState(int n, long seed) {
        final Random r = new Random();
        r.setSeed(seed);
        for (int i = 0; i <= n; i++) {
            move(moveOptions[r.nextInt(moveOptions.length)]);
        }
    }

    // Randomizes a state n times
    public void randomizeState(int n) {
        final Random r = new Random();
        for (int i = 0; i <= n; i++) {
            int randomDirection = r.nextInt(moveOptions.length);
            String direction = moveOptions[randomDirection];
            if (validMove(direction))
                move(direction);
        }
    }

    private static interface Heuristic {
        public int heuristic(Puzzle p);
    }

    public static class H1 implements Heuristic {
        // h1 is the number of misplaced tiles
        public int heuristic(Puzzle p) {
            int misplaceTiles = 0;
            for (int i = 0; i < p.length; i++) {
                for (int j = 0; j < p.width; j++) {
                    if (p.data[i][j] == 0)
                        continue;
                    if (p.data[i][j] != i * p.width + j) {
                        misplaceTiles++;
                    }
                }
            }
            return misplaceTiles;
        }
    }

    public static class H2 implements Heuristic {
        // h2 is the manhattan distance of the tiles to their goal locations
        public int heuristic(Puzzle p) {
            int manhattan = 0;
            for (int i = 0; i < p.length; i++) {
                for (int j = 0; j < p.width; j++) {
                    if (p.data[i][j] == 0)
                        continue;
                    int val = p.data[i][j];

                    int y = val % p.width;
                    int x = (val - y) / p.length;
                    manhattan += Math.abs(i - x) + Math.abs(j - y);
                }
            }
            return manhattan;
        }
    }

    public static class BFS implements Heuristic {
        // no Heuristic for bfs
        public int heuristic(Puzzle p) {
            return 0;
        }
    }

    public static class HeuristicComparator implements Comparator<Puzzle> {
        private final Heuristic heuristic;

        HeuristicComparator(Heuristic heurstic) {
            this.heuristic = heurstic;
        }

        @Override
        public int compare(Puzzle p1, Puzzle p2) {
            return p1.g + heuristic.heuristic(p1) <= p2.g + heuristic.heuristic(p2) ? -1 : 1;
        }
    }

    private static final HeuristicComparator h1 = new HeuristicComparator(new H1());
    private static final HeuristicComparator h2 = new HeuristicComparator(new H2());
    private static final HeuristicComparator bfs = new HeuristicComparator(new BFS());

    // Parses the string into a HeuristicComparator
    private HeuristicComparator heuristic(String heuristic) {
        switch (heuristic) {
            case "h1": {
                return h1;
            }
            case "h2": {
                return h2;
            }

            case "bfs": {
                return bfs;
            }
            default: {
                System.out.println("No heuristic given defaulting to h1");
                return h1;
            }
        }
    }

    // Breadth First Search
    public Puzzle bfs() {
        return aStar("bfs");
    }

    // aStar
    public Puzzle aStar(String heuristic) {
      
        setNodeCount(0);
        long startTime = System.nanoTime();
        HeuristicComparator comparator = heuristic(heuristic);
        HashSet<Puzzle> pastPuzzles = new HashSet<Puzzle>(stateSpaceSize());
        PriorityQueue<Puzzle> q = new PriorityQueue<Puzzle>(comparator);
        q.add(this);
        pastPuzzles.add(this);
        addNode();
        while (!q.isEmpty()) {
            Puzzle p = q.poll();
            if (p.solved()) {
                long endTime = System.nanoTime();
                long diffTime = endTime - startTime;
                p.diffTime = diffTime;
                p.nodeCount = this.nodeCount;
                return p;
            }
            Puzzle[] puzzles = p.childrenPuzzles();
            for (int i = 0; i < puzzles.length; i++) {
                if (pastPuzzles.add(puzzles[i])) {
                    addNode();
                    q.add(puzzles[i]);
                }
            }
        }

        System.out.println("Given an invalid starting state");
        return this;
    }

    // beam search
    public Puzzle beam(String string) {

        setNodeCount(0);
        long startTime = System.nanoTime();
        // Normally start with k states
        int startingK = Integer.parseInt(string);
        int k = startingK;
        HashSet<Puzzle> pastPuzzles = new HashSet<Puzzle>(stateSpaceSize());
        PriorityQueue<Puzzle> q = new PriorityQueue<Puzzle>(h2);
        q.add(this);
        pastPuzzles.add(this);
        addNode();
        while (!q.isEmpty()) {

            // At start you only have 1 state so check all the states if there is less than
            // k
            k = startingK < q.size() ? startingK : q.size();

            // Selecting the next k children for a solution
            Puzzle[] nextPuzzle = new Puzzle[k];
            for (int i = 0; i < k; i++) {
                nextPuzzle[i] = q.poll();
                if (nextPuzzle[i].solved()) {
                    long endTime = System.nanoTime();
                    long diffTime = endTime - startTime;
                    nextPuzzle[i].diffTime = diffTime;
                    nextPuzzle[i].nodeCount = this.nodeCount;
                    return nextPuzzle[i];
                }
            }

            for (int j = 0; j < k; j++) {
                Puzzle[] puzzles = nextPuzzle[j].childrenPuzzles();
                for (int i = 0; i < puzzles.length; i++) {
                    if (pastPuzzles.add(puzzles[i])) {
                        addNode();
                        q.add(puzzles[i]);
                    }
                }
            }
        }
        System.out.println("Given an invalid starting state");
        return this;
    }

    // aStarDivideConquer
    // A* which divide and Conquers the stateSpace by solving sides first
    public Puzzle aStarDivideConquer(String heuristic) {

        setNodeCount(0);
        long startTime = System.nanoTime();
        HeuristicComparator comparator = heuristic(heuristic);
        HashSet<Puzzle> pastPuzzles = new HashSet<Puzzle>(stateSpaceSize());
        PriorityQueue<Puzzle> q = new PriorityQueue<Puzzle>(comparator);
        q.add(this);
        pastPuzzles.add(this);
        addNode();
        while (!q.isEmpty()) {
            Puzzle p = q.poll();
            if (p.solved()) {
                long endTime = System.nanoTime();
                long diffTime = endTime - startTime;
                p.diffTime = diffTime;
                p.nodeCount = this.nodeCount;
                return Puzzle.resetDimenstion(p);
            }

            // Stops at 3x2 State
            if ((p.width + p.length > 5)) {

                // If bottom solved shrink the problem or if bottom has been solved and the
                // right has been solved
                if ((p.length >= p.width && p.bottomSolved()) || (p.width > p.length && p.rightSolved())) {
                    Puzzle small = Puzzle.DivideAndConquer(p);
                    pastPuzzles = null;
                    return small.aStarDivideConquer(heuristic);
                }
            }

            Puzzle[] puzzles = p.childrenPuzzles();
            for (int i = 0; i < puzzles.length; i++) {
                if (pastPuzzles.add(puzzles[i])) {
                    addNode();
                    q.add(puzzles[i]);
                }
            }
        }

        System.out.println("Given an invalid starting state");
        return this;
    }

    // returns an Puzzle[] of all valid children Puzzles
    public Puzzle[] childrenPuzzles() {

        LinkedList<Puzzle> puzzles = new LinkedList<Puzzle>();
        for (int i = 0; i < moveOptions.length; i++) {
            if (validMove(moveOptions[i])) {
                puzzles.add(Puzzle.move(this, moveOptions[i]));
            }
        }
        return puzzles.toArray(new Puzzle[puzzles.size()]);
    }

    // returns the stateSpaceSize of a puzzle
    private int stateSpaceSize() {
        BigInteger maxSize = factorial(width * length).divide(BigInteger.valueOf(2));
        // System.out.println(maxSize);
        if (maxSize.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0) {
            return Integer.MAX_VALUE;
        }
        return maxSize.intValue();
    }

    // Calculates the factorial of a number
    private static BigInteger factorial(int n) {
        BigInteger num = BigInteger.valueOf(1);
        for (int i = 1; i < n; i++) {
            num = num.multiply(BigInteger.valueOf(i));
        }
        return num;
    }

    // Generates a hashcode for the Puzzle based on the data
    @Override
    public int hashCode() {
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

    // Deteremines if a puzzle is eqilvanelt by comparing the data arrays
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        Puzzle p = (Puzzle) o;
        if (length != p.length) {
            return false;
        }
        if (width != p.width) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (this.data[i][j] != p.data[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Checks if a puzzle is solved
    public boolean solved() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (data[i][j] != i * data.length + j) {
                    return false;
                }
            }
        }
        return true;
    }

    // Checks if a puzzle bottom is solved
    public boolean bottomSolved() {
        int i = length - 1;
        for (int j = 0; j < width; j++) {
            if (data[i][j] != i * width + j) {
                return false;
            }
        }

        return true;
    }

    // Checks if a puzzle right side is solved
    public boolean rightSolved() {
        for (int i = 0; i < length; i++) {
            int j = width - 1;
            if (data[i][j] != i * width + j) {
                return false;
            }
        }
        return true;
    }

    // Given a bfs puzzle checks if a puzzle is solved optimally
    public boolean optimal(Puzzle bfs) {
        return bfs.g == this.g;
    }
}