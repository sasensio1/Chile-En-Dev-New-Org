package com.casosemergencias.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.casosemergencias.dao.CaseDAO;
import com.casosemergencias.dao.CasosReiteradosDAO;
import com.casosemergencias.dao.RelacionActivoContactoDAO;
import com.casosemergencias.dao.SuministroDAO;
import com.casosemergencias.dao.vo.CaseVO;
import com.casosemergencias.dao.vo.CasosReiteradosVO;
import com.casosemergencias.dao.vo.RelacionActivoContactoVO;
import com.casosemergencias.dao.vo.SuministroVO;
import com.casosemergencias.model.Caso;
import com.casosemergencias.model.Contacto;
import com.casosemergencias.model.Suministro;
import com.casosemergencias.util.ParserModelVO;
import com.casosemergencias.util.datatables.DataTableProperties;


public class SuministroServiceImpl implements SuministroService{

final static Logger logger = Logger.getLogger(SuministroService.class);
	
	@Autowired
	private SuministroDAO suministroDao;
	
	@Autowired
	private CaseDAO casoDAO;
	
	@Autowired
	private RelacionActivoContactoDAO relacionDAO;
	
	@Autowired
	private CasosReiteradosDAO casoReiteradosDAO;
	
	@Override
	public List<Suministro> readAllSuministros() {
		
		logger.debug("--- Inicio -- readAllSuministros ---");
		
		List<Suministro> listSuministro = new ArrayList<>();
		
		List<SuministroVO> listSuministroVO = suministroDao.readAllSuministro();
		logger.debug("--- Inicio -- readAllSuministros cantidad : " + listSuministroVO.size() + " ---");
		
		for(SuministroVO suministroVO : listSuministroVO){
			Suministro suministro = new Suministro();
			ParserModelVO.parseDataModelVO(suministroVO, suministro);
			listSuministro.add(suministro);
			
		}
		
		logger.debug("--- Fin -- readAllSuministros ---:"+listSuministro.size());
		
		return listSuministro;
	}
	
	/**
	 * Metodo que devuelve una lista con todos los suministros que hay en BBDD
	 * 
	 * @return
	 */
	@Override
	public List<Suministro> readAllSuministros(DataTableProperties propDatatable) {
		
		logger.debug("--- Inicio -- readAllSuministros ---");
		List<Suministro> listSuministro = new ArrayList<Suministro>();
		
		List<SuministroVO> listSuministroVO = suministroDao.readSuministroDataTable(propDatatable);
		logger.debug("--- Inicio -- readAllSuministros cantidad: " + listSuministroVO.size() + " ---");
		
		for (SuministroVO suministroVO : listSuministroVO) {
			Suministro suministro = new Suministro();
			ParserModelVO.parseDataModelVO(suministroVO, suministro);
			listSuministro.add(suministro);
		}
		
		logger.debug("--- Fin -- readAllSuministros ---:"+listSuministro.size());
		return listSuministro;
	}
	
	public Suministro readSuministroBySfid(String sfid){
		SuministroVO suministroVO = suministroDao.readSuministroBySfid(sfid);
		Suministro suministro = new Suministro();
		//Si nos devuelve null, devolvemos null, si no, devolvemos el objeto relleno con los datos que nos devuelve BBDD
		if(suministroVO!=null){
			ParserModelVO.parseDataModelVO(suministroVO, suministro);
			List<CaseVO> listacasosVO = casoDAO.readCaseOfSuministro(sfid);
			List<Caso> casoRelacionado = parseaListaCasos(listacasosVO);
			suministro.setCasos(casoRelacionado);
			List<RelacionActivoContactoVO> listaRelacionVO = relacionDAO.getContactosRelacionadosPorSuministro(sfid);
			List<Contacto> contactosRelacionado = parseaListaContactosRel(listaRelacionVO);
			suministro.setContactosRelacionados(contactosRelacionado);
			
			//Calculamos si el suministro tiene casosReiterados 
			CasosReiteradosVO casosReiteradosVO = casoReiteradosDAO.readCasosReiteradosByName("Suministro");
			int numCasos = 0;
			int limiteDias = casosReiteradosVO.getNumDias().intValue();
			int numCasosReit = casosReiteradosVO.getNumCasos().intValue();
			
			Calendar calendar = Calendar.getInstance();	
			//Definimos el formato para comparar 'fechaApertura' con la fecha actual
			SimpleDateFormat  dateFormat  = new SimpleDateFormat("dd-MM-yyyy");
			
			
			//Se puede añadir dentro del for el calculo de casos abiertos
			for(Caso caso : casoRelacionado){
				try {
					Date dateCreadionCaso = caso.getFechaApertura();
					Date dateHoy = new Date();
					//Fecha apertura del caso le sumamos 'limiteDias'
					calendar.setTime(dateCreadionCaso);
					calendar.add(Calendar.DAY_OF_YEAR, limiteDias);
						
					String stringDate = dateFormat.format(calendar.getTime());
					dateCreadionCaso = dateFormat.parse(stringDate);						
					stringDate = dateFormat.format(dateHoy);
					dateHoy = dateFormat.parse(stringDate);
					
					if(dateCreadionCaso.getTime() > dateHoy.getTime()){
						numCasos ++;
					}
					
				} catch (ParseException e) {
					logger.error("--- readSuministroBySfid -- error al parsear una fecha ---");
					logger.error(e.getMessage());
				}

			}	
			
			if(numCasos >= numCasosReit){
				suministro.setCasosReiterados((double) numCasos);
			}
			
			return suministro;
		}
		return null;
	}
	
	private List<Caso> parseaListaCasos(List<CaseVO> listacasosVO) {
		if(listacasosVO!=null && !listacasosVO.isEmpty()){
			List<Caso> retorno = new ArrayList<Caso>();
			for(CaseVO casoVO: listacasosVO){
				Caso casoRelacionado = new Caso();
				ParserModelVO.parseDataModelVO(casoVO, casoRelacionado);
				retorno.add(casoRelacionado);
			}
			return retorno;
		}
		return null;
	}
	private List<Contacto> parseaListaContactosRel(List<RelacionActivoContactoVO> listaRelacionVO) {
		if(listaRelacionVO!=null && !listaRelacionVO.isEmpty()){
			List<Contacto> retorno = new ArrayList<Contacto>();
			for(RelacionActivoContactoVO relacion: listaRelacionVO){
				if(relacion.getContacto()!=null ){
					Contacto contactoRelacionado = new Contacto();
					ParserModelVO.parseDataModelVO(relacion.getContacto(), contactoRelacionado);
					if(relacion.getTipoRelacionActivo()!=null)
						contactoRelacionado.setRelacionActivo(relacion.getTipoRelacionActivo().getValor());
					contactoRelacionado.setPrincipal(relacion.getPrincipal());
					retorno.add(contactoRelacionado);
				}
			}
			return retorno;
		}
		return null;
	}

	public Integer getNumSuministros(DataTableProperties propDatatable){
		return suministroDao.countSuministro(propDatatable);
	}

}
