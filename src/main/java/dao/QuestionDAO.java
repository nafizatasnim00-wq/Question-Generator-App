package dao;

import model.Question;
import utils.DBConnection;

import java.sql.*;
import java.util.*;

public class QuestionDAO {

    public List<Question> getQuestions(
            String topic,
            String difficulty,
            int limit) {

        List<Question> list = new ArrayList<>();

        String sql = """
            SELECT q.question_id, q.question_text, q.difficulty
            FROM questions q
            JOIN topics t ON q.topic_id = t.topic_no
            WHERE LOWER(t.topic_name) = LOWER(?)
              AND q.difficulty = ?
            LIMIT ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, topic);
            ps.setString(2, difficulty);
            ps.setInt(3, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int qid = rs.getInt("question_id");

                list.add(new Question(
                        qid,
                        rs.getString("question_text"),
                        getOptions(qid),
                        getCorrectIndex(qid),
                        difficulty
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private List<String> getOptions(int qid) throws SQLException {
        List<String> options = new ArrayList<>();

        String sql = "SELECT option_text FROM options WHERE question_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, qid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                options.add(rs.getString("option_text"));
            }
        }
        return options;
    }

    private int getCorrectIndex(int qid) throws SQLException {
        String sql = """
            SELECT option_id FROM options
            WHERE question_id=? AND is_correct=1
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, qid);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? 0 : -1;
        }
    }
    
    public boolean topicExists(String keyword) {

    String sql =
        "SELECT 1 FROM topics WHERE LOWER(topic_name) = LOWER(?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, keyword);
        ResultSet rs = ps.executeQuery();
        return rs.next();

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public List<String> getQuestionTexts(
        String topic,
        String difficulty,
        int limit) {

    List<String> list = new ArrayList<>();

    String sql = """
        SELECT q.question_text
        FROM questions q
        JOIN topics t ON q.topic_id = t.topic_no
        WHERE LOWER(t.topic_name) = LOWER(?)
          AND q.difficulty = ?
        LIMIT ?
    """;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, topic);
        ps.setString(2, difficulty);
        ps.setInt(3, limit);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(rs.getString("question_text"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}
public List<Question> getMCQQuestions(
        String topic,
        String difficulty,
        int limit) {

    List<Question> list = new ArrayList<>();

    String sql = """
        SELECT q.question_id, q.question_text
        FROM questions q
        JOIN topics t ON q.topic_id = t.topic_no
        WHERE LOWER(t.topic_name) = LOWER(?)
          AND q.difficulty = ?
        LIMIT ?
    """;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, topic);
        ps.setString(2, difficulty);
        ps.setInt(3, limit);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int qid = rs.getInt("question_id");

            list.add(new Question(
                    qid,
                    rs.getString("question_text"),
                    getOptions(qid),
                    getCorrectIndex(qid),
                    difficulty
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}
}
