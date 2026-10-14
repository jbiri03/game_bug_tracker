package com.bugtracker.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bugtracker.dao.IssueDAO;
import com.bugtracker.model.Issue;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/issues/export")
public class ExportIssuesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IssueDAO issueDAO = new IssueDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            documentFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Document document = documentFactory.newDocumentBuilder().newDocument();

            Element root = document.createElement("issues");
            document.appendChild(root);

            for (Issue issue : issueDAO.findAll()) {
                Element issueElement = document.createElement("issue");
                issueElement.setAttribute("id", Integer.toString(issue.getId()));
                root.appendChild(issueElement);

                appendElement(document, issueElement, "title", issue.getTitle());
                appendElement(document, issueElement, "description", issue.getDescription());
                appendElement(document, issueElement, "severity", issue.getSeverity());
                appendElement(document, issueElement, "status", issue.getStatus());
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            var transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            response.setContentType("application/xml;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"issues.xml\"");
            transformer.transform(new DOMSource(document),
                    new StreamResult(response.getOutputStream()));
        } catch (SQLException | ParserConfigurationException | TransformerException e) {
            log("Could not export issues as XML.", e);
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Issues could not be exported. Please try again.");
            }
        }
    }

    private static void appendElement(
            Document document, Element parent, String name, String value) {
        Element element = document.createElement(name);
        element.appendChild(document.createTextNode(value == null ? "" : value));
        parent.appendChild(element);
    }
}