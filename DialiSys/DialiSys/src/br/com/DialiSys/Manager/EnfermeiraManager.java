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

import br.com.DialiSys.Model.COREN;
import br.com.DialiSys.Model.CPF;
import br.com.DialiSys.Model.Clinica;
import br.com.DialiSys.Model.Endereco;
import br.com.DialiSys.Model.Enfermeira;
import br.com.DialiSys.Model.Paciente;
import br.com.DialiSys.Model.PessoaFisica;
import br.com.DialiSys.Util.JavaUtil;

public class EnfermeiraManager {
	
	public static long idGerado = 0;
	
	public int CadastrarEnfermeira(String nome, String tel, String email, String sexo, String datanasc,
			String numero_cpf, String data_emissao_cpf, String rua, String numero, String complemento, String bairro, 
			String cidade, String uf, String pais, String cep, JSONArray clinicas_ids, 
			String numero_coren, String data_emissao_coren){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		Enfermeira e = new Enfermeira();
		PessoaFisica pf = new PessoaFisica();
		List<PessoaFisica> pf2 = new ArrayList<PessoaFisica>();
		Endereco endereco = new Endereco();
		CPF cpf = new CPF();
		Set<Clinica> clinicas = new HashSet<Clinica>();
		Set<Long> cl_id = new HashSet<Long>();
		Clinica clinica = new Clinica();
		COREN coren = new COREN();
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
			String consulta = "select count(e) from Enfermeira e where e.coren.numero = :numero";
			TypedQuery<Number> query = em.createQuery(consulta, Number.class);
			query.setParameter("numero", numero_coren);
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
					
					Date datacoren = JavaUtil.ConvertStringToDate(data_emissao_coren);
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
					
					coren.setNumero(numero_coren);
					coren.setDataEmissao(datacoren);
					
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
					
					e.setPessoaf(pf);
					e.setClinicas(clinicas);
					e.setCoren(coren);
					
					count = 1;
					
			}else{
				
				Iterator<PessoaFisica> itr = pf2.iterator();
				
				for(int i=0; i<pf2.size(); i++){
						while(itr.hasNext()){
						pf = itr.next();
						}
				}
				
				Date datacoren = JavaUtil.ConvertStringToDate(data_emissao_coren);
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
				
				coren.setNumero(numero_coren);
				coren.setDataEmissao(datacoren);
				
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
				
				e.setPessoaf(pf);
				e.setClinicas(clinicas);
				e.setCoren(coren);
				
				count = 2;
			}
							
			}else{
	
				System.out.println("Conflito! Erro 409 - Enfermeira ja EXISTE!");
				return 409;	
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao setar os dados!");
		}

		try{
			
			if(count == 1){

			em.getTransaction().begin();
			em.persist(pf);
			idGerado = pf.getId();
			em.persist(e);
			em.getTransaction().commit();
			}
			
			if(count == 2){
				
				em.getTransaction().begin();
				em.merge(pf);
				idGerado = pf.getId(); 
				em.persist(e);
				em.getTransaction().commit();
				
			}

		}catch (Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Nova Enfermeira Cadastrada Com Sucesso!!!");
		return 201;
	}

	/**
	 * ESTA AQUI SOMENTE POR PRECAUÇÃO...SE PRECISAR...O METODO QUE ESTA FUNCIONANDO É O DE BAIXO
	 * ESTE RETORNA TUDO DA ENFERMEIRA...INCLUSIVE SUAS CLINICAS COM TODOS OS SEUS ATRIBUTOS
	 * @return
	 * @throws JSONException
	 */
	/*public List<Enfermeira> BuscarTodosEnfermeiros(){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		String consulta = "select e from Enfermeira e";
		TypedQuery<Enfermeira> query = em.createQuery(consulta, Enfermeira.class);
		List<Enfermeira> resultado = query.getResultList();
		return resultado;	
	}*/
	
	public JSONArray BuscarTodosEnfermeiros() throws JSONException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		JSONArray objarray = new JSONArray();
		
		String consulta = "select e.pessoaf.id, e.pessoaf.nome, e.pessoaf.tel, e.pessoaf.endereco, e.pessoaf.email,"
						+ "e.pessoaf.cpf, e.pessoaf.sexo, e.pessoaf.datanasc, e.coren from Enfermeira e";
		
		TypedQuery<Object[]> query = em.createQuery(consulta, Object[].class);
		List<Object[]> resultado = query.getResultList();
			
		objarray.put(resultado);
		
		return objarray;
		
	}
	
	public List<String> BuscarClinicasDasEnfermeiras(long pessoa_id){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		List<String> nomes = new ArrayList<String>();
		Number n;
		
		try{
			
			String consulta = "select count(e) from Enfermeira e where e.pessoaf.id = :pessoa_id";
			TypedQuery<Number> query = em.createQuery(consulta, Number.class);
			query.setParameter("pessoa_id", pessoa_id);
			n = query.getSingleResult();
			
			if(n.intValue()<1){
				
				System.out.println("Enfermeira NÃO encontrada! Não é possivel fazer a busca!");
				return null;
			}else{
			
				String consulta2 = "select c.nome from Enfermeira e join e.clinicas c where e.pessoaf.id = :pessoa_id";
				TypedQuery<String> query2 = em.createQuery(consulta2, String.class);
				query2.setParameter("pessoa_id", pessoa_id);
				nomes = query2.getResultList();
				
				if(nomes.isEmpty()){
					
					System.out.println("NÃO foi encontrado nenhuma Clinica Para esta Enfermeira");
					return null;
				}
			}
		}catch(Exception ex){
				ex.printStackTrace();
				System.out.println("Erro ao fazer busca no BD");
			}
			
			System.out.println("Clinicas Da Enfermeira Encontrado com Sucesso");
			return nomes;
	}
			
	
	public Enfermeira BuscarPorID(long id){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		TypedQuery<Enfermeira> query = em.createNamedQuery(Enfermeira.BuscarPorID, Enfermeira.class);
		query.setParameter("id", id);
		Enfermeira resultado = query.getSingleResult();
		return resultado;	
	}
	
	public JSONObject BuscarEnfermeiraPorPessoa(long id_pessoa) throws JSONException{
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		JSONObject objRetornado = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
	
		String consulta = "select e.pessoaf.id, e.pessoaf.nome, e.pessoaf.tel, e.pessoaf.endereco, e.pessoaf.email,"
						+ "e.pessoaf.cpf, e.pessoaf.sexo, e.pessoaf.datanasc, e.coren from Enfermeira e where e.pessoaf.id = :id";
		
		Query query = em.createQuery(consulta);
		query.setParameter("id", id_pessoa);
		Object[] resultado = (Object[]) query.getSingleResult();
		
		String consulta2 = "select c.id from Enfermeira e join e.clinicas c where e.pessoaf.id = :id";
		
		Query query2 = em.createQuery(consulta2);
		query2.setParameter("id", id_pessoa);
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
			obj1.putOpt("COREN", resultado[8]);
			
		}
		
		obj2.put("Clinica_ID", resultado2);
		
		objRetornado.put("PessoaFisica", obj1);
		objRetornado.put("Clinicas", obj2);
		
		return objRetornado;
		
	}
	
	public JSONObject BuscarEnfermeiraPorCOREN(String coren) throws JSONException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		JSONObject objRetornado = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
	
		String consulta = "select e.pessoaf.id, e.pessoaf.nome, e.pessoaf.tel, e.pessoaf.endereco, e.pessoaf.email,"
						+ "e.pessoaf.cpf, e.pessoaf.sexo, e.pessoaf.datanasc, e.coren from Enfermeira e where e.coren.numero = :numero_coren";
		
		Query query = em.createQuery(consulta);
		query.setParameter("numero_coren", coren);
		Object[] resultado = (Object[]) query.getSingleResult();
		
		String consulta2 = "select c.id from Enfermeira e join e.clinicas c where e.coren.numero = :numero_coren";
		
		Query query2 = em.createQuery(consulta2);
		query2.setParameter("numero_coren", coren);
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
			obj1.putOpt("COREN", resultado[8]);
			
		}
		
		obj2.put("Clinica_ID", resultado2);
		
		objRetornado.put("PessoaFisica", obj1);
		objRetornado.put("Clinicas", obj2);
		
		return objRetornado;
	}
	
	//TESTAR....
	public List<String> BuscarPacientePorEnfermeira(long id){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		Enfermeira e;
		List<Enfermeira> e_list = new ArrayList<Enfermeira>();
		String nome = null;
		List<String> resultado = null;
		
		try{
			
			String consulta = "select e from Enfermeira e where e.pessoaf.id = :pessoa_id";
			TypedQuery<Enfermeira> query = em.createQuery(consulta, Enfermeira.class);
			query.setParameter("pessoa_id", id);
			e_list = query.getResultList();
			
			if(e_list.isEmpty()){
				
				System.out.println("Enfermeira NÃO encontrada! Não é possivel fazer a busca!");
				return null;
			}else{
				
				Iterator<Enfermeira> itr = e_list.iterator();
				for(int i=0; i<e_list.size(); i++){
					while(itr.hasNext()){
						e = itr.next();
						nome = e.getPessoaf().getNome();
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao buscar a Enfermeira!");
		}
		
		try{
			
			TypedQuery<String> query = em.createNamedQuery(Paciente.BuscarPorEnfermeira, String.class);
			query.setParameter("nome_enfermeira", nome);
			resultado = query.getResultList();
		
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao buscar os Pacientes!");
		}
			
			return resultado;
	}
	

	public int AtualizarEnfermeira(String pessoa_id, String nome, String tel, String email, String sexo, String datanasc,
			String numero_cpf, String data_emissao_cpf, String rua, String numero, String complemento, String bairro, 
			String cidade, String uf, String pais, String cep, JSONArray clinicas_ids, 
			String numero_coren, String data_emissao_coren){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Enfermeira e = new Enfermeira();
		Endereco endereco = new Endereco();
		CPF cpf = new CPF();
		List<Enfermeira> listaE = new ArrayList<Enfermeira>();
		PessoaFisica pf = new PessoaFisica();
		Set<Clinica> clinicas = new HashSet<Clinica>();
		Set<Long> cl_id = new HashSet<Long>();
		Clinica clinica = new Clinica();
		COREN coren = new COREN();
		Number n = null;
		boolean masc = true;
		boolean fem = false;
		long p_id = JavaUtil.ConvertStringToLong(pessoa_id);
		
		try{
			
			String consulta = "select e from Enfermeira e where e.pessoaf.id = :pessoa_id";
			TypedQuery<Enfermeira> query = em.createQuery(consulta, Enfermeira.class);
			query.setParameter("pessoa_id", p_id);
			listaE = query.getResultList();
			
			String consulta2 = "select count(e) from Enfermeira e where e.pessoaf.id = :pessoa_id";
			TypedQuery<Number> query2 = em.createQuery(consulta2, Number.class);
			query2.setParameter("pessoa_id", p_id);
			n = query2.getSingleResult();
			
			if(listaE.isEmpty()){
				
				System.out.println("Enfermeira NÃO Encontrada! Não é possivel Atualizar os Dados!");
				return 404;
			
			}else{
				
				Iterator<Enfermeira> itr = listaE.iterator();
				
				for(int i = 0; i<listaE.size(); i++){
					try{
					while(itr.hasNext()){
						e = em.find(Enfermeira.class, itr.next().getId());
				}
					}catch(Exception ex){
						
						ex.printStackTrace();
						String msgErro = "Status: 500 - Erro ao fazer busca da Enfermeira no BD!";
						System.out.println(msgErro);
					}
				}
			}
				if(n.intValue()<1){
					
					System.out.println("PessoaFisica Não está Cadastrado no BD!! "
							+ "Cadastre antes de atualizar");
					return 404;
				}else{
					
					pf = e.getPessoaf();

				}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao buscar Enfermeira e/ou pessoaFisica no BD");
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
					
					Date datac = JavaUtil.ConvertStringToDate(data_emissao_coren);
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
					
					coren.setNumero(numero_coren);
					coren.setDataEmissao(datac);
					
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
					
					e.setPessoaf(pf);
					e.setClinicas(clinicas);
					e.setCoren(coren);			
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao setar dados!");
		}
		

		try{
			em.getTransaction().begin();
			em.merge(e);
			em.getTransaction().commit();

		}catch(Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		}finally{
			
			em.close();
			factory.close();
		}

		String msg = "Enfermeira Atualizada Com Sucesso!!!";
		System.out.println(msg);
		return 200;

	}

	public int RemoverEnfermeira(long id_pessoa) {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Enfermeira e = null;
		List<Enfermeira> enf = null;
		PessoaFisica pf = null;
		int count = 0;
		
		try { 

			String consulta = "select e from Enfermeira e where e.pessoaf.id = :id_pessoa";
			TypedQuery<Enfermeira> query = em.createQuery(consulta, Enfermeira.class);
			query.setParameter("id_pessoa", id_pessoa);
			enf = query.getResultList();
			
			if(enf.isEmpty()){
				
				String msgErro = "Status: 404 Not Found - Enfermeira NÃO Encontrada no BD!";
				System.out.println(msgErro);
				return 404;
				
			}else{
				
				Iterator<Enfermeira> itr = enf.iterator();
				
				for(int i = 0; i<enf.size(); i++){
					try{
						while(itr.hasNext()){
						e = em.find(Enfermeira.class, itr.next().getId());
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao buscar a Enfermeira");
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
			em.remove(e);
			em.remove(pf);
			em.getTransaction().commit();
			}
			
			if(count == 2){
				
				em.getTransaction().begin();
				em.remove(e);
				em.getTransaction().commit();
			}
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			em.getTransaction().rollback();
		
		}finally{
			
			em.close();
			factory.close();
		}

		String msg = "Enfermeira Removida Com Sucesso!!!";
		System.out.println(msg);
		return 200;
	}
}
