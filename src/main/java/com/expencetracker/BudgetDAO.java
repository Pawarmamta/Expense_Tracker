package com.expencetracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class BudgetDAO {

    private SessionFactory factory;

    public BudgetDAO(SessionFactory factory) {
        this.factory = factory;
    }


    // =========================================
    // CREATE BUDGET
    // =========================================

    public void addBudget(Budget budget) {

        Transaction transaction = null;

        try (Session session = factory.openSession()) {

            transaction = session.beginTransaction();

            session.persist(budget);

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


    // =========================================
    // GET ALL BUDGETS
    // =========================================

    public List<Budget> getAllBudgets() {

        try (Session session = factory.openSession()) {

            return session
                    .createSelectionQuery(
                            "FROM Budget",
                            Budget.class
                    )
                    .getResultList();
        }
    }


    // =========================================
    // GET BUDGET BY CATEGORY + MONTH + YEAR
    // =========================================

    public Budget getBudget(
            String category,
            int month,
            int year) {

        try (Session session = factory.openSession()) {

            return session
                    .createSelectionQuery(
                            "FROM Budget b " +
                                    "WHERE LOWER(b.category) = LOWER(:category) " +
                                    "AND b.month = :month " +
                                    "AND b.year = :year",
                            Budget.class
                    )
                    .setParameter("category", category)
                    .setParameter("month", month)
                    .setParameter("year", year)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        }
    }
}
