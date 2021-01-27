import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 给定一种规律 pattern 和一个字符串 str ，判断 str 是否遵循相同的规律。
// 这里的 遵循 指完全匹配，例如， pattern 里的每个字母和字符串 str 中的每个非空单词之间存在着双向连接的对应规律。

// 示例1:
// 输入: pattern = "abba", str = "dog cat cat dog"
// 输出: true

// 示例 2:
// 输入:pattern = "abba", str = "dog cat cat fish"
// 输出: false

// 示例 3:
// 输入: pattern = "aaaa", str = "dog cat cat dog"
// 输出: false

// 示例 4:
// 输入: pattern = "abba", str = "dog dog dog dog"
// 输出: false

public class WordPattern {

    public static boolean wordPattern(String pattern, String s) {
        char[] patternArr = pattern.toCharArray();
        String[] sArr = s.split(" ");
        if (patternArr.length != sArr.length)
            return false;

        Map<Character, List<Integer>> patternMap = new HashMap<>();
        Map<String, List<Integer>> sMap = new HashMap<>();

        for (int i = 0; i < patternArr.length; i++) {
            char c = patternArr[i];
            List<Integer> patternList = patternMap.get(c);
            if (patternList == null) {
                patternList = new ArrayList<>();
                patternMap.put(c, patternList);
            }
            patternList.add(i);

            String string = sArr[i];
            List<Integer> sList = sMap.get(string);
            if (sList == null) {
                sList = new ArrayList<>();
                sMap.put(string, sList);
            }
            sList.add(i);
        }

        for (int i = 0; i < patternMap.size(); i++) {
            List<Integer> patternList = patternMap.get(patternArr[i]);
            List<Integer> sList = sMap.get(sArr[i]);
            if (patternList.size() != sList.size())
                return false;
            for (int j = 0; j < patternList.size(); j++) {
                int patternI = patternList.get(j);
                int sI = sList.get(j);
                if (patternI != sI) {
                    return false;
                }
            }
        }

        return true;
    }
}
