package dao;

import model.Question;
import utils.DBConnection;

import java.sql.*;
import java.util.*;

public class QuestionDAO {
  
    //Get full Question objects by topic
   

    public List<Question> getQuestions(
            String topic,
            String difficulty,
            int limit) {

        List<Question> list = new ArrayList<>();

        // Null/empty guard
        if (topic == null || topic.trim().isEmpty()) {
            return list;
        }

        // Resolve to canonical topic before querying
        String canonicalTopic = findCanonicalTopic(topic).orElse(topic.trim());

        String sql = """
            SELECT q.question_id, q.question_text, q.difficulty
            FROM questions q
            JOIN topics t ON q.topic_id = t.topic_no
            WHERE LOWER(t.topic_name) = LOWER(?)
              AND q.difficulty = ?
            ORDER BY q.question_id
            LIMIT ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, canonicalTopic);
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

   
    // Get only question texts (no options/answers)
   

    public List<String> getQuestionTexts(
            String topic,
            String difficulty,
            int limit) {

        List<String> list = new ArrayList<>();

        // Null/empty guard
        if (topic == null || topic.trim().isEmpty()) {
            return list;
        }

        // Resolve canonical topic before querying
        String canonicalTopic = findCanonicalTopic(topic).orElse(topic.trim());

        String sql = """
            SELECT q.question_text
            FROM questions q
            JOIN topics t ON q.topic_id = t.topic_no
            WHERE LOWER(t.topic_name) = LOWER(?)
              AND q.difficulty = ?
            ORDER BY q.question_id
            LIMIT ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, canonicalTopic);
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

   
    // Dedicated MCQ question fetcher
   

    public List<Question> getMCQQuestions(
            String topic,
            String difficulty,
            int limit) {

        List<Question> list = new ArrayList<>();

        // Null/empty guard
        if (topic == null || topic.trim().isEmpty()) {
            return list;
        }

        // Resolve canonical topic before querying
        String canonicalTopic = findCanonicalTopic(topic).orElse(topic.trim());

        String sql = """
            SELECT q.question_id, q.question_text
            FROM questions q
            JOIN topics t ON q.topic_id = t.topic_no
            WHERE LOWER(t.topic_name) = LOWER(?)
              AND q.difficulty = ?
            ORDER BY q.question_id
            LIMIT ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, canonicalTopic);
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

  
    // Fetch random questions by difficulty only
    

    public List<Question> getQuestionsByDifficulty(String difficulty, int limit) {
        List<Question> list = new ArrayList<>();

        String sql = """
            SELECT q.question_id, q.question_text, q.difficulty
            FROM questions q
            WHERE q.difficulty = ?
            ORDER BY RANDOM()
            LIMIT ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, difficulty);
            ps.setInt(2, limit);

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

   
    // Canonical topic resolution (exact + loose match)
 

    public Optional<String> findCanonicalTopic(String keyword) {
        if (keyword == null) {
            return Optional.empty();
        }
        String normalized = keyword.trim();
        if (normalized.isEmpty()) {
            return Optional.empty();
        }

        List<String> topics = getAllTopics();

        // Exact match, case-insensitive
        for (String topic : topics) {
            if (topic.equalsIgnoreCase(normalized)) {
                return Optional.of(topic);
            }
        }

        // Loose match: ignore whitespace and punctuation
        String normalizedSimple = normalized.replaceAll("\\W", "").toLowerCase();
        for (String topic : topics) {
            String compareSimple = topic.replaceAll("\\W", "").toLowerCase();
            if (compareSimple.equals(normalizedSimple)) {
                return Optional.of(topic);
            }
        }

        return Optional.empty();
    }

   
    //  topicExists now delegates to findCanonicalTopic


    public boolean topicExists(String keyword) {
        return findCanonicalTopic(keyword).isPresent();
    }

    

    private List<String> getOptions(int qid) throws SQLException {
        List<String> options = new ArrayList<>();

        // ORDER BY option_id for consistent ordering
        String sql = "SELECT option_text FROM options WHERE question_id=? ORDER BY option_id";

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

            // Properly calculate correct option index position
            if (!rs.next()) return -1;

            int correctOptionId = rs.getInt("option_id");

            String sql2 = "SELECT option_id FROM options WHERE question_id=? ORDER BY option_id";
            try (PreparedStatement ps2 = con.prepareStatement(sql2)) {
                ps2.setInt(1, qid);
                ResultSet rs2 = ps2.executeQuery();
                int index = 0;
                while (rs2.next()) {
                    if (rs2.getInt("option_id") == correctOptionId) {
                        return index;
                    }
                    index++;
                }
            }
            return -1;
        }
    }

   
    //Fetch all topics for dropdowns and canonical matching
  

    public List<String> getAllTopics() {
        List<String> topics = new ArrayList<>();
        String sql = "SELECT topic_name FROM topics ORDER BY topic_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                topics.add(rs.getString("topic_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topics;
    }
}