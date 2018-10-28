package io.flippedclassroom.server.im;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 用于网易云信对用户的增删改查
 *
 * @author HASEE
 * @create 2018-10-22 9:33
 */
public class UserController {
    public static void main(String[] args) throws Exception {
        create("user4");

    }




    //创建网易云信用户，传入用户id
    public static void create(String user_id)throws Exception{
        String url = "https://api.netease.im/nimserver/user/create.action";
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("accid", user_id));
        Util.commonHttpRequest(url,pairs);

    }


}