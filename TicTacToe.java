import java.util.Scanner;

public class TicTacToe {
    private char[][] board;
    private Node gameTree;
    private Node currentNode;
    
    public TicTacToe() {
        board = new char[3][3];
        initializeBoard();
        gameTree = createRoot();
        fillTree(gameTree);
        calculateWeights(gameTree);
        currentNode = gameTree;
    }
    
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }
    
    private Node createRoot() {
        return new Node(board, 'X');
    }
    
    private void fillTree(Node node) {
        if (isGameOver(node.getBoard())) {
            return;
        }
        
        char[][] currentBoard = node.getBoard();
        char currentPlayer = node.getCurrentPlayer();
        char nextPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentBoard[i][j] == ' ') {
                    char[][] newBoard = copyBoard(currentBoard);
                    newBoard[i][j] = currentPlayer;
                    Node child = new Node(newBoard, nextPlayer);
                    node.addChild(child);
                    fillTree(child);
                }
            }
        }
    }
    
    private void calculateWeights(Node node) {
        if (node.getChildren().isEmpty()) {
            // Terminal node
            char winner = checkWin(node.getBoard());
            if (winner == 'X') {
                node.setWeight(100.0);
            } else if (winner == 'O') {
                node.setWeight(0.0);
            } else {
                node.setWeight(50.0); // Tie
            }
            return;
        }
        
        // Calculate weights for children first
        for (Node child : node.getChildren()) {
            calculateWeights(child);
        }
        
        // Minimax backup: X maximizes, O minimizes
        if (node.getCurrentPlayer() == 'X') {
            // X wants to maximize score
            double maxWeight = Double.NEGATIVE_INFINITY;
            for (Node child : node.getChildren()) {
                maxWeight = Math.max(maxWeight, child.getWeight());
            }
            node.setWeight(maxWeight);
        } else {
            // O wants to minimize X's score
            double minWeight = Double.POSITIVE_INFINITY;
            for (Node child : node.getChildren()) {
                minWeight = Math.min(minWeight, child.getWeight());
            }
            node.setWeight(minWeight);
        }
    }
    
    private char[][] copyBoard(char[][] original) {
        char[][] copy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }
    
    private char checkWin(char[][] board) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return board[0][j];
            }
        }
        
        // Check diagonals
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }
        
        return ' '; // No winner
    }
    
    private boolean isBoardFull(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isGameOver(char[][] board) {
        return checkWin(board) != ' ' || isBoardFull(board);
    }
    
    public void playAgainstAI() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to Tic Tac Toe!");
        System.out.println("You are X, AI is O");
        System.out.println("Commands: 'move row col' to make a move, 'possible' to see possible moves, 'best' to see best move");
        
        while (!isGameOver(currentNode.getBoard())) {
            printBoard(currentNode.getBoard());
            
            if (currentNode.getCurrentPlayer() == 'X') {
                // Human player's turn
                System.out.print("Enter your command: ");
                String input = scanner.nextLine().trim();
                
                if (input.equals("possible")) {
                    displayPossibleMoves();
                } else if (input.equals("best")) {
                    showBestMove();
                } else if (input.startsWith("move")) {
                    String[] parts = input.split(" ");
                    if (parts.length == 3) {
                        try {
                            int row = Integer.parseInt(parts[1]);
                            int col = Integer.parseInt(parts[2]);
                            Node newNode = makeMove(currentNode, row, col, 'X');
                            if (newNode != null) {
                                currentNode = newNode;
                            } else {
                                System.out.println("Invalid move! Try again.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input! Use format: move row col");
                        }
                    } else {
                        System.out.println("Invalid input! Use format: move row col");
                    }
                } else {
                    System.out.println("Invalid command! Use 'move row col', 'possible', or 'best'");
                }
            } else {
                // AI's turn
                System.out.println("AI is making a move...");
                Node bestMove = getBestMove(currentNode);
                if (bestMove != null) {
                    currentNode = bestMove;
                }
            }
        }
        
        printBoard(currentNode.getBoard());
        announceWinner(currentNode.getBoard());
        scanner.close();
    }
    
    private void displayPossibleMoves() {
        System.out.println("Possible moves and their weights:");
        for (int i = 0; i < currentNode.getChildren().size(); i++) {
            Node child = currentNode.getChildren().get(i);
            System.out.println("Move " + (i + 1) + " (Weight: " + String.format("%.1f", child.getWeight()) + "):");
            printBoard(child.getBoard());
            System.out.println();
        }
    }
    
    private void showBestMove() {
        Node bestMove = getBestMove(currentNode);
        if (bestMove != null) {
            System.out.println("Best move (Weight: " + String.format("%.1f", bestMove.getWeight()) + "):");
            printBoard(bestMove.getBoard());
        }
    }
    
    private Node makeMove(Node node, int row, int col, char player) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || node.getBoard()[row][col] != ' ') {
            return null;
        }
        
        // First try to find the child directly
        for (Node child : node.getChildren()) {
            char[][] childBoard = child.getBoard();
            char[][] expectedBoard = copyBoard(node.getBoard());
            expectedBoard[row][col] = player;
            
            if (LinkedBinaryTree.boardsAreEqual(childBoard, expectedBoard)) {
                return child;
            }
        }
        
        // Fallback to breadth-first search if direct child lookup fails
        char[][] newBoard = copyBoard(node.getBoard());
        newBoard[row][col] = player;
        return LinkedBinaryTree.breadthFirstSearch(gameTree, newBoard);
    }
    
    private Node getBestMove(Node node) {
        if (node.getChildren().isEmpty()) {
            return null;
        }
        
        Node bestMove = node.getChildren().get(0);
        for (Node child : node.getChildren()) {
            if (node.getCurrentPlayer() == 'X') {
                // Maximize for X
                if (child.getWeight() > bestMove.getWeight()) {
                    bestMove = child;
                }
            } else {
                // Minimize for O (AI wants to minimize X's score)
                if (child.getWeight() < bestMove.getWeight()) {
                    bestMove = child;
                }
            }
        }
        return bestMove;
    }
    
    private void announceWinner(char[][] board) {
        char winner = checkWin(board);
        if (winner == 'X') {
            System.out.println("Congratulations! You win!");
        } else if (winner == 'O') {
            System.out.println("AI wins! Better luck next time.");
        } else {
            System.out.println("It's a tie!");
        }
    }
    
    private void printBoard(char[][] board) {
        System.out.println("   0   1   2");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + "  ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j]);
                if (j < 2) System.out.print(" | ");
            }
            System.out.println();
            if (i < 2) System.out.println("  -----------");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.playAgainstAI();
    }
}