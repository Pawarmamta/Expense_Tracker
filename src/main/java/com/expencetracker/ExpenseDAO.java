package com.expencetracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ExpenseDAO {

    private SessionFactory factory;

    public ExpenseDAO(SessionFactory factory) {
        this.factory = factory;
    }


    // =========================
    // CREATE
    // =========================

    public void addExpense(Expense expense) {

        Transaction transaction = null;

        try (Session session = factory.openSession()) {

            transaction = session.beginTransaction();

            session.persist(expense);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

            e.printStackTrace();
        }
    }


    // =========================
    // READ ONE
    // =========================

    public Expense getExpense(int id) {

        try (Session session = factory.openSession()) {

            return session.find(Expense.class, id);
        }
    }


    // =========================
    // READ ALL
    // =========================

    public List<Expense> getAllExpenses() {

        try (Session session = factory.openSession()) {

            return session
                    .createSelectionQuery(
                            "FROM Expense",
                            Expense.class
                    )
                    .getResultList();
        }
    }


    // =========================
    // UPDATE
    // =========================

    public void updateExpense(Expense expense) {

        Transaction transaction = null;

        try (Session session = factory.openSession()) {

            transaction = session.beginTransaction();

            session.merge(expense);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

            e.printStackTrace();
        }
    }


    // =========================
    // DELETE
    // =========================

    public void deleteExpense(int id) {

        Transaction transaction = null;

        try (Session session = factory.openSession()) {

            transaction = session.beginTransaction();

            Expense expense =
                    session.find(Expense.class, id);

            if (expense != null) {
                session.remove(expense);
            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

            e.printStackTrace();
        }
    }


    // =========================
    // GET EXPENSES BY MONTH
    // =========================

    public List<Expense> getExpensesByMonth(
            int month,
            int year) {

        try (Session session = factory.openSession()) {

            return session
                    .createSelectionQuery(
                            "FROM Expense e " +
                                    "WHERE MONTH(e.date) = :month " +
                                    "AND YEAR(e.date) = :year",
                            Expense.class
                    )
                    .setParameter("month", month)
                    .setParameter("year", year)
                    .getResultList();
        }
    }


    // =========================
    // GET PREVIOUS MONTH
    // =========================

    public List<Expense> getPreviousMonthExpenses() {

        java.time.LocalDate previousMonth =
                java.time.LocalDate.now().minusMonths(1);

        return getExpensesByMonth(
                previousMonth.getMonthValue(),
                previousMonth.getYear()
        );
    }
}

