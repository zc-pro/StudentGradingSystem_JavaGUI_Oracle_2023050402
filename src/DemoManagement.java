import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DemoManagement extends JFrame {
    public JButton btnShow;
    public JTextField searchField;
    public JTextField changeField;
    public JButton btnAdd;
    public JButton btnDelete;
    public JTable showTable;
    public DefaultTableModel tableModel;
    public List<List<String>> tableList;

    public BorderLayout borderLayout;

    public DemoManagement(int x, int y, int witdh, int height){
        this.setBounds(x,y,witdh,height);
        borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        btnShow = new JButton("查看");
        searchField = new JTextField(10);
        changeField = new JTextField(10);
        btnAdd = new JButton("增加");
        btnDelete = new JButton("删除");
        showTable = new JTable(30,6);
        showTable.setPreferredSize(null);

        tableModel = (DefaultTableModel)showTable.getModel();
        tableList = new ArrayList<>();

        JPanel sonPanel = new JPanel();

        sonPanel.add(btnShow);
        sonPanel.add(new JLabel("搜索："));
        sonPanel.add(searchField);
        sonPanel.add(new JLabel("更改："));
        sonPanel.add(changeField);
        sonPanel.add(btnAdd);
        sonPanel.add(btnDelete);

        showTable.setMinimumSize(null);
        showTable.setMaximumSize(null);
        showTable.setPreferredSize(null);

        this.add(sonPanel,BorderLayout.NORTH);
        this.add(addJTable(showTable),BorderLayout.CENTER);


        this.setVisible(true);
    }
    public void init(){

    }

    public JScrollPane addJTable(JTable j) {
        JScrollPane scrollPane = new JScrollPane(j);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

}