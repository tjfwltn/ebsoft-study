package org.example.ebsoftboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PostSearchCondition {

    private static final long ONE_YEAR = 1;
    private Long categoryId;
    private String keyword;

    @DateTimeFormat
    private LocalDate startDate;

    @DateTimeFormat
    private LocalDate endDate;

    public LocalDate getStartDateOrDefault() {
        return (startDate == null) ? LocalDate.now().minusYears(ONE_YEAR) : startDate;
    }

    public LocalDate getEndDateOrDefault() {
        return (endDate == null) ? LocalDate.now() : endDate;
    }

}
