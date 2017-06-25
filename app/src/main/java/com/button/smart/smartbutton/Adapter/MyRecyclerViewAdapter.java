package com.button.smart.smartbutton.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.button.smart.smartbutton.Activity.ButAct;
import com.button.smart.smartbutton.Activity.MainActivity;
import com.button.smart.smartbutton.Global.GV;
import com.button.smart.smartbutton.Http.Network_core;
import com.button.smart.smartbutton.Objects.ButtonItem;
import com.button.smart.smartbutton.R;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;


/**
 * Created by charlie on 2017/6/3.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{

    //private List<ButtonItem> bItems;
    private Context mContext;
    private View mHeaderView;
    private View mFooterView;
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    //介面上的 button action
    private ButAct butact = new ButAct();

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public TextView contentTextView;
        SwitchButton sb;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("debug", "###ViewHolder###");

            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView){
                return;
            }
            if (itemView == mFooterView){
                return;
            }
            nameTextView = (TextView) itemView.findViewById(R.id.id);
            //contentTextView = (TextView) itemView.findViewById(R.id.content);
            sb = (SwitchButton) itemView.findViewById(R.id.recycler_item_sb);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

        }
    }

    public MyRecyclerViewAdapter(Context context, List<ButtonItem> di) {
        Log.d("debug", "###MyRecyclerViewAdapter###");
        //bItems = di;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }
        if (position == 0){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d("debug", "###onCreateViewHolder###");
        Context context = viewGroup.getContext();

        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ViewHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ViewHolder(mFooterView);
        }

        View contactView = LayoutInflater.from(context).inflate(R.layout.list_item_content, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyRecyclerViewAdapter.ViewHolder viewHolder, final int position) {
        Log.d("debug", "###onBindViewHolder###");
        Log.d("debug", "position: " + position);

        if(position == 0)
            return;

        final int bPosition = position - 1;
        ButtonItem bItem = GV.bItems.get(bPosition);

        viewHolder.nameTextView.setText(String.valueOf(bItem.getName()));
        //viewHolder.contentTextView.setText(String.valueOf(bItem.getDescription()));
        viewHolder.sb.setOnCheckedChangeListener(null);
        viewHolder.sb.setCheckedImmediately(GV.bItems.get(bPosition).getStatus());
        viewHolder.sb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Network_core nCore = new Network_core(mContext);
                GV.bItems.get(bPosition).setStatus(isChecked);
                GV.refresh_time = 10;
                if (isChecked) {
                    viewHolder.nameTextView.setTextColor(Color.parseColor("#525252"));
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                else {
                    viewHolder.nameTextView.setTextColor(Color.parseColor("#ffffff"));
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#525252"));
                }
                nCore.button_turn(GV.bItems.get(bPosition).getUser(), GV.bItems.get(bPosition).getBid(), isChecked);
                Log.d("debug", "isChecked: " +  GV.bItems.get(bPosition).getStatus());
            }
        });

        /*根據開關狀態更改背景顏色*/
        if (GV.bItems.get(bPosition).getStatus()) {
            viewHolder.nameTextView.setTextColor(Color.parseColor("#525252"));
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#F8F6F6"));
        }
        else {
            viewHolder.nameTextView.setTextColor(Color.parseColor("#F8F6F6"));
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#525252"));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butact.button_information(view, bPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null) {
            return GV.bItems.size();
        } else if (mHeaderView == null && mFooterView != null) {
            return GV.bItems.size() + 1;
        } else if (mHeaderView != null && mFooterView == null) {
            return GV.bItems.size() + 1;
        } else {
            return GV.bItems.size() + 2;
        }
    }

}
