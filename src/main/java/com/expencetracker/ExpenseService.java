package com.expencetracker;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseService {

    private ExpenseDAO expenseDAO;
    private BudgetDAO budgetDAO;

    public ExpenseService(
            ExpenseDAO expenseDAO,
            BudgetDAO budgetDAO) {

        this.expenseDAO = expenseDAO;
        this.budgetDAO = budgetDAO;
    }


    // =====================================================
    // CRUD
    // =====================================================

    public void addExpense(Expense expense) {
        expenseDAO.addExpense(expense);
    }

    public Expense getExpense(int id) {
        return expenseDAO.getExpense(id);
    }

    public List<Expense> getAllExpenses() {
        return expenseDAO.getAllExpenses();
    }

    public void updateExpense(Expense expense) {
        expenseDAO.updateExpense(expense);
    }

    public void deleteExpense(int id) {
        expenseDAO.deleteExpense(id);
    }


    // =====================================================
    // TOTAL EXPENSES
    // =====================================================

    public double getTotalExpenses() {

        List<Expense> expenses =
                expenseDAO.getAllExpenses();

        double total = 0;

        for (Expense expense : expenses) {

            if ("EXPENSE".equalsIgnoreCase(
                    expense.getType())) {

                total += expense.getAmount();
            }
        }

        return total;
    }


    // =====================================================
    // TOTAL INVESTMENTS
    // =====================================================

    public double getTotalInvestments() {

        List<Expense> expenses =
                expenseDAO.getAllExpenses();

        double total = 0;

        for (Expense expense : expenses) {

            if ("INVESTMENT".equalsIgnoreCase(
                    expense.getType())) {

                total += expense.getAmount();
            }
        }

        return total;
    }


    // =====================================================
    // CATEGORY-WISE EXPENSES
    // =====================================================

    public Map<String, Double> getCategoryWiseExpenses() {

        List<Expense> expenses =
                expenseDAO.getAllExpenses();

        Map<String, Double> categoryTotals =
                new HashMap<>();

        for (Expense expense : expenses) {

            if ("EXPENSE".equalsIgnoreCase(
                    expense.getType())) {

                String category =
                        expense.getCategory();

                categoryTotals.put(
                        category,
                        categoryTotals.getOrDefault(
                                category, 0.0
                        ) + expense.getAmount()
                );
            }
        }

        return categoryTotals;
    }


    // =====================================================
    // MONTHLY EXPENSES
    // =====================================================

    public double getMonthlyExpenses(
            int month,
            int year) {

        // DAO gets only the required month's data
        List<Expense> expenses =
                expenseDAO.getExpensesByMonth(
                        month,
                        year
                );

        double total = 0;

        for (Expense expense : expenses) {

            if ("EXPENSE".equalsIgnoreCase(
                    expense.getType())) {

                total += expense.getAmount();
            }
        }

        return total;
    }


    // =====================================================
    // MONTHLY INVESTMENTS
    // =====================================================

    public double getMonthlyInvestments(
            int month,
            int year) {

        List<Expense> expenses =
                expenseDAO.getExpensesByMonth(
                        month,
                        year
                );

        double total = 0;

        for (Expense expense : expenses) {

            if ("INVESTMENT".equalsIgnoreCase(
                    expense.getType())) {

                total += expense.getAmount();
            }
        }

        return total;
    }


    // =====================================================
    // SAVINGS
    // =====================================================

    public double calculateSavings(
            double income,
            int month,
            int year) {

        double expenses =
                getMonthlyExpenses(
                        month,
                        year
                );

        double investments =
                getMonthlyInvestments(
                        month,
                        year
                );

        return income - expenses - investments;
    }


    // =====================================================
    // PREVIOUS MONTH EXPENSES
    // =====================================================

    public double getPreviousMonthExpenses() {

        LocalDate previousMonth =
                LocalDate.now().minusMonths(1);

        return getMonthlyExpenses(
                previousMonth.getMonthValue(),
                previousMonth.getYear()
        );
    }


    // =====================================================
    // PREVIOUS MONTH INVESTMENTS
    // =====================================================

    public double getPreviousMonthInvestments() {

        LocalDate previousMonth =
                LocalDate.now().minusMonths(1);

        return getMonthlyInvestments(
                previousMonth.getMonthValue(),
                previousMonth.getYear()
        );
    }


    // =====================================================
    // EXPENSE COMPARISON
    // =====================================================

    public double compareExpenses(
            int month,
            int year) {

        double currentExpenses =
                getMonthlyExpenses(
                        month,
                        year
                );

        LocalDate previousMonth =
                LocalDate.of(
                        year,
                        month,
                        1
                ).minusMonths(1);

        double previousExpenses =
                getMonthlyExpenses(
                        previousMonth.getMonthValue(),
                        previousMonth.getYear()
                );

        return currentExpenses -
                previousExpenses;
    }


    // =====================================================
    // INVESTMENT COMPARISON
    // =====================================================

    public double compareInvestments(
            int month,
            int year) {

        double currentInvestment =
                getMonthlyInvestments(
                        month,
                        year
                );

        LocalDate previousMonth =
                LocalDate.of(
                        year,
                        month,
                        1
                ).minusMonths(1);

        double previousInvestment =
                getMonthlyInvestments(
                        previousMonth.getMonthValue(),
                        previousMonth.getYear()
                );

        return currentInvestment -
                previousInvestment;
    }


    // =====================================================
    // SAVINGS RATE
    // =====================================================

    public double getSavingsRate(
            double income,
            int month,
            int year) {

        if (income <= 0) {
            return 0;
        }

        double savings =
                calculateSavings(
                        income,
                        month,
                        year
                );

        return (savings / income) * 100;
    }


    // =====================================================
    // CATEGORY-WISE MONTHLY EXPENSE
    // =====================================================

    public Map<String, Double>
    getMonthlyCategoryWiseExpenses(
            int month,
            int year) {

        List<Expense> expenses =
                expenseDAO.getExpensesByMonth(
                        month,
                        year
                );

        Map<String, Double> categoryTotals =
                new HashMap<>();

        for (Expense expense : expenses) {

            if ("EXPENSE".equalsIgnoreCase(
                    expense.getType())) {

                String category =
                        expense.getCategory();

                categoryTotals.put(
                        category,
                        categoryTotals.getOrDefault(
                                category, 0.0
                        ) + expense.getAmount()
                );
            }
        }

        return categoryTotals;
    }


    // =====================================================
    // BUDGET CHECK
    // =====================================================

    public void checkBudget(
            String category,
            int month,
            int year) {

        List<Budget> budgets =
                budgetDAO.getAllBudgets();

        double budgetLimit = 0;

        for (Budget budget : budgets) {

            if (budget.getCategory()
                    .equalsIgnoreCase(category)
                    && budget.getMonth() == month
                    && budget.getYear() == year) {

                budgetLimit =
                        budget.getMonthlyLimit();

                break;
            }
        }

        if (budgetLimit == 0) {

            System.out.println(
                    "No budget found for "
                            + category
            );

            return;
        }


        // Get only this month's expenses
        List<Expense> expenses =
                expenseDAO.getExpensesByMonth(
                        month,
                        year
                );

        double spent = 0;

        for (Expense expense : expenses) {

            if ("EXPENSE".equalsIgnoreCase(
                    expense.getType())
                    &&
                    expense.getCategory()
                            .equalsIgnoreCase(
                                    category)) {

                spent += expense.getAmount();
            }
        }


        double percentage =
                (spent / budgetLimit) * 100;


        System.out.println(
                "\n--- Budget Status ---"
        );

        System.out.println(
                "Category: " + category
        );

        System.out.println(
                "Budget: ₹" + budgetLimit
        );

        System.out.println(
                "Spent: ₹" + spent
        );

        System.out.println(
                "Used: " + percentage + "%"
        );


        if (spent > budgetLimit) {

            System.out.println(
                    "🚨 ALERT! Budget exceeded by ₹"
                            + (spent - budgetLimit)
            );

        } else if (spent >= budgetLimit * 0.8) {

            System.out.println(
                    "⚠ WARNING! You have used 80%+ of your budget."
            );

        } else {

            System.out.println(
                    "✓ You are within your budget."
            );
        }
    }
}

