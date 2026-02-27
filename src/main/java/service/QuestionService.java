package service;

import dao.QuestionDAO;
import model.Question;

import java.util.List;

public class QuestionService {

    private final QuestionDAO dao = new QuestionDAO();
   public String generate(
        String text,
        String type,
        String difficulty,
        boolean fileUploaded) {

    StringBuilder sb = new StringBuilder();
    QuestionDAO dao = new QuestionDAO();

    sb.append("Question Type: ").append(type).append("\n");
    sb.append("Difficulty: ").append(difficulty).append("\n\n");

    boolean foundInDB = false;
    int qNo = 1;

    List<String> keywords = fileUploaded
            ? QuestionGenerator.extractKeywords(text)
            : List.of(text.trim());

    for (String key : keywords) {

        if (!dao.topicExists(key)) continue;

        foundInDB = true;

        //  MCQ FROM DB
        if (type.equals("MCQ")) {

            List<Question> questions =
        dao.getMCQQuestions(key, difficulty, 5);

       for (Question q : questions) {

    sb.append(qNo++).append(". ")
                  .append(q.getText()).append("\n");

     char optionLabel = 'a';
                for (String opt : q.getOptions()) {
                    sb.append("   ")
                      .append(optionLabel++).append(") ")
                      .append(opt).append("\n");
                }
                sb.append("\n");
            }
    sb.append("\n");
        }
        
        //  NON-MCQ 
        else {
            List<String> qs =
                    dao.getQuestionTexts(key, difficulty, 5);

            for (String q : qs) {
                sb.append(qNo++).append(". ")
                  .append(q).append("\n\n");
            }
        }
    }

    //  FALLBACK 
    if (!foundInDB) {
        return QuestionGenerator.generate(text, type, difficulty);
    }

    return sb.toString();
}
}