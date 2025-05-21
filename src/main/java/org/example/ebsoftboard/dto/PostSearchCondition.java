package org.example.ebsoftboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostSearchCondition {

    private static final long ONE_YEAR = 1;
    private static final long ONE_DAY = 1;
    private Long categoryId;
    private String keyword;

    @DateTimeFormat
    private LocalDate startDate;

    @DateTimeFormat
    private LocalDate endDate;

    public LocalDateTime getStartDateOrDefault() {
        return (startDate == null) ? LocalDate.now().minusYears(ONE_YEAR).atStartOfDay() : startDate.atStartOfDay();
    }

    public LocalDateTime getEndDateOrDefault() {
        return (endDate == null) ? LocalDate.now().plusDays(ONE_DAY).atStartOfDay() : endDate.plusDays(ONE_DAY).atStartOfDay();
    }

}
