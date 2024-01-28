package com.ru.jdevrestapi.repository;

import com.ru.jdevrestapi.model.Telefone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends CrudRepository<Telefone, Long> {
}
