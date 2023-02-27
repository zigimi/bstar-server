package com.bstarbackend.bstar.web;

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
        String UPLOAD_PATH = "bstar-server/src/main/resources/static/img";
        try {
            MultipartFile file = multipartFile;

            String fileId = (new Date().getTime()) + "" + (new Random().ints(1000, 9999).findAny().getAsInt()); // 현재 날짜와 랜덤 정수값으로 새로운 파일명 만들기
            String originName = file.getOriginalFilename(); // ex) 파일.jpg
            String fileExtension = originName.substring(originName.lastIndexOf(".") + 1); // ex) jpg
            originName = originName.substring(0, originName.lastIndexOf(".")); // ex) 파일
            long fileSize = file.getSize(); // 파일 사이즈

            Path path = Paths.get(UPLOAD_PATH + fileId + "." + fileExtension).toAbsolutePath();
            File fileSave = Paths.get(UPLOAD_PATH).toAbsolutePath().toFile(); // ex) fileId.jpg
            if(!fileSave.exists()) { // 폴더가 없을 경우 폴더 만들기
                fileSave.mkdirs();
            }

            file.transferTo(path.toFile()); // fileSave의 형태로 파일 저장

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            settingsService.imageUpdate(oAuth2User.getAttribute("email"), "img/" + fileId + "." + fileExtension);

        } catch(IOException e) {
            System.out.println(e);
        }
    }
}
