package com.mishchuk.autotrade.service.user;

import com.mishchuk.autotrade.controller.dto.UserCreateDto;
import com.mishchuk.autotrade.controller.dto.UserDetailDto;
import com.mishchuk.autotrade.controller.dto.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // @Autowired or constructor-inject your repository here
    // private final UserRepository userRepository;

    @Override
    public UserDetailDto createUser(UserCreateDto request) {
        // TODO: create user, send confirmation email
        return new UserDetailDto(/* populate with dummy or real data */);
    }

    @Override
    public void completeRegistration(UserCompleteRegistrationDto request) {
        // TODO: verify token, set password
    }

    @Override
    public UserDetailDto updateUser(UUID id, UserUpdateDto request) {
        // TODO: update user info
        return new UserDetailDto();
    }

    @Override
    public UserDetailDto getUser(UUID id) {
        // TODO: fetch from repository
        return new UserDetailDto();
    }

    @Override
    public void deleteUser(UUID id) {
        // TODO: delete or deactivate user
    }
}