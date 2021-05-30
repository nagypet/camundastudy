package hu.perit.orchestrator.services.taskhistory;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.perit.orchestrator.db.appdb.repo.TaskHistoryRepo;
import hu.perit.orchestrator.db.appdb.table.TaskHistoryEntity;
import hu.perit.orchestrator.services.api.TaskHistoryService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskHistoryServiceImpl implements TaskHistoryService
{
    @Autowired
    private TaskHistoryRepo taskHistoryRepo;

    @Override
    public void storeHistory(DelegateExecution execution)
    {
        TaskHistoryEntity taskHistoryEntity = new TaskHistoryEntity();
        taskHistoryEntity.setProcInstId(execution.getProcessInstanceId());
        taskHistoryEntity.setTaskId(execution.getCurrentActivityId());

        log.debug(taskHistoryEntity.toString());
        
        this.taskHistoryRepo.save(taskHistoryEntity);
    }

}
