package service;

import dao.QuestionDAO;
import model.Question;
import java.util.List;

public class DBOptionProvider implements OptionProvider {

    private final QuestionDAO dao = new QuestionDAO();

    @Override
    public List<String> getOptions(String keyword, String difficulty) {
        List<Question> q =
                dao.getQuestions(keyword, difficulty, 1);
        return q.isEmpty() ? List.of() : q.get(0).getOptions();
    }

    @Override
    public int getCorrectIndex(String keyword) {
        return 0;
    }
}
