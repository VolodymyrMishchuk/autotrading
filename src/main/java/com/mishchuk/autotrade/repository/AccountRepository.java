package com.mishchuk.autotrade.repository;

import com.mishchuk.autotrade.repository.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    List<AccountEntity> findByUser_Id(UUID userId);

}