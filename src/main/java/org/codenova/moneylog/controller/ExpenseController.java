package org.codenova.moneylog.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.codenova.moneylog.entity.Category;
import org.codenova.moneylog.entity.Expense;
import org.codenova.moneylog.entity.User;
import org.codenova.moneylog.query.DailyExpense;
import org.codenova.moneylog.repository.CategoryRepository;
import org.codenova.moneylog.repository.ExpenseRepository;
import org.codenova.moneylog.repository.SearchPeriodRequest;
import org.codenova.moneylog.repository.UserRepository;
import org.codenova.moneylog.request.AddExpenseRequest;
import org.codenova.moneylog.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/expense")
@AllArgsConstructor
public class ExpenseController {

    private final UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ExpenseRepository expenseRepository;

    @GetMapping("/history")
    public String historyHandel(@SessionAttribute("user") User user,
                                @ModelAttribute SearchPeriodRequest searchPeriodRequest,
                                Model model) {

        LocalDate startDate;
        LocalDate endDate;

        if(searchPeriodRequest.getStartDate() != null && searchPeriodRequest.getEndDate() != null) {
            startDate = searchPeriodRequest.getStartDate();
            endDate = searchPeriodRequest.getEndDate();
        } else {
            LocalDate today = LocalDate.now();
            startDate = today.minusDays(today.getDayOfMonth() - 1);
            endDate = startDate.plusMonths(1).minusDays(1);
        }

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        model.addAttribute("categorys", categoryRepository.findByAll());
        model.addAttribute("now", LocalDate.now());

        //model.addAttribute("expense",expenseRepository.findWithCategoryByUserId(user.getId()));

        model.addAttribute("expense",expenseRepository.findByUserIdAndDuration(user.getId(),startDate, endDate));

        //findByWithCategoryByUserIdAndDuration
        return "expense/history";
    }

    @PostMapping("/history")
    public String historyPostHandel(@ModelAttribute @Valid AddExpenseRequest addExpenseRequest,
                                    BindingResult bindingResult,
                                    @SessionAttribute("user") User user,
                                    Model model) {

        if(user.getVerified().equals("F")) {  //인증이 되지 않은 사용자이면 인증 되지 않았다고 에러 표기
            return "/history-error";
        }

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

    @GetMapping("/report")
    public String reportHandel(@SessionAttribute("user") User user,
                                Model model) {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(today.getDayOfMonth() - 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        model.addAttribute("categoryData", expenseRepository.getCategoryExpenseByUserIdOrderByCategoryId(user.getId(), startDate,endDate));

        List<DailyExpense> list = expenseRepository.getDailyExpenseByUserIdAndPeriod(user.getId(), startDate, endDate);

        Map<LocalDate, DailyExpense> listMap = new HashMap<>();
        for(DailyExpense expense : list){
            listMap.put(expense.getExpenseDate(),expense);
        }
        List<DailyExpense> fullList = new ArrayList<>();

        for (int i=0; startDate.plusDays(i).isBefore(endDate) || startDate.plusDays(i).equals(endDate); i++){
            LocalDate d = startDate.plusDays(i);
            if(listMap.get(d) != null) {
                fullList.add(listMap.get(d));
            } else {
                fullList.add(DailyExpense.builder().expenseDate(d).total(0).build());
            }
        }

        model.addAttribute("dailyExpense",fullList);

        return "expense/report";
    }


}
