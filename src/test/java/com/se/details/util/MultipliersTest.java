package com.se.details.util;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
public class MultipliersTest
{
	@Test
	public void specialUnitsShouldNotBeEmpty()
	{
		Multipliers.intializeSpecialUnits();
		Set<String> specialUnits = Multipliers.getSpecialunits();
		boolean expected = !specialUnits.isEmpty();
		assertTrue(expected);
	}

	@Test
	public void multipliersMapShouldNotBeEmpty()
	{
		Multipliers.initializeMultipliersMap();
		Map<String, Integer> multipliersMap = Multipliers.getMultipliersmap();
		boolean expected = !multipliersMap.isEmpty();
		assertTrue(expected);
	}

	@Test
	public void multiplierHasValuePower()
	{
		Integer multiplierPower = Multipliers.getMultiplierValue("m");
		assertNotNull(multiplierPower);
	}

	@Test
	public void multiplierHasNoValuePower()
	{
		Integer multiplierPower = Multipliers.getMultiplierValue("mBgg");
		assertNull(multiplierPower);
	}
}
