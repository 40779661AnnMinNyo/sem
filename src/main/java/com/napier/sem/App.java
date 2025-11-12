package com.napier.sem;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@SpringBootApplication
@RestController

public class App {
    /**
     * Connection to MySQL database.
     */
    private static Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public static void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public static void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Gets a list of employees by role (e.g., 'Manager')
     */
    public ArrayList<Employee> getSalariesByRole(@RequestParam String title) {
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            String query =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, " +
                            "titles.title, salaries.salary, departments.dept_name, dept_manager.emp_no AS manager " +
                            "FROM employees, salaries, titles, departments, dept_emp, dept_manager " +
                            "WHERE employees.emp_no = salaries.emp_no " +
                            "AND salaries.to_date = '9999-01-01' " +
                            "AND titles.emp_no = employees.emp_no " +
                            "AND titles.to_date = '9999-01-01' " +
                            "AND dept_emp.emp_no = employees.emp_no " +
                            "AND dept_emp.to_date = '9999-01-01' " +
                            "AND departments.dept_no = dept_emp.dept_no " +
                            "AND dept_manager.dept_no = dept_emp.dept_no " +
                            "AND dept_manager.to_date = '9999-01-01' " +
                            "AND titles.title = '" + title + "'";

            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");
                emp.dept_name = rset.getString("dept_name");
                emp.manager = rset.getString("manager");
                employees.add(emp);
            }
            rset.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to retrieve employees by role.");
        }
        return employees;
    }

    /**
     * Outputs employee list to Markdown
     */
    /**
     * Outputs employee data in a clean table format to both console and file
     */
    public void outputEmployees(ArrayList<Employee> employees, String filename) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees to output.");
            return;
        }

        // Define column headers
        String header = String.format("%-8s %-12s %-12s %-12s %-8s %-20s %-10s",
                "Emp No", "First Name", "Last Name", "Title", "Salary", "Department", "Manager");

        // Use StringBuilder to collect output
        StringBuilder sb = new StringBuilder();
        sb.append(header).append("\n");
        sb.append("-------------------------------------------------------------------------------------------\n");

        for (Employee emp : employees) {
            sb.append(String.format("%-8d %-12s %-12s %-12s %-8d %-20s %-10s\n",
                    emp.emp_no,
                    emp.first_name,
                    emp.last_name,
                    emp.title,
                    emp.salary,
                    emp.dept_name,
                    emp.manager));

        }

        // Print to console
        System.out.println(sb.toString());

        // Write to file
        try {
            File dir = new File("./reports/");
            if (!dir.exists()) dir.mkdir();

            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
            System.out.println("Report written to ./reports/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Get a single employee record by employee number.
     * Example: http://localhost:8080/employee?id=10001
     *
     * @param id The emp_no of the employee to retrieve.
     * @return The Employee object if found, or null if not found.
     */
    @RequestMapping("/employee")
    public Employee getEmployee(@RequestParam(value = "id") int id) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT emp_no, first_name, last_name, title, salary " +
                    "FROM employees " +
                    "WHERE emp_no = " + id;

            ResultSet rset = stmt.executeQuery(query);

            if (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");
                return emp;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving employee: " + e.getMessage());
            return null;
        }
    }



    /**
     * Main method
     */
    public static void main(String[] args) {
        App app = new App();

        if (args.length < 1) {
            app.connect("localhost:33060", 0);
        } else {
            app.connect(args[0], Integer.parseInt(args[1]));
        }

        ArrayList<Employee> employees = app.getSalariesByRole("Manager");
        app.outputEmployees(employees, "ManagerSalaries.md");

        app.disconnect();
    }

    public void printSalaries(ArrayList<Employee> employess) {
    }


}
