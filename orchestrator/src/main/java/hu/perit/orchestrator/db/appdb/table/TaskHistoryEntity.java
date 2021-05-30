package hu.perit.orchestrator.db.appdb.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "task_history" /*, schema = "taskhistory"*/)
public class TaskHistoryEntity extends BaseEntity
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_history_id", nullable = false, columnDefinition = "bigserial")
    private Long id;

    @Column(name = "proc_inst_id", nullable = false)
    private String procInstId;
    
    @Column(name = "task_id", nullable = false)
    private String taskId;
}
