import common.UnionFind;

public class MakeConnected {
    public int makeConnected(int n, int[][] connections) {
        UnionFind uf = new UnionFind(n);
        int count = 0;
        for (int i = 0; i < connections.length; i++) {
            if (!uf.connected(connections[i][0], connections[i][1])) {
                uf.union(connections[i][0], connections[i][1]);
            } else {
                count += 1;
            }
        }
        int unionCount = uf.getCount();
        if (count < unionCount - 1)
            return -1;
        else
            return unionCount - 1;
    }
}
