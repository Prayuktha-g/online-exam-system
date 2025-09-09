package com.examportal.service;

import com.examportal.entity.Exam;
import com.examportal.entity.Question;
import com.examportal.repository.ExamRepository;
import com.examportal.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<Exam> findAllExams() {
        return examRepository.findAll();
    }

    public List<Exam> findActiveExams() {
        return examRepository.findByActiveTrue();
    }

    public Optional<Exam> findById(Long id) {
        return examRepository.findById(id);
    }

    public Exam saveExam(Exam exam) {
        return examRepository.save(exam);
    }

    public Exam updateExam(Exam exam) {
        return examRepository.save(exam);
    }

    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }

    public List<Question> getQuestionsByExam(Exam exam) {
        return questionRepository.findByExam(exam);
    }

    public List<Question> getQuestionsByExamId(Long examId) {
        return questionRepository.findByExamId(examId);
    }

    public int getQuestionCount(Exam exam) {
        return questionRepository.countByExam(exam);
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Optional<Question> findQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}