package com.examportal.controller;

import com.examportal.entity.Exam;
import com.examportal.entity.Question;
import com.examportal.entity.ExamResult;
import com.examportal.service.ExamService;
import com.examportal.service.ExamResultService;
import com.examportal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamResultService examResultService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalExams", examService.findAllExams().size());
        model.addAttribute("totalStudents", userService.findAllStudents().size());
        model.addAttribute("recentResults", examResultService.getAllResults());
        return "admin/dashboard";
    }

    // Exam Management
    @GetMapping("/exams")
    public String listExams(Model model) {
        model.addAttribute("exams", examService.findAllExams());
        return "admin/exams";
    }

    @GetMapping("/exams/create")
    public String showCreateExamForm(Model model) {
        model.addAttribute("exam", new Exam());
        return "admin/create-exam";
    }

    @PostMapping("/exams/create")
    public String createExam(@Valid @ModelAttribute Exam exam, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/create-exam";
        }
        examService.saveExam(exam);
        redirectAttributes.addFlashAttribute("success", "Exam created successfully!");
        return "redirect:/admin/exams";
    }

    @GetMapping("/exams/{id}/edit")
    public String showEditExamForm(@PathVariable Long id, Model model) {
        Optional<Exam> exam = examService.findById(id);
        if (exam.isPresent()) {
            model.addAttribute("exam", exam.get());
            return "admin/edit-exam";
        }
        return "redirect:/admin/exams";
    }

    @PostMapping("/exams/{id}/edit")
    public String editExam(@PathVariable Long id, @Valid @ModelAttribute Exam exam, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/edit-exam";
        }
        exam.setId(id);
        examService.updateExam(exam);
        redirectAttributes.addFlashAttribute("success", "Exam updated successfully!");
        return "redirect:/admin/exams";
    }

    @PostMapping("/exams/{id}/delete")
    public String deleteExam(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        examService.deleteExam(id);
        redirectAttributes.addFlashAttribute("success", "Exam deleted successfully!");
        return "redirect:/admin/exams";
    }

    // Question Management
    @GetMapping("/exams/{id}/questions")
    public String listQuestions(@PathVariable Long id, Model model) {
        Optional<Exam> exam = examService.findById(id);
        if (exam.isPresent()) {
            model.addAttribute("exam", exam.get());
            model.addAttribute("questions", examService.getQuestionsByExam(exam.get()));
            return "admin/questions";
        }
        return "redirect:/admin/exams";
    }

    @GetMapping("/exams/{examId}/questions/create")
    public String showCreateQuestionForm(@PathVariable Long examId, Model model) {
        Optional<Exam> exam = examService.findById(examId);
        if (exam.isPresent()) {
            Question question = new Question();
            question.setExam(exam.get());
            model.addAttribute("question", question);
            model.addAttribute("exam", exam.get());
            return "admin/create-question";
        }
        return "redirect:/admin/exams";
    }

    @PostMapping("/exams/{examId}/questions/create")
    public String createQuestion(@PathVariable Long examId, @Valid @ModelAttribute Question question, BindingResult result, RedirectAttributes redirectAttributes) {
        Optional<Exam> exam = examService.findById(examId);
        if (result.hasErrors() || !exam.isPresent()) {
            return "admin/create-question";
        }
        question.setExam(exam.get());
        examService.saveQuestion(question);
        redirectAttributes.addFlashAttribute("success", "Question created successfully!");
        return "redirect:/admin/exams/" + examId + "/questions";
    }

    @PostMapping("/questions/{id}/delete")
    public String deleteQuestion(@PathVariable Long id, @RequestParam Long examId, RedirectAttributes redirectAttributes) {
        examService.deleteQuestion(id);
        redirectAttributes.addFlashAttribute("success", "Question deleted successfully!");
        return "redirect:/admin/exams/" + examId + "/questions";
    }

    // Results Management
    @GetMapping("/results")
    public String listResults(Model model) {
        List<ExamResult> results = examResultService.getAllResults();
        model.addAttribute("results", results);
        return "admin/results";
    }

    @GetMapping("/exams/{id}/results")
    public String examResults(@PathVariable Long id, Model model) {
        Optional<Exam> exam = examService.findById(id);
        if (exam.isPresent()) {
            model.addAttribute("exam", exam.get());
            model.addAttribute("results", examResultService.getResultsByExam(exam.get()));
            return "admin/exam-results";
        }
        return "redirect:/admin/exams";
    }
}