package com.napier.sem;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Create new Application
        App app = new App();

        // Connect to database
        app.connect();

        // --- Part 1: Get all salaries ---
        ArrayList<Employee> allEmployees = app.getAllSalaries();
        System.out.println("Total employees: " + allEmployees.size());

        // --- Part 2: Get all Engineers ---
        ArrayList<Employee> engineers = app.getEmployeesByTitle("Engineer");
        System.out.println("Engineers:");
        app.displayEmployees(engineers);

        // Disconnect from database
        app.disconnect();
    }
}
