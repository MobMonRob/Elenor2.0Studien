package de.dhbw.elinor2.services.payments;

import de.dhbw.elinor2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public abstract class PaymentService<PaymentPattern, Entity, Id> implements IPaymentService<PaymentPattern, Entity, Id>
{
    private final JpaRepository<Entity, Id> repository;

    @Autowired
    protected UserService userService;

    public PaymentService(JpaRepository<Entity, Id> repository)
    {
        this.repository = repository;
    }

    @Override
    public Entity create(PaymentPattern paymentPattern, Jwt jwt)
    {
        Entity entity = convertToEntity(paymentPattern, null);
        executePayment(entity, jwt);
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
    public Optional<Entity> update(Id id, PaymentPattern paymentPattern, Jwt jwt)
    {
        Entity updatedEntity = convertToEntity(paymentPattern, id);
        Entity oldEntity = repository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Entity not found"));

        undoPayment(oldEntity, jwt);
        executePayment(updatedEntity, jwt);

        return repository.findById(id).map(entity ->
                repository.save(updatedEntity));
    }

    @Override
    public void deleteById(Id id, Jwt jwt)
    {
        Entity entity = repository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Entity not found"));
        undoPayment(entity, jwt);
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Id id)
    {
        return repository.existsById(id);
    }

    public abstract Entity convertToEntity(PaymentPattern paymentPattern, Id id);

    public abstract void executePayment(Entity entity, Jwt jwt);

    public abstract void undoPayment(Entity entity, Jwt jwt);
}
