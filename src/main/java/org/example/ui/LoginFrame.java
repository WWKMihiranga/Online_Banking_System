package org.example.ui;

import org.example.dao.CustomerDAO;
import org.example.model.Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color PRIMARY_HOVER = new Color(52, 152, 219);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color DANGER_HOVER = new Color(192, 57, 43);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color PLACEHOLDER_COLOR = new Color(149, 165, 166);
    private static final Color BORDER_COLOR = new Color(220, 221, 222);

    public LoginFrame() {
        setTitle("Online Bank - Secure Login");
        setSize(420, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

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
        // Main container with background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Header panel
        JPanel headerPanel = createHeaderPanel();

        // Login card panel
        JPanel cardPanel = createLoginCard();

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(420, 80));
        headerPanel.setLayout(new BorderLayout());

        // Bank icon and title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        titlePanel.setBackground(PRIMARY_COLOR);

        // Bank icon (using Unicode symbol)
        JLabel iconLabel = new JLabel("🏦");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel titleLabel = new JLabel("Online Banking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createLoginCard() {
        JPanel cardContainer = new JPanel();
        cardContainer.setLayout(new GridBagLayout());
        cardContainer.setBackground(BACKGROUND_COLOR);
        cardContainer.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Login card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(40, 30, 40, 30)
        ));

        // Add shadow effect (simulated with multiple borders)
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 20)),
                        BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 0, 0, 10))
                ),
                new EmptyBorder(40, 30, 40, 30)
        ));

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(welcomeLabel);

        JLabel subtitleLabel = new JLabel("Please sign in to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(PLACEHOLDER_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);

        card.add(Box.createVerticalStrut(30));

        // Email field
        card.add(createFieldLabel("Email Address"));
        emailField = createStyledTextField("Enter your email");
        card.add(emailField);
        card.add(Box.createVerticalStrut(20));

        // Password field
        card.add(createFieldLabel("Password"));
        passwordField = createStyledPasswordField("Enter your password");
        card.add(passwordField);
        card.add(Box.createVerticalStrut(30));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(CARD_COLOR);

        JButton loginBtn = createStyledButton("Sign In", PRIMARY_COLOR, PRIMARY_HOVER, true);
        JButton exitBtn = createStyledButton("Exit", DANGER_COLOR, DANGER_HOVER, false);

        buttonPanel.add(loginBtn);
        buttonPanel.add(exitBtn);
        card.add(buttonPanel);

        // Event listeners (preserving original functionality)
        loginBtn.addActionListener(e -> handleLogin());
        exitBtn.addActionListener(e -> System.exit(0));

        // Enter key support
        getRootPane().setDefaultButton(loginBtn);

        cardContainer.add(card);
        return cardContainer;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 5, 5, 0));
        return label;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        styleTextField(field, placeholder);
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        styleTextField(field, placeholder);
        return field;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setPreferredSize(new Dimension(300, 45));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);

        // Placeholder functionality
        field.setText(placeholder);
        field.setForeground(PLACEHOLDER_COLOR);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        new EmptyBorder(11, 14, 11, 14)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(PLACEHOLDER_COLOR);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor, boolean isPrimary) {
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

        button.setPreferredSize(new Dimension(300, 45));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);

        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });

        return button;
    }

    // Original login functionality preserved exactly
    private void handleLogin() {
        // Get text, handling placeholders
        String email = emailField.getText();
        if (email.equals("Enter your email")) {
            email = "";
        }

        String password = new String(passwordField.getPassword());
        if (password.equals("Enter your password")) {
            password = "";
        }
        new AdminDashboardFrame("admin.getUsername()");

        // Admin shortcut (username-only)
        if (email.equals("admin")) {
            var adminDAO = new org.example.dao.AdminDAO();
            var admin = adminDAO.login("admin", password);
            if (admin != null) {
                dispose();
                new AdminDashboardFrame(admin.getUsername());
                return;
            } else {
                showStyledErrorMessage("Admin login failed ❌");
                return;
            }
        }

        // Regular customer login
        CustomerDAO dao = new CustomerDAO();
        Customer customer = dao.login(email, password);

        if (customer != null) {
            dispose();
            new DashboardFrame(customer);
        } else {
            showStyledErrorMessage("Login failed ❌");
        }
    }

    private void showStyledErrorMessage(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "Authentication Error");
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}


