package cn.tealc995.kkmaid.util.comparator;

import cn.tealc995.kikoreu.model.Track;
import cn.tealc995.kkmaid.model.lrc.LrcFile;

import java.util.Comparator;

/**
 * @program: KK-Maid
 * @description:
 * @author: Leck
 * @create: 2025-02-04 00:40
 */
public class LrcFileComparator implements Comparator<LrcFile> {
    @Override
    public int compare(LrcFile o1, LrcFile o2) {
        return naturalOrder(o1.getTitle(), o2.getTitle());
    }
    private int naturalOrder(String s1, String s2) {
        s1 = convert(s1);
        s2 = convert(s2);
        int i1 = 0, i2 = 0;
        int len1 = s1.length(), len2 = s2.length();

        while (i1 < len1 && i2 < len2) {
            char c1 = s1.charAt(i1);
            char c2 = s2.charAt(i2);

            if (Character.isDigit(c1) && Character.isDigit(c2)) {
                // 比较数字部分
                int num1 = 0, num2 = 0;
                while (i1 < len1 && Character.isDigit(s1.charAt(i1))) {
                    num1 = num1 * 10 + (s1.charAt(i1) - '0');
                    i1++;
                }
                while (i2 < len2 && Character.isDigit(s2.charAt(i2))) {
                    num2 = num2 * 10 + (s2.charAt(i2) - '0');
                    i2++;
                }
                if (num1 != num2) {
                    return Integer.compare(num1, num2);
                }
            } else {
                // 比较字符部分
                if (c1 != c2) {
                    return Character.compare(c1, c2);
                }
                i1++;
                i2++;
            }
        }

        return Integer.compare(len1, len2);
    }

    /**
     * @description: 把全角字符转为半角字符
     * @param:	fullwidth
     * @return  java.lang.String
     * @date:   2025/2/4
     */
    public String convert(String fullwidth) {
        StringBuilder halfwidth = new StringBuilder();
        for (char c : fullwidth.toCharArray()) {
            // 处理全角字符（ASCII范围内）
            if (c >= 0xFF01 && c <= 0xFF5E) {
                halfwidth.append((char) (c - 0xFEE0));
            } else {
                halfwidth.append(c); // 其他字符不变
            }
        }
        return halfwidth.toString();
    }

}