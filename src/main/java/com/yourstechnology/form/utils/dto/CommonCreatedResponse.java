package com.yourstechnology.form.utils.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonCreatedResponse {
	private UUID id;
}
