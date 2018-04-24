package com.example.admin.activitytest;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 2018/4/19.
 */

public class DetailAdapter extends BaseAdapter {
    private Context context;
    private List<Detail> list;
    private String FormatTime;

    public DetailAdapter(Context context, List<Detail> list)
    {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getTrans_Mode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ViewHolder holder2 = null;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.detail_item,parent,false);
            holder = new ViewHolder();
            holder2 = new ViewHolder();
            holder.Bus_direction = (TextView) convertView.findViewById(R.id.bus_direction);
            holder.Bus_number = (TextView) convertView.findViewById(R.id.bus_number);
            holder.Bus_duration = (TextView) convertView.findViewById(R.id.bus_duration);
            holder.Start_station = (TextView) convertView.findViewById(R.id.Start_station);
            holder.End_station = (TextView)convertView.findViewById(R.id.End_station);
            holder.Bus_Icon = (ImageView)convertView.findViewById(R.id.BusIcon);
            holder.Line = (ImageView) convertView.findViewById(R.id.banner);

            holder2.Bus_direction = (TextView) convertView.findViewById(R.id.bus_direction);
            holder2.Bus_number = (TextView) convertView.findViewById(R.id.bus_number);
            holder2.Bus_duration = (TextView) convertView.findViewById(R.id.bus_duration);
            holder2.Start_station = (TextView) convertView.findViewById(R.id.Start_station);
            holder2.End_station = (TextView)convertView.findViewById(R.id.End_station);
            holder2.Bus_Icon = (ImageView)convertView.findViewById(R.id.BusIcon);
            holder2.Line = (ImageView) convertView.findViewById(R.id.banner);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
            holder2 = (ViewHolder)convertView.getTag();
        }
        if(list.get(position).getTrans_Mode()==1)
        {

            System.out.println("公交模式第"+position);
            String Bus_direction=(list.get(position).getBus_Direction());
            String start_station = (list.get(position).getBus_Start());
            String end_station = (list.get(position).getBus_Des());
            String Bus_Number = "  "+(list.get(position).getBus_Station_Number())+"  ";
            String Bus_Duration = FormatTimeToString(list.get(position).getBus_duration());


            holder.Bus_Icon.setVisibility(View.VISIBLE);
            holder.Start_station.setVisibility(View.VISIBLE);
            holder.End_station.setVisibility(View.VISIBLE);
            holder.Bus_number.setVisibility(View.VISIBLE);
            holder.Bus_duration.setVisibility(View.VISIBLE);

            holder.Bus_Icon.setImageResource(R.drawable.route_bus_select);
            holder.Bus_number.setText(Bus_Number);
            holder.Bus_duration.setText(Bus_Duration);
            holder.Bus_direction.setText(Bus_direction);
            holder.End_station.setText(end_station);
            holder.Start_station.setText(start_station);
            holder.Line.setImageResource(R.drawable.busbar);


            return convertView;
        }
        else if(list.get(position).getTrans_Mode()==0)
        {
            System.out.println("步行模式第"+position);
            String Walk_distance="步行"+(list.get(position).getWalk_distance().toString())+"米";
            String Walk_Duration="("+FormatTimeToString(list.get(position).getWalk_time())+")";

            holder.Bus_direction.setTextSize(13);
            holder.Bus_direction.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.Bus_direction.setText(Walk_distance+Walk_Duration);
            holder.Line.setImageResource(R.drawable.walkbar);

            
            holder.Start_station.setVisibility(View.GONE);
            holder.End_station.setVisibility(View.GONE);
            holder.Bus_number.setVisibility(View.GONE);
            holder.Bus_duration.setVisibility(View.GONE);
            return convertView;

        }
        else if(list.get(position).getTrans_Mode()==2)
        {

            holder.Start_station.setVisibility(View.GONE);
            holder.End_station.setVisibility(View.GONE);
            holder.Bus_number.setVisibility(View.GONE);
            holder.Bus_duration.setVisibility(View.GONE);
            holder.Line.setImageResource(R.drawable.startdot);
            holder.Bus_direction.setTextSize(15);
            holder.Bus_direction.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            holder.Bus_direction.setText("起点("+list.get(position).getStart_name()+")");



            return convertView;

        }
        else if(list.get(position).getTrans_Mode()==3)
        {

            holder.Start_station.setVisibility(View.GONE);
            holder.End_station.setVisibility(View.GONE);
            holder.Bus_number.setVisibility(View.GONE);
            holder.Bus_duration.setVisibility(View.GONE);
            holder.Line.setImageResource(R.drawable.enddot);
            holder.Bus_direction.setTextSize(15);
            holder.Bus_direction.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.Bus_direction.setText("终点("+list.get(position).getStart_name()+")");
            return convertView;

        }

        else if(list.get(position).getTrans_Mode()==4)
        {

            holder.Start_station.setVisibility(View.INVISIBLE);
            holder.End_station.setVisibility(View.INVISIBLE);
            holder.Bus_number.setVisibility(View.INVISIBLE);
            holder.Bus_duration.setVisibility(View.INVISIBLE);


            holder.Line.setImageResource(R.drawable.playbar);
            holder.Bus_Icon.setImageResource(R.drawable.viewpoint);
            holder.Bus_direction.setTextSize(15);
            holder.Bus_direction.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.Bus_direction.setText("游玩("+list.get(position).getStart_name()+")");
            return convertView;

        }
        return convertView;


    }

    private class ViewHolder{
        ImageView Bus_Icon;
        TextView Start_station;
        TextView End_station;
        TextView Bus_duration;
        TextView Bus_direction;
        TextView Bus_number;
        ImageView Line;
    }


    public String FormatTimeToString(float time)
    {

        int hour = (int) (time/3600);
        int minute = (int) ((time-hour*3600)/60);
        FormatTime = hour+"小时"+minute+"分钟";
        return FormatTime;
    }
}
