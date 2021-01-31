import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import common.UnionFind;

// 你准备参加一场远足活动。给你一个二维 rows x columns 的地图 heights ，其中 heights[row][col] 表示格子 (row, col) 的高度。一开始你在最左上角的格子 (0, 0) ，且你希望去最右下角的格子 (rows-1, columns-1) （注意下标从 0 开始编号）。你每次可以往 上，下，左，右 四个方向之一移动，你想要找到耗费 体力 最小的一条路径。
// 一条路径耗费的 体力值 是路径上相邻格子之间 高度差绝对值 的 最大值 决定的。
// 请你返回从左上角走到右下角的最小 体力消耗值 。
//  
// 示例 1：

// 输入：heights = [[1,2,2],[3,8,2],[5,3,5]]
// 输出：2
// 解释：路径 [1,3,5,3,5] 连续格子的差值绝对值最大为 2 。
// 这条路径比路径 [1,2,2,2,5] 更优，因为另一条路径差值最大值为 3 。

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/path-with-minimum-effort

public class MinimumEffortPath {

    public static void main(String[] args) {
        System.out.println(new MinimumEffortPath().minimumEffortPath(new int[][] { { 1, 2, 1, 1, 1 }, { 1, 2, 1, 2, 1 },
                { 1, 2, 1, 2, 1 }, { 1, 2, 1, 2, 1 }, { 1, 1, 1, 2, 1 } }));
    }

    public int minimumEffortPath(int[][] heights) {
        int row = heights.length;
        int col = heights[0].length;
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int index = i * col + j;
                if (index + 1 < (i + 1) * col) {
                    edges.add(new Edge(index, index + 1, Math.abs(heights[i][j] - heights[i][j + 1])));
                }
                if (index + col < col * row) {
                    edges.add(new Edge(index, index + col, Math.abs(heights[i][j] - heights[i + 1][j])));
                }

            }
        }

        edges.sort(new Comparator<Edge>() {

            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.cost - o2.cost;
            }
        });
        // System.out.println(edges);

        UnionFind uf = new UnionFind(row * col);
        for (Edge edge : edges) {
            if (!uf.connected(edge.x, edge.y)) {
                uf.union(edge.x, edge.y);
            }
            if (uf.connected(0, row * col - 1)) {
                return edge.cost;
            }
        }
        return 0;
    }

    class Edge {
        public Edge(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }

        public int x;
        public int y;
        public int cost;

        @Override
        public String toString() {
            return "[" + x + "," + y + "," + cost + "]";
        }
    }
}
