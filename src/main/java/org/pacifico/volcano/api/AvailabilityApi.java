//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.api;

import org.pacifico.volcano.utils.DateUtils;
import org.pacifico.volcano.beans.AvailabilityBean;
import org.pacifico.volcano.beans.AvailabilityCriteriaBean;
import org.pacifico.volcano.service.AvailabilityService;
import org.pacifico.volcano.utils.Formats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

//=================================================================================================
@RestController
@RequestMapping("/v1/availabilities")
public class AvailabilityApi {
    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private AvailabilityService availabilityService;

    /**
     * GET API end-point, used to retrieve the list of campsite availabilities within
     * the optionally provided start/end date range.
     *
     * Sample cURL usages:
     * -------------------
     * curl http://localhost:8080/v1/availabilities
     * curl http://localhost:8080/v1/availabilities?startDate=2020-07-10&endDate=2020-07-20
     */
    @GetMapping
    public HttpEntity<List<AvailabilityBean>> getAvailabilities(
            @RequestParam(value = "startDate", required = false)
                @DateTimeFormat(pattern = Formats.DATE_FORMAT) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
                @DateTimeFormat(pattern = Formats.DATE_FORMAT) LocalDate endDate) {

        AvailabilityCriteriaBean criteria = AvailabilityCriteriaBean.builder()
                .startDate(dateUtils.getSearchStartDateOrDefault(startDate))
                .endDate(dateUtils.getSearchEndDateOrDefault(endDate))
                .build();

        return new ResponseEntity<>(availabilityService.getAvailabilities(criteria), HttpStatus.OK);
    }
}
