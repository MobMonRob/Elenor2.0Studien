package de.dhbw.elinor2.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Getter
@Setter
public class TestObject<ReceivedEntity, SavedEntity, Id>
{
    private ReceivedEntity updateEntity;

    private ReceivedEntity newEntity;

    private String baseUrl;

    private JpaRepository<SavedEntity, Id> repository;

    private SavedEntity initSavedEntity;

    private Id initSavedEntityId;

    private UUID initPathId;

    private Class<SavedEntity> entityClass;

    private Class<SavedEntity[]> entityArrayClass;


    public UUID getInitPathId()
    {
        if (initPathId == null)
        {
            return initSavedEntityId instanceof UUID ? (UUID) initSavedEntityId : null;
        }
        return initPathId;
    }
}
