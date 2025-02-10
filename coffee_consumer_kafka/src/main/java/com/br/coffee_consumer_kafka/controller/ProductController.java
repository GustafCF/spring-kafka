package com.br.coffee_consumer_kafka.controller;

import com.br.coffee_consumer_kafka.domain.ProductModel;
import com.br.coffee_consumer_kafka.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "List all products",
            description = "Recovers all products registered in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "product list found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductModel.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping(value = "/list")
    public ResponseEntity<List<ProductModel>> list() {
        List<ProductModel> list = productService.listAll();
        return ResponseEntity.ok().body(list);
    }

    @Operation(
            summary = "Search for an product by ID",
            description = "Retrieves the details of a specific product by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Request found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductModel.class))),
                    @ApiResponse(responseCode = "404", description = "Request not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping(value = "/find/{id}")
    public ResponseEntity<ProductModel> findById(@Parameter(description = "Product ID to fetch", required = true) @PathVariable Long id) {
        ProductModel ProductModel = productService.findById(id);
        return ResponseEntity.ok().body(ProductModel);
    }

    @Operation(
            summary = "Insert a new product",
            description = "Endpoint to insert a new product into the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Request created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductModel.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping(value = "/insert")
    public ResponseEntity<ProductModel> insert(@RequestBody ProductModel ProductModel) {
        var product = productService.insert(ProductModel);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(product);
    }


    @Operation(
            summary = "Update an existing product",
            description = "Endpoint to update the details of an existing product based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductModel.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<ProductModel> update(
            @Parameter(description = "ID of the product to be update", required = true) @PathVariable Long id,
            @RequestBody ProductModel ProductModel) {

        var product = productService.update(id, ProductModel);
        return ResponseEntity.ok().body(product);
    }

    @Operation(
            summary = "Delete an product by ID",
            description = "Remove an product from the system based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Request deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Request not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the product to be deleted", required = true) @PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
