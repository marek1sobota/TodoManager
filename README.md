Todo Manager
Overview

Todo Manager is a desktop task management application developed in Kotlin using Compose Desktop. The project was created as part of the Modern Programming Languages course.

The application allows users to create, manage, organize, and track tasks through a modern desktop user interface.

Features
Create tasks
Edit tasks
Delete tasks
Mark tasks as completed
Search tasks by title
Filter tasks (All, Active, Completed)
Sort tasks by:
Priority
Name
Creation Date
Task priorities:
Low
Medium
High
Task statistics
Light and Dark themes
Report generation and export
JSON-based data persistence
Delete confirmation dialog
Technologies Used
Kotlin
Compose Desktop
Gradle
Kotlin Coroutines
StateFlow
Kotlin Serialization
Design Patterns
Abstract Factory

The Abstract Factory pattern is used to manage application themes.

Implemented classes:

ThemeFactory
LightThemeFactory
DarkThemeFactory
ThemeManager
Builder

The Builder pattern is used to generate task reports.

Implemented classes:

ReportBuilder
TextReportBuilder
ReportDirector
## Project Structure

```text
src
└── main
    └── kotlin
        ├── builder
        ├── factory
        ├── model
        ├── repository
        ├── ui
        └── utils
```

Requirements:

JDK 21
Gradle

Run the application using:

./gradlew run

or directly from IntelliJ IDEA.

Author

Marek Sobota

Computer Science (Master's Degree)

Faculty of Exact Sciences and Technology

University of Silesia
