//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.beans;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

//=================================================================================================
@Getter
@Builder
public class AvailabilityCriteriaBean {
    private LocalDate startDate;
    private LocalDate endDate;
}
