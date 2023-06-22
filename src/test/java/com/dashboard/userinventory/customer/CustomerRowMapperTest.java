package com.dashboard.userinventory.customer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled
class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resultSetMock = mock(ResultSet.class);

        when(resultSetMock.getLong("id")).thenReturn(1L);
        when(resultSetMock.getString("name")).thenReturn("Shubham Wagh");
        when(resultSetMock.getString("email")).thenReturn("shubhamwagh@gmail.com");
        when(resultSetMock.getInt("age")).thenReturn(25);
        when(resultSetMock.getString("gender")).thenReturn(Gender.MALE.name());
        when(resultSetMock.getString("password")).thenReturn("password");

        //When
        Customer actual = customerRowMapper.mapRow(resultSetMock, 1);

        //Then
        Customer expected = new Customer(
                1L,
                "Shubham Wagh",
                "shubhamwagh@gmail.com",
                25,
                Gender.MALE,
                "password"
        );

        assertThat(actual).isEqualTo(expected);
    }
}