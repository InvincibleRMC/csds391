
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

    // for now assuming it will be given legal state
    public void setState(String state){
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
        return stringPuzzle;
    }

    



}
