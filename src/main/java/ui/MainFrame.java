package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import service.QuestionService;
import utils.FileUtils;
import model.Question;
import service.QuizService;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;

public class MainFrame extends JFrame {

   
    // Fields

    private JTabbedPane tabbedPane;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private JPanel uploadPanel;
    private JPanel resultPanel;
    private JPanel quizPanel;
    private JPanel topPanel;

    // Dark/light mode toggle state
    private JToggleButton darkModeToggle;
    private boolean darkMode = false;

    // Class-level label references for dynamic theming
    private JLabel lblType, lblDiff, lblTopic;
    private JLabel lblHeaderUpload, lblHeaderResult;
    private JLabel lblQuizTimer;

    private JTextArea lectureArea, outputArea;
    private JComboBox<String> typeBox, difficultyBox, topicBox;

    //  Saved uploaded files combo
    private JPanel savedFilesPanel;   // scrollable list of file entries
    private JScrollPane savedFilesScrollPane;

    private File selectedFile;
    private List<Question> quizQuestions;
    private List<Integer> userAnswers = new ArrayList<>();

    // Quiz timer fields
    private int remainingSeconds;
    private Timer quizTimer;

    private final QuizService quizService = new QuizService();
    private final QuestionService service = new QuestionService();

    
    // Constructor

    public MainFrame() {
        setupTheme();
        initUI();
        setVisible(true);
    }

   
    // Theme Setup

    private void setupTheme() {
        try {
            // light mode by default
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf theme");
        }
    }

    private void applyTheme() {
        Color mainBg  = darkMode ? new Color(43, 43, 43)  : Color.WHITE;
        Color panelBg = darkMode ? new Color(60, 63, 65)  : new Color(245, 245, 245);
        Color textFg  = darkMode ? Color.WHITE             : Color.BLACK;

        getContentPane().setBackground(mainBg);

        if (sidebarPanel  != null) sidebarPanel .setBackground(darkMode ? new Color(43, 43, 43) : new Color(230, 230, 230));
        if (uploadPanel   != null) uploadPanel  .setBackground(panelBg);
        if (resultPanel   != null) resultPanel  .setBackground(panelBg);
        if (quizPanel     != null) quizPanel    .setBackground(panelBg);

        if (lectureArea != null) { lectureArea.setBackground(panelBg); lectureArea.setForeground(textFg); }
        if (outputArea  != null) { outputArea .setBackground(panelBg); outputArea .setForeground(textFg); }

        if (typeBox       != null) { typeBox      .setBackground(darkMode ? new Color(70,70,70) : Color.WHITE); typeBox      .setForeground(textFg); }
        if (difficultyBox != null) { difficultyBox.setBackground(darkMode ? new Color(70,70,70) : Color.WHITE); difficultyBox.setForeground(textFg); }
        if (topicBox      != null) { topicBox     .setBackground(darkMode ? new Color(70,70,70) : Color.WHITE); topicBox     .setForeground(textFg); }
        if (savedFilesPanel != null) {
         savedFilesPanel.setBackground(darkMode ? new Color(50, 50, 50) : new Color(240, 240, 240));
    // Refresh entries so each row picks up new theme colors
         refreshSavedFiles();
        }
        if (tabbedPane != null) {
           tabbedPane.setBackground(darkMode ? new Color(43, 43, 43) : new Color(245, 245, 245));
           tabbedPane.setForeground(darkMode ? Color.WHITE : Color.BLACK);
           }

        if (darkModeToggle  != null) {
            darkModeToggle.setBackground(new Color(74, 144, 226));
            darkModeToggle.setForeground(Color.WHITE);
            darkModeToggle.setText(darkMode ? "Dark Mode" : "Light Mode");
            darkModeToggle.setSelected(darkMode);
        }

        if (lblType         != null) lblType        .setForeground(textFg);
        if (lblDiff         != null) lblDiff        .setForeground(textFg);
        if (lblTopic        != null) lblTopic       .setForeground(textFg);
        if (lblHeaderUpload != null) lblHeaderUpload.setForeground(textFg);
        if (lblHeaderResult != null) lblHeaderResult.setForeground(textFg);
        if (lblQuizTimer    != null) lblQuizTimer   .setForeground(textFg);
    }

    
    // UI Initialization
   

    private void initUI() {
        setTitle("Smart Question Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top bar
        topPanel    = createTopBar();
        sidebarPanel = createSidebar();

        uploadPanel = createUploadPage();
        resultPanel = createResultPage();
        quizPanel   = createQuizPage();

       // Tabs for Lecture and Results
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        UIManager.put("TabbedPane.tabInsets", new Insets(6, 16, 6, 16));
        tabbedPane.addTab("📄 Lecture Content",     uploadPanel);
        tabbedPane.addTab("📝 Generated Questions", resultPanel);

       // Disable results tab until questions are generated
        tabbedPane.setEnabledAt(1, false);

       // CardLayout wraps the tabbed view AND the quiz page
        contentPanel = new JPanel(cardLayout = new CardLayout());
        contentPanel.add(tabbedPane, "TABS");
        contentPanel.add(quizPanel,  "QUIZ");

        mainPanel.add(topPanel,      BorderLayout.NORTH);
        mainPanel.add(sidebarPanel,  BorderLayout.WEST);
        mainPanel.add(contentPanel,  BorderLayout.CENTER);

        add(mainPanel);

        refreshSavedFiles();
        applyTheme();
    }

  
    // Top Bar (Code 2 Feature)
   

    private JPanel createTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(new EmptyBorder(8, 10, 8, 10));

        darkModeToggle = new JToggleButton("Light Mode");
        darkModeToggle.setPreferredSize(new Dimension(140, 30));
        darkModeToggle.addActionListener(e -> {
            darkMode = darkModeToggle.isSelected();
            applyTheme();
        });

        top.add(darkModeToggle, BorderLayout.EAST);
        return top;
    }

    
    // Sidebar
  

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(darkMode ? new Color(43, 43, 43) : new Color(230, 230, 230));
        sidebar.setBorder(new EmptyBorder(25, 15, 25, 15));

        // Upload Button
        JButton uploadBtn = new JButton("↑ Upload Lecture");
        styleButton(uploadBtn, new Color(74, 144, 226));
        uploadBtn.addActionListener(e -> uploadLecture());

        // Type
        lblType = new JLabel("Type:");
        lblType.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        typeBox = new JComboBox<>(new String[]{"MCQ", "Short", "Coding"});
        typeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Difficulty
        lblDiff = new JLabel("Difficulty:");
        lblDiff.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficultyBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Topic
        lblTopic = new JLabel("Quiz Topic:");
        lblTopic.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        List<String> topics = new dao.QuestionDAO().getAllTopics();
        topicBox = new JComboBox<>(topics.toArray(new String[0]));
        topicBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        topicBox.setSelectedIndex(-1);
        topicBox.addActionListener(e -> {
            String sel = (String) topicBox.getSelectedItem();
            if (sel != null) lectureArea.setText(sel);
        });

        // Saved uploaded files section
        JLabel lblSavedFiles = new JLabel("Uploaded Files:");
        lblSavedFiles.setForeground(darkMode ? Color.WHITE : Color.BLACK);

       // Scrollable panel to hold file entries
        savedFilesPanel = new JPanel();
        savedFilesPanel.setLayout(new BoxLayout(savedFilesPanel, BoxLayout.Y_AXIS));
        savedFilesPanel.setBackground(darkMode ? new Color(50, 50, 50) : new Color(240, 240, 240)); 

        savedFilesScrollPane = new JScrollPane(savedFilesPanel); 
        savedFilesScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        savedFilesScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 160));
        savedFilesScrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        savedFilesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Save lecture as file button
        JButton saveLectureBtn = new JButton("Save Lecture As...");
        styleButton(saveLectureBtn, new Color(74, 144, 226));
        saveLectureBtn.addActionListener(e -> saveLectureAsFile());

        // Generate Button
        JButton generateBtn = new JButton("Generate Questions");
        styleButton(generateBtn, new Color(74, 144, 226));
        generateBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        generateBtn.addActionListener(e -> generateQuestions());

        // Start Quiz Button
        JButton quizBtn = new JButton("Start Quiz");
        styleButton(quizBtn, new Color(74, 144, 226));
        quizBtn.addActionListener(e -> {
            // Only allow quiz for MCQ type
            String type = typeBox.getSelectedItem().toString();
            if (!"MCQ".equalsIgnoreCase(type)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Quiz mode is available only for MCQ.\nUse Generate Questions for Short/Coding.",
                    "Quiz not available",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            String topicText = getSelectedTopic();
            quizQuestions = quizService.createQuiz(
                topicText,
                difficultyBox.getSelectedItem().toString(),
                selectedFile != null
            );
            userAnswers.clear();
            loadQuizQuestions();
            // Start countdown timer when quiz begins
            startQuizTimer();
            cardLayout.show(contentPanel, "QUIZ");
        });

        // Assemble sidebar
        sidebar.add(uploadBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(lblType);
        sidebar.add(typeBox);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(lblDiff);
        sidebar.add(difficultyBox);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(lblTopic);
        sidebar.add(topicBox);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(lblSavedFiles);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(savedFilesScrollPane);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(saveLectureBtn);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(generateBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(quizBtn);

        return sidebar;
    }

   
    // Pages
   

    private JPanel createUploadPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(darkMode ? new Color(60, 63, 65) : new Color(245, 245, 245));

        lblHeaderUpload = new JLabel("Lecture Content");
        lblHeaderUpload.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHeaderUpload.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        lblHeaderUpload.setBorder(new EmptyBorder(0, 0, 10, 0));

        lectureArea = new JTextArea();
        lectureArea.setLineWrap(true);
        lectureArea.setWrapStyleWord(true);
        lectureArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lectureArea.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        lectureArea.setBackground(darkMode ? new Color(60, 63, 65) : Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(lectureArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));

        panel.add(lblHeaderUpload, BorderLayout.NORTH);
        panel.add(scrollPane,      BorderLayout.CENTER);

        return panel;
    }

    private JPanel createResultPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(darkMode ? new Color(60, 63, 65) : new Color(245, 245, 245));

        lblHeaderResult = new JLabel("Generated Questions");
        lblHeaderResult.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHeaderResult.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        lblHeaderResult.setBorder(new EmptyBorder(0, 0, 10, 0));

        outputArea = new JTextArea();
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        outputArea.setForeground(darkMode ? Color.WHITE : Color.BLACK);
        outputArea.setBackground(darkMode ? new Color(60, 63, 65) : Color.WHITE);
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));

        JButton backBtn = new JButton("← Back");
        backBtn.setPreferredSize(new Dimension(500, 25));
        backBtn.setBackground(new Color(45, 45, 45));
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(e -> tabbedPane.setSelectedIndex(0));

        panel.add(lblHeaderResult, BorderLayout.NORTH);
        panel.add(scrollPane,      BorderLayout.CENTER);
        panel.add(backBtn,         BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createQuizPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(darkMode ? new Color(60, 63, 65) : new Color(245, 245, 245));

        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(darkMode ? new Color(60, 63, 65) : new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));

        // Timer label at the top of quiz page
        lblQuizTimer = new JLabel("Time remaining: 00:00", SwingConstants.CENTER);
        lblQuizTimer.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblQuizTimer.setForeground(darkMode ? Color.WHITE : Color.BLACK);

        // Restart quiz button
        JButton restartBtn = new JButton("Restart Quiz");
        restartBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        restartBtn.setBackground(new Color(60, 180, 75));
        restartBtn.setForeground(Color.WHITE);
        restartBtn.addActionListener(e -> restartQuiz());

        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        backBtn.setBackground(new Color(120, 120, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.putClientProperty("JButton.arc", 20);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.addActionListener(e -> {
        stopQuizTimer();
        tabbedPane.setSelectedIndex(0);        // Go back to Lecture tab
        cardLayout.show(contentPanel, "TABS"); // Show the tabbed view
        });

        JButton submitBtn = new JButton("Submit Quiz");
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        submitBtn.setBackground(new Color(74, 144, 226));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.putClientProperty("JButton.arc", 20);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorderPainted(false);
        // Submit delegates to performQuizSubmission
        submitBtn.addActionListener(e -> performQuizSubmission(false));

        //Three-button centered bar (Back, Restart, Submit)
        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonBar.setOpaque(false);
        buttonBar.add(backBtn);
        buttonBar.add(restartBtn);
        buttonBar.add(submitBtn);

        panel.add(lblQuizTimer, BorderLayout.NORTH);
        panel.add(scrollPane,   BorderLayout.CENTER);
        panel.add(buttonBar,    BorderLayout.SOUTH);
        panel.putClientProperty("questionsPanel", questionsPanel);

        return panel;
    }

   
    // Quiz Logic
  

    // Centralized quiz submission with force-submit support
    private void performQuizSubmission(boolean forceSubmit) {
        JPanel qPanel = quizPanel;
        JPanel questionsPanel = (JPanel) qPanel.getClientProperty("questionsPanel");

        Component[] blocks  = questionsPanel.getComponents();
        int score           = 0;
        boolean missingAnswer = false;

        for (Component block : blocks) {
            JPanel qp          = (JPanel) block;
            ButtonGroup group  = (ButtonGroup) qp.getClientProperty("group");
            Question question  = (Question)    qp.getClientProperty("question");

            int selectedIndex  = -1;
            int index          = 0;

            for (Enumeration<AbstractButton> btns = group.getElements(); btns.hasMoreElements();) {
                if (btns.nextElement().isSelected()) { selectedIndex = index; break; }
                index++;
            }

            if (selectedIndex == -1) {
                missingAnswer = true;
            } else if (selectedIndex == question.getCorrectIndex()) {
                score++;
            }
        }

        // Warn if unanswered questions remain (unless timer forced submission)
        if (!forceSubmit && missingAnswer) {
            JOptionPane.showMessageDialog(
                this,
                "Please answer all questions before submitting the quiz.",
                "Incomplete Quiz",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        stopQuizTimer();

        String message = forceSubmit && missingAnswer
            ? "Time is up! Quiz auto-submitted.\nScore: " + score + "/" + quizQuestions.size()
            : "Quiz Completed!\nScore: "                  + score + "/" + quizQuestions.size();

        // Highlight correct answers after submission
        markCorrectAnswers(questionsPanel);

        JOptionPane.showMessageDialog(this, message, "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
        // Stay on quiz page for review; user presses Back or Restart to continue
    }

    // Restart quiz with fresh questions and reset timer
    private void restartQuiz() {
        String topicText = getSelectedTopic();
        if (topicText.isEmpty()) {
            showError("No topic selected to restart quiz.");
            return;
        }
        quizQuestions = quizService.createQuiz(
            topicText,
            difficultyBox.getSelectedItem().toString(),
            selectedFile != null
        );
        userAnswers.clear();
        loadQuizQuestions();
        remainingSeconds = quizQuestions.size() * 30;
        updateTimerLabel();
        startQuizTimer();
    }

    //Countdown timer (30s per question)
    private void startQuizTimer() {
        if (quizTimer != null && quizTimer.isRunning()) quizTimer.stop();

        remainingSeconds = quizQuestions.size() * 30;
        updateTimerLabel();

        quizTimer = new Timer(1000, e -> {
            remainingSeconds--;
            updateTimerLabel();
            if (remainingSeconds <= 0) {
                quizTimer.stop();
                performQuizSubmission(true);
            }
        });
        quizTimer.start();
    }

    private void stopQuizTimer() {
        if (quizTimer != null) quizTimer.stop();
    }

    private void updateTimerLabel() {
        int m = remainingSeconds / 60;
        int s = remainingSeconds % 60;
        lblQuizTimer.setText(String.format("Time remaining: %02d:%02d", m, s));
    }

    // Mark correct answers with ✔ and disable options after submit
    private void markCorrectAnswers(JPanel questionsPanel) {
        for (Component block : questionsPanel.getComponents()) {
            if (!(block instanceof JPanel)) continue;
            JPanel qp       = (JPanel) block;
            Question question = (Question) qp.getClientProperty("question");
            if (question == null) continue;

            int correctIndex = question.getCorrectIndex();
            int rbIndex      = 0;

            for (Component c : qp.getComponents()) {
                if (c instanceof JRadioButton rb) {
                    if (rbIndex == correctIndex && !rb.getText().endsWith(" ✔")) {
                        rb.setText(rb.getText().replace(" ✔", "") + " ✔");
                    }
                    rb.setEnabled(false);
                    rb.setForeground(darkMode ? Color.WHITE : Color.BLACK);
                    rbIndex++;
                }
            }
        }
    }

    private void loadQuizQuestions() {
        JPanel qPanel = quizPanel;
        JPanel questionsPanel = (JPanel) qPanel.getClientProperty("questionsPanel");
        questionsPanel.removeAll();

        for (int i = 0; i < quizQuestions.size(); i++) {
            Question q = quizQuestions.get(i);

            JPanel qp = new JPanel();
            qp.setLayout(new BoxLayout(qp, BoxLayout.Y_AXIS));
            qp.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
            qp.setBackground(darkMode ? new Color(50, 50, 50) : Color.WHITE);

            JLabel qLabel = new JLabel("Q" + (i + 1) + ". " + q.getText());
            qLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            qLabel.setForeground(darkMode ? Color.WHITE : Color.BLACK);
            qp.add(qLabel);
            qp.add(Box.createVerticalStrut(8));

            ButtonGroup group = new ButtonGroup();
            for (String option : q.getOptions()) {
                JRadioButton rb = new JRadioButton(option);
                rb.setBackground(darkMode ? new Color(50, 50, 50) : Color.WHITE);
                rb.setForeground(darkMode ? Color.WHITE : Color.BLACK);
                group.add(rb);
                qp.add(rb);
            }

            qp.putClientProperty("group", group);
            qp.putClientProperty("question", q);
            questionsPanel.add(qp);
        }

        questionsPanel.revalidate();
        questionsPanel.repaint();
    }

  
    // File Handling
   

    private void uploadLecture() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Lecture Text File");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Documents (*.txt, *.pdf, *.doc, *.docx)", "txt", "pdf", "doc", "docx"
        ));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            try {
                lectureArea.setText(FileUtils.readFile(selectedFile));

                // Persist uploaded file and refresh saved list
                File saved = FileUtils.copyToUploadDir(selectedFile);
                selectedFile = saved;
                refreshSavedFiles();
                
                if (topicBox != null) topicBox.setSelectedIndex(-1);

            } catch (IOException ex) {
                showError("Failed to read or save file.");
            } catch (Exception ex) {
                showError("Failed to read file.");
            }
        }
    }

    //  Prefer lecture text, fallback to topic combo selection
    private String getSelectedTopic() {
        if (lectureArea != null && !lectureArea.getText().trim().isEmpty()) {
            return lectureArea.getText().trim();
        }
        if (topicBox != null && topicBox.getSelectedItem() != null) {
            return topicBox.getSelectedItem().toString().trim();
        }
        return "";
    }

    // Refresh saved files from upload directory
    private void refreshSavedFiles() {
    if (savedFilesPanel == null) return;

    savedFilesPanel.removeAll();

    List<File> files = FileUtils.listUploadFiles();

    if (files.isEmpty()) {
        JLabel emptyLbl = new JLabel("No uploaded files.");
        emptyLbl.setForeground(Color.GRAY);
        emptyLbl.setFont(new Font("SansSerif", Font.ITALIC, 11));
        emptyLbl.setBorder(new EmptyBorder(5, 8, 5, 8));
        savedFilesPanel.add(emptyLbl);
    }

    for (File file : files) {
        savedFilesPanel.add(createFileEntry(file));
    }

    savedFilesPanel.revalidate();
    savedFilesPanel.repaint();
  }

  private JPanel createFileEntry(File file) {
    JPanel entry = new JPanel(new BorderLayout(4, 0));
    entry.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    entry.setPreferredSize(new Dimension(300, 36)); // Give it a base preferred width so scroll kicks in
    entry.setBorder(new EmptyBorder(3, 6, 3, 6));
    entry.setBackground(darkMode ? new Color(55, 55, 55) : new Color(235, 235, 235));

    // File name label — click to load
    JLabel nameLabel = new JLabel(file.getName());
    nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
    nameLabel.setPreferredSize(null); // Let label grow naturally instead of truncating
    nameLabel.setForeground(darkMode ? Color.WHITE : Color.BLACK);
    nameLabel.setToolTipText(file.getAbsolutePath());
    nameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    nameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            loadFileIntoEditor(file);
        }
    });

    // Right-side button panel
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
    btnPanel.setOpaque(false);

    // × Unlink button — removes entry from panel without deleting file
    JButton unlinkBtn = new JButton("×");
    unlinkBtn.setToolTipText("Remove from list (keeps file on PC)");
    unlinkBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
    unlinkBtn.setForeground(Color.RED);
    unlinkBtn.setBackground(darkMode ? new Color(70, 70, 70) : new Color(220, 220, 220));
    unlinkBtn.setBorder(BorderFactory.createEmptyBorder(1, 6, 1, 6));
    unlinkBtn.setFocusPainted(false);
    unlinkBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    unlinkBtn.addActionListener(e -> {
        // Only removes from the panel view, file stays on disk
        savedFilesPanel.remove(entry);
        savedFilesPanel.revalidate();
        savedFilesPanel.repaint();
    });

    // 🗑 Delete button — permanently deletes file from PC
    JButton deleteBtn = new JButton("🗑");
    deleteBtn.setToolTipText("Permanently delete file from PC");
    deleteBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
    deleteBtn.setForeground(darkMode ? Color.GREEN : Color.BLUE );
    deleteBtn.setBackground(darkMode ? new Color(70, 70, 70) : new Color(220, 220, 220));
    deleteBtn.setBorder(BorderFactory.createEmptyBorder(1, 6, 1, 6));
    deleteBtn.setFocusPainted(false);
    deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    deleteBtn.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Permanently delete \"" + file.getName() + "\" from your PC?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = FileUtils.deleteUploadedFile(file);
            if (deleted) {
                // Clear editor if this file was loaded
                if (selectedFile != null &&
                    selectedFile.getAbsolutePath().equals(file.getAbsolutePath())) {
                    lectureArea.setText("");
                    selectedFile = null;
                }
                refreshSavedFiles();
            } else {
                showError("Could not delete file. It may be in use.");
            }
        }
    });

    btnPanel.add(unlinkBtn);
    btnPanel.add(deleteBtn);

    entry.add(nameLabel, BorderLayout.CENTER);
    entry.add(btnPanel,  BorderLayout.EAST);

    return entry;
}

    // Load a previously saved uploaded file
    private void loadFileIntoEditor(File file) {
    if (!file.exists()) {
        showError("File no longer exists on disk.");
        refreshSavedFiles();
        return;
    }
    try {
        lectureArea.setText(FileUtils.readFile(file));
        selectedFile = file;
        if (topicBox != null) topicBox.setSelectedIndex(-1);
    } catch (Exception ex) {
        showError("Failed to load file: " + file.getName());
    }
}

    // Save typed/pasted lecture content as a named file
    private void saveLectureAsFile() {
        String content = lectureArea.getText().trim();
        if (content.isEmpty()) {
            showError("Lecture text is empty. Please type or paste content first.");
            return;
        }

        String name = JOptionPane.showInputDialog(this, "Enter file name (without extension):", "Save Lecture", JOptionPane.PLAIN_MESSAGE);
        if (name == null) return;
        name = name.trim();
        if (name.isEmpty()) { showError("File name cannot be empty."); return; }

        name = name.replaceAll("[\\/:*?\"<>|]", "_");
        if (!name.toLowerCase().endsWith(".txt")) name += ".txt";

        File target = new File(FileUtils.getUploadDir(), name);
        int suffix = 1;
        while (target.exists()) {
            int dot = name.lastIndexOf('.');
            String base = dot > 0 ? name.substring(0, dot) : name;
            String ext  = dot > 0 ? name.substring(dot)    : "";
            target = new File(FileUtils.getUploadDir(), base + "(" + suffix++ + ")" + ext);
        }

        try {
            FileUtils.writeFile(target.getAbsolutePath(), content);
            selectedFile = target;
            refreshSavedFiles();
            JOptionPane.showMessageDialog(this, "Saved lecture as " + target.getName(), "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Failed to save lecture to file.");
        }
    }

  
    // Question Generation
   

    private void generateQuestions() {
        //  Use getSelectedTopic() for flexible input resolution
        String inputText = getSelectedTopic();
        if (inputText.isEmpty()) {
            showError("Upload lecture content or select a topic first!");
            return;
        }

        String result = service.generate(
            inputText,
            typeBox.getSelectedItem().toString(),
            difficultyBox.getSelectedItem().toString(),
            selectedFile != null   // Pass fileUploaded flag
        );

        outputArea.setText(result);

        try {
            FileUtils.writeFile("questions.txt", result);
        } catch (Exception ex) {
            showError("Failed to save questions.");
        }
        tabbedPane.setEnabledAt(1, true);
        tabbedPane.setSelectedIndex(1);
        cardLayout.show(contentPanel, "TABS"); // Make sure tabs view is showing
    }

    // Utility
    

   
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}