package com.br.coffee_consumer_kafka.repository;

import com.br.coffee_consumer_kafka.domain.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByName(String name);
}
