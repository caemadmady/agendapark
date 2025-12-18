package com.farukgenc.boilerplate.springboot.security.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.UserStatus;
import com.farukgenc.boilerplate.springboot.repository.ExpertRepository;
import com.farukgenc.boilerplate.springboot.repository.TalentProjectDetailRepository;
import com.farukgenc.boilerplate.springboot.repository.TalentRepository;
import com.farukgenc.boilerplate.springboot.security.dto.*;
import com.farukgenc.boilerplate.springboot.security.dto.user.UserResponseDto;
import com.farukgenc.boilerplate.springboot.service.TalentProjectDetailService;
import com.farukgenc.boilerplate.springboot.service.UserValidationService;
import com.farukgenc.boilerplate.springboot.security.mapper.UserMapper;
import com.farukgenc.boilerplate.springboot.utils.GeneralMessageAccessor;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private static final String REGISTRATION_SUCCESSFUL = "registration_successful";

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final UserValidationService userValidationService;

	private final GeneralMessageAccessor generalMessageAccessor;

	@Override
	public User findByUsername(String username) {

		return userRepository.findByUsername(username);
	}

	@Override
	public RegistrationResponse registration(RegistrationRequest registrationRequest) {
        System.out.println("Este es el valor de Request: "+registrationRequest);
		userValidationService.validateUser(registrationRequest);

		final User user = UserMapper.INSTANCE.convertToUser(registrationRequest);
        user.setLastname(registrationRequest.getLastname());
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		UserRole role = UserRole.valueOf(registrationRequest.getUserRole().toUpperCase());
        System.out.println("el rol es ..." + registrationRequest.getUserRole().toUpperCase());
        user.setUserRole(role);
		user.setUserStatus(UserStatus.ACTIVO);

		userRepository.save(user);

		final String username = registrationRequest.getUsername();
		final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, username);

		log.info("{} registered successfully!", username);
        System.out.println(user);

		return new RegistrationResponse(registrationSuccessMessage);
	}

	@Override
	public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {

		final User user = findByUsername(username);

		return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
	}

    @Override
    public List<UserResponseDto> findAll() {
        List<User> response = userRepository.findAll();
        List<UserResponseDto> responseDto = response
                .stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getName(),
                        user.getLastname(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getUserStatus(),
                        user.getUserRole()
                ))
                .toList();

        return responseDto;
    }

    public String getLoggedUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication != null && authentication.isAuthenticated()) {
				Object principal = authentication.getPrincipal();
					if (principal instanceof UserDetails){
						return ((UserDetails) principal).getUsername();
					} else {
						return principal.toString();
					}
				}
				return null;
	}
}
