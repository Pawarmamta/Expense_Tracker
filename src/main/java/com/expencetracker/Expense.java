
package com.expencetracker;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDate;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private double amount;

    @Column
    private String  category;

    @Column
    private String description;

    @Column
    private LocalDate date;

    @Column
    private String type;       // EXPENSE or INVESTMENT

    @Column
    private String dayType;    // NORMAL or WORK_FROM_HOME

    //getter
    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getDayType() {
        return dayType;
    }
// setter
    public void setId(int id) {
        this.id = id;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Expense(int id, double amount, String category, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.type = type;
        this.dayType = dayType;
    }

    public Expense()
        {

        }
}
