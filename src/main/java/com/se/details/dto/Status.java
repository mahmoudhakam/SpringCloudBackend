package com.se.details.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonPropertyOrder({ "code", "message", "success" })
@Getter
@Setter
@NoArgsConstructor
public class Status
{

	@JsonProperty("Code") private Integer code;
	@JsonProperty("Message") private String message;
	@JsonProperty("Success") private Boolean success;

	public Status(OperationMessages message, Boolean success)
	{
		this.message = message.getMsg();
		this.code = message.getCode();
		this.success = success;
	}

	public Status(String message, Boolean success)
	{
		super();
		this.message = message;
		this.success = success;
	}
}
