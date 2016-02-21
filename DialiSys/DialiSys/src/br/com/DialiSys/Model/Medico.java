package br.com.DialiSys.Model;


import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@NamedQueries({
	@NamedQuery(name = Medico.BuscarPorID, query= "select m from Medico m where m.id = :id"),
	@NamedQuery(name = Medico.BuscarPorCRM, query= "select m from Medico m where m.crm.numero = :numero_crm"),
})
@XmlRootElement
public class Medico extends Profissional {

	public static final String BuscarPorID = "Medico.BuscarPorID";
	public static final String BuscarPorCRM = "Medico.BuscarPorCRM";

	@Embedded
	private CRM crm;

	public Medico(){

	}

	public Medico(PessoaFisica pessoaf, long id, Set<Clinica> clinicas, CRM crm) {
		super(pessoaf, id, clinicas);
		this.crm = crm;
	}


	public CRM getCrm() {
		return crm;
	}

	public void setCrm(CRM crm) {
		this.crm = crm;
	}
}