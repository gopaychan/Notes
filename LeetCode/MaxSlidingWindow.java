import java.util.Queue;

import common.Utils;

import java.util.PriorityQueue;
import java.util.Comparator;
// 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
// 返回滑动窗口中的最大值。
//  
// 示例 1：
// 输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
// 输出：[3,3,5,5,6,7]
// 解释：
// 滑动窗口的位置                最大值
// ---------------               -----
// [1  3  -1] -3  5  3  6  7       3
//  1 [3  -1  -3] 5  3  6  7       3
//  1  3 [-1  -3  5] 3  6  7       5
//  1  3  -1 [-3  5  3] 6  7       5
//  1  3  -1  -3 [5  3  6] 7       6
//  1  3  -1  -3  5 [3  6  7]      7

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/sliding-window-maximum

public class MaxSlidingWindow {
    public static void main(String[] args) {
        Utils.printIntegerArray(new MaxSlidingWindow().maxSlidingWindow(new int[] { 1, 3, -1, -3, 5, 3, 6, 7 }, 3));
    }

    public int[] maxSlidingWindow(int[] nums, int k) {
        Queue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer arg0, Integer arg1) {
                return arg1 - arg0;
            }
        });
        for (int i = 0; i < k; i++) {
            queue.offer(nums[i]);
        }
        System.out.println(queue);

        int[] result = new int[nums.length - k + 1];
        int index = 0;
        for (int i = 0; i < nums.length - k; i++) {
            result[index++] = queue.peek();
            queue.remove(nums[i]);
            queue.offer(nums[i + k]);
        }
        result[index] = queue.peek();
        return result;
    }
}
