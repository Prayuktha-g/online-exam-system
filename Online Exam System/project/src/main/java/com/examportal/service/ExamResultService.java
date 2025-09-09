package com.examportal.service;

import com.examportal.entity.*;
import com.examportal.repository.ExamResultRepository;
import com.examportal.repository.StudentAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExamResultService {

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    public ExamResult calculateAndSaveResult(User student, Exam exam, List<StudentAnswer> answers, int timeSpent) {
        // Save all student answers
        for (StudentAnswer answer : answers) {
            answer.setCorrect(answer.getQuestion().getCorrectAnswer().equals(answer.getSelectedAnswer()));
            studentAnswerRepository.save(answer);
        }

        // Calculate score
        int correctAnswers = studentAnswerRepository.countByStudentAndExamAndIsCorrectTrue(student, exam);
        int totalQuestions = answers.size();
        int obtainedMarks = correctAnswers;
        int totalMarks = exam.getMaxMarks();

        // Create and save result
        ExamResult result = new ExamResult(student, exam, totalMarks, obtainedMarks, timeSpent);
        return examResultRepository.save(result);
    }

    public List<ExamResult> getResultsByStudent(User student) {
        return examResultRepository.findByStudentOrderByAttemptedAtDesc(student);
    }

    public List<ExamResult> getResultsByExam(Exam exam) {
        return examResultRepository.findByExam(exam);
    }

    public Optional<ExamResult> findByStudentAndExam(User student, Exam exam) {
        return examResultRepository.findByStudentAndExam(student, exam);
    }

    public boolean hasStudentAttemptedExam(User student, Exam exam) {
        return examResultRepository.findByStudentAndExam(student, exam).isPresent();
    }

    public List<ExamResult> getAllResults() {
        return examResultRepository.findAll();
    }

    public Optional<ExamResult> findById(Long id) {
        return examResultRepository.findById(id);
    }
}