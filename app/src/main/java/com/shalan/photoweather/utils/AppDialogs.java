package com.shalan.photoweather.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.shalan.photoweather.R;

public class AppDialogs {

    public static void showPermissionExplanationDialog(Context context, int message, final int permissionID
            , final PermissionExplanationDialogListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.permission_dialog_title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.grant_permission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onGrantClicked(permissionID);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.denied_permission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDeniedClicked(permissionID);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static void showErrorDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.error_happened_title));
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public interface PermissionExplanationDialogListener{
        void onGrantClicked(int permissionID);
        void onDeniedClicked(int permissionID);
    }
}
