package com.zj.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.base.BaseFragment;
import com.zj.base.FastPoliceApplication;
import com.zj.bean.CaseData;
import com.zj.custom.WhiteCouponView;
import com.zj.fastpolice.R;
import com.zj.fastpolice.RadarActivity;
import com.zj.utils.NetUtils;

import java.text.SimpleDateFormat;
import java.util.List;


public class CaseListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
   public final int SUCCESS=1;
    public final int FAILED=0;
    private boolean mIsFirstCreated = true;
    ListView listView;
    List<CaseData> caseList;
    SwipeRefreshLayout swipeRefreshLayout;
    //http://119.29.200.122   192.168.1.109
    String queryAllUrlPath="http://119.29.200.122:8080/Police/QueryAll";
    String queryByPoliceID="http://119.29.200.122:8080/Police/QueryByPoliceID";

    int[] imageIds=new int[]{R.mipmap.wall01,R.mipmap.wall02,R.mipmap.wall03,R.mipmap.wall04,R.mipmap.wall05,R.mipmap.wall06,R.mipmap.wall07,R.mipmap.wall08,R.mipmap.wall09,R.mipmap.wall10};
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case SUCCESS:
                    swipeRefreshLayout.setRefreshing(false);
                    MyAdapter myadapter=new MyAdapter();
                    myadapter.notifyDataSetChanged();
                    listView.setAdapter(myadapter);

                    //listView.notify();
                    break;
                case FAILED:
                    Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_caselist;
    }

    @Override
    protected void setUpView() {
        if (mIsFirstCreated)
        {
            onRefresh();
        }

        listView = $(R.id.list);
        swipeRefreshLayout=$(R.id.swipe_refresh_layout_caselist);
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);


        swipeRefreshLayout.setOnRefreshListener(this);
        getData();
//        BaseAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.item_list,R.id.name,
//                Arrays.asList("美食劵", "活动劵", "优惠劵", "团购劵", "外卖劵"));
//        listView.setAdapter(arrayAdapter);

    }





    @Override
    public void onRefresh() {
        //tv.setText("正在刷新");
        // TODO Auto-generated method stub
        mIsFirstCreated = false;
        getData();

    }

    public void getData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    FastPoliceApplication fastPoliceApplication=new FastPoliceApplication();
                    int editor=fastPoliceApplication.getRole();

                        caseList = NetUtils.getListShop(queryAllUrlPath);



                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Log.i(tag, "在RUN中LIST长度为" + shopList.size());
                Message msg=new Message();
                if(caseList!=null)
                {
                    msg.what=SUCCESS;
                    msg.obj=caseList;

                }else
                {
                    msg.what=FAILED;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    protected void setUpData() {

    }


    class MyAdapter extends BaseAdapter
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        @Override
        public int getCount() {
            return caseList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(getActivity(), R.layout.item_list, null);
                holder = new ViewHolder();

                holder.ll_id= (TextView) convertView.findViewById(R.id.ll_case_id);
                holder.ll_name= (TextView) convertView.findViewById(R.id.ll_case_name);
                holder.ll_time= (TextView) convertView.findViewById(R.id.ll_case_date);
                holder.left_image= (ImageView) convertView.findViewById(R.id.iv_left_image);
                holder.whiteCouponView= (WhiteCouponView) convertView.findViewById(R.id.wc_backgroud_view);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ll_id.setText("案件编号：SB250"+caseList.get(i).getId());
            holder.ll_name.setText("报案人："+caseList.get(i).getName());
            holder.ll_time.setText("报案时间"+sdf.format(caseList.get(i).getUpdatetime()));
            holder.left_image.setBackgroundResource(imageIds[i % imageIds.length]);
            if (caseList.get(i).getDealed()==0)
            {
                //holder.whiteCouponView.setBackgroundColor(Color.argb(255,137,190,178));
                holder.whiteCouponView.setBackgroundColor(Color.argb(255,103,172,253));
            }else
            {
                holder.whiteCouponView.setBackgroundColor(Color.argb(255,245,181,100));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(getActivity(),"positon"+i,Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), RadarActivity.class);
                        intent.putExtra("caseid",caseList.get(i).getId());
                        startActivity(intent);

                }
            });
            return convertView;

        }


        private class ViewHolder{
            WhiteCouponView whiteCouponView;
            ImageView left_image;
            TextView ll_id;
            TextView ll_name;
            TextView ll_time;
        }

    }



}
