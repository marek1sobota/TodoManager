# Todo Manager

## Overview

Todo Manager is a desktop task management application developed in **Kotlin** using **Compose Desktop**. The project was created as part of the **Modern Programming Languages** course.

The application allows users to create, manage, organize, and track tasks through a modern desktop user interface. The project demonstrates the practical use of contemporary Kotlin technologies, reactive state management, data persistence, and software design patterns.

---

## Features

### Task Management

* Create tasks
* Edit tasks
* Delete tasks
* Mark tasks as completed
* Display task statistics

### Search and Organization

* Search tasks by title
* Filter tasks:

  * All
  * Active
  * Completed
* Sort tasks by:

  * Priority
  * Name
  * Creation Date

### Task Priorities

* Low
* Medium
* High

### Additional Functionality

* Light Theme / Dark Theme
* Report generation and export
* JSON-based data persistence
* Delete confirmation dialog
* Task creation date tracking

---

## Technologies Used

* Kotlin
* Compose Desktop
* Gradle
* Kotlin Coroutines
* StateFlow
* Kotlin Serialization

---

## Design Patterns

### Abstract Factory Pattern

The Abstract Factory pattern is used to manage application themes.

#### Implemented Classes

* ThemeFactory
* LightThemeFactory
* DarkThemeFactory
* ThemeManager

This pattern separates theme creation from the user interface and allows easy extension with additional themes in the future.

---

### Builder Pattern

The Builder pattern is used to generate task reports.

#### Implemented Classes

* ReportBuilder
* TextReportBuilder
* ReportDirector

This approach separates the report construction process from its final representation and improves maintainability and extensibility.

---

## State Management and Persistence

The application uses **StateFlow** for reactive state management. User interface components automatically update whenever task data changes.

Task data is stored in JSON format using **Kotlin Serialization**, allowing tasks to persist between application sessions.

Asynchronous operations are handled using **Kotlin Coroutines**, ensuring responsive application behavior.

---

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

---

## Requirements

* JDK 21
* Gradle

---

## Running the Application

Run the application using:

```bash
./gradlew run
```

or directly from **IntelliJ IDEA**.

---

## Author

**Marek Sobota**

Computer Science (Master's Degree)

Faculty of Exact Sciences and Technology

University of Silesia

---

## Course Information

**Course:** Modern Programming Languages

**Academic Year:** 2025/2026
