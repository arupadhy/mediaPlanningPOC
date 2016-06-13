package com.sapient.ai.media.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sapient.ai.media.domain.loader.DomainDataLoader;

@Controller
public class HomeController {
	
	
	private static final String CLIENT_BRIEF =" [	{		\"client\":\"DaveMart\",		\"headline\":\"Overview for 2016\",		\"description\": \"We are a major retailer with both general merchandise and fresh grocery (meat and produce).  We are trying to grow our Fresh sales with existing customers. Customers who shop with us for dry groceries but go to competitors for fresh purchases\",		\"budget\": {			\"total\": \"700k\",			\"used\": \"150k\",			\"available\": \"550k\",			\"image\":\"linkForImage\"		}	},	{		\"client\":\"DummyclientName\",		\"headline\":\"Overview for 2016\",		\"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis pellentesque interdum scelerisque. Donec venenatis congue rhoncus. Maecenas vitae turpis quis ligula ullamcorper sodales. Nam sed erat dolor. Quisque auctor ipsum ligula, a rhoncus turpis bibendum vel. Sed tincidunt commodo velit, sit amet convallis ligula dapibus vitae. Morbi pellentesque hendrerit quam. Quisque ut varius nulla. Duis quis nibh nec lectus imperdiet sollicitudin. Nullam ligula metus, laoreet ac sollicitudin quis, facilisis eget odio. Cras sed diam eu quam venenatis pharetra sit amet ut elit. Nulla ultricies, arcu in interdum aliquam, nunc lorem porta ante, in blandit justo purus ac magna. Vestibulum iaculis, lacus vel gravida ullamcorper, purus quam ornare nunc, et suscipit diam nisi vitae eros. Maecenas eu pretium arcu. Nulla facilisi.\",		\"budget\": {			\"total\": \"800k\",			\"used\": \"250k\",			\"available\": \"550k\",			\"image\":\"linkForImage\"		}	}]";
	
	@Autowired
	private DomainDataLoader domainLoader;
	
	@RequestMapping("/")
	public String home() {
		System.out.println("got request");
		return "index.html";
	}
	
	@RequestMapping("/clientBrief")
	public @ResponseBody String clientBrief() {
		return CLIENT_BRIEF;
	}
	
	@RequestMapping("/getdomain")
	public @ResponseBody List<Object> getDomainData(@RequestParam String name) throws Exception {
		return domainLoader.getDomainCache().get(name);
	}

}
