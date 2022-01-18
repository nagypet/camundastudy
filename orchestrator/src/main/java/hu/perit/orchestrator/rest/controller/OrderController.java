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

package hu.perit.orchestrator.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import hu.perit.orchestrator.config.Constants;
import hu.perit.orchestrator.rest.api.OrderApi;
import hu.perit.orchestrator.rest.model.PlaceOrderResponse;
import hu.perit.orchestrator.rest.model.ShoppingCart;
import hu.perit.spvitamin.core.took.Took;
import hu.perit.spvitamin.spring.logging.AbstractInterfaceLogger;
import hu.perit.spvitamin.spring.security.auth.AuthorizationService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderController extends AbstractInterfaceLogger implements OrderApi
{

    private final AuthorizationService authorizationService;
    private final RuntimeService camundaRuntimeService;

    protected OrderController(HttpServletRequest httpRequest, AuthorizationService authorizationService,
        RuntimeService camundaRuntimeService)
    {
        super(httpRequest);
        this.authorizationService = authorizationService;
        this.camundaRuntimeService = camundaRuntimeService;
    }


    //------------------------------------------------------------------------------------------------------------------
    // placeOrder
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public PlaceOrderResponse placeOrder(@Valid ShoppingCart shoppingCart)
    {
        UserDetails user = this.authorizationService.getAuthenticatedUser();
        try (Took took = new Took())
        {
            this.traceIn(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_PLACE_ORDER, shoppingCart.toString());

            Map<String, Object> variables = new HashMap<>();
            variables.put("myVar", "12");

            ProcessInstanceWithVariables instance = this.camundaRuntimeService.createProcessInstanceByKey("order-process") //
                .setVariables(variables) //
                .executeWithVariablesInReturn();
            
            String processInstanceId = instance.getProcessInstanceId();
            log.debug(String.format("Order process started. Process id: '%s'", processInstanceId));
            return PlaceOrderResponse.builder().processInstanceId(processInstanceId).build();
        }
        catch (Error | RuntimeException ex)
        {
            this.traceOut(null, user.getUsername(), getMyMethodName(), Constants.EVENT_ID_PLACE_ORDER, ex);
            throw ex;
        }
    }


    @Override
    protected String getSubsystemName()
    {
        return Constants.SUBSYSTEM_NAME;
    }
}
