import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.List;
import java.util.*;

public class TeacherDemo extends JFrame {
    private JButton btnbasicInfo;
    private JButton btnStuGrade;
    private JButton btnPwd;

    private JButton btnSelectCourse;
    private JButton btnSearch;
    private JButton btnSort;
    private JButton btnChange;
    private JButton btnAddGrade;
    private JButton btnCalculate;

    private JTextField addGrade;

    private JComboBox<String> comboBoxCourse;
    private int index = 0;
    private JComboBox<String> comboBoxSort;

    private JPanel panel;
    private JTable table;
    private DefaultTableModel tableModel;
    private static List<List<String>> information;

    public TeacherDemo(int x, int y, int witdh, int height) {
        super("教师端口");
        this.setBounds(x, y, witdh, height);
        init();
        this.setVisible(true);
        this.setLocationByPlatform(true);
    }

    public void init() {
        btnbasicInfo = new JButton("基本信息");
        btnStuGrade = new JButton("管理学生成绩");
        btnPwd = new JButton("修改密码");

        btnSelectCourse = new JButton("选择科目");
        btnSearch = new JButton("查找学生");
        btnSort = new JButton("排序");
        btnChange = new JButton("修改成绩");

        btnCalculate = new JButton("统计数据");
        comboBoxCourse = new JComboBox<>();
        comboBoxSort = new JComboBox<>();
        addGrade = new JTextField();

        JPanel buttonPanel_1 = new JPanel();
        JPanel buttonPanel_2 = new JPanel();

        buttonPanel_1.add(btnbasicInfo);
        buttonPanel_1.add(btnStuGrade);
        buttonPanel_1.add(btnPwd);
        buttonPanel_1.setVisible(true);

        BoxLayout boxLayout = new BoxLayout(buttonPanel_2, BoxLayout.Y_AXIS);
        buttonPanel_2.setLayout(boxLayout);
        buttonPanel_2.add(new JLabel("选择科目："));
        buttonPanel_2.add(comboBoxCourse);
        buttonPanel_2.add(new JLabel("输入学号查找学生："));
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("宋体", Font.PLAIN, 15));
        searchField.setSize(10, 5);
        buttonPanel_2.add(searchField);
        buttonPanel_2.add(new JLabel("排序"));
        comboBoxSort.addItem("按成绩升序排序");
        comboBoxSort.addItem("按成绩降序排序");
        comboBoxSort.addItem("按学号升序排序");
        comboBoxSort.addItem("按学号降序排序");
        buttonPanel_2.add(comboBoxSort);
//        buttonPanel_2.add(new JLabel("输入学号修改成绩"));
//        JTextField changeField = new JTextField();
//        buttonPanel_2.add(changeField);
        buttonPanel_2.add(new JLabel("输入学号添加或修改成绩"));
        buttonPanel_2.add(addGrade);
        buttonPanel_2.add(btnCalculate);

        table = new JTable(40, 8);
        table.setVisible(true);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel sonPanel = new JPanel(gbl);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 2;
        gbc.gridx = 0;
        gbc.ipadx = 50;
        sonPanel.add(buttonPanel_2, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 60;
        gbc.gridx = 1;
        gbc.ipadx = 50;
        sonPanel.add(addJTable(table), gbc);

        panel = new JPanel();
        BoxLayout sonboxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(sonboxLayout);
        panel.add(buttonPanel_1);
        panel.add(sonPanel);
        this.add(panel);
        this.setVisible(true);

        btnbasicInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                    String sql = "SELECT * FROM TBASICINFO WHERE TNO = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, LoginDemo.getUsercode());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        String tno = rs.getString("TNO");
                        String tname = rs.getString("TNAME");
                        String tsex = rs.getString("TSEX");
                        String tposition = rs.getString("TPOSITION");
                        List<String> list = new ArrayList<>();
                        list.add(tno);
                        list.add(tname);
                        list.add(tsex);
                        list.add(tposition);
                        TbasicInfo tbasicInfo = new TbasicInfo(list);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnStuGrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] conlumnNames = {"课程编号", "课程名称", "学生学号", "学生姓名", "学生班别", "成绩", "是否及格"};
                tableModel = (DefaultTableModel) table.getModel();
                tableModel.setColumnIdentifiers(conlumnNames);
                table.repaint();
                List<List<String>> listSet = new ArrayList<>();
                List<String> list;
                Set<String> courseSet = new HashSet<>();
                comboBoxCourse.removeAllItems();
                int cal = 0; //统计一名教师教授的学科数目
                try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                    String sql = "SELECT * FROM TGRADEINFO WHERE TNO = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, LoginDemo.getUsercode());
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        list = new ArrayList<>();
                        String cno = rs.getString("CNO");
                        String cname = rs.getString("CNAME");
                        String sno = rs.getString("SNO");
                        String sname = rs.getString("SNAME");
                        String sclass = rs.getString("SCLASS");
                        String grade = rs.getString("GRADE");
                        String cmp = grade;
                        String pass;
                        if(grade.equals("优")||grade.equals("良")||grade.equals("中")||grade.equals("及格")){
                            pass = "是";
                        }
                        else if(grade.equals("不合格")){
                            pass = "否";
                        }
                        else if(Integer.parseInt(grade)>=60){
                            pass = "是";
                        }
                        else {
                            pass = "否";
                        }
                        //String pass = rs.getString("PASS");
                        list.add(cno);
                        list.add(cname);
                        list.add(sno);
                        list.add(sname);
                        list.add(sclass);
                        list.add(grade);
                        list.add(pass);
                        listSet.add(list);
                        courseSet.add(cname);
                        cal++;
                    }
                    comboBoxCourse.addItem("全部");
                    for (String S : courseSet) {
                        comboBoxCourse.addItem(S);
                    }
                    int i = 0, j = 0;
                    for (List<String> list1 : listSet) {
                        tableModel.setRowCount(rs.getRow());
                        for (String S : list1) {
                            tableModel.setValueAt(S, i, j);
                            j++;
                        }
                        i++;
                        j = 0;
                    }
                    table.repaint();

                    information = new ArrayList<>();
                    for (List<String> element : listSet) {
                        List<String> cloneElement = new ArrayList<>(element);
                        information.add(cloneElement);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        btnPwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TchangePwd tChangePwd = new TchangePwd(600, 400, 400, 250);
            }
        });
        btnSelectCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                    String sql = "SELECT CNAME FROM TSTUGRADEINFO WHERE TNO = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, LoginDemo.getUsercode());
                    ResultSet rs = stmt.executeQuery();
                    List<String> courseName = new ArrayList<>();
                    while (rs.next()) {
                        String temp = rs.getString("CNAME");
                        courseName.add(temp);
                    }

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
                    if (S.length() == information.get(0).get(2).length()) {
                        boolean flag = false;
                        int row = 0;
                        tableModel.setRowCount(0);
                        for (int i = 0; i < information.size(); i++) {
                            if (S.equals(information.get(i).get(2))) {
//                                tableModel.setRowCount(++row);
//                                for (int k = 0; k < information.get(i).size(); k++) {
//                                    tableModel.setValueAt(information.get(i).get(k), row, k);
//                                }
//                                table.setModel(tableModel);
//                                table.repaint();
//                                flag = true;
                                row++;
                                flag = true;
                            }
                        }
                        if (!flag) {
                            JOptionPane.showMessageDialog(null, "没有该学号的学生！");
                        } else {
                            tableModel.setRowCount(row);
                            int num = 0;
                            for (int i = 0; i < information.size(); i++) {
                                if (S.equals(information.get(i).get(2))) {
                                    for (int k = 0; k < information.get(i).size(); k++) {
                                        System.out.println(information.get(i).get(k));
                                        tableModel.setValueAt(information.get(i).get(k), num, k);
                                    }
                                    num++;
                                }
                            }
                            table.setModel(tableModel);
                            table.repaint();
                            flag = true;
                        }
                    }
                    if (S.length() > information.get(0).get(2).length()) {
                        JOptionPane.showMessageDialog(null, "输入学号位数错误！");
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
        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                    String sql = "SELECT DISTINCT CNO, CNAME, SCLASS, HIGHEST, LOWEST, AVERAGE, NUM FROM TGRADEINFO WHERE TNO = ? ";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, LoginDemo.getUsercode());
                    ResultSet rs = stmt.executeQuery();
                    List<List<String>> lists = new ArrayList<>();
                    while (rs.next()) {
                        String cno = rs.getString("CNO");
                        String cname = rs.getString("CNAME");
                        String sclass = rs.getString("SCLASS");
                        String num = rs.getString("NUM");
                        String highest = rs.getString("HIGHEST");
                        String lowest = rs.getString("LOWEST");
                        String average = rs.getString("AVERAGE");
                        List<String> temp = new ArrayList<>();
                        temp.add(cno);
                        temp.add(cname);
                        temp.add(sclass);
                        temp.add(num);
                        temp.add(highest);
                        temp.add(lowest);
                        temp.add(average);
                        lists.add(temp);
                    }
                    Tcalculate tcalculate = new Tcalculate(lists);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        comboBoxCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                    String sql;
                    PreparedStatement stmt = null;
                    int selectedindex = comboBoxCourse.getSelectedIndex();
                    index = selectedindex;
                    if (selectedindex == 0) {
                        sql = "SELECT * FROM TGRADEINFO WHERE TNO = ?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, LoginDemo.getUsercode());
                    } else {
                        sql = "SELECT * FROM TGRADEINFO WHERE TNO = ? and CNAME = ?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, LoginDemo.getUsercode());
                        stmt.setString(2, comboBoxCourse.getItemAt(selectedindex));
                    }
                    ResultSet rs = stmt.executeQuery();
                    List<List<String>> listSet = new ArrayList<>();
                    List<String> list;
                    while (rs.next()) {
                        list = new ArrayList<>();
                        String cno = rs.getString("CNO");
                        String cname = rs.getString("CNAME");
                        String sno = rs.getString("SNO");
                        String sname = rs.getString("SNAME");
                        String sclass = rs.getString("SCLASS");
                        String grade = rs.getString("GRADE");
//                        String pass = rs.getString("PASS");
                        String cmp = grade;
                        String pass;
                        if(grade.equals("优")||grade.equals("良")||grade.equals("中")||grade.equals("及格")){
                            pass = "是";
                        }
                        else if(grade.equals("不合格")){
                            pass = "否";
                        }
                        else if(Integer.parseInt(grade)>=60){
                            pass = "是";
                        }
                        else {
                            pass = "否";
                        }
                        list.add(cno);
                        list.add(cname);
                        list.add(sno);
                        list.add(sname);
                        list.add(sclass);
                        list.add(grade);
                        list.add(pass);
                        listSet.add(list);
                    }
                    int i = 0, j = 0;
                    for (List<String> list1 : listSet) {
                        tableModel.setRowCount(rs.getRow());
                        for (String S : list1) {
                            tableModel.setValueAt(S, i, j);
                            j++;
                        }
                        i++;
                        j = 0;
                    }
                    table.repaint();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        comboBoxSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                    String sql;
                    int index = comboBoxCourse.getSelectedIndex();
                    ResultSet rs = null;
                    switch (comboBoxSort.getSelectedIndex()) {
                        case 0: {
                            if (index == 0) {
                                sql = "SELECT * FROM TGRADEINFO LEFT JOIN GRADE_ORDER ON TGRADEINFO.GRADE = GRADE_ORDER.GRADEFIVE WHERE TNO = ? ORDER BY TGRADEINFO.CNO ASC, CASE WHEN GRADE_ORDER.GRADEFIVE IS NULL THEN CASE WHEN REGEXP_LIKE(TGRADEINFO.GRADE, '^[0-9]+$') THEN CAST(TGRADEINFO.GRADE AS INT) ELSE NULL END ELSE GRADE_ORDER.ORDERNUM END ASC";
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, LoginDemo.getUsercode());
                                rs = stmt.executeQuery();
                            } else {
                                sql = "SELECT * FROM TGRADEINFO LEFT JOIN GRADE_ORDER ON TGRADEINFO.GRADE = GRADE_ORDER.GRADEFIVE WHERE TNO = ? AND CNAME = ? ORDER BY TGRADEINFO.CNO ASC, CASE WHEN GRADE_ORDER.GRADEFIVE IS NULL THEN CASE WHEN REGEXP_LIKE(TGRADEINFO.GRADE, '^[0-9]+$') THEN CAST(TGRADEINFO.GRADE AS INT) ELSE NULL END ELSE GRADE_ORDER.ORDERNUM END ASC";
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, LoginDemo.getUsercode());
                                stmt.setString(2, comboBoxCourse.getItemAt(index));
                                rs = stmt.executeQuery();
                            }
                        }
                        break;
                        case 1: {
                            if (index == 0) {
                                sql = "SELECT * FROM TGRADEINFO LEFT JOIN GRADE_ORDER ON TGRADEINFO.GRADE = GRADE_ORDER.GRADEFIVE WHERE TNO = ? ORDER BY TGRADEINFO.CNO ASC, CASE WHEN GRADE_ORDER.GRADEFIVE IS NULL THEN CASE WHEN REGEXP_LIKE(TGRADEINFO.GRADE, '^[0-9]+$') THEN CAST(TGRADEINFO.GRADE AS INT) ELSE NULL END ELSE GRADE_ORDER.ORDERNUM END DESC";
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, LoginDemo.getUsercode());
                                rs = stmt.executeQuery();
                            } else {
                                sql = "SELECT * FROM TGRADEINFO LEFT JOIN GRADE_ORDER ON TGRADEINFO.GRADE = GRADE_ORDER.GRADEFIVE WHERE TNO = ? AND CNAME = ? ORDER BY TGRADEINFO.CNO ASC, CASE WHEN GRADE_ORDER.GRADEFIVE IS NULL THEN CASE WHEN REGEXP_LIKE(TGRADEINFO.GRADE, '^[0-9]+$') THEN CAST(TGRADEINFO.GRADE AS INT) ELSE NULL END ELSE GRADE_ORDER.ORDERNUM END DESC";
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, LoginDemo.getUsercode());
                                stmt.setString(2, comboBoxCourse.getItemAt(index));
                                rs = stmt.executeQuery();
                            }
                        }
                        break;
                        case 2: {
                            if (index == 0) {
                                sql = "SELECT * FROM TGRADEINFO WHERE TNO = ? ORDER BY CNO ASC, SNO ASC";
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, LoginDemo.getUsercode());
                                rs = stmt.executeQuery();
                            } else {
                                sql = "SELECT * FROM TGRADEINFO WHERE TNO = ? AND CNAME = ? ORDER BY SNO ASC";
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, LoginDemo.getUsercode());
                                stmt.setString(2, comboBoxCourse.getItemAt(index));
                                rs = stmt.executeQuery();
                            }
                        }
                        break;
                        case 3: {
                            if (index == 0) {
                                sql = "SELECT * FROM TGRADEINFO WHERE TNO = ? ORDER BY CNO ASC, SNO DESC";
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, LoginDemo.getUsercode());
                                rs = stmt.executeQuery();
                            } else {
                                sql = "SELECT * FROM TGRADEINFO WHERE TNO = ? AND CNAME = ? ORDER BY SNO DESC";
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, LoginDemo.getUsercode());
                                stmt.setString(2, comboBoxCourse.getItemAt(index));
                                rs = stmt.executeQuery();
                            }
                        }
                        break;
                    }
                    List<List<String>> listSet = new ArrayList<>();
                    List<String> list;
                    while (rs.next()) {
                        list = new ArrayList<>();
                        String cno = rs.getString("CNO");
                        String cname = rs.getString("CNAME");
                        String sno = rs.getString("SNO");
                        String sname = rs.getString("SNAME");
                        String sclass = rs.getString("SCLASS");
                        String grade = rs.getString("GRADE");
                        //String pass = rs.getString("PASS");
                        String cmp = grade;
                        String pass;
                        if(grade.equals("优")||grade.equals("良")||grade.equals("中")||grade.equals("及格")){
                            pass = "是";
                        }
                        else if(grade.equals("不合格")){
                            pass = "否";
                        }
                        else if(Integer.parseInt(grade)>=60){
                            pass = "是";
                        }
                        else {
                            pass = "否";
                        }
                        list.add(cno);
                        list.add(cname);
                        list.add(sno);
                        list.add(sname);
                        list.add(sclass);
                        list.add(grade);
                        list.add(pass);
                        listSet.add(list);
                    }
                    int i = 0, j = 0;
                    for (List<String> list1 : listSet) {
                        tableModel.setRowCount(rs.getRow());
                        for (String S : list1) {
                            tableModel.setValueAt(S, i, j);
                            j++;
                        }
                        i++;
                        j = 0;
                    }
                    table.repaint();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
//        changeField.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                Document doc = e.getDocument();
//                try {
//                    String S = doc.getText(0, doc.getLength());
//                    if (S.length() == information.get(0).get(2).length()) {
//                        boolean flag = false;
//                        TchangeCourse tchangeCourse = new TchangeCourse(S, 600, 400, 500, 350);
//                        //flag = tchangeCourse.getFlag();
////                        if (!flag) {
////                            JOptionPane.showMessageDialog(null, "没有该学号的学生！");
////                        }
//                    }
//                    if (S.length() > information.get(0).get(2).length()) {
//                        JOptionPane.showMessageDialog(null, "输入学号位数错误！");
//                    }
//                } catch (BadLocationException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//            }
//        });
        addGrade.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document doc = e.getDocument();
                try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                    String S = doc.getText(0, doc.getLength());
                    String sqlfind = "SELECT * FROM STUDENT STUDENT WHERE SNO = ?";
                    String sqlfindCourse = "SELECT CNO, CNAME,CSCORE FROM COURSE WHERE TNO = ?";
                    PreparedStatement find1 = conn.prepareStatement(sqlfind);
                    PreparedStatement find2 = conn.prepareStatement(sqlfindCourse);
                    find1.setString(1, S);
                    find2.setString(1, LoginDemo.getUsercode());
                    ResultSet findSet = find1.executeQuery();
                    ResultSet courseSet = find2.executeQuery();
                    Set<Map<String, String>> course = new HashSet<>();
                    Set<Map<String, String>> kind1 = new HashSet<>();
                    Set<Map<String, String>> kind2 = new HashSet<>();
                    while (courseSet.next()) {
                        Map<String, String> map1 = new LinkedHashMap<>();
                        Map<String, String> map2 = new LinkedHashMap<>();
                        Map<String, String> map3 = new LinkedHashMap<>();
                        map1.put(courseSet.getString("CNO"), courseSet.getString("CNAME"));
                        map2.put(courseSet.getString("CNO"), courseSet.getString("CSCORE"));
                        map3.put(courseSet.getString("CNAME"), courseSet.getString("CNO"));
                        course.add(map1);
                        kind1.add(map2);
                        kind2.add(map3);
                    }
                    if (findSet.next()) {
                        List<String> jlabels = new ArrayList<>();
                        jlabels.add("学生学号");
                        jlabels.add("课程名称");
                        jlabels.add("课程分制");
                        jlabels.add("学生成绩");
                        AddGrade addGrade = new AddGrade(400, 200, 500, 300, S, course, kind1, kind2, jlabels);
                    }
                } catch (SQLException | BadLocationException ex) {
                    ex.printStackTrace();
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

    private static class TbasicInfo extends JFrame {
        private final JTextArea textArea;

        public TbasicInfo(List<String> list) {
            super("教师基本信息");
            textArea = new JTextArea();
            this.setBounds(600, 200, 400, 250);
            String text = "工号：" + list.get(0) + "\n姓名：" + list.get(1) + "\n性别：" + list.get(2) + "\n职称：" + list.get(3);
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

    private static class TchangePwd extends JFrame {
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

        public TchangePwd(int x, int y, int witdh, int height) {
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
                            String sql = "UPDATE TEACHER SET PWD = ? WHERE TEACHER.TNO = ?";
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

    private static class TchangeCourse extends JFrame {
        JButton btnOk;
        JTextField[] jTextFields;

        public TchangeCourse(String inputNum, int x, int y, int witdh, int height) {
            super("学号" + inputNum + "的学生的成绩");
            setBounds(x, y, witdh, height);
            this.setVisible(true);
            init(inputNum);

        }
        public void init(String inputNum) {
            //JPanel jPanel = new JPanel(new GridLayout(,3));
        }
    }

    private static class Tcalculate extends JFrame {
        private final String[] instruction;
        private final JPanel jPanel;
        private final JTable table;
        private final DefaultTableModel tableModel;

        public Tcalculate(List<List<String>> lists) {
            super("成绩统计数据");
            this.setBounds(400, 100, 600, 300);
            table = new JTable(lists.size(), 7);
            instruction = new String[]{"课程编号", "课程名称", "班级", "选修人数", "最高分", "最低分", "平均分"};
            tableModel = (DefaultTableModel) table.getModel();
            tableModel.setColumnIdentifiers(instruction);
            int i = 0, j = 0;
            for (List<String> str : lists) {
                for (String e : str) {
                    tableModel.setValueAt(e, i, j);
                    j++;
                }
                j = 0;
                i++;
            }
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


            jPanel = new JPanel();
            BoxLayout boxLayout = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
            jPanel.setLayout(boxLayout);
            jPanel.add(new JLabel("任教科目成绩统计数据"));
            jPanel.add(table);

            table.setDefaultEditor(Object.class, null); //设置表格成绩表不能修改
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);  // 设置显示居中
            table.setDefaultRenderer(Object.class, centerRenderer);

            this.add(jPanel);
            this.setVisible(true);
        }
    }

    private static class AddGrade extends JFrame {
        JButton btnOk;
        JComboBox<String> comboBoxGrade;

        JTextField[] jTextFields;

        public AddGrade(int x, int y, int width, int height, String s, Set<Map<String, String>> course, Set<Map<String, String>> kind1, Set<Map<String, String>> kind2, List<String> jlbnum) {
            this.setTitle("添加成绩");
            this.setBounds(x, y, width, height);
            btnOk = new JButton("确定");
            jTextFields = new JTextField[5];
            comboBoxGrade = new JComboBox<String>();
            Map<String, String> map = new LinkedHashMap<>();
            JPanel jPanel = new JPanel(new GridLayout(5, 2));
            for (int i = 0, j = 0; i < 4; i++) {
                jPanel.add(new JLabel(jlbnum.get(i)));
                if (i != 1) {
                    jTextFields[j] = new JTextField(15);
                    jPanel.add(jTextFields[j]);
                    j++;
                } else {
                    jPanel.add(comboBoxGrade);
                }
            }
            for (Map<String, String> e : course) {
                for (String temp : e.values()) {
                    comboBoxGrade.addItem(temp);
                }
            }
            jTextFields[0].setText(s);
            jPanel.add(btnOk);
            comboBoxGrade.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    String num = comboBoxGrade.getSelectedItem().toString();
                    for (Map<String, String> e1 : kind2) {
                        if (e1.containsKey(num)) {
                            String cno = e1.get(num);
                            for (Map<String, String> e2 : kind1) {
                                if (e2.containsKey(cno)) {
                                    if (e2.get(cno).equals("0")) {
                                        jTextFields[1].setText("百分制");
                                    } else {
                                        jTextFields[1].setText("五分制");
                                    }
                                }
                            }
                        }
                    }
                }
            });
            btnOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isNull = false;
                    for (int i = 0; i < 3; i++) {
                        if (jTextFields[i].getText().equals("")) {
                            isNull = true;
                        }
                    }
                    if (comboBoxGrade.getSelectedItem() == null) {
                        isNull = true;
                    }

                    if (isNull) {
                        JOptionPane.showMessageDialog(null, "输入信息不完整！");
                    }
                    else {
                        boolean judge = true;
                        if (jTextFields[1].getText().equals("五分制")) {
                            String grade = jTextFields[2].getText();
                            if (!grade.equals("优") && !grade.equals("良") && !grade.equals("中") && !grade.equals("差") && !grade.equals("不合格")) {
                                judge = false;
                                JOptionPane.showMessageDialog(null, "输入成绩与分制不符！");
                            }
                        }
                        if (judge) {
                            try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "Aa01076543")) {
                                String sqlFind = "SELECT * FROM GRADES WHERE SNO = ? AND CNO = ?";
                                String sqlChange = "UPDATE GRADES SET GRADE = ? WHERE SNO = ? AND CNO = ?";
                                String sqlInsert = "INSERT INTO GRADES VALUES(?,?,?,?,?)";

                                String sno = s;
                                String cno = null;
                                String grade = jTextFields[2].getText();
                                String cscore;
                                String tno = LoginDemo.getUsercode();

                                for (Map<String, String> m : kind2) {
                                    if (m.containsKey(comboBoxGrade.getSelectedItem().toString())) {
                                        cno = m.get(comboBoxGrade.getSelectedItem().toString());
                                        break;
                                    }
                                }
                                if (jTextFields[1].getText().equals("百分制")) {
                                    cscore = "0";
                                } else {
                                    cscore = "1";
                                }


                                PreparedStatement stmt = conn.prepareStatement(sqlFind);
                                stmt.setString(1, sno);
                                stmt.setString(2, cno);
                                ResultSet rs = stmt.executeQuery();
                                if (rs.next()) {
                                    String show = "正在修改学号" + jTextFields[0].getText() + "的学生的成绩！";
                                    JOptionPane.showMessageDialog(null, show);
                                    stmt = conn.prepareStatement(sqlChange);
                                    stmt.setString(1, grade);
                                    stmt.setString(2, sno);
                                    stmt.setString(3, cno);
                                    int rows = stmt.executeUpdate();
                                    conn.commit();
                                    System.out.println("修改成功");
                                    show = "学号" + jTextFields[0].getText() + "的学生的成绩已修改！";
                                    JOptionPane.showMessageDialog(null, show);
                                } else {
                                    stmt = conn.prepareStatement(sqlInsert);
                                    String show = "正在录入学号" + jTextFields[0].getText() + "的学生的成绩！";
                                    JOptionPane.showMessageDialog(null, show);
//                            stmt.setString(1, s);
//
//                            for (Map<String, String> m : kind2) {
//                                if (m.containsKey(comboBoxGrade.getSelectedItem().toString())) {
//                                    cno = m.get(comboBoxGrade.getSelectedItem().toString());
//                                    stmt.setString(2, cno);
//                                    break;
//                                }
//                            }
//
//                            stmt.setString(3, jTextFields[2].getText());
//
//                            if (jTextFields[1].getText().equals("百分制")) {
//                                stmt.setString(4, "0");
//                            } else {
//                                stmt.setString(4, "1");
//                            }
//
//                            stmt.setString(5, LoginDemo.getUsercode());
                                    stmt.setString(1, sno);
                                    stmt.setString(2, cno);
                                    stmt.setString(3, grade);
                                    stmt.setString(4, cscore);
                                    stmt.setString(5, tno);
                                    int rows = stmt.executeUpdate();
                                    conn.commit();
                                    show = "学号" + jTextFields[0].getText() + "的学生的成绩已录入！";
                                    JOptionPane.showMessageDialog(null, show);
                                }

                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            });
            this.add(jPanel);
            this.setVisible(true);
        }
    }

}