<<<<<<< HEAD
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


public class Conversation2


{
JFrame Frame = new JFrame("智能对话");
JLabel Label1= new JLabel("M:");
JLabel Label2= new JLabel("W:");
JButton Button = new JButton("Conversation");
JTextField Text1= new JTextField("Hello!",30);
JTextField Text2= new JTextField("Hello!",30);
JPanel panel1= new JPanel();
JPanel panel2= new JPanel();

public Conversation2()

{ //建立中间容器面板panel1,panel1的布局管理器为FlowLayout
panel1.setLayout(new FlowLayout());
panel1.add(Label1);
panel1.add(Text1);
panel1.add(Label2);
panel1.add(Text2);
//建立中间容器面板panel2,panel2的布局管理器为FlowLayout
panel2.setLayout(new FlowLayout());
panel2.add(Button);
//建立顶层容器ContentPane,ContentPane布局管理器为BorderLayout
Frame.getContentPane().setLayout(new BorderLayout());
Frame.getContentPane().add(panel1,BorderLayout.NORTH);
Frame.getContentPane().add(panel2,BorderLayout.CENTER);
Frame.setSize(1024,600);
Frame.setVisible(true);
//button的事件处理
class ButtonActionListener implements ActionListener
{
public void actionPerformed(ActionEvent e)


{
try{
FileReader f=new FileReader("d:\\a.txt");
BufferedReader buf= new BufferedReader(f);
String s;
int b=0;

while((s=buf.readLine())!=null)
{
if(s.equals(Text1.getText()))
{break;}
b++; }
if(b==0)
{ Text2.setText("不知道,你来说说!");}
if(b==1)
{ Text1.setText("分3类");
Text1.setText("J2SE,J2ME,J2EE");
Text1.setText("");

}

}catch(Exception ex)
{
System.out.println(e.toString());

}
}



}
Button.addActionListener(new ButtonActionListener());

//Window关闭事件处理
Frame.addWindowListener(new WindowAdapter()
{ public void windowClosing(WindowEvent e)
{
System.exit(0);
}


});
}



public static void main(String[ ] args)
{
Conversation2 conversation= new Conversation2();
}



}
=======
# it
杜某激情修改
上传了数个文件
>>>>>>> 20e91258b78329f2881d9bcdd8306b475fcad9f9
