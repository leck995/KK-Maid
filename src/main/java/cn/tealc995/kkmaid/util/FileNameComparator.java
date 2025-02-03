package cn.tealc995.kkmaid.util;

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
            int cmp = parts1[i].compareToIgnoreCase(parts2[i]);
            if (cmp != 0) {
                // 如果是数字部分，进行数值比较
                if (isNumeric(parts1[i]) && isNumeric(parts2[i])) {
                    return Integer.compare(Integer.parseInt(parts1[i]), Integer.parseInt(parts2[i]));
                }
                return cmp; // 返回字符串比较结果
            }
        }
        return Integer.compare(parts1.length, parts2.length); // 长度比较
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
}