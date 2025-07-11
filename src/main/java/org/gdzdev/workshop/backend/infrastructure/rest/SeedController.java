package org.gdzdev.workshop.backend.infrastructure.rest;

import lombok.RequiredArgsConstructor;

import org.gdzdev.workshop.backend.application.dto.ApiResponse;
import org.gdzdev.workshop.backend.application.usecase.SeedServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seeder")
public class SeedController {
    private final SeedServiceImpl _seederUseCase;

    @PutMapping("/seedAll")
    public ResponseEntity<ApiResponse<?>> seedAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status("success")
                        .response(this._seederUseCase.seedALl())
                        .build()
                );
    }

    @PutMapping("/isSeeded")
    public ResponseEntity<ApiResponse<?>> isSeeded() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status("success")
                        .response("Is seeded 😊")
                        .build()
                );
    }
}

// "Cannot suppress a null exception."