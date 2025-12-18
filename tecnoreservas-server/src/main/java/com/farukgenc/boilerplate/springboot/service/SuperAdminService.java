package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.*;
import com.farukgenc.boilerplate.springboot.model.enums.ProjectPhase;
import com.farukgenc.boilerplate.springboot.repository.*;
import com.farukgenc.boilerplate.springboot.security.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class SuperAdminService {

    @Autowired
    private ExpertService expertService;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private TalentProjectDetailRepository talentProjectDetailRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TalentProjectDetailService talentProjectDetailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceLineRepository serviceLineRepository;

    public ForUserRoleResponse<?> assignProjectAndServiceline (ForUserRoleRequest forUserRoleRequest) {
        try {
            UserRole userRole = userRepository.findById(forUserRoleRequest.getIdUser()).orElseThrow().getUserRole();
            if (userRole != null) {
                //buscar el que es
                if (userRole == UserRole.TALENT) {
                    Optional<TalentProjectDetail> talentProjectDetail = Optional.ofNullable(talentProjectDetailRepository.findFirstByAssociatedProject(forUserRoleRequest.getProjectName()));
                    TalentAndProjectResponseDto talentAndProjectResponseDto = new TalentAndProjectResponseDto();
                    Optional<TalentProjectDetail> existsProjectInLine = Optional.ofNullable(talentProjectDetailRepository.findFirstByAssociatedProjectAndServiceLine_Id(forUserRoleRequest.getProjectName(), forUserRoleRequest.getIdServiceLine()));
                    if (talentProjectDetail.isPresent() && !talentProjectDetail.map(TalentProjectDetail::getServiceLine).get().getId().equals(forUserRoleRequest.getIdServiceLine()) && !existsProjectInLine.isPresent()) {
                        Talent talent = talentRepository.findById(forUserRoleRequest.getIdUser()).orElseThrow();
                        ProjectDetailDto projectDetailDto = new ProjectDetailDto();
                        projectDetailDto.setAssociatedProject(forUserRoleRequest.getProjectName());
                        projectDetailDto.setProjectPhase(ProjectPhase.INICIO.toString());
                        projectDetailDto.setNewServiceLineId(forUserRoleRequest.getIdServiceLine());
                        TalentProjectDetail talentProjectDetail1 = talentProjectDetailService.assignDetails(projectDetailDto, talent, forUserRoleRequest.getIdServiceLine());

                        TalentResponseDto talentResponseDto = new TalentResponseDto();
                        talentResponseDto.setId(talent.getId());
                        talentResponseDto.setName(talent.getName());
                        talentResponseDto.setUsername(talent.getUsername());
                        talentResponseDto.setLineProjectId(talentProjectDetailService.getAllProjectsOfTalent(talent.getId()));
                        talentResponseDto.setEmail(talent.getEmail());

                        talentAndProjectResponseDto.setProjectDetailDto(projectDetailDto);
                        talentAndProjectResponseDto.setTalentResponseDto(talentResponseDto);

                        ForUserRoleResponse<TalentAndProjectResponseDto> response = new ForUserRoleResponse<>();
                        response.setMessage("proyecto asignado a la linea seleccionada.");
                        response.setResponse(talentAndProjectResponseDto);
                        return response;

                    } else if (!talentProjectDetail.isPresent()) {
                        Talent talent = talentRepository.findById(forUserRoleRequest.getIdUser()).orElseThrow();
                        ProjectDetailDto projectDetailDto = new ProjectDetailDto();
                        projectDetailDto.setAssociatedProject(forUserRoleRequest.getProjectName());
                        projectDetailDto.setProjectPhase(ProjectPhase.INICIO.toString());
                        projectDetailDto.setNewServiceLineId(forUserRoleRequest.getIdServiceLine());
                        TalentProjectDetail talentProjectDetail1 = talentProjectDetailService.assignDetails(projectDetailDto, talent, forUserRoleRequest.getIdServiceLine());

                        TalentResponseDto talentResponseDto = new TalentResponseDto();
                        talentResponseDto.setId(talent.getId());
                        talentResponseDto.setName(talent.getName());
                        talentResponseDto.setUsername(talent.getUsername());
                        talentResponseDto.setLineProjectId(talentProjectDetailService.getAllProjectsOfTalent(talent.getId()));
                        talentResponseDto.setEmail(talent.getEmail());

                        talentAndProjectResponseDto.setProjectDetailDto(projectDetailDto);
                        talentAndProjectResponseDto.setTalentResponseDto(talentResponseDto);

                        ForUserRoleResponse<TalentAndProjectResponseDto> response = new ForUserRoleResponse<>();
                        response.setMessage("proyecto asignado a la linea seleccionada.");
                        response.setResponse(talentAndProjectResponseDto);
                        return response;

                    }
                } else if (userRole == UserRole.EXPERT) {
                    ServiceLine serviceLine = serviceLineRepository.findById(forUserRoleRequest.getIdServiceLine()).orElseThrow();
                    Expert expert = expertRepository.findById(forUserRoleRequest.getIdUser()).orElseThrow();
                    expert.setServiceLine(serviceLine);
                    expertRepository.save(expert);
                    ExpertResponseDto expertResponseDto = new ExpertResponseDto();
                    expertResponseDto.setId(expert.getId());
                    expertResponseDto.setName(expert.getName());
                    expertResponseDto.setUsername(expert.getUsername());
                    expertResponseDto.setLineId(expert.getServiceLine().getId());
                    expertResponseDto.setEmail(expert.getEmail());

                    ForUserRoleResponse<ExpertResponseDto> response = new ForUserRoleResponse<>();
                    response.setMessage("Linea de experto actualizada");
                    response.setResponse(expertResponseDto);
                    return response;
                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ForUserRoleResponse<String> response = new ForUserRoleResponse<>();
        response.setMessage(null);
        response.setResponse("No hay cambios a realizar.");
        return response;

    };
}
