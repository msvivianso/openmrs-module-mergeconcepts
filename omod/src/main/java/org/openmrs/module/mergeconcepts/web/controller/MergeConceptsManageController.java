package org.openmrs.module.mergeconcepts.web.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The controller.
 */
@Controller
public class MergeConceptsManageController {
	
	private Logger log = Logger.getLogger(MergeConceptsManageController.class);
	
	/**
	 * Called when any page is requested, does not respond to concept search widgets
	 * @should set model attribute "newConcept" to concept user wants to keep
	 * @param newConceptId
	 * @return
	 */
	@ModelAttribute("newConcept")
	public Concept getNewConcept(@RequestParam(required=false, value="newConceptId") String newConceptId){
		//going to make this use ConceptEditor instead
		Concept newConcept = Context.getConceptService().getConcept(newConceptId);
		return newConcept;
	}
	
	/**
	 * Called when any page is requested, does not respond to concept search widgets
	 * @should set model attribute "oldConcept" to concept user wants to retire
	 * @param oldConceptId
	 * @return
	 */
	@ModelAttribute("oldConcept")
	public Concept getOldConcept(@RequestParam(required=false, value="oldConceptId") String oldConceptId){
		//going to make this use ConceptEditor instead
		Concept oldConcept = Context.getConceptService().getConcept(oldConceptId);
		return oldConcept;
	}
	
	/**
	 * @should generate fresh lists of references to old concept
	 */
	public Map<String, List> generateReferenceLists(@RequestParam(required=false, value="oldConceptId") String oldConceptId){
		
		Map<String, List> oldConceptRefs = new HashMap<String, List>();
		
		//OBS
		ObsService obsService = Context.getObsService(); //ObsEditor?
		
		List<Obs> obsToConvert;
		obsToConvert = obsService.getObservationsByPersonAndConcept(null, getOldConcept(oldConceptId));
		oldConceptRefs.put("obs", obsToConvert);
		log.info("oldObsCount = "+oldConceptRefs.get("obs").size());
		
		return oldConceptRefs;
		
		//FORMS
	}
	

	/**
	 * @should generate fresh lists of references to new concept
	 */
	public Map<String, List> generateNewReferenceLists(@RequestParam(required=false, value="newConceptId") String newConceptId){

		Map<String, List> newConceptRefs = new HashMap<String, List>();
		
		//OBS
		ObsService obsService = Context.getObsService(); //ObsEditor?
		
		List<Obs> newConceptObs;
		newConceptObs = obsService.getObservationsByPersonAndConcept(null, getNewConcept(newConceptId));
		newConceptRefs.put("obs", newConceptObs);
		log.info("newObsCount = "+newConceptRefs.get("obs").size());
		
		return newConceptRefs;
		
		//FORMS
	}
	
	/**
	 * Default page from admin link or results page
	 * @should do nothing
	 * @param map
	 */
	@RequestMapping(value="/module/mergeconcepts/chooseConcepts", 
			method=RequestMethod.GET)
	public void showPage(ModelMap model) {
			
	}
	
	/**
	 * Method is called when going back to choose concepts page after an error
	 * or from preview page "no, I'm not sure"
	 * @should (eventually) prepopulate concept widgets
	 */
	@RequestMapping(value="/module/mergeconcepts/chooseConcepts", 
			method=RequestMethod.POST)
	public void chooseConcepts(){
		//were concepts submitted? is the concept being kept non-retired? etc
		
		
	}
	
	/**
	 * Method is called on submitting chooseConcepts form
	 * @should display references to oldConcept and newConcept
	 * @param map
	 */
	@RequestMapping(value="/module/mergeconcepts/preview", method=RequestMethod.POST)
	public void preview(ModelMap model, @RequestParam("oldConceptId") String oldConceptId,
										@RequestParam("newConceptId") String newConceptId) {
		
		//were concepts submitted? is the concept being kept non-retired? etc. if not, redirect
		
		Map<String, List> newConceptRefs= generateNewReferenceLists(newConceptId);
		Map<String, List> oldConceptRefs= generateNewReferenceLists(oldConceptId);
		
		model.addAttribute("newObsCount", newConceptRefs.get("obs").size());
		model.addAttribute("oldObsCount", oldConceptRefs.get("obs").size());
		
	}
	
	/**
	 * Method is called after user confirms preview page
	 * @should merge concepts
	 * @param map
	 */
	@RequestMapping("/module/mergeconcepts/executeMerge")
	public String executeMerge(ModelMap map) {
			//ask for conceptIds
			//merge!
			//retire oldConcept
			return "redirect:results.form";
	}
	
	/**
	 * Method is called after executeMerge() is finished
	 * @should display updated references to oldConcept and newConcept
	 * @param map
	 */
	@RequestMapping("/module/mergeconcepts/results")
	public void results(ModelMap map) {
		
	}
	
}

/**
 * Pieces of code I might want later...
 * 
 * @param map
 * @param oldConceptId
 * @param newConceptId
 * @param httpSession

@RequestMapping(value="/module/mergeconcepts/chooseConcepts", 
				method=RequestMethod.POST)
public void afterPageSubmission(ModelMap map, 
		@RequestParam("oldConceptId") Integer oldConceptId,
		@RequestParam("newConceptId") Integer newConceptId,
		@RequestParam(required=false, value="back") Boolean back,
		HttpSession httpSession) {
	
	//??
	@ModelAttribute
	public Concept getNewConcept(@RequestParam(required=false, value="newConceptId") Concept newConcept){
		return newConcept;
	}

	@ModelAttribute
	public Concept getOldConcept(@RequestParam(required=false, value="oldConceptId") Concept oldConcept){
		return oldConcept;
	}
	
	model.addAttribute("oldConcept", Context.getConceptService().getConcept(oldConceptId));
	model.addAttribute("newConcept", Context.getConceptService().getConcept(newConceptId));
	
	
	Concept oldConcept = Context.getConceptService().getConcept(oldConceptId); 
	Concept newConcept = Context.getConceptService().getConcept(newConceptId);
	
	
	
	
	List<Obs> obsToConvert;
	ObsService obsService = Context.getObsService();
	obsToConvert = obsService.getObservationsByPersonAndConcept(null, oldConcept);
	
	String msg = "Converted question concept from " + oldConcept + " to " + newConcept;
	
	Integer count = 0;
	for (Obs o : obsToConvert) {
		count = count + 1;
		o.setConcept(newConcept);
		obsService.saveObs(o, msg);
	}
	
	/*
	Set<FormField> formFields1 = new HashSet<FormField>();
	
	List<Form> allForms = Context.getFormService().getAllForms();
	Set<Form> oldConceptForms = new HashSet<Form>();
	
	//FormFields with old concept (might be a better way to do this)
	for(Form f : allForms){
		formFields1.add(Context.getFormService().getFormField(f, oldConcept));
	}		
	
	//update fields
	for(FormField f : formFields1){
		//forms that ref oldConcept
		oldConceptForms.add(f.getForm());
		
		//update fields
		f.getField().setConcept(newConcept);
		Context.getFormService().saveField(f.getField());
	}
	
	//change message
	httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Obs converted successfully. total converted: " + count); //+ " Rebuild all forms that used this concept: " + oldConceptForms);
	
}*/

/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 
package org.openmrs.module.mergeconcepts.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller.

@Controller
public class  MergeConceptsManageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	

	
	
	@RequestMapping (value="/module/mergeconcepts/chooseConceptsToMerge", method=RequestMethod.GET)
	public void showForm(){
		
	}
	
	@RequestMapping(value = "/module/mergeconcepts/manage", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
	}
	
			//method getForms() is in FormFields.java
		for (Field f : fields) {
			f.setConcept(newConcept);
			if (f.getForms() != null)
				formsNeedingRebuilding.addAll(f.getForms());
			Context.getFormService().saveField(f);
		}
}*/
