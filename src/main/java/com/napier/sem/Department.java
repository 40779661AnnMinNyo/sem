package com.napier.sem;

import java.util.ArrayList;

public class Department {
    private String name;
    private ArrayList<Employee> employees = new ArrayList<>();

    // Constructors
    public Department() {}

    public Department(String name) {
        this.name = name;
    }

    // Get department by name
    public Department getDepartment(String deptName) {
        if (this.name != null && this.name.equalsIgnoreCase(deptName)) {
            return this;
        } else {
            System.out.println("Department not found: " + deptName);
            return null;
        }
    }

    // Add employee
    public void addEmployee(Employee e) {
        employees.add(e);
    }

    // Get employees (salaries) by department
    public ArrayList<Employee> getSalariesByDepartment(Department dept) {
        if (dept == null) {
            return new ArrayList<>();
        }
        return dept.employees;
    }

    // Getter for name
    public String getName() {
        return name;
    }
}
