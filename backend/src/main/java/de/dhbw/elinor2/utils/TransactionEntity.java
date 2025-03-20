package de.dhbw.elinor2.utils;

import java.util.UUID;

public class TransactionEntity
{
    private UUID entityId;
    private String name;

    public TransactionEntity(UUID entityId, String name)
    {
        this.entityId = entityId;
        this.name = name;
    }

    public UUID getEntityId()
    {
        return entityId;
    }

    public void setEntityId(UUID entityId)
    {
        this.entityId = entityId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
