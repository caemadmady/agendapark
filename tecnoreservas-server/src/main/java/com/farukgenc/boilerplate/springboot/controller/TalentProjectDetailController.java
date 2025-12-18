package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.model.TalentProjectDetail;
import com.farukgenc.boilerplate.springboot.security.dto.ProjectDetailDto;
import com.farukgenc.boilerplate.springboot.service.TalentProjectDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Project/Details")
public class TalentProjectDetailController {
}
