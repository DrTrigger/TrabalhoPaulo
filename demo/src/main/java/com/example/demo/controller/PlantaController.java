package com.example.demo.controller;

import com.example.demo.Enum.CategoriaEnum;
import com.example.demo.entity.PlantaEntity;
import com.example.demo.entity.ProjetoEntity;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.entity.dto.PlantaDTO;
import com.example.demo.repository.PlantaRepository;
import com.example.demo.service.PlantaService;
import com.example.demo.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/planta")
public class PlantaController implements GenericController<PlantaEntity, String>{

    @Autowired
    private PlantaRepository plantaRepository;

    @Autowired
    private PlantaService plantaService;

    @GetMapping("/plantas")
    @Override
    public ResponseEntity<List<PlantaEntity>> listAll() {
        List<PlantaEntity>plantaEntities = plantaRepository.findAll();
        return ResponseEntity.ok(plantaEntities);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit(@RequestBody @Valid PlantaDTO entity, @PathVariable Long id){
        Optional<PlantaEntity> plantaOptional = plantaRepository.findById(id);

        if (plantaOptional.isPresent()) {
            PlantaEntity plantaEntity = plantaOptional.get();
            plantaEntity.setNome(entity.nome());
            plantaEntity.setNome_cientifico(entity.nome_cientifico());
            plantaEntity.setDescricao(entity.descricao());
            plantaEntity.setOrigem(entity.origem());
            plantaEntity.setCuidados(entity.cuidados());
            Date date;
            plantaEntity.setDataregistro(Date.valueOf(entity.dataregistro()));
            if(!Utils.findMatchEnum(entity.categoria(), CategoriaEnum.class)){throw new RuntimeException("Enum invalido");}
            plantaEntity.setCategoria(CategoriaEnum.valueOf(entity.categoria().toUpperCase()));

            plantaRepository.save(plantaEntity);
            return ResponseEntity.ok("Planta atualizada com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @Deprecated
    @Override
    public ResponseEntity<String> update(Long id, PlantaEntity updatedEntity) {
        return null;
    }

    //@PostMapping("/criar")
    @Deprecated
    @Override
    public ResponseEntity<String> create(@RequestBody @Valid PlantaEntity entity) {
        return null;
    }

    @PostMapping("/criar")
    public ResponseEntity<String> create(@RequestBody @Valid PlantaDTO entity) {
        plantaService.criar(entity);
        return ResponseEntity.ok("Planta criado com sucesso");
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> delete(Long id) {
        Optional<PlantaEntity> plantaEntityOptional = plantaRepository.findById(id);

        if (plantaEntityOptional.isPresent()) {
            this.plantaRepository.deleteById(id);
            return ResponseEntity.ok("planta excluído com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<PlantaEntity> getById(Long id) {
        Optional<PlantaEntity> planta = plantaRepository.findById(id);
        if (planta.isPresent()) {
            return ResponseEntity.ok(planta.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
