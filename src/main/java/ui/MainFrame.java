package ui;

import javax.swing.*;
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

   /*private JTextArea lectureArea, outputArea;
    private JComboBox<String> typeBox, difficultyBox;
    private File selectedFile;*/
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextArea lectureArea, outputArea;
    private JComboBox<String> typeBox, difficultyBox;
    private File selectedFile;
    private List<Question> quizQuestions;
    private int currentQuestionIndex = 0;
    private List<Integer> userAnswers = new ArrayList<>();
    private final QuizService quizService = new QuizService();



    private final QuestionService service = new QuestionService();

    public MainFrame() {
        initUI();
        setVisible(true);
    }

    private void initUI() {
        setTitle("Smart Question Generator");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*setLayout(new BorderLayout());

        setIconImage(new ImageIcon("resources/icon.png").getImage());

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);*/
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createUploadPage(), "UPLOAD");
        mainPanel.add(createResultPage(), "RESULT");
        mainPanel.add(createQuizPage(), "QUIZ");


        add(mainPanel);
    }

    
   private JPanel createUploadPage() {
    JPanel panel = new JPanel(new BorderLayout());

    JPanel topPanel = new JPanel();
    JButton uploadBtn = new JButton("Upload Lecture");
    JButton generateBtn = new JButton("Generate Questions");
    JButton quizBtn = new JButton("Start Quiz");



    typeBox = new JComboBox<>(new String[]{"MCQ", "Short", "Coding"});
    difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});

    topPanel.add(uploadBtn);
    topPanel.add(new JLabel("Type:"));
    topPanel.add(typeBox);
    topPanel.add(new JLabel("Difficulty:"));
    topPanel.add(difficultyBox);
    topPanel.add(generateBtn);
    topPanel.add(quizBtn);

    lectureArea = new JTextArea();
    
    lectureArea.setBorder(
        BorderFactory.createTitledBorder("Lecture Content")
    );

    panel.add(topPanel, BorderLayout.NORTH);
    panel.add(new JScrollPane(lectureArea), BorderLayout.CENTER);

    uploadBtn.addActionListener(e -> uploadLecture());
    generateBtn.addActionListener(e -> generateQuestions());

    quizBtn.addActionListener(e -> {

        if (lectureArea.getText().isEmpty()) {
            showError("Upload lecture content first!");
            return;
        }

        quizQuestions = quizService.createQuiz(lectureArea.getText());
        loadQuizQuestions();
        cardLayout.show(mainPanel, "QUIZ");
    });

    return panel;
}

   private JPanel createResultPage() {
    JPanel panel = new JPanel(new BorderLayout());

    outputArea = new JTextArea();
    outputArea.setBorder(
        BorderFactory.createTitledBorder("Generated Questions")
    );

    JButton backBtn = new JButton("← Back");

    backBtn.addActionListener(e ->
        cardLayout.show(mainPanel, "UPLOAD")
    );

    panel.add(backBtn, BorderLayout.NORTH);
    panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

    return panel;
}

private JPanel createQuizPage() {

    JPanel panel = new JPanel(new BorderLayout());

    JPanel questionsPanel = new JPanel();
    questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));

    JScrollPane scrollPane = new JScrollPane(questionsPanel);

    JButton submitBtn = new JButton("Submit Quiz");

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
            "Quiz Completed!\nScore: " + score + "/" + quizQuestions.size()
        );

        cardLayout.show(mainPanel, "UPLOAD");
    });

    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(submitBtn, BorderLayout.SOUTH);

    // store reference for loading
    panel.putClientProperty("questionsPanel", questionsPanel);

    return panel;
}

private void loadQuizQuestions() {

    JPanel quizPanel = (JPanel) mainPanel.getComponent(2);
    JPanel questionsPanel =
        (JPanel) quizPanel.getClientProperty("questionsPanel");

    questionsPanel.removeAll();

    for (int i = 0; i < quizQuestions.size(); i++) {
        Question q = quizQuestions.get(i);

        JPanel qPanel = new JPanel();
        qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.Y_AXIS));
        qPanel.setBorder(
            BorderFactory.createEmptyBorder(0, 0, 15, 0)
        );


        JLabel qLabel = new JLabel("Q" + (i + 1)+"." + q.getText());
        qPanel.add(qLabel);

        ButtonGroup group = new ButtonGroup();

        for (String option : q.getOptions()) {
            JRadioButton rb = new JRadioButton(option);
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


        if (chooser.showOpenDialog(this)
                == JFileChooser.APPROVE_OPTION) {

            selectedFile = chooser.getSelectedFile();

            try {
                lectureArea.setText(
                    FileUtils.readFile(selectedFile)
                );
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
            difficultyBox.getSelectedItem().toString()
        );

        outputArea.setText(result);

        try {
            FileUtils.writeFile("questions.txt", result);
        } catch (Exception ex) {
            showError("Failed to save questions.");
        }
        cardLayout.show(mainPanel, "RESULT");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(
            this, msg, "Error", JOptionPane.ERROR_MESSAGE
        );
    }
}
