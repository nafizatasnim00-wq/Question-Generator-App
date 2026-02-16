package service;
import java.util.*;
import model.Question;
import java.util.List;

public class QuestionGenerator {

    public static String generate(String text, String type, String difficulty) {

        List<String> keywords = extractKeywords(text);
        StringBuilder sb = new StringBuilder();

        sb.append("Question Type: ").append(type).append("\n");
        sb.append("Difficulty: ").append(difficulty).append("\n\n");

        int limit = difficulty.equals("Easy") ? 3 :
                    difficulty.equals("Medium") ? 5 : 7;

        for (int i = 0; i < Math.min(limit, keywords.size()); i++) {
            String key = keywords.get(i);

            switch (type) {
                case "MCQ":
                    sb.append(i+1).append(". What is ").append(key).append("?\n");
                    sb.append("a) Option 1\nb) Option 2\nc) Option 3\nd) Option 4\n\n");
                    break;

                case "Short":
                    sb.append(i+1).append(". Explain ").append(key).append(".\n\n");
                    break;

                case "Coding":
                    sb.append(i+1).append(". Write a program related to ")
                      .append(key).append(".\n\n");
                    break;
            }
        }

        return sb.toString();
    }

    public static List<String> extractKeywords(String text) {
        String[] words = text.split("\\W+");
        Map<String, Integer> freq = new HashMap<>();

        for (String w : words) {
            if (w.length() > 4) {
                freq.put(w.toLowerCase(), freq.getOrDefault(w.toLowerCase(), 0) + 1);
            }
        }

        List<String> keywords = new ArrayList<>(freq.keySet());
        keywords.sort((a, b) -> freq.get(b) - freq.get(a));
        return keywords;
    }



public static List<Question> generateQuiz(
        String text,
        String difficulty,
        OptionProvider provider) {

    List<String> keywords = extractKeywords(text);
    List<Question> list = new ArrayList<>();

    for (String key : keywords) {

        List<String> options = provider.getOptions(key, difficulty);
        int correct = provider.getCorrectIndex(key);

        if (options.isEmpty()) continue;

        list.add(new Question(
                "What is " + key + "?",
                options,
                correct
        ));
    }
    return list;
}}
