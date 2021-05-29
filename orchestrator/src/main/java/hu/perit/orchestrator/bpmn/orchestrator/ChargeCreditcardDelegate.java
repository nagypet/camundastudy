package hu.perit.orchestrator.bpmn.orchestrator;

import java.util.Random;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import hu.perit.orchestrator.config.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChargeCreditcardDelegate implements JavaDelegate
{

    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        boolean sucess = new Random().nextBoolean();
        log.debug(String.format("Charging creditcard... Success: %b", sucess));
        execution.setVariable(Constants.PROCESS_VARIABLE_PAYMENT_SUCCESSFULL, sucess);
    }

}
