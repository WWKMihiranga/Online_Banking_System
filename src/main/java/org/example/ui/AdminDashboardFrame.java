package org.example.ui;

import org.example.dao.AdminService;
import org.example.dao.InterestService;
import org.example.model.Customer;
import org.example.model.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminDashboardFrame extends JFrame {
    private final String adminUsername;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(52, 73, 94);
    private static final Color PRIMARY_LIGHT = new Color(127, 140, 141);
    private static final Color ACCENT_COLOR = new Color(155, 89, 182);
    private static final Color ACCENT_HOVER = new Color(142, 68, 173);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color SUCCESS_HOVER = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    private static final Color BORDER_COLOR = new Color(220, 221, 222);
    private static final Color TAB_SELECTED = new Color(236, 240, 241);

    public AdminDashboardFrame(String adminUsername) {
        this.adminUsername = adminUsername;
        setTitle("Administrative Dashboard - " + adminUsername);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 650));

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default
        }

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Create header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create main content with styled tabs
        JTabbedPane tabs = createStyledTabbedPane();
        tabs.add("👥 All Customers", buildCustomerTable());
        tabs.add("📊 All Transactions", buildTransactionTable());
        tabs.add("💰 Interest Management", buildInterestPanel());

        add(tabs, BorderLayout.CENTER);

        // Create footer with stats
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1000, 80));

        // Admin info section
        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 20));
        adminPanel.setBackground(PRIMARY_COLOR);

        JLabel adminIcon = new JLabel("👨‍💼");
        adminIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(PRIMARY_COLOR);

        JLabel adminLabel = new JLabel("Administrator: " + adminUsername);
        adminLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        adminLabel.setForeground(Color.WHITE);

        JLabel roleLabel = new JLabel("System Administration Panel");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(189, 195, 199));

        textPanel.add(adminLabel);
        textPanel.add(roleLabel);

        adminPanel.add(adminIcon);
        adminPanel.add(textPanel);

        // Actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 20));
        actionsPanel.setBackground(PRIMARY_COLOR);

        JButton refreshBtn = createHeaderButton("🔄 Refresh", SUCCESS_COLOR);
        refreshBtn.addActionListener(e -> {
            // Refresh all data
            dispose();
            new AdminDashboardFrame(adminUsername);
        });

        JButton logoutBtn = createHeaderButton("🚪 logout", DANGER_COLOR);
        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout from admin panel?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        });

        actionsPanel.add(refreshBtn);
        actionsPanel.add(logoutBtn);

        headerPanel.add(adminPanel, BorderLayout.WEST);
        headerPanel.add(actionsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createHeaderButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    private JTabbedPane createStyledTabbedPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(BACKGROUND_COLOR);
        tabs.setForeground(TEXT_COLOR);

        // Custom tab styling
        UIManager.put("TabbedPane.selected", TAB_SELECTED);
        UIManager.put("TabbedPane.selectedForeground", PRIMARY_COLOR);

        return tabs;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(PRIMARY_LIGHT);
        footerPanel.setPreferredSize(new Dimension(1000, 40));

        JLabel footerLabel = new JLabel("Online Banking Administration System © 2024");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(Color.WHITE);

        footerPanel.add(footerLabel);
        return footerPanel;
    }

    private JPanel buildCustomerTable() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title and stats panel
        JPanel titlePanel = createTableTitlePanel("👥 Customer Management", "Manage all registered customers");

        // Get customers (preserving original functionality)
        AdminService service = new AdminService();
        List<Customer> customers = service.getAllCustomers();

        String[] col = {"Customer ID", "Full Name", "Email Address", "Phone Number"};
        Object[][] data = new Object[customers.size()][4];

        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            data[i][0] = c.getCustomerId();
            data[i][1] = c.getName();
            data[i][2] = c.getEmail();
            data[i][3] = c.getPhone();
        }

        JTable table = new JTable(data, col);
        styleTable(table, PRIMARY_COLOR);

        // Add customer count
        JLabel countLabel = new JLabel("Total Customers: " + customers.size());
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        countLabel.setForeground(PRIMARY_COLOR);
        titlePanel.add(countLabel);

        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel buildTransactionTable() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = createTableTitlePanel("📊 Transaction Management", "Monitor all system transactions");

        // Get transactions (preserving original functionality)
        AdminService service = new AdminService();
        List<Transaction> txns = service.getAllTransactions();

        String[] col = {"Transaction ID", "From Account", "To Account", "Amount", "Type", "Date", "Customer ID"};
        Object[][] data = new Object[txns.size()][7];

        for (int i = 0; i < txns.size(); i++) {
            Transaction t = txns.get(i);
            data[i][0] = t.getTransactionId();
            data[i][1] = t.getFromAccount();
            data[i][2] = t.getToAccount();
            data[i][3] = NumberFormat.getCurrencyInstance(Locale.US).format(t.getAmount());
            data[i][4] = t.getType();
            data[i][5] = t.getDate();
            data[i][6] = t.getCustomerId();
        }

        JTable table = new JTable(data, col);
        styleTable(table, ACCENT_COLOR);

        // Add transaction count
        JLabel countLabel = new JLabel("Total Transactions: " + txns.size());
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        countLabel.setForeground(ACCENT_COLOR);
        titlePanel.add(countLabel);

        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel buildInterestPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Create centered card
        JPanel cardContainer = new JPanel(new GridBagLayout());
        cardContainer.setBackground(BACKGROUND_COLOR);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(50, 40, 50, 40)
        ));
        card.setPreferredSize(new Dimension(500, 350));

        // Icon and title
        JLabel iconLabel = new JLabel("💰");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Interest Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("Apply monthly interest to all savings accounts");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_LIGHT);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Warning panel
        JPanel warningPanel = new JPanel();
        warningPanel.setLayout(new BoxLayout(warningPanel, BoxLayout.X_AXIS));
        warningPanel.setBackground(new Color(255, 243, 205));
        warningPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(WARNING_COLOR, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        warningPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel warningIcon = new JLabel("⚠️");
        warningIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        JLabel warningText = new JLabel("This action will apply interest to ALL savings accounts");
        warningText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        warningText.setForeground(new Color(133, 100, 4));

        warningPanel.add(warningIcon);
        warningPanel.add(Box.createHorizontalStrut(10));
        warningPanel.add(warningText);

        // Interest button
        JButton applyInterest = createStyledButton("💎 Apply Monthly Interest", SUCCESS_COLOR, SUCCESS_HOVER);
        applyInterest.setPreferredSize(new Dimension(280, 50));
        applyInterest.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Preserve original functionality
        applyInterest.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to apply monthly interest to all savings accounts?\n" +
                            "This action cannot be undone.",
                    "Confirm Interest Application",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                boolean applied = new InterestService().applyMonthlyInterest();
                showStyledMessage(
                        applied ? "✅ Interest applied successfully to all savings accounts." : "❌ Failed to apply interest.",
                        "Interest Application Status",
                        applied);
            }
        });

        // Assembly
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(descLabel);
        card.add(Box.createVerticalStrut(30));
        card.add(warningPanel);
        card.add(Box.createVerticalStrut(30));
        card.add(applyInterest);

        cardContainer.add(card);
        mainPanel.add(cardContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createTableTitlePanel(String title, String subtitle) {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_LIGHT);

        headerPanel.add(titleLabel);
        titlePanel.add(headerPanel);

        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel.setBackground(BACKGROUND_COLOR);
        subPanel.add(subtitleLabel);
        titlePanel.add(subPanel);

        return titlePanel;
    }

    private void styleTable(JTable table, Color accentColor) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(40);
        table.setBackground(CARD_COLOR);
        table.setForeground(TEXT_COLOR);
        table.setSelectionBackground(TAB_SELECTED);
        table.setSelectionForeground(TEXT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(accentColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));

        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Apply center alignment to ID columns
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        if (table.getColumnCount() > 6) { // Transaction table
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Amount
            table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Customer ID
        }
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(CARD_COLOR);
        scrollPane.setBackground(CARD_COLOR);
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setPreferredSize(new Dimension(250, 45));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        return button;
    }

    private void showStyledMessage(String message, String title, boolean isSuccess) {
        JOptionPane.showMessageDialog(this, message, title,
                isSuccess ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
}

