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

    //  NO FILE 
    if (!fileUploaded) {

        return dao.getQuestions(text, difficulty, 5);
    }

    //  FILE UPLOADED 
    List<String> keywords =
            KeywordExtractor.extractKeywords(text);

    for (String key : keywords) {

        if (dao.topicExists(key)) {

            List<Question> dbQuestions =
                    dao.getQuestions(key, difficulty, 5);

            finalQuiz.addAll(dbQuestions);
        }
    }

    //  DB MATCH FOUND 
    if (!finalQuiz.isEmpty()) {
        return finalQuiz;
    }

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