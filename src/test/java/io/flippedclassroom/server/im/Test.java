package io.flippedclassroom.server.im;

import org.json.JSONObject;

/**
 * 描述:
 *
 * @author HASEE
 * @create 2018-10-22 20:05
 */
public class Test {


    public static void main(String[] args) {
        String a="{\"msg\":\"aaa\"}";
        System.out.println(new JSONObject(a).get("msg").toString());
    }
}