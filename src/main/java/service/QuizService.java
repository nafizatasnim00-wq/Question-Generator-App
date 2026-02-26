package service;

import java.util.ArrayList;
import java.util.Collections;
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

        List<Question> dbQs =
                dao.getQuestions(text, difficulty, 5);

        for (Question q : dbQs) {
            finalQuiz.add(shuffleOptions(q)); // 🔀 SHUFFLE
        }
        return finalQuiz;
    }

    //  FILE UPLOADED 
    List<String> keywords =
            QuestionGenerator.extractKeywords(text);

    for (String key : keywords) {

        if (dao.topicExists(key)) {

            List<Question> dbQuestions =
                    dao.getQuestions(key, difficulty, 5);

            for (Question q : dbQuestions) {
                finalQuiz.add(shuffleOptions(q)); // 🔀 SHUFFLE
            }
        }
    }

    //  DB MATCH FOUND 
    if (!finalQuiz.isEmpty()) {
        return finalQuiz;
    }

    //  FALLBACK 
    List<Question> generated =
            QuestionGenerator.generateQuiz(
                    text,
                    difficulty,
                    new DBOptionProvider()
            );

    // Shuffle fallback MCQs also
    List<Question> shuffledFallback = new ArrayList<>();
    for (Question q : generated) {
        shuffledFallback.add(shuffleOptions(q));
    }

    return shuffledFallback;
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
    private Question shuffleOptions(Question q) {

    List<String> options = new ArrayList<>(q.getOptions());
    String correctAnswer = options.get(q.getCorrectIndex());

    Collections.shuffle(options);

    int newCorrectIndex = options.indexOf(correctAnswer);

    return new Question(
            q.getId(),
            q.getText(),
            options,
            newCorrectIndex,
            q.getDifficulty()
    );
}
}