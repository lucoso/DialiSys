package br.com.DialiSys.Model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class COREN {

	@Column(name="Enfermeira_COREN", length=17, unique=true, nullable=true)
	private String numero;
	@Column(name="Enfermeira_COREN_DataEmissao", nullable=true)
	@Temporal(value=TemporalType.DATE)
	private Date dataEmissao;

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Date getDataEmissao() {
		return dataEmissao;
	}
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
}
