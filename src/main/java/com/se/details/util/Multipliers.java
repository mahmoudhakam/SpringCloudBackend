package com.se.details.util;

import java.util.*;

public class Multipliers
{
	private static final Map<String, Integer> MULTIPLIERSMAP = new HashMap<>();
	private static final Map<String, List<String>> UNITSMULTIPLIERS = new HashMap<>();
	private static final Set<String> SPECIALUNITS = new HashSet<>();

	private Multipliers()
	{
		intializeSpecialUnits();
		initializeMultipliersMap();
	}

	public static Set<String> getSpecialunits()
	{
		return SPECIALUNITS;
	}

	protected static void intializeSpecialUnits()
	{
		SPECIALUNITS.add("b");
		SPECIALUNITS.add("B");
		SPECIALUNITS.add("bps");
		SPECIALUNITS.add("Bps");
	}

	protected static void initializeMultipliersMap()
	{
		/*
		 * MULTIPLIERS.put("μ", -6); // u and µ and μ are not the same ascii. we don't know how MULTIPLIERS.put("µ", -6);
		 */

		MULTIPLIERSMAP.put("m", -3);
		MULTIPLIERSMAP.put("u", -6);
		MULTIPLIERSMAP.put("n", -9);
		MULTIPLIERSMAP.put("p", -12);
		MULTIPLIERSMAP.put("k", 3);
		MULTIPLIERSMAP.put("K", 3);
		MULTIPLIERSMAP.put("M", 6);
		MULTIPLIERSMAP.put("Mb", 20);
		MULTIPLIERSMAP.put("kb", 10);
		MULTIPLIERSMAP.put("Kb", 10);
		MULTIPLIERSMAP.put("c", -2);
		MULTIPLIERSMAP.put("G", 9);
		MULTIPLIERSMAP.put("Gb", 30);
		MULTIPLIERSMAP.put("T", 12);
		MULTIPLIERSMAP.put("Tb", 40);
		MULTIPLIERSMAP.put("h", 2);

		UNITSMULTIPLIERS.put("A", Arrays.asList("m", "u", "n", "p", "k", "M"));
		UNITSMULTIPLIERS.put("Arms", Arrays.asList("m", "u", "n", "p", "k", "M"));
		UNITSMULTIPLIERS.put("ADC", Arrays.asList("m", "u", "n", "p", "k", "M"));
		UNITSMULTIPLIERS.put("V", Arrays.asList("m", "u", "n", "k", "M"));
		UNITSMULTIPLIERS.put("VDC", Arrays.asList("m", "u", "n", "k", "M"));
		UNITSMULTIPLIERS.put("VAC", Arrays.asList("m", "u", "n", "k", "M"));
		UNITSMULTIPLIERS.put("Vp-p", Arrays.asList("m", "u", "n", "k", "M"));
		UNITSMULTIPLIERS.put("Vrms", Arrays.asList("m", "u", "n", "k", "M"));
		UNITSMULTIPLIERS.put("W", Arrays.asList("m", "u", "n", "k", "M"));
		UNITSMULTIPLIERS.put("VA", Arrays.asList("k", "m", "u", "n", "p"));
		UNITSMULTIPLIERS.put("F", Arrays.asList("k", "m", "u", "n", "p"));
		UNITSMULTIPLIERS.put("m", Arrays.asList("c", "m", "u", "n", "k"));
		UNITSMULTIPLIERS.put("Hz", Arrays.asList("k", "M", "G", "T"));
		UNITSMULTIPLIERS.put("s", Arrays.asList("m", "u", "n", "p"));
		UNITSMULTIPLIERS.put("g", Arrays.asList("m", "k"));
		UNITSMULTIPLIERS.put("bps", Arrays.asList("K", "M", "G", "T"));
		UNITSMULTIPLIERS.put("Bps", Arrays.asList("K", "M", "G", "T"));
		UNITSMULTIPLIERS.put("Bd", Arrays.asList("K", "M", "G"));
		UNITSMULTIPLIERS.put("b", Arrays.asList("K", "M", "G", "T"));
		UNITSMULTIPLIERS.put("B", Arrays.asList("K", "M", "G", "T"));
		UNITSMULTIPLIERS.put("Ohm", Arrays.asList("m", "u", "n", "k", "M", "G"));
		UNITSMULTIPLIERS.put("H", Arrays.asList("m", "u", "n", "k"));
		UNITSMULTIPLIERS.put("bar", Arrays.asList("m", "u", "k"));
		UNITSMULTIPLIERS.put("Pa", Arrays.asList("m", "h", "k", "M", "G"));
	}

	public static Integer getMultiplierValue(String multiplier)
	{
		return MULTIPLIERSMAP.get(multiplier);
	}

	public static Map<String, Integer> getMultipliersmap()
	{
		return MULTIPLIERSMAP;
	}
}
