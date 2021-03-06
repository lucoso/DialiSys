package br.com.DialiSys.Manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.DialiSys.Model.AcessoVascular;
import br.com.DialiSys.Model.AntiCoagulante;
import br.com.DialiSys.Model.CPF;
import br.com.DialiSys.Model.Clinica;
import br.com.DialiSys.Model.Endereco;
import br.com.DialiSys.Model.Enfermeira;
import br.com.DialiSys.Model.Medico;
import br.com.DialiSys.Model.Paciente;
import br.com.DialiSys.Model.PessoaFisica;
import br.com.DialiSys.Util.JavaUtil;

public class PacienteManager {
	
	public static long idGerado = 0;

	public int CadastrarPaciente(String prontuario, String nome, String tel, String email, String sexo, String datanasc,
			String numero_cpf, String data_emissao_cpf, String rua, String numero, String complemento, String bairro, 
			String cidade, String uf, String pais, String cep, String clinica_id, String medico_responsavel, 
			String enfermeira_responsavel, String data_inicio, String peso_seco, String acesso_vascular, String anticoagulante) throws ParseException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Paciente p = new Paciente();
		PessoaFisica pf = new PessoaFisica();
		Endereco endereco = new Endereco();
		CPF cpf = new CPF();
		AcessoVascular av = new AcessoVascular();
		AntiCoagulante ac = new AntiCoagulante();
		Clinica c = new Clinica();
		Medico m = new Medico();
		Enfermeira e = new Enfermeira();
		Number n;
		int count = 0;
		boolean masc = true;
		boolean fem = false;
		List<PessoaFisica> pf_list = new ArrayList<PessoaFisica>();
		List<Clinica> c_list = new ArrayList<Clinica>();
		List<Medico> m_list = new ArrayList<Medico>();
		List<Enfermeira> e_list = new ArrayList<Enfermeira>();
		long c_id = JavaUtil.ConvertStringToLong(clinica_id);
		long m_pessoa_id = JavaUtil.ConvertStringToLong(medico_responsavel);
		long e_pessoa_id = JavaUtil.ConvertStringToLong(enfermeira_responsavel);
		
		try{
			
			String consulta = "select count(p) from Paciente p where p.prontuario = :prontuario";
			TypedQuery<Number> query = em.createQuery(consulta, Number.class);
			query.setParameter("prontuario", prontuario);
			n = query.getSingleResult();
			
			TypedQuery<PessoaFisica> query2 = em.createNamedQuery(PessoaFisica.BuscarPorCPF, PessoaFisica.class);
			query2.setParameter("numero_cpf", numero_cpf);
			pf_list = query2.getResultList();
			
			TypedQuery<Clinica> query3 = em.createNamedQuery(Clinica.BuscarPorID, Clinica.class);
			query3.setParameter("id", c_id);
			c_list = query3.getResultList();
			
			String consulta2 = "select m from Medico m where m.pessoaf.id = :pessoa_id";
			TypedQuery<Medico> query4 = em.createQuery(consulta2, Medico.class);
			query4.setParameter("pessoa_id", m_pessoa_id);
			m_list = query4.getResultList();
			
			String consulta3 = "select e from Enfermeira e where e.pessoaf.id = :pessoa_id";
			TypedQuery<Enfermeira> query5 = em.createQuery(consulta3, Enfermeira.class);
			query5.setParameter("pessoa_id", e_pessoa_id);
			e_list = query5.getResultList();
			
			if(n.intValue()>=1){
				
				System.out.println("Conflito: Paciente j� existe no BD");
				return 409;
			}
			
			if(c_list.isEmpty()){
				System.out.println("Clinica n�o encontrada! Cadastre antes de cadastrar o Paciente!");
				return 404;
				
			}else{
				
				Iterator<Clinica> itr = c_list.iterator();
				
				for(int i=0;i<c_list.size();i++){
					try{
						while(itr.hasNext()){
							c = itr.next();
						}
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("Erro ao buscar a Clinica");
					}
				}
				
			}
			
			if(m_list.isEmpty()){
				
				System.out.println("Medico N�O encontrado no BD! Cadastre antes de cadastrar o Paciente!");
				return 404;
			
			}else{
				
				Iterator<Medico> itr = m_list.iterator();
				
				for(int i=0;i<m_list.size();i++){
					try{
						while(itr.hasNext()){
							m = itr.next();
						}
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("Erro ao buscar o medico");
					}
				}
			}
				
			if(e_list.isEmpty()){
				
				System.out.println("Enfermeira N�O encontrada no BD! Cadastre antes de cadastrar o Paciente!");
				return 404;
			
			}else{
				
				Iterator<Enfermeira> itr = e_list.iterator();
				
				for(int i=0;i<e_list.size();i++){
					try{
						while(itr.hasNext()){
							e = itr.next();
						}
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("Erro ao buscar a enfermeira");
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao fazer busca no BD");
		}
		
		try{
		
			if(pf_list.isEmpty()){
			
			Date datainicio = JavaUtil.ConvertStringToDate(data_inicio);
			Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
			Date datanascimento = JavaUtil.ConvertStringToDate(datanasc);
			
			BigDecimal ps = JavaUtil.ConvertStringToBigDecimal(peso_seco);

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
				
				System.out.println("CPF digitado � INV�LIDO! Por Favor insira o CPF corretamente!");
				return 400;
			}
			
			ac.setNome(anticoagulante);
			av.setTipo(acesso_vascular);
			
			endereco.setBairro(bairro);
			endereco.setCep(cep);
			endereco.setCidade(cidade);
			endereco.setComplemento(complemento);
			endereco.setNumero(numero);
			endereco.setPais(pais);
			endereco.setRua(rua);
			endereco.setUf(uf);
			
			pf.setNome(nome);
			pf.setDatanasc(datanascimento);
			pf.setEmail(email);
			pf.setTel(tel);
			pf.setCpf(cpf);
			pf.setEndereco(endereco);
			
			p.setAcessovascular(av);
			p.setAnticoagulante(ac);
			p.setDatainicio(datainicio);
			p.setPesoseco(ps);
			p.setProntuario(prontuario);
			p.setPessoaf(pf);
			p.setEnfermeiraResponsavel(e);
			p.setMedicoResponsavel(m);
			p.setClinica(c);
			
			count = 1;
		
			}else{
			
				Iterator<PessoaFisica> itr = pf_list.iterator();
				
				for(int i=0;i<pf_list.size();i++){
					try{
						while(itr.hasNext()){
							pf = itr.next();
						}
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("Erro ao buscar a Pessoa Fisica");
					}
				}
				
				
				Date datainicio = JavaUtil.ConvertStringToDate(data_inicio);
				Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
				Date datanascimento = JavaUtil.ConvertStringToDate(datanasc);
				
				BigDecimal ps = JavaUtil.ConvertStringToBigDecimal(peso_seco);
				
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
					
					System.out.println("CPF digitado � INV�LIDO! Por Favor insira o CPF corretamente!");
					return 400;
				}
				
				ac.setNome(anticoagulante);
				av.setTipo(acesso_vascular);
				
				endereco.setBairro(bairro);
				endereco.setCep(cep);
				endereco.setCidade(cidade);
				endereco.setComplemento(complemento);
				endereco.setNumero(numero);
				endereco.setPais(pais);
				endereco.setRua(rua);
				endereco.setUf(uf);
				
				pf.setNome(nome);
				pf.setDatanasc(datanascimento);
				pf.setEmail(email);
				pf.setTel(tel);
				pf.setCpf(cpf);
				pf.setEndereco(endereco);
				
				p.setAcessovascular(av);
				p.setAnticoagulante(ac);
				p.setDatainicio(datainicio);
				p.setPesoseco(ps);
				p.setProntuario(prontuario);
				p.setPessoaf(pf);
				p.setEnfermeiraResponsavel(e);
				p.setMedicoResponsavel(m);
				p.setClinica(c);
				
				count = 2;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao setar os Dados");
		}
			
			
		try{
		
			if(count == 1){
				
				em.getTransaction().begin();
				em.persist(pf);
				idGerado = pf.getId();
				em.persist(p);
				em.getTransaction().commit();
			}
			
			if(count == 2){

			em.getTransaction().begin();
			em.merge(pf);
			idGerado = pf.getId();
			em.persist(p);
			em.getTransaction().commit();
			}

		}catch (Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Novo Paciente Cadastrado Com Sucesso!!!");
		return 201;

	}

	/**
	 * FUNCIONANDO...POREM COM AQUELE MESMO PROBLEMA DE RETORNAR O PACIENTE REPETIDO PARA CADA SESS�O
	 * NESSE CASO RESOLVI RETORNAR TODOS OS PACIENTES SEM AS SESSOES...POIS SE HOUVER 500 OU MAIS SESS�ES...
	 * VAI DAR ESTOURO DE MEMORIA!!
	 * @return
	 * @throws JSONException
	 */
	public JSONObject BuscarTodosPacientes() throws JSONException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		
		JSONObject objRetornado = new JSONObject();

		String consulta = "select p.pessoaf.id, p.pessoaf.nome, p.pessoaf.tel, p.pessoaf.endereco, p.pessoaf.email,"
				+ "p.pessoaf.cpf, p.pessoaf.sexo, p.pessoaf.datanasc, p.prontuario, p.pesoseco, p.datainicio,"
				+ "p.anticoagulante, p.acessovascular, p.medicoResponsavel.id, p.enfermeiraResponsavel.id,"
				+ "p.clinica.id from Paciente p";

		Query query = em.createQuery(consulta);
		List<Object[]> resultado = (List<Object[]>) query.getResultList();
		
		objRetornado.put("Paciente", resultado);
		
		return objRetornado;
	}
	
	public JSONObject BuscarPacientePorPessoa(long pessoa_id) throws JSONException{
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		JSONObject objRetornado = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();

		String consulta = "select p.pessoaf.id, p.pessoaf.nome, p.pessoaf.tel, p.pessoaf.endereco, p.pessoaf.email,"
				+ "p.pessoaf.cpf, p.pessoaf.sexo, p.pessoaf.datanasc, p.prontuario, p.pesoseco, p.datainicio,"
				+ "p.anticoagulante, p.acessovascular, p.medicoResponsavel.id, p.enfermeiraResponsavel.id,"
				+ "p.clinica.id from Paciente p where p.pessoaf.id = :id";

		Query query = em.createQuery(consulta);
		query.setParameter("id", pessoa_id);
		Object[] resultado = (Object[]) query.getSingleResult();

		String consulta2 = "select ps.id from Paciente p join p.sessoes ps where p.pessoaf.id = :id";

		Query query2 = em.createQuery(consulta2);
		query2.setParameter("id", pessoa_id);
		Object resultado2 = (Object) query2.getResultList();

		for (int i = 0; i < resultado.length; i++) {
			obj1.putOpt("ID", resultado[0]);
			obj1.putOpt("Nome", resultado[1]);
			obj1.putOpt("Telefone", resultado[2]);
			obj1.putOpt("Endere�o", resultado[3]);
			obj1.putOpt("Email", resultado[4]);
			obj1.putOpt("CPF", resultado[5]);
			obj1.putOpt("Sexo", resultado[6]);
			obj1.putOpt("Data de Nascimento", resultado[7]);
			obj1.putOpt("Prontu�rio", resultado[8]);
			obj1.putOpt("Peso Seco", resultado[9]);
			obj1.putOpt("Data Inicio", resultado[10]);
			obj1.putOpt("AntiCoagulante", resultado[11]);
			obj1.putOpt("Acesso Vascular", resultado[12]);
			obj1.putOpt("Medico Respons�vel ID", resultado[13]);
			obj1.putOpt("Enfermeira Respons�vel ID", resultado[14]);
			obj1.putOpt("Clinica ID", resultado[15]);

		}

		obj2.put("Sess�o_ID", resultado2);

		objRetornado.put("Paciente", obj1);
		objRetornado.put("Sess�es", obj2);
		
		return objRetornado;
	
	}
	
	public Paciente BuscarPorID(long id){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		TypedQuery<Paciente> query = em.createNamedQuery(Paciente.BuscarPorID, Paciente.class);
				query.setParameter("id", id);
				Paciente resultado = query.getSingleResult();
				return resultado;
	}
	
	public JSONObject BuscarPorProntuario(String prontuario) throws JSONException{
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		JSONObject objRetornado = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();

		String consulta = "select p.pessoaf.id, p.pessoaf.nome, p.pessoaf.tel, p.pessoaf.endereco, p.pessoaf.email,"
				+ "p.pessoaf.cpf, p.pessoaf.sexo, p.pessoaf.datanasc, p.prontuario, p.pesoseco, p.datainicio,"
				+ "p.anticoagulante, p.acessovascular, p.medicoResponsavel.id, p.enfermeiraResponsavel.id,"
				+ "p.clinica.id from Paciente p where p.prontuario = :prontuario";

		Query query = em.createQuery(consulta);
		query.setParameter("prontuario", prontuario);
		Object[] resultado = (Object[]) query.getSingleResult();

		String consulta2 = "select ps.id from Paciente p join p.sessoes ps where p.prontuario = :prontuario";

		Query query2 = em.createQuery(consulta2);
		query2.setParameter("prontuario", prontuario);
		Object resultado2 = (Object) query2.getResultList();

		for (int i = 0; i < resultado.length; i++) {
			obj1.putOpt("ID", resultado[0]);
			obj1.putOpt("Nome", resultado[1]);
			obj1.putOpt("Telefone", resultado[2]);
			obj1.putOpt("Endere�o", resultado[3]);
			obj1.putOpt("Email", resultado[4]);
			obj1.putOpt("CPF", resultado[5]);
			obj1.putOpt("Sexo", resultado[6]);
			obj1.putOpt("Data de Nascimento", resultado[7]);
			obj1.putOpt("Prontu�rio", resultado[8]);
			obj1.putOpt("Peso Seco", resultado[9]);
			obj1.putOpt("Data Inicio", resultado[10]);
			obj1.putOpt("AntiCoagulante", resultado[11]);
			obj1.putOpt("Acesso Vascular", resultado[12]);
			obj1.putOpt("Medico Respons�vel ID", resultado[13]);
			obj1.putOpt("Enfermeira Respons�vel ID", resultado[14]);
			obj1.putOpt("Clinica ID", resultado[15]);

		}

		obj2.put("Sess�o_ID", resultado2);

		objRetornado.put("Paciente", obj1);
		objRetornado.put("Sess�es", obj2);
		
		return objRetornado;
	}
	

	public int AtualizarPaciente(String pessoa_id, String prontuario, String nome, String tel, String email, 
			String sexo, String datanasc, String numero_cpf, String data_emissao_cpf, String rua, String numero, 
			String complemento, String bairro, String cidade, String uf, String pais, String cep, String clinica_id, 
			String medico_responsavel, String enfermeira_responsavel, String data_inicio, String peso_seco, 
			String acesso_vascular, String anticoagulante) throws ParseException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		Paciente p = new Paciente();
		PessoaFisica pf = new PessoaFisica();
		Endereco endereco = new Endereco();
		CPF cpf = new CPF();
		AcessoVascular av = new AcessoVascular();
		AntiCoagulante ac = new AntiCoagulante();
		Clinica c = new Clinica();
		Medico m = new Medico();
		Enfermeira e = new Enfermeira();
		Number n;
		boolean masc = true;
		boolean fem = false;
		List<Paciente> pac_list = new ArrayList<Paciente>();
		List<Clinica> c_list = new ArrayList<Clinica>();
		List<Medico> m_list = new ArrayList<Medico>();
		List<Enfermeira> e_list = new ArrayList<Enfermeira>();
		long c_id = JavaUtil.ConvertStringToLong(clinica_id);
		long m_pessoa_id = JavaUtil.ConvertStringToLong(medico_responsavel);
		long e_pessoa_id = JavaUtil.ConvertStringToLong(enfermeira_responsavel);
		long pac_id = JavaUtil.ConvertStringToLong(pessoa_id);
		
		try{
			
			String consulta = "select p from Paciente p where p.pessoaf.id = :id";
			TypedQuery<Paciente> query = em.createQuery(consulta, Paciente.class);
			query.setParameter("id", pac_id);
			pac_list = query.getResultList();
			
			String consulta2 = "select count(pf) from PessoaFisica pf where pf.id = :id";
			TypedQuery<Number> query2 = em.createQuery(consulta2, Number.class);
			query2.setParameter("id", pac_id);
			n = query2.getSingleResult();
			
			TypedQuery<Clinica> query3 = em.createNamedQuery(Clinica.BuscarPorID, Clinica.class);
			query3.setParameter("id", c_id);
			c_list = query3.getResultList();
			
			String consulta3 = "select m from Medico m where m.pessoaf.id = :pessoa_id";
			TypedQuery<Medico> query4 = em.createQuery(consulta3, Medico.class);
			query4.setParameter("pessoa_id", m_pessoa_id);
			m_list = query4.getResultList();
			
			String consulta4 = "select e from Enfermeira e where e.pessoaf.id = :pessoa_id";
			TypedQuery<Enfermeira> query5 = em.createQuery(consulta4, Enfermeira.class);
			query5.setParameter("pessoa_id", e_pessoa_id);
			e_list = query5.getResultList();
			
			if(pac_list.isEmpty()){
				
				System.out.println("Paciente N�O encontrado! N�o � possivel Atualizar");
				return 404;
				
			}else{
				
				Iterator<Paciente> itr = pac_list.iterator();
				
				for(int i=0; i<pac_list.size(); i++){
					while(itr.hasNext()){
						p = itr.next();
					}
				}
			}
			
			if(n.intValue()<1){
				
				System.out.println("Pessoa Fisica N�o encontrada no BD! Cadastre antes de Atualizar");
				return 404;
			}else{
				
				pf = p.getPessoaf();
			}
			
			if(c_list.isEmpty()){
				
				System.out.println("Clinica N�O encontrada! Cadastre antes de atualizar");
				return 404;
			}else{
				
				Iterator<Clinica> itr = c_list.iterator();
				
				for(int i=0; i<c_list.size(); i++){
					while(itr.hasNext()){
						c = itr.next();
					}
				}
			}
			
			if(m_list.isEmpty()){
				
				System.out.println("Medico N�O encontrado! Cadastre antes de atualizar");
				return 404;
			}else{
				
				Iterator<Medico> itr = m_list.iterator();
				
				for(int i=0; i<c_list.size(); i++){
					while(itr.hasNext()){
						m = itr.next();
					}
				}
			}
			
			if(e_list.isEmpty()){
				System.out.println("Enfermeira N�O encontrada! Cadastre antes de atualizar");
				return 404;
			}else{
				
				Iterator<Enfermeira> itr = e_list.iterator();
				
				for(int i=0; i<c_list.size(); i++){
					while(itr.hasNext()){
						e = itr.next();
					}
				}
			}
				
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao fazer busca no BD");
		}
		
		try{
			
			Date datainicio = JavaUtil.ConvertStringToDate(data_inicio);
			Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
			Date datanascimento = JavaUtil.ConvertStringToDate(datanasc);
			
			BigDecimal ps = JavaUtil.ConvertStringToBigDecimal(peso_seco);

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
				
				System.out.println("CPF digitado � INV�LIDO! Por Favor insira o CPF corretamente!");
				return 400;
			}
			
			ac.setNome(anticoagulante);
			av.setTipo(acesso_vascular);
			
			endereco.setBairro(bairro);
			endereco.setCep(cep);
			endereco.setCidade(cidade);
			endereco.setComplemento(complemento);
			endereco.setNumero(numero);
			endereco.setPais(pais);
			endereco.setRua(rua);
			endereco.setUf(uf);
			
			pf.setNome(nome);
			pf.setDatanasc(datanascimento);
			pf.setEmail(email);
			pf.setTel(tel);
			pf.setCpf(cpf);
			pf.setEndereco(endereco);
			
			p.setAcessovascular(av);
			p.setAnticoagulante(ac);
			p.setDatainicio(datainicio);
			p.setPesoseco(ps);
			p.setProntuario(prontuario);
			p.setPessoaf(pf);
			p.setEnfermeiraResponsavel(e);
			p.setMedicoResponsavel(m);
			p.setClinica(c);
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao setar os dados");
		}
		
		try{
			em.getTransaction().begin();
			em.merge(p);
			em.getTransaction().commit();

		}catch(Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Paciente Atualizado Com Sucesso!!!");
		return 200;

	}

	public int RemoverPaciente(long id_pessoa){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		Paciente p = null;
		PessoaFisica pf = null;
		List<Paciente> p_list  = null;
		Number n;
		int count = 0;
		
		try{
			
			String consulta = "select p from Paciente p where p.pessoaf.id = :id_pessoa";
			TypedQuery<Paciente> query = em.createQuery(consulta, Paciente.class);
			query.setParameter("id_pessoa", id_pessoa);
			p_list = query.getResultList();
			
			String consulta2 = "select count(pf) from PessoaFisica pf where pf.id = :id_pessoa";
			TypedQuery<Number> query2 = em.createQuery(consulta2, Number.class);
			query2.setParameter("id_pessoa", id_pessoa);
			n = query2.getSingleResult();
			
			if(p_list.isEmpty()){
				
				System.out.println("Paciente N�O esta cadastrado no BD! N�o � possivel remover!");
				return 404;
				
			}else{
				
				Iterator<Paciente> itr = p_list.iterator();
				
				for(int i=0; i<p_list.size(); i++){
					try{
						while(itr.hasNext()){
							p = itr.next();
						}
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("Erro ao buscar Paciente");
					}
				}
			}
			
			if(n.intValue()<1){
				System.out.println("Pessoa Fisica N�O encontrada no BD");
				return 404;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao fazer busca no BD");
		}

		
		try{
			
			String consulta = "select m.pessoaf.id from Medico m where m.pessoaf.id = :id_pessoa";
			TypedQuery<Long> query = em.createQuery(consulta, Long.class);
			query.setParameter("id_pessoa", id_pessoa);
			List<Long> med_list = query.getResultList();
			
			String consulta2 = "select e.pessoaf.id from Enfermeira e where e.pessoaf.id = :id_pessoa";
			TypedQuery<Long> query2 = em.createQuery(consulta2, Long.class);
			query2.setParameter("id_pessoa", id_pessoa);
			List<Long> enf_list = query2.getResultList();
			
			String consulta3 = "select u.pessoaf.id from Usuario u where u.pessoaf.id = :id_pessoa";
			TypedQuery<Long> query3 = em.createQuery(consulta3, Long.class);
			query3.setParameter("id_pessoa", id_pessoa);
			List<Long> user_list = query3.getResultList();
			
			if((med_list.isEmpty()) && (enf_list.isEmpty()) && (user_list.isEmpty())){
				/**
				 * PessoaFisica N�O Esta sendo usada no sistema!
				 * PODE ser excluida
				 */
				pf = p.getPessoaf();
				count = 1;
			
			}else{
				/**
				 * PessoaFisica Esta sendo usada no sistema!
				 * N�O PODE ser excluida
				 */
				count = 2;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao verificar Pessoa Fisica");
		}
		
		try { 

			if(count == 1){
				
				em.getTransaction().begin();  
				em.remove(p); 
				em.remove(pf);
				em.getTransaction().commit(); 
			}
			if(count == 2){
			
				em.getTransaction().begin();  
				em.remove(p); 
				em.getTransaction().commit(); 
			}

		} catch (Exception ex) { 

			ex.printStackTrace(); 
			em.getTransaction().rollback(); 

		}finally{
			
			em.close();
			factory.close();
		} 

		System.out.println("Paciente Removido Com Sucesso!!!");
		return 200;
	}
}
	
	

