import java.util.LinkedList;
import java.util.Queue;

public class LinkedBinaryTree {
    
    public static boolean boardsAreEqual(char[][] board1, char[][] board2) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board1[i][j] != board2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static Node breadthFirstSearch(Node root, char[][] targetBoard) {
        if (root == null) {
            return null;
        }
        
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            if (boardsAreEqual(current.getBoard(), targetBoard)) {
                return current;
            }
            
            for (Node child : current.getChildren()) {
                queue.add(child);
            }
        }
        
        return null;
    }
}