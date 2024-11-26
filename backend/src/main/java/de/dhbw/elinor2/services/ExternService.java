package de.dhbw.elinor2.services;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.repositories.ExternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExternService
{

    @Autowired
    private ExternRepository externRepository;

    @Transactional
    public Extern createExtern(Extern extern)
    {
        return externRepository.save(extern);
    }

    public Optional<Extern> getExternById(UUID id)
    {
        return externRepository.findById(id);
    }

    public List<Extern> getAllExtern()
    {
        return externRepository.findAll();
    }

    @Transactional
    public Optional<Extern> updateExtern(UUID id, Extern externDetails)
    {
        return externRepository.findById(id).map(extern ->
        {
            extern.setName(externDetails.getName());
            return externRepository.save(extern);
        });
    }

    @Transactional
    public void deleteExtern(UUID id)
    {
        externRepository.deleteById(id);
    }
}
