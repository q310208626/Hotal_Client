package com.lsj.hotalclient;

import com.lsj.httpmethod.LogupHttp;
import com.lsj.plaindocument.MyRegExp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hdmi on 16-11-28.
 */
public class Logup_Client extends JFrame {
    public String URL="http://localhost:8080/Hotal_Server/Registservlet";
    private JTextField accountTextField=null;
    private JTextField passwordTextField=null;
    private JTextField surePasswordTextField=null;
    private JLabel messageLabel=null;
    private Boolean loginResult=false;

    public Logup_Client(){
        Container con=this.getContentPane();
        this.setSize(300,250);
        this.setVisible(true);
        Toolkit kit = Toolkit.getDefaultToolkit();    // 定义工具包
        Dimension screenSize = kit.getScreenSize();   // 获取屏幕的尺寸
        int screenWidth = screenSize.width/2;         // 获取屏幕的宽
        int screenHeight = screenSize.height/2;       // 获取屏幕的高
        int height = this.getHeight();
        int width = this.getWidth();
        this.setLocation(screenWidth-width/2, screenHeight-height/2);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                con.setVisible(false);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        messageLabel=new JLabel("输入以英文开头数英结合");

        //账号Panel
        JPanel accountPanel=new JPanel();
        JLabel accountLabel=new JLabel("账户");
        accountTextField=new JTextField(15);
        accountTextField.setDocument(new MyRegExp("^[A-Za-z][a-zA-Z0-9]{0,14}"));
        accountPanel.add(accountLabel);
        accountPanel.add(accountTextField);
        //密码Panel
        JPanel passwordPanel=new JPanel();
        JLabel passwordLabel=new JLabel("密码");
        passwordTextField=new JTextField(15);
        passwordTextField.setDocument(new MyRegExp("[A-Za-z0-9]{0,15}"));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordTextField);
        //密码确认Panel
        JPanel surePasswordPanel=new JPanel();
        JLabel surePasswordLabel=new JLabel("确认");
        surePasswordTextField=new JTextField(15);
        surePasswordTextField.setDocument(new MyRegExp("[A-Za-z0-9]{0,15}"));
        surePasswordPanel.add(surePasswordLabel);
        surePasswordPanel.add(surePasswordTextField);
        //登录取消按钮
        JButton loginButton=new JButton("           注册           ");
        loginButton.addActionListener(new LogupActionListner());
        JLabel tempLabel=new JLabel("   ");
        JButton cancleButton=new JButton("           取消           ");
        cancleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        con.setLayout(new GridBagLayout());
        GridBagConstraints c0=new GridBagConstraints();
        c0.gridx=0;c0.gridy=0;
        GridBagConstraints c1=new GridBagConstraints();
        c1.gridx=0;c1.gridy=1;
        GridBagConstraints c2=new GridBagConstraints();
        c2.gridx=0;c2.gridy=2;
        GridBagConstraints c3=new GridBagConstraints();
        c3.gridx=0;c3.gridy=3;
        GridBagConstraints c4=new GridBagConstraints();
        c4.gridx=0;c4.gridy=4;
        GridBagConstraints c5=new GridBagConstraints();
        c5.gridx=0;c5.gridy=5;
        GridBagConstraints c6=new GridBagConstraints();
        c6.gridx=0;c6.gridy=6;

        con.add(accountPanel,c0);
        con.add(passwordPanel,c1);
        con.add(surePasswordPanel,c2);
        con.add(loginButton,c3);
        con.add(tempLabel,c4);
        con.add(cancleButton,c5);
        con.add(messageLabel,c6);
    }

    public class LogupActionListner implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            LogupHttp logupHttp=new LogupHttp();
            String account=accountTextField.getText();
            String password=passwordTextField.getText();
            String surePassword=surePasswordTextField.getText();
            Map<String,String> map=new HashMap<String,String>();
            if(account.equals("")) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        messageLabel.setText("用户名不能为空");
                    }
                });
            }else if (password.equals("")) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        messageLabel.setText("密码不能为空");
                    }
                });
            } else if(surePassword.equals("")){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        messageLabel.setText("确认不能为空");
                    }
                });
            }else if(!password.equals(surePassword)){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        messageLabel.setText("两次密码不同");
                    }
                });
            }else {
                map.put("account", account);
                map.put("password", password);
                new Runnable() {
                    @Override
                    public void run() {
                        loginResult=logupHttp.doPost(URL, map, "utf-8");
                    }
                }.run();
            }

            if(loginResult==true){
                Logup_Client.this.setVisible(false);
            }else{
                messageLabel.setText("注册失败");
            }
        }
    }
}
