package de.dhbw.elinor2.services.payments;

import de.dhbw.elinor2.utils.InputPayment;
import de.dhbw.elinor2.utils.OutputPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class PaymentService<IP extends InputPayment, OP extends OutputPayment, T, ID> implements IPaymentService<IP, OP, T, ID>
{
    private final JpaRepository<T, ID> repository;

    protected PaymentService(JpaRepository<T, ID> repository)
    {
        this.repository = repository;
    }

    @Override
    public T create(IP paymentPattern, Jwt jwt)
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
    public Collection<OP> findAll()
    {
        List<OP> result = new ArrayList<>();
        List<T> entities = repository.findAll();
        for(T entity : entities)
        {
            result.add(convertEntityToOutputPattern(entity));
        }
        return result;
    }

    @Override
    public OP update(ID id, IP paymentPattern, Jwt jwt)
    {
        T updatedEntity = convertToEntity(paymentPattern, id);
        T oldEntity = repository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Entity not found"));

        undoPayment(oldEntity, jwt);
        executePayment(updatedEntity, jwt);

        Optional<T> savedEntity = repository.findById(id).map(entity ->
                repository.save(updatedEntity));
        if(savedEntity.isPresent())
        {
            return convertEntityToOutputPattern(savedEntity.get());
        }
        throw new IllegalArgumentException("Updated entity not found!");
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

    public abstract T convertToEntity(IP paymentPattern, ID id);

    public abstract void executePayment(T entity, Jwt jwt);

    public abstract void undoPayment(T entity, Jwt jwt);

    public abstract OP convertEntityToOutputPattern(T entity);
}
