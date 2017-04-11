package com.zy.gcode.utils;

import java.sql.Timestamp;

/**
 * Created by admin5 on 17/4/5.
 */
public class TappidUtils {
    private static final char[] toTappidTable = {
            'M', 'F', 'O', 'E', 'D', 'B', 'G', 'H', 'I', 'V', 'P', 'Z', 'A',
            'N', 'C', 'K', 'Q', 'e', 'S', 'T', 'i', 'J', 'W', 'X', 'l', 'm',
            '2', 'b', 'c', 'd', '8', '5', 'g', 'u', 'U', '9', 'j', 'y', 'L',
            'n', 'o', 'p', '3', '7', 's', 't', 'r', 'v', 'w', 'x', 'Y',
            '0', '1', 'a', 'q', '4', 'f', '6', 'h', 'R', 'k'};
    private static final int PLAIN_SERCET=176999825;

    public static String toTappid(long id, long time) {
        return transform(id) + "z" + transform(time);
    }

    public static TappidEntry deTappid(String tappid) {
        String[] strs = tappid.split("z");
        return new TappidEntry(to10(strs[0]), to10(strs[1]));
    }

    private static int charAt(char a) {
        int len = toTappidTable.length;
        for (int i = 0; i < len; i++) {
            if (a == toTappidTable[i]) {
                return i;
            }
        }
        return -1;
    }

    private static long to10(String str) {
        long num = 0;
        int len = toTappidTable.length;
        char[] ids = str.toCharArray();
        int j = 0;
        for (int i = ids.length - 1; i >= 0; i--) {
            int n = charAt(ids[j]);
            //   Du.pl(ids[j]+":"+n);
            j++;
            num += baseNum(len, i) * n;
        }
        return num ^ PLAIN_SERCET;
    }

    private static long baseNum(int len, int n) {
        long num = 1;
        for (int i = 0; i < n; i++) {
            num *= len;
        }
        return num;
    }


    private static String transform(long num) {
        int n = toTappidTable.length;
        num ^= PLAIN_SERCET;
        StringBuilder builder = new StringBuilder();
        while (num != 0) {//当输入的数不为0时循环执行求余和赋值
            Long remainder = num % n;
            num = num / n;
            builder.append(toTappidTable[remainder.intValue()]);
        }
        return builder.reverse().toString();
    }
    public static class TappidEntry {
        long userConfigId;
        long appOpenTime;

        public TappidEntry(long userConfigId, long appOpenTime) {
            this.userConfigId = userConfigId;
            this.appOpenTime = appOpenTime;
        }

        public long getUserConfigId() {
            return userConfigId;
        }

        public long getAppOpenTime() {
            return appOpenTime;
        }

        public Timestamp getAppOpenTimeTimeStamp() {
            return new Timestamp(appOpenTime);
        }
    }

}
