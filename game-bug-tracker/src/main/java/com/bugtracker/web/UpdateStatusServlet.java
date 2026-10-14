package com.bugtracker.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import com.bugtracker.dao.IssueDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/issues/status")
public class UpdateStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Set<String> STATUSES =
            Set.of("Open", "In Progress", "Resolved");

    private final IssueDAO issueDAO = new IssueDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idValue = request.getParameter("id");
        String status = request.getParameter("status");

        int issueId;
        try {
            issueId = Integer.parseInt(idValue);
            if (issueId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/issues?error=update");
            return;
        }

        if (!STATUSES.contains(status)) {
            response.sendRedirect(request.getContextPath() + "/issues?error=update");
            return;
        }

        try {
            boolean updated = issueDAO.updateStatus(issueId, status);
            String result = updated ? "updated=1" : "error=update";
            response.sendRedirect(request.getContextPath() + "/issues?" + result);
        } catch (SQLException e) {
            log("Could not update issue status.", e);
            response.sendRedirect(request.getContextPath() + "/issues?error=update");
        }
    }
}