package com.casosemergencias.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.casosemergencias.dao.DireccionDAO;
import com.casosemergencias.dao.vo.DireccionVO;
import com.casosemergencias.model.Direccion;
import com.casosemergencias.util.ParserModelVO;


//las transacciones se abren y cierran aqui
public class DireccionServiceImpl implements DireccionService{
	
	final static Logger logger = Logger.getLogger(DireccionService.class);
	
	@Autowired
	private DireccionDAO direccionDao;
		
	/**
	 * Metodo que devuelve una lista de todas las direccions a mostrar en la tabla de nuestra app.
	 * @return listofalldireccions
	 */
	@Override
	public List<Direccion> listOfDireccionesTable() {
		
		List<Direccion> listOfDireccionesTable= new ArrayList<Direccion>();
		List<DireccionVO> listOfAllDirecciones =new ArrayList<DireccionVO>();
		listOfAllDirecciones=direccionDao.readAllDireccion();
		Direccion direccion= null;
		
		
		for(DireccionVO dir:listOfAllDirecciones){
			
			direccion = new Direccion();
			
			ParserModelVO.parseDataModelVO(dir, direccion);
		
			listOfDireccionesTable.add(direccion);
					
		}		
		return listOfDireccionesTable;		
	}
	
	@Override
	public Direccion readDireccionBySfid(String sfid){
		Direccion returnDireccion = new Direccion();
		DireccionVO direccionVO = direccionDao.readDireccionBySfid(sfid);
		if (direccionVO != null){
			ParserModelVO.parseDataModelVO(direccionVO, returnDireccion);
		}
		return returnDireccion;
	}




}