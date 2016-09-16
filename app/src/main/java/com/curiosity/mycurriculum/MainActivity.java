package com.curiosity.mycurriculum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView im;
    private String cookie;
    private String viewstate;
    private Button btn;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        im = (ImageView) findViewById(R.id.code);
        et = (EditText) findViewById(R.id.title);
        btn = (Button) findViewById(R.id.login);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(
                            "http://www.curiooosity.com/asd.html")
                            .timeout(5000).get();
                    Elements links = doc.body().getElementsByTag("a");
                    for (Element link : links) {
                        Log.d("myd" , link.text() + " " + link.attr("href"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

       /* getCode();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myd", "login...!");
                Login(user, pwd, et.getText().toString());
            }
        });*/
    }

    private static String host = "http://jwgl.gdut.edu.cn";
    private static String codeUrl = "http://jwgl.gdut.edu.cn/CheckCode.aspx";
    private static String referUrl = "http://jwgl.gdut.edu.cn/default2.aspx";
    private static String mainUrl = "http://jwgl.gdut.edu.cn/xs_main.aspx?xh=";
    private static String user = "3114006092";
    private static String pwd = "hyf627507";


    /**
     * 获取验证码
     */
    public void getCode() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(codeUrl).build();

        final Call call = mOkHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        InputStream is = response.body().byteStream();

//						Log.d("myd", "succeed!");
                        final Bitmap bitmap = BitmapFactory.decodeStream(is);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                im.setImageBitmap(bitmap);
                            }
                        });
                        String c1 = response.header("Set-Cookie");

                        cookie = c1;
                        getViewState(host, cookie);
//						Log.d("myd", "code cookie:" + c1);
                    } else {
                        Log.d("myd", "failed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public String getViewState(String url, String cookie) {
        OkHttpClient okHttpClient1 = new OkHttpClient();
        Request request = new Request.Builder().url(host)
                .addHeader("Cookie", cookie)
                .build();
        Call call1 = okHttpClient1.newCall(request);
        try {
            Response response1 = call1.execute();
            InputStream is = response1.body().byteStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "gb2312"));
            String tmp;
            StringBuilder sb = new StringBuilder();

            while ((tmp = br.readLine()) != null) {
//                            Log.d("myd", tmp);
                sb.append(tmp + "\n");
            }
            viewstate = Jsoup.parse(sb.toString()).select("input[name=__VIEWSTATE]").val();
            Log.d("myd", viewstate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 登录
     *
     * @param user 用户名
     * @param pwd  密码
     * @param code 验证码
     */
    public void Login(final String user, String pwd, String code) {
        final OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder().followRedirects(false)
                .followSslRedirects(false).build();

        RequestBody requestBody = new FormBody.Builder()
                .add("__VIEWSTATE", viewstate)
                .add("txtUserName", user)
                .add("TextBox2", pwd)
                .add("txtSecretCode", code)
                .add("RadioButtonList1", "学生")
                .add("Button1", "")
                .add("lbLanguage", "")
                .add("hidPdrs", "")
                .add("hidsc", "").build();

//		Log.d("myd", "login cookie:" + cookie);
        final Request request = new Request.Builder().addHeader("Cookie", cookie).post(requestBody)
                .url(referUrl).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = mOkHttpClient.newCall(request).execute();
                    Log.d("myd", String.valueOf(response.code()));
                    if (response.code() == 302) {
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                        Request request1 = new Request.Builder().get().url(mainUrl + user)
                                .addHeader("Accept", "text/html")
                                .addHeader("Accept", "application/xhtml+xml")
                                .addHeader("Accept", "application/xml")
                                .addHeader("Accept-Encoding", "sdch")
                                .addHeader("Accept-Language", "zh-CN")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("Host", "jwgl.gdut.edu.cn")
                                .addHeader("User-Agent", "Mozilla/5.0")
                                .addHeader("Cookie", cookie)
                                .addHeader("Referer", referUrl).build();

                        Response response1 = okHttpClient.newCall(request1).execute();
                        Log.d("myd", "main: " + response1.code() + " " + "length: " + response1.body().contentLength());
//					if (response1.code() == 200) {
                        Log.d("myd", "succeed");
                        InputStream is = response1.body().byteStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is,
                                "gb2312"));
                        String tmp;
                        StringBuilder sb = new StringBuilder();

                        while ((tmp = br.readLine()) != null) {
                            Log.d("myd", tmp);
                            sb.append(tmp + "\n");
                        }
                        String name = Jsoup.parse(sb.toString()).getElementById("xhxm").text();
                        Log.d("myd", name);
//						Log.d("myd", sb.toString());
                    }
//
//					} else {
//						Log.d("myd", "haha");
//					}
                } catch (IOException e) {
                    Log.d("myd", "fail!!!");
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
