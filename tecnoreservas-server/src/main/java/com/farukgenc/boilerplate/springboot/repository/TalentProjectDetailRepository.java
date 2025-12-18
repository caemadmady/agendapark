package com.farukgenc.boilerplate.springboot.repository;

import com.farukgenc.boilerplate.springboot.model.TalentProjectDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalentProjectDetailRepository extends JpaRepository<TalentProjectDetail, Long> {
    List<TalentProjectDetail> findAllByTalentId(Long talentId);
    List<TalentProjectDetail> findAllByServiceLine_Id(Long serviceLineId);
    TalentProjectDetail findFirstByAssociatedProject(String name);
    TalentProjectDetail findFirstByAssociatedProjectAndServiceLine_Id(String projectName, Long serviceLineId);
}
