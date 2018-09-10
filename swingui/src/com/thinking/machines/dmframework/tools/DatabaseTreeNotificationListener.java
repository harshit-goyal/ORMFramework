package com.thinking.machines.dmframework.tools;
import javax.swing.table.*;
public interface DatabaseTreeNotificationListener
{
public void updateStatus(String status,boolean connected);
public void updatePropertiesModel(TMTableModel model);
public void clearPropertiesModel();
}