package com.finops.financial_operations_platform.externalLedger;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/provider-statements")
public class ProviderStatementImportController {

    private final ProviderStatementImportService service;

    public ProviderStatementImportController(ProviderStatementImportService service) {
        this.service = service;
    }

    @PostMapping(
            value = "/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> importStatement( @RequestParam("file") MultipartFile file) throws IOException {

        service.createProviderTransaction(file.getInputStream());

        return ResponseEntity.ok().build();
    }
}
