package com.dotcom.jamaatAdmin.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dotcom.jamaatAdmin.model.Attachment;
import com.dotcom.jamaatAdmin.R;
import com.dotcom.jamaatAdmin.interfaces.IAdapterCallBack;

import java.util.ArrayList;

/**
 * Created by anjanik on 12/7/16.
 */
public class AttachmentAdapter extends ArrayAdapter<Attachment> {
    Context context;
    int layoutResourceId;
    private String dealId ;
    ArrayList<Attachment> data = new ArrayList<Attachment>();
    IAdapterCallBack iAdapterCallBack;
    public AttachmentAdapter(Context context, int layoutResourceId, ArrayList<Attachment> attachmentList, IAdapterCallBack iAdapterCallBack) {
        super(context, layoutResourceId, attachmentList);
        this.context =  context;
        this.layoutResourceId = layoutResourceId;
        this.data = attachmentList;
        this.iAdapterCallBack =  iAdapterCallBack;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.mAttachmentname_tv = (TextView) view.findViewById(R.id.attachmentname_tv);
            holder.mDownload_imageview = (ImageView) view.findViewById(R.id.download_imageview);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Attachment attachment = data.get(position);
        holder.mAttachmentname_tv.setText(attachment.getAttachmentName());
        holder.mAttachmentname_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAdapterCallBack.fragmentCalling(attachment,""+position,"download");
            }
        });


        return view;

    }

    static class ViewHolder {
        private TextView mAttachmentname_tv;
        private ImageView mDownload_imageview;


//        Button btnEdit;
//        Button btnDelete;
    }
}