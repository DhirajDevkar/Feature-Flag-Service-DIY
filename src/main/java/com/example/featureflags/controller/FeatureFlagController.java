package com.example.featureflags.controller;

import com.example.featureflags.model.FeatureFlag;
import com.example.featureflags.service.FeatureFlagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flags")
public class FeatureFlagController {

    @Autowired
    private FeatureFlagService service;

    @PostMapping
    public void save(@RequestBody FeatureFlag flag) {
        service.saveFlag(flag);
    }

    @GetMapping("/{name}")
    public FeatureFlag get(@PathVariable String name) {
        return service.getFlag(name);
    }

    @DeleteMapping("/{name}")
    public void delete(@PathVariable String name) {
        service.deleteFlag(name);
    }
}
