package de.dhbw.elinor2.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Getter
@Setter
public class TestObject<Entity, Id>
{
    private String baseUrl;

    private JpaRepository<Entity, Id> repository;

    private Entity initEntity;

    private Id initEntityId;

    private UUID initPathId;

    private Entity updateEntity;

    private Entity newEntity;

    private Class<Entity> entityClass;

    private Class<Entity[]> entityArrayClass;


    public UUID getInitPathId()
    {
        if(initPathId == null)
        {
            return initEntityId instanceof UUID ? (UUID) initEntityId : null;
        }
        return initPathId;
    }
}
