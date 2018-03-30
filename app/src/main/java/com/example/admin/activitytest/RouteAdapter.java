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
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.Time_View.setText(String.valueOf(list.get(position).getTotalTime()));
        holder.Cost_View.setText(String.valueOf(list.get(position).getTotalCost()));
        return convertView;
    }

    private class ViewHolder{
        TextView Cost_View;
        TextView Time_View;
    }

}
