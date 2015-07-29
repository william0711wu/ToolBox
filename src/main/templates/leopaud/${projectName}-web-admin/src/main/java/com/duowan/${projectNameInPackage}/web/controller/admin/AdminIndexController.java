package com.duowan.${projectNameInPackage}.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = AdminIndexController.DIR)
public class AdminIndexController {
	public static final String DIR = "/admin";

	@RequestMapping
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("/admin/index");
		return modelAndView;
	}

}
