package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.ForUserRoleRequest;
import com.farukgenc.boilerplate.springboot.security.dto.ForUserRoleResponse;
import com.farukgenc.boilerplate.springboot.security.dto.TalentDto;
import com.farukgenc.boilerplate.springboot.service.SuperAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    @Operation(
            summary = "Añade a una linea de servicio un experto o proyecto",
            description = "Cambia la linea de servicio de un experto o añade un proyecto a la linea de servicio.",
            tags = "SuperAdmin"
    )
    @ApiResponse(
            responseCode = "200",
            description = "experto o proyecto añadido a la linea seleccionada.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ForUserRoleResponse.class)
            )
    )
    @PatchMapping("/assign-serviceline/expert-project")
    public ResponseEntity<ForUserRoleResponse<?>> prueba (@RequestBody ForUserRoleRequest forUserRoleRequest){
        return ResponseEntity.ok(superAdminService.assignProjectAndServiceline(forUserRoleRequest));
    }

}
