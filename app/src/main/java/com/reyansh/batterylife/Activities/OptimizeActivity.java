package com.reyansh.batterylife.Activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ram.speed.booster.RAMBooster;
import com.ram.speed.booster.interfaces.CleanListener;
import com.ram.speed.booster.interfaces.ScanListener;
import com.ram.speed.booster.utils.ProcessInfo;
import com.reyansh.batterylife.Adapters.SoftwareAdapter;
import com.reyansh.batterylife.Common;
import com.reyansh.batterylife.R;
import com.reyansh.batterylife.Service.BatteryAccessibilityService;

import java.util.ArrayList;
import java.util.List;

public class OptimizeActivity extends AppCompatActivity {

    private RAMBooster booster;
    private RecyclerView mRecyclerView;
    private CollapsingToolbarLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimize);

        mCoordinatorLayout = findViewById(R.id.toolbar_layout);
        mRecyclerView = findViewById(R.id.recycler_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SoftwareAdapter(this, getApps()));
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    getSupportActionBar().setTitle("Optimize");
                } else if (verticalOffset == 0) {
                    getSupportActionBar().setTitle("");
                    mCoordinatorLayout.setTitle("");
                } else {
                    mCoordinatorLayout.setTitle("");
                    getSupportActionBar().setTitle("");
                }
            }
        });

        startService(new Intent(this, BatteryAccessibilityService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Common.getInstance().getBatteryAccessibilityService() != null) {
            Common.getInstance().getBatteryAccessibilityService().mFromThis = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<ApplicationInfo> getApps() {

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        List<ApplicationInfo> installedApps = new ArrayList<>();

        for (ApplicationInfo app : apps) {

            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            } else {
                if (!app.packageName.contains("com.reyansh"))
                    installedApps.add(app);
            }
        }
        return installedApps;
    }


    public void cleanIt() {
        if (booster == null)
            booster = null;
        booster = new RAMBooster(OptimizeActivity.this);
        booster.setDebug(true);
        booster.setScanListener(new ScanListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onFinished(long availableRam, long totalRam, List<ProcessInfo> appsToClean) {
                List<String> apps = new ArrayList<>();
                for (ProcessInfo info : appsToClean) {
                    apps.add(info.getProcessName());
                }
                booster.startClean();
            }
        });

        booster.setCleanListener(new CleanListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onFinished(final long availableRam, final long totalRam) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OptimizeActivity.this, "Optimized.", Toast.LENGTH_SHORT).show();
                    }
                });
                booster = null;
            }
        });
        booster.startScan(true);
    }

}
