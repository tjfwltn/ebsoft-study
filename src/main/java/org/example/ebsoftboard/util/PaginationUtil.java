package org.example.ebsoftboard.util;

import org.example.ebsoftboard.dto.PageGroupDTO;
import org.example.ebsoftboard.dto.PostListResponseDTO;
import org.springframework.data.domain.Page;

public final class PaginationUtil {

    private PaginationUtil() {
        throw new AssertionError("util class");
    }

    public static PageGroupDTO calculatePageGroupInfo(Page<PostListResponseDTO> postList) {
        int pageGroupSize = 10;
        int currentPageNumber = postList.getNumber();
        int totalPages = postList.getTotalPages();

        int startPage = (currentPageNumber / pageGroupSize) * pageGroupSize;

        int endPage = Math.min(startPage + pageGroupSize - 1, totalPages - 1);

        boolean hasPrev = startPage > 0;
        boolean hasNext = endPage < totalPages - 1;

        return new PageGroupDTO(startPage + 1, endPage + 1, pageGroupSize, hasPrev, hasNext);
    }
}
