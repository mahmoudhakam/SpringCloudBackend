package com.se.details.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.se.details.util.PDMicroserviceConstants.RequestResponseFormat.*;

/***
 * @author Mahmoud_Abdelhakam
 */

@Component
@Order(10) // higher number runs first
public class FormatFilter implements Filter
{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void destroy()
	{
		logger.info("Inside destroy method for filter FormatFilter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse respose, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) respose;

		String format = req.getParameter(REQUEST_FORMAT);

		String acceptedHeader = MediaType.APPLICATION_JSON_VALUE;

		if(format != null && format.equals(XML_FORMAT))
		{
			acceptedHeader = MediaType.APPLICATION_XML_VALUE;
		}

		HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
		requestWrapper.addHeader(ACCEPT_HEADER, acceptedHeader);

		chain.doFilter(requestWrapper, resp);

		if(format != null && format.equals(XML_FORMAT))
		{
			resp.setContentType(MediaType.APPLICATION_XML_VALUE);
		}
		else
		{
			resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		logger.info("Inside init method for filter FormatFilter");
	}

}
