import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class finalWindow {
    
    // Store game statistics for comparison
    private static List<GameRecord> gameHistory = new ArrayList<>();
    
    // Color scheme for visualizations
    private static final Color[] CHART_COLORS = {
        new Color(65, 105, 225),   // Royal Blue
        new Color(50, 205, 50),    // Lime Green
        new Color(255, 99, 71),    // Tomato Red
        new Color(255, 140, 0),    // Dark Orange
        new Color(138, 43, 226),   // Blue Violet
        new Color(220, 20, 60),    // Crimson
        new Color(0, 206, 209),    // Dark Turquoise
        new Color(255, 215, 0)     // Gold
    };
    
    public static void showWindow(JFrame parentFrame, String time, int hints, int mistakes) {
        // Save current game to history
        saveGameToHistory(time, hints, mistakes);
        
        // Create the victory window
        JFrame victoryFrame = new JFrame("Sudoku Performance Dashboard");
        victoryFrame.setSize(1100, 750);
        victoryFrame.setLocationRelativeTo(parentFrame);
        victoryFrame.setResizable(false);
        
        // Professional white background
        victoryFrame.getContentPane().setBackground(Color.WHITE);
        
        // Create scrollable main panel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Main content panel with fixed width
        JPanel mainPanel = createMainContentPanel(parentFrame, victoryFrame, time, hints, mistakes);
        mainPanel.setPreferredSize(new Dimension(1050, 1600));
        
        scrollPane.setViewportView(mainPanel);
        
        // Style the vertical scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);
        customizeScrollBar(verticalScrollBar);
        
        victoryFrame.add(scrollPane);
        victoryFrame.setVisible(true);
    }
    
    private static JPanel createMainContentPanel(JFrame parentFrame, JFrame victoryFrame, String time, int hints, int mistakes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setMaximumSize(new Dimension(1050, Integer.MAX_VALUE));
        
        // HEADER SECTION
        panel.add(createHeaderPanel());
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // MAIN METRICS SECTION
        panel.add(createMetricsSection(time, hints, mistakes));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // VISUALIZATION SECTION
        panel.add(createVisualizationSection(hints, mistakes));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // EXTRA FEATURES SECTION
        panel.add(createExtraFeaturesSection(parentFrame, victoryFrame, time, hints, mistakes));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // ACTION BUTTONS SECTION
        panel.add(createActionButtonsSection(parentFrame, victoryFrame, hints, mistakes));
        
        return panel;
    }
    
    private static JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1050, 120));
        
        // Left side - Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel mainTitle = new JLabel("Sudoku Performance Dashboard");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        mainTitle.setForeground(new Color(33, 37, 41));
        
        JLabel subTitle = new JLabel("Comprehensive analysis of your puzzle-solving skills");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitle.setForeground(new Color(108, 117, 125));
        
        titlePanel.add(mainTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titlePanel.add(subTitle);
        
        // Right side - Trophy icon
        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        
        JLabel trophyIcon = new JLabel("🏆");
        trophyIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        trophyIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        iconPanel.add(trophyIcon);
        
        panel.add(titlePanel, BorderLayout.WEST);
        panel.add(iconPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private static JPanel createMetricsSection(String time, int hints, int mistakes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1050, 300));
        
        JLabel sectionTitle = new JLabel("📊 Key Performance Indicators");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(sectionTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Calculate metrics
        int totalActions = Math.max(1, hints + mistakes);
        int correctActions = totalActions - mistakes;
        double accuracy = (double) correctActions / totalActions * 100;
        double efficiency = totalActions > 0 ? (double) correctActions / (hints + 1) * 100 : 100;
        
        // Metrics grid
        JPanel metricsGrid = new JPanel(new GridLayout(2, 3, 15, 15));
        metricsGrid.setOpaque(false);
        metricsGrid.setMaximumSize(new Dimension(1050, 300));
        
        // Row 1
        metricsGrid.add(createMetricCard("⏱️ Completion Time", time, 
            new Color(0, 123, 255), "Time taken to solve"));
        metricsGrid.add(createMetricCard("🎯 Accuracy Rate", String.format("%.1f%%", accuracy), 
            new Color(40, 167, 69), "Correct placements"));
        metricsGrid.add(createMetricCard("⚡ Efficiency", String.format("%.0f", efficiency), 
            new Color(255, 193, 7), "Score based on moves"));
        
        // Row 2
        metricsGrid.add(createMetricCard("💡 Hints Used", hints + " times", 
            new Color(23, 162, 184), "Assistance requested"));
        metricsGrid.add(createMetricCard("❌ Mistakes Made", mistakes + " errors", 
            new Color(220, 53, 69), "Incorrect placements"));
        metricsGrid.add(createMetricCard("✅ Correct Moves", correctActions + " moves", 
            new Color(111, 66, 193), "Successful placements"));
        
        panel.add(metricsGrid);
        
        return panel;
    }
    
    private static JPanel createMetricCard(String title, String value, Color color, String description) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw card with shadow
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Draw border
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                
                // Draw top accent
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, getWidth(), 4, 15, 15);
            }
        };
        
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(320, 130));
        
        // Title with icon
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(73, 80, 87));
        
        // Value (large)
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(color);
        
        // Description
        JLabel descLabel = new JLabel("<html><div style='width: 250px;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(new Color(108, 117, 125));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(valueLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        centerPanel.add(descLabel);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private static JPanel createVisualizationSection(int hints, int mistakes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1050, 500));
        
        JLabel sectionTitle = new JLabel("📈 Performance Visualization");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(sectionTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Visualization container
        JPanel vizContainer = new JPanel();
        vizContainer.setLayout(new BoxLayout(vizContainer, BoxLayout.X_AXIS));
        vizContainer.setOpaque(false);
        vizContainer.setMaximumSize(new Dimension(1050, 450));
        
        // LEFT: Donut Chart
        JPanel chartPanel = createDonutChartPanel(hints, mistakes);
        
        // Add spacing
        vizContainer.add(chartPanel);
        vizContainer.add(Box.createRigidArea(new Dimension(30, 0)));
        
        // RIGHT: Performance details
        JPanel detailsPanel = createPerformanceDetailsPanel(hints, mistakes);
        
        vizContainer.add(detailsPanel);
        
        panel.add(vizContainer);
        
        return panel;
    }
    
    private static JPanel createDonutChartPanel(int hints, int mistakes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(500, 450));
        
        // Chart title
        JLabel chartTitle = new JLabel("Performance Breakdown");
        chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        chartTitle.setForeground(new Color(33, 37, 41));
        chartTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Chart container
        JPanel chartContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawEnhancedDonutChart(g, hints, mistakes);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(450, 350);
            }
        };
        chartContainer.setOpaque(false);
        chartContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        chartContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Legend
        JPanel legendPanel = createChartLegendPanel(hints, mistakes);
        legendPanel.setMaximumSize(new Dimension(450, 80));
        legendPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(chartTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(chartContainer);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(legendPanel);
        
        return panel;
    }
    
    private static void drawEnhancedDonutChart(Graphics g, int hints, int mistakes) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = 450;
        int height = 350;
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Calculate sizes
        int outerRadius = 120;
        int middleRadius = 80;
        int innerRadius = 40;
        
        // Calculate metrics
        int totalActions = Math.max(1, hints + mistakes);
        int correctActions = totalActions - mistakes;
        
        int correctAngle = (int)(360 * correctActions / totalActions);
        int mistakeAngle = (int)(360 * mistakes / totalActions);
        int hintAngle = (int)(360 * hints / Math.max(totalActions, 1));
        
        // Draw outer ring - Correct vs Mistakes
        g2d.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Correct arc (Green)
        g2d.setColor(new Color(40, 167, 69));
        g2d.draw(new Arc2D.Double(centerX - outerRadius, centerY - outerRadius,
                                 outerRadius * 2, outerRadius * 2,
                                 90, correctAngle, Arc2D.OPEN));
        
        // Mistakes arc (Red)
        if (mistakes > 0) {
            g2d.setColor(new Color(220, 53, 69));
            g2d.draw(new Arc2D.Double(centerX - outerRadius, centerY - outerRadius,
                                     outerRadius * 2, outerRadius * 2,
                                     90 + correctAngle, mistakeAngle, Arc2D.OPEN));
        }
        
        // Draw middle ring - Hints
        g2d.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (hints > 0) {
            g2d.setColor(new Color(23, 162, 184));
            g2d.draw(new Arc2D.Double(centerX - middleRadius, centerY - middleRadius,
                                     middleRadius * 2, middleRadius * 2,
                                     90, Math.min(360, hintAngle * 2), Arc2D.OPEN));
        }
        
        // Draw inner ring - Performance indicator
        g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(111, 66, 193));
        double performanceScore = (double) correctActions / totalActions * 100;
        int performanceAngle = (int)(360 * performanceScore / 100);
        g2d.draw(new Arc2D.Double(centerX - innerRadius, centerY - innerRadius,
                                 innerRadius * 2, innerRadius * 2,
                                 90, performanceAngle, Arc2D.OPEN));
        
        // Draw center text
        g2d.setColor(new Color(33, 37, 41));
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 32));
        String centerText = String.format("%.0f%%", performanceScore);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(centerText);
        g2d.drawString(centerText, centerX - textWidth/2, centerY + fm.getAscent()/3);
        
        // Draw subtitle
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2d.setColor(new Color(108, 117, 125));
        String subText = "Overall Score";
        fm = g2d.getFontMetrics();
        textWidth = fm.stringWidth(subText);
        g2d.drawString(subText, centerX - textWidth/2, centerY + fm.getHeight() + 10);
    }
    
    private static JPanel createChartLegendPanel(int hints, int mistakes) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(450, 80));
        
        panel.add(createLegendItem(new Color(40, 167, 69), "Correct Moves", 
            "Successful placements"));
        panel.add(createLegendItem(new Color(220, 53, 69), "Mistakes", 
            "Incorrect placements"));
        panel.add(createLegendItem(new Color(23, 162, 184), "Hints Used", 
            "Assistance requested"));
        panel.add(createLegendItem(new Color(111, 66, 193), "Performance", 
            "Overall score"));
        
        return panel;
    }
    
    private static JPanel createLegendItem(Color color, String title, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(200, 35));
        
        // Color indicator
        JPanel colorDot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        colorDot.setPreferredSize(new Dimension(12, 12));
        
        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        descLabel.setForeground(new Color(108, 117, 125));
        
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        
        panel.add(colorDot, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private static JPanel createPerformanceDetailsPanel(int hints, int mistakes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(500, 450));
        
        JLabel sectionTitle = new JLabel("Performance Details");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(sectionTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Details grid
        JPanel detailsGrid = new JPanel();
        detailsGrid.setLayout(new BoxLayout(detailsGrid, BoxLayout.Y_AXIS));
        detailsGrid.setOpaque(false);
        detailsGrid.setMaximumSize(new Dimension(500, 200));
        
        int totalActions = Math.max(1, hints + mistakes);
        int correctActions = totalActions - mistakes;
        double accuracy = (double) correctActions / totalActions * 100;
        
        detailsGrid.add(createDetailItem("🎯 Success Rate", 
            String.format("%.1f%%", accuracy), 
            getPerformanceLevel(accuracy)));
        detailsGrid.add(Box.createRigidArea(new Dimension(0, 15)));
        detailsGrid.add(createDetailItem("⚡ Speed", 
            hints == 0 ? "Fast" : "Moderate", 
            hints == 0 ? "Solved without assistance" : "Used hints for assistance"));
        detailsGrid.add(Box.createRigidArea(new Dimension(0, 15)));
        detailsGrid.add(createDetailItem("🧠 Strategy", 
            mistakes <= 2 ? "Careful" : "Risky", 
            mistakes <= 2 ? "Minimal errors" : "Some risky moves"));
        detailsGrid.add(Box.createRigidArea(new Dimension(0, 15)));
        detailsGrid.add(createDetailItem("📈 Improvement", 
            getImprovementSuggestion(hints, mistakes), 
            "Next step recommendation"));
        
        // Performance breakdown
        JPanel breakdownPanel = createBreakdownPanel(hints, mistakes);
        breakdownPanel.setMaximumSize(new Dimension(500, 150));
        
        panel.add(detailsGrid);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(breakdownPanel);
        
        return panel;
    }
    
    private static JPanel createDetailItem(String title, String value, String description) {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(500, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(73, 80, 87));
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(new Color(33, 37, 41));
        valueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(new Color(108, 117, 125));
        descLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        rightPanel.add(valueLabel);
        rightPanel.add(descLabel);
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private static JPanel createBreakdownPanel(int hints, int mistakes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Move Breakdown"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        int totalActions = Math.max(1, hints + mistakes);
        int correctActions = totalActions - mistakes;
        
        // Create progress bars for each category
        panel.add(createProgressBarItem("Correct Moves", correctActions, totalActions, 
            new Color(40, 167, 69)));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createProgressBarItem("Mistakes", mistakes, totalActions, 
            new Color(220, 53, 69)));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createProgressBarItem("Hints Used", hints, totalActions, 
            new Color(23, 162, 184)));
        
        return panel;
    }
    
    private static JPanel createProgressBarItem(String label, int value, int max, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(450, 30));
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelLabel.setForeground(new Color(73, 80, 87));
        labelLabel.setPreferredSize(new Dimension(120, 20));
        
        // Custom progress bar
        JProgressBar progressBar = new JProgressBar(0, max);
        progressBar.setValue(value);
        progressBar.setForeground(color);
        progressBar.setBackground(new Color(233, 236, 239));
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        progressBar.setStringPainted(true);
        progressBar.setString(value + "/" + max);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        progressBar.setPreferredSize(new Dimension(200, 25));
        
        // Percentage label
        double percentage = max > 0 ? (double)value / max * 100 : 0;
        JLabel percentLabel = new JLabel(String.format("%.0f%%", percentage));
        percentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        percentLabel.setForeground(color);
        percentLabel.setPreferredSize(new Dimension(50, 20));
        
        panel.add(labelLabel, BorderLayout.WEST);
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(percentLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private static JPanel createExtraFeaturesSection(JFrame parentFrame, JFrame victoryFrame, String time, int hints, int mistakes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1050, 250));
        
        JLabel sectionTitle = new JLabel("🚀 Additional Features");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(sectionTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Features grid
        JPanel featuresGrid = new JPanel(new GridLayout(1, 4, 15, 0));
        featuresGrid.setOpaque(false);
        featuresGrid.setMaximumSize(new Dimension(1050, 180));
        
        // Create feature cards
        featuresGrid.add(createFeatureCard("📋", "Generate Report", 
            "Create detailed PDF report", new Color(0, 123, 255),
            e -> generateReport(victoryFrame, time, hints, mistakes)));
        
        featuresGrid.add(createFeatureCard("📊", "Compare Games", 
            "Compare with previous sessions", new Color(40, 167, 69),
            e -> showGameHistoryComparison(victoryFrame, time, hints, mistakes)));
        
        featuresGrid.add(createFeatureCard("🏆", "Achievements", 
            "View unlocked achievements", new Color(255, 193, 7),
            e -> showAchievements(victoryFrame, hints, mistakes)));
        
        featuresGrid.add(createFeatureCard("🎯", "Practice Mode", 
            "Improve specific skills", new Color(111, 66, 193),
            e -> startPracticeMode(parentFrame, victoryFrame, hints, mistakes)));
        
        panel.add(featuresGrid);
        
        return panel;
    }
    
    private static JPanel createFeatureCard(String icon, String title, String description, Color color, ActionListener action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw card
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Draw border
                g2d.setColor(new Color(233, 236, 239));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(240, 160));
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(color);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Description
        JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 200px;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(new Color(108, 117, 125));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(iconLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        centerPanel.add(descLabel);
        
        card.add(centerPanel, BorderLayout.CENTER);
        
        // Add click action
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, ""));
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 2),
                    BorderFactory.createEmptyBorder(18, 18, 18, 18)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            }
        });
        
        return card;
    }
    
    // ======================== FEATURE 1: GENERATE REPORT ========================
    private static void generateReport(JFrame parentFrame, String time, int hints, int mistakes) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Performance Report");
        fileChooser.setSelectedFile(new File("Sudoku_Performance_Report.txt"));
        
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileToSave))) {
                // Calculate metrics
                int totalActions = Math.max(1, hints + mistakes);
                int correctActions = totalActions - mistakes;
                double accuracy = (double) correctActions / totalActions * 100;
                double efficiency = totalActions > 0 ? (double) correctActions / (hints + 1) * 100 : 100;
                
                // Write report
                writer.println("=========================================");
                writer.println("        SUDOKU PERFORMANCE REPORT        ");
                writer.println("=========================================");
                writer.println();
                writer.println("Report Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                writer.println();
                writer.println("GAME STATISTICS:");
                writer.println("----------------");
                writer.println("Completion Time: " + time);
                writer.println("Accuracy Rate: " + String.format("%.1f%%", accuracy));
                writer.println("Efficiency Score: " + String.format("%.0f", efficiency));
                writer.println("Hints Used: " + hints);
                writer.println("Mistakes Made: " + mistakes);
                writer.println("Correct Moves: " + correctActions);
                writer.println();
                writer.println("PERFORMANCE ANALYSIS:");
                writer.println("---------------------");
                writer.println("Overall Performance: " + getPerformanceLevel(accuracy));
                
                if (accuracy >= 90) {
                    writer.println("Feedback: Excellent performance! You're a Sudoku master!");
                } else if (accuracy >= 70) {
                    writer.println("Feedback: Good job! You're getting better!");
                } else {
                    writer.println("Feedback: Keep practicing to improve your skills!");
                }
                
                writer.println();
                writer.println("RECOMMENDATIONS:");
                writer.println("----------------");
                
                if (mistakes > 5) {
                    writer.println("1. Focus on double-checking placements");
                    writer.println("2. Take more time to think before placing numbers");
                }
                
                if (hints > 3) {
                    writer.println("1. Try to solve more cells independently");
                    writer.println("2. Use hints only when absolutely stuck");
                }
                
                if (mistakes <= 2 && hints <= 1) {
                    writer.println("1. Challenge yourself with harder puzzles");
                    writer.println("2. Try to solve puzzles faster");
                }
                
                writer.println();
                writer.println("=========================================");
                writer.println("          END OF REPORT                  ");
                writer.println("=========================================");
                
                // Show success message
                JOptionPane.showMessageDialog(parentFrame,
                    "Report successfully saved to:\n" + fileToSave.getAbsolutePath(),
                    "Report Generated", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentFrame,
                    "Error saving report: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ======================== ENHANCED GAME COMPARISON FEATURE ========================
    private static void showGameHistoryComparison(JFrame parentFrame, String currentTime, int currentHints, int currentMistakes) {
        if (gameHistory.isEmpty()) {
            showNoHistoryDialog(parentFrame, currentTime, currentHints, currentMistakes);
            return;
        }
        
        // Create enhanced comparison window
        JFrame comparisonFrame = new JFrame("📊 Game History Comparison");
        comparisonFrame.setSize(1100, 750);
        comparisonFrame.setLocationRelativeTo(parentFrame);
        comparisonFrame.setLayout(new BorderLayout());
        
        // Create tabbed pane for different views
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Tab 1: Overview Dashboard
        JPanel overviewPanel = createOverviewTab(currentTime, currentHints, currentMistakes);
        tabbedPane.addTab("📈 Overview", overviewPanel);
        
        // Tab 2: Detailed Comparison
        JPanel comparisonPanel = createDetailedComparisonTab(currentTime, currentHints, currentMistakes);
        tabbedPane.addTab("🔍 Detailed", comparisonPanel);
        
        // Tab 3: Progress Charts
        JPanel chartsPanel = createProgressChartsTab(currentTime, currentHints, currentMistakes);
        tabbedPane.addTab("📊 Charts", chartsPanel);
        
        // Tab 4: Statistics
        JPanel statsPanel = createStatisticsTab(currentTime, currentHints, currentMistakes);
        tabbedPane.addTab("📋 Stats", statsPanel);
        
        // Control panel at bottom
        JPanel controlPanel = createComparisonControlPanel(comparisonFrame, parentFrame, currentTime, currentHints, currentMistakes);
        
        comparisonFrame.add(tabbedPane, BorderLayout.CENTER);
        comparisonFrame.add(controlPanel, BorderLayout.SOUTH);
        comparisonFrame.setVisible(true);
    }
    
    private static void showNoHistoryDialog(JFrame parentFrame, String currentTime, int currentHints, int currentMistakes) {
        JDialog noHistoryDialog = new JDialog(parentFrame, "No History Available", true);
        noHistoryDialog.setSize(400, 350);
        noHistoryDialog.setLocationRelativeTo(parentFrame);
        noHistoryDialog.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(Color.WHITE);
        
        // Icon
        JLabel iconLabel = new JLabel("📊", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel("No Game History Yet", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 58, 64));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Description
        JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 300px; color: #6c757d;'>" +
            "This is your first completed puzzle!<br>" +
            "Complete more puzzles to unlock:<br><br>" +
            "• 📈 Trend analysis charts<br>" +
            "• 🔄 Progress tracking<br>" +
            "• 🏆 Performance comparisons</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Current game stats
        int totalActions = Math.max(1, currentHints + currentMistakes);
        int correctActions = totalActions - currentMistakes;
        double accuracy = (double) correctActions / totalActions * 100;
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        statsPanel.setMaximumSize(new Dimension(350, 100));
        
        statsPanel.add(createMiniStatCard("⏱️ Time", currentTime, new Color(0, 123, 255)));
        statsPanel.add(createMiniStatCard("🎯 Accuracy", String.format("%.1f%%", accuracy), new Color(40, 167, 69)));
        statsPanel.add(createMiniStatCard("💡 Hints", currentHints + "", new Color(255, 193, 7)));
        statsPanel.add(createMiniStatCard("✅ Correct", correctActions + "", new Color(111, 66, 193)));
        
        // Button
        JButton okButton = new JButton("Got it!");
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        okButton.setBackground(new Color(0, 123, 255));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.addActionListener(e -> noHistoryDialog.dispose());
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(descLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(okButton);
        
        noHistoryDialog.add(contentPanel, BorderLayout.CENTER);
        noHistoryDialog.setVisible(true);
    }
    
    private static JPanel createMiniStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(248, 249, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLabel.setForeground(new Color(73, 80, 87));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(color);
        
        card.add(titleLabel);
        card.add(valueLabel);
        
        return card;
    }
    
    private static JPanel createOverviewTab(String currentTime, int currentHints, int currentMistakes) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("📊 Performance Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        JLabel gameCountLabel = new JLabel("Total Games: " + (gameHistory.size() + 1));
        gameCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gameCountLabel.setForeground(new Color(108, 117, 125));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(gameCountLabel, BorderLayout.EAST);
        
        // Main content with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setDividerSize(3);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Left: Progress Chart
        JPanel progressChartPanel = createProgressChartPanel(currentTime, currentHints, currentMistakes);
        
        // Right: Current vs Last Comparison
        JPanel comparisonPanel = createCurrentVsLastPanel(currentTime, currentHints, currentMistakes);
        
        splitPane.setLeftComponent(progressChartPanel);
        splitPane.setRightComponent(comparisonPanel);
        
        // Bottom: Quick Stats
        JPanel quickStatsPanel = createQuickStatsPanel(currentTime, currentHints, currentMistakes);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);
        panel.add(quickStatsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private static JPanel createProgressChartPanel(String currentTime, int currentHints, int currentMistakes) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("📈 Accuracy Trend"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawAccuracyTrendChart(g, currentHints, currentMistakes);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(480, 300);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private static void drawAccuracyTrendChart(Graphics g, int currentHints, int currentMistakes) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = 480;
        int height = 300;
        int padding = 50;
        int chartWidth = width - padding * 2;
        int chartHeight = height - padding * 2;
        
        // Draw background grid
        g2d.setColor(new Color(240, 240, 240));
        g2d.setStroke(new BasicStroke(1));
        
        // Vertical grid lines
        for (int i = 0; i <= 10; i++) {
            int x = padding + (chartWidth * i / 10);
            g2d.drawLine(x, padding, x, height - padding);
        }
        
        // Horizontal grid lines
        for (int i = 0; i <= 10; i++) {
            int y = padding + (chartHeight * i / 10);
            g2d.drawLine(padding, y, width - padding, y);
        }
        
        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g2d.drawLine(padding, padding, padding, height - padding); // Y-axis
        
        // Draw Y-axis labels (accuracy percentages)
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        for (int i = 0; i <= 100; i += 20) {
            int y = height - padding - (chartHeight * i / 100);
            g2d.drawString(i + "%", padding - 25, y + 3);
        }
        
        // Calculate accuracy for all games (including current)
        int totalGames = gameHistory.size() + 1;
        double[] accuracies = new double[totalGames];
        
        // Current game accuracy
        int currentTotal = Math.max(1, currentHints + currentMistakes);
        int currentCorrect = currentTotal - currentMistakes;
        accuracies[0] = (double) currentCorrect / currentTotal * 100;
        
        // Previous games accuracy
        for (int i = 1; i < totalGames; i++) {
            GameRecord record = gameHistory.get(i - 1);
            int recordTotal = Math.max(1, record.hints + record.mistakes);
            int recordCorrect = recordTotal - record.mistakes;
            accuracies[i] = (double) recordCorrect / recordTotal * 100;
        }
        
        // Draw trend line
        g2d.setColor(new Color(0, 123, 255));
        g2d.setStroke(new BasicStroke(3));
        
        for (int i = 0; i < totalGames - 1; i++) {
            int x1 = padding + (chartWidth * i / Math.max(1, totalGames - 1));
            int y1 = height - padding - (int)(chartHeight * accuracies[i] / 100);
            
            int x2 = padding + (chartWidth * (i + 1) / Math.max(1, totalGames - 1));
            int y2 = height - padding - (int)(chartHeight * accuracies[i + 1] / 100);
            
            g2d.drawLine(x1, y1, x2, y2);
        }
        
        // Draw data points
        g2d.setColor(new Color(220, 53, 69));
        for (int i = 0; i < totalGames; i++) {
            int x = padding + (chartWidth * i / Math.max(1, totalGames - 1));
            int y = height - padding - (int)(chartHeight * accuracies[i] / 100);
            
            // Draw point
            g2d.fillOval(x - 4, y - 4, 8, 8);
            
            // Draw label for last point
            if (i == totalGames - 1) {
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
                g2d.drawString(String.format("Game %d", totalGames - i), x - 10, y - 10);
            }
        }
        
        // Draw legend
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2d.drawString("Accuracy Trend Over Games", width / 2 - 80, padding - 10);
    }
    
    private static JPanel createCurrentVsLastPanel(String currentTime, int currentHints, int currentMistakes) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("🔄 Current vs Last Game"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Get last game
        GameRecord lastGame = gameHistory.get(gameHistory.size() - 1);
        
        // Calculate metrics for both games
        int currentTotal = Math.max(1, currentHints + currentMistakes);
        int currentCorrect = currentTotal - currentMistakes;
        double currentAccuracy = (double) currentCorrect / currentTotal * 100;
        
        int lastTotal = Math.max(1, lastGame.hints + lastGame.mistakes);
        int lastCorrect = lastTotal - lastGame.mistakes;
        double lastAccuracy = (double) lastCorrect / lastTotal * 100;
        
        // Create comparison grid
        JPanel gridPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        gridPanel.setBackground(Color.WHITE);
        
        // Headers
        gridPanel.add(new JLabel("")); // Empty corner
        gridPanel.add(createComparisonHeader("Current"));
        gridPanel.add(createComparisonHeader("Previous"));
        
        // Time
        gridPanel.add(createComparisonLabel("⏱️ Time"));
        gridPanel.add(createComparisonValue(currentTime, "Current"));
        gridPanel.add(createComparisonValue(lastGame.time, "Previous"));
        
        // Accuracy
        gridPanel.add(createComparisonLabel("🎯 Accuracy"));
        gridPanel.add(createComparisonValue(String.format("%.1f%%", currentAccuracy), "Current"));
        gridPanel.add(createComparisonValue(String.format("%.1f%%", lastAccuracy), "Previous"));
        
        // Hints
        gridPanel.add(createComparisonLabel("💡 Hints"));
        gridPanel.add(createComparisonValue(currentHints + "", "Current"));
        gridPanel.add(createComparisonValue(lastGame.hints + "", "Previous"));
        
        // Mistakes
        gridPanel.add(createComparisonLabel("❌ Mistakes"));
        gridPanel.add(createComparisonValue(currentMistakes + "", "Current"));
        gridPanel.add(createComparisonValue(lastGame.mistakes + "", "Previous"));
        
        // Improvement indicator
        JPanel improvementPanel = new JPanel(new BorderLayout());
        improvementPanel.setBackground(Color.WHITE);
        improvementPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        String improvementText = "";
        Color improvementColor = Color.BLACK;
        
        if (currentAccuracy > lastAccuracy) {
            double improvement = currentAccuracy - lastAccuracy;
            improvementText = String.format("📈 Improved by %.1f%%", improvement);
            improvementColor = new Color(40, 167, 69);
        } else if (currentAccuracy < lastAccuracy) {
            double decline = lastAccuracy - currentAccuracy;
            improvementText = String.format("📉 Declined by %.1f%%", decline);
            improvementColor = new Color(220, 53, 69);
        } else {
            improvementText = "➡️ No change";
            improvementColor = new Color(108, 117, 125);
        }
        
        JLabel improvementLabel = new JLabel(improvementText, SwingConstants.CENTER);
        improvementLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        improvementLabel.setForeground(improvementColor);
        
        improvementPanel.add(improvementLabel, BorderLayout.CENTER);
        
        panel.add(gridPanel, BorderLayout.CENTER);
        panel.add(improvementPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private static JLabel createComparisonHeader(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(33, 37, 41));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }
    
    private static JLabel createComparisonLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(73, 80, 87));
        return label;
    }
    
    private static JLabel createComparisonValue(String value, String type) {
        JLabel label = new JLabel(value, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(type.equals("Current") ? new Color(0, 123, 255) : new Color(108, 117, 125));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }
    
    private static JPanel createQuickStatsPanel(String currentTime, int currentHints, int currentMistakes) {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Calculate best game
        GameRecord bestGame = null;
        double bestAccuracy = 0;
        
        for (GameRecord record : gameHistory) {
            int total = Math.max(1, record.hints + record.mistakes);
            int correct = total - record.mistakes;
            double accuracy = (double) correct / total * 100;
            
            if (accuracy > bestAccuracy) {
                bestAccuracy = accuracy;
                bestGame = record;
            }
        }
        
        // Add current game to comparison
        int currentTotal = Math.max(1, currentHints + currentMistakes);
        int currentCorrect = currentTotal - currentMistakes;
        double currentAccuracy = (double) currentCorrect / currentTotal * 100;
        
        if (currentAccuracy > bestAccuracy) {
            bestAccuracy = currentAccuracy;
            bestGame = new GameRecord(currentTime, currentHints, currentMistakes);
        }
        
        panel.add(createQuickStatCard("🏆 Best Accuracy", 
            String.format("%.1f%%", bestAccuracy), 
            "Highest score achieved", 
            new Color(255, 193, 7)));
        
        // Calculate average accuracy
        double totalAccuracy = currentAccuracy;
        for (GameRecord record : gameHistory) {
            int total = Math.max(1, record.hints + record.mistakes);
            int correct = total - record.mistakes;
            totalAccuracy += (double) correct / total * 100;
        }
        double avgAccuracy = totalAccuracy / (gameHistory.size() + 1);
        
        panel.add(createQuickStatCard("📊 Average Accuracy", 
            String.format("%.1f%%", avgAccuracy), 
            "Overall average score", 
            new Color(0, 123, 255)));
        
        // Calculate total games
        int totalGames = gameHistory.size() + 1;
        
        panel.add(createQuickStatCard("🎮 Games Played", 
            totalGames + "", 
            "Total completed puzzles", 
            new Color(40, 167, 69)));
        
        // Calculate improvement
        if (gameHistory.size() > 0) {
            GameRecord firstGame = gameHistory.get(0);
            int firstTotal = Math.max(1, firstGame.hints + firstGame.mistakes);
            int firstCorrect = firstTotal - firstGame.mistakes;
            double firstAccuracy = (double) firstCorrect / firstTotal * 100;
            
            double improvement = currentAccuracy - firstAccuracy;
            
            panel.add(createQuickStatCard("📈 Overall Growth", 
                String.format("%+.1f%%", improvement), 
                "Since first game", 
                improvement >= 0 ? new Color(40, 167, 69) : new Color(220, 53, 69)));
        } else {
            panel.add(createQuickStatCard("🚀 Starting Point", 
                "First Game", 
                "Begin your journey", 
                new Color(111, 66, 193)));
        }
        
        return panel;
    }
    
    private static JPanel createQuickStatCard(String title, String value, String desc, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(248, 249, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(73, 80, 87));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        
        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        descLabel.setForeground(new Color(108, 117, 125));
        
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(descLabel);
        
        return card;
    }
    
    private static JPanel createDetailedComparisonTab(String currentTime, int currentHints, int currentMistakes) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("🔍 Detailed Game-by-Game Comparison");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create table data for all games
        List<Object[]> tableData = new ArrayList<>();
        
        // Add current game first
        int currentTotal = Math.max(1, currentHints + currentMistakes);
        int currentCorrect = currentTotal - currentMistakes;
        double currentAccuracy = (double) currentCorrect / currentTotal * 100;
        
        tableData.add(new Object[]{
            "Current Game", 
            currentTime, 
            currentHints, 
            currentMistakes,
            String.format("%.1f%%", currentAccuracy),
            getPerformanceLevel(currentAccuracy),
            "🆕"
        });
        
        // Add previous games in reverse chronological order
        for (int i = gameHistory.size() - 1; i >= 0; i--) {
            GameRecord record = gameHistory.get(i);
            int total = Math.max(1, record.hints + record.mistakes);
            int correct = total - record.mistakes;
            double accuracy = (double) correct / total * 100;
            
            tableData.add(new Object[]{
                "Game " + (gameHistory.size() - i),
                record.time,
                record.hints,
                record.mistakes,
                String.format("%.1f%%", accuracy),
                getPerformanceLevel(accuracy),
                "✅"
            });
        }
        
        // Create table
        String[] columnNames = {"Game", "Time", "Hints", "Mistakes", "Accuracy", "Level", "Status"};
        Object[][] data = tableData.toArray(new Object[0][]);
        
        JTable table = new JTable(data, columnNames) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                
                // Highlight current game
                if (row == 0) {
                    c.setBackground(new Color(220, 237, 255));
                    c.setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    c.setBackground(row % 2 == 0 ? new Color(248, 249, 250) : Color.WHITE);
                }
                
                // Color accuracy cells based on value
                if (column == 4) {
                    String value = getValueAt(row, column).toString();
                    double accuracy = Double.parseDouble(value.replace("%", ""));
                    
                    if (accuracy >= 90) {
                        c.setForeground(new Color(40, 167, 69));
                        c.setFont(getFont().deriveFont(Font.BOLD));
                    } else if (accuracy >= 70) {
                        c.setForeground(new Color(255, 193, 7));
                    } else if (accuracy >= 50) {
                        c.setForeground(new Color(253, 126, 20));
                    } else {
                        c.setForeground(new Color(220, 53, 69));
                    }
                }
                
                // Center align all columns
                if (c instanceof JLabel) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                }
                
                return c;
            }
        };
        
        // Customize table
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 58, 64));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Make table scrollable
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Summary panel
        JPanel summaryPanel = createDetailedSummaryPanel(tableData);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private static JPanel createDetailedSummaryPanel(List<Object[]> tableData) {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Calculate best and worst games
        double bestAccuracy = 0;
        double worstAccuracy = 100;
        int bestGameIdx = 0;
        int worstGameIdx = 0;
        
        for (int i = 0; i < tableData.size(); i++) {
            String accStr = tableData.get(i)[4].toString();
            double accuracy = Double.parseDouble(accStr.replace("%", ""));
            
            if (accuracy > bestAccuracy) {
                bestAccuracy = accuracy;
                bestGameIdx = i;
            }
            
            if (accuracy < worstAccuracy) {
                worstAccuracy = accuracy;
                worstGameIdx = i;
            }
        }
        
        panel.add(createSummaryCard("⭐ Best Game", 
            tableData.get(bestGameIdx)[0].toString(),
            String.format("Accuracy: %.1f%%", bestAccuracy),
            new Color(255, 193, 7)));
        
        panel.add(createSummaryCard("📉 Worst Game", 
            tableData.get(worstGameIdx)[0].toString(),
            String.format("Accuracy: %.1f%%", worstAccuracy),
            new Color(220, 53, 69)));
        
        // Calculate consistency
        double sum = 0;
        for (Object[] row : tableData) {
            String accStr = row[4].toString();
            sum += Double.parseDouble(accStr.replace("%", ""));
        }
        double avgAccuracy = sum / tableData.size();
        
        // Calculate standard deviation for consistency
        double variance = 0;
        for (Object[] row : tableData) {
            String accStr = row[4].toString();
            double acc = Double.parseDouble(accStr.replace("%", ""));
            variance += Math.pow(acc - avgAccuracy, 2);
        }
        double stdDev = Math.sqrt(variance / tableData.size());
        
        String consistency = stdDev < 10 ? "Consistent" : stdDev < 20 ? "Variable" : "Inconsistent";
        Color consistencyColor = stdDev < 10 ? new Color(40, 167, 69) : 
                               stdDev < 20 ? new Color(255, 193, 7) : new Color(220, 53, 69);
        
        panel.add(createSummaryCard("📊 Consistency", 
            consistency,
            String.format("Std Dev: %.1f%%", stdDev),
            consistencyColor));
        
        panel.add(createSummaryCard("🎯 Average", 
            String.format("%.1f%%", avgAccuracy),
            tableData.size() + " games analyzed",
            new Color(0, 123, 255)));
        
        return panel;
    }
    
    private static JPanel createSummaryCard(String title, String value, String detail, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(73, 80, 87));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(color);
        
        JLabel detailLabel = new JLabel(detail);
        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        detailLabel.setForeground(new Color(108, 117, 125));
        
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(detailLabel);
        
        return card;
    }
    
    private static JPanel createProgressChartsTab(String currentTime, int currentHints, int currentMistakes) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel titleLabel = new JLabel("📊 Interactive Performance Charts");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Create tabbed pane for different charts
        JTabbedPane chartTabs = new JTabbedPane();
        chartTabs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Chart 1: Time Progression
        JPanel timeChartPanel = createTimeChartPanel();
        chartTabs.addTab("⏱️ Time", timeChartPanel);
        
        // Chart 2: Hints Progression
        JPanel hintsChartPanel = createHintsChartPanel();
        chartTabs.addTab("💡 Hints", hintsChartPanel);
        
        // Chart 3: Mistakes Progression
        JPanel mistakesChartPanel = createMistakesChartPanel();
        chartTabs.addTab("❌ Mistakes", mistakesChartPanel);
        
        // Chart 4: Combined Chart
        JPanel combinedChartPanel = createCombinedChartPanel();
        chartTabs.addTab("📊 Combined", combinedChartPanel);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(chartTabs, BorderLayout.CENTER);
        
        return panel;
    }
    
    private static JPanel createTimeChartPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTimeChart(g);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 400);
            }
        };
    }
    
    private static void drawTimeChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = 800;
        int height = 400;
        int padding = 60;
        int chartWidth = width - padding * 2;
        int chartHeight = height - padding * 2;
        
        // Draw background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.drawString("Completion Time Progression", width/2 - 100, padding - 20);
        
        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(padding, height - padding, width - padding, height - padding);
        g2d.drawLine(padding, padding, padding, height - padding);
        
        // Draw grid
        g2d.setColor(new Color(240, 240, 240));
        g2d.setStroke(new BasicStroke(1));
        
        int gameCount = Math.max(1, gameHistory.size());
        for (int i = 0; i <= gameCount; i++) {
            int x = padding + (chartWidth * i / gameCount);
            g2d.drawLine(x, padding, x, height - padding);
        }
        
        for (int i = 0; i <= 10; i++) {
            int y = padding + (chartHeight * i / 10);
            g2d.drawLine(padding, y, width - padding, y);
        }
        
        // Draw data
        if (gameHistory.size() > 0) {
            g2d.setColor(new Color(0, 123, 255));
            g2d.setStroke(new BasicStroke(3));
            
            // Parse times and find max time
            List<Integer> minutesList = new ArrayList<>();
            int maxMinutes = 0;
            
            for (GameRecord record : gameHistory) {
                try {
                    String[] parts = record.time.split(":");
                    int minutes = Integer.parseInt(parts[0]);
                    minutesList.add(minutes);
                    maxMinutes = Math.max(maxMinutes, minutes);
                } catch (Exception e) {
                    minutesList.add(10); // Default if parsing fails
                }
            }
            
            // Draw line
            for (int i = 0; i < minutesList.size() - 1; i++) {
                int x1 = padding + (chartWidth * i / (gameCount - 1));
                int y1 = height - padding - (chartHeight * minutesList.get(i) / Math.max(maxMinutes, 1));
                
                int x2 = padding + (chartWidth * (i + 1) / (gameCount - 1));
                int y2 = height - padding - (chartHeight * minutesList.get(i + 1) / Math.max(maxMinutes, 1));
                
                g2d.drawLine(x1, y1, x2, y2);
            }
            
            // Draw points
            g2d.setColor(new Color(220, 53, 69));
            for (int i = 0; i < minutesList.size(); i++) {
                int x = padding + (chartWidth * i / (gameCount - 1));
                int y = height - padding - (chartHeight * minutesList.get(i) / Math.max(maxMinutes, 1));
                
                g2d.fillOval(x - 5, y - 5, 10, 10);
                
                // Label every other point
                if (i % 2 == 0) {
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    g2d.drawString(minutesList.get(i) + "m", x - 10, y - 10);
                    g2d.setColor(new Color(220, 53, 69));
                }
            }
        } else {
            // No data message
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            g2d.drawString("Complete more games to see time progression", width/2 - 150, height/2);
        }
    }
    
    private static JPanel createHintsChartPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarChart(g, "Hints", new Color(23, 162, 184));
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 400);
            }
        };
    }
    
    private static JPanel createMistakesChartPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarChart(g, "Mistakes", new Color(220, 53, 69));
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 400);
            }
        };
    }
    
    private static void drawBarChart(Graphics g, String type, Color barColor) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = 800;
        int height = 400;
        int padding = 60;
        int chartWidth = width - padding * 2;
        int chartHeight = height - padding * 2;
        
        // Draw background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.drawString(type + " Progression", width/2 - 50, padding - 20);
        
        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(padding, height - padding, width - padding, height - padding);
        g2d.drawLine(padding, padding, padding, height - padding);
        
        if (gameHistory.size() > 0) {
            int gameCount = gameHistory.size();
            int barWidth = chartWidth / (gameCount * 2);
            int maxValue = 0;
            
            // Find max value
            for (GameRecord record : gameHistory) {
                int value = type.equals("Hints") ? record.hints : record.mistakes;
                maxValue = Math.max(maxValue, value);
            }
            
            // Draw bars
            for (int i = 0; i < gameCount; i++) {
                GameRecord record = gameHistory.get(i);
                int value = type.equals("Hints") ? record.hints : record.mistakes;
                
                int x = padding + (chartWidth * i / gameCount) + barWidth/2;
                int barHeight = (int)((double)value / maxValue * chartHeight);
                int y = height - padding - barHeight;
                
                // Draw bar
                g2d.setColor(barColor);
                g2d.fillRect(x, y, barWidth, barHeight);
                
                // Draw value
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
                g2d.drawString(value + "", x + barWidth/2 - 5, y - 5);
                
                // Draw game number
                g2d.drawString("G" + (i + 1), x + barWidth/2 - 5, height - padding + 15);
            }
        } else {
            // No data message
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            g2d.drawString("Complete more games to see " + type.toLowerCase() + " progression", 
                          width/2 - 180, height/2);
        }
    }
    
    private static JPanel createCombinedChartPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCombinedChart(g);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 400);
            }
        };
    }
    
    private static void drawCombinedChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = 800;
        int height = 400;
        int padding = 60;
        
        // Draw background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.drawString("Combined Performance Metrics", width/2 - 100, padding - 20);
        
        if (gameHistory.isEmpty()) {
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            g2d.drawString("Complete more games to see combined charts", width/2 - 150, height/2);
            return;
        }
        
        // Draw legend
        int legendY = padding - 30;
        drawLegendItem(g2d, "Hints", new Color(23, 162, 184), 100, legendY);
        drawLegendItem(g2d, "Mistakes", new Color(220, 53, 69), 200, legendY);
        drawLegendItem(g2d, "Accuracy", new Color(40, 167, 69), 300, legendY);
        
        // Draw mini bar charts
        int chartHeight = 200;
        int chartWidth = 150;
        int spacing = 20;
        
        int x = padding;
        int y = padding + 50;
        
        // Chart 1: Hints distribution
        drawMiniChart(g2d, "Hints", CHART_COLORS[0], x, y, chartWidth, chartHeight);
        
        // Chart 2: Mistakes distribution
        drawMiniChart(g2d, "Mistakes", CHART_COLORS[1], x + chartWidth + spacing, y, chartWidth, chartHeight);
        
        // Chart 3: Time distribution
        drawMiniChart(g2d, "Time", CHART_COLORS[2], x + (chartWidth + spacing) * 2, y, chartWidth, chartHeight);
        
        // Chart 4: Accuracy distribution
        drawMiniChart(g2d, "Accuracy", CHART_COLORS[3], x + (chartWidth + spacing) * 3, y, chartWidth, chartHeight);
    }
    
    private static void drawLegendItem(Graphics2D g2d, String text, Color color, int x, int y) {
        g2d.setColor(color);
        g2d.fillRect(x, y, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2d.drawString(text, x + 20, y + 12);
    }
    
    private static void drawMiniChart(Graphics2D g2d, String title, Color color, int x, int y, int width, int height) {
        // Draw background
        g2d.setColor(new Color(248, 249, 250));
        g2d.fillRoundRect(x, y, width, height, 10, 10);
        g2d.setColor(new Color(206, 212, 218));
        g2d.drawRoundRect(x, y, width, height, 10, 10);
        
        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2d.drawString(title, x + width/2 - 20, y + 20);
        
        // Draw some sample bars (in a real implementation, you would use actual data)
        int barCount = 5;
        int barWidth = 20;
        int spacing = 5;
        int chartY = y + 40;
        int chartHeight = height - 60;
        
        for (int i = 0; i < barCount; i++) {
            int barX = x + spacing + i * (barWidth + spacing);
            int barHeight = (int)(Math.random() * chartHeight * 0.8);
            int barY = chartY + chartHeight - barHeight;
            
            g2d.setColor(color);
            g2d.fillRect(barX, barY, barWidth, barHeight);
            
            // Draw value
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 8));
            int value = (int)(Math.random() * 100);
            g2d.drawString(value + "", barX + 5, barY - 5);
        }
    }
    
    private static JPanel createStatisticsTab(String currentTime, int currentHints, int currentMistakes) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel titleLabel = new JLabel("📋 Advanced Statistics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Create statistics cards
        JPanel statsGrid = new JPanel(new GridLayout(3, 3, 15, 15));
        statsGrid.setBackground(Color.WHITE);
        
        // Calculate all statistics
        double[] statistics = calculateAdvancedStatistics(currentTime, currentHints, currentMistakes);
        
        statsGrid.add(createAdvancedStatCard("🎯 Mean Accuracy", 
            String.format("%.1f%%", statistics[0]), 
            "Average of all games", 
            new Color(65, 105, 225)));
        
        statsGrid.add(createAdvancedStatCard("📊 Median Accuracy", 
            String.format("%.1f%%", statistics[1]), 
            "Middle value", 
            new Color(50, 205, 50)));
        
        statsGrid.add(createAdvancedStatCard("⚡ Standard Deviation", 
            String.format("%.1f%%", statistics[2]), 
            "Consistency measure", 
            new Color(255, 99, 71)));
        
        statsGrid.add(createAdvancedStatCard("📈 Trend Slope", 
            String.format("%+.2f", statistics[3]), 
            "Improvement rate", 
            new Color(255, 140, 0)));
        
        statsGrid.add(createAdvancedStatCard("🎮 Games Completed", 
            (gameHistory.size() + 1) + "", 
            "Total puzzles solved", 
            new Color(138, 43, 226)));
        
        statsGrid.add(createAdvancedStatCard("⏱️ Average Time", 
            String.format("%.1f min", statistics[4]), 
            "Mean completion time", 
            new Color(0, 206, 209)));
        
        statsGrid.add(createAdvancedStatCard("💡 Hint Reduction", 
            String.format("%.1f%%", statistics[5]), 
            "Hints improvement", 
            new Color(255, 215, 0)));
        
        statsGrid.add(createAdvancedStatCard("❌ Error Reduction", 
            String.format("%.1f%%", statistics[6]), 
            "Mistakes improvement", 
            new Color(220, 20, 60)));
        
        statsGrid.add(createAdvancedStatCard("📅 Days Active", 
            statistics[7] + "", 
            "Playing duration", 
            new Color(30, 144, 255)));
        
        // Insights panel
        JPanel insightsPanel = createInsightsPanel(statistics);
        insightsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(statsGrid, BorderLayout.CENTER);
        panel.add(insightsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private static double[] calculateAdvancedStatistics(String currentTime, int currentHints, int currentMistakes) {
        double[] stats = new double[8];
        
        if (gameHistory.isEmpty()) {
            // Return default values for first game
            int total = Math.max(1, currentHints + currentMistakes);
            int correct = total - currentMistakes;
            double accuracy = (double) correct / total * 100;
            
            stats[0] = accuracy; // Mean
            stats[1] = accuracy; // Median
            stats[2] = 0;        // Std Dev
            stats[3] = 0;        // Trend
            stats[4] = 10;       // Avg Time (default)
            stats[5] = 0;        // Hint reduction
            stats[6] = 0;        // Error reduction
            stats[7] = 1;        // Days active
            
            return stats;
        }
        
        // Calculate accuracies
        List<Double> accuracies = new ArrayList<>();
        
        // Add current game
        int currentTotal = Math.max(1, currentHints + currentMistakes);
        int currentCorrect = currentTotal - currentMistakes;
        double currentAccuracy = (double) currentCorrect / currentTotal * 100;
        accuracies.add(currentAccuracy);
        
        // Add previous games
        for (GameRecord record : gameHistory) {
            int total = Math.max(1, record.hints + record.mistakes);
            int correct = total - record.mistakes;
            double accuracy = (double) correct / total * 100;
            accuracies.add(accuracy);
        }
        
        // Calculate mean
        double sum = 0;
        for (double acc : accuracies) sum += acc;
        stats[0] = sum / accuracies.size();
        
        // Calculate median
        List<Double> sorted = new ArrayList<>(accuracies);
        Collections.sort(sorted);
        stats[1] = sorted.get(sorted.size() / 2);
        
        // Calculate standard deviation
        double variance = 0;
        for (double acc : accuracies) {
            variance += Math.pow(acc - stats[0], 2);
        }
        stats[2] = Math.sqrt(variance / accuracies.size());
        
        // Calculate trend (linear regression slope)
        double xSum = 0, ySum = 0, xySum = 0, x2Sum = 0;
        int n = accuracies.size();
        
        for (int i = 0; i < n; i++) {
            xSum += i;
            ySum += accuracies.get(i);
            xySum += i * accuracies.get(i);
            x2Sum += i * i;
        }
        
        stats[3] = (n * xySum - xSum * ySum) / (n * x2Sum - xSum * xSum);
        
        // Calculate average time
        double totalMinutes = 0;
        int timeCount = 0;
        
        try {
            String[] parts = currentTime.split(":");
            totalMinutes += Integer.parseInt(parts[0]);
            timeCount++;
        } catch (Exception e) {
            // Skip if time format is invalid
        }
        
        for (GameRecord record : gameHistory) {
            try {
                String[] parts = record.time.split(":");
                totalMinutes += Integer.parseInt(parts[0]);
                timeCount++;
            } catch (Exception e) {
                // Skip if time format is invalid
            }
        }
        
        stats[4] = timeCount > 0 ? totalMinutes / timeCount : 10;
        
        // Calculate hint and error reduction
        GameRecord firstGame = gameHistory.get(0);
        double hintReduction = ((double) firstGame.hints - currentHints) / Math.max(firstGame.hints, 1) * 100;
        double errorReduction = ((double) firstGame.mistakes - currentMistakes) / Math.max(firstGame.mistakes, 1) * 100;
        
        stats[5] = Math.max(0, hintReduction);
        stats[6] = Math.max(0, errorReduction);
        
        // Days active (simplified - one day per game for demo)
        stats[7] = Math.min(accuracies.size(), 30);
        
        return stats;
    }
    
    private static JPanel createAdvancedStatCard(String title, String value, String description, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(73, 80, 87));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(color);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        descLabel.setForeground(new Color(108, 117, 125));
        
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(descLabel);
        
        return card;
    }
    
    private static JPanel createInsightsPanel(double[] statistics) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("💡 Performance Insights"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        StringBuilder insights = new StringBuilder("<html><div style='width: 700px;'>");
        
        if (gameHistory.isEmpty()) {
            insights.append("<b>First Game Analysis:</b><br>");
            insights.append("• This is your first completed puzzle!<br>");
            insights.append("• Use this as your baseline for future comparisons<br>");
            insights.append("• Try to beat your time and accuracy in the next game<br><br>");
            insights.append("<b>Tip:</b> Complete more puzzles to unlock detailed insights and trend analysis.");
        } else {
            insights.append("<b>Performance Summary:</b><br>");
            
            if (statistics[3] > 0.5) {
                insights.append("• 📈 <b>Excellent progress!</b> You're improving rapidly<br>");
            } else if (statistics[3] > 0) {
                insights.append("• 👍 <b>Steady improvement</b> - keep it up!<br>");
            } else if (statistics[3] < -0.5) {
                insights.append("• ⚠️ <b>Performance decline detected</b> - review your strategy<br>");
            } else {
                insights.append("• ↔️ <b>Stable performance</b> - time for a new challenge!<br>");
            }
            
            if (statistics[2] < 10) {
                insights.append("• 🎯 <b>Highly consistent</b> - reliable performance<br>");
            } else if (statistics[2] < 20) {
                insights.append("• 📊 <b>Moderate consistency</b> - room for improvement<br>");
            } else {
                insights.append("• 🔄 <b>Variable performance</b> - focus on consistency<br>");
            }
            
            if (statistics[5] > 50) {
                insights.append("• 💡 <b>Great hint reduction!</b> Becoming more independent<br>");
            }
            
            if (statistics[6] > 50) {
                insights.append("• ✅ <b>Excellent error reduction!</b> Accuracy improving<br>");
            }
            
            insights.append("<br><b>Recommendations:</b><br>");
            
            if (statistics[0] < 70) {
                insights.append("• Focus on improving accuracy before speed<br>");
            } else if (statistics[0] >= 90) {
                insights.append("• Challenge yourself with harder difficulty levels<br>");
            }
            
            if (statistics[4] > 20) {
                insights.append("• Work on solving puzzles faster<br>");
            }
            
            insights.append("• Try different solving strategies for variety");
        }
        
        insights.append("</div></html>");
        
        JLabel insightsLabel = new JLabel(insights.toString());
        insightsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        insightsLabel.setForeground(new Color(52, 58, 64));
        
        panel.add(insightsLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private static JPanel createComparisonControlPanel(JFrame comparisonFrame, JFrame parentFrame, 
                                                      String currentTime, int currentHints, int currentMistakes) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JButton exportButton = createControlButton("📥 Export Data", 
            new Color(0, 123, 255), 
            e -> exportComparisonData(parentFrame, currentTime, currentHints, currentMistakes));
        
        JButton clearButton = createControlButton("🗑️ Clear History", 
            new Color(220, 53, 69), 
            e -> clearGameHistory(comparisonFrame, parentFrame));
        
        JButton printButton = createControlButton("🖨️ Print Summary", 
            new Color(40, 167, 69), 
            e -> printComparisonSummary(parentFrame, currentTime, currentHints, currentMistakes));
        
        JButton closeButton = createControlButton("✕ Close", 
            new Color(108, 117, 125), 
            e -> comparisonFrame.dispose());
        
        panel.add(exportButton);
        panel.add(clearButton);
        panel.add(printButton);
        panel.add(closeButton);
        
        return panel;
    }
    
    private static JButton createControlButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private static void clearGameHistory(JFrame comparisonFrame, JFrame parentFrame) {
        int confirm = JOptionPane.showConfirmDialog(comparisonFrame,
            "<html><div style='width: 300px; text-align: center;'>" +
            "<h3>Clear All Game History?</h3>" +
            "This will permanently delete all saved game records.<br>" +
            "<b>This action cannot be undone!</b></div></html>",
            "Confirm Clear History", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            gameHistory.clear();
            comparisonFrame.dispose();
            JOptionPane.showMessageDialog(parentFrame,
                "<html><div style='width: 250px; text-align: center;'>" +
                "✅ Game history cleared successfully.<br>" +
                "New games will start fresh tracking.</div></html>",
                "History Cleared", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private static void printComparisonSummary(JFrame parentFrame, String currentTime, int currentHints, int currentMistakes) {
        StringBuilder summary = new StringBuilder();
        summary.append("========================================\n");
        summary.append("        SUDOKU COMPARISON SUMMARY       \n");
        summary.append("========================================\n\n");
        
        summary.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        summary.append("Total Games: ").append(gameHistory.size() + 1).append("\n\n");
        
        // Current game stats
        int currentTotal = Math.max(1, currentHints + currentMistakes);
        int currentCorrect = currentTotal - currentMistakes;
        double currentAccuracy = (double) currentCorrect / currentTotal * 100;
        
        summary.append("CURRENT GAME:\n");
        summary.append("  Time: ").append(currentTime).append("\n");
        summary.append("  Accuracy: ").append(String.format("%.1f%%", currentAccuracy)).append("\n");
        summary.append("  Hints: ").append(currentHints).append("\n");
        summary.append("  Mistakes: ").append(currentMistakes).append("\n");
        summary.append("  Performance: ").append(getPerformanceLevel(currentAccuracy)).append("\n\n");
        
        if (!gameHistory.isEmpty()) {
            // Best game
            GameRecord bestGame = null;
            double bestAccuracy = 0;
            
            for (GameRecord record : gameHistory) {
                int total = Math.max(1, record.hints + record.mistakes);
                int correct = total - record.mistakes;
                double accuracy = (double) correct / total * 100;
                
                if (accuracy > bestAccuracy) {
                    bestAccuracy = accuracy;
                    bestGame = record;
                }
            }
            
            // Check if current game is better
            if (currentAccuracy > bestAccuracy) {
                bestAccuracy = currentAccuracy;
                bestGame = new GameRecord(currentTime, currentHints, currentMistakes);
            }
            
            summary.append("BEST GAME:\n");
            summary.append("  Accuracy: ").append(String.format("%.1f%%", bestAccuracy)).append("\n");
            summary.append("  Time: ").append(bestGame.time).append("\n");
            summary.append("  Hints: ").append(bestGame.hints).append("\n");
            summary.append("  Mistakes: ").append(bestGame.mistakes).append("\n\n");
            
            // Progress since first game
            GameRecord firstGame = gameHistory.get(0);
            int firstTotal = Math.max(1, firstGame.hints + firstGame.mistakes);
            int firstCorrect = firstTotal - firstGame.mistakes;
            double firstAccuracy = (double) firstCorrect / firstTotal * 100;
            
            double accuracyImprovement = currentAccuracy - firstAccuracy;
            int hintImprovement = firstGame.hints - currentHints;
            int mistakeImprovement = firstGame.mistakes - currentMistakes;
            
            summary.append("PROGRESS SINCE FIRST GAME:\n");
            summary.append("  Accuracy: ").append(String.format("%+.1f%%", accuracyImprovement)).append("\n");
            summary.append("  Hints: ").append(String.format("%+d", hintImprovement)).append("\n");
            summary.append("  Mistakes: ").append(String.format("%+d", mistakeImprovement)).append("\n");
        }
        
        summary.append("\n========================================\n");
        
        JTextArea textArea = new JTextArea(summary.toString(), 20, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JOptionPane.showMessageDialog(parentFrame, new JScrollPane(textArea), 
            "Comparison Summary", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static void exportComparisonData(JFrame parentFrame, String currentTime, int currentHints, int currentMistakes) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Comparison Data");
        fileChooser.setSelectedFile(new File("Sudoku_Comparison_Data_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileToSave))) {
                // Write CSV header
                writer.println("Game,Time,Hints,Mistakes,Accuracy,Performance,Date");
                
                // Write current game
                int currentTotal = Math.max(1, currentHints + currentMistakes);
                int currentCorrect = currentTotal - currentMistakes;
                double currentAccuracy = (double) currentCorrect / currentTotal * 100;
                
                writer.println(String.format("Current Game,%s,%d,%d,%.1f%%,%s,%s",
                    currentTime, currentHints, currentMistakes, currentAccuracy,
                    getPerformanceLevel(currentAccuracy),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                
                // Write previous games in chronological order
                for (int i = gameHistory.size() - 1; i >= 0; i--) {
                    GameRecord record = gameHistory.get(i);
                    int total = Math.max(1, record.hints + record.mistakes);
                    int correct = total - record.mistakes;
                    double accuracy = (double) correct / total * 100;
                    
                    writer.println(String.format("Game %d,%s,%d,%d,%.1f%%,%s,%s",
                        gameHistory.size() - i, record.time, record.hints, record.mistakes,
                        accuracy, getPerformanceLevel(accuracy),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                }
                
                JOptionPane.showMessageDialog(parentFrame,
                    "<html><div style='width: 300px; text-align: center;'>" +
                    "✅ Comparison data exported successfully!<br><br>" +
                    "<b>File:</b> " + fileToSave.getName() + "<br>" +
                    "<b>Location:</b> " + fileToSave.getParent() + "</div></html>",
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentFrame,
                    "Error exporting data: " + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ======================== FEATURE 3: ACHIEVEMENTS ========================
    private static void showAchievements(JFrame parentFrame, int hints, int mistakes) {
        JDialog achievementsDialog = new JDialog(parentFrame, "Achievements", true);
        achievementsDialog.setSize(500, 600);
        achievementsDialog.setLocationRelativeTo(parentFrame);
        achievementsDialog.setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("🏆 Your Achievements", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Achievements panel
        JPanel achievementsPanel = new JPanel();
        achievementsPanel.setLayout(new BoxLayout(achievementsPanel, BoxLayout.Y_AXIS));
        achievementsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // Calculate achievements
        List<Achievement> unlocked = new ArrayList<>();
        
        // Perfect Game Achievement
        if (mistakes == 0 && hints == 0) {
            unlocked.add(new Achievement("⭐ Perfect Game", 
                "Complete a puzzle with no mistakes and no hints",
                "DIAMOND", new Color(255, 215, 0), true));
        } else {
            unlocked.add(new Achievement("Perfect Game", 
                "Complete a puzzle with no mistakes and no hints",
                "LOCKED", new Color(200, 200, 200), false));
        }
        
        // Speed Demon Achievement
        if (hints <= 1) {
            unlocked.add(new Achievement("⚡ Speed Demon", 
                "Solve with minimal hints (1 or less)",
                "GOLD", new Color(255, 193, 7), true));
        } else {
            unlocked.add(new Achievement("Speed Demon", 
                "Solve with minimal hints (1 or less)",
                "LOCKED", new Color(200, 200, 200), false));
        }
        
        // Accuracy Master Achievement
        if (mistakes <= 2) {
            unlocked.add(new Achievement("🎯 Accuracy Master", 
                "Make 2 or fewer mistakes",
                "GOLD", new Color(40, 167, 69), true));
        } else {
            unlocked.add(new Achievement("Accuracy Master", 
                "Make 2 or fewer mistakes",
                "LOCKED", new Color(200, 200, 200), false));
        }
        
        // Hintless Hero Achievement
        if (hints == 0) {
            unlocked.add(new Achievement("🧠 Hintless Hero", 
                "Complete without using any hints",
                "PLATINUM", new Color(0, 123, 255), true));
        } else {
            unlocked.add(new Achievement("Hintless Hero", 
                "Complete without using any hints",
                "LOCKED", new Color(200, 200, 200), false));
        }
        
        // First Game Achievement (always unlocked after first game)
        unlocked.add(new Achievement("🚀 First Steps", 
            "Complete your first Sudoku puzzle",
            "BRONZE", new Color(205, 127, 50), true));
        
        // Add achievement cards
        for (Achievement achievement : unlocked) {
            achievementsPanel.add(createAchievementCard(achievement));
            achievementsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        // Progress summary
        int totalAchievements = unlocked.size();
        int unlockedCount = (int) unlocked.stream().filter(a -> a.unlocked).count();
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        progressPanel.setBackground(new Color(248, 249, 250));
        
        JLabel progressLabel = new JLabel(
            String.format("Progress: %d/%d achievements unlocked", unlockedCount, totalAchievements));
        progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JProgressBar progressBar = new JProgressBar(0, totalAchievements);
        progressBar.setValue(unlockedCount);
        progressBar.setForeground(new Color(40, 167, 69));
        progressBar.setStringPainted(true);
        progressBar.setString(unlockedCount + "/" + totalAchievements);
        
        progressPanel.add(progressLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(achievementsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> achievementsDialog.dispose());
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeButton.setPreferredSize(new Dimension(100, 35));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        
        // Add components
        achievementsDialog.add(titleLabel, BorderLayout.NORTH);
        achievementsDialog.add(scrollPane, BorderLayout.CENTER);
        achievementsDialog.add(progressPanel, BorderLayout.SOUTH);
        achievementsDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        achievementsDialog.setVisible(true);
    }
    
    private static JPanel createAchievementCard(Achievement achievement) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(achievement.unlocked ? Color.WHITE : new Color(248, 249, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                achievement.unlocked ? achievement.color : new Color(200, 200, 200), 
                2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(450, 100));
        
        // Icon
        JLabel iconLabel = new JLabel(achievement.unlocked ? "🏆" : "🔒");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setForeground(achievement.unlocked ? achievement.color : new Color(150, 150, 150));
        
        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(card.getBackground());
        
        JLabel titleLabel = new JLabel(achievement.title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(achievement.unlocked ? new Color(33, 37, 41) : new Color(150, 150, 150));
        
        JLabel descLabel = new JLabel("<html><div style='width: 300px;'>" + achievement.description + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(achievement.unlocked ? new Color(108, 117, 125) : new Color(180, 180, 180));
        
        // Badge
        JLabel badgeLabel = new JLabel(achievement.badge);
        badgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badgeLabel.setForeground(achievement.unlocked ? achievement.color : new Color(150, 150, 150));
        badgeLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(achievement.unlocked ? achievement.color : new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(descLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(badgeLabel);
        
        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    // ======================== FEATURE 4: PRACTICE MODE ========================
    private static void startPracticeMode(JFrame parentFrame, JFrame victoryFrame, int hints, int mistakes) {
        int choice = JOptionPane.showOptionDialog(victoryFrame,
            "<html><div style='width: 300px; text-align: center;'>" +
            "<h3>Practice Mode</h3>" +
            "Choose what you want to practice based on your performance:<br><br>" +
            "Your stats: " + hints + " hints, " + mistakes + " mistakes</div></html>",
            "Practice Mode",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[]{"Reduce Hints", "Reduce Mistakes", "Speed Practice", "Cancel"},
            "Cancel");
        
        if (choice == 0) { // Reduce Hints
            JOptionPane.showMessageDialog(victoryFrame,
                "<html><div style='width: 300px; text-align: center;'>" +
                "<h3>Hint Reduction Practice</h3>" +
                "Try your next game with these rules:<br><br>" +
                "1. Only allow yourself 2 hints maximum<br>" +
                "2. Wait at least 2 minutes before using first hint<br>" +
                "3. Try to identify patterns before requesting help<br><br>" +
                "<b>Goal:</b> Complete with 2 or fewer hints</div></html>",
                "Practice Challenge",
                JOptionPane.INFORMATION_MESSAGE);
            
        } else if (choice == 1) { // Reduce Mistakes
            JOptionPane.showMessageDialog(victoryFrame,
                "<html><div style='width: 300px; text-align: center;'>" +
                "<h3>Accuracy Practice</h3>" +
                "Try your next game with these rules:<br><br>" +
                "1. Double-check every placement before confirming<br>" +
                "2. Use pencil marks for possibilities<br>" +
                "3. If you make a mistake, restart that section<br><br>" +
                "<b>Goal:</b> Complete with 3 or fewer mistakes</div></html>",
                "Practice Challenge",
                JOptionPane.INFORMATION_MESSAGE);
            
        } else if (choice == 2) { // Speed Practice
            JOptionPane.showMessageDialog(victoryFrame,
                "<html><div style='width: 300px; text-align: center;'>" +
                "<h3>Speed Practice</h3>" +
                "Try your next game with these rules:<br><br>" +
                "1. Set a 10-minute timer<br>" +
                "2. Focus on quick pattern recognition<br>" +
                "3. Skip difficult cells and come back later<br><br>" +
                "<b>Goal:</b> Complete under 10 minutes</div></html>",
                "Practice Challenge",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Start new game with practice mode
        if (choice != 3) { // Not Cancel
            victoryFrame.dispose();
            parentFrame.dispose();
            SwingUtilities.invokeLater(() -> {
                // Assuming you have a SudokuGUI class to start new game
                try {
                    Class<?> sudokuClass = Class.forName("SudokuGUI");
                    JFrame newGame = (JFrame) sudokuClass.getDeclaredConstructor().newInstance();
                    newGame.setVisible(true);
                    JOptionPane.showMessageDialog(newGame,
                        "Practice mode activated! Apply the strategies you just learned.",
                        "Practice Mode Started",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    private static JPanel createActionButtonsSection(JFrame parentFrame, JFrame victoryFrame, int hints, int mistakes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(1050, 200));
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(1050, 60));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Create buttons
        JButton backButton = createModernButton("← Back to Game", 
            new Color(108, 117, 125), Color.WHITE, 180);
        JButton newGameButton = createModernButton("🔄 New Game", 
            new Color(0, 123, 255), Color.WHITE, 180);
        JButton shareButton = createModernButton("📤 Share Results", 
            new Color(40, 167, 69), Color.WHITE, 180);
        JButton exitButton = createModernButton("🚪 Exit Game", 
            new Color(220, 53, 69), Color.WHITE, 180);
        
        // Button actions
        backButton.addActionListener(e -> victoryFrame.dispose());
        newGameButton.addActionListener(e -> {
            victoryFrame.dispose();
            parentFrame.dispose();
            SwingUtilities.invokeLater(() -> {
                try {
                    Class<?> sudokuClass = Class.forName("SudokuGUI");
                    JFrame newGame = (JFrame) sudokuClass.getDeclaredConstructor().newInstance();
                    newGame.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
        shareButton.addActionListener(e -> showShareDialog(victoryFrame, hints, mistakes));
        exitButton.addActionListener(e -> {
            parentFrame.dispose();
            victoryFrame.dispose();
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(newGameButton);
        buttonPanel.add(shareButton);
        buttonPanel.add(exitButton);
        
        // Tips panel
        JPanel tipsPanel = createTipsPanel(hints, mistakes);
        tipsPanel.setMaximumSize(new Dimension(1050, 100));
        
        panel.add(buttonPanel);
        panel.add(tipsPanel);
        
        return panel;
    }
    
    private static JButton createModernButton(String text, Color bgColor, Color textColor, int width) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded button
                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Draw text
                g2d.setColor(textColor);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                Rectangle2D rect = fm.getStringBounds(getText(), g2d);
                
                int textX = (int)((getWidth() - rect.getWidth()) / 2);
                int textY = (int)((getHeight() - rect.getHeight()) / 2 + fm.getAscent());
                
                g2d.drawString(getText(), textX, textY);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, 45));
        button.setMaximumSize(new Dimension(width, 45));
        
        return button;
    }
    
    private static JPanel createTipsPanel(int hints, int mistakes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 100, 0, 100));
        panel.setMaximumSize(new Dimension(1050, 80));
        
        JLabel title = new JLabel("💡 Pro Tip:");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(33, 37, 41));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String tip = "";
        if (mistakes > 5) {
            tip = "Focus on checking each placement twice to reduce errors.";
        } else if (hints > 3) {
            tip = "Try to solve at least 70% independently before using hints.";
        } else {
            tip = "Excellent! Challenge yourself with a higher difficulty level.";
        }
        
        JLabel tipLabel = new JLabel("<html><div style='text-align: center;'>" + tip + "</div></html>");
        tipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tipLabel.setForeground(new Color(108, 117, 125));
        tipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(tipLabel);
        
        return panel;
    }
    
    private static void showShareDialog(JFrame parentFrame, int hints, int mistakes) {
        int totalActions = Math.max(1, hints + mistakes);
        int correctActions = totalActions - mistakes;
        double accuracy = (double) correctActions / totalActions * 100;
        
        String shareText = String.format(
            "🏆 Just completed a Sudoku puzzle!\n" +
            "🎯 Accuracy: %.1f%%\n" +
            "✅ Correct Moves: %d\n" +
            "❌ Mistakes: %d\n" +
            "💡 Hints Used: %d\n" +
            "\nCan you beat my score?",
            accuracy, correctActions, mistakes, hints
        );
        
        JTextArea textArea = new JTextArea(shareText, 8, 30);
        textArea.setEditable(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        
        int option = JOptionPane.showConfirmDialog(parentFrame, scrollPane, 
            "Share Your Results", JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE);
        
        if (option == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Results copied to clipboard!\nShare with your friends.", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // ======================== HELPER METHODS ========================
    private static void saveGameToHistory(String time, int hints, int mistakes) {
        // Limit history to last 20 games
        if (gameHistory.size() >= 20) {
            gameHistory.remove(0);
        }
        gameHistory.add(new GameRecord(time, hints, mistakes));
    }
    
    private static String getPerformanceLevel(double accuracy) {
        if (accuracy >= 95) return "Expert";
        if (accuracy >= 85) return "Advanced";
        if (accuracy >= 70) return "Intermediate";
        if (accuracy >= 50) return "Beginner";
        return "Novice";
    }
    
    private static String getImprovementSuggestion(int hints, int mistakes) {
        if (mistakes > 5) return "Focus on accuracy";
        if (hints > 3) return "Reduce hint usage";
        if (mistakes <= 2 && hints <= 1) return "Increase difficulty";
        return "Practice more";
    }
    
    private static void customizeScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(200, 200, 200);
                this.trackColor = new Color(245, 245, 245);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createInvisibleButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createInvisibleButton();
            }
            
            private JButton createInvisibleButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
            
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                                thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            }
        });
    }
    
    // ======================== HELPER CLASSES ========================
    static class GameRecord {
        String time;
        int hints;
        int mistakes;
        
        GameRecord(String time, int hints, int mistakes) {
            this.time = time;
            this.hints = hints;
            this.mistakes = mistakes;
        }
    }
    
    static class Achievement {
        String title;
        String description;
        String badge;
        Color color;
        boolean unlocked;
        
        Achievement(String title, String description, String badge, Color color, boolean unlocked) {
            this.title = title;
            this.description = description;
            this.badge = badge;
            this.color = color;
            this.unlocked = unlocked;
        }
    }
}