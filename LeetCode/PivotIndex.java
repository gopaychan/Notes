// 给定一个整数类型的数组 nums，请编写一个能够返回数组 “中心索引” 的方法。
// 我们是这样定义数组 中心索引 的：数组中心索引的左侧所有元素相加的和等于右侧所有元素相加的和。
// 如果数组不存在中心索引，那么我们应该返回 -1。如果数组有多个中心索引，那么我们应该返回最靠近左边的那一个。

// 示例 1：
// 输入：
// nums = [1, 7, 3, 6, 5, 6]
// 输出：3
// 解释：
// 索引 3 (nums[3] = 6) 的左侧数之和 (1 + 7 + 3 = 11)，与右侧数之和 (5 + 6 = 11) 相等。
// 同时, 3 也是第一个符合要求的中心索引。

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/find-pivot-index

public class PivotIndex {

    public static void main(String[] args) {
        System.out.println(new PivotIndex().pivotIndex(new int[] { -1, -1, -1, 0, 1, 1 }));
    }

    public int pivotIndex(int[] nums) {
        if (nums.length == 0)
            return -1;
        else if (nums.length == 1)
            return 0;

        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum - nums[0] == 0)
            return 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[0] * 2 == sum - nums[i])
                return i;
            else
                nums[0] += nums[i];
        }
        return -1;
    }
}
