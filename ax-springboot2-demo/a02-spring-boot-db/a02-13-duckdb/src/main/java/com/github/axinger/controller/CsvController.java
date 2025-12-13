package com.github.axinger.controller;

import com.github.axinger.server.CsvReaderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/csv")
public class CsvController {

    private final CsvReaderService csvReaderService;

    public CsvController(CsvReaderService csvReaderService) {
        this.csvReaderService = csvReaderService;
    }

    @GetMapping("/read")
    public List<Map<String, Object>> readCsv(@RequestParam(defaultValue = "/opt/123.csv") String filePath) {
        return csvReaderService.readCsv(filePath);
    }

    @PostMapping("/create-view")
    public String createView(@RequestParam String filePath, @RequestParam String viewName) {
        csvReaderService.createViewFromCsv(filePath, viewName);
        return "View '" + viewName + "' created successfully";
    }

    @GetMapping("/query-view")
    public List<Map<String, Object>> queryView(@RequestParam String viewName) {
        return csvReaderService.queryFromView(viewName);
    }
}
