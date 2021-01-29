import common.ListNode;

// 请判断一个链表是否为回文链表。
// 示例 1:
// 输入:1->2
// 输出:false
//
// 示例 2:
// 输入:1->2->2->1
// 输出:true
// 进阶：你能否用 O(n),时间复杂度和 O(1) 空间复杂度解决此题？

public class IsPalindrome {
    public static void main(String[] args) {
        ListNode node0 = new ListNode(0);
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(1);
        ListNode node3 = new ListNode(0);
        node0.next = node1;
        node1.next = node2;
        node2.next = node3;
        boolean result = new IsPalindrome().isPalindrome(node0);
        System.out.println("result = " + result);
    }

    public boolean isPalindrome(ListNode head) {
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
