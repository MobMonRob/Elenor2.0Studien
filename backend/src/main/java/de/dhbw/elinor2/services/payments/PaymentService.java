package de.dhbw.elinor2.services.payments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public abstract class PaymentService<P, T, ID> implements IPaymentService<P, T, ID>
{
    private final JpaRepository<T, ID> repository;

    protected PaymentService(JpaRepository<T, ID> repository)
    {
        this.repository = repository;
    }

    @Override
    public T create(P paymentPattern, Jwt jwt)
    {
        T entity = convertToEntity(paymentPattern, null);
        executePayment(entity, jwt);
        return repository.save(entity);
    }

    @Override
    public Optional<T> findById(ID id)
    {
        return repository.findById(id);
    }

    @Override
    public Iterable<T> findAll()
    {
        return repository.findAll();
    }

    @Override
    public Optional<T> update(ID id, P paymentPattern, Jwt jwt)
    {
        T updatedEntity = convertToEntity(paymentPattern, id);
        T oldEntity = repository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Entity not found"));

        undoPayment(oldEntity, jwt);
        executePayment(updatedEntity, jwt);

        return repository.findById(id).map(entity ->
                repository.save(updatedEntity));
    }

    @Override
    public void deleteById(ID id, Jwt jwt)
    {
        T entity = repository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Entity not found"));
        undoPayment(entity, jwt);
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(ID id)
    {
        return repository.existsById(id);
    }

    public abstract T convertToEntity(P paymentPattern, ID id);

    public abstract void executePayment(T entity, Jwt jwt);

    public abstract void undoPayment(T entity, Jwt jwt);
}
