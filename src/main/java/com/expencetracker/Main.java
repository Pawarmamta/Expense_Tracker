package com.expencetracker;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // =========================================
        // HIBERNATE CONFIGURATION
        // =========================================

        Configuration configuration = new Configuration()
                .addAnnotatedClass(Expense.class)
                .addAnnotatedClass(Budget.class);

        SessionFactory factory = configuration
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();


        // =========================================
        // DAO
        // =========================================

        ExpenseDAO expenseDAO = new ExpenseDAO(factory);
        BudgetDAO budgetDAO = new BudgetDAO(factory);


        // =========================================
        // SERVICE
        // =========================================

        ExpenseService expenseService =
                new ExpenseService(expenseDAO, budgetDAO);


        Scanner scanner = new Scanner(System.in);


        // =========================================
        // 1. MONTHLY INCOME
        // =========================================

        System.out.print("Enter your monthly income: ₹");

        double income = scanner.nextDouble();
        scanner.nextLine();


        // =========================================
        // 2. PREDEFINED CATEGORIES
        // =========================================

        String[] categories = {

                "Rent / PG",
                "Food",
                "Travel",
                "Bills",
                "Gym / Health",
                "Shopping",
                "Education",
                "Entertainment",
                "Investment",
                "Other"
        };


        // =========================================
        // 3. SET BUDGETS
        // =========================================

        System.out.println("\n=================================");
        System.out.println("       SET MONTHLY BUDGET");
        System.out.println("=================================");

        System.out.println(
                "Enter 0 if you don't want to set a budget for a category."
        );


        for (String category : categories) {

            System.out.print(
                    category + " budget: ₹"
            );

            double budgetAmount =
                    scanner.nextDouble();

            if (budgetAmount > 0) {

                Budget budget = new Budget();

                budget.setCategory(category);

                budget.setMonthlyLimit(
                        budgetAmount
                );

                budget.setMonth(
                        LocalDate.now().getMonthValue()
                );

                budget.setYear(
                        LocalDate.now().getYear()
                );

                budgetDAO.addBudget(budget);
            }
        }


        // =========================================
        // 4. ENTER TRANSACTIONS
        // =========================================

        scanner.nextLine();

        String continueInput = "yes";


        while (continueInput.equalsIgnoreCase("yes")) {

            System.out.println("\n=================================");
            System.out.println("       ENTER TRANSACTION");
            System.out.println("=================================");


            // AMOUNT

            System.out.print("Enter amount: ₹");

            double amount =
                    scanner.nextDouble();

            scanner.nextLine();


            // CATEGORY

            System.out.println("\nSelect category:");

            for (int i = 0; i < categories.length; i++) {

                System.out.println(
                        (i + 1) + ". " +
                                categories[i]
                );
            }


            System.out.print(
                    "Enter category number: "
            );

            int categoryChoice =
                    scanner.nextInt();

            scanner.nextLine();


            if (categoryChoice < 1 ||
                    categoryChoice > categories.length) {

                System.out.println(
                        "Invalid category!"
                );

                continue;
            }


            String category =
                    categories[categoryChoice - 1];


            // DESCRIPTION

            System.out.print(
                    "Enter description: "
            );

            String description =
                    scanner.nextLine();


            // TYPE

            String type;

            while (true) {

                System.out.print(
                        "Enter type (EXPENSE/INVESTMENT): "
                );

                type =
                        scanner.nextLine()
                                .trim()
                                .toUpperCase();


                if (type.equals("EXPENSE") ||
                        type.equals("INVESTMENT")) {

                    break;
                }


                System.out.println(
                        "Invalid type!"
                );
            }


            // DAY TYPE

            String dayType;

            while (true) {

                System.out.print(
                        "Enter day type (NORMAL/WORK_FROM_HOME): "
                );

                dayType =
                        scanner.nextLine()
                                .trim()
                                .toUpperCase();


                if (dayType.equals("NORMAL") ||
                        dayType.equals("WORK_FROM_HOME")) {

                    break;
                }


                System.out.println(
                        "Invalid day type!"
                );
            }


            // =========================================
            // CREATE EXPENSE
            // =========================================

            Expense expense =
                    new Expense();

            expense.setAmount(amount);

            expense.setCategory(category);

            expense.setDescription(description);

            expense.setDate(
                    LocalDate.now()
            );

            expense.setType(type);

            expense.setDayType(dayType);


            // =========================================
            // SAVE
            // =========================================

            expenseService.addExpense(expense);


            System.out.println(
                    "\n✓ Transaction saved successfully!"
            );


            // =========================================
            // CHECK BUDGET IMMEDIATELY
            // =========================================

            expenseService.checkBudget(
                    category,
                    LocalDate.now().getMonthValue(),
                    LocalDate.now().getYear()
            );


            // =========================================
            // ANOTHER TRANSACTION?
            // =========================================

            System.out.print(
                    "\nDo you want to enter another transaction? (yes/no): "
            );

            continueInput =
                    scanner.nextLine();
        }


        // =========================================
        // 5. CURRENT MONTH
        // =========================================

        int currentMonth =
                LocalDate.now().getMonthValue();

        int currentYear =
                LocalDate.now().getYear();


        // =========================================
        // 6. MONTHLY EXPENSE
        // =========================================

        double monthlyExpenses =
                expenseService.getMonthlyExpenses(
                        currentMonth,
                        currentYear
                );


        // =========================================
        // 7. MONTHLY INVESTMENT
        // =========================================

        double monthlyInvestment =
                expenseService.getMonthlyInvestments(
                        currentMonth,
                        currentYear
                );


        // =========================================
        // 8. SAVINGS
        // =========================================

        double savings =
                expenseService.calculateSavings(
                        income,
                        currentMonth,
                        currentYear
                );


        // =========================================
        // 9. CATEGORY REPORT
        // =========================================

        Map<String, Double> categoryReport =
                expenseService.getCategoryWiseExpenses();


        // =========================================
        // 10. FINAL REPORT
        // =========================================

        System.out.println("\n\n=================================");
        System.out.println("       MONTHLY FINANCIAL REPORT");
        System.out.println("=================================");

        System.out.println(
                "Monthly Income   : ₹" + income
        );

        System.out.println(
                "Total Expenses   : ₹" + monthlyExpenses
        );

        System.out.println(
                "Total Investment : ₹" + monthlyInvestment
        );

        System.out.println(
                "Savings          : ₹" + savings
        );


        // =========================================
        // REMAINING MONEY
        // =========================================

        double remaining =
                income
                        - monthlyExpenses
                        - monthlyInvestment;


        System.out.println(
                "Money Remaining  : ₹" + remaining
        );


        // =========================================
        // CATEGORY-WISE REPORT
        // =========================================

        System.out.println(
                "\n--- CATEGORY-WISE EXPENSES ---"
        );


        for (Map.Entry<String, Double> entry :
                categoryReport.entrySet()) {

            System.out.println(
                    entry.getKey()
                            + " → ₹"
                            + entry.getValue()
            );
        }


        // =========================================
        // FINAL ADVICE
        // =========================================

        System.out.println(
                "\n--- FINANCIAL SUMMARY ---"
        );


        if (remaining > 0) {

            System.out.println(
                    "✓ You have ₹"
                            + remaining
                            + " available."
            );

            System.out.println(
                    "You can save or invest this amount."
            );

        } else if (remaining == 0) {

            System.out.println(
                    "⚠ Your entire income has been used."
            );

        } else {

            System.out.println(
                    "🚨 WARNING: You spent more than your income!"
            );
        }

        // =========================================
// 12. PREVIOUS MONTH COMPARISON
// =========================================

        LocalDate previousDate = LocalDate.now().minusMonths(1);

        int previousMonth = previousDate.getMonthValue();
        int previousYear = previousDate.getYear();

        double previousExpenses =
                expenseService.getMonthlyExpenses(
                        previousMonth,
                        previousYear
                );

        double previousInvestment =
                expenseService.getMonthlyInvestments(
                        previousMonth,
                        previousYear
                );

        double previousSavings =
                income - previousExpenses - previousInvestment;


// =========================================
// COMPARISON REPORT
// =========================================

        System.out.println("\n=================================");
        System.out.println("       PREVIOUS MONTH COMPARISON");
        System.out.println("=================================");

        System.out.println(
                "Previous Month Expenses : ₹"
                        + previousExpenses
        );

        System.out.println(
                "Current Month Expenses  : ₹"
                        + monthlyExpenses
        );

        double expenseDifference =
                previousExpenses - monthlyExpenses;

        if (expenseDifference > 0) {

            System.out.println(
                    "✓ You spent ₹"
                            + expenseDifference
                            + " LESS than last month."
            );

        } else if (expenseDifference < 0) {

            System.out.println(
                    "⚠ You spent ₹"
                            + Math.abs(expenseDifference)
                            + " MORE than last month."
            );

        } else {

            System.out.println(
                    "Your expenses are SAME as last month."
            );
        }


// =========================================
// INVESTMENT COMPARISON
// =========================================

        System.out.println();

        System.out.println(
                "Previous Month Investment : ₹"
                        + previousInvestment
        );

        System.out.println(
                "Current Month Investment  : ₹"
                        + monthlyInvestment
        );

        double investmentDifference =
                monthlyInvestment - previousInvestment;

        if (investmentDifference > 0) {

            System.out.println(
                    "📈 You invested ₹"
                            + investmentDifference
                            + " MORE than last month."
            );

        } else if (investmentDifference < 0) {

            System.out.println(
                    "⚠ You invested ₹"
                            + Math.abs(investmentDifference)
                            + " LESS than last month."
            );

        } else {

            System.out.println(
                    "Investment is SAME as last month."
            );
        }


// =========================================
// SAVINGS COMPARISON
// =========================================

        System.out.println();

        System.out.println(
                "Previous Month Savings : ₹"
                        + previousSavings
        );

        System.out.println(
                "Current Month Savings  : ₹"
                        + savings
        );

        double savingsDifference =
                savings - previousSavings;

        if (savingsDifference > 0) {

            System.out.println(
                    "🎉 You saved ₹"
                            + savingsDifference
                            + " MORE than last month."
            );

        } else if (savingsDifference < 0) {

            System.out.println(
                    "⚠ You saved ₹"
                            + Math.abs(savingsDifference)
                            + " LESS than last month."
            );

        } else {

            System.out.println(
                    "Savings are SAME as last month."
            );
        }


        scanner.close();

        factory.close();
    }
}

