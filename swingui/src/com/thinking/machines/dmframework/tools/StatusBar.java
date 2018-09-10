package com.thinking.machines.dmframework.tools;
import java.awt.*;
import javax.swing.*;
class StatusBar extends JPanel 
{
private JLabel status;
public StatusBar() 
{
setLayout(new BorderLayout());
setPreferredSize(new Dimension(10, 23));
JPanel rightPanel = new JPanel(new BorderLayout());
rightPanel.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
rightPanel.setOpaque(false);
status=new JLabel("");
status.setFont(new Font("Verdana",Font.BOLD,14));
add(status,BorderLayout.CENTER);
add(rightPanel, BorderLayout.EAST);
setBackground(SystemColor.control);
}
public void setStatus(String status)
{
this.status.setText(status);
}
protected void paintComponent(Graphics g) 
{
super.paintComponent(g);
int y = 0;
g.setColor(new Color(156, 154, 140));
g.drawLine(0, y, getWidth(), y);
y++;
g.setColor(new Color(196, 194, 183));
g.drawLine(0, y, getWidth(), y);
y++;
g.setColor(new Color(218, 215, 201));
g.drawLine(0, y, getWidth(), y);
y++;
g.setColor(new Color(233, 231, 217));
g.drawLine(0, y, getWidth(), y);
y=getHeight() - 3;
g.setColor(new Color(233, 232, 218));
g.drawLine(0, y, getWidth(), y);
y++;
g.setColor(new Color(233, 231, 216));
g.drawLine(0, y, getWidth(), y);
y=getHeight() - 1;
g.setColor(new Color(221, 221, 220));
g.drawLine(0, y, getWidth(), y);
}
}

