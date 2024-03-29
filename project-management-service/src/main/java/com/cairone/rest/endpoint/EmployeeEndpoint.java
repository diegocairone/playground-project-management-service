package com.cairone.rest.endpoint;

import com.cairone.core.resource.EmployeeResource;
import com.cairone.rest.ctrl.constraint.PageConstraint;
import com.cairone.rest.request.EmployeeRequest;
import com.cairone.rest.resource.MultiErrorResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Validated
@Tag(name = "Employees")
@RequestMapping("/api/employees")
public interface EmployeeEndpoint {

    @GetMapping
    @Operation(summary = "Employees page", description = "Get a page of employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employees",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Page.class)))),
            @ApiResponse(responseCode = "400", description = "Bad parameter value for sorting",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MultiErrorResource.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            )
    })
    ResponseEntity<Page<EmployeeResource>> getPagedEmployees(
            @PageConstraint(fields = {"id", "curp", "name", "familyName", "birthDate", "gender"})
            Pageable pageable);

    @PostMapping
    @Operation(summary = "Create employee", description = "Create a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created",
                    content = @Content(schema = @Schema(implementation = EmployeeResource.class)),
                    headers = { @Header(
                            name = "Location",
                            description = "The URL to retrieve the created employee",
                            schema = @Schema(type = "string"))
                    }
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            )
    })
    ResponseEntity<EmployeeResource> create(@Valid @RequestBody EmployeeRequest request);

    @PutMapping("{id}")
    @Operation(summary = "Update employee", description = "Update an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated",
                    content = @Content(schema = @Schema(implementation = EmployeeResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            )
    })
    ResponseEntity<EmployeeResource> update(UUID id, @Valid @RequestBody EmployeeRequest request);

    @PatchMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee fields updated",
                    content = @Content(schema = @Schema(implementation = EmployeeResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            )
    })
    ResponseEntity<EmployeeResource> modify(UUID id, @RequestBody Map<String, Object> request);

    @DeleteMapping("{id}")
    @Operation(summary = "Delete employee", description = "Delete an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            )
    })
    ResponseEntity<Void> delete(UUID id);

    @GetMapping("{id}")
    @Operation(summary = "Employee by ID", description = "Get employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee",
                    content = @Content(schema = @Schema(implementation = EmployeeResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            )
    })
    ResponseEntity<EmployeeResource> getEmployeeById(
            @PathVariable("id") UUID id,
            @RequestParam(name = "with-avatar", required = false, defaultValue = "false") Boolean withAvatar);

    @PostMapping(path = "{id}/avatar", consumes = "multipart/form-data")
    @Operation(summary = "Upload employee avatar", description = "Upload employee avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee avatar uploaded",
                    headers = { @Header(
                            name = "Location",
                            description = "The URL to get avatar image from content storage")
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            )
    })
    ResponseEntity<Void> uploadAvatar(UUID id, @RequestParam("file") MultipartFile avatar);

    @PutMapping(path = "{id}/avatar", consumes = "multipart/form-data")
    @Operation(summary = "Replace employee avatar", description = "Replace employee avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee avatar uploaded",
                    headers = { @Header(
                            name = "Location",
                            description = "The URL to get avatar image from content storage")
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            )
    })
    ResponseEntity<Void> replaceAvatar(UUID id, @RequestParam("file") MultipartFile avatar);

    @GetMapping(path = "{id}/avatar")
    @Operation(summary = "Download employee avatar", description = "Download employee avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee avatar",
                    content = @Content(schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            ),
            @ApiResponse(responseCode = "403", description = "Access denied; user must be in role CONTENT_ADMIN to download avatar",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            ),
            @ApiResponse(responseCode = "404", description = "Employee avatar not found",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class)))
    })
    @PreAuthorize("hasRole('CONTENT_ADMIN')")
    ResponseEntity<Resource> downloadAvatar(UUID id);

    @DeleteMapping(path = "{id}/avatar")
    @Operation(summary = "Remove employee avatar", description = "Remove employee avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee avatar removed"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Void.class)))
            ),
            @ApiResponse(responseCode = "404", description = "Employee avatar not found",
                    content = @Content(schema = @Schema(implementation = MultiErrorResource.class)))
    })
    ResponseEntity<Void> removeAvatar(UUID id);
}
