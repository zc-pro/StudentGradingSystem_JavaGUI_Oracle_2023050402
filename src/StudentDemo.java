import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDemo extends JFrame {
    private JButton btnbasicInfo;
    private JButton btnGrade;
    private JButton btnPwd;

    private JPanel panel;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentDemo(int x, int y, int witdh, int height) throws SQLException {
        super("学生端口");
        this.setBounds(x, y, witdh, height);
        init();
        this.setLocationByPlatform(true); //居中显示
    }

    public void init() throws SQLException {
        btnbasicInfo = new JButton("基本信息");
        btnGrade = new JButton("查询成绩");
        btnPwd = new JButton("修改密码");
        JPanel buttonPanel = new JPanel();

        buttonPanel.add(btnbasicInfo);
        buttonPanel.add(btnGrade);
        buttonPanel.add(btnPwd);
        buttonPanel.setVisible(true);

        table = new JTable(20, 9);
        table.setVisible(true);

        panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        panel.add(buttonPanel);
        panel.add(addJTable(table));
        this.add(panel);
        this.setVisible(true);

        btnGrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                    String sql = "SELECT * FROM STUGRADEINFO WHERE SNO = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, LoginDemo.getUsercode());
                    ResultSet rs = stmt.executeQuery();
                    List<List<String>> list = new ArrayList<>();
                    while (rs.next()) {
                        String cno = rs.getString("CNO");
                        String cname = rs.getString("CNAME");
                        String ccredit = rs.getString("CCREDIT");
                        String grade = rs.getString("GRADE");
                        String tno = rs.getString("TNO");
                        String tname = rs.getString("TNAME");
                        List<String> temp = new ArrayList<>();
                        temp.add(cno);
                        temp.add(cname);
                        temp.add(ccredit);
                        temp.add(grade);
                        temp.add(tno);
                        temp.add(tname);

                        list.add(temp);
                    }
                    tableModel = new DefaultTableModel(list.size(), 7);
                    String[] conlumnNames = {"课程编号", "课程名称", "学分", "成绩", "任课教师编号", "任课教师"};
                    tableModel.setColumnIdentifiers(conlumnNames);
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < 6; j++) {
                            tableModel.setValueAt(list.get(i).get(j), i, j);
                        }
                    }
                    table.setModel(tableModel);
                    table.setDefaultEditor(Object.class, null); //设置表格成绩表不能修改
                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                    centerRenderer.setHorizontalAlignment(JLabel.CENTER);  // 设置显示居中
                    table.setDefaultRenderer(Object.class, centerRenderer);
                    table.repaint();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnbasicInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                    String sql = "SELECT * FROM STUBASICINFO WHERE SNO = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, LoginDemo.getUsercode());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        String sno = rs.getString("SNO");
                        String sname = rs.getString("SNAME");
                        String ssex = rs.getString("SSEX");
                        String sage = rs.getString("SAGE");
                        String sclass = rs.getString("SCLASS");
                        String faculty = rs.getString("FACULTY");
                        List<String> list = new ArrayList<>();
                        list.add(sno);
                        list.add(sname);
                        list.add(ssex);
                        list.add(sage);
                        list.add(sclass);
                        list.add(faculty);
                        StuBasicInfo stuBasicInfo = new StuBasicInfo(list);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnPwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StuChangePwd stuChangePwd = new StuChangePwd(600, 400, 400, 250);
            }
        });

    }

    public static class StuBasicInfo extends JFrame {
        private final JTextArea textArea;

        public StuBasicInfo(List<String> list) {
            super("学生基本信息");
            textArea = new JTextArea();
            this.setBounds(600, 200, 400, 250);
            String text = "学号：" + list.get(0) + "\n姓名：" + list.get(1) + "\n性别：" + list.get(2) + "\n年龄：" + list.get(3) + "\n班级：" + list.get(4) + "\n学院：" + list.get(5);
            textArea.setText(text);
            textArea.setFont(new Font("宋体", Font.BOLD, 15));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            this.add(scrollPane);
            this.setVisible(true);
        }
    }

    public static class StuChangePwd extends JFrame {
        JLabel btn_old_OK;
        JLabel btn_new_OK;
        JLabel btn_twice_OK;
        JButton btn_finall_OK;
        JPasswordField pwdNewOneField;
        JPasswordField pwdNewTwoField;
        JPasswordField pwdOldField;
        String oldPwd;
        String newPwd;
        String twicePwd;

        public StuChangePwd(int x, int y, int witdh, int height) {
            super("修改密码");
            this.setBounds(x, y, witdh, height);
            init();
        }

        public void init() {
            btn_old_OK = new JLabel("输入旧密码");
            btn_new_OK = new JLabel("输入新密码");
            btn_twice_OK = new JLabel("确认新密码");
            btn_finall_OK = new JButton("确认修改");
            pwdOldField = new JPasswordField();
            pwdNewOneField = new JPasswordField();
            pwdNewTwoField = new JPasswordField();
            GridLayout gridLayout = new GridLayout(3, 2);
            JPanel jPanel = new JPanel();
            jPanel.setLayout(gridLayout);
            jPanel.add(btn_old_OK);
            jPanel.add(pwdOldField);
            jPanel.add(btn_new_OK);
            jPanel.add(pwdNewOneField);
            jPanel.add(btn_twice_OK);
            jPanel.add(pwdNewTwoField);
            jPanel.setVisible(true);
            this.setLayout(new BorderLayout());
            this.add(jPanel, BorderLayout.NORTH);
            this.add(btn_finall_OK, BorderLayout.SOUTH);
            this.setVisible(true);
            btn_finall_OK.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    oldPwd = pwdOldField.getText();
                    newPwd = pwdNewOneField.getText();
                    twicePwd = pwdNewTwoField.getText();
                    if (!oldPwd.equals(LoginDemo.getPassword())) {
                        JOptionPane.showMessageDialog(null, "输入原密码有误！");
                    } else if (newPwd.equals(oldPwd)) {
                        JOptionPane.showMessageDialog(null, "新密码与旧密码相同！");
                    } else if (!newPwd.equals(twicePwd)) {
                        JOptionPane.showMessageDialog(null, "前后两次输入的新密码不同！");
                    } else {
                        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                            String sql = "UPDATE STUDENT SET PWD = ? WHERE STUDENT.SNO = ?";
                            PreparedStatement stmt = conn.prepareStatement(sql);
                            stmt.setString(1, twicePwd);
                            stmt.setString(2, LoginDemo.getUsercode());
                            int count = stmt.executeUpdate();
                            conn.commit();
                            if (count > 0) {
                                JOptionPane.showMessageDialog(null, "修改成功！");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });

        }


    }

    public JScrollPane addJTable(JTable j) {
        JScrollPane scrollPane = new JScrollPane(j);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

}
