import java.util.ArrayList;
import java.util.List;

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
    public int minimumEffortPath(int[][] heights) {
        int row = heights.length;
        int col = heights[0].length;
        int[] h = new int[row * col];
        List<Line> lines = new ArrayList<>();
        Line line0 = new Line();
        line0.path.add(0);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                heights[i][j]
            }
        }
        return 0;
    }

    private void addNextPoint(List<Line> lines, Line line, int x, int y) {

    }

    class Line {
        public List<Integer> path = new ArrayList<>();
        public int cost = 0;
    }
}
