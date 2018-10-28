package io.flippedclassroom.server.im;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 群管理
 *
 * @author HASEE
 * @create 2018-10-22 10:05
 */
public class TeamController {
    public static void main(String[] args)throws Exception {
        create("dev1","owner",new String[]{"user1","user2","user3"},"welcome",0,0);
    }
    /**
     * @Author HASEE
     * @Description //TODO
     * @Date 13:33 2018/10/22
     * @Param magree:管理后台建群时，0不需要被邀请人同意加入群，1需要被邀请人同意才可以加入群。其它会返回414,
     *         joinmode：群建好后，sdk操作时，0不用验证，1需要验证,2不允许任何人加入。其它返回414
     * @return void
     **/
    public static void create(String t_name,String owner,String[] members,String msg,int magree,int joinmode)throws Exception{
        String url="https://api.netease.im/nimserver/team/create.action";
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("tname", t_name));
        pairs.add(new BasicNameValuePair("owner",owner));
        pairs.add(new BasicNameValuePair("members",new JSONArray(members).toString()));
        pairs.add(new BasicNameValuePair("msg",msg));
        pairs.add(new BasicNameValuePair("magree",magree+""));
        pairs.add(new BasicNameValuePair("joinmode",joinmode+""));
        Util.commonHttpRequest(url,pairs);
    }
}