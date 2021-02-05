// 给你一个仅由大写英文字母组成的字符串，你可以将任意位置上的字符替换成另外的字符，总共可最多替换 k 次。在执行上述操作后，找到包含重复字母的最长子串的长度。

// 注意：字符串长度 和 k 不会超过 104。

//  

// 示例 1：

// 输入：s = "ABAB", k = 2
// 输出：4
// 解释：用两个'A'替换为两个'B',反之亦然。

// 来源：力扣（LeetCode）
// 链接：https://leetcode-cn.com/problems/longest-repeating-character-replacement

public class CharacterReplacement {
    public static void main(String[] args) {
        System.out.println(new CharacterReplacement().characterReplacement("ABAA", 0));
    }

    public int characterReplacement(String s, int k) {
        int[] nums = new int[26];
        int left = 0;
        int right = 0;
        int max = 0;
        int n = s.length();
        while (right < n) {
            nums[s.charAt(right) - 'A']++;
            max = Math.max(nums[s.charAt(right) - 'A'], max);
            if (right - left + 1 - max > k) {
                nums[s.charAt(left++) - 'A']--;
            }
            right++;
        }
        return right - left;
    }
}
