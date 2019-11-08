package src;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/** Author: Matthew Chen
 *  This is the WeightedQuickUnionFind data structure to power the game
 *  I hashed the block position and used hashing a virtual top and virtual bottom to speed up the union process
 */
public class Percolation {
    int N;
    private Block[][] blocks;
    private int openSites;
    private WeightedQuickUnionUF WQU;
    private Block vrTop;
    private Block vrBot;

    class Block {
        boolean openness;
        final int xPos;
        final int yPos;
        int hash;

        private Block(int x, int y) {
            openness = false;
            xPos = x;
            yPos = y;
            hash = -1;
        }

        private Block(int hashCode) {
            openness = true;
            xPos = -1;
            yPos = -1;
            hash = hashCode;
        }
    }

    public Percolation(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        N = n;
        vrTop = new Block(N*N+1);
        vrBot = new Block(N*N+2);
        openSites = 0;
        WQU = new WeightedQuickUnionUF(N*N+3);
        blocks = new Block[N][N];
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                blocks[row][col] = new Block(row, col);
                blocks[row][col].hash = row*N + col;
            }
        }

    }

    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        blocks[row][col].openness = true;
        openSites += 1;
        for (int i = row - 1; i <= row + 1; i += 2) {
            try {
                if (blocks[i][col].openness) {
                    WQU.union(blocks[row][col].hash, blocks[i][col].hash);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Index out of Bound!");
            }
        }
        for (int j = col - 1; j <= col + 1; j += 2) {
            try {
                if (blocks[row][j].openness) {
                    WQU.union(blocks[row][j].hash, blocks[row][col].hash);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Index out of Bound!");
            }
        }

        if (row == 0) {
            WQU.union(blocks[row][col].hash, vrTop.hash);
        } else if (row == N-1) {
            WQU.union(blocks[row][col].hash, vrBot.hash);
        }
    }

    public boolean isOpen(int row, int col) throws IndexOutOfBoundsException {
        try {
            return blocks[row][col].openness;
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    public boolean isFull(int row, int col) {
        try {
            if (!blocks[row][col].openness) {
                return false;
            }
            int groupNo = WQU.find(blocks[row][col].hash);
            if (WQU.find(vrTop.hash) == groupNo) {
                return true;
            }
            return false;
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        if (WQU.find(vrTop.hash) == WQU.find(vrBot.hash)) {
            return true;
        }
        return false;
    }
}                       
