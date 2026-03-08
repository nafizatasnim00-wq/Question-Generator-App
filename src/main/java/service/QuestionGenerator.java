package service;
import java.util.*;
import model.Question;

public class QuestionGenerator {

    public static String generate(String text, String type, String difficulty) {

        List<String> keywords = KeywordExtractor.extractKeywords(text);
        StringBuilder sb = new StringBuilder();

        sb.append("Question Type: ").append(type).append("\n");
        sb.append("Difficulty: ").append(difficulty).append("\n\n");

        int limit = difficulty.equals("Easy") ? 5 :
                    difficulty.equals("Medium") ? 10 : 15;

        for (int i = 0; i < Math.min(limit, keywords.size()); i++) {
            String key = keywords.get(i);

            Random rand = new Random();

        switch (type) {

        case "MCQ":

        String[] mcqTemplates = {
        "What is %s?",
        "Which statement best describes %s?",
        "What is the purpose of %s?",
        "Which of the following relates to %s?"
         };

        String q = String.format(
        mcqTemplates[rand.nextInt(mcqTemplates.length)],
        key
        );

        sb.append(i + 1).append(". ").append(q).append("\n");
        sb.append("a) Option 1\nb) Option 2\nc) Option 3\nd) Option 4\n\n");
        break;


        case "Short":

        String[] shortTemplates = {
        "Explain %s.",
        "Define %s.",
        "Discuss %s.",
        "What are the uses of %s?",
        "Why is %s important?"
        };

        q = String.format(
        shortTemplates[rand.nextInt(shortTemplates.length)],
        key
        );

        sb.append(i + 1).append(". ").append(q).append("\n\n");
        break;


       case "Coding":

       String[] codingTemplates = {
        "Write a program demonstrating %s.",
        "Implement %s in a program.",
        "Write a code example of %s.",
        "Develop a program using %s.",
        "Create a simple application illustrating %s."
       };

       q = String.format(
        codingTemplates[rand.nextInt(codingTemplates.length)],
        key
       );

       sb.append(i + 1).append(". ").append(q).append("\n\n");
       break;
        }
      }

        return sb.toString();
    }



public static List<Question> generateQuiz(
        String text,
        String difficulty,
        OptionProvider provider) {

    List<String> keywords = KeywordExtractor.extractKeywords(text);
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
