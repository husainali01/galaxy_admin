package com.dotcom.jamaatAdmin.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dotcom.jamaatAdmin.R;
import com.dotcom.jamaatAdmin.model.Notification;

/**
 * Created by Husain on 23-03-2017.
 */

public class NotificationHolder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final TextView mDate;

    private Notification notification;
    private Context context;

    public NotificationHolder(Context context, View itemView) {

        super(itemView);

        // 1. Set the context
        this.context = context;

        // 2. Set up the UI widgets of the holder
        this.title = (TextView) itemView.findViewById(R.id.title_notification);
        this.mDate = (TextView) itemView.findViewById(R.id.date_notification);
        // 3. Set the "onClick" listener of the holder
    }

    public void bindBakery(Notification notification) {

        // 4. Bind the data to the ViewHolder
        this.notification = notification;
        this.title.setText(notification.getTitle());
        this.mDate.setText(notification.getDate());
    }
}
