package com.zj.utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zj.bean.CaseData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jjx on 2016/6/6.
 */
public class NetUtils {

   static LinearLayout linearLayout;
  static  String url="http://119.29.200.122:8080/Police/InsertServlet?";
    static String updatePoliceurl="http://119.29.200.122:8080/Police/UpdatePoliceId?";
    static String updateDealedurl="http://119.29.200.122:8080/Police/UpdateDeald?";
    public static void SendCaseData(LinearLayout ll,final Context context,CaseData caseData)
    {
        linearLayout=ll;

        String data= null;
        try {

            data = "name="+caseData.getName()+"&telephone="+caseData.getTelephone()+"&address="+ URLEncoder.encode(caseData.getAddress(), "utf-8")+"&latitude="+caseData.getLatitude()+"&longtitude="+caseData.getLongtitude()+"&description="+URLEncoder.encode(caseData.getDescription(),"utf-8")+"&dealed="+0+"&policeid="+0;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url + data, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(context,"SUccess",Toast.LENGTH_SHORT).show();
                //linearLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                linearLayout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onStart() {
                super.onStart();
                linearLayout.setVisibility(View.VISIBLE);
            }



            @Override
            public void onStopped() {
                super.onStopped();
                linearLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(HttpException e, String s) {

                Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                //linearLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
            }
        });

    }


    public static void updatePoliceId(final Context context,int caseID,int policeID)
    {
        String data=null;
        data="caseid="+caseID+"&policeid="+policeID;
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, updatePoliceurl + data, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }


    public static void updateDeadID(final Context context,int caseID)
    {
        String data=null;
        data="caseid="+caseID;
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, updateDealedurl + data, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }




    public static List<CaseData> getListShop(String urlPath) throws Exception {

        List<CaseData> mlists = new ArrayList<CaseData>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        byte[] data = readParse(urlPath);

        JSONArray array = new JSONArray(new String(data));

        for (int i = 0; i < array.length(); i++) {

            JSONObject item = array.getJSONObject(i);

            int id=item.getInt("id");
            String mytime=item.getString("updatetime");
            Date date=sdf.parse(mytime);
            String name=item.getString("name");
             String telephone=item.getString("telephone");
             String address=item.getString("address");
             double latitude=item.getDouble("latitude");
             double longtitude=item.getDouble("longtitude");
             String description=item.getString("description");
             int dealed=item.getInt("dealed");
             int policeid=item.getInt("policeid");

            mlists.add(new CaseData(id, date, name,telephone,address,latitude,longtitude,description,dealed,policeid));


        }

        return mlists;

    }


    public static List<CaseData> getListByID(String urlPath) throws Exception {

        List<CaseData> mlists = new ArrayList<CaseData>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        byte[] data = readParse(urlPath);

        JSONArray array = new JSONArray(new String(data));

        for (int i = 0; i < array.length(); i++) {

            JSONObject item = array.getJSONObject(i);

            int id=item.getInt("id");
            String mytime=item.getString("updatetime");
            Date date=sdf.parse(mytime);
            String name=item.getString("name");
            String telephone=item.getString("telephone");
            String address=item.getString("address");
            double latitude=item.getDouble("latitude");
            double longtitude=item.getDouble("longtitude");
            String description=item.getString("description");
            int dealed=item.getInt("dealed");
            int policeid=item.getInt("policeid");

            mlists.add(new CaseData(id, date, name,telephone,address,latitude,longtitude,description,dealed,policeid));


        }

        return mlists;

    }

    /**

     * 从指定的url中获取字节数组

     *

     * @param urlPath

     * @return 字节数组

     * @throws Exception

     */

    public static byte[] readParse(String urlPath) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] data = new byte[1024];

        int len = 0;

        URL url = new URL(urlPath);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        InputStream inStream = conn.getInputStream();

        while ((len = inStream.read(data)) != -1) {

            outStream.write(data, 0, len);

        }

        inStream.close();

        return outStream.toByteArray();

    }

}
