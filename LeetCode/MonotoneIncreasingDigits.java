// 给定一个非负整数 N，找出小于或等于 N 的最大的整数，同时这个整数需要满足其各个位数上的数字是单调递增。

// （当且仅当每个相邻位数上的数字 x 和 y 满足 x <= y 时，我们称这个整数是单调递增的。）

// 示例 1:
// 输入: N = 10
// 输出: 9

// 示例 2:
// 输入: N = 1234
// 输出: 1234

// 示例 3:
// 输入: N = 332
// 输出: 299

public class MonotoneIncreasingDigits {
    
    public int monotoneIncreasingDigits(int N) {
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
}
