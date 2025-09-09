package com.examportal.config;

import com.examportal.entity.User;
import com.examportal.entity.Exam;
import com.examportal.entity.Question;
import com.examportal.service.UserService;
import com.examportal.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private ExamService examService;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if doesn't exist
        if (!userService.existsByUsername("admin")) {
            User admin = new User("admin", "admin@examportal.com", "admin123", "System Administrator", User.Role.ADMIN);
            userService.saveUser(admin);
        }

        // Create a test student if doesn't exist
        if (!userService.existsByUsername("student")) {
            User student = new User("student", "student@examportal.com", "student123", "Test Student", User.Role.STUDENT);
            userService.saveUser(student);
        }

        // Create a sample exam if no exams exist
        if (examService.findAllExams().isEmpty()) {
            Exam exam = new Exam("Java Programming Basics", "Test your knowledge of Java programming fundamentals", 30, 10);
            Exam savedExam = examService.saveExam(exam);

            // Add sample questions
            Question q1 = new Question(
                "What is the correct way to declare a variable in Java?",
                "var x = 10;",
                "int x = 10;",
                "x = 10;",
                "declare int x = 10;",
                "B",
                1,
                savedExam
            );
            examService.saveQuestion(q1);

            Question q2 = new Question(
                "Which of the following is NOT a Java primitive data type?",
                "int",
                "boolean",
                "String",
                "char",
                "C",
                1,
                savedExam
            );
            examService.saveQuestion(q2);

            Question q3 = new Question(
                "What is the entry point of a Java application?",
                "start() method",
                "main() method",
                "run() method",
                "begin() method",
                "B",
                1,
                savedExam
            );
            examService.saveQuestion(q3);

            Question q4 = new Question(
                "Which keyword is used to create a class in Java?",
                "class",
                "Class",
                "create",
                "new",
                "A",
                1,
                savedExam
            );
            examService.saveQuestion(q4);

            Question q5 = new Question(
                "What is the size of an int in Java?",
                "16 bits",
                "32 bits",
                "64 bits",
                "8 bits",
                "B",
                1,
                savedExam
            );
            examService.saveQuestion(q5);
        }
    }
}