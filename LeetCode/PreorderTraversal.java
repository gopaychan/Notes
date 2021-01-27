import common.TreeNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

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

public class PreorderTraversal {
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        preorderInner(root, list);
        return list;
    }

    public void preorderInner(TreeNode header, List<Integer> result) {
        if (header == null)
            return;
        result.add(header.val);
        preorderInner(header.left, result);
        preorderInner(header.right, result);

    }

    public List<Integer> preorderTraversalv2(TreeNode root) {
        List<Integer> list = new ArrayList<>();
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
}