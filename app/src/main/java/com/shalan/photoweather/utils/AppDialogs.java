package com.shalan.photoweather.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.shalan.photoweather.R;

public class AppDialogs {

    public static void showPermissionExplanationDialog(Context context, int message, final PermissionExplanationDialogListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.permission_dialog_title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.grant_permission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onGrantClicked();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.denied_permission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDeniedClicked();
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public interface PermissionExplanationDialogListener{
        void onGrantClicked();
        void onDeniedClicked();
    }
}
