package com.button.smart.smartbutton.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.button.smart.smartbutton.Global.GV;
import com.button.smart.smartbutton.Objects.ButtonItem;
import com.button.smart.smartbutton.R;

import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by charlie on 2017/6/12.
 */

public class ButAct {
    public void button_addition(View view) {
        Context context = view.getContext();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_addition, null);
        EditText edit_bid = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_bid);
        EditText edit_group = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_group);
        EditText edit_name = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_name);
        EditText edit_description = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_description);

        AlertDialog dlg = new AlertDialog.Builder(context)
                .setTitle("新增開關")   //dialog title
                .setView(dialogView)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() { //dialog 按鈕
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //將資料上傳到server
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public void check_modify(View view) {
        Context context = view.getContext();

        ColorDialog dialog = new ColorDialog(context);
        dialog.setTitle("Operation");
        dialog.setContentText("確認修改?");
        //dialog.setContentImage(getResources().getDrawable(R.mipmap.sample_img));
        dialog.setPositiveListener("OK!", new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {

            }
        })
                .setNegativeListener("cancel", new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void button_information(final View view, int position) {
        Context context = view.getContext();
        ButtonItem bItem = GV.bItems.get(position);

        new PromptDialog(context)
                .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                .setAnimationEnable(true)
                .setTitleText("開關資訊")
                .setContentText(
                        "Bid: " + bItem.getBid() + "\n" +
                                "group: " + bItem.getGroup() + "\n" +
                                "name: " + bItem.getName() + "\n" +
                                "Description: " + bItem.getDescription()
                )
                .setPositiveListener("修改", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        //確認修改
                        check_modify(view);
                    }
                }).show();
    }


}
