package com.examportal.repository;

import com.examportal.entity.Question;
import com.examportal.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByExam(Exam exam);
    List<Question> findByExamId(Long examId);
    int countByExam(Exam exam);
}