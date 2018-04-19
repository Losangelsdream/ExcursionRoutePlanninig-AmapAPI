package com.example.admin.activitytest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Admin on 2018/3/29.
 */

public class RouteAdapter extends BaseAdapter {

    private Context context;
    private List<RouteModel> list;
    private String FormatTime;
    private String FormatMoney;
    private String Route_Text;

    public RouteAdapter(Context context, List<RouteModel> list)
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

            return list.get(position).getID();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.route_item,parent,false);
            holder = new ViewHolder();
            holder.Cost_View = (TextView) convertView.findViewById(R.id.TotalCost);
            holder.Time_View = (TextView) convertView.findViewById(R.id.TotalTime);
            holder.ID_View = (TextView) convertView.findViewById(R.id.ID);
            holder.Route_View = (TextView) convertView.findViewById(R.id.route_show);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        String Time_show= FormatTimeToString(list.get(position).getTotalTime());
        String Money_show = FormatMoneyToString(list.get(position).getTotalCost());
        String Route_Text = RouteWriteToString(list.get(position).getScenicSpot());
        System.out.println(Route_Text);

        holder.Time_View.setText(Time_show);
        holder.Cost_View.setText(Money_show);
        holder.Route_View.setText(Route_Text);
        holder.ID_View.setText("路线"+String.valueOf(list.get(position).getID()));
        return convertView;
    }

    private class ViewHolder{
        TextView Cost_View;
        TextView Time_View;
        TextView ID_View;
        TextView Route_View;
    }

    public String RouteWriteToString(List<String> scenicspot)
    {
        String spot_Text = null;
        String route_Text = scenicspot.get(0);
        for (int i=1; i<=scenicspot.size()-1;i++)
        {
            spot_Text = scenicspot.get(i);
            route_Text = route_Text+" --> "+spot_Text;
        }
        System.out.println(route_Text);
        return route_Text;
    }

    public String FormatTimeToString(double time)
    {
        double all_seconds = time*60*60;
        int hour = (int) (all_seconds/3600);
        int minute = (int) ((all_seconds-hour*3600)/60);
        FormatTime = hour+"小时"+minute+"分钟";
        return FormatTime;
    }

    public String FormatMoneyToString(int money)
    {
        FormatMoney="花费"+money+"元";
        return FormatMoney;
    }

}
