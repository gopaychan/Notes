import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Comparator;
import common.UnionFind;
import common.Utils;

// 在一个 N x N 的坐标方格 grid 中，每一个方格的值 grid[i][j] 表示在位置 (i,j) 的平台高度。
// 现在开始下雨了。当时间为 t 时，此时雨水导致水池中任意位置的水位为 t 。你可以从一个平台游向四周相邻的任意一个平台，但是前提是此时水位必须同时淹没这两个平台。假定你可以瞬间移动无限距离，也就是默认在方格内部游动是不耗时的。当然，在你游泳的时候你必须待在坐标方格里面。
// 你从坐标方格的左上平台 (0，0) 出发。最少耗时多久你才能到达坐标方格的右下平台 (N-1, N-1)？
//  
// 示例 1:
// 输入: [[0,2],[1,3]]
// 输出: 3
// 解释:
// 时间为0时，你位于坐标方格的位置为 (0, 0)。
// 此时你不能游向任意方向，因为四个相邻方向平台的高度都大于当前时间为 0 时的水位。
// 等时间到达 3 时，你才可以游向平台 (1, 1). 因为此时的水位是 3，坐标方格中的平台没有比水位 3 更高的，所以你可以游向坐标方格中的任意位置

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/swim-in-rising-water

public class SwimInWater {

    public static void main(String[] args) {
        System.out.println(new SwimInWater().swimInWaterV2(new int[][] { { 0, 2 }, { 1, 3 } }));
        System.out.println(new SwimInWater().swimInWaterV2(Utils
                .stringToIntArray("   [[0         ,1,2,3,4  ],  [24,23,22    ,21,5],[12,13,  14,15,16],[11,17,18,19,20],[10,9,8,7,6]]")));
    }

    public int swimInWater(int[][] grid) {
        int[] dy = new int[] { 1, 0, -1, 0 };// 下，右，上，左
        int[] dx = new int[] { 0, 1, 0, -1 };
        int row = grid.length;
        int col = grid[0].length;

        Queue<Search> queue = new LinkedList<>();
        int dist[] = new int[row * col];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Search s = new Search(0, 0);
        s.path.add(0);
        queue.add(s);
        dist[0] = grid[0][0];

        while (!queue.isEmpty()) {
            Search search = queue.poll();
            int y = search.x;
            int x = search.y;
            // if (x == col - 1 && y == row - 1)
            // break;
            int index = row * y + x;
            for (int i = 0; i < dy.length; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx >= 0 && nx < col && ny >= 0 && ny < row) {
                    int nIndex = row * ny + nx;
                    if (search.path.contains(nIndex))
                        continue;
                    dist[nIndex] = Math.min(Math.max(dist[index], grid[ny][nx]), dist[nIndex]);
                    Search sAdd = new Search(ny, nx);
                    sAdd.path.addAll(search.path);
                    sAdd.path.add(nIndex);
                    queue.offer(sAdd);
                }
            }
        }
        return dist[row * col - 1];
    }

    class Search {
        public Search(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x;
        public int y;
        List<Integer> path = new ArrayList<>();
    }

    public int swimInWaterV2(int[][] grid) {
        int row = grid.length;
        int col = grid[0].length;
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int index = i * col + j;
                if (index + 1 < (i + 1) * col) {
                    edges.add(new Edge(index, index + 1, Math.max(grid[i][j], grid[i][j + 1])));
                }
                if (index + col < col * row) {
                    edges.add(new Edge(index, index + col, Math.max(grid[i][j], grid[i + 1][j])));
                }

            }
        }

        edges.sort(new Comparator<Edge>() {

            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.height - o2.height;
            }
        });
        // System.out.println(edges);

        UnionFind uf = new UnionFind(row * col);
        for (Edge edge : edges) {
            if (!uf.connected(edge.x, edge.y)) {
                uf.union(edge.x, edge.y);
            }
            if (uf.connected(0, row * col - 1)) {
                return edge.height;
            }
        }
        return 0;
    }

    class Edge {
        public Edge(int x, int y, int height) {
            this.x = x;
            this.y = y;
            this.height = height;
        }

        public int x;
        public int y;
        public int height;

        @Override
        public String toString() {
            return "[" + x + "," + y + "," + height + "]";
        }
    }
}