package com.reyansh.batterylife.Adapters;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reyansh.batterylife.Common;
import com.reyansh.batterylife.KeyUtil;
import com.reyansh.batterylife.R;
import com.reyansh.batterylife.Service.BatteryAccessibilityService;

import java.util.List;

public class SoftwareAdapter extends RecyclerView.Adapter<SoftwareAdapter.ItemHolder> {

    private List<ApplicationInfo> mApps;
    private PackageManager mPackageManager;
    private Context mContext;

    public SoftwareAdapter(Context context, List<ApplicationInfo> apps) {
        mApps = apps;
        mPackageManager = context.getPackageManager();
        mContext = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_softwares, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.mAppName.setText(mPackageManager.getApplicationLabel(mApps.get(position)));
        holder.mAppIcon.setImageDrawable(mPackageManager.getApplicationIcon(mApps.get(position)));
    }

    @Override
    public int getItemCount() {
        return mApps == null ? 0 : mApps.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public ImageView mAppIcon;
        public TextView mAppName;
        public TextView mAppSize;

        public ItemHolder(View itemView) {
            super(itemView);
            mAppIcon = itemView.findViewById(R.id.app_icon);
            mAppName = itemView.findViewById(R.id.app_name);
            mAppSize = itemView.findViewById(R.id.app_size);

            itemView.findViewById(R.id.button_hibernate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!KeyUtil.isAccessibilityServiceEnabled(mContext, BatteryAccessibilityService.class)) {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                                .setTitle(R.string.accessibility_permission)
                                .setMessage(R.string.accessibility_permission_message)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);

                                    }
                                }).create();
                        alertDialog.show();
                    } else {
                        try {
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + mApps.get(getAdapterPosition()).packageName));
                            mContext.startActivity(intent);
                            Common.getInstance().getBatteryAccessibilityService().mFromThis = true;
                        } catch (ActivityNotFoundException e) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            mContext.startActivity(intent);
                            Common.getInstance().getBatteryAccessibilityService().mFromThis = true;

                        }
                    }
                }
            });
        }
    }
}
