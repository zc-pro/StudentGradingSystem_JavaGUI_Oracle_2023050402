import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executor;

public class AdministratorDemo extends JFrame {
    private JButton btnStudentManagement;
    private JButton btnTeacherManagement;
    private JButton btnCourseManagement;
    private JButton btnAccountManagement;

    public AdministratorDemo(int x, int y, int width, int height) {
        super("管理员界面");
        this.setBounds(x, y, width, height);
        init();
        this.setVisible(true);
        this.setLocationByPlatform(true);
    }

    public void init() {
        btnStudentManagement = new JButton("学生信息管理");
        btnTeacherManagement = new JButton("教师信息管理");
        btnCourseManagement = new JButton("课程信息管理");
        btnAccountManagement = new JButton("账号信息管理");
        JPanel jPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
        jPanel.add(btnStudentManagement);
        jPanel.add(btnTeacherManagement);
        jPanel.add(btnCourseManagement);
        jPanel.add(btnAccountManagement);
        this.add(jPanel);

        btnStudentManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentManagement studentManagement = new StudentManagement(400, 200, 1000, 400);
            }
        });
        btnTeacherManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TeacherManagement teacherManagement = new TeacherManagement(400, 200, 800, 400);
            }
        });
        btnCourseManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CourseManagement courseManagement = new CourseManagement(400, 200, 700, 400);
            }
        });
        btnAccountManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AccountManagement accountManagement = new AccountManagement(600, 300, 500, 400);
            }
        });
    }


    public class StudentManagement extends DemoManagement {
        public StudentManagement(int x, int y, int witdh, int hegith) {
            super(x, y, witdh, hegith);
            init();
            this.setTitle("学生信息管理界面");
            this.setVisible(true);
        }

        public void init() {
            btnShow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                        String sql = "SELECT * FROM SMANAGE";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery(sql);
                        tableList.clear();
                        while (rs.next()) {
                            List<String> elements = new ArrayList<>();
                            elements.add(rs.getString("SNO"));
                            elements.add(rs.getString("SNAME"));
                            elements.add(rs.getString("SSEX"));
                            elements.add(rs.getString("SAGE"));
                            elements.add(rs.getString("FACULTY"));
                            elements.add(rs.getString("SCLASS"));
                            elements.add(rs.getString("SYEAR"));
                            elements.add(rs.getString("SENTER"));
                            tableList.add(elements);
                        }
//                        if (tableList.size() > tableModel.getRowCount()) {
//                            tableModel.setRowCount(tableList.size());
//                        }
                        tableModel.setRowCount(0);
                        tableModel.setRowCount(tableList.size()+20);
                        String[] conlumnNames = {"学号", "姓名", "性别", "年龄", "所属学院", "班级", "年级", "入学时间"};
                        tableModel.setColumnIdentifiers(conlumnNames);
                        tableModel = (DefaultTableModel) showTable.getModel();
                        for (int i = 0; i < tableList.size(); i++) {
                            for (int j = 0; j < 8; j++) {
                                tableModel.setValueAt(tableList.get(i).get(j), i, j);
                                //System.out.println(tableList.get(i).get(j));
                            }
                        }
                        showTable.setDefaultEditor(Object.class, null); //设置表格成绩表不能修改
                        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                        centerRenderer.setHorizontalAlignment(JLabel.CENTER);  // 设置显示居中
                        showTable.setDefaultRenderer(Object.class, centerRenderer);

                        //设置表格的列的宽度
                        TableColumnModel columnModel = showTable.getColumnModel();
                        for (int i = 0; i < showTable.getColumnCount(); i++) {
                            TableColumn column = columnModel.getColumn(i);
                            if (i == 0) {
                                column.setPreferredWidth(140);
                            } else if (i == 2 || i == 3) {
                                column.setPreferredWidth(40);
                            } else if (i == 4) {
                                column.setPreferredWidth(200);
                            } else if (i == 1 || i == 6) {
                                column.setPreferredWidth(60);
                            } else {
                                column.setPreferredWidth(70);
                            }
                        }


                        showTable.repaint();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try {
                        String S = doc.getText(0, doc.getLength());
                        if (S.length() == tableList.get(0).get(0).length()) {
                            boolean flag = false;
                            int row = 0;
                            tableModel.setRowCount(0);
                            for (int i = 0; i < tableList.size(); i++) {
                                if (S.equals(tableList.get(i).get(0))) {
                                    row++;
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                JOptionPane.showMessageDialog(null, "没有该账号！");
                            } else {
                                tableModel.setRowCount(row);
                                int num = 0;
                                for (int i = 0; i < tableList.size(); i++) {
                                    if (S.equals(tableList.get(i).get(0))) {
                                        for (int k = 0; k < tableList.get(i).size(); k++) {
                                            //System.out.println(tableList.get(i).get(k));
                                            tableModel.setValueAt(tableList.get(i).get(k), num, k);
                                        }
                                        num++;
                                    }
                                }
                                showTable.setModel(tableModel);
                                showTable.setDefaultEditor(Object.class, null); //设置表格成绩表不能修改
                                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                                centerRenderer.setHorizontalAlignment(JLabel.CENTER);  // 设置显示居中
                                showTable.setDefaultRenderer(Object.class, centerRenderer);
                                showTable.repaint();
                            }
                        }
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {

                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
            changeField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")){
                        String S = doc.getText(0, doc.getLength());
                        String sqlFind = "SELECT * FROM STUDENT WHERE SNO = ?";
                        PreparedStatement stmt = conn.prepareStatement(sqlFind);
                        stmt.setString(1,S);
                        ResultSet findSet = stmt.executeQuery();
                        if(findSet.next()){
                            Change changeStudent = new Change(400,300,600,300, S);
                        }
                    }
                    catch (SQLException ex){
                        ex.printStackTrace();
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<String> labels = new ArrayList<>();
                    labels.add("学生编号");
                    labels.add("学生姓名");
                    labels.add("学生年龄");
                    labels.add("所属班级");
                    labels.add("所属学院");
                    labels.add("入学时间");
                    labels.add("所属年级");
                    labels.add("学生性别");
                    Add addstudent = new Add(400, 200, 500, 300, labels);
                }
            });
            btnDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Delete deletestudent = new Delete(400,200,350,100);
                }
            });
        }
        public class Add extends JFrame {
            JButton btnOk;
            List<String> list = new ArrayList<>();
            JTextField[] jTextField;
            JComboBox<String> comboBoxSex = new JComboBox<>();
            JComboBox<String> comboBoxGrade = new JComboBox<>();

            public Add(int x, int y, int witdh, int height, List<String> jlbNum) {
                this.setTitle("增加学生");
                btnOk = new JButton("确定");
                comboBoxSex.addItem("男");
                comboBoxSex.addItem("女");
                comboBoxGrade.addItem("大一");
                comboBoxGrade.addItem("大二");
                comboBoxGrade.addItem("大三");
                comboBoxGrade.addItem("大四");
                JPanel jPanel = new JPanel();
                jTextField = new JTextField[jlbNum.size()];
                jPanel.setLayout(new GridLayout(9, 2));
                for (int i = 0; i < 8; i++) {
                    JPanel temp = new JPanel();
                    temp.add(new JLabel(jlbNum.get(i)));
                    jTextField[i] = new JTextField(15);
                    if (i != 6 && i!= 7) {
                        temp.add(jTextField[i]);
                    }
                    else if(i==6){
                        temp.add(comboBoxGrade);
                    }
                    else if(i==7){
                        temp.add(comboBoxSex);
                    }
                    jPanel.add(temp);
                }
                JPanel btn = new JPanel();
                btn.add(btnOk);
                jPanel.add(btn);
                this.add(jPanel);
                this.setBounds(x, y, witdh, height);
                this.setVisible(true);

                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean flag = true;
                        list.clear();
                        for (int i = 0; i < 6; i++) {
                            if (jTextField[i].getText().equals("")) {
                                flag = false;
                            } else {
                                list.add(jTextField[i].getText());
                            }
                        }
                        if (!flag) {
                            JOptionPane.showMessageDialog(null, "部分信息未录入");
                        } else {
                            try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                                String judge = "SELECT * FROM STUDENT WHERE SNO = ?";
                                PreparedStatement stmt1 = conn.prepareStatement(judge);
                                stmt1.setString(1,list.get(0));
                                ResultSet rs1 = stmt1.executeQuery();
                                if(rs1.next()){
                                    JOptionPane.showMessageDialog(null,"已存在该名学生");
                                }
                                else {
                                    String sql = "INSERT INTO STUDENT(SNO,SNAME,SAGE,SCLASS,FACULTY,SENTER,SSEX,SYEAR,PWD) VALUES(?,?,?,?,?,?,?,?,123456)";
                                    PreparedStatement stmt = conn.prepareStatement(sql);
                                    for (int i = 0; i < 8; i++) {
                                        if(i!=6&&i!=7) {
                                            stmt.setString(i + 1, list.get(i));
                                        }
                                        else if(i==6){
                                            stmt.setString(i+1,comboBoxSex.getSelectedItem().toString());
                                        }
                                        else {
                                            stmt.setString(i+1,comboBoxGrade.getSelectedItem().toString());
                                        }
                                    }
                                    int rows = stmt.executeUpdate();
                                    conn.commit();
                                    String show = "账号"+list.get(0)+"添加成功";
                                    JOptionPane.showMessageDialog(null,show);
                                    System.out.println("commit成功");
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
        }

        public class Delete extends JFrame{
            JButton btnOk;
            JTextField inputCode;
            public Delete(int x, int y, int witdh, int heigth){
                this.setBounds(x,y,witdh,heigth);
                this.setTitle("删除学生");
                btnOk = new JButton("确定");
                inputCode = new JTextField(15);
                this.setLayout(new FlowLayout());
                this.add(new JLabel("账号："));
                this.add(inputCode);
                this.add(btnOk);
                this.setVisible(true);
                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(inputCode.getText().equals("")){
                            JOptionPane.showMessageDialog(null,"输入为空！");
                        }
                        try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")){
                            String code = inputCode.getText();
                            String sqlFind = "SELECT * FROM STUDENT WHERE SNO = ?";
                            PreparedStatement stmt = conn.prepareStatement(sqlFind);
                            stmt.setString(1,code);
                            ResultSet findSet = stmt.executeQuery();
                            if(!findSet.next()){
                                JOptionPane.showMessageDialog(null,"不存在该名学生！");
                            }
                            else{
                                String sqlDelete = "DELETE FROM STUDENT WHERE SNO = ?";
                                stmt = conn.prepareStatement(sqlDelete);
                                stmt.setString(1,code);
                                int rows = stmt.executeUpdate();
                                conn.commit();
                                System.out.println("commit");
                                String show = "账号"+code+"数据已删除！！！";
                                JOptionPane.showMessageDialog(null,show);
                            }
                        }
                        catch (SQLException ex){
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }

        public class Change extends JFrame{
            JButton btnOk;
            JTextField[] jTextField;
            public Change(int x, int y, int width,int height,String S){
                this.setTitle("修改学生信息");
                this.setBounds(x,y,width,height);
                btnOk = new JButton("确定");
                jTextField = new JTextField[16];
                for (int i =0;i<16;i++){
                    jTextField[i]=new JTextField(15);
                }
                this.setVisible(true);
                init(S);

            }
            public void init(String inputSno){
                JPanel jPanel = new JPanel(new GridLayout(9,1));
                String[] list = {"学生学号","学生姓名","学生性别","学生年龄","所属学院","所属班级","所属年级","入学时间"};
                int k =0;
                for(int i =0;i<8;i++){
                    for (int j =0;j<3;j++) {
                        JPanel sonPanel = new JPanel();
                        if(j==0){
                            sonPanel.add(new JLabel(list[i]));
                        }
                        else {
                            sonPanel.add(jTextField[k]);
                            k++;
                        }
                        jPanel.add(sonPanel);
                    }
                }
                JPanel btnPanel = new JPanel();
                btnPanel.add(btnOk);
                jPanel.add(btnPanel);
                this.add(jPanel);
                try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")){
                    String sqlShow = "SELECT * FROM STUDENT WHERE SNO = ?";
                    PreparedStatement stmt = conn.prepareStatement(sqlShow);
                    stmt.setString(1,inputSno);
                    ResultSet previous = stmt.executeQuery();
                    if (previous.next()){
                        jTextField[0].setText(previous.getString("SNO"));
                        jTextField[2].setText(previous.getString("SNAME"));
                        jTextField[4].setText(previous.getString("SSEX"));
                        jTextField[6].setText(previous.getString("SAGE"));
                        jTextField[8].setText(previous.getString("FACULTY"));
                        jTextField[10].setText(previous.getString("SCLASS"));
                        jTextField[12].setText(previous.getString("SYEAR"));
                        jTextField[14].setText(previous.getString("SENTER"));

                        jTextField[1].setText(previous.getString("SNO"));
                        jTextField[3].setText(previous.getString("SNAME"));
                        jTextField[5].setText(previous.getString("SSEX"));
                        jTextField[7].setText(previous.getString("SAGE"));
                        jTextField[9].setText(previous.getString("FACULTY"));
                        jTextField[11].setText(previous.getString("SCLASS"));
                        jTextField[13].setText(previous.getString("SYEAR"));
                        jTextField[15].setText(previous.getString("SENTER"));

                        for (int i=0;i<16;i+=2){
                           jTextField[i].setEditable(false);
                           jTextField[i].setEnabled(false);
                        }
                        jTextField[1].setEditable(false);  //学号不可修改
                        System.out.println("成功");
                    }
                    btnOk.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // 以下为输入检查
                            Calendar calendar = Calendar.getInstance();
                            int currentYear = calendar.get(Calendar.YEAR); //获取当前年份
                            String ssex = jTextField[5].getText().replaceAll(" ","");
                            if(!ssex.equals("男")&&!ssex.equals("女")){
                                JOptionPane.showMessageDialog(null,"输入的性别有误！\n性别只能为“男”或“女”");
                            }
                            else if(Integer.parseInt(jTextField[7].getText())<=10&&Integer.parseInt(jTextField[7].getText())>=100){
                                JOptionPane.showMessageDialog(null,"输入的年龄有误！");
                            }
                            else if(!jTextField[13].getText().equals("大一")&&!jTextField[13].getText().equals("大二")&&!jTextField[13].getText().equals("大三")&&!jTextField[13].getText().equals("大四")){
                                JOptionPane.showMessageDialog(null,"输入的年级有误！");
                            }
                            else if(Integer.parseInt(jTextField[15].getText())>currentYear||Integer.parseInt(jTextField[15].getText())<=2000){
                                JOptionPane.showMessageDialog(null,"输入的入学时间有误！");
                            }
                            else {
                                try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                                    String sqlUpdate = "UPDATE STUDENT SET SNAME= ?, SSEX = ?,SAGE = ?,FACULTY = ?,SCLASS = ?, SYEAR = ?, SENTER = ? WHERE SNO = ?";
                                    PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
                                    stmt.setString(1,jTextField[3].getText());
                                    stmt.setString(2,jTextField[5].getText());
                                    stmt.setString(3,jTextField[7].getText());
                                    stmt.setString(4,jTextField[9].getText());
                                    stmt.setString(5,jTextField[11].getText());
                                    stmt.setString(6,jTextField[13].getText());
                                    stmt.setString(7,jTextField[15].getText());
                                    stmt.setString(8,jTextField[0].getText());
                                    int rows = stmt.executeUpdate();
                                    conn.commit();
                                    System.out.println("修改成功！");
                                    JOptionPane.showMessageDialog(null,"修改成功！");
                                }
                                catch (SQLException ex){
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });

                }
                catch (SQLException ex){
                    ex.printStackTrace();
                }

            }
        }
    }

    public class TeacherManagement extends DemoManagement {
        public TeacherManagement(int x, int y, int witdh, int height) {
            super(x, y, witdh, height);
            init();
            this.setTitle("教师信息管理界面");
            this.setVisible(true);
        }

        public void init() {
            btnShow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                        String sql = "SELECT * FROM TMANAGE";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery(sql);
                        tableList.clear();
                        while (rs.next()) {
                            List<String> elements = new ArrayList<>();
                            elements.add(rs.getString("TNO"));
                            elements.add(rs.getString("TNAME"));
                            elements.add(rs.getString("TSEX"));
                            elements.add(rs.getString("TAGE"));
                            elements.add(rs.getString("TPOSITION"));
                            elements.add(rs.getString("TFACULTY"));
                            tableList.add(elements);
                        }
                        tableModel.setRowCount(0);
                        tableModel.setRowCount(tableList.size()+20);
                        String[] conlumnNames = {"教师编号", "名字", "性别", "年龄", "职称", "所属学院"};
                        tableModel.setColumnIdentifiers(conlumnNames);
                        tableModel = (DefaultTableModel) showTable.getModel();
                        for (int i = 0; i < tableList.size(); i++) {
                            for (int j = 0; j < 6; j++) {
                                tableModel.setValueAt(tableList.get(i).get(j), i, j);
                                //System.out.println(tableList.get(i).get(j));
                            }
                        }
                        showTable.setDefaultEditor(Object.class, null); //设置表格成绩表不能修改
                        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                        centerRenderer.setHorizontalAlignment(JLabel.CENTER);  // 设置显示居中
                        showTable.setDefaultRenderer(Object.class, centerRenderer);

                        //设置表格的列的宽度
                        TableColumnModel columnModel = showTable.getColumnModel();
                        for (int i = 0; i < showTable.getColumnCount(); i++) {
                            TableColumn column = columnModel.getColumn(i);
                            if (i == 0) {
                                column.setPreferredWidth(140);
                            } else if (i == 2 || i == 3) {
                                column.setPreferredWidth(50);
                            } else if (i == 1 || i == 4) {
                                column.setPreferredWidth(100);
                            } else if (i == 5) {
                                column.setPreferredWidth(200);
                            }
                        }
                        showTable.repaint();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try {
                        String S = doc.getText(0, doc.getLength());
                        if (S.length() == tableList.get(0).get(0).length()) {
                            boolean flag = false;
                            int row = 0;
                            tableModel.setRowCount(0);
                            for (int i = 0; i < tableList.size(); i++) {
                                System.out.println("tablelist.get:" + tableList.get(i).get(0));
                                if (S.equals(tableList.get(i).get(0))) {
                                    row++;
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                JOptionPane.showMessageDialog(null, "没有该账号！");
                            } else {
                                tableModel.setRowCount(row);
                                int num = 0;
                                for (int i = 0; i < tableList.size(); i++) {
                                    if (S.equals(tableList.get(i).get(0))) {
                                        for (int k = 0; k < tableList.get(i).size(); k++) {
                                            System.out.println(tableList.get(i).get(k));
                                            tableModel.setValueAt(tableList.get(i).get(k), num, k);
                                        }
                                        num++;
                                    }
                                }
                                showTable.setModel(tableModel);
                                showTable.repaint();
                                flag = true;
                            }
                        }
//                        if (S.length() > listAccount.get(0).get(0).length()) {
//                            JOptionPane.showMessageDialog(null, "输入账号错误！");
//                        }
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {

                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
            changeField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")){
                        String S = doc.getText(0, doc.getLength());
                        String sqlFind = "SELECT * FROM TEACHER WHERE TNO = ?";
                        PreparedStatement stmt = conn.prepareStatement(sqlFind);
                        stmt.setString(1,S);
                        ResultSet findSet = stmt.executeQuery();
                        if(findSet.next()){
                            Change changeStudent = new Change(400,300,600,300, S);
                        }
                    }
                    catch (SQLException ex){
                        ex.printStackTrace();
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<String> labels = new ArrayList<>();
                    labels.add("教师编号");
                    labels.add("教师姓名");
                    labels.add("教师年龄");
                    labels.add("教师职称");
                    labels.add("所属学院");
                    labels.add("教师性别");
                    Add addTeacher = new Add(400, 200, 500, 300, labels);
                }
            });
            btnDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Delete deletestudent = new Delete(400,200,350,100);
                }
            });
        }
        public class Add extends JFrame {
            JButton btnOk;
            List<String> list = new ArrayList<>();
            JTextField[] jTextField;
            JComboBox<String> comboBox = new JComboBox<>();

            public Add(int x, int y, int witdh, int height, List<String> jlbNum) {
                this.setTitle("增加教师");
                btnOk = new JButton("确定");
                comboBox.addItem("男");
                comboBox.addItem("女");
                JPanel jPanel = new JPanel();
                jTextField = new JTextField[jlbNum.size()];
                jPanel.setLayout(new GridLayout(6, 2));
                for (int i = 0; i < 6; i++) {
                    JPanel temp = new JPanel();
                    temp.add(new JLabel(jlbNum.get(i)));
                    jTextField[i] = new JTextField(15);
                    if (i != 5) {
                        temp.add(jTextField[i]);
                    } else {
                        temp.add(comboBox);
                    }
                    jPanel.add(temp);
                }
                JPanel btn = new JPanel();
                btn.add(btnOk);
                jPanel.add(btn);
                this.add(jPanel);
                this.setBounds(x, y, witdh, height);
                this.setVisible(true);

                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean flag = true;
                        list.clear();
                        for (int i = 0; i < 5; i++) {
                            if (jTextField[i].getText().equals("")) {
                                flag = false;
                            } else {
                                list.add(jTextField[i].getText());
                            }
                        }
                        if (!flag) {
                            JOptionPane.showMessageDialog(null, "部分信息未录入");
                        } else {
                            try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                                String judge = "SELECT * FROM TEACHER WHERE TNO = ?";
                                PreparedStatement stmt1 = conn.prepareStatement(judge);
                                stmt1.setString(1,list.get(2));
                                ResultSet rs1 = stmt1.executeQuery();
                                if(rs1.next()){
                                    JOptionPane.showMessageDialog(null,"已存在该名教师");
                                }
                                else {
                                    String sql = "INSERT INTO TEACHER(TNO,TNAME,TAGE,TPOSITION,TFACULTY,TSEX,PWD) VALUES(?, ?, ?, ?, ?, ?, 123456)";
                                    PreparedStatement stmt = conn.prepareStatement(sql);
                                    for (int i = 0; i < 6; i++) {
                                        if(i!=5) {
                                            stmt.setString(i + 1, list.get(i));
                                        }
                                        else {
                                            stmt.setString(i+1,comboBox.getSelectedItem().toString());
                                        }
                                    }
                                    int rows = stmt.executeUpdate();
                                    conn.commit();
                                    System.out.println("commit成功");
                                    String show = "账号"+list.get(0)+"添加成功";
                                    JOptionPane.showMessageDialog(null,show);
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        public class Delete extends JFrame{
            JButton btnOk;
            JTextField inputCode;
            public Delete(int x, int y, int witdh, int heigth){
                this.setBounds(x,y,witdh,heigth);
                this.setTitle("删除教师");
                btnOk = new JButton("确定");
                inputCode = new JTextField(15);
                this.setLayout(new FlowLayout());
                this.add(new JLabel("账号："));
                this.add(inputCode);
                this.add(btnOk);
                this.setVisible(true);
                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(inputCode.getText().equals("")){
                            JOptionPane.showMessageDialog(null,"输入为空！");
                        }
                        try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")){
                            String code = inputCode.getText();
                            String sqlFind = "SELECT * FROM TEACHER WHERE TNO = ?";
                            PreparedStatement stmt = conn.prepareStatement(sqlFind);
                            stmt.setString(1,code);
                            ResultSet findSet = stmt.executeQuery();
                            if(!findSet.next()){
                                JOptionPane.showMessageDialog(null,"不存在该教师！");
                            }
                            else{
                                String sqlDelete = "DELETE FROM TEACHER WHERE TNO = ?";
                                stmt = conn.prepareStatement(sqlDelete);
                                stmt.setString(1,code);
                                int rows = stmt.executeUpdate();
                                conn.commit();
                                System.out.println("commit");
                                String show = "账号"+code+"数据已删除！！！";
                                JOptionPane.showMessageDialog(null,show);
                            }
                        }
                        catch (SQLException ex){
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }
        public class Change extends JFrame{
            JButton btnOk;
            JTextField[] jTextField;
            public Change(int x, int y, int width,int height,String S){
                this.setTitle("修改教师信息");
                this.setBounds(x,y,width,height);
                btnOk = new JButton("确定");
                jTextField = new JTextField[12];
                for (int i =0;i<12;i++){
                    jTextField[i]=new JTextField(15);
                }
                this.setVisible(true);
                init(S);

            }
            public void init(String inputSno){
                JPanel jPanel = new JPanel(new GridLayout(7,1));
                String[] list = {"教师编号","教师姓名","教师性别","教师年龄","教师职称","所属学院"};
                int k =0;
                for(int i =0;i<6;i++){
                    for (int j =0;j<3;j++) {
                        JPanel sonPanel = new JPanel();
                        if(j==0){
                            sonPanel.add(new JLabel(list[i]));
                        }
                        else {
                            sonPanel.add(jTextField[k]);
                            k++;
                        }
                        jPanel.add(sonPanel);
                    }
                }
                JPanel btnPanel = new JPanel();
                btnPanel.add(btnOk);
                jPanel.add(btnPanel);
                this.add(jPanel);
                try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")){
                    String sqlShow = "SELECT * FROM TEACHER WHERE TNO = ?";
                    PreparedStatement stmt = conn.prepareStatement(sqlShow);
                    stmt.setString(1,inputSno);
                    ResultSet previous = stmt.executeQuery();
                    if (previous.next()){
                        jTextField[0].setText(previous.getString("TNO"));
                        jTextField[2].setText(previous.getString("TNAME"));
                        jTextField[4].setText(previous.getString("TSEX"));
                        jTextField[6].setText(previous.getString("TAGE"));
                        jTextField[8].setText(previous.getString("TPOSITION"));
                        jTextField[10].setText(previous.getString("TFACULTY"));

                        jTextField[1].setText(previous.getString("TNO"));
                        jTextField[3].setText(previous.getString("TNAME"));
                        jTextField[5].setText(previous.getString("TSEX"));
                        jTextField[7].setText(previous.getString("TAGE"));
                        jTextField[9].setText(previous.getString("TPOSITION"));
                        jTextField[11].setText(previous.getString("TFACULTY"));
                        for (int i=0;i<12;i+=2){
                            jTextField[i].setEditable(false);
                            jTextField[i].setEnabled(false);
                        }
                        jTextField[1].setEditable(false);  //学号不可修改
                        System.out.println("成功");
                    }
                    btnOk.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("成功222 "); // 以下为输入检查
                            Calendar calendar = Calendar.getInstance();
                            int currentYear = calendar.get(Calendar.YEAR);
                            String ssex = jTextField[5].getText().replaceAll(" ","");
                            if(!ssex.equals("男")&&!ssex.equals("女")){
                                JOptionPane.showMessageDialog(null,"输入的性别有误！\n性别只能为“男”或“女”");
                            }
                            else if(Integer.parseInt(jTextField[7].getText())<=10&&Integer.parseInt(jTextField[7].getText())>=100){
                                JOptionPane.showMessageDialog(null,"输入的年龄有误！");
                            }
                            else if(!jTextField[9].getText().equals("副教授")&&!jTextField[13].getText().equals("教授")&&!jTextField[13].getText().equals("讲师")){
                                JOptionPane.showMessageDialog(null,"输入的职称有误！");
                            }
                            else {
                                try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                                    String sqlUpdate = "UPDATE TEACHER SET TNAME= ?, TSEX = ? ,TAGE = ?, TPOSITION = ? ,TFACULTY = ? WHERE TNO = ?";
                                    PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
                                    stmt.setString(1,jTextField[3].getText());
                                    //stmt.setString(1,jTextField[3].getText());
                                    stmt.setString(2,jTextField[5].getText());
                                    stmt.setString(3,jTextField[7].getText());
                                    stmt.setString(4,jTextField[9].getText());
                                    stmt.setString(5,jTextField[11].getText());
                                    stmt.setString(6,jTextField[0].getText());

                                    int rows = stmt.executeUpdate();
                                    conn.commit();
                                    System.out.println("修改成功！");
                                    JOptionPane.showMessageDialog(null,"修改成功！");
                                }
                                catch (SQLException ex){
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });

                }
                catch (SQLException ex){
                    ex.printStackTrace();
                }

            }
        }

    }

    public class CourseManagement extends DemoManagement {
        public CourseManagement(int x, int y, int witdh, int height) {
            super(x, y, witdh, height);
            this.setTitle("课程信息管理界面");
            init();
            this.setVisible(true);
        }

        public void init() {
            btnShow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                        String sql = "SELECT * FROM COURSEINFO";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery(sql);
                        tableList.clear();
                        while (rs.next()) {
                            List<String> elements = new ArrayList<>();
                            elements.add(rs.getString("CNO"));
                            elements.add(rs.getString("CNAME"));
                            elements.add(rs.getString("TNO"));
                            elements.add(rs.getString("TNAME"));
                            elements.add(rs.getString("SELECT_COUNT"));
                            elements.add(rs.getString("CCREDIT"));
                            if (rs.getString("CSCORE").equals("0")) {
                                elements.add("百分制");
                            } else {
                                elements.add("等级制");
                            }
                            tableList.add(elements);
                        }
                        tableModel.setRowCount(0);
                        tableModel.setRowCount(tableList.size()+20);
                        String[] conlumnNames = {"课程编号", "课程名称", "教师编号", "教师名字", "选课人数", "学分", "课程分制"};
                        tableModel.setColumnIdentifiers(conlumnNames);
                        tableModel = (DefaultTableModel) showTable.getModel();
                        for (int i = 0; i < tableList.size(); i++) {
                            for (int j = 0; j < 7; j++) {
                                tableModel.setValueAt(tableList.get(i).get(j), i, j);
                            }
                        }

                        showTable.setDefaultEditor(Object.class, null); //设置表格成绩表不能修改
                        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                        centerRenderer.setHorizontalAlignment(JLabel.CENTER);  // 设置显示居中
                        showTable.setDefaultRenderer(Object.class, centerRenderer);
                        showTable.repaint();
                        showTable.repaint();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try {
                        String S = doc.getText(0, doc.getLength());
                        if (S.length() == tableList.get(0).get(0).length()) {
                            boolean flag = false;
                            int row = 0;
                            tableModel.setRowCount(0);
                            for (int i = 0; i < tableList.size(); i++) {
                                System.out.println("tablelist.get:" + tableList.get(i).get(0));
                                if (S.equals(tableList.get(i).get(0))) {
                                    row++;
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                JOptionPane.showMessageDialog(null, "没有该账号！");
                            } else {
                                tableModel.setRowCount(row);
                                int num = 0;
                                for (int i = 0; i < tableList.size(); i++) {
                                    if (S.equals(tableList.get(i).get(0))) {
                                        for (int k = 0; k < tableList.get(i).size(); k++) {
                                            System.out.println(tableList.get(i).get(k));
                                            tableModel.setValueAt(tableList.get(i).get(k), num, k);
                                        }
                                        num++;
                                    }
                                }
                                showTable.setModel(tableModel);

                                showTable.setDefaultEditor(Object.class, null); //设置表格成绩表不能修改
                                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                                centerRenderer.setHorizontalAlignment(JLabel.CENTER);  // 设置显示居中
                                showTable.setDefaultRenderer(Object.class, centerRenderer);
                                showTable.repaint();
                            }
                        }
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {

                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
            changeField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")){
                        String S = doc.getText(0, doc.getLength());
                        String sqlFind = "SELECT * FROM COURSE WHERE CNO = ?";
                        PreparedStatement stmt = conn.prepareStatement(sqlFind);
                        stmt.setString(1,S);
                        ResultSet findSet = stmt.executeQuery();
                        if(findSet.next()){
                            Change changeStudent = new Change(400,300,600,300, S);
                        }
                    }
                    catch (SQLException ex){
                        ex.printStackTrace();
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<String> labels = new ArrayList<>();
                    labels.add("课程编号");
                    labels.add("课程名称");
                    labels.add("教师编号");
                    labels.add("教师名字");
                    labels.add("课程学分");
                    labels.add("课程分制");
                    Add addcourse = new Add(400, 200, 500, 300, labels);
                }
            });
            btnDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Delete deletestudent = new Delete(400,200,350,100);
                }
            });
        }

        public class Add extends JFrame {
            JButton btnOk;
            List<String> list = new ArrayList<>();
            JTextField[] jTextField;
            JComboBox<String> comboBox = new JComboBox<>();

            public Add(int x, int y, int witdh, int height, List<String> jlbNum) {
                this.setTitle("增加课程");
                btnOk = new JButton("确定");
                comboBox.addItem("百分制");
                comboBox.addItem("五分制");
                JPanel jPanel = new JPanel();
                jTextField = new JTextField[jlbNum.size()];
                jPanel.setLayout(new GridLayout(6, 2));
                for (int i = 0; i < 6; i++) {
                    JPanel temp = new JPanel();
                    temp.add(new JLabel(jlbNum.get(i)));
                    jTextField[i] = new JTextField(15);
                    if (i != 5) {
                        temp.add(jTextField[i]);
                    } else {
                        temp.add(comboBox);
                    }
                    jPanel.add(temp);
                }
                JPanel btn = new JPanel();
                btn.add(btnOk);
                jPanel.add(btn);
                this.add(jPanel);
                this.setBounds(x, y, witdh, height);
                this.setVisible(true);

                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean flag = true;
                        list.clear();
                        for (int i = 0; i < 5; i++) {
                            if (jTextField[i].getText().equals("")) {
                                flag = false;
                            } else {
                                list.add(jTextField[i].getText());
                            }
                        }
                        if (!flag) {
                            JOptionPane.showMessageDialog(null, "部分信息未录入");
                        } else {
                            try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                                String sql1 = "SELECT * FROM TEACHER WHERE TNO = ? AND TNAME = ?";
                                String sql2 = "SELECT * FROM TEACHER RIGHT OUTER JOIN COURSE ON TEACHER.TNO = COURSE.TNO WHERE TEACHER.TNO = ? AND COURSE.CNO = ?";
                                PreparedStatement stmt1 = conn.prepareStatement(sql1);
                                PreparedStatement stmt2 = conn.prepareStatement(sql2);
                                stmt1.setString(1,list.get(2));
                                stmt1.setString(2,list.get(3));
                                stmt2.setString(1,list.get(2));
                                stmt2.setString(2,list.get(0));
                                ResultSet rs1 = stmt1.executeQuery();
                                ResultSet rs2 = stmt2.executeQuery();
                                if(!rs1.next()){
                                   JOptionPane.showMessageDialog(null,"不存在该名教师");
                                }
                                else if(rs2.next()){
                                    JOptionPane.showMessageDialog(null,"已存在该课程");
                                }
                                else {
                                    String sql = "INSERT INTO COURSE(CNO,CNAME,TNO,TNAME,CCREDIT,CSCORE) VALUES(?, ?, ?, ?, ?, ?)";
                                    PreparedStatement stmt = conn.prepareStatement(sql);
                                    for (int i = 0; i < 6; i++) {
                                        if (i != 5) {
                                            stmt.setString(i + 1, list.get(i));
                                        } else {
                                            if (comboBox.getSelectedItem().equals("百分制")) {
                                                stmt.setString(i + 1, "0");
                                            } else {
                                                stmt.setString(i + 1, "1");
                                            }
                                        }
                                    }
                                    System.out.println(stmt);
                                    int rows = stmt.executeUpdate();
                                    conn.commit();
                                    System.out.println("commit成功");
                                    String show = "课程"+list.get(0)+"添加成功";
                                    JOptionPane.showMessageDialog(null,show);
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        public class Delete extends JFrame{
            JButton btnOk;
            JTextField inputCode;
            public Delete(int x, int y, int witdh, int heigth){
                this.setBounds(x,y,witdh,heigth);
                this.setTitle("删除课程");
                btnOk = new JButton("确定");
                inputCode = new JTextField(15);
                this.setLayout(new FlowLayout());
                this.add(new JLabel("账号："));
                this.add(inputCode);
                this.add(btnOk);
                this.setVisible(true);
                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(inputCode.getText().equals("")){
                            JOptionPane.showMessageDialog(null,"输入为空！");
                        }
                        try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")){
                            String code = inputCode.getText();
                            String sqlFind = "SELECT * FROM COURSE WHERE CNO = ?";
                            PreparedStatement stmt = conn.prepareStatement(sqlFind);
                            stmt.setString(1,code);
                            ResultSet findSet = stmt.executeQuery();
                            if(!findSet.next()){
                                JOptionPane.showMessageDialog(null,"不存在该课程！");
                            }
                            else{
                                String sqlDelete = "DELETE FROM COURSE WHERE CNO = ?";
                                stmt = conn.prepareStatement(sqlDelete);
                                stmt.setString(1,code);
                                int rows = stmt.executeUpdate();
                                conn.commit();
                                System.out.println("commit");
                                String show = "课程"+code+"数据已删除！！！";
                                JOptionPane.showMessageDialog(null,show);
                            }
                        }
                        catch (SQLException ex){
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }
        public class Change extends JFrame{
            JButton btnOk;
            JTextField[] jTextField;
            public Change(int x, int y, int width,int height,String S){
                this.setTitle("修改课程息");
                this.setBounds(x,y,width,height);
                btnOk = new JButton("确定");
                jTextField = new JTextField[12];
                for (int i =0;i<12;i++){
                    jTextField[i]=new JTextField(15);
                }
                this.setVisible(true);
                init(S);

            }
            public void init(String inputSno){
                JPanel jPanel = new JPanel(new GridLayout(7,1));
                String[] list = {"课程编号","课程姓名","课程学分","任课老师","课程分制","教师名称"};
                int k =0;
                for(int i =0;i<6;i++){
                    for (int j =0;j<3;j++) {
                        JPanel sonPanel = new JPanel();
                        if(j==0){
                            sonPanel.add(new JLabel(list[i]));
                        }
                        else {
                            sonPanel.add(jTextField[k]);
                            k++;
                        }
                        jPanel.add(sonPanel);
                    }
                }
                JPanel btnPanel = new JPanel();
                btnPanel.add(btnOk);
                jPanel.add(btnPanel);
                this.add(jPanel);
                try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")){

                    String sqlFind = "SELECT * FROM COURSE WHERE CNO = ?";
                    PreparedStatement stmt = conn.prepareStatement(sqlFind);
                    stmt.setString(1,inputSno);
                    String name="选择该科目的不同任课老师：\n";
                    String tno ="";
                    ResultSet findSet = stmt.executeQuery();
                    while (findSet.next()) {
                        name += " 编号：";
                        name += findSet.getString("TNO");
                        name += " 名字 ";
                        name += findSet.getString("TNAME");
                        name += "\n";
                    }
                    name += "输入教师编号选择修改";
                    tno = JOptionPane.showInputDialog(name);

                    String sqlShow = "SELECT * FROM COURSE WHERE CNO = ? AND TNO = ?";
                    stmt = conn.prepareStatement(sqlShow);
                    stmt.setString(1,inputSno);
                    stmt.setString(2,tno);
                    ResultSet previous = stmt.executeQuery();
                    if (previous.next()){
                        jTextField[0].setText(previous.getString("CNO"));
                        jTextField[2].setText(previous.getString("CNAME"));
                        jTextField[4].setText(previous.getString("CCREDIT"));
                        jTextField[6].setText(previous.getString("TNO"));
                        jTextField[8].setText(previous.getString("CSCORE"));
                        jTextField[10].setText(previous.getString("TNAME"));

                        jTextField[1].setText(previous.getString("CNO"));
                        jTextField[3].setText(previous.getString("CNAME"));
                        jTextField[5].setText(previous.getString("CCREDIT"));
                        jTextField[7].setText(previous.getString("TNO"));
                        jTextField[9].setText(previous.getString("CSCORE"));
                        jTextField[11].setText(previous.getString("TNAME"));

                        for (int i=0;i<12;i+=2){
                            jTextField[i].setEditable(false);
                            jTextField[i].setEnabled(false);
                        }
                        jTextField[1].setEditable(false);  //课程编号不可修改
                        jTextField[9].setEditable(false);  //分制不可修改
                        jTextField[7].setEditable(false);  //教师不可修改
                        System.out.println("成功");
                    }
                    btnOk.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("成功222 "); // 以下为输入检查

                            if(Integer.parseInt(jTextField[5].getText())>6||Integer.parseInt(jTextField[5].getText())<0.5){
                                JOptionPane.showMessageDialog(null,"输入的学分有误！\n学分取值为 0.5~6");
                            }
                            else {
                                try(Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                                    String sqlUpdate = "UPDATE COURSE SET CNAME = ? ,CCREDIT = ?, TNAME = ? WHERE CNO = ? AND TNO = ?";
                                    PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
                                    stmt.setString(1,jTextField[3].getText());
                                    stmt.setString(2,jTextField[5].getText());
                                    stmt.setString(3,jTextField[11].getText());
                                    stmt.setString(4,jTextField[0].getText());
                                    stmt.setString(5,jTextField[7].getText());

                                    int rows = stmt.executeUpdate();
                                    conn.commit();
                                    System.out.println("修改成功！");
                                    JOptionPane.showMessageDialog(null,"修改成功！");
                                }
                                catch (SQLException ex){
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });

                }
                catch (SQLException ex){
                    ex.printStackTrace();
                }

            }
        }
    }

    public class AccountManagement extends JFrame {
        JButton btnAccountShow;
        JTextField jtfAccountSearch;
        JButton btnAccountAdd;
        JTextField jtfAccountReset;
        JTable AccountTable;
        DefaultTableModel tableModel;

        List<List<String>> listAccount;

        public AccountManagement(int x, int y, int width, int hegiht) {
            super("账号管理界面");
            this.setBounds(x, y, width, hegiht);
            init();
            this.setVisible(true);
        }

        public void init() {
            btnAccountShow = new JButton("查看");
            jtfAccountSearch = new JTextField();
            jtfAccountReset = new JTextField();
            btnAccountAdd = new JButton("添加账号");
            AccountTable = new JTable(40, 3);
            listAccount = new ArrayList<>();
            JPanel jPanel = new JPanel();
            JPanel north = new JPanel();
            JPanel sourth = new JPanel();

            BoxLayout boxLayout = new BoxLayout(north, BoxLayout.X_AXIS);
            north.setLayout(boxLayout);
            north.add(btnAccountShow);
            north.add(new JLabel("搜索:"));
            north.add(jtfAccountSearch);
            north.add(new JLabel("重置密码:"));
            north.add(jtfAccountReset);
            sourth.add(addJTable(AccountTable));

            jPanel.setLayout(new BorderLayout());
            jPanel.add(north, BorderLayout.NORTH);
            jPanel.add(sourth, BorderLayout.CENTER);
            this.add(jPanel);
            this.setVisible(true);

            btnAccountShow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] conlumnNames = {"账号", "密码", "类型"};
                    tableModel = (DefaultTableModel) AccountTable.getModel();
                    tableModel.setColumnIdentifiers(conlumnNames);
                    tableModel.setRowCount(0);
                    try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                        String sql = "SELECT * FROM ACCOUNTMANAGE";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery();
                        listAccount.clear();
                        while (rs.next()) {
                            List<String> elements = new ArrayList<>();
                            elements.add(rs.getString("ANO"));
                            elements.add(rs.getString("PWD"));
                            if (rs.getString("ANO").charAt(0) == 't') {
                                elements.add("教师");
                            } else {
                                elements.add("学生");
                            }
                            listAccount.add(elements);
                        }
                        System.out.println(listAccount);
                        tableModel.setRowCount(listAccount.size());
                        for (int i = 0; i < listAccount.size(); i++) {
                            for (int j = 0; j < 3; j++) {
                                tableModel.setValueAt(listAccount.get(i).get(j), i, j);
                            }
                        }
                        AccountTable.setModel(tableModel);
                        AccountTable.setDefaultEditor(Object.class, null); //设置表格成绩表不能修改
                        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                        centerRenderer.setHorizontalAlignment(JLabel.CENTER);  // 设置显示居中
                        AccountTable.setDefaultRenderer(Object.class, centerRenderer);
                        AccountTable.repaint();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }


                }
            });
            btnAccountAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addAccount acc = new addAccount(200, 200, 400, 200);
                }
            });
            jtfAccountSearch.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                        String S = doc.getText(0, doc.getLength());
//                        if (S.length() == listAccount.get(0).get(0).length()) {
//                            boolean flag = false;
//                            int row = 0;
//                            tableModel.setRowCount(0);
//                            for (int i = 0; i < listAccount.size(); i++) {
//                                System.out.println("listAccount.get:" + listAccount.get(i).get(0));
//                                if (S.equals(listAccount.get(i).get(0))) {
//                                    row++;
//                                    flag = true;
//                                }
//                            }
//                            if (!flag) {
//                                JOptionPane.showMessageDialog(null, "没有该账号！");
//                            } else {
//                                tableModel.setRowCount(row);
//                                int num = 0;
//                                for (int i = 0; i < listAccount.size(); i++) {
//                                    if (S.equals(listAccount.get(i).get(0))) {
//                                        for (int k = 0; k < listAccount.get(i).size(); k++) {
//                                            System.out.println(listAccount.get(i).get(k));
//                                            tableModel.setValueAt(listAccount.get(i).get(k), num, k);
//                                        }
//                                        num++;
//                                    }
//                                }
//                                AccountTable.setModel(tableModel);
//                                AccountTable.setDefaultEditor(Object.class, null); //设置表格成绩表不能修改
//                                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//                                centerRenderer.setHorizontalAlignment(JLabel.CENTER);  // 设置显示居中
//                                AccountTable.setDefaultRenderer(Object.class, centerRenderer);
//                                AccountTable.repaint();
//                                flag = true;
//                            }
//                        }
                        String sql = "SELECT * FROM ACCOUNTMANAGE WHERE ANO = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, S);
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            List<String> elements = new ArrayList<>();
                            elements.add(rs.getString("ANO"));
                            elements.add(rs.getString("PWD"));
                            if (rs.getString("ANO").charAt(0) == 't') {
                                elements.add("教师");
                            } else {
                                elements.add("学生");
                            }
                            tableModel.setRowCount(1);
                            for (int i = 0; i < elements.size(); i++) {
                                tableModel.setValueAt(elements.get(i), 0, i);
                            }
                            AccountTable.setModel(tableModel);
                            AccountTable.setDefaultEditor(Object.class, null); //设置表格成绩表不能修改
                            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                            centerRenderer.setHorizontalAlignment(JLabel.CENTER);  // 设置显示居中
                            AccountTable.setDefaultRenderer(Object.class, centerRenderer);
                            AccountTable.repaint();
                        }
//                        else {
//                            JOptionPane.showMessageDialog(null,"没有该账号！");
//                        }
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    } catch (SQLException ex1) {
                        ex1.printStackTrace();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {

                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
            jtfAccountReset.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    Document doc = e.getDocument();
                    try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                        String S = doc.getText(0, doc.getLength());
                        String sql = "SELECT * FROM ACCOUNTMANAGE WHERE ANO = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, S);
                        ResultSet rs = stmt.executeQuery();
                        List<String> set = new ArrayList<>();
                        if (rs.next()) {
                            set.add(rs.getString("ANO"));
                            set.add(rs.getString("PWD"));
                            reSetPwd reSetPwd = new reSetPwd(200, 100, 400, 250, set.get(0), set.get(1));
                        }
                    } catch (BadLocationException ex1) {
                        throw new RuntimeException(ex1);
                    } catch (SQLException ex2) {
                        ex2.printStackTrace();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {

                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
        }

        public JScrollPane addJTable(JTable j) {
            JScrollPane scrollPane = new JScrollPane(j);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            return scrollPane;
        }

        public class addAccount extends JFrame {
            public JRadioButton isTeacher = new JRadioButton("教师");
            public JRadioButton isStudent = new JRadioButton("学生");
            public JTextField account;
            public JTextField password;
            public JTextField name;
            public JButton btnOk;

            public addAccount(int x, int y, int width, int height) {
                super("添加账号");
                this.setBounds(x, y, width, height);
                init();
                this.setVisible(true);
            }

            public void init() {
                account = new JTextField();
                password = new JTextField();
                name = new JTextField();
                btnOk = new JButton("确定");

                ButtonGroup buttonGroup = new ButtonGroup();
                buttonGroup.add(isStudent);
                buttonGroup.add(isTeacher);

                JPanel kindPanel = new JPanel();
                kindPanel.add(new JLabel("选择账号类型"));
                kindPanel.add(isStudent);
                kindPanel.add(isTeacher);

                JPanel jPanel = new JPanel();
                BoxLayout boxLayout = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
                jPanel.setLayout(boxLayout);


                jPanel.add(kindPanel);
                jPanel.add(new JLabel("输入账号名："));
                jPanel.add(account);
                jPanel.add(new JLabel("输入姓名："));
                jPanel.add(name);
                jPanel.add(new JLabel("输入密码（默认123456）："));
                jPanel.add(password);
                jPanel.add(Box.createHorizontalGlue());
                jPanel.add(btnOk);
                this.add(jPanel);
                this.setVisible(true);

                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                            if (!isStudent.isSelected() && !isTeacher.isSelected() || account.getText().equals("") || name.getText().equals("")) {
                                JOptionPane.showMessageDialog(null, "信息不完整\n请重试！");
                            } else {
                                String setAccount = account.getText();
                                String setPwd = password.getText();
                                String setName = name.getText();
                                String sql;
                                if (isTeacher.isSelected()) {
                                    sql = "INSERT INTO TEACHER (TNO, TNAME, PWD) VALUES(?, ?, ?)";
                                } else {
                                    sql = "INSET INTO STUDENT (SNO, SNAME, PWD) VALUES(?, ?, ?)";
                                }
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, setAccount);
                                stmt.setString(2, setName);
                                if (setPwd.equals("")) {
                                    stmt.setString(3, "123456");
                                } else {
                                    stmt.setString(3, setPwd);
                                }
                                stmt.executeQuery();
                                System.out.println("插入数据成功！");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    }
                });
            }
        }

        public class reSetPwd extends JFrame {
            JTextField showOldPwd;
            JTextField setNewPwd;
            JButton btnOk;
            String newPwd = "";

            public reSetPwd(int x, int y, int width, int height, String Ano, String oldPwd) {
                showOldPwd = new JTextField(15);
                setNewPwd = new JTextField(15);
                btnOk = new JButton("确认");
                this.setBounds(x, y, width, height);
                init(Ano, oldPwd);
                this.setTitle("修改密码");
                this.setVisible(true);
            }

            public void init(String Ano, String oldPwd) {
                this.setLayout(new GridLayout(3, 1));
                JPanel north = new JPanel();
                JPanel center = new JPanel();
                JPanel south = new JPanel();
                north.add(new JLabel("原密码："));
                showOldPwd.setText(oldPwd);
                showOldPwd.setEditable(false);
                north.add(showOldPwd);
                center.add(new JLabel("新密码："));
                center.add(setNewPwd);
                south.add(btnOk);
                this.add(north);
                this.add(center);
                this.add(south);

                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                            if (!showOldPwd.getText().equals(setNewPwd.getText())) {
                                newPwd = setNewPwd.getText();
                                String sql = "";
                                if (!(Ano.charAt(0) == 't')) {
                                    sql = "UPDATE STUDENT SET PWD = ? WHERE SNO = ?";
                                } else {
                                    sql = "UPDATE TEACHER SET PWD = ? WHERE TNO = ?";
                                }
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, newPwd);
                                stmt.setString(2, Ano);
                                stmt.executeUpdate();
                                conn.commit();
                                String message = "账号" + Ano + "密码修改为：\n" + newPwd;
                                JOptionPane.showMessageDialog(null, message);
                            } else {
                                JOptionPane.showMessageDialog(null, "新旧密码重复！");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

        }
    }

}
