package br.com.DialiSys.Model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Embeddable
public class PressaoArterial {

	@Transient
	private BigDecimal sistolica;
	
	@Transient
	private BigDecimal diastolica;
	
	@Column(name="Sessao_Paciente_Pressao", length=50, nullable=false)
	private String pressao; 
	
	@Column(name="Pressao_Hora", nullable=true)
	@Temporal(value=TemporalType.TIME)
	private Date hora; 

	/**
	 * Construtor Sem Argumentos
	 */
	public PressaoArterial(){
		
	}
	
	public PressaoArterial(BigDecimal sistolica, BigDecimal diastolica,
			String pressao, Date hora) {
		super();
		this.sistolica = sistolica;
		this.diastolica = diastolica;
		this.pressao = pressao;
		this.hora = hora;
	}
	
	public String getPressao() {
		return pressao;
	}
	public void setPressao(String pressao){
		this.pressao = pressao;
	}
	public Date getHora() {
		return hora;
	}
	public void setHora(Date hora) {
		this.hora = hora;
	}
	public BigDecimal getSistolica() {
		return sistolica;
	}
	public void setSistolica(BigDecimal sistolica) {
		this.sistolica = sistolica;
	}
	public BigDecimal getDiastolica() {
		return diastolica;
	}
	public void setDiastolica(BigDecimal diastolica) {
		this.diastolica = diastolica;
	}

	public String MontarPressao(){

		String pa = this.sistolica + "/" + this.diastolica;

		this.setPressao(pa);

		return pressao;
	}



}
