package com.dotcom.jamaatAdmin.interfaces;

/**
 * Created by anjanik on 18/5/16.
 */
public interface IAdapterCallBack {
    public void fragmentCalling(Object calledObject, String position,String value);
    public void fragmentCallingByType(Object calledObject, String position,String value,String type);
}
