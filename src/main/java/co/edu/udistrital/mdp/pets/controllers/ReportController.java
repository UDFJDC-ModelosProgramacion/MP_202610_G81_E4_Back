package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.dto.ReportDTO;
import co.edu.udistrital.mdp.pets.dto.ReportDetailDTO;
import co.edu.udistrital.mdp.pets.entities.ReportEntity;
import co.edu.udistrital.mdp.pets.services.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReportDetailDTO> findAll() {
        List<ReportEntity> entities = reportService.getReports();
        return modelMapper.map(entities, new TypeToken<List<ReportDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReportDetailDTO findOne(@PathVariable Long id) {
        ReportEntity entity = reportService.getReport(id);
        return modelMapper.map(entity, ReportDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportDTO create(@RequestBody ReportDTO reportDTO) {
        ReportEntity entity = reportService.createReport(
                modelMapper.map(reportDTO, ReportEntity.class));
        return modelMapper.map(entity, ReportDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReportDTO update(@PathVariable Long id, @RequestBody ReportDTO reportDTO) {
        ReportEntity entity = reportService.updateReport(id,
                modelMapper.map(reportDTO, ReportEntity.class));
        return modelMapper.map(entity, ReportDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}