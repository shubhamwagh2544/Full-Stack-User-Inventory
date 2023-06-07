package com.dashboard.userinventory.customer;

public record CustomerUpdateRequest (
        String name,
        String email,
        String password,
        Integer age,
        Gender gender
) {

}
