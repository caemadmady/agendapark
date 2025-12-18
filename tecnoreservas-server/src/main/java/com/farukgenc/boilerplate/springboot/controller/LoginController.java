package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.LoginRequest;
import com.farukgenc.boilerplate.springboot.security.dto.LoginResponse;
import com.farukgenc.boilerplate.springboot.security.dto.TalentDto;
import com.farukgenc.boilerplate.springboot.security.jwt.JwtTokenService;
import com.farukgenc.boilerplate.springboot.service.SessionLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

	private final JwtTokenService jwtTokenService;
    private final SessionLogService sessionLogService;

	@PostMapping
    @Operation(
            summary = "Iniciar sesión",
            description = "Permite autenticar a un usuario enviando su usuario y contraseña. "
                    + "Si las credenciales son correctas, devuelve un token JWT y los datos básicos del usuario.",
            tags = "Login"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inicio de sesión exitoso. Devuelve el token de autenticación y los datos del usuario.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida. Faltan datos obligatorios o tienen formato incorrecto.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales incorrectas. No se pudo autenticar al usuario.",
                    content = @Content(mediaType = "application/json")
            )
    })
	public ResponseEntity<LoginResponse> loginRequest(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

		final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);

        sessionLogService.registerLogin(loginRequest.getUsername(), request);

		return ResponseEntity.ok(loginResponse);
	}

}
