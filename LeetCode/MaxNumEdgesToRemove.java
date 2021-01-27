import common.TestInterface;
import common.UnionFind;

// Alice 和 Bob 共有一个无向图，其中包含 n 个节点和 3  种类型的边：
// 类型 1：只能由 Alice 遍历。
// 类型 2：只能由 Bob 遍历。
// 类型 3：Alice 和 Bob 都可以遍历。
// 给你一个数组 edges ，其中 edges[i] = [typei, ui, vi] 表示节点 ui 和 vi 之间存在类型为 typei 的双向边。请你在保证图仍能够被 Alice和 Bob 完全遍历的前提下，找出可以删除的最大边数。如果从任何节点开始，Alice 和 Bob 都可以到达所有其他节点，则认为图是可以完全遍历的。
// 返回可以删除的最大边数，如果 Alice 和 Bob 无法完全遍历图，则返回 -1 。

// 示例 1：
// 输入：n = 4, edges = [[3,1,2],[3,2,3],[1,1,3],[1,2,4],[1,1,2],[2,3,4]]
// 输出：2
// 解释：如果删除 [1,1,2] 和 [1,1,3] 这两条边，Alice 和 Bob 仍然可以完全遍历这个图。再删除任何其他的边都无法保证图可以完全遍历。所以可以删除的最大边数是 2 。

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/remove-max-number-of-edges-to-keep-graph-fully-traversable

public class MaxNumEdgesToRemove implements TestInterface {

    @Override
    public void test() {
        int[][] edges = new int[][] { { 1, 1, 2 }, { 2, 1, 2 }, { 3, 1, 2 } };
        System.out.println(maxNumEdgesToRemove(2, edges));
    }

    public int maxNumEdgesToRemove(int n, int[][] edges) {
        UnionFind ufa = new UnionFind(n);
        UnionFind ufb = new UnionFind(n);
        int count = 0;
        for (int i = 0; i < edges.length; i++) {
            int[] edge = edges[i];
            if (edge[0] == 3) {
                if (ufa.connected(edge[1] - 1, edge[2] - 1)) {
                    count += 1;
                    continue;
                }
                ufa.union(edge[1] - 1, edge[2] - 1);
                ufb.union(edge[1] - 1, edge[2] - 1);
            }
        }
        for (int i = 0; i < edges.length; i++) {
            int[] edge = edges[i];
            if (edge[0] == 1) {
                if (ufa.connected(edge[1] - 1, edge[2] - 1)) {
                    count += 1;
                    continue;
                }
                ufa.union(edge[1] - 1, edge[2] - 1);
            } else if (edge[0] == 2){
                if (ufb.connected(edge[1] - 1, edge[2] - 1)) {
                    count += 1;
                    continue;
                }
                ufb.union(edge[1] - 1, edge[2] - 1);
            }
        }
        if (ufa.getCount() > 1 || ufb.getCount() > 1)
            return -1;

        return count;
    }
}