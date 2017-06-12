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

    //介面上的 button action
    private ButAct butact = new ButAct();

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public TextView contentTextView;
        SwitchButton sb;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("debug", "###ViewHolder###");

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
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d("debug", "###onCreateViewHolder###");
        Context context = viewGroup.getContext();
        View contactView = LayoutInflater.from(context).inflate(R.layout.list_item_content, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyRecyclerViewAdapter.ViewHolder viewHolder, final int position) {
        Log.d("debug", "###onBindViewHolder###");
        Log.d("debug", "position: " + position);

        ButtonItem bItem = GV.bItems.get(position);

        viewHolder.nameTextView.setText(String.valueOf(bItem.getName()));
        //viewHolder.contentTextView.setText(String.valueOf(bItem.getDescription()));
        viewHolder.sb.setOnCheckedChangeListener(null);
        viewHolder.sb.setCheckedImmediately(GV.bItems.get(position).getStatus());
        viewHolder.sb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GV.bItems.get(position).setStatus(isChecked);
                if (isChecked) {
                    viewHolder.nameTextView.setTextColor(Color.parseColor("#525252"));
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                else {
                    viewHolder.nameTextView.setTextColor(Color.parseColor("#ffffff"));
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#525252"));
                }
                Log.d("debug", "isChecked: " +  GV.bItems.get(position).getStatus());
            }
        });

        /*根據開關狀態更改背景顏色*/
        if (GV.bItems.get(position).getStatus()) {
            viewHolder.nameTextView.setTextColor(Color.parseColor("#525252"));
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else {
            viewHolder.nameTextView.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#525252"));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butact.button_information(view, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return GV.bItems.size();
    }

}
