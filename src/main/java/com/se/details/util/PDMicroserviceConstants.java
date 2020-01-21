package com.se.details.util;

public class PDMicroserviceConstants
{
	public static class AsyncConfig
	{
		private AsyncConfig()
		{
		}

		public static final String MAX_NUMBER_OF_THREADS = "max.number.of.threads";
		public static final String NUMBER_OF_THREADS = "number.of.threads";
	}

	public static class RequestResponseFormat
	{
		private RequestResponseFormat()
		{
		}

		public static final String ACCEPT_HEADER = "Accept";
		public static final String XML_FORMAT = "xml";
		public static final String DEFAULT_FORMAT = "json";
		public static final String REQUEST_FORMAT = "fmt";
	}

	public static class PararmeterNames
	{
		private PararmeterNames()
		{
		}

		public static final String COM_IDS = "comIds";
		public static final String CATEGORIES = "categories";
	}

	public static class ActorNames
	{
		private ActorNames()
		{
		}

		public static final String PART_DETAILS_ACTOR = "partdetailsActor";
		public static final String PARAMETRICFEATURES = "ParametricFeatures";
		public static final String SUMMARYFEATURES = "SummaryFeatures";
		public static final String RISKFEATURES = "RiskFeatures";
	}

	public static class LCForecastSolrFields
	{
		public static final String FILTERED_FIELDS = "COM_ID,LIFECYCLE_STATUS,YEARS_EOL,LIFECYCLE_RISK,AVAILABILITY_RISK,NUMBER_OF_OTHER_SOURCES";
		public static final String COMID = "COM_ID";
		public static final String LIFECYCLE_STATUS = "LIFECYCLE_STATUS";
		public static final String YEARS_EOL = "YEARS_EOL";
		public static final String LIFECYCLE_RISK = "LIFECYCLE_RISK";
		public static final String AVAILABILITY_RISK = "AVAILABILITY_RISK";
		public static final String NUMBER_OF_OTHER_SOURCES = "NUMBER_OF_OTHER_SOURCES";

		private LCForecastSolrFields()
		{
		}
	}

	public static class TaxonomySolrFields
	{
		public static final String TAX_PL_TYPE_SORT = "PL_TYPE_SORT";
		public static final String TAX_PL_TYPE_NAME = "TYPENAME";
		public static final String TAX_MAIN_NAME = "MAINNAME";
		public static final String TAX_MAIN_ID = "MAINID";
		public static final String TAX_SUB_NAME = "SUBNAME";
		public static final String TAX_SUB_ID = "SUBID";
		public static final String TAX_PL_ID = "PLID";
		public static final String TAX_SUBSEARCHABLE_FLAG = "SUBSEARCHABLEFLAG";
		public static final String TAX_MAINSEARCHABLE_FLAG = "MAINSEARCHABLEFLAG";
		public static final String TAX_PL_NAME = "PLNAME";
		public static final String HCOLNAME = "HCOLNAME";
		public static final String FEATURENAME = "FEATURENAME";

		private TaxonomySolrFields()
		{
		}
	}

	public static class ParametricSolrFields
	{
		public static final String PART_ID = "PART_ID";
		public static final String UNIT = "UNIT";
		public static final String HCOLNAME = "HCOLNAME";
		public static final String FEATURENAME = "FEATURENAME";
		public static final String FILTERD_FIELDS = "PART_ID,PL_NAME,*_FULL";
		public static final String FULL_VALUE = "_FULL";
		public static final String VALUE = "_VALUE";

		private ParametricSolrFields()
		{
		}
	}

	public static class RiksFeaturesNames
	{
		public static final String INVENTORYRISK = "InventoryRisk";
		public static final String LIFECYCLERISK = "LifecycleRisk";
		public static final String YEARSTOENDOFLIFE = "EstimatedYearsToEOL";
		public static final String OTHERSOURCES = "OtherSources";
		public static final String AVAILABILITYRISK = "availabiltyRisk";
		public static final String LIFECYCLESTATUS = "lifecycleStatus";

		private RiksFeaturesNames()
		{
		}
	}

	public static class GSheetStatisticsFeaturesNames
	{
		public static final String SOLESOURCE = "SoleSource";
		public static final String SINGLESOURCE = "SingleSource";
		public static final String MULTISOURCE = "MultiSource";

		private GSheetStatisticsFeaturesNames()
		{
		}
	}

	public static class PartDetailsEndPointResponseColumns
	{

		private PartDetailsEndPointResponseColumns()
		{

		}

		public static final String STATUS = "Status";
		public static final String SERVICE_TIME = "ServiceTime";
		public static final String STATISTICS = "Statistics";
		public static final String PART_FEATURES = "PartsFeatures";

	}

	public static class Jackson
	{
		private Jackson()
		{
		}

		public static final String PART_DETAILS_FILTER_NAME = "partDetailsFilter";
	}

	public static class SummaryCoreFields
	{
		private SummaryCoreFields()
		{
		}

		public static final String COO = "COO";
		public static final String HTSUSA = "HTSUSA";
	}

	public static class SummaryFeatureName
	{
		private SummaryFeatureName()
		{
		}

		public static final String COO = "CountryOfOrigin";
		public static final String HTSUSA = "HTSUSA";
	}
}
