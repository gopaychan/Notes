import java.lang.Math;

// 给定一个未经排序的整数数组，找到最长且 连续递增的子序列，并返回该序列的长度。
// 连续递增的子序列 可以由两个下标 l 和 r（l < r）确定，如果对于每个 l <= i < r，都有 nums[i] < nums[i + 1] ，那么子序列 [nums[l], nums[l + 1], ..., nums[r - 1], nums[r]] 就是连续递增子序列。

// 示例 1：

// 输入：nums = [1,3,5,4,7]
// 输出：3
// 解释：最长连续递增序列是 [1,3,5], 长度为3。
// 尽管 [1,3,5,7] 也是升序的子序列, 但它不是连续的，因为 5 和 7 在原数组里被 4 隔开。 

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/longest-continuous-increasing-subsequence

public class FindLengthOfLCIS {

   public static void main(String[] args) {
        System.out.println(new FindLengthOfLCIS().findLengthOfLCIS(new int[] { 1,3,5,7 }));
    }

    public int findLengthOfLCIS(int[] nums) {
        if (nums.length <= 1)
            return nums.length;
        int result = 1;
        int count = 1;
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] < nums[i + 1]) {
                count += 1;
            } else {
                result = Math.max(result, count);
                count = 1;
            }
        }
        result = Math.max(result, count);
        return result;
    }
}
