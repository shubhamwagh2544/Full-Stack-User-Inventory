package com.dashboard.userinventory;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TestContainersTests extends AbstractTestContainers {

    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

}
