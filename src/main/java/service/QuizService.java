package service;

import java.util.List;
import model.Question;

public class QuizService {

    public List<Question> createQuiz(String lectureText) {
        return QuestionGenerator.generateQuiz(lectureText, 5);
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
