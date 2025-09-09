package com.examportal.repository;

import com.examportal.entity.ExamResult;
import com.examportal.entity.User;
import com.examportal.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByStudent(User student);
    List<ExamResult> findByExam(Exam exam);
    Optional<ExamResult> findByStudentAndExam(User student, Exam exam);
    List<ExamResult> findByStudentOrderByAttemptedAtDesc(User student);
}