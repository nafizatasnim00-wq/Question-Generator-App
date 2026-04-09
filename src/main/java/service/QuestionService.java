package service;

import dao.QuestionDAO;
import model.Question;

import java.util.List;
import java.util.Optional;

public class QuestionService {

    private final QuestionDAO dao = new QuestionDAO();

    public String generate(
            String text,
            String type,
            String difficulty,
            boolean fileUploaded) {

        StringBuilder sb = new StringBuilder();

        sb.append("Question Type: ").append(type).append("\n");
        sb.append("Difficulty: ").append(difficulty).append("\n\n");

        boolean foundInDB = false;
        int qNo = 1;

        // Code 1 Feature: Extract multiple keywords if file uploaded, else treat as single topic
        List<String> keywords = fileUploaded
                ? KeywordExtractor.extractKeywords(text)
                : List.of(text.trim());

        for (String key : keywords) {

            // Code 2 Feature: Use canonical topic lookup instead of simple topicExists check
            Optional<String> canonicalTopic = dao.findCanonicalTopic(key.trim());

            if (canonicalTopic.isEmpty()) {
                // Code 2 Feature: Log when topic is not found
                System.out.println("QuestionService: Topic not found, skipping key='" + key.trim() + "'");
                continue;
            }

            String topic = canonicalTopic.get();

            // Code 1 Feature: Branch MCQ and Non-MCQ into separate DAO calls
            List<Question> questions;
            if ("MCQ".equalsIgnoreCase(type)) {
                questions = dao.getMCQQuestions(topic, difficulty, 5);
            } else {
                questions = dao.getQuestions(topic, difficulty, 5);
            }

            if (questions.isEmpty()) {
                // Code 2 Feature: Log when no questions found for difficulty
                System.out.println("QuestionService: No DB questions for topic='" + topic + "', difficulty='" + difficulty + "'");
                continue;
            }

            foundInDB = true;

            sb.append("Topic: ").append(topic).append("\n\n");

            for (Question q : questions) {
                sb.append(qNo++).append(". ").append(q.getText()).append("\n");

                if ("MCQ".equalsIgnoreCase(type)) {
                    // Code 1 Feature: Display MCQ options with letter labels
                    List<String> options = q.getOptions();
                    for (int j = 0; j < options.size(); j++) {
                        sb.append("   ")
                          .append((char) ('a' + j)).append(") ")
                          .append(options.get(j)).append("\n");
                    }
                    // Code 2 Feature: Display correct answer after options
                    sb.append("   Correct answer: ")
                      .append((char) ('a' + q.getCorrectIndex())).append("\n");
                }

                sb.append("\n");
            }

            sb.append("\n");
        }

        // Code 1 Feature: Fallback to AI generator if nothing found in DB
        if (!foundInDB) {
            System.out.println("QuestionService: No topics found in DB, falling back to generator.");
            return QuestionGenerator.generate(text, type, difficulty);
        }

        return sb.toString();
    }
}