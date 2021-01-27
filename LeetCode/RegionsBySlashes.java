import common.TestInterface;
import common.UnionFind;

// 在由 1 x 1 方格组成的 N x N 网格 grid 中，每个 1 x 1 方块由 /、\ 或空格构成。这些字符会将方块划分为一些共边的区域。
// （请注意，反斜杠字符是转义的，因此 \ 用 "\\" 表示。）。
// 返回区域的数目。

// 示例 1：
// 输入：
// [
//   " /",
//   "/ "
// ]
// 输出：2
// 解释：2x2 网格如下：

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/regions-cut-by-slashes

public class RegionsBySlashes implements TestInterface {

    @Override
    public void test() {
        String[] grid = new String[] { "/\\", "\\/" };
        System.out.println(regionsBySlashes(grid));
    }

    public int regionsBySlashes(String[] grid) {
        int n = grid.length;
        UnionFind uf = new UnionFind((n + 1) * (n + 1));
        for (int i = 0; i < n; i++) {
            uf.union(i, i + 1);
            uf.union(n * (n + 1) + i, n * (n + 1) + i + 1);
            uf.union(i * (n + 1), (i + 1) * (n + 1));
            uf.union(i * (n + 1) + n, (i + 1) * (n + 1) + n);
        }

        int count = 1;
        for (int i = 0; i < n; i++) {
            char[] columns = grid[i].toCharArray();
            for (int j = 0; j < n; j++) {
                int start = -1;
                int end = -1;
                if (columns[j] == '\\') {
                    start = j + i * (n + 1);
                    end = j + i * (n + 1) + n + 2;
                } else if (columns[j] == '/') {
                    start = j + i * (n + 1) + 1;
                    end = j + i * (n + 1) + n + 1;
                } else {
                    continue;
                }
                if (uf.connected(start, end)) {
                    System.out.println("start=" + start + " end=" + end);
                    count++;
                } else
                    uf.union(start, end);
            }
        }
        return count;
    }
}
