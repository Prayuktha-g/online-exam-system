package com.examportal.controller;

import com.examportal.entity.*;
import com.examportal.service.ExamService;
import com.examportal.service.ExamResultService;
import com.examportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamResultService examResultService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        Optional<User> student = userService.findByUsername(authentication.getName());
        if (student.isPresent()) {
            List<Exam> availableExams = examService.findActiveExams();
            List<ExamResult> results = examResultService.getResultsByStudent(student.get());
            
            model.addAttribute("student", student.get());
            model.addAttribute("availableExams", availableExams);
            model.addAttribute("recentResults", results);
            model.addAttribute("examResultService", examResultService);
        }
        return "student/dashboard";
    }

    @GetMapping("/exams")
    public String listAvailableExams(Model model, Authentication authentication) {
        Optional<User> student = userService.findByUsername(authentication.getName());
        if (student.isPresent()) {
            List<Exam> exams = examService.findActiveExams();
            model.addAttribute("exams", exams);
            model.addAttribute("student", student.get());
            model.addAttribute("examResultService", examResultService);
        }
        return "student/exams";
    }

    @GetMapping("/exams/{id}/start")
    public String startExam(@PathVariable Long id, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        Optional<User> student = userService.findByUsername(authentication.getName());
        Optional<Exam> exam = examService.findById(id);
        
        if (student.isPresent() && exam.isPresent()) {
            // Check if student has already attempted this exam
            if (examResultService.hasStudentAttemptedExam(student.get(), exam.get())) {
                redirectAttributes.addFlashAttribute("error", "You have already attempted this exam!");
                return "redirect:/student/exams";
            }
            
            List<Question> questions = examService.getQuestionsByExam(exam.get());
            model.addAttribute("exam", exam.get());
            model.addAttribute("questions", questions);
            model.addAttribute("student", student.get());
            return "student/take-exam";
        }
        return "redirect:/student/exams";
    }

    @PostMapping("/exams/{id}/submit")
    public String submitExam(@PathVariable Long id, 
                           @RequestParam Map<String, String> answers,
                           @RequestParam int timeSpent,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        Optional<User> student = userService.findByUsername(authentication.getName());
        Optional<Exam> exam = examService.findById(id);
        
        if (student.isPresent() && exam.isPresent()) {
            List<Question> questions = examService.getQuestionsByExam(exam.get());
            List<StudentAnswer> studentAnswers = new ArrayList<>();
            
            for (Question question : questions) {
                String answerKey = "question_" + question.getId();
                String selectedAnswer = answers.get(answerKey);
                if (selectedAnswer != null) {
                    StudentAnswer studentAnswer = new StudentAnswer(question, student.get(), exam.get(), selectedAnswer);
                    studentAnswers.add(studentAnswer);
                }
            }
            
            ExamResult result = examResultService.calculateAndSaveResult(student.get(), exam.get(), studentAnswers, timeSpent);
            redirectAttributes.addFlashAttribute("success", "Exam submitted successfully!");
            return "redirect:/student/results/" + result.getId();
        }
        
        return "redirect:/student/exams";
    }

    @GetMapping("/results")
    public String listResults(Model model, Authentication authentication) {
        Optional<User> student = userService.findByUsername(authentication.getName());
        if (student.isPresent()) {
            List<ExamResult> results = examResultService.getResultsByStudent(student.get());
            model.addAttribute("results", results);
            model.addAttribute("student", student.get());
        }
        return "student/results";
    }

    @GetMapping("/results/{id}")
    public String viewResult(@PathVariable Long id, Model model, Authentication authentication) {
        Optional<User> student = userService.findByUsername(authentication.getName());
        Optional<ExamResult> result = examResultService.findById(id);
        
        if (student.isPresent() && result.isPresent() && result.get().getStudent().getId().equals(student.get().getId())) {
            model.addAttribute("result", result.get());
            return "student/view-result";
        }
        
        return "redirect:/student/results";
    }
}