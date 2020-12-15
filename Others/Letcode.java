import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Collection;

public class Letcode {
    public static void main(String[] args) {
        int[] arr = new int[] { 2 };
        // boolean result = uniqueOccurrences(arr);
        // TreeNode.test();
        // boolean result = validMountainArray(arr);
        int result = monotoneIncreasingDigits(332);
        System.out.println("result=" + result);
    }

    public static int monotoneIncreasingDigits(int N) {
        String NStr = String.valueOf(N);
        char[] charArr = NStr.toCharArray();
        int index = charArr.length - 1;
        while (index > 0) {
            if (charArr[index] < charArr[index - 1]) {
                for (int i = index; i < charArr.length; i++) {
                    charArr[i] = '9';
                }
                if (charArr[index - 1] == '0')
                    charArr[index - 1] = '9';
                else
                    charArr[index - 1] -= 1;
            }
            index -= 1;
        }
        int result = 0;
        for (char c : charArr) {
            result = result * 10 + c - 48;
        }
        return result;
    }

    // 我们把数组 A 中符合下列属性的任意连续子数组 B 称为“山脉”：
    // B.length>=3
    // 存在 0<i<B.length-1 使得 B[0]<B[1]<...B[i-1]<B[i]>B[i+1]>...>B[B.length-1]
    // （注意：B 可以是 A 的任意子数组，包括整个数组 A。）
    // 给出一个整数数组 A，返回最长“山脉”的长度。
    // 如果不含有“山脉”则返回 0。
    // 示例 1：
    // 输入：[2,1,4,7,3,2,5]
    // 输出：5
    // 解释：最长的“山脉”是[1,4,7,3,2]，长度为 5。
    //
    // 示例 2：
    // 输入：[2,2,2]
    // 输出：0
    // 解释：不含“山脉”。
    public static int longestMountain(int[] A) {
        int[] left = new int[A.length];
        int[] right = new int[A.length];
        for (int i = 1; i < A.length; i++) {
            left[i] = A[i - 1] < A[i] ? left[i - 1] + 1 : 0;
        }

        for (int i = A.length - 2; i > 0; i--) {
            right[i] = A[i] > A[i + 1] ? right[i + 1] + 1 : 0;
        }
        int max = 0;
        for (int i = 0; i < A.length; i++) {
            max = Math.max(max, (left[i] == 0 || right[i] == 0) ? 0 : left[i] + right[i] + 1);
        }
        return max;
    }

    // 给你一个数组 nums，对于其中每个元素 nums[i]，请你统计数组中比它小的所有数字的数目。换而言之，对于每个 nums[i] 你必须计算出有效的 j 的数量，其中
    // j 满足 j!= i 且 nums[j]<nums[i] 。以数组形式返回答案。
    // 示例 1：
    // 输入：nums=[8,1,2,2,3]
    // 输出：[4,0,1,1,3]
    // 解释：对于 nums[0]=8 存在四个比它小的数字：（1，2，2 和 3）。对于 nums[1]=1 不存在比它小的数字。对于 nums[2]=2
    // 存在一个比它小的数字：（1）。对于 nums[3]=2 存在一个比它小的数字：（1）。对于 nums[4]=3 存在三个比它小的数字：（1，2 和 2）。
    //
    // 示例 2：
    // 输入：nums=[6,5,4,8]
    // 输出：[2,1,0,3]
    //
    // 示例 3：
    // 输入：nums=[7,7,7,7]
    // 输出：[0,0,0,0]
    public static int[] smallerNumbersThanCurrent(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i]))
                continue;
            for (int j = 0; j < nums.length; j++) {
                if (i == j)
                    continue;
                if (nums[i] > nums[j]) {
                    int tmp = map.getOrDefault(nums[i], 0);
                    map.put(nums[i], ++tmp);
                }
            }
            set.add(nums[i]);
        }
        int[] result = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            result[i] = map.getOrDefault(nums[i], 0);
        }
        return result;
    }

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
    public static boolean uniqueOccurrences(int[] arr) {
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

    // 给定一个包含 0 和 1 的二维网格地图，其中 1 表示陆地 0 表示水域。
    // 网格中的格子水平和垂直方向相连（对角线方向不相连）。整个网格被水完全包围，但其中恰好有一个岛屿（或者说，一个或多个表示陆地的格子相连组成的岛屿）。
    // 岛屿中没有“湖”（“湖” 指水域在岛屿内部且不和岛屿周围的水相连）。格子是边长为 1 的正方形。网格为长方形，且宽度和高度均不超过 100
    // 。计算这个岛屿的周长。
    // 示例 :
    // 输入:
    // [[0,1,0,0],startI
    // 输出: 16
    // public static int islandPerimeter(int[][] grid) {
    // int count = 0;
    // int startLength = -1;
    // int endLength = -1;
    // int startWidth = -1;
    // int endWidth = -1;
    // for (int i = 0; i < grid.length; i++) {
    // boolean start = false;
    // boolean middle = false;
    // for (int j = 0; j < grid[i].length; j++) {
    // if (grid[i][j] == 1) {
    // if (start && middle) {
    // count += 1;
    // // start = false;
    // middle = false;
    // System.out.println("i=" + i + " j=" + j);
    // } else {
    // start = true;
    // }
    // if (startWidth == -1) {
    // startWidth = j;
    // endWidth = j;
    // startLength = i;
    // endLength = i;
    // } else {
    // startWidth = Math.min(startWidth, j);
    // endWidth = Math.max(endWidth, j);
    // startLength = Math.min(startLength, i);
    // endLength = Math.max(endLength, i);
    // }
    // }
    // if (grid[i][j] == 0 && start) {
    // middle = true;
    // }
    // }
    // }
    // System.out.println("count=" + count);

    // for (int j = 0; j < grid[0].length; j++) {
    // boolean start = false;
    // boolean middle = false;
    // for (int i = 0; i < grid.length; i++) {
    // if (grid[i][j] == 1) {
    // if (start && middle) {
    // count += 1;
    // middle = false;
    // } else {
    // start = true;
    // }
    // }
    // if (grid[i][j] == 0 && start) {
    // middle = true;
    // }
    // }
    // }
    // System.out.println("count=" + count);
    // return (endLength - startLength + 1 + endWidth - startWidth + 1 + count) * 2;
    // }

    static int[] dx = { 0, 1, 0, -1 };
    static int[] dy = { 1, 0, -1, 0 };

    public static int islandPerimeterv2(int[][] grid) {
        int n = grid.length, m = grid[0].length;
        int ans = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (grid[i][j] == 1) {
                    int cnt = 0;
                    for (int k = 0; k < 4; ++k) {
                        int tx = i + dx[k];
                        int ty = j + dy[k];
                        if (tx < 0 || tx >= n || ty < 0 || ty >= m || grid[tx][ty] == 0) {
                            cnt += 1;
                        }
                    }
                    ans += cnt;
                }
            }
        }
        return ans;
    }

    public static boolean validMountainArray(int[] A) {
        boolean up = false, down = false;
        for (int i = 1; i < A.length; i++) {
            if (A[i - 1] < A[i]) {
                if (down)
                    return false;
                up = true;
            } else if (A[i - 1] > A[i]) {
                if (!up && !down)
                    return false;
                else if (up) {
                    down = true;
                    up = false;
                }
            } else {
                return false;
            }
        }
        return !up && down;
    }

    public static void printIntegerArray(int[] array) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i : array) {
            stringBuilder.append(i).append(",");
        }
        int start = stringBuilder.lastIndexOf(",");
        stringBuilder.replace(start, start + 1, "]");
        System.out.println(stringBuilder.toString());
    }
}

// 请判断一个链表是否为回文链表。
// 示例 1:
// 输入:1->2
// 输出:false
//
// 示例 2:
// 输入:1->2->2->1
// 输出:true
// 进阶：你能否用 O(n),时间复杂度和 O(1) 空间复杂度解决此题？
class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }

    public static void test() {
        ListNode node0 = new ListNode(0);
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(1);
        ListNode node3 = new ListNode(0);
        node0.next = node1;
        node1.next = node2;
        node2.next = node3;
        boolean result = ListNode.isPalindrome(node0);
        System.out.println("result = " + result);
    }

    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null)
            return true;
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null) {
            fast = fast.next;
            if (fast != null) {
                fast = fast.next;
            }
            slow = slow.next;
        }

        ListNode pre = null;
        ListNode next = slow.next;
        slow.next = pre;
        while (next != null) {
            pre = slow;
            slow = next;
            next = slow.next;
            slow.next = pre;
        }

        do {
            if (slow.val != head.val) {
                return false;
            }
            slow = slow.next;
            head = head.next;
        } while (slow != null);

        return true;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public static void test() {

        TreeNode node9 = new TreeNode(9);
        // TreeNode node10 = new TreeNode(10);
        TreeNode node5 = new TreeNode(5);
        TreeNode node6 = new TreeNode(6);
        TreeNode node7 = new TreeNode(7);
        TreeNode node8 = new TreeNode(8, node9, null);
        TreeNode node4 = new TreeNode(4, node7, node8);
        TreeNode node3 = new TreeNode(3, node6, null);
        TreeNode node2 = new TreeNode(2, node4, node5);
        TreeNode node1 = new TreeNode(1, node2, node3);
        // List<Integer> result = preorderTraversalv2(node1);
        int result = sumNumbers(node1);
        System.out.println("result=" + result);

        // StringBuilder stringBuilder = new StringBuilder("[");
        // for (int i : result) {
        // stringBuilder.append(i).append(",");
        // }
        // int start = stringBuilder.lastIndexOf(",");
        // stringBuilder.replace(start, start + 1, "]");
        // System.out.println(stringBuilder.toString());
    }

    // 给定一个二叉树，返回它的 前序 遍历。
    // 示例:
    // 输入: [1,null,2,3]
    // 1
    // \
    // 2
    // /
    // 3
    // 输出: [1,2,3]
    // 进阶: 递归算法很简单，你可以通过迭代算法完成吗？
    // Definition for a binarytree node.
    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList();
        preorderInner(root, list);
        return list;
    }

    public static void preorderInner(TreeNode header, List<Integer> result) {
        if (header == null)
            return;
        result.add(header.val);
        preorderInner(header.left, result);
        preorderInner(header.right, result);

    }

    public static List<Integer> preorderTraversalv2(TreeNode root) {
        List<Integer> list = new ArrayList();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode header = root;
        while (header != null || !stack.empty()) {
            if (header == null)
                header = stack.pop();
            list.add(header.val);
            if (header.right != null) {
                stack.push(header.right);
            }
            header = header.left;
        }
        return list;
    }

    /********************************************************************************/

    // 给定一个二叉树，它的每个结点都存放一个 0-9 的数字，每条从根到叶子节点的路径都代表一个数字。
    // 例如，从根到叶子节点路径 1->2->3 代表数字 123。
    // 计算从根到叶子节点生成的所有数字之和。
    // 说明: 叶子节点是指没有子节点的节点。
    // 示例 1:
    // 输入: [1,2,3]
    // 1
    // / \
    // 2 3
    // 输出: 25
    // 解释:
    // 从根到叶子节点路径 1->2 代表数字 12.
    // 从根到叶子节点路径 1->3 代表数字 13.
    // 因此，数字总和 = 12 + 13 = 25.
    public static int sumNumbers(TreeNode root) {
        int[] sum = { 0 };
        collectNums(sum, 0, root);
        return sum[0];
    }

    public static void collectNums(int[] result, int top_num, TreeNode curr) {
        if (curr == null)
            return;
        int tmp = top_num * 10 + curr.val;
        if (curr.left == null && curr.right == null)
            result[0] += tmp;
        else {
            collectNums(result, tmp, curr.left);
            collectNums(result, tmp, curr.right);
        }
    }
}