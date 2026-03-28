package com.bs.mall.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Util {
    // 中国大陆手机号运营商号段（截至2024年）
    private static final Set<String> VALID_PREFIXES = new HashSet<>();

    static {
        // 中国移动
        String[] mobilePrefixes = {
                "134", "135", "136", "137", "138", "139",
                "147", "148", "150", "151", "152", "157",
                "158", "159", "165", "172", "178", "182",
                "183", "184", "187", "188", "195", "197", "198"
        };

        // 中国联通
        String[] unicomPrefixes = {
                "130", "131", "132", "133", "145", "146",
                "155", "156", "166", "167", "171", "175",
                "176", "185", "186", "196"
        };

        // 中国电信
        String[] telecomPrefixes = {
                "133", "149", "153", "173", "177", "180",
                "181", "189", "191", "193", "199"
        };

        // 虚拟运营商（170号段）
        String[] virtualPrefixes = {
                "170", "171"
        };

        // 添加所有号段到集合
        addPrefixes(mobilePrefixes);
        addPrefixes(unicomPrefixes);
        addPrefixes(telecomPrefixes);
        addPrefixes(virtualPrefixes);
    }

    private static void addPrefixes(String[] prefixes) {
        for (String prefix : prefixes) {
            VALID_PREFIXES.add(prefix);
        }
    }

    // 回false表示数据符合规范，返回true表示数据不符合规范
    public static boolean isValidPhone(String phone) {
        // 基础格式验证
        if (phone == null || phone.trim().isEmpty()) {
            return true;
        }
        if (!phone.matches("\\d{11}")) {
            return true;
        }
        // 验证号段（前3位）
        String prefix = phone.substring(0, 3);
        return !VALID_PREFIXES.contains(prefix);
    }


    // 回false表示数据符合规范，返回true表示数据不符合规范
    public static boolean isValidGender(String gender) {
        // 基础格式验证
        if (gender == null || gender.trim().isEmpty()) {
            return true;
        }
        return !gender.matches("[01]");
    }


    // 验证注册数据符合规范
    // 回false表示数据符合规范，返回true表示数据不符合规范
    public static boolean validData(String regex, String data) {
        if (data == null || data.trim().isEmpty()){
            return true;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        return !matcher.matches();
    }

    // 验证出生日期有效，今天或以前
    // 回false表示数据符合规范，返回true表示数据不符合规范
    public static boolean validBirthday(String dataStr) {
        if (dataStr == null || dataStr.trim().isEmpty()) {
            return true;
        }
        try {
            String[] parts = dataStr.split("-");
            if (parts.length != 3) return true;

            LocalDate date = LocalDate.of(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])
            );
            return date.isAfter(LocalDate.now());
        } catch (NumberFormatException | DateTimeException e) {
            return true;
        }
    }
}

