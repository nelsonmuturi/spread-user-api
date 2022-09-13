package com.spread.users.controller;

import com.spread.users.exception.ResourceNotFoundException;
import com.spread.users.model.Id;
import com.spread.users.model.primitive.IdChar;
import com.spread.users.repository.ReadyRepository;
import com.spread.users.service.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang3.Validate.notNull;

@RestController
@RequestMapping("users")
public class ReadinessController {

    private static final String READY_ID = "kslw2isu74";

    private final ReadyRepository readyRepository;

    public ReadinessController(final ReadyRepository readyRepository) {
        this.readyRepository = notNull(readyRepository);
    }

    @GetMapping("/readiness")
    public String readiness() {
        // signal readiness
        if (readyRepository.findById(
                new Id(new IdChar(READY_ID))).isPresent()) {
            return "Users Api is ready";
        } else {
            throw new ResourceNotFoundException(
                    "Users Api is NOT ready!");
        }
    }
}
