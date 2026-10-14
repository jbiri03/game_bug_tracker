package com.bugtracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bugtracker.model.Issue;

public class IssueDAO {
    private static final String FIND_ALL_SQL =
            "SELECT id, title, description, severity, status, created_at "
            + "FROM issues ORDER BY created_at DESC";

    public List<Issue> findAll() throws SQLException {
        List<Issue> issues = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Issue issue = new Issue();
                issue.setId(resultSet.getInt("id"));
                issue.setTitle(resultSet.getString("title"));
                issue.setDescription(resultSet.getString("description"));
                issue.setSeverity(resultSet.getString("severity"));
                issue.setStatus(resultSet.getString("status"));

                java.sql.Timestamp createdAt = resultSet.getTimestamp("created_at");
                if (createdAt != null) {
                    issue.setCreatedAt(createdAt.toLocalDateTime());
                }

                issues.add(issue);
            }
        }

        return issues;
    }
    
    public void create(String title, String description, String severity)
            throws SQLException {
        String sql = "INSERT INTO issues (title, description, severity, status) "
                + "VALUES (?, ?, ?, 'Open')";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, severity);
            statement.executeUpdate();
        }
    }
    
    public boolean updateStatus(int issueId, String status) throws SQLException {
        String sql = "UPDATE issues SET status = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, issueId);
            return statement.executeUpdate() == 1;
        }
    }
}