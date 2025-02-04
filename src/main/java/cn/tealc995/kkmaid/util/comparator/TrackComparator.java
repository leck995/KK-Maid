package cn.tealc995.kkmaid.util.comparator;

import cn.tealc995.kikoreu.model.Track;

import java.util.Comparator;

/**
 * @program: KK-Maid
 * @description:
 * @author: Leck
 * @create: 2025-02-04 00:40
 */
public class TrackComparator implements Comparator<Track> {
    @Override
    public int compare(Track o1, Track o2) {
         if (o1.isFolder() && !o2.isFolder()) {
            return -1;
        }else if (o2.isFolder() && !o1.isFolder()) {
            return 1;
        }else {
            return naturalOrder(o1.getTitle(), o2.getTitle());
        }

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

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
}