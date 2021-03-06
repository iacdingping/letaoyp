package com.iac.letaoyp.controller.info;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.iac.letaoyp.biz.web.AjaxResult;
import com.iac.letaoyp.controller.BasicController;
import com.iac.letaoyp.entity.info.Advertise;
import com.iac.letaoyp.service.info.AdvertiseService;


@Controller
@RequestMapping("info/advertise")
public class AdvertiseController extends BasicController {
	
	@Autowired
	private AdvertiseService advertiseService;
	
	/** 列表 */
	@RequestMapping
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = DEFAULT_SORT_TYPE) String sortType,
			Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<Advertise> page = advertiseService.findPage(searchParams, pageNumber, pageSize, sortType);
				
		model.addAttribute("page", page);
		model.addAttribute("sortType", sortType);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "info/advertiseList";
	}
	
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public String createForm(ModelMap model) {
		return "info/advertiseForm";
	}
	
	@RequestMapping(value="create/from", method = RequestMethod.GET)
	public String createFrom(ModelMap model, @RequestParam("sequence") Long sequence, 
			@RequestParam("model") Advertise.Model advertiseModel) {
		Advertise advertise = advertiseService.findBySequenceAndModel(sequence, advertiseModel);
		model.addAttribute("advertise",advertise);
		return "info/advertiseForm";
	}
	
	@RequestMapping(value="update/{id}", method = RequestMethod.GET)
	public String updateForm(ModelMap model, @PathVariable java.lang.Long id) {
		Advertise advertise = advertiseService.get(id);
		model.addAttribute("advertise",advertise);
		return "info/advertiseForm";
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult update(@Valid @ModelAttribute("advertise") Advertise advertise) {
		advertiseService.save(advertise);
		return AjaxResult.SUCCEED;
	}
	
	/** 删除 */
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public AjaxResult delete(@PathVariable("id") java.lang.Long id) {
		advertiseService.delete(id);
		return AjaxResult.succeed();
	}

	@RequestMapping(value = "delete", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult batchDelete(@RequestParam("ids") java.lang.Long[] ids) {
		if(ids == null || ids.length == 0) {
			return AjaxResult.ERROR_PARAM;
		}
		advertiseService.delete(ids);
		return AjaxResult.succeed();
	}
	
	@RequestMapping(value = "settings", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult settings(@RequestParam("ids")Long[] ids, @RequestParam("active")boolean active) {
		if(ids == null || ids.length == 0) {
			return AjaxResult.ERROR_PARAM;
		}
		
		advertiseService.updateActiveByIds(active, ids);
		return AjaxResult.succeed();
	}

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,
	 * 先根据form的id从数据库查出menu对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getAdvertise(@RequestParam(value = "id", defaultValue = "-1") java.lang.Long id, Model model) {
		if (id != -1) {
			model.addAttribute("advertise", advertiseService.get(id));
		}
	}
}

