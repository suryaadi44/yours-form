package com.yourstechnology.form.features.user;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yourstechnology.form.features.user.dto.GetUserResponse;
import com.yourstechnology.form.utils.ResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService mainService;

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<GetUserResponse>> getUser(
            @PathVariable("userId") UUID userId) {
        GetUserResponse result = mainService.getUser(userId);

        ResponseDto<GetUserResponse> response = new ResponseDto<>();
        response.setMessage(mainService.FEATURE_NAME);
        response.setData(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
