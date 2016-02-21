package br.com.DialiSys.Model;


import java.util.Set;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@NamedQueries({
	@NamedQuery(name = Enfermeira.BuscarPorID, query= "select e from Enfermeira e where e.id = :id"),
	@NamedQuery(name = Enfermeira.BuscarPorCOREN, query= "select e from Enfermeira e where e.coren.numero = :numero_coren"),
	})
public class Enfermeira extends Profissional {

	public static final String BuscarPorID = "Enfermeira.BuscarPorID";
	public static final String BuscarPorCOREN = "Enfermeira.BuscarPorCOREN";
	

	@Embedded
	private COREN coren;

	/**
	 * Construtor Sem Argumentos
	 */
	public Enfermeira(){

	}

	public Enfermeira(PessoaFisica pessoaf, long id, Set<Clinica> clinicas, COREN coren) {
		super(pessoaf, id, clinicas);
		this.coren = coren;
	}


	public COREN getCoren() {
		return coren;
	}

	public void setCoren(COREN coren) {
		this.coren = coren;
	}
	
	
}