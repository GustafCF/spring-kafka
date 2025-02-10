package com.br.coffee_consumer_kafka.controller;

import com.br.coffee_consumer_kafka.domain.OrderModel;
import com.br.coffee_consumer_kafka.domain.UserModel;
import com.br.coffee_consumer_kafka.service.UserService;
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
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "List all users",
            description = "Recovers all users registered in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "user list found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping(value = "/list")
    public ResponseEntity<List<UserModel>> list() {
        List<UserModel> list = userService.listAll();
        return ResponseEntity.ok().body(list);
    }

    @Operation(
            summary = "Search for an user by ID",
            description = "Retrieves the details of a specific user by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Request found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class))),
                    @ApiResponse(responseCode = "404", description = "Request not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping(value = "/find/{id}")
    public ResponseEntity<UserModel> findById(@Parameter(description = "User ID to fetch", required = true) @PathVariable Long id) {
        UserModel userModel = userService.findById(id);
        return ResponseEntity.ok().body(userModel);
    }

    @Operation(
            summary = "Insert a new user",
            description = "Endpoint to insert a new user into the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Request created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping(value = "/insert")
    public ResponseEntity<UserModel> insert(@RequestBody UserModel userModel) {
        var user = userService.insert(userModel);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }


    @Operation(
            summary = "Update an existing user",
            description = "Endpoint to update the details of an existing user based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<UserModel> update(
            @Parameter(description = "ID of the user to be update", required = true) @PathVariable Long id,
            @RequestBody UserModel userModel) {

        var user = userService.update(id, userModel);
        return ResponseEntity.ok().body(user);
    }

    @Operation(
            summary = "Delete an user by ID",
            description = "Remove an user from the system based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Request deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Request not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the user to be deleted", required = true) @PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
