package com.barry.servicesoftwaretesting.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneNumberValidatorTest {

    private PhoneNumberValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new PhoneNumberValidator();
    }


    @ParameterizedTest
    @CsvSource({
            "+447000000000, true",
            "+447000000000000, false",
            "447000000000, false"})
    void shouldCheckIsPhoneNumberIsValid(String phoneNumber, boolean expected) {
        //Arrange

        //Act
        boolean result = underTest.validate(phoneNumber);

        //Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("should fail when length is bigger than 13")
    void shouldValidatePhoneNumberWhenIncorrectAndHasLengthBiggerThan13() {
        //Arrange
        String phoneNumber = "+447000000000000";

        //Act
        boolean result = underTest.validate(phoneNumber);

        //Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should fail when does not start with +")
    void shouldValidatePhoneNumberWhenDoesNotStartWithPlusSign() {
        //Arrange
        String phoneNumber = "447000000000";

        //Act
        boolean result = underTest.validate(phoneNumber);

        //Assert
        assertThat(result).isFalse();
    }

}
