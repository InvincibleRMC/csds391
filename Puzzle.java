
public class Puzzle {
    
    private int width;
    private int length;
    private int[][] data;
    
    public Puzzle(int width,int length){
        this.width = width;
        this.length = length;
        data = new int[width][length];
    }

    public Puzzle(int n){
    width = n;
    length = n;
    data = new int[n][n];
    }
    public Puzzle(){
    }

    // for now assuming it will be given legal state
    public void setState(int[][] state){
        length = state.length;
        width = state[0].length;
        data = state;
    }
    // waiting on wether solved at 12345678b or b12345678
    /* 
    public boolean checkRight(){

    }
    public boolean checkBottom(){

    }*/
    public boolean checkPuzzle(){
        
        return true;
    }
    

    public void printState(){
        System.out.println(toString());
    }

    public String toString(){
        String stringPuzzle ="";

        for(int i =0; i< length;i++){
            for(int j=0; j< width;j++){
                stringPuzzle = stringPuzzle +data[i][j];
            }
        }
        return stringPuzzle.replace('0', 'b');
    }


    public void aStar(String string) {
    }

    public void beam(String string) {
    }

    public void randomizeState(String string) {
    }

    public void move(String string) {
    }

    public void maxNodes(String string) {
    }

    



}
