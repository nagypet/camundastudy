/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import io.swagger.annotations.Authorization;


public interface OrderApi
{
    String BASE_URL_ORDERS = "/orders";

    //------------------------------------------------------------------------------------------------------------------
    // placeOrder
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping(BASE_URL_ORDERS)
    @ApiOperation(value = "placeOrder() - creates a new order", authorizations = {@Authorization(value = "basicAuth")})
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
