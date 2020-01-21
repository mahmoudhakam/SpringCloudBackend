package com.se.details;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(tags = "Error Endpoint")
public class ErrorEndPoint {

    @ApiOperation(value = "get part details error documentation")
    @GetMapping("/errors/{errorNumber}")
    public ResponseEntity getErrorDocumentation(@PathVariable("errorNumber") String errorNumber) {
        return ResponseEntity.noContent().build();
    }

}
