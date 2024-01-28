package com.ru.jdevrestapi.controller;

import com.ru.jdevrestapi.model.Profissao;
import com.ru.jdevrestapi.repository.ProfissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/profissao")
public class ProfissaoController {

    @Autowired
    private ProfissaoRepository profissaoRepository;

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<Profissao>> profissoes() {
        List<Profissao> lista = profissaoRepository.findAll();
        return new ResponseEntity<List<Profissao>>(lista, HttpStatus.OK);
    }
}
