package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import service.QuestionService;
import utils.FileUtils;
import model.Question;
import service.QuizService;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    private JTextArea lectureArea, outputArea;
    private JComboBox<String> typeBox, difficultyBox, topicBox;
    private File selectedFile;
    private List<Question> quizQuestions;
    private List<Integer> userAnswers = new ArrayList<>();
    private final QuizService quizService = new QuizService();
    private final QuestionService service = new QuestionService();

    public MainFrame() {
        setupTheme();
        initUI();
        setVisible(true);
    }

    private void setupTheme() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf theme");
        }
    }

    private void initUI() {
        setTitle("Smart Question Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Main container with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // --- SIDEBAR (WEST) ---
        JPanel sidebar = createSidebar();

        // --- CONTENT AREA (CENTER) ---
        contentPanel = new JPanel(cardLayout = new CardLayout());
        contentPanel.add(createUploadPage(), "UPLOAD");
        contentPanel.add(createResultPage(), "RESULT");
        contentPanel.add(createQuizPage(), "QUIZ");

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(new Color(43, 43, 43));
        sidebar.setBorder(new EmptyBorder(25, 15, 25, 15));

        // Upload Button
        JButton uploadBtn = new JButton("↑ Upload Lecture");
        uploadBtn.setBackground(new Color(74, 144, 226));
        uploadBtn.setForeground(Color.WHITE);
        uploadBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        uploadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        uploadBtn.addActionListener(e -> uploadLecture());

        // Type Label & Combo
        JLabel lblType = new JLabel("Type:");
        lblType.setForeground(Color.WHITE);
        typeBox = new JComboBox<>(new String[]{"MCQ", "Short", "Coding"});
        typeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        typeBox.setForeground(Color.WHITE);

        // Difficulty Label & Combo
        JLabel lblDiff = new JLabel("Difficulty:");
        lblDiff.setForeground(Color.WHITE);
        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficultyBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        difficultyBox.setForeground(Color.WHITE);

        // Topic Label & Combo
        JLabel lblTopic = new JLabel("Quiz Topic:");
        lblTopic.setForeground(Color.WHITE);
        List<String> topics = new dao.QuestionDAO().getAllTopics();
        topicBox = new JComboBox<>(topics.toArray(new String[0]));
        topicBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        topicBox.setForeground(Color.WHITE);
        topicBox.setSelectedIndex(-1);
        topicBox.addActionListener(e -> {
            String sel = (String) topicBox.getSelectedItem();
            if (sel != null) {
                lectureArea.setText(sel);
            }
        });

        // Generate Button
        JButton generateBtn = new JButton("Generate Questions");
        generateBtn.setBackground(new Color(74, 144, 226));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        generateBtn.addActionListener(e -> generateQuestions());

        // Start Quiz Button
        JButton quizBtn = new JButton("Start Quiz");
        quizBtn.setBackground(new Color(74, 144, 226));
        quizBtn.setForeground(Color.WHITE);
        quizBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        quizBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        quizBtn.addActionListener(e -> {
            quizQuestions = quizService.createQuiz(
                lectureArea.getText(),
                difficultyBox.getSelectedItem().toString(),
                selectedFile != null
            );
            userAnswers.clear();
            loadQuizQuestions();
            cardLayout.show(contentPanel, "QUIZ");
        });

        // Add components to sidebar
        sidebar.add(uploadBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));
        sidebar.add(lblType);
        sidebar.add(typeBox);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(lblDiff);
        sidebar.add(difficultyBox);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(lblTopic);
        sidebar.add(topicBox);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(generateBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(quizBtn);

        return sidebar;
    }

    private JPanel createUploadPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblHeader = new JLabel("Lecture Content");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBorder(new EmptyBorder(0, 0, 10, 0));

        lectureArea = new JTextArea();
        lectureArea.setLineWrap(true);
        lectureArea.setWrapStyleWord(true);
        lectureArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lectureArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(lectureArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));

        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createResultPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblHeader = new JLabel("Generated Questions");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBorder(new EmptyBorder(0, 0, 10, 0));

        outputArea = new JTextArea();
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        outputArea.setForeground(Color.WHITE);
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));

        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> cardLayout.show(contentPanel, "UPLOAD"));
         backBtn.setPreferredSize(new Dimension(500, 25));
        backBtn.setBackground(new Color(45, 45, 45));
        backBtn.setForeground(Color.WHITE);

        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createQuizPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));

        JButton submitBtn = new JButton("Submit Quiz");
        submitBtn.setPreferredSize(new Dimension(200, 25));
        submitBtn.putClientProperty("JButton.arc", 20);
        submitBtn.setBackground(new Color(45, 45, 45));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitBtn.setFocusPainted(false);
        submitBtn.setBorderPainted(false);
        

        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> cardLayout.show(contentPanel, "UPLOAD"));
        backBtn.setPreferredSize(new Dimension(200, 25));
        backBtn.putClientProperty("JButton.arc", 20);
        backBtn.setBackground(new Color(45, 45, 45));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        submitBtn.addActionListener(e -> {
            int score = 0;
            Component[] questionBlocks = questionsPanel.getComponents();

            for (Component block : questionBlocks) {
                JPanel qPanel = (JPanel) block;
                ButtonGroup group = (ButtonGroup) qPanel.getClientProperty("group");
                Question question = (Question) qPanel.getClientProperty("question");

                int selectedIndex = -1;
                int index = 0;

                for (Enumeration<AbstractButton> buttons = group.getElements();
                     buttons.hasMoreElements();) {
                    if (buttons.nextElement().isSelected()) {
                        selectedIndex = index;
                        break;
                    }
                    index++;
                }

                if (selectedIndex == question.getCorrectIndex()) {
                    score++;
                }
            }

            JOptionPane.showMessageDialog(
                this,
                "Quiz Completed!\nScore: " + score + "/" + quizQuestions.size(),
                "Quiz Result",
                JOptionPane.INFORMATION_MESSAGE
            );

            cardLayout.show(contentPanel, "UPLOAD");
        });

        // Bottom panel to hold buttons
JPanel bottomPanel = new JPanel(new BorderLayout());
bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

// Left side → Back button
bottomPanel.add(backBtn, BorderLayout.WEST);

// Right side → Submit button
bottomPanel.add(submitBtn, BorderLayout.EAST);

// Add to main panel
panel.add(scrollPane, BorderLayout.CENTER);
panel.add(bottomPanel, BorderLayout.SOUTH);
        panel.putClientProperty("questionsPanel", questionsPanel);

        return panel;
    }

    private void loadQuizQuestions() {
        JPanel quizPanel = (JPanel) contentPanel.getComponent(2);
        JPanel questionsPanel = (JPanel) quizPanel.getClientProperty("questionsPanel");
        questionsPanel.removeAll();

        for (int i = 0; i < quizQuestions.size(); i++) {
            Question q = quizQuestions.get(i);

            JPanel qPanel = new JPanel();
            qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.Y_AXIS));
            qPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
            qPanel.setBackground(new Color(50, 50, 50));

            JLabel qLabel = new JLabel("Q" + (i + 1) + ". " + q.getText());
            qLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            qLabel.setForeground(Color.WHITE);
            qPanel.add(qLabel);
            qPanel.add(Box.createVerticalStrut(8));

            ButtonGroup group = new ButtonGroup();

            for (String option : q.getOptions()) {
                JRadioButton rb = new JRadioButton(option);
                rb.setBackground(new Color(50, 50, 50));
                group.add(rb);
                qPanel.add(rb);
            }

            qPanel.putClientProperty("group", group);
            qPanel.putClientProperty("question", q);

            questionsPanel.add(qPanel);
        }

        questionsPanel.revalidate();
        questionsPanel.repaint();
    }

    private void uploadLecture() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Lecture Text File");
        chooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Documents (*.txt, *.pdf, *.doc, *.docx)",
                "txt", "pdf", "doc", "docx"
            )
        );

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            try {
                lectureArea.setText(FileUtils.readFile(selectedFile));
            } catch (Exception ex) {
                showError("Failed to read file.");
            }
        }
    }

    private void generateQuestions() {
        if (lectureArea.getText().isEmpty()) {
            showError("Upload lecture content first!");
            return;
        }

        String result = service.generate(
            lectureArea.getText(),
            typeBox.getSelectedItem().toString(),
            difficultyBox.getSelectedItem().toString(),
            selectedFile != null
        );

        outputArea.setText(result);

        try {
            FileUtils.writeFile("questions.txt", result);
        } catch (Exception ex) {
            showError("Failed to save questions.");
        }
        cardLayout.show(contentPanel, "RESULT");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(
            this, msg, "Error", JOptionPane.ERROR_MESSAGE
        );
    }
}