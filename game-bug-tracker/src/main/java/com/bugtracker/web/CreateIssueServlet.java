package com.bugtracker.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import com.bugtracker.dao.IssueDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/issues/create")
public class CreateIssueServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Set<String> SEVERITIES = Set.of("Low", "Medium", "High");
    private final IssueDAO issueDAO = new IssueDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String severity = request.getParameter("severity");

        title = title == null ? "" : title.trim();
        description = description == null ? "" : description.trim();

        String error = null;
        if (title.isEmpty()) {
            error = "title";
        } else if (title.length() > 120) {
            error = "title-length";
        } else if (description.isEmpty()) {
            error = "description";
        } else if (!SEVERITIES.contains(severity)) {
            error = "severity";
        }

        if (error != null) {
            response.sendRedirect(request.getContextPath() + "/issues?error=" + error);
            return;
        }

        try {
            issueDAO.create(title, description, severity);
            response.sendRedirect(request.getContextPath() + "/issues?created=1");
        } catch (SQLException e) {
            log("Could not save the submitted issue.", e);
            response.sendRedirect(request.getContextPath() + "/issues?error=save");
        }
    }
}