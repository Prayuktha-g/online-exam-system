package com.examportal.repository;

import com.examportal.entity.StudentAnswer;
import com.examportal.entity.User;
import com.examportal.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByStudentAndExam(User student, Exam exam);
    int countByStudentAndExamAndIsCorrectTrue(User student, Exam exam);
}