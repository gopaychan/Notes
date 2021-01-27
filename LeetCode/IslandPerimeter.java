// 给定一个包含 0 和 1 的二维网格地图，其中 1 表示陆地 0 表示水域。
// 网格中的格子水平和垂直方向相连（对角线方向不相连）。整个网格被水完全包围，但其中恰好有一个岛屿（或者说，一个或多个表示陆地的格子相连组成的岛屿）。
// 岛屿中没有“湖”（“湖” 指水域在岛屿内部且不和岛屿周围的水相连）。格子是边长为 1 的正方形。网格为长方形，且宽度和高度均不超过 100
// 。计算这个岛屿的周长。
// 示例 :
// 输入:
// [[0,1,0,0],startI
// 输出: 16

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/island-perimeter/

public class IslandPerimeter {
    
    public int islandPerimeter(int[][] grid) {
        int count = 0;
        int startLength = -1;
        int endLength = -1;
        int startWidth = -1;
        int endWidth = -1;
        for (int i = 0; i < grid.length; i++) {
            boolean start = false;
            boolean middle = false;
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    if (start && middle) {
                        count += 1;
                        // start = false;
                        middle = false;
                        System.out.println("i=" + i + " j=" + j);
                    } else {
                        start = true;
                    }
                    if (startWidth == -1) {
                        startWidth = j;
                        endWidth = j;
                        startLength = i;
                        endLength = i;
                    } else {
                        startWidth = Math.min(startWidth, j);
                        endWidth = Math.max(endWidth, j);
                        startLength = Math.min(startLength, i);
                        endLength = Math.max(endLength, i);
                    }
                }
                if (grid[i][j] == 0 && start) {
                    middle = true;
                }
            }
        }
        System.out.println("count=" + count);

        for (int j = 0; j < grid[0].length; j++) {
            boolean start = false;
            boolean middle = false;
            for (int i = 0; i < grid.length; i++) {
                if (grid[i][j] == 1) {
                    if (start && middle) {
                        count += 1;
                        middle = false;
                    } else {
                        start = true;
                    }
                }
                if (grid[i][j] == 0 && start) {
                    middle = true;
                }
            }
        }
        System.out.println("count=" + count);
        return (endLength - startLength + 1 + endWidth - startWidth + 1 + count) * 2;
    }

    int[] dx = { 0, 1, 0, -1 };
    int[] dy = { 1, 0, -1, 0 };

    public int islandPerimeterv2(int[][] grid) {
        int n = grid.length, m = grid[0].length;
        int ans = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (grid[i][j] == 1) {
                    int cnt = 0;
                    for (int k = 0; k < 4; ++k) {
                        int tx = i + dx[k];
                        int ty = j + dy[k];
                        if (tx < 0 || tx >= n || ty < 0 || ty >= m || grid[tx][ty] == 0) {
                            cnt += 1;
                        }
                    }
                    ans += cnt;
                }
            }
        }
        return ans;
    }

}
