package com.amigoscode.testing.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class PhoneNumberValidatorTest {

    private PhoneNumberValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new PhoneNumberValidator();
    }

    @Test
    void itShouldValidatePhoneNumber() {
        // Given

        String phoneNumber = "+351926828283";

        // When

        boolean isValid = underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should faild when length bigger than 13 ")
    void itShouldValidateWhenPhoneNumberIsIncorret() {
        // Given
        String phoneNumber = "+3519268282833";
        // When
        boolean isValid = underTest.test(phoneNumber);
        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should faild when does not start with +351 ")
    void itShouldValidateWhenPhoneNumberisIncorrer() {
        // Given
        String phoneNumber = "351926828283";
        // When
        boolean isValid = underTest.test(phoneNumber);
        // Then
        assertThat(isValid).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"+351926828283, true",
            "351926828283,false",
            "+3519268282833,false"
    })
    void itShouldTestWithParametirezedVariables(String phoneNumber, boolean expected) {
        // Given
        // When
        boolean isValid = underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isEqualTo(expected);
    }
}
