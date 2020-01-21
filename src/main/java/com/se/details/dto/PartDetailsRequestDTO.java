package com.se.details.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@ApiModel(description = "Class representing a part detail request.")
@Getter
@Setter
@NoArgsConstructor
public class PartDetailsRequestDTO
{

	private Set<@Pattern(regexp = "[0-9]+") String> comIds;

	@Valid @NotNull private Set<CategoryDTO> categories;

	public PartDetailsRequestDTO(Set<String> comIds, Set<CategoryDTO> categories)
	{
		super();
		this.comIds = comIds;
		this.categories = categories;
	}

	@Override
	public String toString()
	{
		return "PartDetailsRequestDTO{" + "comIds=" + comIds + ", categories=" + categories + '}';
	}
}