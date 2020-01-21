package com.se.details.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public enum OperationMessages
{

	//@formatter:off
    
	SUCCESSFULL_OPERATION(0, "Successfull Operation"), 
	FAILED_OPERATION(1, "Failed Operation"),
    NO_RESULT_FOUND(2, "No Result Found"),
    INTERNAL_ERROR(3, "Internal Error"),
    NO_CATEGORIES_RECEIVED(4, "No Categories Received");
	
	//@formatter:on

	private Integer code;
	private String msg;

	private OperationMessages(int code, String msg)
	{
		this.code = code;
		this.msg = msg;
	}
}
