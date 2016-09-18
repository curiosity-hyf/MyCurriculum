package com.curiosity.mycurriculum.util;

import com.curiosity.mycurriculum.bean.CourseInfo;
import com.curiosity.mycurriculum.bean.CurriculumInfo;
import com.curiosity.mycurriculum.bean.StudentInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 该工具类用于 Dom的处理
 */
public class InfoUtils {

    private InfoUtils(){}
    /**
     * 获取学生信息
     *
     * @param doc 学生信息页面
     * @return 学生信息实体
     */
    public static StudentInfo getStuInfo(Document doc) {
        StudentInfo info = new StudentInfo();
        info.setName(doc.select("span#xm").text());
        info.setStuNum(doc.select("span#xh").text());
        info.setInstitute(doc.select("span#lbl_xy").text());
        info.setMajor(doc.select("span#lbl_zymc").text());
        info.setClas(doc.select("span#lbl_xzb").text());
        info.setAdmission(doc.select("span#lbl_rxrq").text().substring(0, 4));
//        System.out.println(info.toString());
        return info;
    }

    /**
     * 获取 可用课表年份
     *
     * @param doc 课表页面
     * @return 可用课表年份列表
     */
    public static List<String> getYearsInfo(Document doc) {
        List<String> list = new ArrayList<>();
        Elements els = doc.select("select#xnd > option");
        for (Element e : els) {
            list.add(e.text());
        }
        return list;
    }

    /**
     * 获取所有课程信息
     * @param curriculums 所有课表 Dom
     * @return 所有课程信息
     */
    public static List<CurriculumInfo> getAllCurriInfo(List<Document> curriculums) {
        List<CurriculumInfo> curriculumInfos = new ArrayList<>();
        for (Document doc : curriculums) {
//        Document doc = curriculums.get(4);
            if (doc.body().toString().contains("您本学期课所选学分小于 1.50分")) {
                System.out.println("false!!!");
            }
//        System.out.println(doc.body().toString());
            doc = Jsoup.parse(doc.html().replace("<br><br>", "<br>").replace("<br>", "$$$")); //将每个子项用 $$$ 分隔，便于后续处理

            CurriculumInfo info = new CurriculumInfo();

            String year = doc.select("select#xnd > option[selected=selected]").text(); //获取当前学年
            String semester = doc.select("select#xqd > option[selected=selected]").text(); //获取当前学期
            info.setYear(year);
            info.setSemester(semester);

            List<CourseInfo> courseInfos = new ArrayList<>();

            Elements td = doc.select("table#Table1 > tbody > tr > td"); //获取待处理部分
            boolean skip = true;
            List<String> list = null;
            for (int i = 0; i < td.size(); ++i) {
                String s = td.get(i).text();
                if ("上午".equals(s) || "下午".equals(s) || "晚上".equals(s)) { //略过部分关键字
                    skip = false;
                    continue;
                } else if (skip) {
                    continue;
                }
                if ('第' == s.charAt(0)) { //新的一节
                    list = new ArrayList<>();
                }
                if (list == null) {
                    list = new ArrayList<>();
                } else {
                    list.add(s);
                }
                if (i + 1 < td.size() && '第' == td.get(i + 1).text().charAt(0)) { //结束一节标志
                    courseInfos.addAll(TextUtils.getCourse(list)); //处理当前节
                }
            }
            for (CourseInfo courseInfo : courseInfos) {
                System.out.println(courseInfo);
            }
        }
        return curriculumInfos;
    }

}
