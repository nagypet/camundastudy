package hu.perit.orchestrator.bpmn.orchestrator;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReleaseStockDelegate implements JavaDelegate
{

    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        log.debug("Releasing stock...");
    }

}
