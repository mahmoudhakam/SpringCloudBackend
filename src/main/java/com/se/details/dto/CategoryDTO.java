package com.se.details.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO
{
	@NotEmpty private String categoryName;
	private Set<String> features;
}
