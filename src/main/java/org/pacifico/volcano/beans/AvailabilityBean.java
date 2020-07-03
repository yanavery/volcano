//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.pacifico.volcano.utils.Formats;

import java.time.LocalDate;

//=================================================================================================
@Getter
@Setter
@AllArgsConstructor
public class AvailabilityBean {
    @JsonFormat(pattern = Formats.DATE_FORMAT)
    LocalDate date;
}
