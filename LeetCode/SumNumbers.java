import common.TreeNode;

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

public class SumNumbers {

    public static void main(String[] args) {

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
        int result = new SumNumbers().sumNumbers(node1);
        System.out.println("result=" + result);

        // StringBuilder stringBuilder = new StringBuilder("[");
        // for (int i : result) {
        // stringBuilder.append(i).append(",");
        // }
        // int start = stringBuilder.lastIndexOf(",");
        // stringBuilder.replace(start, start + 1, "]");
        // System.out.println(stringBuilder.toString());
    }

    public int sumNumbers(TreeNode root) {
        int[] sum = { 0 };
        collectNums(sum, 0, root);
        return sum[0];
    }

    public void collectNums(int[] result, int top_num, TreeNode curr) {
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
