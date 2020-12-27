package com.freezing.leetcode.jike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Week4 {
    /**
     * 127. 单词接龙
     * https://leetcode-cn.com/problems/word-ladder/description/
     *
     * @param beginWord
     * @param endWord
     * @param wordList
     * @return
     */
    private Set<String> wordSet;

    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        int count = 1;
        queue.offer(beginWord);
        visited.add(beginWord);
        wordSet.remove(beginWord);
        while (!queue.isEmpty()) {
            count++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String s = queue.poll();
                for (String neighbor : findNeighbors(s)) {
                    if (endWord.equals(neighbor)) return count;
                    if (!visited.contains(neighbor)) {
                        queue.offer(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
        }
        return 0;
    }

    private List<String> findNeighbors(String s) {
        char[] chars = s.toCharArray();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            for (char j = 'a'; j <= 'z'; j++) {
                if (j == c) continue;
                chars[i] = j;
                String newString = new String(chars);
                if (wordSet.contains(newString)) list.add(newString);
            }
            chars[i] = c;
        }
        return list;
    }

    /**
     * 126. 单词接龙 II
     * https://leetcode-cn.com/problems/word-ladder-ii/
     *
     * @param beginWord
     * @param endWord
     * @param wordList
     * @return
     */
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return res;
        Queue<List<String>> queue = new LinkedList<>();
        List<String> path = new ArrayList<>();
        boolean isFound = false;
        Set<String> visited = new HashSet<>();
        path.add(beginWord);
        queue.offer(path);
        wordSet.remove(beginWord);
        visited.add(beginWord);
        while (!queue.isEmpty()) {
            int size = queue.size();
            Set<String> subVisited = new HashSet<>();
            for (int i = 0; i < size; i++) {
                List<String> p = queue.poll();
                String s = p.get(p.size() - 1);
                for (String neighbor : findNeighbors(s, wordSet)) {
                    if (visited.contains(neighbor)) continue;
                    p.add(neighbor);
                    if (endWord.equals(neighbor)) {
                        isFound = true;
                        res.add(new ArrayList<>(p));
                    }
                    queue.offer(new ArrayList<>(p));
                    p.remove(neighbor);
                    subVisited.add(neighbor);
                }
            }
            visited.addAll(subVisited);
            if (isFound) break;
        }
        return res;
    }

    public Set<String> findNeighbors(String s, Set<String> wordSet) {
        char[] chars = s.toCharArray();
        Set<String> neighbor = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            char c = chars[i];
            for (char j = 'a'; j <= 'z'; j++) {
                if (c == j) continue;
                chars[i] = j;
                String newStr = new String(chars);
                if (wordSet.contains(newStr)) neighbor.add(newStr);
            }
            chars[i] = c;
        }
        return neighbor;
    }

    /**
     * 200. 岛屿数量
     * https://leetcode-cn.com/problems/number-of-islands/
     *
     * @param grid
     * @return
     */
    public int numIslands(char[][] grid) {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    count += area(grid, i, j);
                }
            }
        }
        return count;
    }

    public int area(char[][] grid, int row, int column) {
        if (!inArea(grid, row, column)) return 0;
        if (grid[row][column] != '1') return 0;
        grid[row][column] = '2';
        area(grid, row - 1, column);
        area(grid, row + 1, column);
        area(grid, row, column - 1);
        area(grid, row, column + 1);

        return 1;
    }

    /**
     * 860. 柠檬水找零
     * https://leetcode-cn.com/problems/lemonade-change/
     *
     * @param bills
     * @return
     */
    public boolean lemonadeChange(int[] bills) {
        if (bills == null || bills.length == 0) return false;
        int five = 0, ten = 0;
        for (int i = 0; i < bills.length; i++) {
            if (bills[i] == 5) {
                five++;
            } else if (bills[i] == 10) {
                five--;
                ten++;
            } else {
                if (ten > 0) {
                    ten--;
                    five--;
                } else {
                    five = five - 3;
                }
            }
            if (five < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 122. 买卖股票的最佳时机 II
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/description/
     *
     * @param prices
     * @return
     */
    public int maxProfit(int[] prices) {
        int count = 0;
        for (int i = 1; i < prices.length; i++) {
            int profit = prices[i] - prices[i - 1];
            if (profit > 0) count += profit;
        }
        return count;
    }

    /**
     * 455. 分发饼干
     * https://leetcode-cn.com/problems/assign-cookies/description/
     *
     * @param g
     * @param s
     * @return
     */
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int j = 0;
        int res = 0;
        for (int i = 0; i < s.length; i++) {
            if (j == g.length) break;
            if (g[j] <= s[i]) {
                res++;
                j++;
            }
        }
        return res;
    }

    public boolean inArea(char[][] grid, int row, int column) {
        return 0 <= row && row < grid.length
                && 0 <= column && column < grid[0].length;
    }

    /**
     * 529. 扫雷游戏
     * https://leetcode-cn.com/problems/minesweeper/description/
     *
     * @param board
     * @param click
     * @return
     */
    int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};
    int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};

    public char[][] updateBoard(char[][] board, int[] click) {
        int x = click[0], y = click[1];
        if (board[x][y] == 'M') {
            board[x][y] = 'X';
        } else {
//            dfs(board, x, y);
            int row = board.length, column = board[0].length;
            Queue<int[]> queue = new LinkedList<>();
            boolean[][] visited = new boolean[row][column];
            queue.offer(click);
            visited[x][y] = true;
            while (!queue.isEmpty()) {
                int[] point = queue.poll();
                int i = point[0], j = point[1];
                int count = 0;
                for (int k = 0; k < 8; k++) {
                    int newX = i + dx[k];
                    int newY = i + dy[k];
                    if (!inBoard(board, newX, newY)) continue;
                    if (board[x][y] == 'M') count++;
                }
                if (count > 0) {
                    board[i][j] = (char) (count + '0');
                } else {
                    board[i][j] = 'B';
                    for (int k = 0; k < 8; k++) {
                        int newX = i + dx[k];
                        int newY = i + dy[k];
                        if (!inBoard(board, newX, newY) || board[newX][newY] != 'E' || visited[newX][newY])
                            continue;
                        visited[newX][newY] = true;
                        queue.offer(new int[]{newX, newY});
                    }
                }
            }
        }
        return board;
    }

    private void dfs(char[][] board, int r, int c) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int x = r + dx[i];
            int y = c + dy[i];
            if (!inBoard(board, x, y)) continue;
            if (board[x][y] == 'M') {
                count++;
            }
        }
        if (count > 0) {
            board[r][c] = (char) (count + '0');
            return;
        }
        board[r][c] = 'B';
        for (int i = 0; i < 8; i++) {
            int x = r + dx[i];
            int y = c + dy[i];
            if (!inBoard(board, x, y) || board[x][y] != 'E') continue;
            dfs(board, x, y);
        }
    }

    private boolean inBoard(char[][] board, int r, int c) {
        return 0 <= r && r < board.length
                && 0 <= c && c < board[0].length;
    }

    /**
     * 874. 模拟行走机器人
     * https://leetcode-cn.com/problems/walking-robot-simulation/description/
     *
     * @param commands
     * @param obstacles
     * @return
     */
    public int robotSim(int[] commands, int[][] obstacles) {
        int res = 0;
        int x = 0, y = 0, direction = 0;
        int[][] xy = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        Set<String> obstacleSet = new HashSet<>();
        for (int[] obstacle : obstacles) {
            obstacleSet.add(obstacle[0] + "," + obstacle[1]);
        }
        for (int com : commands) {
            if (com == -2) {
                direction = (direction + 3) % 4;
            } else if (com == -1) {
                direction = (direction + 1) % 4;
            } else {
                while (com-- > 0 && !obstacleSet.contains(x + xy[direction][0] + "," + y + xy[direction][1])) {
                    x += xy[direction][0];
                    y += xy[direction][1];
                }
                res = Math.max(res, x * x + y * y);
            }
        }
        return res;
    }

    /**
     * 55. 跳跃游戏
     * https://leetcode-cn.com/problems/jump-game/
     *
     * @param nums
     * @return
     */
    public boolean canJump(int[] nums) {
        if (nums.length < 2) return true;
        int minStep = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] >= minStep) {
                minStep = 1;
            } else {
                minStep++;
            }
        }
        return minStep == 1;
    }

    /**
     * 45. 跳跃游戏 II
     * https://leetcode-cn.com/problems/jump-game-ii/
     *
     * @param nums
     * @return
     */
    public int jump(int[] nums) {
        int end = 0, maxPosition = 0,res = 0,now = 0;
        while(end < nums.length - 1){
            maxPosition = Math.max(maxPosition,now+nums[now]);
            if(now == end){
                end = maxPosition;
                res ++;
            }
            now ++;
        }
        return res;
    }

    /**
     * 33. 搜索旋转排序数组
     * https://leetcode-cn.com/problems/search-in-rotated-sorted-array/
     * @param nums
     * @param target
     * @return
     */
    public int search(int[] nums, int target) {
        int start = 0, end = nums.length - 1;
        while(start <= end){
            int mid = (start + end + 1) >>> 1;
            if(nums[mid] == target) return mid;
            if(target >= nums[0]){
                // 目标值大于等于最左边的值，说明在左半段
                // 若mid比最左边的值还小，说明mid在右半段，将mid的值置为Integer.MAX_VALUE
                if(nums[mid] < nums[0]){
                    nums[mid] = Integer.MAX_VALUE;
                }
            } else {
                // 目标值小于最左边的值，说明在右半段
                // 如果mid比最左边的值还大，说明mid在左半段，将mid的值置位Integer.MIN_VALUE
                if(nums[mid] >= nums[0]){
                    nums[mid] = Integer.MIN_VALUE;
                }
            }
            if(target > nums[mid]){
                start = mid + 1;
            } else {
                end = mid - 1;
            }

        }
        return -1;
    }

    /**
     * 153. 寻找旋转排序数组中的最小值
     * https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array/
     * @param nums
     * @return
     */
    public int findMin(int[] nums) {
        int start = 0, end = nums.length - 1;
        // 最小的值肯定在非单调递增的一端
        while(start <= end){
            if(nums[start] < nums[end]) return nums[start];
            int mid = (start + end) >>> 1;
            if(nums[mid] > nums[start]){
                // mid的值大于最左边的值，说明左边单调递增，最小值在右边
                start = mid + 1;
            } else {
                // mid的值小于左边的值，说明右边单调递增，最小值在左边
                end = mid;
            }
        }
        return -1;
    }

    /**
     * 74. 搜索二维矩阵
     * https://leetcode-cn.com/problems/search-a-2d-matrix/
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        if(matrix == null || matrix.length == 0) return false;
        int row = matrix.length, column = matrix[0].length;
        int start = 0, end = row * column - 1;
        while(start <= end){
            int mid = (start + end) >>> 1;
            int value = matrix[mid/column][mid%row];
            if(value == target) return true;
            if(value < target){
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        Week4 week4 = new Week4();
        int x = Integer.MAX_VALUE - 1;
        int y = Integer.MAX_VALUE;
        System.out.println("x = " + x);
        System.out.println("y = " + y);
        System.out.println("x + y = " + (x+y));
        System.out.println("(x+y)/2 = " + ((x+y)/2));
        System.out.println("(x+y)>>1 = " + ((x+y)>>1));
        System.out.println("(x+y)>>>1 = " + ((x+y)>>>1));
    }
}
