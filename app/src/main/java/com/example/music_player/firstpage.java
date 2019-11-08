package com.example.music_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class firstpage extends AppCompatActivity {
Button bmenu,bnext,bprev,bpause,bback;
TextView t1;
SeekBar s1;
String sname;

static MediaPlayer media;
int position;
ArrayList<File> mysongs;
Thread updateseek;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        bmenu=findViewById(R.id.button6);
        bnext=findViewById(R.id.button4);
        bprev=findViewById(R.id.button3);
        bpause=findViewById(R.id.button2);
        t1=findViewById(R.id.textView);
        s1=findViewById(R.id.seekBar);
        bback=findViewById(R.id.button5);
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(getApplicationContext(),songlist.class);
                startActivity(i2);
            }
        });



       /* getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        bmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pop=new PopupMenu(firstpage.this,bmenu);
                pop.getMenuInflater().inflate(R.menu.option_menu,pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(getApplication(),"Item clicked: "+ menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                pop.show();
            }
        });

        updateseek=new Thread()
        {

            @Override
            public void run()
            {

                int totalduration= media.getDuration();
                int currentpos=0;

                while(currentpos<totalduration)
                {
                    try {
                        sleep(500);
                        currentpos=media.getCurrentPosition();
                        s1.setProgress(currentpos);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        };


        if(media!=null){
            media.stop();
            media.release();
        }
        Intent i= getIntent();
        Bundle bundle=i.getExtras();

        mysongs=(ArrayList)bundle.getParcelableArrayList("songs");
        sname=mysongs.get(position).getName().toString();
        String songName=i.getStringExtra("songname");
        t1.setText(songName);
        t1.setSelected(true);

        position=bundle.getInt("pos",0);
        Uri u=Uri.parse(mysongs.get(position).toString());
        media=media.create(getApplicationContext(),u);
        media.start();
        s1.setMax(media.getDuration());
       // updateseek.start();
        s1.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        s1.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);


        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                media.seekTo(seekBar.getProgress());
            }
        });

        bpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s1.setMax(media.getDuration());
                if(media.isPlaying())
                {
                    bpause.setBackgroundResource(R.mipmap.playicon);
                    media.pause();
                }
                else{
                    bpause.setBackgroundResource(R.mipmap.pauseicon);
                    media.start();
                }
            }
        });

        bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                media.stop();
                media.release();
                position=((position+1)%mysongs.size());

                Uri u=Uri.parse(mysongs.get(position).toString());
                media=MediaPlayer.create(getApplicationContext(),u);
                sname = mysongs.get(position).getName().toString();
                t1.setText(sname);
                media.start();
            }
        });

        bprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                media.stop();
                media.release();
                position=((position-1)<0)?(mysongs.size()-1):(position-1);
                Uri u=Uri.parse(mysongs.get(position).toString());
                media=MediaPlayer.create(getApplicationContext(),u);
                sname=mysongs.get(position).getName().toString();
                t1.setText(sname);
                media.start();
            }
        });

    }

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }*/
}
