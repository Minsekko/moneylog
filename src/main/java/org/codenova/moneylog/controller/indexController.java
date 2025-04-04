package org.codenova.moneylog.controller;

import lombok.AllArgsConstructor;
import org.codenova.moneylog.entity.User;
import org.codenova.moneylog.repository.ExpenseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class indexController {

    private ExpenseRepository expenseRepository;

    @GetMapping("/index")
    public String indexHandle(@SessionAttribute("user")Optional<User> user, Model model) {
        if(user.isEmpty()){
            return "index";
        }else {
            return "redirect:/home";
        }
    }

    @GetMapping("/home")
    public String homeHandle(@SessionAttribute("user")Optional<User> user, Model model) { //Optional<User> 옵션한거라 user.getId 바로 써줄수 없다

        if(user.isPresent()){ //유저 값이 존재 한다면
            model.addAttribute("user",user.get());

            LocalDate today = LocalDate.now();

            LocalDate startDate = today.minusDays(today.getDayOfWeek().getValue() -1);
            LocalDate endDate = today.plusDays( 7-today.getDayOfWeek().getValue());

            model.addAttribute("user", user.get());
            model.addAttribute("startDate",startDate);
            model.addAttribute("endDate",endDate);
            model.addAttribute("totalexpense",expenseRepository.findSumAmountByUserId(user.get().getId(),startDate,endDate));
            model.addAttribute("expensesList",expenseRepository.findWeeklyExpenseByUserId(user.get().getId(),startDate,endDate));
            model.addAttribute("grandtotal",expenseRepository.getCategoryByUserIdDuration(user.get().getId(),startDate,endDate));
            return "home";
        }else {
            return "redirect:/index";
        }
    }

}
