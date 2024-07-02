package ua.vital.securefilesystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.vital.securefilesystem.dto.UserInfo;

import java.util.Map;

@RestController
public class GoogleUserController {
    @GetMapping("/profile")
    public ResponseEntity<UserInfo> retrieveProfile(@AuthenticationPrincipal(expression = "attributes") Map<String, Object> attrs) {
        System.out.println(attrs);
        return ResponseEntity.ok(
                UserInfo.builder()
                        .sub(attrs.get("sub").toString())
                        .email(attrs.get("email").toString())
                        .name(attrs.get("name").toString())
                        .picture(attrs.get("picture").toString())
                        .build());
    }
}
