<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Game Bug Tracker</title>
    <style>
        :root {
            color-scheme: light;
            --ink: #20243a;
            --muted: #69708a;
            --accent: #6258d9;
            --accent-dark: #4d43bd;
            --surface: #ffffff;
            --line: #e5e7f0;
            --background: #f5f6fb;
            --error: #a82d45;
        }

        * {
            box-sizing: border-box;
        }

        body {
            max-width: 940px;
            margin: 0 auto;
            padding: 3rem 1.25rem;
            background: var(--background);
            color: var(--ink);
            font-family: "Segoe UI", Arial, sans-serif;
            line-height: 1.6;
        }

        h1 {
            margin: 0;
            font-size: clamp(2rem, 5vw, 3rem);
            letter-spacing: -0.04em;
        }

        h1 + p {
            margin: .4rem 0 2rem;
        }

        a {
            color: var(--accent-dark);
            font-weight: 600;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        body > form {
            margin: 1.5rem 0 2.5rem;
            padding: 1.5rem;
            background: var(--surface);
            border: 1px solid var(--line);
            border-radius: 16px;
            box-shadow: 0 8px 24px rgb(32 36 58 / 6%);
        }

        label {
            display: inline-block;
            margin-bottom: .35rem;
            color: var(--ink);
            font-size: .92rem;
            font-weight: 600;
        }

        input[type="text"],
        textarea,
        select {
            width: 100%;
            max-width: 100%;
            padding: .7rem .8rem;
            border: 1px solid #d7d9e5;
            border-radius: 9px;
            background: #fff;
            color: var(--ink);
            font: inherit;
        }

        textarea {
            resize: vertical;
        }

        input:focus,
        textarea:focus,
        select:focus {
            border-color: var(--accent);
            outline: 3px solid rgb(98 88 217 / 16%);
        }

        button {
            padding: .7rem 1rem;
            border: 0;
            border-radius: 9px;
            background: var(--accent);
            color: #fff;
            font: inherit;
            font-weight: 650;
            cursor: pointer;
            transition: background .15s ease, transform .15s ease;
        }

        button:hover {
            background: var(--accent-dark);
            transform: translateY(-1px);
        }

        .issue {
            margin: 1rem 0;
            padding: 1.35rem 1.5rem;
            border: 1px solid var(--line);
            border-radius: 16px;
            background: var(--surface);
            box-shadow: 0 6px 20px rgb(32 36 58 / 5%);
        }

        .issue h2 {
            margin: 0 0 .65rem;
            font-size: 1.25rem;
            overflow-wrap: anywhere;
        }

        .issue p {
            color: var(--muted);
        }

        .badge {
            display: inline-block;
            margin: .2rem .3rem .2rem 0;
            padding: .25rem .65rem;
            border: 1px solid #e2defe;
            border-radius: 999px;
            background: #f1efff;
            color: #5147ac;
            font-size: .84rem;
            font-weight: 600;
        }

        .issue form {
            display: flex;
            align-items: center;
            gap: .65rem;
            flex-wrap: wrap;
            margin: 1rem 0;
        }

        .issue form label {
            margin: 0;
        }

        .issue form select {
            width: auto;
            min-width: 150px;
        }

        .error {
            padding: .75rem 1rem;
            border: 1px solid #f0c4cd;
            border-radius: 10px;
            background: #fff0f2;
            color: var(--error);
        }

        .empty {
            padding: 1.25rem;
            border: 1px dashed #c9ccda;
            border-radius: 12px;
            color: var(--muted);
            text-align: center;
        }

        @media (max-width: 600px) {
            body {
                padding: 1.5rem 1rem;
            }

            body > form,
            .issue {
                padding: 1rem;
                border-radius: 12px;
            }

            .issue form {
                align-items: stretch;
            }

            .issue form select,
            .issue form button {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <h1>Game Bug Tracker</h1>
	    <p>
	        <a href="${pageContext.request.contextPath}/issues/export">Download issues as XML</a>
	    </p>
        <c:if test="${param.created eq '1'}">
        <p>Issue submitted successfully.</p>
	    <c:if test="${param.updated eq '1'}">
	        <p>Issue status updated.</p>
	    </c:if>
    	</c:if>

    <c:if test="${not empty param.error}">
        <p class="error">
            <c:choose>
                <c:when test="${param.error eq 'title'}">Enter a title.</c:when>
                <c:when test="${param.error eq 'title-length'}">Title must be 120 characters or fewer.</c:when>
                <c:when test="${param.error eq 'description'}">Enter a description.</c:when>
                <c:when test="${param.error eq 'severity'}">Choose a valid severity.</c:when>
                <c:otherwise>The issue could not be saved. Please try again.</c:otherwise>
            </c:choose>
        </p>
    </c:if>

    <form action="${pageContext.request.contextPath}/issues/create" method="post">
        <label for="title">Title</label><br>
        <input id="title" name="title" type="text" maxlength="120" required><br><br>

        <label for="description">Description</label><br>
        <textarea id="description" name="description" rows="5" required></textarea><br><br>

        <label for="severity">Severity</label><br>
        <select id="severity" name="severity" required>
            <option value="Low">Low</option>
            <option value="Medium">Medium</option>
            <option value="High">High</option>
        </select><br><br>

        <button type="submit">Submit issue</button>
    </form>

    <c:if test="${not empty databaseError}">
        <p class="error"><c:out value="${databaseError}" /></p>
    </c:if>

    <c:choose>
        <c:when test="${empty issues}">
            <p class="empty">No issues to display.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="issue" items="${issues}">
                <article class="issue">
                    <h2>#<c:out value="${issue.id}" /> — <c:out value="${issue.title}" /></h2>
                    <p><c:out value="${issue.description}" /></p>
                    <p>
                        <span class="badge">Severity: <c:out value="${issue.severity}" /></span>
                        <span class="badge">Status: <c:out value="${issue.status}" /></span>
                    </p>
                    <form action="${pageContext.request.contextPath}/issues/status" method="post">
                        <input type="hidden" name="id" value="<c:out value='${issue.id}' />">
                        <label for="status-${issue.id}">Update status</label>
                        <select id="status-${issue.id}" name="status">
                            <option value="Open" <c:if test="${issue.status eq 'Open'}">selected</c:if>>Open</option>
                            <option value="In Progress" <c:if test="${issue.status eq 'In Progress'}">selected</c:if>>In Progress</option>
                            <option value="Resolved" <c:if test="${issue.status eq 'Resolved'}">selected</c:if>>Resolved</option>
                        </select>
                        <button type="submit">Update</button>
                    </form>
                    <p>Created: <c:out value="${issue.createdAt}" /></p>
                </article>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</body>
</html>