package com.bstarbackend.bstar.web;

import com.bstarbackend.bstar.service.AwsS3Service;
import com.bstarbackend.bstar.service.SettingsService;
import com.bstarbackend.bstar.web.dto.SettingUpdateRequestDto;
import com.bstarbackend.bstar.web.dto.SettingsResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Random;

@RequiredArgsConstructor
@RestController
public class SettingController {

    private final SettingsService settingsService;
    private final AwsS3Service awsS3Service;

    @GetMapping("/setting/info")
    public SettingsResponseDto enroll(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        SettingsResponseDto settings = settingsService.findByEmail(oAuth2User.getAttribute("name"), oAuth2User.getAttribute("email"));
        return settings;
    }

    @PutMapping("/setting/info")
    public void update(Authentication authentication, @RequestBody SettingUpdateRequestDto requestDto) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        settingsService.update(oAuth2User.getAttribute("email"), requestDto);
    }

    @PostMapping("/setting/info")
    public void updateImg(Authentication authentication, MultipartFile multipartFile, HttpServletRequest request) {;
        String file = awsS3Service.uploadImage(multipartFile);

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        settingsService.imageUpdate(oAuth2User.getAttribute("email"), file);
    }
}
