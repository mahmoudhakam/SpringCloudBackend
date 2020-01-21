package com.se.details.resource;

import com.se.details.ErrorEndPoint;
import com.se.details.exception.ApiError;
import org.springframework.hateoas.ResourceSupport;

import java.util.Objects;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ApiErrorResource extends ResourceSupport
{

	private final ApiError apiError;

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof ApiErrorResource))
			return false;
		if(!super.equals(o))
			return false;
		ApiErrorResource that = (ApiErrorResource) o;
		return Objects.equals(apiError, that.apiError);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), apiError);
	}

	public ApiErrorResource(ApiError apiError)
	{
		this.apiError = apiError;
		String errorNumber = apiError.getErrorNumber();
		this.add(linkTo(methodOn(ErrorEndPoint.class, errorNumber).getErrorDocumentation(errorNumber)).withSelfRel());
	}

	public ApiError getApiError()
	{
		return apiError;
	}
}
