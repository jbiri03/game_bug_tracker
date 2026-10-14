package com.bugtracker.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import com.bugtracker.dao.IssueDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/issues")
public class IssueListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IssueDAO issueDAO = new IssueDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("issues", issueDAO.findAll());
        } catch (SQLException e) {
            log("Could not load issues from the database.", e);
            request.setAttribute("issues", Collections.emptyList());
            request.setAttribute("databaseError",
                    "Issues could not be loaded. Check the database connection and try again.");
        }

        request.getRequestDispatcher("/WEB-INF/issues.jsp").forward(request, response);
    }
}