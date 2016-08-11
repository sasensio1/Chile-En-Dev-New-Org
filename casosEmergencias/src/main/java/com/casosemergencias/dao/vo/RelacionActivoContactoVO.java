package com.casosemergencias.dao.vo;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.WhereJoinTable;

@Entity
@Table(name="salesforce.relacion_activo_contacto__c")
public class RelacionActivoContactoVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//campo de Heroku
	//Used to track the IsDeleted field from Salesforce allowing Heroku Connect to handle deletes when polling for updates
	@Column(name = "isdeleted")
	private Boolean isDeleted;
	//campo de Heroku
	//Used to track the IsDeleted field from Salesforce allowing Heroku Connect to handle deletes when polling for updates
	@Column(name = "systemmodstamp")
	private Date systemDate;
	//campo de Heroku
	@Column(name = "_hc_lastop")
	private String hcLastop;
	//campo de Heroku
	@Column(name = "_hc_err")
	private String hcError;
	//campo de Heroku
	@Column(name="createddate")
	private Date createdDate;
	

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "sfid")
	private String sfid;

	@Column(name = "name")
	private String name;
	
	@Column(name = "contacto__c")
	private String contactoId;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tipo_de_relaci_n__c", referencedColumnName="codigo", insertable = false, updatable=false)
	@WhereJoinTable(clause = "campo ='Tipo_de_Relaci_n__c' and objeto='relacion_activo_contacto__c'")
	private PickListsRelacionActivoVO tipoRelacionActivo;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="activo__c", referencedColumnName="sfid", insertable = false, updatable=false)
	private AssetVO activo;

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getSystemDate() {
		return systemDate;
	}

	public void setSystemDate(Date systemDate) {
		this.systemDate = systemDate;
	}

	public String getHcLastop() {
		return hcLastop;
	}

	public void setHcLastop(String hcLastop) {
		this.hcLastop = hcLastop;
	}

	public String getHcError() {
		return hcError;
	}

	public void setHcError(String hcError) {
		this.hcError = hcError;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSfid() {
		return sfid;
	}

	public void setSfid(String sfid) {
		this.sfid = sfid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactoId() {
		return contactoId;
	}

	public void setContactoId(String contactoId) {
		this.contactoId = contactoId;
	}

	public PickListsRelacionActivoVO getTipoRelacionActivo() {
		return tipoRelacionActivo;
	}

	public void setTipoRelacionActivo(PickListsRelacionActivoVO tipoRelacionActivo) {
		this.tipoRelacionActivo = tipoRelacionActivo;
	}

	public AssetVO getActivo() {
		return activo;
	}

	public void setActivo(AssetVO activo) {
		this.activo = activo;
	}
	

}