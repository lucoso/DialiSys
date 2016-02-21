package br.com.DialiSys.Model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
public abstract class PessoaJuridica extends Pessoa {
	
	@Embedded
	private CNPJ cnpj;
	
	@Column(name="Empresa_Site", length=50, nullable=true)
	private String site;
	
	/**
	 * Construtor Sem Argumentos
	 */
	public PessoaJuridica (){
		
	}

	public PessoaJuridica(long id, String nome, String tel, String email,
			Endereco endereco, CNPJ cnpj, String site) {
		super(id, nome, tel, email, endereco);
		this.cnpj = cnpj;
		this.site = site;
	}

	public CNPJ getCnpj() {
		return cnpj;
	}

	public void setCnpj(CNPJ cnpj) {
		this.cnpj = cnpj;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
}
