package com.dh.PI.services;

import com.dh.PI.dto.Characteristics.CharacteristicsDTO;
import com.dh.PI.exceptions.ResourceAlreadyExistsException;
import com.dh.PI.exceptions.ResourceNotFoundException;
import com.dh.PI.model.Characteristic;
import com.dh.PI.repositories.CharacteristicRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CharacteristicService {

    @Autowired
    private CharacteristicRepository repository;

    public CharacteristicsDTO create(CharacteristicsDTO characteristicsDTO){

        if (repository.findByName(characteristicsDTO.getName()) != null){
            throw new ResourceAlreadyExistsException("Characteristic already exist: " + characteristicsDTO.getName());
        }

        Characteristic characteristic = new Characteristic();

        BeanUtils.copyProperties(characteristicsDTO, characteristic);

        return  new CharacteristicsDTO(repository.save(characteristic));
    }

    public List<Characteristic> findAllByName(Set<String> characters){

        List<Characteristic> characteristics = new ArrayList<>();
        List<String> namesNotMatch = new ArrayList<>();

        characters.forEach(character -> {
            if (repository.findByName(character) == null) {
                namesNotMatch.add(character);
            }else{
                characteristics.add(repository.findByName(character));
            }
        });

        if (!namesNotMatch.isEmpty()){
            throw new ResourceNotFoundException("Characteristic not found: " + namesNotMatch);
        }

        return characteristics;
    }

    public Characteristic findByName(String characteristic){
        Characteristic characteristicModel = repository.findByName(characteristic);
        if (characteristicModel == null){
            throw new ResourceNotFoundException("Characteristic not found: " + characteristic);
        }

        return characteristicModel;
    }

}
