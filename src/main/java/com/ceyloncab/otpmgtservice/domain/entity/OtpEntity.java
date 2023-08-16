package com.ceyloncab.otpmgtservice.domain.entity;

import com.ceyloncab.otpmgtservice.domain.utils.UserRole;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Otp")
public class OtpEntity {
	@Id
    private String id;
	private UserRole role;
	@Indexed(unique = true)
	private String msisdn;
	private List<OtpData> actions = new ArrayList<>();
	@LastModifiedDate
	private Date updatedTime;
}
