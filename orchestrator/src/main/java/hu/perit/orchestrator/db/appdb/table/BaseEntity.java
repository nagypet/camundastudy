package hu.perit.orchestrator.db.appdb.table;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity
{
    protected abstract Long getId();

    // Auditing
    @CreatedBy
    @Column(name = "createdby", nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "createdat", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updatedby", nullable = false, updatable = true)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updatedat", nullable = false, updatable = true)
    private LocalDateTime updatedAt;


    @Override
    public int hashCode()
    {
        return new HashCodeBuilder() //
            .append(this.getId()) //
            .hashCode();
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;

        return new EqualsBuilder() //
            .append(this.getId(), other.getId()).isEquals();
    }

}
