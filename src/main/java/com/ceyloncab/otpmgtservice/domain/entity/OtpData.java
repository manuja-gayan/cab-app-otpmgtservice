package com.ceyloncab.otpmgtservice.domain.entity;

import com.ceyloncab.otpmgtservice.domain.utils.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpData {
    private Action action;
	private Integer requestCount;
	private Integer attemptCount;
	private Long initialedTime;
	private Long generatedTime;
	private Long verifiedTime;
	private String otpSecret;
}
