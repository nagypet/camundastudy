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

package hu.perit.orchestrator.bpmn.orchestrator;

import org.apache.commons.lang3.BooleanUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import hu.perit.orchestrator.config.Constants;
import hu.perit.orchestrator.services.api.TaskHistoryService;
import hu.perit.spvitamin.spring.config.SpringContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendEmailDelegate implements JavaDelegate
{

    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        // Store history
        SpringContext.getBean(TaskHistoryService.class).storeHistory(execution);

        Boolean stockAvailable = (Boolean) execution.getVariable(Constants.PROCESS_VARIABLE_STOCK_AVAILABLE);
        Boolean paymentSuccessfull = (Boolean) execution.getVariable(Constants.PROCESS_VARIABLE_PAYMENT_SUCCESSFULL);

        if (BooleanUtils.isNotTrue(stockAvailable))
        {
            log.debug("Sending email: sorry, stock is not available!");
        }
        else if (BooleanUtils.isNotTrue(paymentSuccessfull))
        {
            log.debug("Sending email: sorry, your payment was not successfull!");
        }
        else
        {
            log.debug("Sending email: your order has been successfully placed!");
        }
    }

}
