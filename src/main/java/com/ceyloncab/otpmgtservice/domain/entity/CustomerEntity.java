package com.ceyloncab.otpmgtservice.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Customer")
public class CustomerEntity {
	@Id
    private String userId;
	private String firstName;
	private String lastName;
	@Indexed(unique = true)
	private String msisdn;
	private String location;
	@LastModifiedDate
	private Date updatedTime;
}
