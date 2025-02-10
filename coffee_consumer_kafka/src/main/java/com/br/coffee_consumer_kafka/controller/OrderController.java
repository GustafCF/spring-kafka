package com.br.coffee_consumer_kafka.controller;

import com.br.coffee_consumer_kafka.domain.OrderModel;
import com.br.coffee_consumer_kafka.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/order")
@Tag(name = "Orders", description = "API for order management")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "List all orders",
            description = "Recovers all orders registered in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order list found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderModel.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping(value = "/listAll")
    public ResponseEntity<List<OrderModel>> listAll() {
        List<OrderModel> list = orderService.findAll();
        return ResponseEntity.ok().body(list);
    }


    @Operation(
            summary = "Search for an order by ID",
            description = "Retrieves the details of a specific order by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Request found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderModel.class))),
                    @ApiResponse(responseCode = "404", description = "Request not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping(value = "/find/{id}")
    public ResponseEntity<OrderModel> findById(
            @Parameter(description = "Order ID to fetch", required = true, example = "1")
            @PathVariable Long id
    ) {
        OrderModel orderModel = orderService.findById(id);
        return ResponseEntity.ok().body(orderModel);
    }

    @Operation(
            summary = "Insert a new order",
            description = "Endpoint to insert a new order into the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Request created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderModel.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping(value = "/insert")
    public ResponseEntity<OrderModel> insert(
            @RequestBody OrderModel orderModel,
            @Parameter(description = "Product name sent in the header", required = true, example = "coffee")
            @RequestHeader("X-Custom-Header") String name
    ) {
        OrderModel order = orderService.sendOrder(orderModel, name);
        URI uri = URI.create("/orders/" + order.getId());
        return ResponseEntity.created(uri).body(order);
    }

    @Operation(
            summary = "Delete an order by ID",
            description = "Remove an order from the system based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Request deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Request not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<OrderModel> delete(
            @Parameter(description = "ID of the order to be deleted", required = true)
            @PathVariable Long id
    ) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

}