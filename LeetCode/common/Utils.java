package common;

import java.util.List;
import java.util.ArrayList;

public class Utils {

    public static void printIntegerArray(int[] array) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i : array) {
            stringBuilder.append(i).append(",");
        }
        int start = stringBuilder.lastIndexOf(",");
        stringBuilder.replace(start, start + 1, "]");
        System.out.println(stringBuilder.toString());
    }

    public static int[][] stringToIntArray(String string) {
        // System.out.println(string);
        string = string.replaceAll(" +", "");
        string = string.substring(1, string.length() - 1);
        // System.out.println(string);

        List<List<Integer>> list = new ArrayList<>();
        String[] stringArr = string.split("],");
        for (String s : stringArr) {
            // System.out.println(s);
            s = s.replace("[", "").replace("]", "");
            String[] numStringArr = s.split(",");
            List<Integer> l = new ArrayList<>();
            for (String numStr : numStringArr) {
                // System.out.println(numStr);
                if (numStr.equals(""))
                    continue;
                l.add(Integer.valueOf(numStr));
            }
            list.add(l);
        }
        int[][] result = new int[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            int[] intArr = new int[list.get(i).size()];
            for (int j = 0; j < list.get(i).size(); j++) {
                intArr[j] = list.get(i).get(j);
            }
            // printIntegerArray(intArr);
            result[i] = intArr;
        }
        return result;
    }
}
