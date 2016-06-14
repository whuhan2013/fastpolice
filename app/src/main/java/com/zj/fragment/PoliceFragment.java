package com.zj.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.zj.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.zj.adapter.MyAdapter;
import com.zj.base.BaseFragment;
import com.zj.fastpolice.R;
import com.zj.manager.MyLinearLayoutManager;
import com.zj.utils.RecyclerViewUtils;

import java.util.Arrays;
import java.util.List;




public class PoliceFragment extends BaseFragment {

    private int item_normal_height;
    private int item_max_height;
    private float item_normal_alpha;
    private float item_max_alpha;
    private float alpha_d;
    private float item_normal_font_size;
    private float item_max_font_size;
    private float font_size_d;

    private List<Integer> walls = Arrays.asList(R.mipmap.police1,
            R.mipmap.police2, R.mipmap.police3, R.mipmap.police4, R.mipmap.police5,
            R.mipmap.police6, R.mipmap.police17, R.mipmap.police8, R.mipmap.police9,
            R.mipmap.police10);
    private List<String> names= Arrays.asList("高颖","张婷","朱彦超","陈戴茜","吴蕾蕾",
            "柯占军","黄晶莹","张虹","吴宝山","陶莎莎");

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_police;
    }

    @Override
    protected void setUpView() {

        item_max_height = (int) getResources().getDimension(R.dimen.item_max_height);
        item_normal_height = (int) getResources().getDimension(R.dimen.item_normal_height);
        item_normal_font_size = getResources().getDimension(R.dimen.item_normal_font_size);
        item_max_font_size = getResources().getDimension(R.dimen.item_max_font_size);
        item_normal_alpha = getResources().getFraction(R.fraction.item_normal_mask_alpha, 1, 1);
        item_max_alpha = getResources().getFraction(R.fraction.item_max_mask_alpha, 1, 1);

        font_size_d = item_max_font_size - item_normal_font_size;
        alpha_d = item_max_alpha - item_normal_alpha;

        RecyclerView recyclerView=$(R.id.rv_fragmentpolice);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(false);

        MyAdapter myAdapter = new MyAdapter(getActivity(), walls,names,recyclerView);
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(myAdapter);
        recyclerView.setAdapter(headerAndFooterRecyclerViewAdapter);
        View moreView = getActivity().getLayoutInflater().inflate(R.layout.footer_more, null);

        TextView more = (TextView) moreView.findViewById(R.id.moredeatil);
//        more.getLayoutParams().height = DeviceUtils.getScreenHeight(getActivity()) -
//                item_max_height;
        //more.getLayoutParams().height=100;
        RecyclerViewUtils.setFooterView(recyclerView, moreView);
        //RecyclerViewUtils.setHeaderView(recyclerView,moreView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                RecyclerView.ViewHolder firstViewHolder = recyclerView
                        .findViewHolderForLayoutPosition(linearLayoutManager.findFirstVisibleItemPosition());
                RecyclerView.ViewHolder secondViewHolder = recyclerView
                        .findViewHolderForLayoutPosition(linearLayoutManager.findFirstCompletelyVisibleItemPosition());
                RecyclerView.ViewHolder threeViewHolder = recyclerView
                        .findViewHolderForLayoutPosition(linearLayoutManager.findFirstCompletelyVisibleItemPosition() + 1);
                RecyclerView.ViewHolder lastViewHolder = recyclerView
                        .findViewHolderForLayoutPosition(linearLayoutManager.findLastVisibleItemPosition());
                if (firstViewHolder != null && firstViewHolder instanceof MyAdapter.ViewHolder) {
                    MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) firstViewHolder;
                    if (viewHolder.itemView.getLayoutParams().height - dy <= item_max_height
                            && viewHolder.itemView.getLayoutParams().height - dy >= item_normal_height) {
                        viewHolder.itemView.getLayoutParams().height = viewHolder.itemView.getLayoutParams().height - dy;
                        viewHolder.mark.setAlpha(viewHolder.mark.getAlpha() - dy * alpha_d / item_normal_height);
                        viewHolder.text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                viewHolder.text.getTextSize() - dy * font_size_d / item_normal_height);
                        viewHolder.itemView.setLayoutParams(viewHolder.itemView.getLayoutParams());
                    }
                }
                if (secondViewHolder != null && secondViewHolder instanceof MyAdapter.ViewHolder) {
                    MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) secondViewHolder;
                    if (viewHolder.itemView.getLayoutParams().height + dy <= item_max_height
                            && viewHolder.itemView.getLayoutParams().height + dy >= item_normal_height) {
                        viewHolder.itemView.getLayoutParams().height = viewHolder.itemView.getLayoutParams().height + dy;
                        viewHolder.mark.setAlpha(viewHolder.mark.getAlpha() + dy * alpha_d / item_normal_height);
                        viewHolder.text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                viewHolder.text.getTextSize() + dy * font_size_d / item_normal_height);
                        viewHolder.itemView.setLayoutParams(viewHolder.itemView.getLayoutParams());
                    }
                }

                if (threeViewHolder != null && threeViewHolder instanceof MyAdapter.ViewHolder) {
                    MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) threeViewHolder;
                    viewHolder.mark.setAlpha(item_normal_alpha);
                    viewHolder.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_normal_font_size);
                    viewHolder.itemView.getLayoutParams().height = item_normal_height;
                    viewHolder.itemView.setLayoutParams(viewHolder.itemView.getLayoutParams());
                }
                if (lastViewHolder != null && lastViewHolder instanceof MyAdapter.ViewHolder) {
                    MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) lastViewHolder;
                    viewHolder.mark.setAlpha(item_normal_alpha);
                    viewHolder.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_normal_font_size);
                    viewHolder.itemView.getLayoutParams().height = item_normal_height;
                    viewHolder.itemView.setLayoutParams(viewHolder.itemView.getLayoutParams());
                }
            }
        });

    }

    @Override
    protected void setUpData() {

    }



}
