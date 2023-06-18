package com.dashboard.userinventory.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
    @JsonProperty("male")
    MALE,
    @JsonProperty("female")
    FEMALE
}
