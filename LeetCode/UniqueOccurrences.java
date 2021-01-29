import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// 给你一个整数数组 arr，请你帮忙统计数组中每个数的出现次数。
// 如果每个数的出现次数都是独一无二的，就返回 true；否则返回 false。
// 示例 1：
// 输入：arr=[1,2,2,1,1,3]
// 输出：true
// 解释：在该数组中，1 出现了 3 次，2 出现了 2 次，3 只出现了 1 次。没有两个数的出现次数相同。

// 示例 2：
// 输入：arr=[1,2]
// 输出：false

// 示例 3：
// 输入：arr=[-3,0,1,-3,1,1,1,-3,10,0]
// 输出：true

public class UniqueOccurrences {

    public boolean uniqueOccurrences(int[] arr) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int i : arr) {
            int count = countMap.getOrDefault(i, 0);
            countMap.put(i, ++count);
        }
        List<Integer> countUnit = new ArrayList<>();
        Collection<Integer> values = countMap.values();
        for (Integer integer : values) {
            if (countUnit.contains(integer)) {
                return false;
            }
            countUnit.add(integer);
        }
        return true;
    }
}
