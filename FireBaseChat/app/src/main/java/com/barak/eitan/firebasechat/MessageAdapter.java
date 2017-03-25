package com.barak.eitan.firebasechat;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by barak on 17/03/2017.
 */

public class MessageAdapter extends ArrayAdapter<MessageItem> {

    private Context context;
    private int resID;
    private List<MessageItem> objects;
    private Random r;

    // View lookup cache
    private static class ViewHolder {
        TextView tvUsr;
        TextView tvMsg;
        TextView tvDt;
    }

    public MessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MessageItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resID = resource;
        this.objects = objects;
        this.r = new Random();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MessageItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resID, parent, false);
            viewHolder.tvUsr = (TextView) convertView.findViewById(R.id.MSGTvName);
            viewHolder.tvMsg = (TextView) convertView.findViewById(R.id.MSGTvData);
            viewHolder.tvDt = (TextView) convertView.findViewById(R.id.MSGTvDate);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        View v;
//        LayoutInflater inflater = LayoutInflater.from(context);
//        v = inflater.inflate(resID, parent,false);
//        TextView tvUsr, tvMsg, tvDt;
//        Random r = new Random();
//
//        tvUsr = (TextView) v.findViewById(R.id.MSGTvName);
//        tvMsg = (TextView) v.findViewById(R.id.MSGTvData);
//        tvDt = (TextView) v.findViewById(R.id.MSGTvDate);

        viewHolder.tvUsr.setText(objects.get(position).getSender());
        viewHolder.tvUsr.setTextColor(r.nextInt(0xffffff)| 0xff000000);
        viewHolder.tvMsg.setText(objects.get(position).getMessage());
        viewHolder.tvDt.setText(objects.get(position).getDate().toString());


        return convertView;
    }
}
