package com.thinking.machines.dmframework.tools;
import javax.swing.event.*;
import com.thinking.machines.dmframework.pojo.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;
public class DefaultCompareOrderDialog extends JDialog 
{
private TMTable columnsInCompareOrderTable;
private TMTable columnsNotInCompareOrderTable;
private JScrollPane columnsInCompareOrderTableScrollPane;
private JScrollPane columnsNotInCompareOrderTableScrollPane;
private JLabel titleLabel;
private ViewPropertiesModel viewPropertiesModel;
private ColumnsInCompareOrderModel columnsInCompareOrderModel;
private ColumnsNotInCompareOrderModel columnsNotInCompareOrderModel;
public DefaultCompareOrderDialog(java.awt.Frame parent, boolean modal,ViewPropertiesModel viewPropertiesModel)
{
super(parent, modal);
setIconImage(Icons.LogoIcon.getImage());
this.viewPropertiesModel=viewPropertiesModel;
this.columnsInCompareOrderModel=new ColumnsInCompareOrderModel(viewPropertiesModel);
this.columnsNotInCompareOrderModel=new ColumnsNotInCompareOrderModel(viewPropertiesModel);
this.columnsInCompareOrderModel.setColumnsNotInCompareOrderModel(columnsNotInCompareOrderModel);
this.columnsNotInCompareOrderModel.setColumnsInCompareOrderModel(columnsInCompareOrderModel);
initComponents();
}
private void initComponents() 
{
titleLabel = new JLabel();
columnsNotInCompareOrderTableScrollPane=new JScrollPane();
columnsNotInCompareOrderTable = new TMTable(columnsNotInCompareOrderModel);
columnsInCompareOrderTableScrollPane=new JScrollPane();
columnsInCompareOrderTable=new TMTable(columnsInCompareOrderModel);
this.columnsNotInCompareOrderModel.setColumnsInCompareOrderTMTable(columnsInCompareOrderTable);
setAlwaysOnTop(true);
setFont(new java.awt.Font("Verdana", 0, 20)); 
setModal(true);
setPreferredSize(new java.awt.Dimension(700, 500));
JPanel centerPanel=new JPanel();
centerPanel.setLayout(new GridLayout(2,1));
JPanel topPanel=new JPanel();
topPanel.setLayout(new BorderLayout());
getContentPane().setLayout(new BorderLayout());
titleLabel.setFont(new java.awt.Font("Verdana", 0, 20)); 
titleLabel.setText("View : "+viewPropertiesModel.getValueAt(1,1));
titleLabel.setIcon(Icons.TableIcon);
topPanel.add(titleLabel);
getContentPane().add(topPanel,BorderLayout.NORTH);
columnsNotInCompareOrderTableScrollPane.setViewportView(columnsNotInCompareOrderTable);
columnsInCompareOrderTableScrollPane.setViewportView(columnsInCompareOrderTable);
centerPanel.add(columnsInCompareOrderTableScrollPane);
centerPanel.add(columnsNotInCompareOrderTableScrollPane);
getContentPane().add(centerPanel,BorderLayout.CENTER);
pack();
Dimension desktopSize=Toolkit.getDefaultToolkit().getScreenSize();
setSize(800,550);
setLocation(desktopSize.width/2-400,(desktopSize.height-50)/2-275);
}
}
