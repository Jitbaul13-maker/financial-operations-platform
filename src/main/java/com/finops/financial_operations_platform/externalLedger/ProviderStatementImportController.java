package com.finops.financial_operations_platform.externalLedger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "External Ledger",
        description = "APIs for retrieving transaction records from the external ledger"
)
public class ProviderStatementImportController {

    private final ProviderStatementImportService service;

    public ProviderStatementImportController(ProviderStatementImportService service) {
        this.service = service;
    }

    @PostMapping(
            value = "/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Retrieve external ledger record",
            description = "Retrieves the requested record from the external ledger."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "External ledger record retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request violates the defined constraints"
            )
    })
    public ResponseEntity<Void> importStatement( @RequestParam("file") MultipartFile file) throws IOException {

        service.createProviderTransaction(file.getInputStream());

        return ResponseEntity.ok().build();
    }
}
