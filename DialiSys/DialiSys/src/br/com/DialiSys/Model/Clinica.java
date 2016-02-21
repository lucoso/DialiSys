package br.com.DialiSys.Model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@NamedQueries({
	@NamedQuery(name = Clinica.BuscarPorID, query= "select c from Clinica c where c.id = :id"),
	@NamedQuery(name = Clinica.BuscarPorCNPJ, query= "select c from Clinica c where c.cnpj.numero = :numero_cnpj")
})
public class Clinica extends PessoaJuridica {

	public static final String BuscarPorID = "Clinica.BuscarPorID";
	public static final String BuscarPorCNPJ = "Clinica.BuscarPorCNPJ";

	
	public Clinica (){
		
	}

	public Clinica(long id, String nome, String tel, String email,
			Endereco endereco, CNPJ cnpj, String site) {
		super(id, nome, tel, email, endereco, cnpj, site);
	}

}