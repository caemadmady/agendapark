package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.ServiceLine;
import com.farukgenc.boilerplate.springboot.repository.ServiceLineRepository;
import com.farukgenc.boilerplate.springboot.security.dto.LineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLineService {

    @Autowired
    private ServiceLineRepository serviceLineRepository;

    public LineDto getLineById(Long id){
        ServiceLine serviceLine = serviceLineRepository.findById(id).orElseThrow();
        LineDto lineDto = new LineDto();
        lineDto.setId(serviceLine.getId());
        lineDto.setName(serviceLine.getServiceLineName());
        return lineDto;
    }

    public List<LineDto> getLines(){
        List<ServiceLine> lines = serviceLineRepository.findAll();
        List<LineDto> list = new ArrayList<>();
        for (ServiceLine line: lines){
            LineDto lineDto = new LineDto();
            lineDto.setId(line.getId());
            lineDto.setName(line.getServiceLineName());
            list.add(lineDto);
        }
        return list;
    }
}
