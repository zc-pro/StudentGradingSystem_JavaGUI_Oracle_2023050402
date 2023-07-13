import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import javax.swing.*;
import javax.swing.JOptionPane;
public class LoginDemo extends JFrame {
    private static JButton btnLogin; //登录按钮
    private static JTextField usernameFiled; //输入名字文本条
    private static JTextField usercodeFiled; //输入名字文本条
    private static JPasswordField passwordField;//输入密码文本条
    private static JRadioButton rbt1;
    private static JRadioButton rbt2;
    private static JRadioButton rbt3;
    private static ButtonGroup group;
    private List<Connection> list;

    public LoginDemo(int x, int y, int witdh, int height){
        super("学生成绩管理系统");
        //MyDataScoure conn = new MyDataScoure();
        this.setBounds(x, y, witdh, height);
        this.setVisible(true);
        setLocationRelativeTo(null);
        init();
    }
    public void init(){

        this.setLayout(new GridLayout(4,1));
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        JPanel jPanel3 = new JPanel();
        JPanel jPanel4 = new JPanel();

        btnLogin = new JButton("登录");
        usernameFiled = new JTextField(20);
        usercodeFiled = new JTextField(20);
        passwordField = new JPasswordField(20);

        group = new ButtonGroup();
        rbt1 = new JRadioButton("学生");
        rbt2 = new JRadioButton("教师");
        rbt3 = new JRadioButton("管理员");
        group.add(rbt1);
        group.add(rbt2);
        group.add(rbt3);

        jPanel1.add(rbt1);
        jPanel1.add(rbt2);
        jPanel1.add(rbt3);


        jPanel2.add(new JLabel("账号:"));
        jPanel2.add(usercodeFiled);

        jPanel3.add(new JLabel("密码:"));
        jPanel3.add(passwordField);
        jPanel4.add(btnLogin);



        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!rbt1.isSelected()&&!rbt2.isSelected()&&!rbt3.isSelected()){
                    JOptionPane.showMessageDialog(null,"未选择用户类型！","提示",1);
                }
                String sql = null;
                PreparedStatement stmt =null;
                try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","system","Aa01076543")){
                    System.out.println("连接成功！");
                    if(rbt1.isSelected()){
                        sql = "SELECT * FROM STUDENT WHERE SNO = ? AND PWD = ?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1,usercodeFiled.getText());
                        stmt.setString(2,passwordField.getText());
                        ResultSet rs = stmt.executeQuery();
                        if(rs.next()){
                            StudentDemo s = new StudentDemo(400,150,700,300);
                        }
                        else{
                            conn.close();
                            JOptionPane.showMessageDialog(null, "用户名/学号/密码有误！","错误",0);
                        }
                    }
                    else if(rbt2.isSelected()){
                        sql = "SELECT * FROM TEACHER WHERE TNO = ? AND PWD = ?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1,usercodeFiled.getText());
                        stmt.setString(2,passwordField.getText());
                        ResultSet rs = stmt.executeQuery();
                        if(rs.next()){
                            TeacherDemo t = new TeacherDemo(400,150,1000,600);
                        }
                        else{
                            conn.close();
                            JOptionPane.showMessageDialog(null, "用户名或密码有误！","错误",0);
                        }
                    }
                    else if(rbt3.isSelected()){
                        if(getUsercode().equals("system")&&getPassword().equals("Aa01076543") ){
                            //进入管理员界面
                            AdministratorDemo a = new AdministratorDemo(400,150,320,150);
                        }
                        else{
                            conn.close();
                            JOptionPane.showMessageDialog(null, "用户名或密码有误！","错误",0);
                        }
                    }

                }
                catch (SQLException ex){
                    ex.printStackTrace();
                }
            }
        });

        this.add(jPanel1);
        this.add(jPanel2);
        this.add(jPanel3);
        this.add(jPanel4);
        this.setVisible(true);
    }

    public static String getPassword(){
        return passwordField.getText();
    }
    public static String getUsercode(){
        return usercodeFiled.getText();
    }
}
