import java.util.*;

class PuzzleSolver {
    static final int N = 3;
    static final int[][] goalState = {{1, 2, 3}, {8, 0, 4}, {7, 6, 5}};

    static class PuzzleState implements Comparable<PuzzleState> {
        int[][] board;
        int g;
        int h; // heuristic function
        int f;

        public PuzzleState(int[][] board, int g) {
            this.board = new int[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    this.board[i][j] = board[i][j];
                }
            }
            this.g = g;
            this.h = calculateHeuristic();
            this.f = this.g + this.h;
        }

        private int calculateHeuristic() {
            int heuristic = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    int value = this.board[i][j];
                    if (value != 0) {
                        int goalRow = (value - 1) / N;
                        int goalCol = (value - 1) % N;
                        heuristic += Math.abs(i - goalRow) + Math.abs(j - goalCol);
                    }
                }
            }
            return heuristic;
        }


        public boolean isGoal() {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (this.board[i][j] != goalState[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }

        public List<PuzzleState> getSuccessors() {
            List<PuzzleState> successors = new ArrayList<>();
            int row = -1, col = -1;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (this.board[i][j] == 0) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // trên dưới trái phải
            for (int[] d : directions) {
                int newRow = row + d[0], newCol = col + d[1];
                if (newRow >= 0 && newRow < N && newCol >= 0 && newCol < N) {
                    int[][] newBoard = new int[N][N];
                    for (int i = 0; i < N; i++) {
                        System.arraycopy(this.board[i], 0, newBoard[i], 0, N);
                    }

                    newBoard[row][col] = newBoard[newRow][newCol];
                    newBoard[newRow][newCol] = 0;
                    successors.add(new PuzzleState(newBoard, this.g + 1));
                }
            }
            return successors;
        }

        @Override
        public int compareTo(PuzzleState other) {
            return this.f - other.f;
        }

        public void printState() {
            System.out.println("g = " + this.g + ", h = " + this.h + ", f = " + this.f);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(this.board[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void solvePuzzle(int[][] initialState) {
        PriorityQueue<PuzzleState> openList = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        PuzzleState startState = new PuzzleState(initialState, 0);
        openList.add(startState);

        while (!openList.isEmpty()) {
            PuzzleState currentState = openList.poll();
            currentState.printState();

            if (currentState.isGoal()) {
                System.out.println("Goal state reached!");
                return;
            }

            visited.add(Arrays.deepToString(currentState.board));

            for (PuzzleState nextState : currentState.getSuccessors()) {
                if (!visited.contains(Arrays.deepToString(nextState.board))) {
                    openList.add(nextState);
                }
            }
        }
        System.out.println("Solution not found.");
    }

    public static void main(String[] args) {
        // Dùng 0 để biểu diễn ô trống
        int[][] initialState = {{1,0, 7}, {5, 4, 8}, {2, 3, 6}};
        solvePuzzle(initialState);
    }
}