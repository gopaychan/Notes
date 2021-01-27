import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import common.TestInterface;

// 给你一个由一些多米诺骨牌组成的列表 dominoes。
// 如果其中某一张多米诺骨牌可以通过旋转 0 度或 180 度得到另一张多米诺骨牌，我们就认为这两张牌是等价的。
// 形式上，dominoes[i] = [a, b] 和 dominoes[j] = [c, d] 等价的前提是 a==c 且 b==d，或是 a==d 且 b==c。
// 在 0 <= i < j < dominoes.length 的前提下，找出满足 dominoes[i] 和 dominoes[j] 等价的骨牌对 (i, j) 的数量。

// 示例：
// 输入：dominoes = [[1,2],[2,1],[3,4],[5,6]]
// 输出：1

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/number-of-equivalent-domino-pairs

public class NumEquivDominoPairs implements TestInterface {

    @Override
    public void test() {
        int[][] dominoes = new int[][] { { 1, 2 }, { 1, 2 }, { 1, 1 }, { 1, 2 }, { 2, 2 } };
        System.out.println(numEquivDominoPairs(dominoes));
    }

    public int numEquivDominoPairs(int[][] dominoes) {
        Map<String, Integer> list = new HashMap<>();
        int count = 0;
        for (int i = 0; i < dominoes.length; i++) {
            int[] dominoe = dominoes[i];
            String normalDominoeStr = "" + dominoe[0] + dominoe[1];
            String reverseDominoeStr = "" + dominoe[1] + dominoe[0];
            if (list.containsKey(normalDominoeStr)) {
                int add = list.get(normalDominoeStr);
                count += add;
                list.put(normalDominoeStr, add + 1);
                list.put(reverseDominoeStr, add + 1);
            } else {
                list.put(normalDominoeStr, 1);
                list.put(reverseDominoeStr, 1);
            }
        }
        return count;
    }

    public int numEquivDominoPairsV2(int[][] dominoes) {
        int count = 0;
        for (int i = 0; i < dominoes.length; i++) {
            for (int j = i + 1; j < dominoes.length; j++) {
                if ((dominoes[i][0] == dominoes[j][0] && dominoes[i][1] == dominoes[j][1])
                        || (dominoes[i][0] == dominoes[j][1] && dominoes[i][1] == dominoes[j][0])) {
                    count += 1;
                }
            }
        }
        return count;
    }

    public int numEquivDominoPairsV3(int[][] dominoes) {
        List<String> normalList = new ArrayList<>();
        List<String> reverseList = new ArrayList<>();

        for (int i = 0; i < dominoes.length; i++) {
            StringBuilder normal = new StringBuilder();
            StringBuilder reverse = new StringBuilder();
            for (int j = 0; j < dominoes[i].length; j++) {
                normal.append(dominoes[i][j]);
                reverse.append(dominoes[i][dominoes[i].length - 1 - j]);
            }
            normalList.add(normal.toString());
            reverseList.add(reverse.toString());
        }

        int count = 0;
        for (int i = 0; i < normalList.size(); i++) {
            for (int j = i + 1; j < normalList.size(); j++) {
                if (normalList.get(i).equals(normalList.get(j)) || normalList.get(i).equals(reverseList.get(j))) {
                    count += 1;
                }
            }
        }
        return count;
    }
}
