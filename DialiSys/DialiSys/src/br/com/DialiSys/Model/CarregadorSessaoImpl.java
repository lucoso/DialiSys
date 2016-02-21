/*package br.com.DialiSys.Model;


import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;



public class CarregadorSessaoImpl implements CarregadorSessao {



	public List<Sessao> BuscarSessaoCalculoMedia(long id_paciente, Date dataI, Date dataF) {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		String consulta = "select s from Sessao s where s.paciente.id = :id_pac and s.dataSessao between :dataInicial and :dataFinal)";
		TypedQuery<Sessao> query = em.createQuery(consulta, Sessao.class);
		query.setParameter("id_pac", id_paciente);
		query.setParameter("dataInicial", dataI);
		query.setParameter("dataFinal", dataF);
		List<Sessao> resultado = query.getResultList();

		return resultado;
	}

}
*/