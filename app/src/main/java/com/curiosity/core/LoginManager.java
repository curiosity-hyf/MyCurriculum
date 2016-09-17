package com.curiosity.core;

import com.curiosity.bean.CurriculumInfo;
import com.curiosity.exception.VerifyException;
import com.curiosity.util.InfoUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class LoginManager {

    private static final int TIME_OUT = 60 * 1000;

    private static Map<String, String> cookie = null; //存放cookie

    private static Map<String, String> postData = null; //登录时post 数据

    private static String viewState = null; //登录页面隐藏字段

    private static String aco = "";
    private static String pwwwd = "";

    private static LoginManager manager = new LoginManager();

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        return manager;
    }

    /**
     * 输入信息 及 登录
     * @throws IOException
     * @throws VerifyException
     */
    public void accessView() throws IOException, VerifyException {
        postData = new HashMap<>();
        getVerifyCode();

        postData.put("__VIEWSTATE", viewState);
        postData.put("RadioButtonList1", "学生");
        postData.put("Button1", "");
        postData.put("lbLanguage", "");
        postData.put("hidPdrs", "");
        postData.put("hidsc", "");

        Scanner scanner = new Scanner(new BufferedInputStream(System.in));
//        System.out.print("Input account: ");
//        postData.put("txtUserName", scanner.next());
//        System.out.print("Input password: ");
//        postData.put("TextBox2", scanner.next());
        postData.put("txtUserName", aco);

        postData.put("TextBox2", pwwwd);
        System.out.print("Input verifyCode: ");
        postData.put("txtSecretCode", scanner.next());
        scanner.close();
        getAllInfo();
    }

    /**
     * 获取验证码
     */
    private static void getVerifyCode() throws IOException {
        Connection.Response response = Jsoup.connect("http://jwgl.gdut.edu.cn/CheckCode.aspx")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
                .timeout(TIME_OUT).ignoreContentType(true).execute();

        cookie = response.cookies();
        FileOutputStream fos = new FileOutputStream(new File("images/code.jpg"));
        fos.write(response.bodyAsBytes());
        getViewState();

        fos.close();
        fos = null;

    }

    /**
     * 获取登录页面隐藏字段 __VIEWSTATE
     */
    private static void getViewState() throws IOException {
        viewState = Jsoup.connect("http://jwgl.gdut.edu.cn/")
                .cookies(cookie)
                .timeout(10000)
                .get().select("input[name=__VIEWSTATE]").get(0).attr("value");
    }

    /**
     * 验证登录
     *
     * @return 状态码是否为 302  是则验证成功 否则验证失败
     * @throws IOException
     */
    private boolean getAccess() throws IOException{
        Connection.Response response = Jsoup
                .connect("http://jwgl.gdut.edu.cn/default2.aspx")
                .cookies(cookie)
                .data(postData)
                .timeout(TIME_OUT)
                .followRedirects(false)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
                .execute();
        return response.statusCode() == 302;
    }

    private Document getFirstCurr() throws IOException {
        Document doc = Jsoup
                .connect("http://jwgl.gdut.edu.cn/xskbcx.aspx?xh=" + aco + "&xm=学生个人课表&gnmkdm=N121603")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
                .cookies(cookie)
                .referrer("http://jwgl.gdut.edu.cn/xs_main.aspx?xh=" + aco)
                .timeout(10000)
                .post();
        return doc;
    }

    /**
     * 获取各个学期课表
     *
     * @param doc      上一个学期的dom  *因为要用到上一个学期dom页面中的 隐藏表单 "__VIEWSTATE"
     * @param year     课表年份
     * @param semester 课表学期
     * @return 课表dom
     * @throws IOException
     */
    private Document getCurri(Document doc, String year, String semester) throws IOException {
        String s = doc.select("input[name=__VIEWSTATE]").get(0).attr("value");
        Document doc2 = Jsoup
                .connect("http://jwgl.gdut.edu.cn/xskbcx.aspx?xh=" + aco + "&xm=学生个人课表&gnmkdm=N121603")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
                .cookies(cookie)
                .data("__EVENTTARGET", "xnd")
                .data("__EVENTARGUMENT", "")
                .data("__VIEWSTATE", s)
                .data("xnd", year)
                .data("xqd", semester)
                .referrer("http://jwgl.gdut.edu.cn/xskbcx.aspx?xh=" + aco + "&xm=学生个人课表&gnmkdm=N121603")
                .timeout(TIME_OUT)
                .post();
//        System.out.println(doc2.select("select#xnd > option[selected=selected]").text());
//        System.out.println(doc2.select("select#xqd > option[selected=selected]").text());
        return doc2;
    }

    private List<Document> getAllCurriDom() throws IOException {
        List<Document> curriculums = new ArrayList<>();
        Document docTemp = getFirstCurr();
        List<String> years = InfoUtils.getYearsInfo(docTemp);
        Collections.reverse(years);
        for (String year : years) {
            Document document1 = getCurri(docTemp, year, "1");
            docTemp = document1;
            curriculums.add(document1);
            Document document2 = getCurri(docTemp, year, "2");
            docTemp = document2;
            curriculums.add(document2);
        }
        return curriculums;
    }

    /**
     * 加载学生信息页面
     *
     * @return 学生信息页面Dom
     * @throws IOException
     */
    private Document getStuInfoDoc() throws IOException {
        Document doc = Jsoup
                .connect("http://jwgl.gdut.edu.cn/xsgrxx.aspx?xh=" + aco + "&xm=%BB%C6%D2%BB%B7%AB&gnmkdm=N121501")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
                .cookies(cookie)
                .referrer("http://jwgl.gdut.edu.cn/xs_main.aspx?xh=" + aco)
                .timeout(TIME_OUT)
                .get();
        System.out.println(doc.body().toString().getBytes().length == 64); //64时说明炸了！
        return doc;
    }

    private List<CurriculumInfo> curriculumInfos = null;
    private List<Document> curriculums = null;

    private void getAllInfo() throws IOException, VerifyException {
        if (getAccess()) {
            InfoUtils.getStuInfo(getStuInfoDoc());
            curriculums = getAllCurriDom();
            curriculumInfos = InfoUtils.getAllCurriInfo(curriculums);
        } else {
            throw new VerifyException();
        }
    }
}
