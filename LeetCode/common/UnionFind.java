package common;

import java.util.Arrays;

public class UnionFind {
    int[] mParent;
    int[] mSz;
    int count = 0;

    public UnionFind(int n) {
        mParent = new int[n];
        mSz = new int[n];
        count = n;
        Arrays.fill(mSz, 1);
        for (int i = 0; i < n; i++) {
            mParent[i] = i;
            // mSz[i] = 1;
        }
    }

    public int root(int i) {
        while (mParent[i] != i) {
            mParent[i] = mParent[mParent[i]];
            i = mParent[i];
        }
        return i;
    }

    public boolean connected(int i, int j) {
        return root(i) == root(j);
    }

    public boolean union(int i, int j) {
        int rootI = root(i);
        int rootJ = root(j);
        if (rootI == rootJ)
            return false;
        if (mSz[rootI] >= mSz[rootJ]) {
            mParent[rootJ] = rootI;
            mSz[rootI] = mSz[rootI] + mSz[rootJ];
            mSz[rootJ] = 0;
        } else {
            mParent[rootI] = rootJ;
            mSz[rootJ] = mSz[rootI] + mSz[rootJ];
            mSz[rootI] = 0;
        }
        count -= 1;
        return true;
    }

    public int getCount() {
        return count;
    }

    public int getSameTopCount(int root) {
        return mSz[root];
    }

    public void print() {
        Utils.printIntegerArray(mParent);
    }
}
