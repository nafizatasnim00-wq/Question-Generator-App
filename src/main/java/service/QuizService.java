package service;

import java.util.ArrayList;
import java.util.List;
import model.Question;
import dao.QuestionDAO;

public class QuizService {

public List<Question> createQuiz(
        String text,
        String difficulty,
        boolean fileUploaded) {

    QuestionDAO dao = new QuestionDAO();
    List<Question> finalQuiz = new ArrayList<>();

    // 1️⃣ No file → DB quiz directly
    if (!fileUploaded) {
        return dao.getQuestions(text, difficulty, 5);
    }

    // 2️⃣ File uploaded → extract ALL keywords
    List<String> keywords =
            QuestionGenerator.extractKeywords(text);

    // 3️⃣ For EACH keyword, try DB match
    for (String key : keywords) {

        if (dao.topicExists(key)) {

            List<Question> dbQuestions =
                    dao.getQuestions(key, difficulty, 5);

            finalQuiz.addAll(dbQuestions);
        }
    }

    // 4️⃣ If at least ONE DB topic matched → return DB quiz
    if (!finalQuiz.isEmpty()) {
        return finalQuiz;
    }

    // 5️⃣ Otherwise → fallback to logic-based quiz
    return QuestionGenerator.generateQuiz(
            text,
            difficulty,
            new DBOptionProvider()
    );
}



    public int calculateScore(List<Question> questions, List<Integer> answers) {
        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getCorrectIndex() == answers.get(i)) {
                score++;
            }
        }
        return score;
    }
}