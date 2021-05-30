package hu.perit.orchestrator.db.appdb.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.perit.orchestrator.db.appdb.table.TaskHistoryEntity;

public interface TaskHistoryRepo extends JpaRepository<TaskHistoryEntity, Long>
{

}
