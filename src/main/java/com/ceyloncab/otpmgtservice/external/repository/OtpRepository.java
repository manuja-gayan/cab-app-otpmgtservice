package com.ceyloncab.otpmgtservice.external.repository;

import com.ceyloncab.otpmgtservice.domain.entity.OtpEntity;
import com.ceyloncab.otpmgtservice.domain.utils.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<OtpEntity,String> {
    Optional<OtpEntity> findOneByMsisdnAndRole(String msisdn, UserRole role);
}
