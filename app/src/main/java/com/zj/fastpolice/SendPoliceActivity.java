package com.zj.fastpolice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.zj.base.BaseActivity;
import com.zj.bean.CaseData;
import com.zj.utils.NetUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendPoliceActivity extends BaseActivity {

    Bundle bundle;
    LatLng latLng;
    @Bind(R.id.send_police_address)
    EditText sendpoliceaddress;
    @Bind(R.id.send_police_icon)
    ImageView sendpoliceicon;
    @Bind(R.id.send_police_name)
    EditText sendpolicename;
    @Bind(R.id.send_police_phone)
    EditText sendpolicephone;
    @Bind(R.id.send_police_desc)
    EditText sendpolicedesc;
    @Bind(R.id.ll_progress)
    LinearLayout llprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_police);
        ButterKnife.bind(this);
        sendpoliceicon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_mail_send).sizeDp(20));
        Intent intent=getIntent();
        bundle=intent.getBundleExtra("mybundle");
        sendpoliceaddress.setText(bundle.getString("address"));
        latLng=bundle.getParcelable("location");
        //Toast.makeText(getApplication(),latLng.longitude+"",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.imageView_send)
    public void Click(View view)
    {
        CaseData data=new CaseData();
        data.setName(sendpolicename.getText().toString());
        data.setTelephone(sendpolicephone.getText().toString());
        data.setAddress(sendpoliceaddress.getText().toString());
        data.setLatitude(latLng.latitude);
        data.setLongtitude(latLng.longitude);
        data.setDescription(sendpolicedesc.getText().toString());
        data.setDealed(0);
        data.setPoliceid(0);
        if (data.getName().equals(""))
        {
            Toast.makeText(getApplication(),"不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        NetUtils.SendCaseData(llprogress,SendPoliceActivity.this,data);
    }

}
