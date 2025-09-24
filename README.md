# System Architecture

## Core Game Logic
* **Board Representation:** Uses a 3x3 array to represent the Tic Tac Toe board state.
* **Game Tree Structure:** Implements a complete game tree using a `LinkedBinaryTree` data structure where each node represents a game state.
* **AI Decision Engine:** Uses weighted probability calculations to determine optimal moves, with weights normalized to percentages totaling 100.

## Game Tree Construction
* **Recursive Tree Building:** Constructs the entire game tree recursively from the root node, exploring all possible game states.
* **Deep Copy Strategy:** Implements board copying to maintain independent game states across tree nodes.
* **Terminal Node Evaluation:** Assigns final weights to leaf nodes based on game outcomes (win/tie/loss).

## Weight Calculation System
* **Probability-Based Scoring:** Calculates move quality by dividing individual child weights by total weight and converting to percentages.
* **Minimax Integration:** Uses weight propagation through the tree to evaluate position strength.
* **Best Move Selection:** Identifies optimal moves by selecting children nodes with highest calculated weights.

## Game Flow Management
* **Interactive Gameplay:** Provides console-based interface for human vs AI gameplay.
* **Move Validation:** Includes game state checking for wins, ties, and valid moves.
* **Decision Support:** Offers features to display possible moves and show AI's best move recommendation.

## Search and Navigation
* **Breadth-First Search:** Implements BFS algorithm to locate specific nodes within the game tree.
* **Board Comparison:** Uses cell-by-cell comparison to identify matching game states.
* **Tree Traversal:** Supports navigation through different game states via tree structure.
