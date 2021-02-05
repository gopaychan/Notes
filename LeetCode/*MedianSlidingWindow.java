// import java.util.Comparator;
// import java.util.PriorityQueue;
// import java.util.Queue;

// import common.Utils;

// // 中位数是有序序列最中间的那个数。如果序列的大小是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
// // 例如：
// // [2,3,4]，中位数是 3
// // [2,3]，中位数是 (2 + 3) / 2 = 2.5
// // 给你一个数组 nums，有一个大小为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
// //  
// // 示例：
// // 给出 nums = [1,3,-1,-3,5,3,6,7]，以及 k = 3。
// // 窗口位置                      中位数
// // ---------------               -----
// // [1  3  -1] -3  5  3  6  7       1
// //  1 [3  -1  -3] 5  3  6  7      -1
// //  1  3 [-1  -3  5] 3  6  7      -1
// //  1  3  -1 [-3  5  3] 6  7       3
// //  1  3  -1  -3 [5  3  6] 7       5
// //  1  3  -1  -3  5 [3  6  7]      6
// //  因此，返回该滑动窗口的中位数数组 [1,-1,-1,3,5,6]。

// // 来源：力扣（LeetCode）
// // 链接：https://leetcode-cn.com/problems/sliding-window-median

// public class MedianSlidingWindow {
//     public static void main(String[] args) {
//         Utils.printBooleanArray(
//                 new MedianSlidingWindow().medianSlidingWindow(new int[] { 1, 3, -1, -3, 5, 3, 6, 7 }, 3));
//     }

//     public double[] medianSlidingWindow(int[] nums, int k) {
//         Queue<int[]> bigTopHeap = new PriorityQueue<>(new Comparator<int[]>() {
//             public int compare(int[] arg0, int[] arg1) {
//                 return arg0[0] != arg1[0] ? arg1[0] - arg0[0] : arg0[1] - arg1[1];
//             };
//         });

//         Queue<int[]> smallTopHeap = new PriorityQueue<>(new Comparator<int[]>() {
//             public int compare(int[] arg0, int[] arg1) {
//                 return arg0[0] != arg1[0] ? arg0[0] - arg1[0] : arg0[1] - arg1[1];
//             };
//         });

//         double[] result = new double[nums.length - k + 1];
//         for (int i = 0; i < k; i++) {
//             bigTopHeap.add(new int[] { nums[i], i });
//         }
//         int half = k / 2;
//         boolean isOdd = half * 2 != k;
//         for (int i = 0; i < half; i++) {
//             smallTopHeap.add(bigTopHeap.poll());
//         }
//         if (isOdd) {
//             result[0] = bigTopHeap.peek()[0];
//         } else {
//             result[0] = (bigTopHeap.peek()[0] + smallTopHeap.peek()[0]) / 2.0;
//         }

//         int index = 1;
//         for (int i = k; i < nums.length; i++) {
//             int removeNum = nums[i - k];
//             if ()


//             bigTopHeap.add(new int[] { nums[i], i });
//             int[] num = bigTopHeap.poll();
//             if (num[1] <= i - k) {
                
//             }
//             smallTopHeap.add();
//             if (isOdd) {
//                 result[index++] = bigTopHeap.peek();
//             } else {
//                 result[index++] = (bigTopHeap.peek() + smallTopHeap.peek()) / 2.0;
//             }
//             // bigTopHeap.offer(num);
//             // if (bigTopHeap.size() > )
//         }

//         return result;
//     }
// }
