package com.curiosity.mycurriculum.util;

import com.curiosity.mycurriculum.bean.CourseInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 该工具类用于 文本的处理
 */
public class TextUtils {

    private TextUtils() {}

    private static String getName(String s) {
        return s;
    }

    private static int getDayOfWeek(String s) {
        char w = s.charAt(1);
        switch (w) {
            case '一':
                return 1;
            case '二':
                return 2;
            case '三':
                return 3;
            case '四':
                return 4;
            case '五':
                return 5;
            case '六':
                return 6;
            case '日':
                return 7;
            default:
                return 0;
        }
    }

    private static int getTimeFrom(String s) {
//        System.out.println(s);
        String ss = s.substring(3, s.indexOf("节"));
        String[] sss = ss.split(",");
        return Integer.valueOf(sss[0]);
    }

    private static int getTimeTo(String s) {
        String ss = s.substring(3, s.indexOf("节"));
        String[] sss = ss.split(",");
        return Integer.valueOf(sss[sss.length - 1]);
    }

    private static int getWeekFrom(String s) {
        return Integer.valueOf(s.substring(s.lastIndexOf("第") + 1, s.indexOf("-")));
    }

    private static int getWeekTo(String s) {
        return Integer.valueOf(s.substring(s.indexOf("-") + 1, s.indexOf("周", s.indexOf("-"))));
    }

    private static String getTechName(String s) {
        return s;
    }

    private static String getClassNum(String s) {
        return s;
    }

    private static int getWeekFlag(String s) {
        String[] ss = s.split("\\|");
        if (ss.length == 1) {
            return 0;
        } else if (ss[1].contains("单")) {
            return 1;
        } else if (ss[1].contains("双")) {
            return 2;
        } else {
            return 0;
        }
    }

    private static List<String> getChangeType(String s) {
        return new ArrayList<>(Arrays.asList(s.split("、")));
    }

    /**
     * 获取某节一整周的课程 e.g. 周一到周日的 每天第一节
     *
     * @param list 未处理的 某节一整周的课程
     * @return 处理好的 某节一整周的课程
     */
    public static List<CourseInfo> getCourse(List<String> list) {
        List<CourseInfo> info = new ArrayList<>();
        int tb = Integer.valueOf(list.get(0).substring(1, list.get(0).length() - 1)); //起始节
        for (int i = 1; i < list.size(); ++i) { //i 表示周几
            String s = list.get(i);
            System.out.println(s);
            if (" ".equals(s)) //没课
                continue;
            info.addAll(getCourse(list.get(i), i, tb));
        }
        return info;
    }

    /**
     * 获取周几某节的所有课程 e.g. 周三第一节
     *
     * @param s     未处理的 周几某节的所有课程信息
     * @param dow   周几
     * @param begin 某节起始
     * @return 处理完毕的 周几某节的所有课程信息
     */
    private static List<CourseInfo> getCourse(String s, int dow, int begin) {
        List<CourseInfo> list = new ArrayList<>();
        String str[] = s.split("\\$\\$\\$"); //以$$$分隔

        for (int i = 0; i < str.length; ) {
            CourseInfo info = new CourseInfo();

            info.setDayOfWeek(dow); //指定默认 周几
            info.setTimeFrom(begin); //指定默认 某节起始
            info.setTimeTo(begin + 1); //指定默认 某节终止

            info.setName(getName(str[i])); //设置课程名
            //排除 诸如 {第1-9周} 而非 周一第1,2节{第7-9周} 的情况
            if (i + 1 < str.length && !"".equals(str[i + 1]) && '{' != str[i + 1].charAt(0)) {
                info.setDayOfWeek(getDayOfWeek(str[i + 1])); //设置 周几
                info.setTimeFrom(getTimeFrom(str[i + 1])); //设置 起始时间
                info.setTimeTo(getTimeTo(str[i + 1])); //设置 终止时间
            }
            info.setWeekFrom(getWeekFrom(str[i + 1])); //设置 起始周
            info.setWeekTo(getWeekTo(str[i + 1])); //设置 终止周
            info.setWeekFlag(getWeekFlag(str[i + 1])); //设置 单双周标志位
            info.setTechName(getTechName(str[i + 2])); //设置 教师名称
            info.setClassNum(getClassNum(str[i + 3])); //设置 课时
            //检查特殊情况 如 垃圾信息 调课信息
            if (i + 4 < str.length && !"".equals(str[i + 4])) {
                if (Character.isDigit(str[i + 4].charAt(0)) || '第' == str[i + 4].charAt(0)) { //排除垃圾信息
                    i += 2;
                }
                if (i + 4 < str.length && !"".equals(str[i + 4]) && '(' == str[i + 4].charAt(0)) { //考虑是否有课程变化情况
                    info.setChangeFlag(1); //设置变化标志位
                    info.setChangeType(getChangeType(str[i + 4])); //设置 变化情况
                    i += 1;
                }
            }
            i += 4;
            list.add(info);
        }
        return list;
    }
}
