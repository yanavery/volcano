//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

//=================================================================================================
@ExtendWith(MockitoExtension.class)
public class DateUtilsTest {
    @InjectMocks
    private DateUtils dateUtils;
    @Mock
    private Configuration config;

    //---------------------------------------------------------------------------------------------
    @Test
    void testGetCurrentDate() {
        LocalDate actual = dateUtils.getCurrentDate();
        LocalDate expected = LocalDate.now();
        assertEquals(expected, actual);
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testGetSearchStartDateOrDefault_WithNull() {
        when(config.getMinLeadTimeDays()).thenReturn(1);
        LocalDate actual = dateUtils.getSearchStartDateOrDefault(null);
        LocalDate expected = LocalDate.now().plusDays(1);
        assertEquals(expected, actual);
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testGetSearchStartDateOrDefault_WithValue() {
        LocalDate seed = LocalDate.now().plusDays(10);
        LocalDate actual = dateUtils.getSearchStartDateOrDefault(seed);
        LocalDate expected = LocalDate.now().plusDays(10);
        assertEquals(expected, actual);
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testGetSearchEndDateOrDefault_WithNull() {
        when(config.getMaxLeadTimeDays()).thenReturn(30);
        LocalDate actual = dateUtils.getSearchEndDateOrDefault(null);
        LocalDate expected = LocalDate.now().plusDays(30);
        assertEquals(expected, actual);
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testGetSearchEndDateOrDefault_WithValue() {
        LocalDate seed = LocalDate.now().plusDays(10);
        LocalDate actual = dateUtils.getSearchEndDateOrDefault(seed);
        LocalDate expected = LocalDate.now().plusDays(10);
        assertEquals(expected, actual);
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testIsWithinDaysFuture_WithDateInRange() {
        assertTrue(dateUtils.isWithinDaysFuture(LocalDate.now().plusDays(10), 10));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testIsWithinDaysFuture_WithDateOutOfRange() {
        assertFalse(dateUtils.isWithinDaysFuture(LocalDate.now().plusDays(10), 5));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testIsWithinDaysFuture_WithDateInRangeLowerBoundary() {
        assertTrue(dateUtils.isWithinDaysFuture(LocalDate.now(), 1));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testIsWithinDaysFuture_WithDateInRangeUpperBoundary() {
        assertTrue(dateUtils.isWithinDaysFuture(LocalDate.now().plusDays(10), 10));
    }

    //---------------------------------------------------------------------------------------------
    @Test
    void testIsWithinDaysFuture_WithDateInThePast() {
        assertFalse(dateUtils.isWithinDaysFuture(LocalDate.now().minusDays(1), 10));
    }
}
