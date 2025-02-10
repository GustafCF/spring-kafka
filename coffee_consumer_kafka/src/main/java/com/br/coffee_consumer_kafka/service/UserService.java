package com.br.coffee_consumer_kafka.service;

import com.br.coffee_consumer_kafka.domain.UserModel;
import com.br.coffee_consumer_kafka.repository.UserRepository;
import com.br.coffee_consumer_kafka.service.exceptions.DatabaseException;
import com.br.coffee_consumer_kafka.service.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public List<UserModel> listAll(){
        return userRepository.findAll();
    }

    public UserModel findById(Long id) {
        Optional<UserModel> user = userRepository.findById(id);
        return user.orElseThrow(()-> new ResourceNotFoundException(id));
    }

    public UserModel insert(UserModel user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public UserModel update(Long id, UserModel user) {
        try {
            UserModel obj = userRepository.getReferenceById(id);
            updateData(obj, user);
            return userRepository.save(obj);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(UserModel entity, UserModel obj) {
        entity.setName(Optional.ofNullable(obj.getName()).orElse(entity.getName()));
        entity.setEmail(Optional.ofNullable(obj.getEmail()).orElse(entity.getEmail()));
        entity.setPhone(Optional.ofNullable(obj.getPhone()).orElse(entity.getPhone()));
    }
}
