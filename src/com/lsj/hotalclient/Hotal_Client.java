package com.lsj.hotalclient;

import com.lsj.httpmethod.OrderDetailHttp;
import com.lsj.httpmethod.OrderQueryHttp;
import com.lsj.httpmethod.ReserveHttp;
import com.lsj.httpmethod.UnsubscribeHttp;
import com.lsj.user.User;
import com.qt.datapicker.DatePicker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hdmi on 16-11-25.
 */
public class Hotal_Client extends JFrame {
    String ReserveUrl="http://localhost:8080/Hotal_Server/Reserveservlet";
    String OrderUrl="http://localhost:8080/Hotal_Server/Orderserver";
    String OrderDetailUrl="http://localhost:8080/Hotal_Server/OrderDetailservlet";
    String UnsubscribelUrl="http://localhost:8080/Hotal_Server/Unsubscribeservlet";
    JButton allordrJButton=null;//全部订单
    JButton noworderJButton=null;
    JButton unsubcribeJButton=null;//退订按钮
    JButton loginButton=null;//登录
    JButton logupButton=null;//注册
    JButton reserveButton=null;//预定按钮
    JTextField accountTextField=null;
    ObservingTextField startTimeTextField=null;//入住时间
    ObservingTextField finishTimeTextField=null;//结束时间
    JComboBox firstRoomComboBox=null;//一等房数量
    JComboBox secondRoomComboBox=null;
    JComboBox thirdRoomComboBox=null;
    JTable orderJTable=null;//订单表格
    TableModel orderTableModel=null;
    JTable detailorderJTable=null;
    TableModel detailorderJTableModel=null;
    User user1=null;
    JSONObject reserveResultJsonObject=null;
    JSONArray orderQueryJsonArray=null;
    JSONArray orderDetailJsonArray=null;


    String[] columName={"预定时间","一等房","二等房","三等房","入住时间","离开时间"};
    String[] detailColumName={"房号","类型","价格","押金"};

    public Hotal_Client(User user){
        this();
        user1=user;
        if(user1.getAccount()!=null){
            accountTextField.setText(user1.getAccount());
        }
    }

    public Hotal_Client(){
        Container con=this.getContentPane();
        this.setVisible(true);
        this.setSize(900,500);
        con.setLayout(new GridBagLayout());
        GridBagConstraints c1=new GridBagConstraints();
        c1.gridx=0;c1.gridy=0;
        GridBagConstraints c2=new GridBagConstraints();
        c2.gridx=0;c2.gridy=1;c2.gridheight=5;
        GridBagConstraints c3=new GridBagConstraints();
        c3.gridx=1;c3.gridy=0;//c3.gridwidth=1;
        GridBagConstraints c4=new GridBagConstraints();
        c4.gridx=1;c4.gridy=1;
        GridBagConstraints c5=new GridBagConstraints();
        c5.gridx=1;c5.gridy=2;
        GridBagConstraints c6=new GridBagConstraints();
        c6.gridx=1;c6.gridy=3;
        GridBagConstraints c7=new GridBagConstraints();
        c7.gridx=1;c7.gridy=4;
        GridBagConstraints c8=new GridBagConstraints();
        c8.gridx=1;c8.gridy=5;
        GridBagConstraints c9=new GridBagConstraints();
        c9.gridx=0;c9.gridy=6;
        GridBagConstraints c10=new GridBagConstraints();
        c10.gridx=1;c10.gridy=6;

//       订单选择Panel
        JPanel order_buttonJPanel=new JPanel();
        allordrJButton=new JButton("订单");
        noworderJButton=new JButton("详细");
        unsubcribeJButton=new JButton("退订");
        JLabel tempLabel=new JLabel("       ");
        allordrJButton.addActionListener(new OrderQueryListener());
        noworderJButton.addActionListener(new OrderDetailListener());
        unsubcribeJButton.addActionListener(new UnsubscribeListener());
        order_buttonJPanel.add(allordrJButton);
        order_buttonJPanel.add(noworderJButton);
        order_buttonJPanel.add(tempLabel);
        order_buttonJPanel.add(unsubcribeJButton);
        order_buttonJPanel.setVisible(true);

        //登录注册预定Panel
        JPanel logJPanel=new JPanel();
        loginButton =new JButton("登录");
        logupButton=new JButton("注册");
        logJPanel.add(loginButton);
        logJPanel.add(logupButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login_Client(Hotal_Client.this);
            }
        });
        logupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Logup_Client();
            }
        });
        //账户Panel
        JPanel accountJPanel=new JPanel();
        JLabel accountJLabel=new JLabel("当前用户");
        accountTextField=new JTextField(15);
        accountTextField.setEditable(false);
        accountJPanel.add(accountJLabel);
        accountJPanel.add(accountTextField);


        //reservePanel
        JPanel reserveJPanel=new JPanel();
        reserveButton=new JButton("           预定          ");
        reserveButton.addActionListener(new ReserveActionListener());
        //开始时间Panel
        JPanel startTimePanel=new JPanel();
        JLabel startTimeJLabel=new JLabel("开始时间");
        startTimeTextField=new ObservingTextField(10);
        startTimeTextField.setEditable(false);
        JButton startTimeJButton=new JButton("选择");
        startTimeJButton.addActionListener(new datePicker_TextFieldActionListener(startTimeTextField,startTimeJButton));
        startTimePanel.add(startTimeJLabel);
        startTimePanel.add(startTimeTextField);
        startTimePanel.add(startTimeJButton);
        //结束时间Panel
        JPanel finishiTimePanel=new JPanel();
        JLabel finishTimeJLabel=new JLabel("结束时间");
        finishTimeTextField=new ObservingTextField(10);
        JButton finishTimeJButton=new JButton("选择");
        finishTimeTextField.setEditable(false);
        finishTimeJButton.addActionListener(new datePicker_TextFieldActionListener(finishTimeTextField,finishTimeJButton));
        finishiTimePanel.add(finishTimeJLabel);
        finishiTimePanel.add(finishTimeTextField);
        finishiTimePanel.add(finishTimeJButton);

        //一等房数量选择Panel
        JPanel firstRoomPanel=new JPanel();
        JLabel firstRoomLabel=new JLabel("一等房");
        firstRoomComboBox=new JComboBox();
        for(int i=0;i<5;i++){
            firstRoomComboBox.addItem(i);
        }
        firstRoomPanel.add(firstRoomLabel);
        firstRoomPanel.add(firstRoomComboBox);
        //二等房数量选择Panel
        JPanel secondRoomPanel=new JPanel();
        JLabel secondRoomLabel=new JLabel("二等房");
        secondRoomComboBox=new JComboBox();
        for(int i=0;i<10;i++){
            secondRoomComboBox.addItem(i);
        }
        secondRoomPanel.add(secondRoomLabel);
        secondRoomPanel.add(secondRoomComboBox);
        //三等房数量选择Panel
        JPanel thirdRoomPanel=new JPanel();
        JLabel thirdRoomLabel=new JLabel("三等房");
        thirdRoomComboBox=new JComboBox();
        for(int i=0;i<20;i++){
            thirdRoomComboBox.addItem(i);
        }
        thirdRoomPanel.add(thirdRoomLabel);
        thirdRoomPanel.add(thirdRoomComboBox);
        //房间选择Panel
        JPanel  selectRoomPanel=new JPanel();
        selectRoomPanel.add(firstRoomPanel);
        selectRoomPanel.add(secondRoomPanel);
        selectRoomPanel.add(thirdRoomPanel);

        //订单内容Panel
        JPanel orderJPanel=new JPanel();
        JLabel orderJLabel=new JLabel("订单:");
        orderJTable=new JTable(new Object[][]{},columName){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTableModel=new DefaultTableModel();
        //设置只能选择单行
        orderJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderJTable.setPreferredScrollableViewportSize(new Dimension(500,150));
        JScrollPane orderTableScrollPane=new JScrollPane(orderJTable);
        orderTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        orderJPanel.add(orderJLabel);
        orderJPanel.add(orderTableScrollPane);

        //详细订单内容
        JPanel detailOrderPanel=new JPanel();
        JLabel detailOrderJLabel=new JLabel("详情:");
        detailorderJTable=new JTable(new Object[][]{},detailColumName){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        detailorderJTableModel=new DefaultTableModel();
        detailorderJTable.setPreferredScrollableViewportSize(new Dimension(500,150));
        JScrollPane detailOrderTableScrollPane=new JScrollPane(detailorderJTable);
        detailOrderTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        detailOrderPanel.add(detailOrderJLabel);
        detailOrderPanel.add(detailOrderTableScrollPane);

        //设置屏幕在中间
        Toolkit kit = Toolkit.getDefaultToolkit();    // 定义工具包
        Dimension screenSize = kit.getScreenSize();   // 获取屏幕的尺寸
        int screenWidth = screenSize.width/2;         // 获取屏幕的宽
        int screenHeight = screenSize.height/2;       // 获取屏幕的高
        int height = this.getHeight();
        int width = this.getWidth();
        this.setLocation(screenWidth-width/2, screenHeight-height/2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        con.add(order_buttonJPanel,c1);
        con.add(orderJPanel,c2);
        con.add(logJPanel,c3);
        con.add(accountJPanel,c4);
        con.add(startTimePanel,c5);
        con.add(finishiTimePanel,c6);
        con.add(selectRoomPanel,c7);
        con.add(reserveButton,c8);
        con.add(detailOrderPanel,c9);

    }

    public class datePicker_TextFieldActionListener implements ActionListener{
        ObservingTextField observingTextField;
        JButton jButton;
        public datePicker_TextFieldActionListener(ObservingTextField observingTextField,JButton button){
            this.observingTextField=observingTextField;
            this.jButton=button;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            DatePicker dp = new DatePicker(observingTextField, Locale.CHINA);
            // previously selected date
            Date selectedDate = dp.parseDate(observingTextField.getText());
            dp.setSelectedDate(selectedDate);
            dp.start(observingTextField);

        }
    }

    public class ReserveActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String startTime=null;
            String finishTime=null;
            int firstRoomAmount;
            int secondRoomAmount;
            int thirdRoomAmount;
            Map<String,String> reserveMap=new HashMap<String,String>();
            //判断是否登录
            if(user1==null){
                JOptionPane.showMessageDialog(null,"请先登录");
            }else{
                startTime=startTimeTextField.getText();
                finishTime=finishTimeTextField.getText();
                //判断是否选择日期
                if(startTime.equals("")||finishTime.equals("")){
                    JOptionPane.showMessageDialog(null,"请先选择日期");
                }else{
                    DateFormat sdf =SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,Locale.CHINA); //new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
                    try {
                        Date startdate = sdf.parse(startTime);
                        Date finishtdate = sdf.parse(finishTime);
                        Date nowDate=new Date();
                        //判断其实日期是否大于当前日期
                        if(nowDate.after(startdate)){
                            JOptionPane.showMessageDialog(null,"不能预定过去时间");
                        }else if(finishtdate.before(startdate)){
                            JOptionPane.showMessageDialog(null,"离开时间早于预定时间");
                        }else{
                            firstRoomAmount=Integer.parseInt(firstRoomComboBox.getSelectedItem().toString());
                            secondRoomAmount=Integer.parseInt(secondRoomComboBox.getSelectedItem().toString());
                            thirdRoomAmount=Integer.parseInt(thirdRoomComboBox.getSelectedItem().toString());
                            //判断是否有预定房间
                            if(firstRoomAmount==0&&secondRoomAmount==0&&thirdRoomAmount==0){
                                JOptionPane.showMessageDialog(null,"请选择订房数量");
                            }else {
                                SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                                startTime = sf1.format(startdate);
                                finishTime = sf1.format(finishtdate);
                                reserveMap.put("account",user1.getAccount());
                                reserveMap.put("startTime",startTime);
                                reserveMap.put("finishTime",finishTime);
                                reserveMap.put("firstRoomAmount",String.valueOf(firstRoomAmount));
                                reserveMap.put("secondRoomAmount",String.valueOf(secondRoomAmount));
                                reserveMap.put("thirdRoomAmount",String.valueOf(thirdRoomAmount));
                                new Runnable(){
                                    @Override
                                    public void run() {
                                        reserveResultJsonObject=new ReserveHttp().doPost(ReserveUrl,reserveMap,"utf-8");

                                    }
                                }.run();
                                String resultFromServer=(reserveResultJsonObject.get("result").toString().trim());
                                if(resultFromServer.equals("true")){
                                    float firstPrice=Float.parseFloat(reserveResultJsonObject.get("firstPrice").toString());
                                    float secondPrce=Float.parseFloat(reserveResultJsonObject.get("secondPrce").toString());
                                    float thirdPrice=Float.parseFloat(reserveResultJsonObject.get("thirdPrice").toString());
                                    float firstDeposit=Float.parseFloat(reserveResultJsonObject.get("firstDeposit").toString());
                                    float secondDeposit=Float.parseFloat(reserveResultJsonObject.get("secondDeposit").toString());
                                    float thirdDeposit=Float.parseFloat(reserveResultJsonObject.get("thirdDeposit").toString());
                                    int firstRoomAmountFromServer=Integer.parseInt(reserveResultJsonObject.get("firstRoomAmount").toString());
                                    int secondRoomAmountFromServer=Integer.parseInt(reserveResultJsonObject.get("secondRoomAmount").toString());
                                    int thirdRoomAmountFromServer=Integer.parseInt(reserveResultJsonObject.get("thirdRoomAmount").toString());
                                    String startTimeFromServer=reserveResultJsonObject.get("startTime").toString();
                                    String finishTimeFromServer=reserveResultJsonObject.get("finishTime").toString();
                                    DecimalFormat decimalFormat=new DecimalFormat(".00");
                                    float allOrderMoney= (float) (firstPrice*firstRoomAmount*1.0+secondPrce*secondRoomAmount*1.0+thirdPrice*thirdRoomAmount*1.0);
                                    String allOrderMoneyString=decimalFormat.format(allOrderMoney);
                                    float allOrderDeposit= (float) (firstDeposit*firstRoomAmount*1.0+secondDeposit*secondRoomAmount*1.0+thirdDeposit*thirdRoomAmount*1.0);
                                    String allOrderDepositString=decimalFormat.format(allOrderDeposit);
                                    String resultOptionPaneString="订房成功：\n一等房:"+firstRoomAmountFromServer+"\n二等房："+
                                            secondRoomAmountFromServer+"\n三等房："+thirdRoomAmountFromServer+"\n押金："+allOrderDepositString+"\n总金额："+
                                            allOrderMoneyString+"\n时间："+startTimeFromServer+" - "+finishTimeFromServer;
                                    JOptionPane.showMessageDialog(null,resultOptionPaneString);
                                }else if(resultFromServer.equals("false")){
                                    int firstRoomAmountFromServer=Integer.parseInt(reserveResultJsonObject.get("firstRoomAmount").toString());
                                    int secondRoomAmountFromServer=Integer.parseInt(reserveResultJsonObject.get("secondRoomAmount").toString());
                                    int thirdRoomAmountFromServer=Integer.parseInt(reserveResultJsonObject.get("thirdRoomAmount").toString());
                                    String resultOptionPaneString="订房失败，剩余："+"\n一等房："+firstRoomAmountFromServer+"\n二等房："+
                                            secondRoomAmountFromServer+"\n三等房："+thirdRoomAmountFromServer;
                                    JOptionPane.showMessageDialog(null,resultOptionPaneString);
                                }else if(resultFromServer.equals("connectError")){
                                    JOptionPane.showMessageDialog(null,"连接出错");
                                }
                        }
                    }
                }catch (Exception e1) {
                        e1.printStackTrace();
                    }
            }
        }
    }



}

    public class OrderQueryListener implements  ActionListener{
        Map<String,String> orderMap=new HashMap<String,String>();

        @Override
        public void actionPerformed(ActionEvent e) {
            if(user1==null){
                JOptionPane.showMessageDialog(null,"请先登录");
            }else{
                orderMap.put("account",user1.getAccount());
                new Runnable(){
                    @Override
                    public void run() {
                        orderQueryJsonArray=new OrderQueryHttp().doPost(OrderUrl,orderMap,"utf-8");
                        if(orderQueryJsonArray!=null){
                            try {
                                JSONObject orderResultJsonObject=(JSONObject)orderQueryJsonArray.get(0);
                                String orderResult=orderResultJsonObject.get("result").toString();
                                if(orderResult.equals("true")){
                                    Vector ordercolumName=new Vector();
                                    for (int i=0;i<columName.length;i++){
                                        ordercolumName.add(columName[i]);
                                    }
                                    Vector data=new Vector();
                                    for (int i=1;i<orderQueryJsonArray.length();i++){
                                        JSONObject orderRowJsonObject=(JSONObject)orderQueryJsonArray.get(i);
                                        Vector row=new Vector();
                                        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                                        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                                        DateFormat sdf =SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,Locale.CHINA);
                                        try {
                                            Date startdate = sdf.parse(orderRowJsonObject.get("check_in_time").toString());
                                            Date finishtdate = sdf.parse(orderRowJsonObject.get("departure_time").toString());
                                            Date order_datetime = sf2.parse(orderRowJsonObject.get("order_datetime").toString());
                                            String startdateString=sf1.format(startdate);
                                            String finishtdateString=sf1.format(finishtdate);
                                            String order_datetimeString=sf2.format(order_datetime);
                                            row.add(order_datetimeString);
                                            row.add(orderRowJsonObject.get("firstRoomAmount").toString());
                                            row.add(orderRowJsonObject.get("secondRoomAmount").toString());
                                            row.add(orderRowJsonObject.get("thirdRoomAmount").toString());
                                            row.add(startdateString);
                                            row.add(finishtdateString);
                                            data.add(row);
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }


                                    }
                                    orderTableModel=new DefaultTableModel(data,ordercolumName);
                                    orderJTable.setModel(orderTableModel);


                                }else  if(orderResult.equals("false")){
                                    JOptionPane.showMessageDialog(null,"查询失败");
                                }else if (orderResult.equals("connectError")){
                                    JOptionPane.showMessageDialog(null,"连接出错");
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }.run();
            }
        }
    }

    public class OrderDetailListener implements ActionListener{
        Map<String,String> orderdetailMap=new HashMap<String,String>();
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectOrderTableRow=orderJTable.getSelectedRow();
            if(user1==null){
                JOptionPane.showMessageDialog(null,"请先登录");
            }
            else if(selectOrderTableRow==-1){
                JOptionPane.showMessageDialog(null,"选择一个订单才能查看详情");
            }else{

                    orderdetailMap.put("account",user1.getAccount());
                    String order_dateTime=(String)orderJTable.getValueAt(selectOrderTableRow,0);
                    orderdetailMap.put("order_datetime",order_dateTime);
                    new Runnable(){
                        @Override
                        public void run() {
                            orderDetailJsonArray=new OrderDetailHttp().doPost(OrderDetailUrl,orderdetailMap,"utf-8");
                            System.out.print(orderDetailJsonArray+"\n");
                        }
                    }.run();
                    if(orderDetailJsonArray!=null){
                        JSONObject orderResultJsonObject= null;
                        try {
                            //取得第一项访问结果是否成功
                            orderResultJsonObject = (JSONObject)orderQueryJsonArray.get(0);
                            String orderResult=orderResultJsonObject.get("result").toString();
                            if(orderResult.equals("true")){
                                Vector ordercolumName=new Vector();
                                for (int i=0;i<detailColumName.length;i++){
                                    ordercolumName.add(detailColumName[i]);
                                }
                                Vector data=new Vector();
                                for(int i=1;i<orderDetailJsonArray.length();i++){
                                    JSONObject orderDetailJsonObject=(JSONObject) orderDetailJsonArray.get(i);
                                    Vector row =new Vector();
                                    String roomid=orderDetailJsonObject.get("roomid").toString();
                                    String typename=orderDetailJsonObject.get("typename").toString();
                                    String price=orderDetailJsonObject.get("price").toString();
                                    String deposit=orderDetailJsonObject.get("deposit").toString();
                                    row.add(roomid);
                                    row.add(typename);
                                    row.add(price);
                                    row.add(deposit);
                                    data.add(row);
                                }
                                detailorderJTableModel=new DefaultTableModel(data,ordercolumName);
                                detailorderJTable.setModel(detailorderJTableModel);

                            }else  if(orderResult.equals("false")){
                                JOptionPane.showMessageDialog(null,"查询失败");
                            }else if (orderResult.equals("connectError")){
                                JOptionPane.showMessageDialog(null,"连接出错");
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }

            }
        }
    }

    public class UnsubscribeListener implements ActionListener{
        Map<String,String> unsubscribeMap=new HashMap<String,String>();
        String unsubscribeResult =null;
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectOrderTableRow=orderJTable.getSelectedRow();
            if(user1!=null){
                unsubscribeMap.put("account",user1.getAccount());
                if(selectOrderTableRow!=-1){
                    String order_dateTime=(String)orderJTable.getValueAt(selectOrderTableRow,0);
                    String checkin_dateTime=(String)orderJTable.getValueAt(selectOrderTableRow,5);
                    unsubscribeMap.put("order_dateTime",order_dateTime);
                    System.out.print(unsubscribeMap);
                    DateFormat sdf =SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,Locale.CHINA);
                    try {
                        Date startTime=sdf.parse(checkin_dateTime);
                        Date nowDate=new Date();
                        //比较订单日期是否过期
                        if(startTime.getTime()<nowDate.getTime()){
                            JOptionPane.showMessageDialog(null,"请确保订单时间在有效期内");
                        }else {
                            new Runnable() {
                                @Override
                                public void run() {
                                    unsubscribeResult = new UnsubscribeHttp().doPost(UnsubscribelUrl, unsubscribeMap, "utf-8");
                                    System.out.print("\n" + unsubscribeResult);
                                }
                            }.run();
                            if (unsubscribeResult.equals("true")) {
                                JOptionPane.showMessageDialog(null, "退订成功，请刷新订单");
                            } else if (unsubscribeResult.equals("false")) {
                                JOptionPane.showMessageDialog(null, "退订失败");
                            } else if (unsubscribeResult.equals("connectError")) {
                                JOptionPane.showMessageDialog(null, "连接出错");
                            }
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }


                }
            }
        }
    }
    public static void main(String args[]){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Hotal_Client HC=new Hotal_Client();
            }
        });
    }
}
