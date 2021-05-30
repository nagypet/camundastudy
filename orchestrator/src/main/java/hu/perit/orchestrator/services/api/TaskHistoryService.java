package hu.perit.orchestrator.services.api;

import org.camunda.bpm.engine.delegate.DelegateExecution;

public interface TaskHistoryService
{
    void storeHistory(DelegateExecution execution);
}
