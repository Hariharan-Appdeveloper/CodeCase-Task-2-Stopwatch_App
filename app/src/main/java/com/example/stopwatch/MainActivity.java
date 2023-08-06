package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean isresume;
    private int seconds=0;

    private boolean running;
    int i=0;
    int millsec;

    int flagmillisec=0,flagmins,flagsecs, diffmillisec=0, diffmins=0,diffsecs=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        ImageButton start,reset,flag;

        Vibrator flagvibe;

        flagvibe=(Vibrator)getSystemService(VIBRATOR_SERVICE);

        TextView timer=findViewById(R.id.timer);
        TextView title=findViewById(R.id.laptitle);
        start=findViewById(R.id.play);
        reset=findViewById(R.id.reset);
        flag=findViewById(R.id.flag);


        ListView lap=findViewById(R.id.lap);

        Handler h=new Handler();


        List<String> laparray=new ArrayList<String>();
        List<String> reverselist=new ArrayList<>();



        ArrayAdapter<String> lapadp=new ArrayAdapter<String>(getApplicationContext(),R.layout.textcolourlistview,reverselist);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagvibe.vibrate(100);
                if(!isresume){
                    isresume=true;
                    running=true;
                    start.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                }
                else{
                    isresume=false;
                    running=false;
                    start.setImageDrawable(getResources().getDrawable(R.drawable.play));
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagvibe.vibrate(100);
                i=0;
                start.setImageDrawable(getResources().getDrawable(R.drawable.play));
                seconds=0;

                timer.setText("00:00.00");
                laparray.clear();
                reverselist.clear();
                title.setVisibility(View.INVISIBLE);
                ArrayAdapter<String> lapadp=new ArrayAdapter<String>(getApplicationContext(),R.layout.textcolourlistview,reverselist);
                lap.setAdapter(lapadp);
                running=false;
                isresume=false;
            }
        });

        h.post(new Runnable() {
            @Override
            public void run() {

                int mins=seconds/3600;
                int secs=(seconds%3600)/60;
                millsec=seconds%60;

                String time=String.format("%02d:%02d.%02d",mins,secs,millsec);
                flagmillisec=millsec;
                flagmins=mins;
                flagsecs=secs;

                if(running){
                    seconds++;
                }

                timer.setText(time);
                h.postDelayed((Runnable) this,0);
            }
        });


        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagvibe.vibrate(50);
                if(running==false){
                    return;
                }
                int mins,secs,millisec;
                mins=0;
                i++;
                reverselist.clear();
                ArrayAdapter<String> lapadp=new ArrayAdapter<String>(getApplicationContext(),R.layout.textcolourlistview,reverselist);
                lap.setAdapter(lapadp);
                String timestr=String.format("%02d:%02d.%02d",Math.abs(flagmins),Math.abs(flagsecs),Math.abs(flagmillisec));
                String laptime;
                if(i<10){
                    laptime=String.format("%d                 "+timestr,i);}
                else{
                    laptime=String.format("%d               "+timestr,i);
                }

                if(flagmillisec<diffmillisec){
                    flagmillisec=flagmillisec+100;
                    flagsecs=flagsecs-1;
                    millisec=flagmillisec-diffmillisec;
                    if(flagsecs<diffsecs){
                        flagmins=flagmins-1;
                        flagsecs=flagsecs+60;
                        secs=flagsecs-diffsecs;
                        mins=flagmins-diffmins;
                    }
                    else{
                        secs=flagsecs-diffsecs;
                        mins=flagmins-diffmins;
                    }
                }else{
                    millisec=flagmillisec-diffmillisec;
                    if(flagsecs<diffsecs){
                        flagmins=flagmins-1;
                        flagsecs=flagsecs+60;
                        secs=flagsecs-diffsecs;
                        mins=flagmins-diffmins;
                    }
                    else{
                        secs=flagsecs-diffsecs;
                        mins=flagmins-diffmins;
                    }

                }


                mins=Math.abs(mins);
                secs=Math.abs(secs);
                String laptt=String.format("%02d:%02d.%02d",Math.abs(mins),Math.abs(secs),Math.abs(millisec));
                if(i==1){
                    title.setVisibility(View.VISIBLE);
                    laptt="";
                    diffsecs=diffmillisec=diffmins=0;
                }
                diffmillisec=flagmillisec;
                diffmins=flagmins;
                diffsecs=flagsecs;

                if(i<10){
                    laptime=String.format("        %d                     "+timestr+"             "+laptt,i);}
                else{
                    laptime=String.format("       %d                    "+timestr+"             "+laptt,i);
                }

                laparray.add(laptime);


                Collections.reverse(laparray);
                for(String s:laparray){
                    reverselist.add(s);
                }
                Collections.reverse(laparray);


            }
        });

    }
}