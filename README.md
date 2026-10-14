# Game Bug Tracker

A Java web application for submitting, viewing, and managing game bug reports. Issues are stored in MySQL using JDBC and can be exported as an XML file.

## Features

- Submit issues with a title, description, and severity.
- View saved issues, including their severity, status, and creation date.
- Update an issue’s status to **Open**, **In Progress**, or **Resolved**.
- Export saved issues as an XML download.
- Validate required fields and allowed severity and status values.
- Escape issue text when displaying it in the page.

## Built with

- Java 21
- Jakarta Servlets 6.0 and JSP
- Apache Tomcat 10.1
- Maven
- MySQL and JDBC
- HTML and CSS

## Requirements

Install:

- JDK 21
- Apache Tomcat 10.1
- MySQL Server
- Eclipse IDE for Enterprise Java and Web Developers (or another Java IDE with Maven support)

## Database setup

Run this SQL in MySQL Workbench to create the database and table:

```sql
CREATE DATABASE IF NOT EXISTS game_issue_tracker;
USE game_issue_tracker;

CREATE TABLE IF NOT EXISTS issues (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    description TEXT NOT NULL,
    severity VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Open',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
