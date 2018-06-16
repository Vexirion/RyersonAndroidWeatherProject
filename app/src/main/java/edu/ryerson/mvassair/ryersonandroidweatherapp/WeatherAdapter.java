package edu.ryerson.mvassair.ryersonandroidweatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Locale;

class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private ArrayList<DBWeatherInfo> weatherdata;
    private ArrayList<DBLocation> locations;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView location;
        TextView weather;
        public ImageView icon;
        ViewHolder(LinearLayout v){
            super(v);
            location = v.findViewById(R.id.Location);
            weather = v.findViewById(R.id.WeatherString);
            icon = v.findViewById(R.id.WeatherImage);
        }
    }

    WeatherAdapter (ArrayList<DBWeatherInfo> weather){
        weatherdata = weather;
    }

    //You can fill in some placeholder weatherData here if you like
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype){
        LinearLayout v = (LinearLayout)
                LayoutInflater.from (parent.getContext()).inflate(R.layout.weatherline, parent, false);

        return new ViewHolder(v);
    }

    //This will populate weatherData in the passed ViewHolder
    public void onBindViewHolder(ViewHolder holder, int position){
        DBWeatherInfo data = weatherdata.get(position);
        DBLocation loc = data.locinfo;
        if(loc != null) {
            holder.location.setText(String.format("%s, %s", data.location, loc.country));
            holder.weather.setText(String.format(Locale.CANADA, "%.2f°C", data.temperature));
            switch (data.category) {
                case SUN:
                    holder.icon.setImageResource(R.drawable.ic_placeholder_sun);
                    break;
                case RAIN:
                    holder.icon.setImageResource(R.drawable.ic_placeholder_rain);
                    break;
                case CLOUDS:
                    holder.icon.setImageResource(R.drawable.ic_placeholder_cloud);
                    break;
                case THUNDER:
                    holder.icon.setImageResource(R.drawable.ic_placeholder_thunder);
                    break;
                case SNOW:
                    holder.icon.setImageResource(R.drawable.ic_placeholder_snow);
                    break;
                case FOG:
                    holder.icon.setImageResource(R.drawable.ic_placeholder_fog);
                    break;
                default:
                    holder.icon.setImageResource(R.drawable.ic_placeholder_warning);
            }
        }
    }

    public int getItemCount(){
        return weatherdata.size();
    }

}
