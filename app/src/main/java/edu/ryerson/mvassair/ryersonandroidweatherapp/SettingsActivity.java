package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SettingsActivity extends AppCompatActivity {

    Button settings;
    RecyclerView loclist;
    RecyclerView.LayoutManager locmanager;
    LocAdapter locadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = findViewById(R.id.settings);
        settings.setText("Save");

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOps save = new DBOps(MainActivity.DB);
                save.setLocUpdate(MainActivity.locations);
                save.execute();
                try{
                    System.out.println(save.get() + "\n");
                }catch(ExecutionException | InterruptedException e){e.printStackTrace();}
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });

        loclist = findViewById(R.id.LocationList);
        locmanager = new LinearLayoutManager(this);
        locadapter = new LocAdapter(MainActivity.locations);
        loclist.setLayoutManager(locmanager);
        loclist.setAdapter(locadapter);
        locadapter.notifyDataSetChanged();
        }
    }

    //Since it's smaller than the WeatherAdapter, I just made it an inner class. Woohoo, laziness!
    class LocAdapter extends RecyclerView.Adapter<LocAdapter.ViewHolder>{

        private ArrayList<DBLocation> locations;

        class ViewHolder extends RecyclerView.ViewHolder{
            Switch sw;
            ViewHolder (LinearLayout l){
                super(l);
                sw = l.findViewById(R.id.locswitch);
            }
        }

        LocAdapter(ArrayList<DBLocation> locations){
            this.locations = locations;
        }

        public LocAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype){
            return new ViewHolder((LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.locationline, parent, false));
        }

        public void onBindViewHolder(LocAdapter.ViewHolder holder, int position){
            final DBLocation loc = locations.get(position);
            holder.itemView.setVisibility(View.INVISIBLE);
            holder.sw.setText(String.format("%s, %s", loc.name, loc.country));

            if(loc.selected > 0) {
                holder.sw.setChecked(true);
            }
            else {
                holder.sw.setChecked(false);
            }

            holder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton b, boolean checked) {
                    loc.selected = loc.selected > 0? 0:1;
                }
            });
            holder.itemView.setVisibility(View.VISIBLE);
        }
        public int getItemCount(){ return locations.size();}
}