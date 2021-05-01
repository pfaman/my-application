package com.app.wsdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    storyAdapter storyAdapter1;
    File[] files;
    ArrayList<Object>filesList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener(){

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                      }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.cancelPermissionRequest();
                    }
                }).check();
    }

    private void initViews() {

        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipereRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                setUpRefreshLayout();
                (
                        new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Refresh!", Toast.LENGTH_SHORT).show();
                    }
                },2000);

            }
        });
    }

    private void setUpRefreshLayout() {

        filesList.clear();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        storyAdapter1=new storyAdapter(MainActivity.this,getData());
        recyclerView.setAdapter(storyAdapter1);
        storyAdapter1.notifyDataSetChanged();
    }

    private ArrayList<Object> getData() {

        StoryModel f;
        String targetPath= Environment.getExternalStorageDirectory().getAbsolutePath()+Constant.FOLDER_NAME+"Media/.Statuses";
        File targetDirectory=new File(targetPath);

        files=targetDirectory.listFiles();

        for(int i=0;i<files.length;i++){
            File file=files[i];
            f=new StoryModel();
            f.setUri(Uri.fromFile(file));
            f.setFilename(file.getName());
            if(!f.getUri().toString().endsWith(".nomedia")){
                filesList.add(f);
            }
        }
        return filesList;
    }

}