package service;

public class QuestionService {
    public String generate(String text, String type, String difficulty) {
        return QuestionGenerator.generate(text, type, difficulty);
    }
}
