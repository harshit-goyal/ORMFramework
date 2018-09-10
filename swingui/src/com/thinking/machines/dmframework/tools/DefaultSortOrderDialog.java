package com.thinking.machines.dmframework.tools;
import javax.swing.event.*;
import com.thinking.machines.dmframework.pojo.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;
public class DefaultSortOrderDialog extends JDialog 
{
private TMTable columnsInSortOrderTable;
private TMTable columnsNotInSortOrderTable;
private JScrollPane columnsInSortOrderTableScrollPane;
private JScrollPane columnsNotInSortOrderTableScrollPane;
private JLabel titleLabel;
private TablePropertiesModel tablePropertiesModel;
private ViewPropertiesModel viewPropertiesModel;
private ColumnsInSortOrderModel columnsInSortOrderModel;
private ColumnsNotInSortOrderModel columnsNotInSortOrderModel;
public DefaultSortOrderDialog(java.awt.Frame parent, boolean modal,TablePropertiesModel tablePropertiesModel)
{
super(parent, modal);
this.tablePropertiesModel=tablePropertiesModel;
this.columnsInSortOrderModel=new ColumnsInSortOrderModel(tablePropertiesModel);
this.columnsNotInSortOrderModel=new ColumnsNotInSortOrderModel(tablePropertiesModel);
this.columnsInSortOrderModel.setColumnsNotInSortOrderModel(columnsNotInSortOrderModel);
this.columnsNotInSortOrderModel.setColumnsInSortOrderModel(columnsInSortOrderModel);
initComponents();
}
public DefaultSortOrderDialog(java.awt.Frame parent, boolean modal,ViewPropertiesModel viewPropertiesModel)
{
super(parent, modal);
setIconImage(Icons.LogoIcon.getImage());
this.viewPropertiesModel=viewPropertiesModel;
this.columnsInSortOrderModel=new ColumnsInSortOrderModel(viewPropertiesModel);
this.columnsNotInSortOrderModel=new ColumnsNotInSortOrderModel(viewPropertiesModel);
this.columnsInSortOrderModel.setColumnsNotInSortOrderModel(columnsNotInSortOrderModel);
this.columnsNotInSortOrderModel.setColumnsInSortOrderModel(columnsInSortOrderModel);
initComponents();
}
private void initComponents() 
{
titleLabel = new JLabel();
columnsNotInSortOrderTableScrollPane=new JScrollPane();
columnsNotInSortOrderTable = new TMTable(columnsNotInSortOrderModel);
columnsInSortOrderTableScrollPane=new JScrollPane();
columnsInSortOrderTable=new TMTable(columnsInSortOrderModel);
this.columnsNotInSortOrderModel.setColumnsInSortOrderTMTable(columnsInSortOrderTable);
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
if(this.tablePropertiesModel!=null)
{
titleLabel.setText("Table : "+tablePropertiesModel.getValueAt(1,1));
}
else
{
titleLabel.setText("View : "+viewPropertiesModel.getValueAt(1,1));
}
titleLabel.setIcon(Icons.TableIcon);
topPanel.add(titleLabel);
getContentPane().add(topPanel,BorderLayout.NORTH);
columnsNotInSortOrderTableScrollPane.setViewportView(columnsNotInSortOrderTable);
columnsInSortOrderTableScrollPane.setViewportView(columnsInSortOrderTable);
centerPanel.add(columnsInSortOrderTableScrollPane);
centerPanel.add(columnsNotInSortOrderTableScrollPane);
getContentPane().add(centerPanel,BorderLayout.CENTER);
pack();
Dimension desktopSize=Toolkit.getDefaultToolkit().getScreenSize();
setSize(800,550);
setLocation(desktopSize.width/2-400,(desktopSize.height-50)/2-275);
}
}
