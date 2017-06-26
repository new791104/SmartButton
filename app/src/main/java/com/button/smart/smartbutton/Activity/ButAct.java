package com.button.smart.smartbutton.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.button.smart.smartbutton.Global.GV;
import com.button.smart.smartbutton.Http.Network_core;
import com.button.smart.smartbutton.Objects.ButtonItem;
import com.button.smart.smartbutton.R;

import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by charlie on 2017/6/12.
 */

public class ButAct {

    Network_core nCore;

    public void button_addition(final View view) {
        final Context context = view.getContext();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_addition, null);
        final EditText edit_bid = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_bid);
        final EditText edit_group = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_group);
        final EditText edit_name = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_name);
        final EditText edit_description = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_description);

        AlertDialog dlg = new AlertDialog.Builder(context)
                .setTitle("新增開關")   //dialog title
                .setView(dialogView)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() { //dialog 按鈕
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //將資料上傳到server
                        nCore = new Network_core(context);
                        nCore.button_add(GV.username, edit_bid.getText().toString(), edit_group.getText().toString(), edit_name.getText().toString(), edit_description.getText().toString());
                        nCore.setCallback(new Network_core.netCallback() {
                            @Override
                            public String response(String response) {
                                if (response.indexOf("success") >= 0) {
                                    color_dialog(view, PromptDialog.DIALOG_TYPE_SUCCESS, "開關設置成功!");
                                }
                                return null;
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public void check_delete(final View view, final String bid) {
        final Context context = view.getContext();

        ColorDialog dialog = new ColorDialog(context);
        dialog.setTitle("Operation");
        dialog.setContentText("確認刪除?");
        //dialog.setContentImage(getResources().getDrawable(R.mipmap.sample_img));
        dialog.setPositiveListener("OK!", new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
                //將資料上傳到server
                nCore = new Network_core(context);
                nCore.button_delete(GV.username, bid);

                nCore.setCallback(new Network_core.netCallback() {
                    @Override
                    public String response(String response) {
                        if (response.indexOf("success") >= 0) {
                            color_dialog(view, PromptDialog.DIALOG_TYPE_SUCCESS, "開關刪除成功!");
                        }
                        return null;
                    }
                });
            }
        }).setNegativeListener("cancel", new ColorDialog.OnNegativeListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    public void check_modify(final View view, final String bid, final String group, final String name, final String description) {
        final Context context = view.getContext();

        ColorDialog dialog = new ColorDialog(context);
        dialog.setTitle("Operation");
        dialog.setContentText("確認修改?");
        //dialog.setContentImage(getResources().getDrawable(R.mipmap.sample_img));
        dialog.setPositiveListener("OK!", new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
                //將資料上傳到server
                nCore = new Network_core(context);
                nCore.button_update(GV.username, bid, group, name, description);

                nCore.setCallback(new Network_core.netCallback() {
                    @Override
                    public String response(String response) {
                        if (response.indexOf("success") >= 0) {
                            color_dialog(view, PromptDialog.DIALOG_TYPE_SUCCESS, "開關修改成功!");
                        }
                        return null;
                    }
                });
            }
        }).setNegativeListener("cancel", new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void color_dialog(View view, int type, String content) {
        Context context = view.getContext();

        new PromptDialog(context).setDialogType(type)
                .setTitleText("Success").setContentText(content)
                .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void button_information(final View view, final int position) {
        final Context context = view.getContext();
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
                        // 修改
                        modify_information(view, GV.bItems.get(position).getBid().toString());

                    }
                }).show();
    }

    private void modify_information(final View view, final String bid) {
        Context context = view.getContext();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_modify, null);
        final EditText edit_group = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_group);
        final EditText edit_name = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_name);
        final EditText edit_description = (EditText) dialogView.findViewById(R.id.dialog_addition_edit_description);

        AlertDialog dlg = new AlertDialog.Builder(context)
                .setTitle("修改開關")   //dialog title
                .setView(dialogView)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() { //dialog 按鈕
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //確認修改
                        check_modify(view, bid, edit_group.getText().toString(), edit_name.getText().toString(), edit_description.getText().toString());
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


}
