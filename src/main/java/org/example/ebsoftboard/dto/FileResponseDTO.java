package org.example.ebsoftboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.ebsoftboard.entity.File;

@Getter
@Builder
@AllArgsConstructor
public class FileResponseDTO {

    private Long id;
    private String originalFileName;

    public static FileResponseDTO from(File file) {
        return new FileResponseDTO(file.getId(), file.getOriginalFilename());
    }
}
