package de.dhbw.elinor2.services.payments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public abstract class PaymentService<PaymentPattern, Entity, Id> implements IPaymentService<PaymentPattern, Entity, Id>
{
    private final JpaRepository<Entity, Id> repository;

    public PaymentService(JpaRepository<Entity, Id> repository)
    {
        this.repository = repository;
    }

    @Override
    public Entity create(PaymentPattern paymentPattern)
    {
        Entity entity = convertToEntity(paymentPattern);
        executePayment(entity);
        return repository.save(entity);
    }

    @Override
    public Optional<Entity> findById(Id id)
    {
        return repository.findById(id);
    }

    @Override
    public Iterable<Entity> findAll()
    {
        return repository.findAll();
    }

    @Override
    public Optional<Entity> update(Id id, PaymentPattern paymentPattern)
    {
        Entity updatedEntity = convertToEntity(paymentPattern);
        Entity oldEntity = repository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Entity not found"));

        Optional<Entity> response = repository.findById(id).map(entity ->
                repository.save(updatedEntity));

        undoPayment(oldEntity);
        executePayment(updatedEntity);

        return response;
    }

    @Override
    public void deleteById(Id id)
    {
        Entity entity = repository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Entity not found"));
        undoPayment(entity);
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Id id)
    {
        return repository.existsById(id);
    }

    public abstract Entity convertToEntity(PaymentPattern paymentPattern);

    public abstract void executePayment(Entity entity);

    public abstract void undoPayment(Entity entity);
}
