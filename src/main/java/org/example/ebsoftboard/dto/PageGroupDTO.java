package org.example.ebsoftboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageGroupDTO {

    private int startPage;
    private int endPage;
    private int pageGroupSize;
}
