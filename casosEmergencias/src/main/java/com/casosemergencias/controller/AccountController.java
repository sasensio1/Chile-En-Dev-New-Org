package com.casosemergencias.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.casosemergencias.controller.views.AccountView;
import com.casosemergencias.logic.AccountService;
import com.casosemergencias.model.Cuenta;
import com.casosemergencias.util.ParserModelVO;
import com.casosemergencias.util.constants.Constantes;
import com.casosemergencias.util.datatables.DataTableParser;
import com.casosemergencias.util.datatables.DataTableProperties;

@Controller
public class AccountController {
	
	final static Logger logger = Logger.getLogger(LoginController.class);
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/private/homeCuentas", method = RequestMethod.GET)
	public ModelAndView listadoCuentas() {
		
		logger.info("--- Inicio -- listadoCuentas ---");
		
		ModelAndView model = new ModelAndView();
		model.setViewName("private/homeCuentasPage");
			
		logger.info("--- Fin -- listadoCuentas ---");
		return model;
	}
	
	/**
	 * Método que obtiene los datos de una cuenta a partir de su id. y los
	 * devuelve a la página.
	 * 
	 * @param sfid
	 *            Identificador de la cuenta.
	 * @return ModelAndView Datos de la cuenta a mostrar en la página.
	 */
	@RequestMapping(value = "/private/entidadCuenta", method = RequestMethod.GET)
	public ModelAndView getAccountData(@RequestParam String sfid,HttpServletRequest request) {
		logger.trace("Detalle de cuenta");
		HttpSession session = request.getSession(true);
		
		
		session.setAttribute(Constantes.SFID_CUENTA, sfid);	
		session.setAttribute(Constantes.FINAL_DETAIL_PAGE, "CUENTA");
		
		AccountView cuentaView = new AccountView();
		ModelAndView model = new ModelAndView();
		Cuenta cuentaBBDD = accountService.getAccountBySfid(sfid);
		
		if (cuentaBBDD != null) {
			ParserModelVO.parseDataModelVO(cuentaBBDD, cuentaView);
		}
		
		/*Almacenamos sfid de contactos y suministros relacionados en caso de que la cuenta seleccionado tenga uno de cada
		
		if(cuentaView.getContactos()!=null && cuentaView.getContactos().isEmpty()==false  && cuentaView.getContactos().size()==1 && session.getAttribute(Constantes.SFID_CONTACTO)==null){
			session.setAttribute(Constantes.SFID_CONTACTO, cuentaView.getContactos().get(0).getSfid());					
		}
		
		if(cuentaView.getSuministros()!=null && cuentaView.getSuministros().isEmpty()==false  && cuentaView.getSuministros().size()==1 && session.getAttribute(Constantes.SFID_SUMINISTRO)==null){
			session.setAttribute(Constantes.SFID_SUMINISTRO, cuentaView.getSuministros().get(0).getSfid());					
		}*/
		
		logger.info("SFID_CUENTA" + session.getAttribute(Constantes.SFID_CUENTA));
		logger.info("SFID_CONTACTO" + session.getAttribute(Constantes.SFID_CONTACTO));
		logger.info("SFID_SUMINISTRO" + session.getAttribute(Constantes.SFID_SUMINISTRO));
		logger.info("FINAL_DETAIL_PAGE" + session.getAttribute(Constantes.FINAL_DETAIL_PAGE));

		
		
		model.addObject("sfid", sfid);
		model.setViewName("private/entidadCuentaPage");
		model.addObject("cuenta", cuentaView);
		return model;
	}

	/**
	 * M&eacute;todo para recuperar los datos del lsitado de de cuentas.
	 * 
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/listarCuentas", method = RequestMethod.POST)
	public @ResponseBody String listadoCuentas(@RequestBody String body,HttpServletRequest request){
		
		logger.info("--- Inicio -- listadoCuentas ---");
		
		//Limpieza de sfid que arrastramos
		
		HttpSession session = request.getSession(true);	
		
		session.setAttribute(Constantes.SFID_SUMINISTRO, null);	
		session.setAttribute(Constantes.SFID_CONTACTO, null);	
		session.setAttribute(Constantes.SFID_CUENTA, null);	
		session.setAttribute(Constantes.FINAL_DETAIL_PAGE, null);	
		
		//Limpieza de sfid que arrastramos
		
		DataTableProperties propDataTable = DataTableParser.parseBodyToDataTable(body);
		List<Cuenta> listaCuentas = new ArrayList<Cuenta>();
		
		JSONObject jsonResult = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		listaCuentas = accountService.readAllCuentas(propDataTable);

		for (Cuenta cuenta : listaCuentas) {
			jsonResult = new JSONObject();
			jsonResult.put("name", cuenta.getName());
			jsonResult.put("masterrecord__run__c", cuenta.getAccountRun());
			jsonResult.put("tel_fono_principal__c", cuenta.getTelefonoPrincipal());
			jsonResult.put("email", cuenta.getEmailPrincipal());
			jsonResult.put("sfid", cuenta.getSfid());
			jsonArray.put(jsonResult);
		}
		
		Integer numCuentas = accountService.getNumCuentas(propDataTable);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("iTotalRecords", numCuentas); 
		jsonObject.put("iTotalDisplayRecords", numCuentas); 
		jsonObject.put("data", jsonArray);
		jsonObject.put("draw", propDataTable.getDraw());
		
		logger.info("SFID_CUENTA" + session.getAttribute(Constantes.SFID_CUENTA));
		logger.info("SFID_CONTACTO" + session.getAttribute(Constantes.SFID_CONTACTO));
		logger.info("SFID_SUMINISTRO" + session.getAttribute(Constantes.SFID_SUMINISTRO));
		
		logger.info("--- Fin -- listadoCuentas ---");
		
		return jsonObject.toString();
	}
}