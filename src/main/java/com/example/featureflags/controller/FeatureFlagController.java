package com.example.featureflags.controller;

import com.example.featureflags.model.ApiResponse;
import com.example.featureflags.model.CreateFlagRequest;
import com.example.featureflags.model.FeatureFlag;
import com.example.featureflags.service.FeatureFlagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/flags")
public class FeatureFlagController {

    @Autowired
    private FeatureFlagService service;

    // The POST endpoint now accepts the new DTO and has a clearer intent.
    @PostMapping
    public ResponseEntity<FeatureFlag> create(@RequestBody CreateFlagRequest createRequest) {
        FeatureFlag createdFlag = service.createFlag(createRequest);
        return new ResponseEntity<>(createdFlag, HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> get(@PathVariable String name) {
        Optional<FeatureFlag> flagOptional = service.getFlag(name);

        if (flagOptional.isPresent()) {
            return new ResponseEntity<>(flagOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse("Feature flag not found: " + name), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{name}")
    public ResponseEntity<?> updateFlag(
        @PathVariable String name,
        @RequestBody FeatureFlag patchRequest) {

        Optional<FeatureFlag> updatedFlagOptional = service.patchFlag(name, patchRequest);

        if (updatedFlagOptional.isPresent()) {
            return new ResponseEntity<>(updatedFlagOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse("Feature flag not found: " + name), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name) {
        boolean wasDeleted = service.deleteFlag(name);

        if (wasDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
