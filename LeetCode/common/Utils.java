package common;

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
}
