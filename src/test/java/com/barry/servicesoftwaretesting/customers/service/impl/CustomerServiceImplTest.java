package com.barry.servicesoftwaretesting.customers.service.impl;

import com.barry.servicesoftwaretesting.customers.models.Customer;
import com.barry.servicesoftwaretesting.customers.repositories.CustomerRepository;
import com.barry.servicesoftwaretesting.utils.PhoneNumberValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl serviceUnderTest;
    @Mock
    private PhoneNumberValidator phoneNumberValidator;
    @Captor
    private ArgumentCaptor<Customer> captorCustomer;

    @Test
    void shouldSaveNewCustomerWhenCustomerPhoneNumberDoesNotExistAndValid() {
        //Arrange
        final Customer customer =
                Customer.builder().id(UUID.randomUUID()).name("Aisha").phoneNumber("+0986666").build();

        given(customerRepository.findByPhoneNumber(customer.getPhoneNumber())).willReturn(Optional.empty());
        given(phoneNumberValidator.validate(customer.getPhoneNumber())).willReturn(true);

        //Act
        serviceUnderTest.registerNewCustomer(customer);

        //Assert
        verify(customerRepository, times(1)).save(captorCustomer.capture());
        Customer customerValue = captorCustomer.getValue();
        assertThat(customerValue).isEqualTo(customer);

    }

    @Test
    void shouldThrowExceptionWhenCustomerPhoneNumberIsNotValid() {
        //Arrange
        Customer customer =
                Customer.builder().id(UUID.randomUUID()).name("Aisha").phoneNumber("+0986666").build();

        given(phoneNumberValidator.validate(customer.getPhoneNumber())).willReturn(false);

        //Act
        assertThatThrownBy(()-> serviceUnderTest.registerNewCustomer(customer))
                .hasMessageContaining(String.format("Phone number %s is not Valid", customer.getPhoneNumber()))
                .isInstanceOf(IllegalArgumentException.class);

        //Assert
        verify(customerRepository, never()).save(any());
    }


    @Test
    void shouldNotSaveCustomerWhenCustomerExists() {
        //Arrange
        Customer customer = Customer.builder().id(UUID.randomUUID()).name("Aisha").phoneNumber("+0986666").build();


        when(customerRepository.findByPhoneNumber(customer.getPhoneNumber())).thenReturn(Optional.of(customer));
        given(phoneNumberValidator.validate(customer.getPhoneNumber())).willReturn(true);

        //Act
        serviceUnderTest.registerNewCustomer(customer);

        //Assert
        verify(customerRepository, never()).save(any());
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPhoneNumberIsTaken() {
        //Arrange
        String phoneNumber = "+0986666";
        Customer customer = Customer.builder().id(UUID.randomUUID()).name("Aisha").phoneNumber(phoneNumber).build();
        Customer customerTwo = new Customer(UUID.randomUUID(), "John", phoneNumber);

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(customerTwo));
        given(phoneNumberValidator.validate(customer.getPhoneNumber())).willReturn(true);

        //Act
        final Exception exception = assertThrows(IllegalArgumentException.class,
                () -> serviceUnderTest.registerNewCustomer(customer));
        assertThat(exception.getMessage()).isEqualTo(String.format("phone number [%s] is taken", customer.getPhoneNumber()));

        //Assert
        verify(customerRepository, never()).save(any());
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void shouldSaveNewCustomerWhenIdIsNull() {
        //Arrange
        String phoneNumber = "+9890700";
        Customer customer = Customer.builder().id(null).name("John").phoneNumber(phoneNumber).build();
        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        given(phoneNumberValidator.validate(phoneNumber)).willReturn(true);

        //Act
        serviceUnderTest.registerNewCustomer(customer);

        //Assert
        verify(customerRepository,times(1)).save(captorCustomer.capture());
        final Customer customerValue = captorCustomer.getValue();
        assertThat(customerValue).isEqualTo(customer);
    }

    @Test
    void shouldNotRegisterNewCustomerSameNameAndSamePhoneNumber() {
        // Arrange
        String phoneNumber = "+1234567890";
        String customerName = "John Doe";

        Customer existingCustomer = new Customer();
        existingCustomer.setName(customerName);

        Customer newCustomer = new Customer();
        newCustomer.setName(customerName);
        newCustomer.setPhoneNumber(phoneNumber);

        when(phoneNumberValidator.validate(phoneNumber)).thenReturn(true);
        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(existingCustomer));

        // Act
        assertDoesNotThrow(() -> serviceUnderTest.registerNewCustomer(newCustomer),
                "Expected the method to return early when names are the same.");

        //Assert
        assertDoesNotThrow(() -> serviceUnderTest.registerNewCustomer(newCustomer),
                "Expected the method to return early without calling the save method.");

        verify(customerRepository, never()).save(any());
        then(customerRepository).should(never()).save(any());
    }
}
