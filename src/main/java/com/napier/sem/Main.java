package com.napier.sem;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.connect();

        // Example: get all Engineers
        ArrayList<Employee> engineers = app.getEmployeesByTitle("Engineer");

        // Display results
        app.displayEmployees(engineers);

        app.disconnect();
    }
}
