package com.se.details.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.se.details.util.PDMicroserviceConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartDetailsBaseResponse
{

	@JsonProperty(PDMicroserviceConstants.PartDetailsEndPointResponseColumns.STATUS) private Status status;

	@JsonProperty(PDMicroserviceConstants.PartDetailsEndPointResponseColumns.SERVICE_TIME) private String serviceTime;

	public PartDetailsBaseResponse(Status status)
	{
		this.status = status;
	}

	@Override
	public String toString()
	{
		return "PartDetailsBaseResponse{" + "status=" + status + ", serviceTime='" + serviceTime + '\'' + '}';
	}
}
