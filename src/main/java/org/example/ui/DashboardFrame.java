package org.example.ui;

import org.example.dao.AccountDAO;
import org.example.dao.TransactionDAO;
import org.example.model.Customer;
import org.example.model.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardFrame extends JFrame {
    private final Customer customer;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color PRIMARY_LIGHT = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color SUCCESS_HOVER = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color WARNING_HOVER = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color DANGER_HOVER = new Color(192, 57, 43);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    private static final Color BORDER_COLOR = new Color(220, 221, 222);
    private static final Color TAB_SELECTED = new Color(236, 240, 241);

    public DashboardFrame(Customer customer) {
        this.customer = customer;
        setTitle("Online Banking Dashboard - " + customer.getName());
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));

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
        tabs.add("📊 My Accounts", buildAccountsPanel());
        tabs.add("💰 Deposit", buildDepositPanel());
        tabs.add("💸 Withdraw", buildWithdrawPanel());
        tabs.add("🔄 Transfer", buildTransferPanel());
        tabs.add("📋 Transactions", buildTransactionPanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(900, 70));

        // Welcome section
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        welcomePanel.setBackground(PRIMARY_COLOR);

        JLabel welcomeIcon = new JLabel("👋");
        welcomeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel welcomeLabel = new JLabel("Welcome back, " + customer.getName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);

        welcomePanel.add(welcomeIcon);
        welcomePanel.add(welcomeLabel);

        // Quick actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        actionsPanel.setBackground(PRIMARY_COLOR);

        JButton logoutBtn = createHeaderButton("🚪 Logout", DANGER_COLOR);
        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        });

        actionsPanel.add(logoutBtn);

        headerPanel.add(welcomePanel, BorderLayout.WEST);
        headerPanel.add(actionsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createHeaderButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
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

    private JPanel buildAccountsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Account cards container
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsPanel.setBackground(BACKGROUND_COLOR);

        AccountDAO dao = new AccountDAO();
        var checking = dao.getCheckingAccount(customer.getCustomerId());
        var savings = dao.getSavingsAccount(customer.getCustomerId());

        // Checking account card
        JPanel checkingCard = createAccountCard("💳 Checking Account",
                checking != null ? String.valueOf(checking.getAccountNumber()) : "N/A",
                checking != null ? checking.getBalance() : 0.0,
                checking != null,
                SUCCESS_COLOR);

        // Savings account card
        JPanel savingsCard = createAccountCard("🏦 Savings Account",
                savings != null ? String.valueOf(savings.getAccountNumber()) : "N/A",
                savings != null ? savings.getBalance() : 0.0,
                savings != null,
                PRIMARY_COLOR);

        cardsPanel.add(checkingCard);
        cardsPanel.add(savingsCard);

        // Add interest rate info for savings if available
        if (savings != null) {
            JPanel interestPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            interestPanel.setBackground(BACKGROUND_COLOR);
            JLabel interestLabel = new JLabel(String.format("💎 Savings Interest Rate: %.2f%%", savings.getInterestRate()));
            interestLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            interestLabel.setForeground(PRIMARY_COLOR);
            interestPanel.add(interestLabel);

            mainPanel.add(interestPanel, BorderLayout.SOUTH);
        }

        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createAccountCard(String title, String accountNumber, double balance, boolean isActive, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(25, 20, 25, 20)
        ));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Account number
        JLabel accountLabel = new JLabel("Account: " + accountNumber);
        accountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accountLabel.setForeground(TEXT_LIGHT);
        accountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Balance
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        JLabel balanceLabel = new JLabel(currencyFormat.format(balance));
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        balanceLabel.setForeground(isActive ? TEXT_COLOR : TEXT_LIGHT);
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Status
        JLabel statusLabel = new JLabel(isActive ? "✅ Active" : "❌ Not Available");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(isActive ? SUCCESS_COLOR : TEXT_LIGHT);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(accountLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(balanceLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(statusLabel);

        return card;
    }

    private JPanel buildDepositPanel() {
        return createTransactionPanel("💰 Make a Deposit", "Deposit", SUCCESS_COLOR, (accField, amtField) -> {
            try {
                int acc = Integer.parseInt(accField.getText());
                double amt = Double.parseDouble(amtField.getText());
                boolean success = new TransactionDAO().deposit(acc, amt, customer.getCustomerId(), "Deposit");
                showStyledMessage(success ? "✅ Deposit Successful" : "❌ Failed to Deposit", success);
                if (success) {
                    accField.setText("");
                    amtField.setText("");
                }
            } catch (Exception ex) {
                showStyledMessage("Invalid input ❗", false);
            }
        });
    }

    private JPanel buildWithdrawPanel() {
        return createTransactionPanel("💸 Make a Withdrawal", "Withdraw", WARNING_COLOR, (accField, amtField) -> {
            try {
                int acc = Integer.parseInt(accField.getText());
                double amt = Double.parseDouble(amtField.getText());
                boolean success = new TransactionDAO().withdraw(acc, amt, customer.getCustomerId());
                showStyledMessage(success ? "✅ Withdrawal Successful" : "❌ Insufficient funds", success);
                if (success) {
                    accField.setText("");
                    amtField.setText("");
                }
            } catch (Exception ex) {
                showStyledMessage("Invalid input ❗", false);
            }
        });
    }

    private JPanel buildTransferPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Create form card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(40, 30, 40, 30)
        ));

        // Title
        JLabel titleLabel = new JLabel("🔄 Transfer Funds");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(30));

        // Form fields
        JTextField fromAcc = createStyledFormField("From Account Number");
        JTextField toAcc = createStyledFormField("To Account Number");
        JTextField amt = createStyledFormField("Transfer Amount");

        card.add(createFieldLabel("From Account #:"));
        card.add(fromAcc);
        card.add(Box.createVerticalStrut(20));

        card.add(createFieldLabel("To Account #:"));
        card.add(toAcc);
        card.add(Box.createVerticalStrut(20));

        card.add(createFieldLabel("Amount:"));
        card.add(amt);
        card.add(Box.createVerticalStrut(30));

        // Transfer button
        JButton transferBtn = createStyledButton("Transfer Funds", PRIMARY_COLOR, PRIMARY_LIGHT);
        card.add(transferBtn);

        // Preserve original functionality
        transferBtn.addActionListener(e -> {
            try {
                int from = Integer.parseInt(fromAcc.getText());
                int to = Integer.parseInt(toAcc.getText());
                double amount = Double.parseDouble(amt.getText());
                boolean success = new TransactionDAO().transfer(from, to, amount, customer.getCustomerId());
                showStyledMessage(success ? "✅ Transfer Successful" : "❌ Transfer failed", success);
                if (success) {
                    fromAcc.setText("");
                    toAcc.setText("");
                    amt.setText("");
                }
            } catch (Exception ex) {
                showStyledMessage("Invalid input ❗", false);
            }
        });

        mainPanel.add(card, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel buildTransactionPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("📋 Transaction History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titlePanel.add(titleLabel);

        // Get transactions (preserving original functionality)
        List<Transaction> txns = new TransactionDAO().getTransactions(customer.getCustomerId());

        String[] col = {"Type", "From Account", "To Account", "Amount", "Date"};
        Object[][] data = new Object[txns.size()][5];
        for (int i = 0; i < txns.size(); i++) {
            Transaction t = txns.get(i);
            data[i][0] = t.getType();
            data[i][1] = t.getFromAccount();
            data[i][2] = t.getToAccount();
            data[i][3] = NumberFormat.getCurrencyInstance(Locale.US).format(t.getAmount());
            data[i][4] = t.getDate().toString();
        }

        // Create styled table
        JTable table = new JTable(data, col);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(CARD_COLOR);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
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
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        // Center align amount column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    }

    private JPanel createTransactionPanel(String title, String buttonText, Color buttonColor, TransactionAction action) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Create form card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(40, 30, 40, 30)
        ));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(buttonColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(30));

        // Form fields
        JTextField accField = createStyledFormField("Account Number");
        JTextField amtField = createStyledFormField("Amount");

        card.add(createFieldLabel("Account Number:"));
        card.add(accField);
        card.add(Box.createVerticalStrut(20));

        card.add(createFieldLabel("Amount:"));
        card.add(amtField);
        card.add(Box.createVerticalStrut(30));

        // Action button
        JButton actionBtn = createStyledButton(buttonText, buttonColor, buttonColor.brighter());
        card.add(actionBtn);

        // Preserve original functionality
        actionBtn.addActionListener(e -> action.execute(accField, amtField));

        mainPanel.add(card, BorderLayout.CENTER);
        return mainPanel;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 5, 5, 0));
        return label;
    }

    private JTextField createStyledFormField(String placeholder) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 45));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);

        // Focus effects
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        new EmptyBorder(11, 14, 11, 14)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });

        return field;
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

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setPreferredSize(new Dimension(200, 45));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);

        return button;
    }

    private void showStyledMessage(String message, boolean isSuccess) {
        JOptionPane.showMessageDialog(this, message,
                isSuccess ? "Success" : "Error",
                isSuccess ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    @FunctionalInterface
    private interface TransactionAction {
        void execute(JTextField accField, JTextField amtField);
    }
}


