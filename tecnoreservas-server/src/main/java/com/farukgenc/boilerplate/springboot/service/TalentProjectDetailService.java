package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.NameTrl;
import com.farukgenc.boilerplate.springboot.model.enums.ProjectPhase;
import com.farukgenc.boilerplate.springboot.repository.*;
import com.farukgenc.boilerplate.springboot.security.dto.ProjectDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TalentProjectDetailService {

    @Autowired
    private ServiceLineRepository serviceLineRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TalentProjectDetailRepository talentProjectDetailRepository;

    @Autowired
    private ExpertRepository expertRepository;

    public TalentProjectDetail assignDetails(ProjectDetailDto projectDetailDto, Talent talent, Long newServiceLineId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        TalentProjectDetail projectDetail = new TalentProjectDetail();

        if (projectDetailDto.getProjectPhase() == null) {
            throw new IllegalArgumentException("La fase no puede ser nula.");
        }
        //convertir texto a mayusculas
        String phase = projectDetailDto.getProjectPhase().toUpperCase();

        switch (phase) {
            case "INICIO":
            case "PLANEACION":
                projectDetail.setNameTrl(NameTrl.TRL6);
                break;

            case "EJECUCION":
            case "CIERRE":
                projectDetail.setNameTrl(NameTrl.TRL7);
                break;

            default:
                throw new IllegalArgumentException("Fase no valida: " + projectDetailDto.getProjectPhase());
        }
        projectDetail.setProjectPhase(ProjectPhase.valueOf(phase));
        projectDetail.setAssociatedProject(projectDetailDto.getAssociatedProject());
        projectDetail.setTalent(talent);
        //validar el rol del usuario en sesión para asignar la línea del proyecto
        if (user.getUserRole().equals(UserRole.EXPERT)){
            Expert expert = expertRepository.findById(user.getId()).orElseThrow();
            Long serviceLineId = expert.getServiceLine().getId();
            ServiceLine serviceLine = serviceLineRepository.findById(serviceLineId).orElseThrow();
            projectDetail.setServiceLine(serviceLine);
        } else if (user.getUserRole().equals(UserRole.SUPERADMIN )) {
            ServiceLine newServiceLine = serviceLineRepository.findById(newServiceLineId).orElseThrow();
            System.out.println("Id de la linea nueva" + newServiceLine);
            projectDetail.setServiceLine(newServiceLine);
        }

        System.out.println("Los datos: "+ projectDetail);
        talentProjectDetailRepository.save(projectDetail);
        return projectDetail;
    }

    public TalentProjectDetail getTalentProjectDetailById(Long id){
        return talentProjectDetailRepository.findById(id).orElseThrow();
    }

    public List<Long> getLineProjectTalentId(Long talentId){
        List<TalentProjectDetail> projectDetail = talentProjectDetailRepository.findAllByTalentId(talentId);
        List<Long> response = new ArrayList<>();
        for (TalentProjectDetail talentProjectDetail: projectDetail) {
            response.add(talentProjectDetail.getId());
        }
        return response;
    }

    public List<Long> getAllProjectsOfTalent(Long talentId){
        List<TalentProjectDetail> projectDetails = talentProjectDetailRepository.findAllByTalentId(talentId);
        List<Long> response = new ArrayList<>();
        for (TalentProjectDetail talentProjectDetail: projectDetails){
            Long id = talentProjectDetail.getId();
            response.add(id);
        }
        return response;
    }
}
