package hu.perit.orchestrator.rest.api;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import hu.perit.orchestrator.rest.model.PlaceOrderResponse;
import hu.perit.orchestrator.rest.model.ShoppingCart;
import hu.perit.spvitamin.spring.logging.EventLogId;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface OrderApi
{
    String BASE_URL_ORDERS = "/orders";

    //------------------------------------------------------------------------------------------------------------------
    // placeOrder
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping(BASE_URL_ORDERS)
    @ApiOperation(value = "placeOrder() - creates a new order")
    @ApiResponses(value = { //
        @ApiResponse(code = 201, message = "Created"), //
        @ApiResponse(code = 400, message = "Bad request"), //
        @ApiResponse(code = 401, message = "Invalid credentials"), //
        @ApiResponse(code = 500, message = "Internal server error") //
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @EventLogId(eventId = 1)
    PlaceOrderResponse placeOrder(@Valid @RequestBody ShoppingCart shoppingCart);
}
