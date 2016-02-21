package br.com.DialiSys.Model;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Papel {


	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="PessoaFisica_id", unique=true)
	private PessoaFisica pessoaf;

	/**
	 * Construtor Sem Argumentos
	 */
	public Papel(){

	}
	
	public Papel(PessoaFisica pessoaf) {
		super();
		this.pessoaf = pessoaf;
	}

	public PessoaFisica getPessoaf() {
		return pessoaf;
	}

	public void setPessoaf(PessoaFisica pessoaf) {
		this.pessoaf = pessoaf;
	}	
}
