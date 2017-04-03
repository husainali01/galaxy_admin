package com.dotcom.jamaatAdmin.adapters;

/**
 * Created by hali on 19/7/16.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dotcom.jamaatAdmin.model.Notification;
import com.dotcom.jamaatAdmin.view.NotificationHolder;

import java.util.ArrayList;




public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {
    Context context;
    int layoutResourceId;
    ArrayList<Notification> data = new ArrayList<Notification>();

    public NotificationAdapter(Context context, int layoutResourceId,
                               ArrayList<Notification> data) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
//        this.data = data;
        if(data == null){
            this.data = new ArrayList<Notification>();
        }else {
            this.data = data;
        }
    }
    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.layoutResourceId, parent, false);
        return new NotificationHolder(this.context, view);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        Notification notification = this.data.get(position);

        // 6. Bind the bakery object to the holder
        holder.bindBakery(notification);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.data.size();    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//        ViewHolder holder = null;
//        String userType = SharedPreferencesManager.getStringPreference(Constants.USERTYPE,null);
//        if (view == null) {
//            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            view = inflater.inflate(R.layout.notification_list_item, parent, false);
//            holder = new ViewHolder();
//            holder.title = (TextView) view.findViewById(R.id.title_notification);
//            holder.mDate = (TextView) view.findViewById(R.id.date_notification);
//
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
//        Notification notification = data.get(position);
//        holder.title.setText(notification.getTitle());
//        holder.mDate.setText(notification.getDate());
//
//        return view;
//    }
    //    "dealComment", "dealDetail", "meetingDetail", "meetingComment", "membershipDetail", "meetingCancelled"
//    static class ViewHolder {
//        private TextView title;
//        private ImageView icon;
//        private TextView mDate;
//    }
//    public void setItems(ArrayList<Notification> data)
//    {
////        this.data.clear();
//        this.data.addAll(data);
//        this.notifyDataSetChanged();
//    }
//
//    public void addItems(ArrayList<Notification> data)
//    {
//        this.data.addAll(data);
//        this.notifyDataSetChanged();
//    }

}


