package br.com.DialiSys.Manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.DialiSys.Model.CPF;
import br.com.DialiSys.Model.CRM;
import br.com.DialiSys.Model.Clinica;
import br.com.DialiSys.Model.Endereco;
import br.com.DialiSys.Model.Medico;
import br.com.DialiSys.Model.Paciente;
import br.com.DialiSys.Model.PessoaFisica;
import br.com.DialiSys.Util.JavaUtil;

public class MedicoManager {
	
	public static long idGerado = 0;
	
	public int CadastrarMedico(String nome, String tel, String email, String sexo, String datanasc,
			String numero_cpf, String data_emissao_cpf, String rua, String numero, String complemento, String bairro, 
			String cidade, String uf, String pais, String cep, JSONArray clinicas_ids, 
			String numero_crm, String data_emissao_crm){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		Medico m = new Medico();
		PessoaFisica pf = new PessoaFisica();
		List<PessoaFisica> pf2 = new ArrayList<PessoaFisica>();
		Endereco endereco = new Endereco();
		CPF cpf = new CPF();
		Set<Clinica> clinicas = new HashSet<Clinica>();
		Set<Long> cl_id = new HashSet<Long>();
		Clinica clinica = new Clinica();
		CRM crm = new CRM();
		Number c2 = null;
		int count = 0;
		boolean masc = true;
		boolean fem = false;
		
		
		try{
		for(int i = 0; i<clinicas_ids.length(); i++){
			
			cl_id.add(clinicas_ids.getLong(i));
			
	}
		}catch(JSONException jex){
			
			jex.printStackTrace();
		
		}
		
		try{
			String consulta = "select count(m) from Medico m where m.crm.numero = :numero";
			TypedQuery<Number> query = em.createQuery(consulta, Number.class);
			query.setParameter("numero", numero_crm);
			c2 = query.getSingleResult();
			
			String consulta2 = "select pf from PessoaFisica pf where pf.cpf.numero = :numero_cpf";
			TypedQuery<PessoaFisica> query2 = em.createQuery(consulta2, PessoaFisica.class);
			query2.setParameter("numero_cpf", numero_cpf);
			pf2 = query2.getResultList();
			
			Iterator<Long> itr = cl_id.iterator();
			
			for(int i=0; i<cl_id.size(); i++){
				try{
					while(itr.hasNext()){
					clinica = em.find(Clinica.class, itr.next());
					clinicas.add(clinica);
					
					}
				}catch(Exception ex){
					
					ex.printStackTrace();
					System.out.println("Erro ao fazer buscas no BD");
				}	
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao fazer Busca no BD!");
		}
			
		try{
			if(c2.intValue()<1){
				if(pf2.isEmpty()){
					
					Date datacrm = JavaUtil.ConvertStringToDate(data_emissao_crm);
					Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
					Date datan = JavaUtil.ConvertStringToDate(datanasc);
					
					if((sexo.equals("Masculino")) || (sexo.equals("masculino"))){
						
						pf.setSexo(masc);
					}
					if((sexo.equals("Feminino")) || (sexo.equals("feminino"))){
						
						pf.setSexo(fem);
					}
					
					if(cpf.validaCPF(numero_cpf) == true){
						
						cpf.setDataEmissao(datacpf);
						cpf.setNumero(numero_cpf);
					}else{
						
						System.out.println("CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!");
						return 400;
					}
					
					crm.setNumero(numero_crm);
					crm.setDataEmissao(datacrm);
					
					endereco.setRua(rua);
					endereco.setNumero(numero);
					endereco.setComplemento(complemento);
					endereco.setBairro(bairro);
					endereco.setCep(cep);
					endereco.setCidade(cidade);
					endereco.setUf(uf);
					endereco.setPais(pais);
					
					pf.setNome(nome);
					pf.setTel(tel);
					pf.setEndereco(endereco);
					pf.setEmail(email);
					pf.setCpf(cpf);
					pf.setDatanasc(datan);
					
					m.setPessoaf(pf);
					m.setClinicas(clinicas);
					m.setCrm(crm);
					
			}else{
				
				Iterator<PessoaFisica> itr = pf2.iterator();
				
				for(int i=0; i<pf2.size(); i++){
						while(itr.hasNext()){
						pf = itr.next();
						}
				}
				
				count++;
				
				Date datacrm = JavaUtil.ConvertStringToDate(data_emissao_crm);
				Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
				Date datan = JavaUtil.ConvertStringToDate(datanasc);
				
				if((sexo.equals("Masculino")) || (sexo.equals("masculino"))){
					
					pf.setSexo(masc);
				}
				if((sexo.equals("Feminino")) || (sexo.equals("feminino"))){
					
					pf.setSexo(fem);
				}
				
				if(cpf.validaCPF(numero_cpf) == true){
					
					cpf.setDataEmissao(datacpf);
					cpf.setNumero(numero_cpf);
				}else{
					
					System.out.println("CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!");
					return 400;
				}
				
				crm.setNumero(numero_crm);
				crm.setDataEmissao(datacrm);
				
				endereco.setRua(rua);
				endereco.setNumero(numero);
				endereco.setComplemento(complemento);
				endereco.setBairro(bairro);
				endereco.setCep(cep);
				endereco.setCidade(cidade);
				endereco.setUf(uf);
				endereco.setPais(pais);
				
				pf.setNome(nome);
				pf.setTel(tel);
				pf.setEndereco(endereco);
				pf.setEmail(email);
				pf.setCpf(cpf);
				pf.setDatanasc(datan);
				
				m.setPessoaf(pf);
				m.setClinicas(clinicas);
				m.setCrm(crm);
			}
							
			}else{
	
				System.out.println("Conflito! Erro 409 - Medico ja EXISTE!");
				return 409;	
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao setar os dados!");
		}

		try{
			
			if(count == 0){

			em.getTransaction().begin();
			em.persist(pf);
			idGerado = pf.getId();
			em.persist(m);
			em.getTransaction().commit();
			}
			
			if(count == 1){
				
				em.getTransaction().begin();
				em.merge(pf);
				idGerado = pf.getId();
				em.persist(m);
				em.getTransaction().commit();
				
			}

		}catch (Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Novo Medico Cadastrado Com Sucesso!!!");
		return 201;
	}
	

	/**
	 * ESTA AQUI SOMENTE POR PRECAUÇÃO...SE PRECISAR...O METODO QUE ESTA FUNCIONANDO É O DE BAIXO
	 * ESTE RETORNA TUDO DO MEDICO...INCLUSIVE SUAS CLINICAS COM TODOS OS SEUS ATRIBUTOS 
	 * @param 
	 * @return
	 */
	/*public List<Medico> BuscarTodosMedicos(){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		String consulta = "select m from Medico m";
		TypedQuery<Medico> query = em.createQuery(consulta, Medico.class);
		List<Medico> resultado = query.getResultList();
		return resultado;	
	}*/
	
	public JSONArray BuscarTodosMedicos() throws JSONException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		JSONArray objRetornado = new JSONArray();
	
		String consulta = "select m.pessoaf.id, m.pessoaf.nome, m.pessoaf.tel, m.pessoaf.endereco, m.pessoaf.email,"
						+ "m.pessoaf.cpf, m.pessoaf.sexo, m.pessoaf.datanasc, m.crm from Medico m";
		
		Query query = em.createQuery(consulta);
		List<Object[]> resultado = (List<Object[]>) query.getResultList();
				
		objRetornado.put(resultado);
			
		return objRetornado;	
	}
	
	public List<String> BuscarClinicasDosMedicos(long pessoa_id){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		List<String> nomes = new ArrayList<String>();
		Number n;
		
		try{
			
			String consulta = "select count(m) from Medico m where m.pessoaf.id = :pessoa_id";
			TypedQuery<Number> query = em.createQuery(consulta, Number.class);
			query.setParameter("pessoa_id", pessoa_id);
			n = query.getSingleResult();
			
			if(n.intValue()<1){
				
				System.out.println("Médico NÃO encontrado! Não é possivel fazer a busca!");
				return null;
			}else{
			
				String consulta2 = "select c.nome from Medico m join m.clinicas c where m.pessoaf.id = :pessoa_id";
				TypedQuery<String> query2 = em.createQuery(consulta2, String.class);
				query2.setParameter("pessoa_id", pessoa_id);
				nomes = query2.getResultList();
				
				if(nomes.isEmpty()){
					
					System.out.println("NÃO foi encontrado nenhuma Clinica Para este Médico");
					return null;
				}
			}
		}catch(Exception ex){
				ex.printStackTrace();
				System.out.println("Erro ao fazer busca no BD");
			}
			
			System.out.println("Clinicas Do Medico Encontrado com Sucesso");
			return nomes;
	}

	public Medico BuscarMedicoPorID(long id){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		TypedQuery<Medico> query = em.createNamedQuery(Medico.BuscarPorID, Medico.class);
		query.setParameter("id", id);
		Medico resultado = query.getSingleResult();
		return resultado;	
	}
	
	public JSONObject BuscarMedicoPorPessoa(long pessoa_id) throws JSONException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		JSONObject objRetornado = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
	
		String consulta = "select m.pessoaf.id, m.pessoaf.nome, m.pessoaf.tel, m.pessoaf.endereco, m.pessoaf.email,"
						+ "m.pessoaf.cpf, m.pessoaf.sexo, m.pessoaf.datanasc, m.crm from Medico m where m.pessoaf.id = :id";
		
		Query query = em.createQuery(consulta);
		query.setParameter("id", pessoa_id);
		Object[] resultado = (Object[]) query.getSingleResult();
		
		String consulta2 = "select c.id from Medico m join m.clinicas c where m.pessoaf.id = :id";
		
		Query query2 = em.createQuery(consulta2);
		query2.setParameter("id", pessoa_id);
		Object resultado2 = (Object) query2.getResultList();
				
		for(int i=0;i<resultado.length;i++){
			obj1.putOpt("ID", resultado[0]);
			obj1.putOpt("Nome", resultado[1]);
			obj1.putOpt("Telefone", resultado[2]);
			obj1.putOpt("Endereço", resultado[3]);
			obj1.putOpt("Email", resultado[4]);
			obj1.putOpt("CPF", resultado[5]);
			obj1.putOpt("Sexo", resultado[6]);
			obj1.putOpt("Data de Nascimento", resultado[7]);
			obj1.putOpt("CRM", resultado[8]);
			
		}
		
		obj2.put("Clinica_ID", resultado2);
		
		objRetornado.put("PessoaFisica", obj1);
		objRetornado.put("Clinicas", obj2);
		
		return objRetornado;	
	}
		

	public JSONObject BuscarMedicoPorCRM(String crm) throws JSONException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		JSONObject objRetornado = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
	
		String consulta = "select m.pessoaf.id, m.pessoaf.nome, m.pessoaf.tel, m.pessoaf.endereco, m.pessoaf.email,"
						+ "m.pessoaf.cpf, m.pessoaf.sexo, m.pessoaf.datanasc, m.crm from Medico m where m.crm.numero = :numero_crm";
		
		Query query = em.createQuery(consulta);
		query.setParameter("numero_crm", crm);
		Object[] resultado = (Object[]) query.getSingleResult();
		
		String consulta2 = "select c.id from Medico m join m.clinicas c where m.crm.numero = :numero_crm";
		
		Query query2 = em.createQuery(consulta2);
		query2.setParameter("numero_crm", crm);
		Object resultado2 = (Object) query2.getResultList();
				
		for(int i=0;i<resultado.length;i++){
			obj1.putOpt("ID", resultado[0]);
			obj1.putOpt("Nome", resultado[1]);
			obj1.putOpt("Telefone", resultado[2]);
			obj1.putOpt("Endereço", resultado[3]);
			obj1.putOpt("Email", resultado[4]);
			obj1.putOpt("CPF", resultado[5]);
			obj1.putOpt("Sexo", resultado[6]);
			obj1.putOpt("Data de Nascimento", resultado[7]);
			obj1.putOpt("CRM", resultado[8]);
			
		}
		
		obj2.put("Clinica_ID", resultado2);
		
		objRetornado.put("PessoaFisica", obj1);
		objRetornado.put("Clinicas", obj2);
		
		return objRetornado;
	}
	
	
	public List<String> BuscarPacientesPorMedico(long id){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		Medico m;
		String nome = null;
		List<String> resultado = null;
		List<Medico> m_list = new ArrayList<Medico>();
		
		try{
			
			String consulta = "select m from Medico m where m.pessoaf.id = :pessoa_id";
			TypedQuery<Medico> query = em.createQuery(consulta, Medico.class);
			query.setParameter("pessoa_id", id);
			m_list = query.getResultList();
			
			if(m_list.isEmpty()){
				
				System.out.println("Medico NÃO encontrado! Não é possivel fazer a busca!");
				return null;
			}else{
				
				Iterator<Medico> itr = m_list.iterator();
				for(int i=0; i<m_list.size(); i++){
					while(itr.hasNext()){
						m = itr.next();
						nome = m.getPessoaf().getNome();
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao buscar o Medico!");
		}
		
		try{
			
			TypedQuery<String> query = em.createNamedQuery(Paciente.BuscarPorMedico, String.class);
			query.setParameter("nome_medico", nome);
		    resultado = query.getResultList();
		
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao buscar os Pacientes");
		}
		
		return resultado;
	}

	
	public int AtualizarMedico(String pessoa_id, String nome, String tel, String email, String sexo, String datanasc,
			String numero_cpf, String data_emissao_cpf, String rua, String numero, String complemento, String bairro, 
			String cidade, String uf, String pais, String cep, JSONArray clinicas_ids, 
			String numero_crm, String data_emissao_crm){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		Medico m = new Medico();
		Endereco endereco = new Endereco();
		CPF cpf = new CPF();
		List<Medico> listaM = new ArrayList<Medico>();
		PessoaFisica pf = new PessoaFisica();
		Set<Clinica> clinicas = new HashSet<Clinica>();
		Set<Long> cl_id = new HashSet<Long>();
		Clinica clinica = new Clinica();
		CRM crm = new CRM();
		Number n = null;
		boolean masc = true;
		boolean fem = false;
		long p_id = JavaUtil.ConvertStringToLong(pessoa_id);
		
		try{
			
			String consulta = "select m from Medico m where m.pessoaf.id = :pessoa_id";
			TypedQuery<Medico> query = em.createQuery(consulta, Medico.class);
			query.setParameter("pessoa_id", p_id);
			listaM = query.getResultList();
			
			String consulta2 = "select count(m) from Medico m where m.pessoaf.id = :pessoa_id";
			TypedQuery<Number> query2 = em.createQuery(consulta2, Number.class);
			query2.setParameter("pessoa_id", p_id);
			n = query2.getSingleResult();
			
			if(listaM.isEmpty()){
				
				System.out.println("Medico NÃO Encontrado! Não é possivel Atualizar os Dados!");
				return 404;
			
			}else{
				
				Iterator<Medico> itr = listaM.iterator();
				
				for(int i = 0; i<listaM.size(); i++){
					try{
					while(itr.hasNext()){
						m = em.find(Medico.class, itr.next().getId());
				}
					}catch(Exception ex){
						
						ex.printStackTrace();
						String msgErro = "Status: 500 - Erro ao fazer busca do Médico no BD!";
						System.out.println(msgErro);
					}
				}
			}
				if(n.intValue()<1){
					
					System.out.println("PessoaFisica Não está Cadastrado no BD!! "
							+ "Cadastre antes de atualizar");
					return 404;
					
				}else{
					
					pf = m.getPessoaf();

				}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao buscar Medico e/ou pessoaFisica no BD");
		}
		
		try{
		for(int i = 0; i<clinicas_ids.length(); i++){
			
			cl_id.add(clinicas_ids.getLong(i));
			
	}
		}catch(JSONException jex){
			
			jex.printStackTrace();
		}
		
			Iterator<Long> itr2 = cl_id.iterator();
			
			for(int i=0; i<cl_id.size(); i++){
				try{
					while(itr2.hasNext()){
					clinica = em.find(Clinica.class, itr2.next());
					clinicas.add(clinica);
					
					}
				}catch(Exception ex){
					
					ex.printStackTrace();
					System.out.println("Erro ao buscar as Clinicas do BD");
				}	
			}
			
		try{
					
					Date datac = JavaUtil.ConvertStringToDate(data_emissao_crm);
					Date datan = JavaUtil.ConvertStringToDate(datanasc);
					Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
					
					if((sexo.equals("Masculino")) || (sexo.equals("masculino"))){
						
						pf.setSexo(masc);
					}
					if((sexo.equals("Feminino")) || (sexo.equals("feminino"))){
						
						pf.setSexo(fem);
					}
					
					if(cpf.validaCPF(numero_cpf) == true){
						
						cpf.setDataEmissao(datacpf);
						cpf.setNumero(numero_cpf);
					}else{
						
						System.out.println("CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!");
						return 400;
					}
					
					crm.setNumero(numero_crm);
					crm.setDataEmissao(datac);
					
					endereco.setRua(rua);
					endereco.setNumero(numero);
					endereco.setComplemento(complemento);
					endereco.setBairro(bairro);
					endereco.setCep(cep);
					endereco.setCidade(cidade);
					endereco.setUf(uf);
					endereco.setPais(pais);
					
					pf.setNome(nome);
					pf.setTel(tel);
					pf.setEndereco(endereco);
					pf.setEmail(email);
					pf.setCpf(cpf);
					pf.setDatanasc(datan);
					
					m.setPessoaf(pf);
					m.setClinicas(clinicas);
					m.setCrm(crm);			
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao setar dados!");
		}
		

		try{
			em.getTransaction().begin();
			em.merge(m);
			em.getTransaction().commit();

		}catch(Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		}finally{
			
			em.close();
			factory.close();
		}

		String msg = "Medico Atualizado Com Sucesso!!!";
		System.out.println(msg);
		return 200;

	}

	
	public int RemoverMeidco(long id_pessoa){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Medico m = null;
		List<Medico> med = null;
		PessoaFisica pf = null;
		int count = 0;
		
		try { 

			String consulta = "select m from Medico m where m.pessoaf.id = :id_pessoa";
			TypedQuery<Medico> query = em.createQuery(consulta, Medico.class);
			query.setParameter("id_pessoa", id_pessoa);
			med = query.getResultList();
			
			if(med.isEmpty()){
				
				String msgErro = "Status: 404 Not Found - Medico NÃO Encontrado no BD!";
				System.out.println(msgErro);
				return 404;
				
			}else{
				
				Iterator<Medico> itr = med.iterator();
				
				for(int i = 0; i<med.size(); i++){
					try{
						while(itr.hasNext()){
						m = em.find(Medico.class, itr.next().getId());
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao buscar o Medico");
		}
		
		try{
			
			String consulta = "select p.pessoaf.id from Paciente p where p.pessoaf.id = :id_pessoa";
			TypedQuery<Long> query = em.createQuery(consulta, Long.class);
			query.setParameter("id_pessoa", id_pessoa);
			List<Long> pac_list = query.getResultList();
			
			String consulta2 = "select u.pessoaf.id from Usuario u where u.pessoaf.id = :id_pessoa";
			TypedQuery<Long> query2 = em.createQuery(consulta2, Long.class);
			query2.setParameter("id_pessoa", id_pessoa);
			List<Long> user_list = query2.getResultList();
			
			String consulta3 = "select count(pf) from PessoaFisica pf where pf.id = :id_pessoa";
			TypedQuery<Number> query3 = em.createQuery(consulta3, Number.class);
			query3.setParameter("id_pessoa", id_pessoa);
			Number numero_pf = query3.getSingleResult();
			
			if((pac_list.isEmpty()) && (user_list.isEmpty()) && (numero_pf.intValue()<=1)){
				
					try{
						
						pf = em.find(PessoaFisica.class, id_pessoa);
						count = 1;
						
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("Erro ao buscar a Pessoa Fisica!");
					}
				
			}else{
				
				count = 2;
			}
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao buscar ID da Pessoa Fisica!");
				
			}
		
		try{
			if(count == 1){
			
			em.getTransaction().begin();
			em.remove(m);
			em.remove(pf);
			em.getTransaction().commit();
			}
			
			if(count == 2){
				
				em.getTransaction().begin();
				em.remove(m);
				em.getTransaction().commit();
			}
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			em.getTransaction().rollback();
		
		}finally{
			
			em.close();
			factory.close();
		}

		String msg = "Medico Removido Com Sucesso!!!";
		System.out.println(msg);
		return 200;
	}
}

