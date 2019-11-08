package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
/*import com.karumi.dexter.listener.PermissionRequest;*/
/*import com.karumi.dexter.listener.single.BasePermissionListener;*/
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.File;
import java.util.ArrayList;

public class songlist extends AppCompatActivity {
    String[] arr;
    ListView l;
    ImageView iv;
    AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlist);

        l=findViewById(R.id.list);
        iv=findViewById(R.id.imageView2);
        animationDrawable=(AnimationDrawable)iv.getBackground();
        animationDrawable.start();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(getApplicationContext(),firstpage.class);
                startActivity(i2);
            }
        });
        runtimePermission();
    }



    public void runtimePermission(){

        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                display();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
              token.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> findsong(File file)
    {
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        for(File singleFile:files)
        {
            if(singleFile.isDirectory() && !singleFile.isHidden())
            {
                arrayList.addAll(findsong(singleFile));
            }
            else
            {
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav"))
                {
                    arrayList.add(singleFile);

                }
            }

        }
        return arrayList;
    }

    void display(){
        final ArrayList<File> mysongs=findsong(Environment.getExternalStorageDirectory());
        arr=new String[mysongs.size()];
        for(int i=0; i<mysongs.size();i++)
        {
            arr[i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }
        ArrayAdapter<String> myadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);
        l.setAdapter(myadapter);


        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long j) {

                String songName = l.getItemAtPosition(pos).toString();

                startActivity(new Intent(getApplicationContext(),firstpage.class)
                        .putExtra("songs",mysongs)
                        .putExtra("songname",songName)
                        .putExtra("pos",pos));

            }
        });
    }
}
