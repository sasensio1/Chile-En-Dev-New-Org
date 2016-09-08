package com.casosemergencias.logic;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.casosemergencias.dao.CaseCommentDAO;
import com.casosemergencias.dao.CaseDAO;
import com.casosemergencias.dao.CaseHistoryDAO;
import com.casosemergencias.dao.vo.CaseCommentVO;
import com.casosemergencias.dao.vo.CaseHistoryVO;
import com.casosemergencias.dao.vo.CaseVO;
import com.casosemergencias.logic.sf.response.CreateCaseResponse;
import com.casosemergencias.logic.sf.rest.CreateCase;
import com.casosemergencias.logic.sf.util.SalesforceLoginChecker;
import com.casosemergencias.model.CaseComment;
import com.casosemergencias.model.CaseHistory;
import com.casosemergencias.model.Caso;
import com.casosemergencias.model.User;
import com.casosemergencias.model.UserSessionInfo;
import com.casosemergencias.util.ParserModelVO;
import com.casosemergencias.util.datatables.DataTableProperties;

public class CaseServiceImpl implements CaseService{

	final static Logger logger = Logger.getLogger(CaseService.class);
	
	@Autowired
	private CaseDAO caseDao;
	
	@Autowired
	private CaseHistoryDAO caseHistoryDao;
	
	@Autowired
	private CaseCommentDAO caseCommentDao;
	
	@Autowired
	private SalesforceLoginChecker salesforceLoginChecker;
	
	/**
	 * Metodo que devuelve una lista con todos los Casos que hay en BBDD
	 * 
	 * @return
	 */
	@Override
	public List<Caso> readAllCase(DataTableProperties propDatatable) {
		
		logger.debug("--- Inicio -- readAllCase ---");
		
		List<Caso> listCaso = new ArrayList<>();
		List<CaseVO> listCasosVO = caseDao.readCaseDataTable(propDatatable);

		logger.debug("--- Inicio -- readAllCase casos en la lista: " + listCasosVO.size() + " ---");
		
		for (CaseVO casoVO : listCasosVO) {
			Caso caso = new Caso();
			ParserModelVO.parseDataModelVO(casoVO, caso);
			listCaso.add(caso);
		}
		
		logger.debug("--- Fin -- readAllCase ---");
		return listCaso;
	}
	
	/**
	 * Metodo que devuelve caso de BBDD por su sfid
	 * 
	 * @return
	 */
	@Override
	public Caso readCaseBySfid(String sfid){
		Caso returnCase = new Caso();
		CaseVO casoVO = caseDao.readCaseBySfid(sfid);
		if (casoVO != null){
			ParserModelVO.parseDataModelVO(casoVO, returnCase);
		}
		
		List<CaseHistoryVO> listaHistorialCasoVO = caseHistoryDao.readCaseHistoryByCaseId(sfid);
		List<CaseHistory> historialCasosRelacionado = parseaYPreparaListaHistorialCasos(listaHistorialCasoVO);
		returnCase.setHistorialCaso(historialCasosRelacionado);
		
		List<CaseCommentVO> listaComentarioCasoVO = caseCommentDao.readCaseCommentByCaseId(sfid);
		List<CaseComment> comentariosCasoRelacionado = parseaYPreparaListaComentariosCasos(listaComentarioCasoVO);
		returnCase.setCommentarioCaso(comentariosCasoRelacionado);
		
		return returnCase;
	}
	
	/**
	 * Metodo que devuelve caso de BBDD por su id
	 * 
	 * @return
	 */
	@Override
	public Caso readCaseById(Integer id){
		Caso returnCase = new Caso();
		CaseVO casoVO = caseDao.readCaseById(id);
		if (casoVO != null){
			ParserModelVO.parseDataModelVO(casoVO, returnCase);
		}
		return returnCase;
	}
	
	public Integer getNumCasos(DataTableProperties propDatatable){
		logger.debug("--- getNumCasos ---");
		return caseDao.getNumCasos(propDatatable);
	}
	
	public Caso insertCase(Caso caso) {
		logger.trace("--- Servicio insertCase iniciado ---");
		// 1. Leer usuario de fichero de propiedades
		InputStream propsInputStream = null;
		Properties properties = new Properties();
		String username = null;
		String password = null;
		String token = null;
		String userId = null;
		UserSessionInfo userSessionInfo = null;
		CreateCaseResponse respuestaCaso = null;
		CaseVO casoVO = null;
		
		try {
			propsInputStream = getClass().getClassLoader().getResourceAsStream("/environment/dev/config.properties");
			properties.load(propsInputStream);
			
			username = properties.getProperty("heroku.user");
			password = properties.getProperty("heroku.pass");
			token = properties.getProperty("heroku.token");
			userId = properties.getProperty("heroku.userid");
			
			if (username != null && !"".equals(username) 
					&& password != null && !"".equals(password) 
					&& token != null && !"".equals(token)) {
				
				userSessionInfo = salesforceLoginChecker.getUserSessionInfo(username, password, token);
				if (userSessionInfo != null) {
					caso.setPropietarioCaso(userId);
					respuestaCaso = CreateCase.createCaseInSalesforce(userSessionInfo, caso);
					if (respuestaCaso != null) {
						if ("0".equals(respuestaCaso.getControlErrores().getCodigoError())) {
							if (respuestaCaso.getIdCaso() != null && !"".equals(respuestaCaso.getIdCaso())) {
								caso.setSfid(respuestaCaso.getIdCaso());
								logger.info("Caso creado con sfid:" + respuestaCaso.getIdCaso());
							}
							
							if (respuestaCaso.getNumeroCaso() != null && !"".equals(respuestaCaso.getNumeroCaso())) {
								caso.setNumeroCaso(respuestaCaso.getNumeroCaso());
								logger.info("Caso creado con no. de caso: " + respuestaCaso.getNumeroCaso());
							}
							
							casoVO = new CaseVO();
							ParserModelVO.parseDataModelVO(caso, casoVO);
							Integer id = caseDao.insertCase(casoVO);
							if (id != null && id.intValue() > 0) {
								logger.info("Caso insertado correctamente en Heroku con id: " + id.intValue());
							} else {
								caso = null;
								logger.warn("No se ha podido insertar el caso en Heroku");
								//TODO: Qué hacer si falla la inserción en BBDD? Se subirá a Salesforce y acabará bajando a Heroku?
							}
						} else {
							caso = null;
							logger.error("Se ha producido un error al insertar el caso en SalesForce");
							logger.error("Código de error: " + respuestaCaso.getControlErrores().getCodigoError() + ". Mensaje: " + respuestaCaso.getControlErrores().getMensajeError());
						}
					} else {
						caso = null;
						logger.warn("No se ha podido insertar el caso en SalesForce");
						//TODO: Implementar excepciones propìas
					}
				}
			}
		} catch (IOException ioException) {
			logger.error(" Error cargando archivo de propiedades: " + ioException);
		} finally {
			if (propsInputStream != null) {
				try {
					propsInputStream.close();
				} catch (IOException ioException) {
					logger.error(" Error cargando archivo de propiedades: " + ioException);
				}
			}
		}

		logger.trace("--- Servicio insertCase completado ---");
		return caso;
	}

	@Override
	public Integer updateCase(Caso caso) {
		CaseVO casoVO = new CaseVO();
		casoVO = caseDao.readCaseBySfid(caso.getSfid());
		
		casoVO.setDescription(caso.getDescription());
		Integer id = caseDao.updateCase(casoVO);
		return id;
	}
	
	/*
	 * Método que parsea una lista de CaseHistoryVO en CaseHistory.
	 * Ademas prepara la lista con los datos listos para mostrar en pantalla:
	 * 		-- 1.- Si 'oldvalue' y 'newvalue' son sfid (no tiene longitud 18, sin espacios ni puntos), si es un sfid no se añade a la lista que devuelve el metodo
	 * 		-- 2.- Si 'labelOldValuePickList' y 'labelNewValuePickList' estan vacios, se almacena el valor de 'oldvalue' y 'newvalue'
	 * 		-- 3.- Si hay varios registros con la misma Fecha, hora y minutos borramos los datos de 'createdate', 'createdbyid' y 'userJoi'n de todos, 
	 * 				excepto el 1º registros
	 * */
	private List<CaseHistory> parseaYPreparaListaHistorialCasos(List<CaseHistoryVO> listaCaseHistoryVO) {
		if(listaCaseHistoryVO!=null && !listaCaseHistoryVO.isEmpty()){
			List<CaseHistory> retorno = new ArrayList<CaseHistory>();

			//def variables auxiliares para las comprobaciones del punto 2
			SimpleDateFormat  dateFormat  = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date fechaModif = null; //guardar 'createddate' que utilizamos para comparar con la iteracion (usaremos formato 'dateFormat')
			Date fechaIteracion; //guardar 'createddate' de cada iteracion
			
			
			for(CaseHistoryVO historiaCasoVO: listaCaseHistoryVO){
				CaseHistory casoRelacionado = new CaseHistory();
				ParserModelVO.parseDataModelVO(historiaCasoVO, casoRelacionado);
	
				//Comrobacion punto 1
				//Comprobamos si 'oldvalue' tiene un sfid
				String value = casoRelacionado.getOldvalue();
				if(value != null && !"".equals(value)){
					//comprobamos 'oldvalue' que no sea sfid, es decir, si tiene 18 caracteres y no contiene espacios ni puntos
					if(value.length()==18 && value.indexOf(" ") == -1 && value.indexOf(".") == -1){
						//pasamos a la siguiente iteracion del for
						continue;						
					}					
				}
				//Repetimos la comprobacion con 'newvalue'
				value = casoRelacionado.getNewvalue();
				if(value != null && !"".equals(value)){
					//comprobamos 'oldvalue' que no sea sfid, es decir, si tiene 18 caracteres y no contiene espacios ni puntos
					if(value.length()==18 && value.indexOf(" ") == -1 && value.indexOf(".") == -1){
						//pasamos a la siguiente iteracion del for
						continue;						
					}					
				}
				
				//Comprobacion punto 2		
				//'labelOldValuePickList' y 'labelNewValuePickList' no tienen valor, almacenamos oldvalue y newvalue en los campos.
				String oldValuePickList = casoRelacionado.getLabelOldValuePickList();
				if(oldValuePickList == null ||  "".equals(oldValuePickList)){
					if(casoRelacionado.getOldvalue() != null){						
						casoRelacionado.setLabelOldValuePickList(casoRelacionado.getOldvalue());
					}else{
						casoRelacionado.setLabelOldValuePickList("");
					}
				}
				
				String newValuePickList = casoRelacionado.getLabelNewValuePickList();
				if(newValuePickList == null ||  "".equals(newValuePickList)){
					if(casoRelacionado.getNewvalue() != null){
						casoRelacionado.setLabelNewValuePickList(casoRelacionado.getNewvalue());
					}else{
						casoRelacionado.setLabelNewValuePickList("");
					}
				}
				
				//Comprobacion punto 3		
				try {
					fechaIteracion = dateFormat.parse(casoRelacionado.getCreateddate().toString());
				
					if(fechaModif != null && fechaModif.equals(fechaIteracion)){
						casoRelacionado.setCreateddate(null);
						casoRelacionado.setCreatedbyid(null);
						casoRelacionado.setUserJoin(new User());
					}else{
						fechaModif = dateFormat.parse(casoRelacionado.getCreateddate().toString());
					}
				} catch (ParseException e) {
					logger.error("--- parseaYPreparaListaHistorialCasos -- error al parsear una fecha ---");
					logger.error(e.getMessage());					
				}
				
				
				retorno.add(casoRelacionado);
			}
			return retorno;
		}
		return null;
	}
	
	/*
	 * Método que parsea una lista de CaseHistoryVO en CaseComment.
	 * Ademas si 'createdate' coincide con 'lastmodifieddate', elimina el valor de 'lastmodifieddate' y 'lastmodifiedbyid', ya que el comentario no ha sido modificado
	 * */
	private List<CaseComment> parseaYPreparaListaComentariosCasos(List<CaseCommentVO> listaCaseCommentVO) {
		if(listaCaseCommentVO!=null && !listaCaseCommentVO.isEmpty()){
			
			List<CaseComment> retorno = new ArrayList<CaseComment>();
			
			SimpleDateFormat  dateFormat  = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//Definimos el formato para comparar 'createdate' con 'lastmodifieddate'
			
			for(CaseCommentVO comentarioCasoVO: listaCaseCommentVO){
				CaseComment casoRelacionado = new CaseComment();
				ParserModelVO.parseDataModelVO(comentarioCasoVO, casoRelacionado);
				
				try {
					
					Date fechaCreate = null;
					Date fechaModif = null;					
					if(casoRelacionado.getCreateddate() != null){
						fechaModif = dateFormat.parse(casoRelacionado.getCreateddate().toString());
					}
					if(casoRelacionado.getLastmodifieddate() != null){
						fechaCreate = dateFormat.parse(casoRelacionado.getLastmodifieddate().toString());
					}
				
					if(fechaCreate != null && fechaModif != null && fechaCreate.equals(fechaModif)){
						//El comentario ha sido insertado, no ha sido modificado
						casoRelacionado.setLastmodifieddate(null);
						casoRelacionado.setLastmodifiedbyid(null);
					}
				} catch (ParseException e) {
					logger.error("--- parseaYPreparaListaComentariosCasos -- error al parsear una fecha ---");
					logger.error(e.getMessage());					
				}
				
				retorno.add(casoRelacionado);
			}
			return retorno;
		}
		return null;
	}
}
