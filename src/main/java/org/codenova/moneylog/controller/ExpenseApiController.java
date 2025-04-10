package org.codenova.moneylog.controller;

import org.codenova.moneylog.repository.ExpenseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/expense")
public class ExpenseApiController {
    private final ExpenseRepository expenseRepository;

    public ExpenseApiController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/next-month")
    @ResponseBody
    public String nextMonth(@RequestParam("date")LocalDate date) {

        return date.plusMonths(1).minusDays(1).toString();

    }

    @GetMapping("/auto-complete")
    @ResponseBody
    public String autoComplete(@RequestParam("word") String word) {
        List<String> list = expenseRepository.getDistinctDescription(word+"%");
        return list.toString().replace("[","").replace("]","");
    }
}
