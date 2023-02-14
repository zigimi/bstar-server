package com.bstarbackend.bstar.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettingImageUpdateRequestDto {
    private String image;

    @Builder
    public SettingImageUpdateRequestDto(String image) {
        this.image = image;
    }
}
