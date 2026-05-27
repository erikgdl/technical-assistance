package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.dto.DashboardResponseDto;
import com.example.assistencia_tecnica.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/dashboard")
@RequiredArgsConstructor
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDto> obterDashboard() {
        DashboardResponseDto dados = dashboardService.obterDadosDashboard();
        return ResponseEntity.ok(dados);
    }

}
