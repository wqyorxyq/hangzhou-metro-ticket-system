import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
// 地铁站类
class MetroStation {
    String name;
    String line;
    int id;
    boolean isTransfer; // 是否为换乘站
    
    public MetroStation(String name, String line, int id, boolean isTransfer) {
        this.name = name;
        this.line = line;
        this.id = id;
        this.isTransfer = isTransfer;
    }
    
    @Override
    public String toString() {
        return line + " - " + name;
    }
}

// 地铁线路类
class MetroLine {
    String name;
    Color color;
    List<MetroStation> stations;
    
    public MetroLine(String name, Color color) {
        this.name = name;
        this.color = color;
        this.stations = new ArrayList<>();
    }
    
    public void addStation(MetroStation station) {
        stations.add(station);
    }
}

// 可拖拽的线路显示面板
class LineDisplayPanel extends JPanel {
    private List<MetroLine> lines;
    private MetroStationClickListener clickListener;
    private int currentLineIndex = 0;
    
    // 按钮
    private JButton prevButton;
    private JButton nextButton;
    private JLabel lineTitleLabel;
    private JPanel stationsPanel;
    
    public LineDisplayPanel(List<MetroLine> lines) {
        this.lines = lines;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // 创建标题和导航面板
        createHeaderPanel();
        
        // 创建站点显示面板
        createStationsPanel();
        
        // 初始显示第一条线路
        showLine(currentLineIndex);
    }
    
    // 创建标题和导航面板
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(new Color(245, 248, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 线路标题
        lineTitleLabel = new JLabel();
        lineTitleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        lineTitleLabel.setForeground(new Color(25, 70, 130));
        
        // 左侧按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(245, 248, 250));
        
        prevButton = createNavButton("<", new Color(100, 150, 220));
        prevButton.addActionListener(e -> showPreviousLine());
        
        nextButton = createNavButton(">", new Color(100, 150, 220));
        nextButton.addActionListener(e -> showNextLine());
        
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        
        headerPanel.add(lineTitleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    // 创建导航按钮
    private JButton createNavButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(new Color(25, 70, 130));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // 鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    // 创建站点显示面板
    private void createStationsPanel() {
        stationsPanel = new JPanel();
        stationsPanel.setBackground(Color.WHITE);
        stationsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(stationsPanel, BorderLayout.CENTER);
    }
    
    // 显示指定索引的线路
    private void showLine(int lineIndex) {
        if (lineIndex < 0 || lineIndex >= lines.size()) return;
        
        currentLineIndex = lineIndex;
        MetroLine line = lines.get(lineIndex);
        
        // 更新标题
        lineTitleLabel.setText(line.name + " (" + (currentLineIndex + 1) + "/" + lines.size() + ")");
        
        // 更新按钮状态
        prevButton.setEnabled(currentLineIndex > 0);
        nextButton.setEnabled(currentLineIndex < lines.size() - 1);
        
        // 清空并重新绘制站点面板
        stationsPanel.removeAll();
        
        // 使用流式布局，站点垂直排列
        stationsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // 添加线路信息标签
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(line.color, 3),
            "线路信息",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14),
            line.color
        ));
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JLabel stationsCountLabel = new JLabel("站点数: " + line.stations.size());
        stationsCountLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        JLabel lineColorLabel = new JLabel("线路颜色:");
        lineColorLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        JPanel colorSample = new JPanel();
        colorSample.setBackground(line.color);
        colorSample.setPreferredSize(new Dimension(30, 20));
        colorSample.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        infoPanel.add(stationsCountLabel);
        infoPanel.add(lineColorLabel);
        infoPanel.add(colorSample);
        
        stationsPanel.add(infoPanel, gbc);
        gbc.gridy++;
        
        // 添加站点显示区域
        JPanel stationListPanel = new JPanel();
        stationListPanel.setLayout(new BoxLayout(stationListPanel, BoxLayout.Y_AXIS));
        stationListPanel.setBackground(Color.WHITE);
        stationListPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // 绘制线路示意图
        JPanel lineDiagramPanel = createLineDiagram(line);
        stationListPanel.add(lineDiagramPanel);
        
        // 添加分隔线
        stationListPanel.add(Box.createVerticalStrut(20));
        stationListPanel.add(new JSeparator());
        stationListPanel.add(Box.createVerticalStrut(20));
        
        // 添加站点列表
        JLabel stationListLabel = new JLabel("站点列表");
        stationListLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        stationListLabel.setForeground(new Color(25, 70, 130));
        stationListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stationListPanel.add(stationListLabel);
        stationListPanel.add(Box.createVerticalStrut(15));
        
        // 创建站点按钮
        for (MetroStation station : line.stations) {
            JPanel stationPanel = createStationPanel(station, line.color);
            stationPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            stationListPanel.add(stationPanel);
            stationListPanel.add(Box.createVerticalStrut(5));
        }
        
        // 添加滚动条
        JScrollPane scrollPane = new JScrollPane(stationListPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(450, 400));
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        stationsPanel.add(scrollPane, gbc);
        
        // 重新绘制
        stationsPanel.revalidate();
        stationsPanel.repaint();
    }
    
    // 创建线路示意图
    private JPanel createLineDiagram(MetroLine line) {
        JPanel diagramPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // 绘制线路背景
                g2d.setColor(new Color(250, 250, 255));
                g2d.fillRect(0, 0, width, height);
                
                // 绘制线路标题
                g2d.setColor(new Color(25, 70, 130));
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
                String title = line.name + " 线路示意图";
                int titleWidth = g2d.getFontMetrics().stringWidth(title);
                g2d.drawString(title, (width - titleWidth) / 2, 25);
                
                // 绘制水平线路
                g2d.setColor(line.color);
                g2d.setStroke(new BasicStroke(6));
                int lineY = height / 2;
                g2d.drawLine(30, lineY, width - 30, lineY);
                
                // 绘制站点
                int stationCount = line.stations.size();
                int availableWidth = width - 60;
                int stationSpacing = availableWidth / (stationCount + 1);
                
                for (int i = 0; i < stationCount; i++) {
                    int stationX = 30 + (i + 1) * stationSpacing;
                    
                    // 绘制站点圆圈
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(stationX - 10, lineY - 10, 20, 20);
                    g2d.setColor(line.color);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawOval(stationX - 10, lineY - 10, 20, 20);
                    
                    // 绘制站点编号
                    g2d.setColor(line.color);
                    g2d.setFont(new Font("微软雅黑", Font.BOLD, 12));
                    String stationNum = String.valueOf(i + 1);
                    int numWidth = g2d.getFontMetrics().stringWidth(stationNum);
                    g2d.drawString(stationNum, stationX - numWidth/2, lineY + 4);
                    
                    // 绘制站点名称（在线上方或下方交替显示）
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("微软雅黑", Font.PLAIN, 10));
                    String stationName = line.stations.get(i).name;
                    int nameWidth = g2d.getFontMetrics().stringWidth(stationName);
                    
                    // 如果是换乘站，用红色标记
                    if (line.stations.get(i).isTransfer) {
                        g2d.setColor(Color.RED);
                        g2d.fillOval(stationX - 5, lineY - 5, 10, 10);
                        g2d.setColor(Color.BLACK);
                    }
                    
                    // 交替显示站点名称在线上方和下方
                    if (i % 2 == 0) {
                        g2d.drawString(stationName, stationX - nameWidth/2, lineY - 15);
                    } else {
                        g2d.drawString(stationName, stationX - nameWidth/2, lineY + 30);
                    }
                }
                
                // 绘制方向箭头
                g2d.setColor(line.color);
                g2d.fillPolygon(
                    new int[]{width - 40, width - 20, width - 40},
                    new int[]{lineY - 10, lineY, lineY + 10},
                    3
                );
                
                // 添加方向文字
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("微软雅黑", Font.PLAIN, 12));
                g2d.drawString("运行方向", width - 70, lineY + 30);
            }
        };
        
        diagramPanel.setPreferredSize(new Dimension(500, 150));
        diagramPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        return diagramPanel;
    }
    
    // 创建站点面板
    private JPanel createStationPanel(MetroStation station, Color lineColor) {
        JPanel stationPanel = new JPanel(new BorderLayout(10, 0));
        stationPanel.setBackground(Color.WHITE);
        stationPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        stationPanel.setPreferredSize(new Dimension(400, 60));
        stationPanel.setMaximumSize(new Dimension(400, 60));
        
        // 站点序号和名称
        JPanel infoPanel = new JPanel(new BorderLayout(10, 0));
        infoPanel.setBackground(Color.WHITE);
        
        // 站点序号
        JLabel indexLabel = new JLabel("站");
        indexLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        indexLabel.setForeground(Color.WHITE);
        indexLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel indexPanel = new JPanel(new BorderLayout());
        indexPanel.setBackground(lineColor);
        indexPanel.setPreferredSize(new Dimension(40, 40));
        indexPanel.setBorder(BorderFactory.createLineBorder(lineColor.darker(), 1));
        indexPanel.add(indexLabel, BorderLayout.CENTER);
        
        // 站点信息
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(station.name);
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        
        JLabel lineLabel = new JLabel(station.line);
        lineLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        lineLabel.setForeground(new Color(100, 100, 100));
        
        textPanel.add(nameLabel);
        textPanel.add(lineLabel);
        
        infoPanel.add(indexPanel, BorderLayout.WEST);
        infoPanel.add(textPanel, BorderLayout.CENTER);
        
        // 换乘站标记
        if (station.isTransfer) {
            JLabel transferLabel = new JLabel("换乘站");
            transferLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
            transferLabel.setForeground(Color.RED);
            transferLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            infoPanel.add(transferLabel, BorderLayout.EAST);
        }
        
        // 选择按钮
        JButton selectButton = createSelectButton(station);
        
        stationPanel.add(infoPanel, BorderLayout.CENTER);
        stationPanel.add(selectButton, BorderLayout.EAST);
        
        return stationPanel;
    }
    
    // 创建选择按钮
    private JButton createSelectButton(MetroStation station) {
        JButton button = new JButton("选择");
        button.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        button.setBackground(new Color(80, 150, 220));
        button.setForeground(new Color(25, 70, 130));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 130, 200), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // 鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 170, 240));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(80, 150, 220));
            }
        });
        
        // 点击事件
        button.addActionListener(e -> {
            if (clickListener != null) {
                clickListener.onStationClicked(station);
            }
        });
        
        return button;
    }
    
    // 显示上一条线路
    private void showPreviousLine() {
        if (currentLineIndex > 0) {
            showLine(currentLineIndex - 1);
        }
    }
    
    // 显示下一条线路
    private void showNextLine() {
        if (currentLineIndex < lines.size() - 1) {
            showLine(currentLineIndex + 1);
        }
    }
    
    // 设置点击监听器
    public void setStationClickListener(MetroStationClickListener listener) {
        this.clickListener = listener;
    }
}

// 站点点击监听器接口
interface MetroStationClickListener {
    void onStationClicked(MetroStation station);
}

// 主程序类
public class HangzhouMetroTicketSystem extends JFrame {
    // 组件声明
    private JComboBox<MetroStation> startComboBox;
    private JComboBox<MetroStation> endComboBox;
    private JLabel priceLabel;
    private JLabel paidLabel;
    private JLabel remainingLabel;
    private JTextArea ticketArea;
    private LineDisplayPanel linePanel;
    private JPanel coinPanel;
    private JPanel banknotePanel;
    
    // 数据
    private List<MetroLine> lines;
    private double ticketPrice = 0.0;
    private double paidAmount = 0.0;
    private double totalPrice = 0.0;
    private boolean ticketIssued = false;
    
    // 纸币和硬币面值
    private final double[] COINS = {0.5, 1.0, 2.0};
    private final double[] BANKNOTES = {5.0, 10.0, 20.0, 50.0, 100.0};
    
    public HangzhouMetroTicketSystem() {
        // 初始化地铁数据
        initializeMetroData();
        
        // 设置窗口
        setTitle("杭州地铁自助购票系统");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 245, 250));
        
        // 创建顶部标题
        createHeaderPanel();
        
        // 创建主体部分
        createMainPanel();
        
        // 创建底部按钮
        createFooterPanel();
        
        // 初始更新
        updatePrice();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    // 初始化地铁数据
    private void initializeMetroData() {
        lines = new ArrayList<>();
        
        // 1号线
        MetroLine line1 = new MetroLine("1号线", new Color(0, 150, 57));
        line1.addStation(new MetroStation("湘湖", "1号线", 1, false));
        line1.addStation(new MetroStation("滨康路", "1号线", 2, true));
        line1.addStation(new MetroStation("西兴", "1号线", 3, false));
        line1.addStation(new MetroStation("滨和路", "1号线", 4, false));
        line1.addStation(new MetroStation("江陵路", "1号线", 5, false));
        line1.addStation(new MetroStation("近江", "1号线", 6, true));
        line1.addStation(new MetroStation("婺江路", "1号线", 7, false));
        line1.addStation(new MetroStation("城站", "1号线", 8, false));
        line1.addStation(new MetroStation("定安路", "1号线", 9, false));
        line1.addStation(new MetroStation("龙翔桥", "1号线", 10, false));
        line1.addStation(new MetroStation("凤起路", "1号线", 11, true));
        line1.addStation(new MetroStation("武林广场", "1号线", 12, true));
        line1.addStation(new MetroStation("西湖文化广场", "1号线", 13, false));
        line1.addStation(new MetroStation("打铁关", "1号线", 14, false));
        line1.addStation(new MetroStation("闸弄口", "1号线", 15, false));
        lines.add(line1);
        
        // 2号线
        MetroLine line2 = new MetroLine("2号线", new Color(158, 0, 93));
        line2.addStation(new MetroStation("良渚", "2号线", 21, false));
        line2.addStation(new MetroStation("杜甫村", "2号线", 22, false));
        line2.addStation(new MetroStation("白洋", "2号线", 23, false));
        line2.addStation(new MetroStation("金家渡", "2号线", 24, false));
        line2.addStation(new MetroStation("墩祥街", "2号线", 25, false));
        line2.addStation(new MetroStation("三墩", "2号线", 26, false));
        line2.addStation(new MetroStation("虾龙圩", "2号线", 27, false));
        line2.addStation(new MetroStation("三坝", "2号线", 28, false));
        line2.addStation(new MetroStation("文新", "2号线", 29, false));
        line2.addStation(new MetroStation("丰潭路", "2号线", 30, false));
        line2.addStation(new MetroStation("古翠路", "2号线", 31, false));
        line2.addStation(new MetroStation("学院路", "2号线", 32, false));
        line2.addStation(new MetroStation("沈塘桥", "2号线", 33, false));
        line2.addStation(new MetroStation("武林门", "2号线", 34, false));
        line2.addStation(new MetroStation("凤起路", "2号线", 35, true));
        lines.add(line2);
        
        // 4号线
        MetroLine line4 = new MetroLine("4号线", new Color(0, 168, 228));
        line4.addStation(new MetroStation("浦沿", "4号线", 41, false));
        line4.addStation(new MetroStation("杨家墩", "4号线", 42, false));
        line4.addStation(new MetroStation("中医药大学", "4号线", 43, true));
        line4.addStation(new MetroStation("联庄", "4号线", 44, false));
        line4.addStation(new MetroStation("水澄桥", "4号线", 45, false));
        line4.addStation(new MetroStation("复兴路", "4号线", 46, false));
        line4.addStation(new MetroStation("南星桥", "4号线", 47, false));
        line4.addStation(new MetroStation("甬江路", "4号线", 48, false));
        line4.addStation(new MetroStation("近江", "4号线", 49, true));
        line4.addStation(new MetroStation("城星路", "4号线", 50, false));
        line4.addStation(new MetroStation("市民中心", "4号线", 51, false));
        line4.addStation(new MetroStation("江锦路", "4号线", 52, false));
        line4.addStation(new MetroStation("钱江路", "4号线", 53, false));
        line4.addStation(new MetroStation("景芳", "4号线", 54, false));
        lines.add(line4);
        
        // 5号线
        MetroLine line5 = new MetroLine("5号线", new Color(178, 111, 40));
        line5.addStation(new MetroStation("姑娘桥", "5号线", 61, false));
        line5.addStation(new MetroStation("双桥", "5号线", 62, false));
        line5.addStation(new MetroStation("火车南站", "5号线", 63, false));
        line5.addStation(new MetroStation("通惠中路", "5号线", 64, false));
        line5.addStation(new MetroStation("育才北路", "5号线", 65, false));
        line5.addStation(new MetroStation("人民广场", "5号线", 66, false));
        line5.addStation(new MetroStation("金鸡路", "5号线", 67, false));
        line5.addStation(new MetroStation("博奥路", "5号线", 68, false));
        line5.addStation(new MetroStation("滨康路", "5号线", 69, true));
        line5.addStation(new MetroStation("江晖路", "5号线", 70, false));
        line5.addStation(new MetroStation("聚才路", "5号线", 71, false));
        line5.addStation(new MetroStation("长河", "5号线", 72, false));
        line5.addStation(new MetroStation("中医药大学", "5号线", 73, true));
        lines.add(line5);
    }
    
    // 创建顶部标题
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 70, 130));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("杭州地铁自助购票系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Hangzhou Metro Self-service Ticketing System");
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    // 创建主体部分
    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 245, 250));
        
        // 左侧：选择和支付面板
        JPanel leftPanel = createLeftPanel();
        
        // 右侧：线路显示面板
        JPanel rightPanel = createRightPanel();
        
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    // 创建左侧面板
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(0, 15));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // 站点选择面板
        JPanel selectionPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        selectionPanel.setBackground(Color.WHITE);
        selectionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 200), 1),
            "站点选择",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14),
            new Color(50, 100, 180)
        ));
        
        // 起始站
        JPanel startPanel = new JPanel(new BorderLayout(10, 0));
        startPanel.setBackground(Color.WHITE);
        JLabel startLabel = new JLabel("起始站:");
        startLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        startComboBox = new JComboBox<>();
        populateStationsComboBox(startComboBox);
        startComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        startComboBox.addActionListener(e -> updatePrice());
        startPanel.add(startLabel, BorderLayout.WEST);
        startPanel.add(startComboBox, BorderLayout.CENTER);
        
        // 终点站
        JPanel endPanel = new JPanel(new BorderLayout(10, 0));
        endPanel.setBackground(Color.WHITE);
        JLabel endLabel = new JLabel("终点站:");
        endLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        endComboBox = new JComboBox<>();
        populateStationsComboBox(endComboBox);
        endComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        endComboBox.addActionListener(e -> updatePrice());
        endPanel.add(endLabel, BorderLayout.WEST);
        endPanel.add(endComboBox, BorderLayout.CENTER);
        
        // 票价显示面板
        JPanel displayPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        displayPanel.setBackground(Color.WHITE);
        displayPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 200), 1),
            "票价信息",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14),
            new Color(50, 100, 180)
        ));
        
        // 票价
        JPanel pricePanel = new JPanel(new BorderLayout(10, 0));
        pricePanel.setBackground(Color.WHITE);
        JLabel priceTextLabel = new JLabel("票价:");
        priceTextLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        priceLabel = new JLabel("0.0 元");
        priceLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        priceLabel.setForeground(new Color(220, 60, 60));
        pricePanel.add(priceTextLabel, BorderLayout.WEST);
        pricePanel.add(priceLabel, BorderLayout.CENTER);
        
        // 已付金额
        JPanel paidPanel = new JPanel(new BorderLayout(10, 0));
        paidPanel.setBackground(Color.WHITE);
        JLabel paidTextLabel = new JLabel("已付金额:");
        paidTextLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        paidLabel = new JLabel("0.0 元");
        paidLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        paidLabel.setForeground(new Color(30, 130, 70));
        paidPanel.add(paidTextLabel, BorderLayout.WEST);
        paidPanel.add(paidLabel, BorderLayout.CENTER);
        
        // 还需支付
        JPanel remainingPanel = new JPanel(new BorderLayout(10, 0));
        remainingPanel.setBackground(Color.WHITE);
        JLabel remainingTextLabel = new JLabel("还需支付:");
        remainingTextLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        remainingLabel = new JLabel("0.0 元");
        remainingLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        remainingLabel.setForeground(new Color(220, 60, 60));
        remainingPanel.add(remainingTextLabel, BorderLayout.WEST);
        remainingPanel.add(remainingLabel, BorderLayout.CENTER);
        
        // 添加到选择面板
        selectionPanel.add(startPanel);
        selectionPanel.add(endPanel);
        selectionPanel.add(pricePanel);
        selectionPanel.add(paidPanel);
        selectionPanel.add(remainingPanel);
        
        // 支付面板
        JPanel paymentPanel = new JPanel(new BorderLayout(0, 15));
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 200), 1),
            "支付方式",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14),
            new Color(50, 100, 180)
        ));
        
        // 硬币面板
        coinPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        coinPanel.setBackground(Color.WHITE);
        coinPanel.setBorder(BorderFactory.createTitledBorder("硬币投币"));
        
        for (double coin : COINS) {
            JButton coinButton = createMoneyButton(String.valueOf(coin), coin, "coin");
            coinPanel.add(coinButton);
        }
        
        // 纸币面板
        banknotePanel = new JPanel(new GridLayout(1, 5, 10, 0));
        banknotePanel.setBackground(Color.WHITE);
        banknotePanel.setBorder(BorderFactory.createTitledBorder("纸币付钞"));
        
        for (double note : BANKNOTES) {
            JButton noteButton = createMoneyButton(String.valueOf(note), note, "banknote");
            banknotePanel.add(noteButton);
        }
        
        // 添加到支付面板
        paymentPanel.add(coinPanel, BorderLayout.NORTH);
        paymentPanel.add(banknotePanel, BorderLayout.CENTER);
        
        // 车票显示区域
        JPanel ticketPanel = new JPanel(new BorderLayout());
        ticketPanel.setBackground(Color.WHITE);
        ticketPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 200), 1),
            "车票信息",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 14),
            new Color(50, 100, 180)
        ));
        
        ticketArea = new JTextArea();
        ticketArea.setFont(new Font("宋体", Font.PLAIN, 14));
        ticketArea.setEditable(false);
        ticketArea.setBackground(new Color(250, 250, 210));
        ticketArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ticketArea.setText("请选择起始站和终点站，然后支付相应金额。\n支付完成后将显示车票信息。");
        
        JScrollPane scrollPane = new JScrollPane(ticketArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        ticketPanel.add(scrollPane, BorderLayout.CENTER);
        
        // 添加到左侧面板
        leftPanel.add(selectionPanel, BorderLayout.NORTH);
        leftPanel.add(paymentPanel, BorderLayout.CENTER);
        leftPanel.add(ticketPanel, BorderLayout.SOUTH);
        
        return leftPanel;
    }
    
    // 创建右侧面板（线路显示）
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // 标题
        JLabel titleLabel = new JLabel("地铁线路查看 (使用 < > 按钮切换线路)");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setForeground(new Color(25, 70, 130));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // 创建线路显示面板
        linePanel = new LineDisplayPanel(lines);
        linePanel.setStationClickListener(new MetroStationClickListener() {
            @Override
            public void onStationClicked(MetroStation station) {
                handleStationClickFromPanel(station);
            }
        });
        
        rightPanel.add(titleLabel, BorderLayout.NORTH);
        rightPanel.add(linePanel, BorderLayout.CENTER);
        
        return rightPanel;
    }
    
    // 创建底部按钮
    private void createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        footerPanel.setBackground(new Color(245, 248, 250));
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 210, 220)));
        
        // 重置按钮
        JButton resetButton = new JButton("重置购票");
        styleButton(resetButton, new Color(100, 150, 220));
        resetButton.addActionListener(e -> resetSystem());
        
        // 出票按钮
        JButton issueButton = new JButton("出票");
        styleButton(issueButton, new Color(70, 180, 110));
        issueButton.addActionListener(e -> issueTicket());
        
        // 帮助按钮
        JButton helpButton = new JButton("帮助");
        styleButton(helpButton, new Color(220, 160, 60));
        helpButton.addActionListener(e -> showHelp());
        
        footerPanel.add(resetButton);
        footerPanel.add(issueButton);
        footerPanel.add(helpButton);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    // 创建支付按钮
    private JButton createMoneyButton(String text, double value, String type) {
        JButton button = new JButton(text + "元");
        
        if (type.equals("coin")) {
            button.setBackground(new Color(240, 230, 180));
            button.setForeground(new Color(120, 90, 40));
            button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        } else {
            button.setBackground(new Color(180, 220, 240));
            button.setForeground(new Color(30, 80, 140));
            button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        }
        
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        
        button.addActionListener(e -> addPayment(value));
        
        return button;
    }
    
    // 样式化按钮
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("微软雅黑", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(new Color(25, 70, 130));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // 鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    // 填充站点下拉框
    private void populateStationsComboBox(JComboBox<MetroStation> comboBox) {
        DefaultComboBoxModel<MetroStation> model = new DefaultComboBoxModel<>();
        
        // 添加提示项
        model.addElement(new MetroStation("请选择站点", "", -1, false));
        
        for (MetroLine line : lines) {
            for (MetroStation station : line.stations) {
                model.addElement(station);
            }
        }
        
        comboBox.setModel(model);
    }
    
    // 处理从线路面板点击的站点
    private void handleStationClickFromPanel(MetroStation station) {
        // 弹出选择对话框
        String[] options = {"设为起始站", "设为终点站", "取消"};
        int choice = JOptionPane.showOptionDialog(this,
            "选择 " + station.name + " (" + station.line + ") 作为:",
            "选择站点",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == 0) {
            // 设为起始站
            startComboBox.setSelectedItem(station);
        } else if (choice == 1) {
            // 设为终点站
            endComboBox.setSelectedItem(station);
        }
    }
    
    // 计算票价
    private void calculatePrice() {
        MetroStation start = (MetroStation) startComboBox.getSelectedItem();
        MetroStation end = (MetroStation) endComboBox.getSelectedItem();
        
        // 检查是否选择了有效站点
        if (start == null || end == null || 
            start.id == -1 || end.id == -1) {
            ticketPrice = 0.0;
            return;
        }
        
        // 确保起始站和终点站不同
        if (start.equals(end) || start.id == end.id) {
            ticketPrice = 0.0;
            return;
        }
        
        // 简化票价计算：基于站数计算
        int startId = start.id;
        int endId = end.id;
        int distance = Math.abs(startId - endId);
        
        // 杭州地铁票价规则简化版：起步价2元，4-12公里每4公里加1元，12-24公里每6公里加1元
        if (distance <= 4) {
            ticketPrice = 2.0;
        } else if (distance <= 12) {
            ticketPrice = 2.0 + Math.ceil((distance - 4) / 4.0);
        } else {
            ticketPrice = 4.0 + Math.ceil((distance - 12) / 6.0);
        }
        
        // 最高不超过8元
        ticketPrice = Math.min(ticketPrice, 8.0);
    }
    
    // 更新价格显示
    private void updatePrice() {
        calculatePrice();
        totalPrice = ticketPrice;
        
        priceLabel.setText(String.format("%.1f 元", ticketPrice));
        paidLabel.setText(String.format("%.1f 元", paidAmount));
        
        double remaining = totalPrice - paidAmount;
        if (remaining < 0) remaining = 0;
        
        remainingLabel.setText(String.format("%.1f 元", remaining));
        
        // 更新颜色
        if (remaining <= 0 && totalPrice > 0) {
            remainingLabel.setForeground(new Color(30, 130, 70));
            remainingLabel.setText("0.0 元 (可出票)");
        } else {
            remainingLabel.setForeground(new Color(220, 60, 60));
        }
    }
    
    // 添加支付
    private void addPayment(double amount) {
        if (ticketIssued) {
            JOptionPane.showMessageDialog(this, "请先取走车票再进行新的购票！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (totalPrice == 0) {
            JOptionPane.showMessageDialog(this, "请先选择起始站和终点站！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        paidAmount += amount;
        updatePrice();
        
        // 播放投币音效提示
        Toolkit.getDefaultToolkit().beep();
    }
    
    // 出票
    private void issueTicket() {
        if (ticketIssued) {
            JOptionPane.showMessageDialog(this, "车票已出，请取走您的车票！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (totalPrice == 0) {
            JOptionPane.showMessageDialog(this, "请先选择起始站和终点站！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (paidAmount < totalPrice) {
            JOptionPane.showMessageDialog(this, 
                String.format("支付不足！还需支付 %.1f 元", totalPrice - paidAmount), 
                "支付不足", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 生成车票信息
        MetroStation start = (MetroStation) startComboBox.getSelectedItem();
        MetroStation end = (MetroStation) endComboBox.getSelectedItem();
        double change = paidAmount - totalPrice;
        
        StringBuilder ticketInfo = new StringBuilder();
        ticketInfo.append("================================\n");
        ticketInfo.append("        杭州地铁单程票        \n");
        ticketInfo.append("================================\n");
        ticketInfo.append(String.format("起始站: %s\n", start.name));
        ticketInfo.append(String.format("终点站: %s\n", end.name));
        ticketInfo.append(String.format("线路: %s -> %s\n", start.line, end.line));
        ticketInfo.append(String.format("票价: %.1f 元\n", totalPrice));
        ticketInfo.append(String.format("支付: %.1f 元\n", paidAmount));
        ticketInfo.append(String.format("找零: %.1f 元\n", change));
        ticketInfo.append(String.format("购票时间: %s\n", new Date()));
        ticketInfo.append("有效期: 当日当站有效\n");
        ticketInfo.append("================================\n");
        ticketInfo.append("温馨提示:\n");
        ticketInfo.append("• 请妥善保管车票\n");
        ticketInfo.append("• 进出站请刷卡\n");
        ticketInfo.append("• 需要帮助请联系工作人员\n");
        ticketInfo.append("================================\n");
        
        ticketArea.setText(ticketInfo.toString());
        ticketIssued = true;
        
        // 显示成功消息
        JOptionPane.showMessageDialog(this, 
            String.format("出票成功！找零 %.1f 元\n请取走您的车票。", change), 
            "出票成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // 重置系统
    private void resetSystem() {
        startComboBox.setSelectedIndex(0);
        endComboBox.setSelectedIndex(0);
        paidAmount = 0.0;
        totalPrice = 0.0;
        ticketPrice = 0.0;
        ticketIssued = false;
        updatePrice();
        ticketArea.setText("请选择起始站和终点站，然后支付相应金额。\n支付完成后将显示车票信息。");
    }
    
    // 显示帮助
    private void showHelp() {
        String helpText = "杭州地铁自助购票系统使用说明:\n\n" +
                         "1. 选择起始站和终点站\n" +
                         "   - 从下拉列表中选择\n" +
                         "   - 或从右侧线路面板中点击站点按钮选择\n\n" +
                         "2. 查看票价并支付\n" +
                         "   - 使用硬币或纸币按钮支付\n" +
                         "   - 实时显示已付金额和剩余金额\n\n" +
                         "3. 支付完成后点击'出票'\n" +
                         "   - 系统将打印车票\n" +
                         "   - 自动计算找零\n\n" +
                         "4. 线路查看\n" +
                         "   - 使用 < > 按钮切换查看不同线路\n" +
                         "   - 每条线路显示线路示意图和站点列表\n" +
                         "   - 换乘站用红色标记\n\n" +
                         "5. 点击'重置购票'开始新的购票";
        
        JOptionPane.showMessageDialog(this, helpText, "使用帮助", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        // 设置外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new HangzhouMetroTicketSystem());
    }
}
