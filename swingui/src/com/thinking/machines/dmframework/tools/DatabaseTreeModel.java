package com.thinking.machines.dmframework.tools;
import com.thinking.machines.dmframework.pojo.*;
import java.util.*;
import javax.swing.tree.*;
import javax.swing.*;
import javax.swing.event.*;
public class DatabaseTreeModel  implements TreeModel
{
private String architecture;
private Database database;
private EventListenerList listenerList = new EventListenerList();
public DatabaseTreeModel()
{
this.database=new Database();
this.architecture=null;
}
public void setDatabase(Database database)
{
Database oldDatabase=this.database;
this.database=database;
fireTreeStructureChanged(oldDatabase);
}
public void setArchitecture(String architecture)
{
this.architecture=architecture;
}
public Object getRoot()
{
return this.database;
}
public Object getChild(Object parent,int index)
{
if(parent instanceof Database)
{
if(index==0)
{
return new TableNode("Tables ("+this.database.getTables().size()+")");
}
if(index==1)
{
return new ViewNode("Views ("+this.database.getViews().size()+")");
}
if(index==2)
{
return new SequenceNode("Sequences ("+this.database.getSequences().size()+")");
}
}
if(parent instanceof TableNode)
{
return this.database.getTables().get(index);
}
if(parent instanceof ViewNode)
{
return this.database.getViews().get(index);
}
if(parent instanceof Table)
{
return ((Table)parent).getColumns().get(index);
}
if(parent instanceof View)
{
return ((View)parent).getColumns().get(index);
}
if(parent instanceof SequenceNode)
{
Sequence sequence=new Sequence();
sequence.setName(this.database.getSequences().get(index));
return sequence;
}
return null;
}
public int getChildCount(Object parent)
{
if(parent instanceof Database) 
{
if(this.architecture==null) return 0;
if(this.architecture.equalsIgnoreCase("oracle 11g")) return 3;
return 2;
}
if(parent instanceof TableNode)
{
return this.database.getTables().size();
}
if(parent instanceof ViewNode)
{
return this.database.getViews().size();
}
if(parent instanceof SequenceNode)
{
return this.database.getSequences().size();
}
if(parent instanceof Table)
{
return ((Table)parent).getColumns().size();
}
if(parent instanceof View)
{
return ((View)parent).getColumns().size();
}
return 0;
}
public boolean isLeaf(Object node)
{
if(node instanceof Database) return false;
if(node instanceof ViewNode) return false;
if(node instanceof TableNode) return false;
if(node instanceof Table) return false;
if(node instanceof View) return false;
if(node instanceof SequenceNode) return false;
return true;
}
public void valueForPathChanged(TreePath path,Object newValue)
{
}
public int getIndexOfChild(Object parent,Object child)
{
if(child instanceof TableNode) return 0;
if(child instanceof ViewNode) return 1;
if(child instanceof SequenceNode) return 2;
int x;
if(child instanceof Table)
{
Table table=(Table)child;
x=0;
for(Table t:this.database.getTables())
{
if(table.getName().equals(t.getName()))
{
return x;
}
x++;
}
}
if(child instanceof View)
{
View view=(View)child;
x=0;
for(View v:this.database.getViews())
{
if(view.getName().equals(v.getName()))
{
return x;
}
x++;
}
}
if(child instanceof Sequence)
{
Sequence sequence=(Sequence)child;
x=0;
for(String s:this.database.getSequences())
{
if(s.equals(sequence.getName())) return x;
x++;
}
}
if(child instanceof Column)
{
Table table=(Table)parent;
Column column=(Column)child;
x=0;
for(Column c:table.getColumns())
{
if(column.getName().equals(c.getName()))
{
return x;
}
x++;
}
}
return -1;
}
public void addTreeModelListener(TreeModelListener t)
{
listenerList.add(TreeModelListener.class,t);
}
public void removeTreeModelListener(TreeModelListener t)
{
listenerList.remove(TreeModelListener.class,t);
}
protected void fireTreeStructureChanged(Object oldRoot)
{
TreeModelEvent event = new TreeModelEvent(this, new Object[] { oldRoot });
EventListener[] listeners = listenerList.getListeners(TreeModelListener.class);
for(int i = 0; i < listeners.length; i++)
{
((TreeModelListener) listeners[i]).treeStructureChanged(event);
}
}
}