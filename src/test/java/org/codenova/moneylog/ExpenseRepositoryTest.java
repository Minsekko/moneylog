package org.codenova.moneylog;

import org.codenova.moneylog.query.DailyExpense;
import org.codenova.moneylog.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ExpenseRepositoryTest {
    @Autowired
    ExpenseRepository expenseRepository;

    @Test
    public void dailyExpenseTest() {

        List<DailyExpense> list = expenseRepository.getDailyExpenseByUserIdAndPeriod(11, LocalDate.of(2025,4,1),LocalDate.of(2025,4,30));
        for(DailyExpense expense : list){
            System.out.println(expense.getExpenseDate() + "/" +expense.getTotal());
        }
        System.out.println("==================================================");
        list.add(2,DailyExpense.builder().expenseDate(LocalDate.of(2025,4,5)).total(0).build());

        for (DailyExpense expense : list) {
            System.out.println(expense.getExpenseDate() + "/" + expense.getTotal());
        }

    }
    @Test
    public void test2() {
        LocalDate from = LocalDate.of(2025,4,1);
        LocalDate to = LocalDate.of(2025,4,30);

        //LocalDate date = from;
        //System.out.println(date.plusDays(0));
        //System.out.println(date.plusDays(1));
        //System.out.println(date.plusDays(2));

        List<LocalDate> dates = new ArrayList<>();

        //dates.add(from.plusDays(0));
        //dates.add(from.plusDays(1));

        for (int i=0; from.plusDays(i).isBefore(to) || from.plusDays(i).equals(to); i++) {
            dates.add(from.plusDays(i));
        }

        System.out.println(dates);

        List<DailyExpense> expenses = new ArrayList<>();

        for(LocalDate date : dates) {
            expenses.add(DailyExpense.builder().expenseDate(date).total(0).build());
        }

        for(DailyExpense one : expenses) {
            System.out.println(one);
        }
    }

    @Test
    public void test3() {
        LocalDate from = LocalDate.of(2025,4,1);
        LocalDate to = LocalDate.of(2025,4,14);

        List<DailyExpense> list = expenseRepository.getDailyExpenseByUserIdAndPeriod(11, from,to);
        Map<LocalDate, DailyExpense> listMap = new HashMap<>();
        for(DailyExpense expense : list){
            listMap.put(expense.getExpenseDate(),expense);
        }
        List<DailyExpense> fullList = new ArrayList<>();

        for (int i=0; from.plusDays(i).isBefore(to) || from.plusDays(i).equals(to); i++){
            LocalDate d = from.plusDays(i);
            if(listMap.get(d) != null) {
                fullList.add(listMap.get(d));
            } else {
                fullList.add(DailyExpense.builder().expenseDate(d).total(0).build());
            }
        }

        for(DailyExpense expense : fullList) {
            System.out.println(expense);
        }

    }
}
