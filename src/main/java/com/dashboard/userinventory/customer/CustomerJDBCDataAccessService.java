package com.dashboard.userinventory.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql =
                """
                SELECT * from customer
                """;

        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper);
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long customerId) {
        var sql =
                """
                SELECT * from customer
                WHERE id = ?
                """;
        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper, customerId);
        return customers
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql =
                """
                INSERT INTO customer (name, email, age, gender, password) 
                values (?, ?, ?, ?, ?)
                """;

        int updatedRows = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender().name(),
                customer.getPassword()
        );
        System.out.println("INSERT : " + updatedRows);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql =
                """
                SELECT COUNT(*) FROM customer
                WHERE name = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerWithId(Long customerId) {
        var sql =
                """
                SELECT COUNT(*) FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, customerId);
        return count != null && count > 0;
    }

    @Override
    public void updateCustomer(Customer update) {
        Customer customer =
                selectCustomerById(update.getId())
                        .orElseThrow();

        if (update.getName() != null && !update.getName().equals(customer.getName())) {
            var sql = "UPDATE customer SET name = ? where id = ?";
            int update1 = jdbcTemplate.update(sql, update.getName(), update.getId());
            System.out.println("Name Update : " + update1);
        }
        if (update.getEmail() != null && !update.getEmail().equals(customer.getEmail())) {
            var sql = "UPDATE customer SET email = ? where id = ?";
            int update1 = jdbcTemplate.update(sql, update.getEmail(), update.getId());
            System.out.println("Email Update : " + update1);
        }
        if (update.getAge() != null && !update.getAge().equals(customer.getAge())) {
            var sql = "UPDATE customer SET age = ? where id = ?";
            int update1 = jdbcTemplate.update(sql, update.getAge(), update.getId());
            System.out.println("Age Update : " + update1);
        }
        if (update.getGender() != null && !update.getGender().equals(customer.getGender())) {
            var sql = "UPDATE customer SET gender = ? where id = ?";
            int update1 = jdbcTemplate.update(sql, update.getGender().name(), update.getId());
            System.out.println("Gender Update : " + update1);
        }
        if (update.getPassword() != null && !update.getPassword().equals(customer.getPassword())) {
            var sql = "UPDATE customer SET password = ? where id = ?";
            int update1 = jdbcTemplate.update(sql, update.getPassword(), update.getId());
            System.out.println("Password Update : " + update1);
        }
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        var sql =
                """
                DELETE FROM customer
                WHERE id = ?
                """;
        int updatedRow = jdbcTemplate.update(sql, customerId);
        System.out.println("DELETE : " + updatedRow);
    }

    @Override
    public Optional<Customer> selectCustomerByEmail(String email) {
        var sql =
                """
                SELECT * FROM customer
                WHERE email = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, email)
                .stream()
                .findFirst();
    }
}
