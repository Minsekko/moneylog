package org.codenova.moneylog.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.codenova.moneylog.entity.Category;
import org.codenova.moneylog.entity.Expense;
import org.codenova.moneylog.entity.User;
import org.codenova.moneylog.repository.CategoryRepository;
import org.codenova.moneylog.repository.ExpenseRepository;
import org.codenova.moneylog.repository.UserRepository;
import org.codenova.moneylog.request.AddExpenseRequest;
import org.codenova.moneylog.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/expense")
@AllArgsConstructor
public class ExpenseController {

    private final UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ExpenseRepository expenseRepository;

    @GetMapping("/history")
    public String historyHandel(@SessionAttribute("user") User user,Model model) {
        model.addAttribute("categorys", categoryRepository.findByAll());
        model.addAttribute("now", LocalDate.now());

        model.addAttribute("expense",expenseRepository.findByUserId(user.getId()));
        return "expense/history";
    }

    @PostMapping("/history")
    public String historyPostHandel(@ModelAttribute @Valid AddExpenseRequest addExpenseRequest,
                                    BindingResult bindingResult,
                                    @SessionAttribute("user") User user,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            return "/history-error";
        }
        Expense expense = Expense.builder()
                .userId(user.getId())
                .expenseDate(addExpenseRequest.getExpenseDate())
                .description(addExpenseRequest.getDescription())
                .amount(addExpenseRequest.getAmount())
                .categoryId(addExpenseRequest.getCategoryId())
                .build();

        expenseRepository.save(expense);

        return "redirect:/expense/history";
    }


}
