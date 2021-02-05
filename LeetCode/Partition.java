import java.util.List;
import common.ListNode;

// 给你一个链表和一个特定值 x ，请你对链表进行分隔，使得所有小于 x 的节点都出现在大于或等于 x 的节点之前。
// 你应当保留两个分区中每个节点的初始相对位置。
//  
// 示例：
// 输入：head = 1->4->3->2->5->2, x = 3
// 输出：1->2->2->4->3->5

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/partition-list

public class Partition {
    public static void main(String[] args) {
        ListNode node0 = new ListNode(2);
        ListNode node1 = new ListNode(5, node0);
        ListNode node2 = new ListNode(2, node1);
        ListNode node3 = new ListNode(3, node2);
        ListNode node4 = new ListNode(4, node3);
        ListNode node5 = new ListNode(1, node4);
        ListNode result = new Partition().partition(node5, 3);
        while (result != null) {
            System.out.println(result.val);
            result = result.next;
        }
    }

    public ListNode partition(ListNode head, int x) {
        ListNode cur = head;
        ListNode smallTail = null;
        ListNode otherTail = null;
        ListNode otherHead = null;
        while (cur != null) {
            if (cur.val >= x) {
                if (otherHead == null) {
                    otherHead = cur;
                    otherTail = otherHead;
                } else {
                    otherTail.next = cur;
                    otherTail = cur;
                }
            } else {
                if (smallTail == null) {
                    head = cur;
                    smallTail = head;
                } else {
                    smallTail.next = cur;
                    smallTail = cur;
                }
            }
            cur = cur.next;
        }
        if (otherTail != null)
            otherTail.next = null;
        if (smallTail != null) {
            if (otherHead != null) {
                smallTail.next = otherHead;
            }
            return head;
        } else {
            return otherHead;
        }
    }
}
