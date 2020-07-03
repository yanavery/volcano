//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pacifico.volcano.beans.CustomerBean;
import org.pacifico.volcano.exception.InvalidParameterException;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//=================================================================================================
@ExtendWith(MockitoExtension.class)
public class ValidationUtilsTest {
    @InjectMocks
    private ValidationUtils validationUtils;
    @Mock
    private DateUtils dateUtils;
    @Mock
    private Configuration config;

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateFutureDated_Success() {
        when(dateUtils.getCurrentDate()).thenReturn(LocalDate.now());

        assertDoesNotThrow(() -> {
            validationUtils.validateFutureDated(LocalDate.now().plusDays(3));
        });
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateFutureDated_Failure() {
        when(dateUtils.getCurrentDate()).thenReturn(LocalDate.now());

        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            validationUtils.validateFutureDated(LocalDate.now().minusDays(3));
        });

        assertThat(exception.getMessage(), containsString("must be in the future"));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateStartDateBeforeEndDate_Success() {
        assertDoesNotThrow(() -> {
            validationUtils.validateStartDateBeforeEndDate(
                    LocalDate.now().plusDays(5), LocalDate.now().plusDays(10));
        });
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateStartDateBeforeEndDate_Failure() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            validationUtils.validateStartDateBeforeEndDate(
                    LocalDate.now().plusDays(10), LocalDate.now().plusDays(5));
        });

        assertThat(exception.getMessage(), containsString("must be before end date"));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateStartEndDateMaxSpread_Success() {
        when(config.getMaxDurationDays()).thenReturn(3);

        assertDoesNotThrow(() -> {
            validationUtils.validateStartEndDateMaxSpread(
                    LocalDate.now(), LocalDate.now().plusDays(3));
        });
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateStartEndDateMaxSpread_Failure() {
        when(config.getMaxDurationDays()).thenReturn(3);

        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            validationUtils.validateStartEndDateMaxSpread(
                    LocalDate.now(), LocalDate.now().plusDays(4));
        });

        assertThat(exception.getMessage(), containsString("Invalid date range. There's a maximum"));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateStartEndDateMinSpread_Success() {
        when(config.getMinDurationDays()).thenReturn(1);

        assertDoesNotThrow(() -> {
            validationUtils.validateStartEndDateMinSpread(
                    LocalDate.now(), LocalDate.now().plusDays(1));
        });
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateStartEndDateMinSpread_Failure() {
        when(config.getMinDurationDays()).thenReturn(1);

        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            validationUtils.validateStartEndDateMinSpread(
                    LocalDate.now(), LocalDate.now());
        });

        assertThat(exception.getMessage(), containsString("Invalid date rage. There's a minimum"));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateDateProximity_Success() {
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(15);

        when(config.getMaxLeadTimeDays()).thenReturn(30);
        when(config.getMaxDurationDays()).thenReturn(3);
        when(dateUtils.isWithinDaysFuture(startDate, 30)).thenReturn(true);
        when(dateUtils.isWithinDaysFuture(endDate, 30 + 3)).thenReturn(true);

        assertDoesNotThrow(() -> {
            validationUtils.validateDateProximity(startDate, endDate);
        });
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateDateProximity_Failure() {
        LocalDate startDate = LocalDate.now().plusDays(40);
        LocalDate endDate = LocalDate.now().plusDays(50);

        when(config.getMaxLeadTimeDays()).thenReturn(30);
        when(config.getMaxDurationDays()).thenReturn(3);
        when(dateUtils.isWithinDaysFuture(startDate, 30)).thenReturn(false);

        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            validationUtils.validateDateProximity(startDate, endDate);
        });

        assertThat(exception.getMessage(), containsString("too far away in the future"));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateCustomerPresence_Success() {
        CustomerBean customer = new CustomerBean();
        assertDoesNotThrow(() -> {
            validationUtils.validateCustomerPresence(customer);
        });
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateCustomerPresence_Failure() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            validationUtils.validateCustomerPresence(null);
        });

        assertThat(exception.getMessage(), containsString("Customer information is missing"));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateCustomerEmail_Success() {
        CustomerBean customer = new CustomerBean();
        customer.setEmail("test@yopmail.com");
        assertDoesNotThrow(() -> {
            validationUtils.validateCustomerEmail(customer);
        });
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateCustomerEmail_Failure() {
        CustomerBean customer = new CustomerBean();
        customer.setEmail(null);
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            validationUtils.validateCustomerEmail(customer);
        });

        assertThat(exception.getMessage(), containsString("Customer email is missing"));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateCustomerName_Success() {
        CustomerBean customer = new CustomerBean();
        customer.setName("Some Customer");
        assertDoesNotThrow(() -> {
            validationUtils.validateCustomerName(customer);
        });
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testValidateCustomerName_Failure() {
        CustomerBean customer = new CustomerBean();
        customer.setName(null);
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            validationUtils.validateCustomerName(customer);
        });

        assertThat(exception.getMessage(), containsString("Customer name is missing"));
    }
}
