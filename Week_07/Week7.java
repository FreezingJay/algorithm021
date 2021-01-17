package com.freezing.leetcode.jike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Week7 {

    /**
     * 212. 单词搜索 II
     * https://leetcode-cn.com/problems/word-search-ii/
     *
     * @param board
     * @param words
     * @return
     */
    Set<String> res;
    TrieNode root;
    int[] dx = {0, 0, -1, 1};
    int[] dy = {-1, 1, 0, 0};

    public List<String> findWords(char[][] board, String[] words) {
        root = new TrieNode();
        for (String word : words) {
            insert(word);
        }
        int m = board.length, n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        res = new HashSet<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (root.next[board[i][j] - 'a'] == null) continue;
                dfs(board, visited, root, i, j, new StringBuilder());
            }
        }
        return new ArrayList<>(res);
    }

    private void dfs(char[][] board, boolean[][] visited, TrieNode node, int x, int y, StringBuilder sb) {
        sb.append(board[x][y]);
        visited[x][y] = true;
        node = node.next[board[x][y] - 'a'];
        if (node.isEnd) res.add(sb.toString());

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            if (!inBoard(board, newX, newY) || visited[newX][newY] || node.next[board[newX][newY] - 'a'] == null)
                continue;
            dfs(board, visited, node, newX, newY, sb);
        }
        sb.deleteCharAt(sb.length() - 1);
        visited[x][y] = false;
    }

    public boolean inBoard(char[][] board, int x, int y) {
        return 0 <= x && x < board.length && 0 <= y && y < board[0].length;
    }

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            TrieNode tmp = node.next[c - 'a'];
            if (tmp == null) {
                tmp = new TrieNode();
                node.next[c - 'a'] = tmp;
            }
            node = tmp;
        }
        node.isEnd = true;
    }

    /**
     * 547. 省份数量
     * https://leetcode-cn.com/problems/number-of-provinces/
     *
     * @param isConnected
     * @return
     */
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        UnionFind unionFind = new UnionFind(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected[i][j] == 1) {
                    unionFind.union(i, j);
                }
            }
        }
        return unionFind.getCount();
    }

    /**
     * 130. 被围绕的区域
     * https://leetcode-cn.com/problems/surrounded-regions/
     *
     * @param board
     */
    public void solve(char[][] board) {
        if (board == null || board.length == 0) return;
        int m = board.length, n = board[0].length;
        UnionFind uf = new UnionFind(m * n + 1);
        int dummyNode = m * n;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O') {
                    boolean isEdge = i == 0 || j == 0 || i == m - 1 || j == n - 1;
                    if (isEdge) {
                        uf.union(getIndex(i, j, n), dummyNode);
                    } else {
                        for (int k = 0; k < dx.length; k++) {
                            int x = i + dx[k];
                            int y = j + dy[k];
                            if (inBoard(board, x, y) && board[x][y] == 'O') {
                                uf.union(getIndex(i, j, n), getIndex(x, y, n));
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O' && uf.find(getIndex(i, j, n)) != uf.find(dummyNode)) {
                    board[i][j] = 'X';
                }
            }
        }
    }

    public int getIndex(int i, int j, int c) {
        return i * c + j;
    }

    /**
     * 36. 有效的数独
     * https://leetcode-cn.com/problems/valid-sudoku/
     *
     * @param board
     * @return
     */
    public boolean isValidSudoku(char[][] board) {
        int[][] row = new int[9][9];
        int[][] col = new int[9][9];
        int[][] box = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int num = board[i][j] - '1';
                    int boxIndex = i / 3 * 3 + j / 3;
                    if (row[i][num] == 1) return false;
                    row[i][num] = 1;
                    if (col[j][num] == 1) return false;
                    col[j][num] = 1;
                    if (box[boxIndex][num] == 1) return false;
                    box[boxIndex][num] = 1;
                }
            }
        }
        return true;
    }

    /**
     * 37. 解数独
     * https://leetcode-cn.com/problems/sudoku-solver/
     *
     * @param board
     */
    public void solveSudoku(char[][] board) {
        boolean[][] rowUsed = new boolean[9][10];
        boolean[][] colUsed = new boolean[9][10];
        boolean[][][] boxUsed = new boolean[3][3][10];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '.') {
                    int num = board[i][j] - '0';
                    rowUsed[i][num] = true;
                    colUsed[j][num] = true;
                    boxUsed[i / 3][j / 3][num] = true;
                }
            }
        }
        dfs(board, rowUsed, colUsed, boxUsed, 0, 0);
    }

    public boolean dfs(char[][] board, boolean[][] rowUsed, boolean[][] colUsed, boolean[][][] boxUsed, int row, int col) {
        if (col == board[0].length) {
            col = 0;
            row++;
            if (row == board.length) return true;
        }
        if (board[row][col] == '.') {
            for (int i = 1; i <= 9; i++) {
                boolean canUse = !rowUsed[row][i] && !colUsed[col][i] && !boxUsed[row / 3][col / 3][i];
                if (canUse) {
                    board[row][col] = (char) (i + '0');
                    rowUsed[row][i] = true;
                    colUsed[col][i] = true;
                    boxUsed[row / 3][col / 3][i] = true;
                    if (dfs(board, rowUsed, colUsed, boxUsed, row, col + 1)) return true;
                    board[row][col] = '.';
                    rowUsed[row][i] = false;
                    colUsed[col][i] = false;
                    boxUsed[row / 3][col / 3][i] = false;
                }
            }
        } else {
            return dfs(board, rowUsed, colUsed, boxUsed, row, col + 1);
        }
        return false;
    }

    /**
     * 22. 括号生成
     * https://leetcode-cn.com/problems/generate-parentheses/
     *
     * @param n
     * @return
     */
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        dfs(0, 0, n, "", res);
        return res;
    }

    public void dfs(int left, int right, int max, String s, List<String> res) {
        if (left == max && right == max) {
            res.add(s);
            return;
        }
        if (left < max) {
            dfs(left + 1, right, max, s + "(", res);
        }
        if (right < left) {
            dfs(left, right + 1, max, s + ")", res);
        }
    }


    /**
     * 51. N 皇后
     * https://leetcode-cn.com/problems/n-queens/
     *
     * @param n
     * @return
     */
    List<List<String>> solveNQueensRes;

    public List<List<String>> solveNQueens(int n) {
        solveNQueensRes = new ArrayList<>();
        dfs(new int[n], 0, n, new HashSet<Integer>(), new HashSet<Integer>(), new HashSet<Integer>());
        return solveNQueensRes;
    }

    public void dfs(int[] queens, int row, int n, Set<Integer> colSet, Set<Integer> pieSet, Set<Integer> naSet) {
        if (row == n) {
            solveNQueensRes.add(generateBoard(queens, n));
            return;
        }
        for (int i = 0; i < n; i++) {
            if (colSet.contains(i)) continue;
            int pie = row + i;
            if (pieSet.contains(pie)) continue;
            int na = row - i;
            if (naSet.contains(na)) continue;

            queens[row] = i;
            colSet.add(i);
            pieSet.add(pie);
            naSet.add(na);
            dfs(queens, row + 1, n, colSet, pieSet, naSet);
            queens[row] = -1;
            colSet.remove(i);
            pieSet.remove(pie);
            naSet.remove(na);
        }
    }

    public List<String> generateBoard(int[] queens, int n) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            char[] chars = new char[n];
            Arrays.fill(chars, '.');
            chars[queens[i]] = 'Q';
            list.add(new String(chars));
        }
        return list;
    }

    /**
     * 127. 单词接龙
     * https://leetcode-cn.com/problems/word-ladder/
     *
     * @param beginWord
     * @param endWord
     * @param wordList
     * @return
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;
        Set<String> visited = new HashSet<>();
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        int count = 1;
        beginSet.add(beginWord);
        endSet.add(endWord);
        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
            count++;
            if (beginSet.size() > endSet.size()) {
                Set<String> tmp = beginSet;
                beginSet = endSet;
                endSet = tmp;
            }
            Set<String> newBeginSet = new HashSet<>();
            for (String word : beginSet) {
                if (changeLetter(wordSet, word, visited, newBeginSet, endSet)) return count;
            }
            beginSet = newBeginSet;
        }
        return 0;
    }

    public boolean changeLetter(Set<String> wordSet, String s, Set<String> visited, Set<String> newBeginSet, Set<String> endSet) {
        char[] chars = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            char c = chars[i];
            for (char j = 'a'; j <= 'z'; j++) {
                if (c == j) continue;
                chars[i] = j;
                String newWord = String.valueOf(chars);
                if (endSet.contains(newWord)) return true;
                if (visited.contains(newWord) || !wordSet.contains(newWord)) continue;
                newBeginSet.add(newWord);
                visited.add(newWord);
            }
            chars[i] = c;
        }
        return false;
    }


    /**
     * 433. 最小基因变化
     * https://leetcode-cn.com/problems/minimum-genetic-mutation/
     *
     * @param start
     * @param end
     * @param bank
     * @return
     */
    char[] genes = {'A', 'C', 'G', 'T'};

    public int minMutation(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        if (!bankSet.contains(end)) return -1;
        Set<String> visited = new HashSet<>();
        Set<String> startSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        int count = 0;
        startSet.add(start);
        endSet.add(end);
        while (!startSet.isEmpty() && !endSet.isEmpty()) {
            count++;
            if (startSet.size() > endSet.size()) {
                Set<String> tmp = startSet;
                startSet = endSet;
                endSet = tmp;
            }
            Set<String> newStartSet = new HashSet<>();
            for (String s : startSet) {
                if (changeGene(bankSet, s, visited, newStartSet, endSet)) return count;
            }
            startSet = newStartSet;
        }
        return -1;
    }

    public boolean changeGene(Set<String> bankSet, String s, Set<String> visited, Set<String> newStartSet, Set<String> endSet) {
        char[] chars = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            char c = chars[i];
            for (char j : genes) {
                if (c == j) continue;
                chars[i] = j;
                String newGene = String.valueOf(chars);
                if (endSet.contains(newGene)) return true;
                if (visited.contains(newGene) || !bankSet.contains(newGene)) continue;
                visited.add(newGene);
                newStartSet.add(newGene);
            }
            chars[i] = c;
        }
        return false;
    }


    /**
     * 773. 滑动谜题
     * https://leetcode-cn.com/problems/sliding-puzzle/
     *
     * @param board
     * @return
     */
    int[][] neighbor = {
            {1, 3},
            {0, 2, 4},
            {1, 5},
            {0, 4},
            {1, 3, 5},
            {2, 4}
    };

    public int slidingPuzzle(int[][] board) {
        String target = "123450";
        String start = boardToString(board);
        if (start.equals(target)) return 0;
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        int count = 0;
        queue.offer(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            count++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String s = queue.poll();
                int zeroIndex = s.indexOf('0');
                for (int j = 0; j < neighbor[zeroIndex].length; j++) {
                    String newStr = swap(s, zeroIndex, neighbor[zeroIndex][j]);
                    if (newStr.equals(target)) return count;
                    if (visited.contains(newStr)) continue;
                    queue.offer(newStr);
                    visited.add(newStr);
                }
            }
        }
        return -1;
    }

    public String swap(String s, int i, int j) {
        char[] chars = s.toCharArray();
        char tmp = chars[i];
        chars[i] = chars[j];
        chars[j] = tmp;
        return String.valueOf(chars);
    }

    public String boardToString(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                sb.append(board[i][j]);
            }
        }
        return sb.toString();
    }

    /**
     * 1091. 二进制矩阵中的最短路径
     * https://leetcode-cn.com/problems/shortest-path-in-binary-matrix/
     *
     * @param grid
     * @return
     */
    public int shortestPathBinaryMatrix(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        if (grid[0][0] == 1 || grid[m - 1][n - 1] == 1) return -1;
        if (m == 1 && n == 1) return 1;
        int[][] xy = {{0, 1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {-1, -1}, {-1, 0}, {-1, 1}};
        int count = 1;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        while (!queue.isEmpty()) {
            count++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] arr = queue.poll();
                for (int j = 0; j < 8; j++) {
                    int x = arr[0] + xy[j][0], y = arr[1] + xy[j][1];
                    if (!inGrid(grid, x, y) || grid[x][y] == 1)
                        continue;
                    if (x == m - 1 && y == n - 1) return count;
                    queue.offer(new int[]{x, y});
                    grid[x][y] = 1;
                }
            }
        }
        return -1;
    }

    public boolean inGrid(int[][] grid, int r, int c) {
        return 0 <= r && r < grid.length && 0 <= c && c < grid[0].length;
    }


    public static void main(String[] args) {
        Week7 week7 = new Week7();
//        week7.findWords(new char[][]{{'o', 'a', 'a', 'n'}, {'e', 't', 'a', 'e'}, {'i', 'h', 'k', 'r'}, {'i', 'f', 'l', 'v'}},
//                new String[]{"oath", "pea", "eat", "rain"});
//        week7.findCircleNum(new int[][]{{1, 1, 0}, {1, 1, 1}, {0, 1, 1}});
        week7.shortestPathBinaryMatrix(new int[][]{{0, 0, 0, 0, 1}, {1, 0, 0, 0, 0}, {0, 1, 0, 1, 0}, {0, 0, 0, 1, 1}, {0, 0, 0, 1, 0}});
    }
}
