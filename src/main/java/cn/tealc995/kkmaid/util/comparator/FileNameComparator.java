package cn.tealc995.kkmaid.util.comparator;

import java.util.Comparator;

/**
 * @program: KK-Maid
 * @description: 文件名排序，仿照Win的文件管理器排序逻辑
 * @author: Leck
 * @create: 2025-02-03 19:39
 */
public class FileNameComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        return naturalOrder(s1, s2);
    }


    private int naturalOrder(String s1, String s2) {
        // 使用正则表达式将字符串分割为数字和非数字部分
        String[] parts1 = s1.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        String[] parts2 = s2.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

        for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
            int cmp;
            // 如果都是数字，进行数值比较
            if (isNumeric(parts1[i]) && isNumeric(parts2[i])) {
                cmp = Integer.compare(Integer.parseInt(parts1[i]), Integer.parseInt(parts2[i]));
            } else {
                // 否则，进行字符串比较
                cmp = parts1[i].compareTo(parts2[i]);
            }
            if (cmp != 0) {
                return cmp; // 返回比较结果
            }
        }
        // 如果到这里，说明前面部分相同，比较长度
        return Integer.compare(parts1.length, parts2.length);
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
}