import java.util.ArrayList;
import java.util.List;

public class Node {
    private char[][] board;
    private double weight;
    private List<Node> children;
    private char currentPlayer;
    
    public Node(char[][] board, char currentPlayer) {
        this.board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = board[i][j];
            }
        }
        this.currentPlayer = currentPlayer;
        this.weight = 0.0;
        this.children = new ArrayList<>();
    }
    
    public char[][] getBoard() {
        return board;
    }
    
    public void setBoard(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public List<Node> getChildren() {
        return children;
    }
    
    public void addChild(Node child) {
        this.children.add(child);
    }
    
    public char getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void setCurrentPlayer(char currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}