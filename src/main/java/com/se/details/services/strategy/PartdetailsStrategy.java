package com.se.details.services.strategy;

import com.se.details.dto.PartdetailsFeatures;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface PartdetailsStrategy
{
	Map<String, Map<String, List<PartdetailsFeatures>>> publishPartdetailsData(List<String> strings, Map<String, Set<String>> categories, boolean b);
}
