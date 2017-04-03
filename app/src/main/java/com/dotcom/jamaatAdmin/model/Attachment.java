package com.dotcom.jamaatAdmin.model;

import java.io.Serializable;

/**
 * Created by anjanik on 17/6/16.
 */
public class Attachment implements Serializable{

    String attachmentType;
    String attachmentUrl;
    String attachmentName;
    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }
}
