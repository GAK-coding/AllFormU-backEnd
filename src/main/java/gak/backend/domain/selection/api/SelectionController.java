package gak.backend.domain.selection.api;

import gak.backend.domain.selection.application.SelectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class SelectionController {
    private final SelectionService selectionService;
}
