package com.yourstechnology.form.features.user;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.yourstechnology.form.exception.ResourceNotFoundException;
import com.yourstechnology.form.features.user.dto.GetUserResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    public final String FEATURE_NAME = "User";

    private final UserRepository mainRepository;

    private final ModelMapper modelMapper;

    public GetUserResponse getUser(UUID id) {
        User user = mainRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(FEATURE_NAME, FEATURE_NAME));
        
        GetUserResponse response = modelMapper.map(user, GetUserResponse.class);

        return response;
    }
}

