package com.springSecurity.token;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<TokenModel, Integer> {
    Optional<TokenModel> findByValue(String value);
}
