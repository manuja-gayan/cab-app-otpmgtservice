package com.ceyloncab.otpmgtservice.external.repository;

import com.ceyloncab.otpmgtservice.domain.entity.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<CustomerEntity,String> {

}
