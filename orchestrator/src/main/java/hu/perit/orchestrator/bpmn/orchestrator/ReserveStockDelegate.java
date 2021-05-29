package hu.perit.orchestrator.bpmn.orchestrator;

import java.util.Random;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import hu.perit.orchestrator.config.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReserveStockDelegate implements JavaDelegate
{

    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        boolean available = new Random().nextBoolean();
        log.debug(String.format("Reserving stock... Available: %b", available));
        execution.setVariable(Constants.PROCESS_VARIABLE_STOCK_AVAILABLE, available);
    }

}
