package hu.perit.orchestrator.bpmn.orchestrator;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import hu.perit.orchestrator.services.api.TaskHistoryService;
import hu.perit.spvitamin.spring.config.SpringContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReleaseStockDelegate implements JavaDelegate
{

    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        // Store history
        SpringContext.getBean(TaskHistoryService.class).storeHistory(execution);

        log.debug("Releasing stock...");
    }

}
