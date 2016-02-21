package br.com.DialiSys.Manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import br.com.DialiSys.Model.Ator;
import br.com.DialiSys.Model.CPF;
import br.com.DialiSys.Model.Endereco;
import br.com.DialiSys.Model.PessoaFisica;
import br.com.DialiSys.Model.Usuario;
import br.com.DialiSys.Util.JavaUtil;

public class UsuarioManager {
	
	public static long idGerado;
	
	public int CriarUsuarioComoPessoa(String nome, String tel, String email, String sexo, String datanasc,
			String numero_cpf, String data_emissao_cpf, String rua, String numero, String complemento, String bairro, 
			String cidade, String uf, String pais, String cep, String login, String senha, String ator){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		Ator a = null;
		Usuario user = new Usuario();
		Endereco e = new Endereco();
		CPF cpf = new CPF();
		PessoaFisica pf = new PessoaFisica();
		List<Usuario> user_list = new ArrayList<Usuario>();
		List<PessoaFisica> pf_list = new ArrayList<PessoaFisica>();
		int count = 0;
		boolean masc = true;
		boolean fem = false;
		
		try{
			
			TypedQuery<Usuario> query = em.createNamedQuery(Usuario.BuscarPorLogin, Usuario.class);
			query.setParameter("login_usuario", login);
			user_list = query.getResultList();
			
			TypedQuery<PessoaFisica> query2 = em.createNamedQuery(PessoaFisica.BuscarPorCPF, PessoaFisica.class);
			query2.setParameter("numero_cpf", numero_cpf);
			pf_list = query2.getResultList();
					
		if(user_list.isEmpty()){
			if(pf_list.isEmpty()){
				
			
			Date datan = JavaUtil.ConvertStringToDate(datanasc);
			Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
			
			if((sexo.equals("Masculino")) || (sexo.equals("masculino"))){
				
				pf.setSexo(masc);
			}
			if((sexo.equals("Feminino")) || (sexo.equals("feminino"))){
				
				pf.setSexo(fem);
			}
			
			a = JavaUtil.ConvertStringToAtor(ator);
			
			if(cpf.validaCPF(numero_cpf) == true){
			
				cpf.setDataEmissao(datacpf);
				cpf.setNumero(numero_cpf);
			}else{
				
				System.out.println("CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!");
				return 400;
			}
			
			e.setRua(rua);
			e.setNumero(numero);
			e.setComplemento(complemento);
			e.setCidade(cidade);
			e.setBairro(bairro);
			e.setPais(pais);
			e.setUf(uf);
			e.setCep(cep);
			
			pf.setNome(nome);
			pf.setDatanasc(datan);
			pf.setCpf(cpf);
			pf.setEmail(email);
			pf.setTel(tel);
			pf.setEndereco(e);
			
			user.setLogin(login);
			user.setSenha(senha);
			user.setAtor(a);
			user.setPessoaFisica(pf);
			
			count = 1;
			
		}else{
		
			Iterator<PessoaFisica> itr = pf_list.iterator();
			
			for(int i=0; i<pf_list.size(); i++){
				try{
					while(itr.hasNext()){
						pf = em.find(PessoaFisica.class, itr.next().getId());
					}
				}catch(Exception ex){
					ex.printStackTrace();
					System.out.println("Erro ao buscar a Pessoa Fisica!");
				}
			}
			
			Date datan = JavaUtil.ConvertStringToDate(datanasc);
			Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
			
			if((sexo.equals("Masculino")) || (sexo.equals("masculino"))){
				
				pf.setSexo(masc);
			}
			if((sexo.equals("Feminino")) || (sexo.equals("feminino"))){
				
				pf.setSexo(fem);
			}
			
			a = JavaUtil.ConvertStringToAtor(ator);
			
			if(cpf.validaCPF(numero_cpf) == true){
				
				cpf.setDataEmissao(datacpf);
				cpf.setNumero(numero_cpf);
			}else{
				
				System.out.println("CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!");
				return 400;
			}
			
			e.setRua(rua);
			e.setNumero(numero);
			e.setComplemento(complemento);
			e.setCidade(cidade);
			e.setBairro(bairro);
			e.setPais(pais);
			e.setUf(uf);
			e.setCep(cep);
			
			pf.setNome(nome);
			pf.setDatanasc(datan);
			pf.setCpf(cpf);
			pf.setEmail(email);
			pf.setTel(tel);
			pf.setEndereco(e);
			
			user.setLogin(login);
			user.setSenha(senha);
			user.setAtor(a);
			user.setPessoaFisica(pf);
			
			count = 2;
		}
		}else{
			
			System.out.println("Usuario ja Existe! Não é possivel criar um novo!");
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
			em.persist(user);
			idGerado = pf.getId();
			em.getTransaction().commit();
			}
			
			if(count == 2){
			
				em.getTransaction().begin();
				em.merge(pf);
				em.persist(user);
				idGerado = pf.getId();
				em.getTransaction().commit();
			}

		}catch (Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		}

		System.out.println("Novo Usuário Criado Com Sucesso!!!");
		return 201;
	}
	
	public int CriarUsuario(String login, String senha, String ator){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Usuario user = new Usuario();
		List<Usuario> user_list = new ArrayList<Usuario>();
		Ator a = null;
		
		try{
			
			TypedQuery<Usuario> query = em.createNamedQuery(Usuario.BuscarPorLogin, Usuario.class);
			query.setParameter("login_usuario", login);
			user_list = query.getResultList();
			
			if(user_list.isEmpty()){
				
				a = JavaUtil.ConvertStringToAtor(ator);
				
				user.setLogin(login);
				user.setSenha(senha);
				user.setAtor(a);
				
			}else{
				
				System.out.println("Usuario ja Existe! Não é possivel criar um novo!");
				return 409;
			}
				
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao setar os dados!");
	}
			
			try{

				em.getTransaction().begin();
				em.persist(user);
				idGerado = user.getId();
				em.getTransaction().commit();
				
			}catch (Exception ex) {

				ex.printStackTrace();
				em.getTransaction().rollback();
			}

			System.out.println("Novo Usuário Criado Com Sucesso!!!");
			return 201;
			}
	
	public List<Usuario> BuscarTodosUsuarios(){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		String consulta = "select u from Usuario u order by u.id";
		TypedQuery<Usuario> query = em.createQuery(consulta, Usuario.class);
		List<Usuario> resultado = query.getResultList();
		return resultado;	
	}
	
	public Usuario BuscarUsuarioPorID(long id){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		TypedQuery<Usuario> query = em.createNamedQuery(Usuario.BuscarPorID, Usuario.class);
		query.setParameter("id", id);
		Usuario resultado = query.getSingleResult();
		return resultado;	
	}
	
	public Usuario BuscarUsuarioPorLogin(String login){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
			
		TypedQuery<Usuario> query = em.createNamedQuery(Usuario.BuscarPorLogin, Usuario.class);
		query.setParameter("login_usuario", login);
		Usuario resultado = query.getSingleResult();
		return resultado;
	}
	
	
	public int AtualizarUsuarioComoPessoa(String user_id, String nome, String tel, String email, String sexo, String datanasc,
			String numero_cpf, String data_emissao_cpf, String rua, String numero, String complemento, String bairro, 
			String cidade, String uf, String pais, String cep, String login, String senha, String ator){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		Usuario user = new Usuario();
		List<Usuario> user_list = new ArrayList<Usuario>();
		PessoaFisica pf = new PessoaFisica();
		CPF cpf = new CPF();
		Endereco e = new Endereco();
		Ator a = null;
		long id = JavaUtil.ConvertStringToLong(user_id);
		Long pf_id = null;
		Number n = 0;
		boolean masc = true;
		boolean fem = false;
		
		try{
			
			TypedQuery<Usuario> query = em.createNamedQuery(Usuario.BuscarPorID, Usuario.class);
			query.setParameter("id", id);
			user_list = query.getResultList();

			
				if(user_list.isEmpty()){
					
					System.out.println("Usuario NÃO Encontrado! Cadastre antes de Atualizar!");
					return 404;
					
				}else{
					
					Iterator<Usuario> itr = user_list.iterator();
					
					for(int i = 0; i<user_list.size(); i++){
						try{
							while(itr.hasNext()){
								user = em.find(Usuario.class, itr.next().getId());
								pf_id = user.getPessoaFisica().getId();
						}
						}catch(Exception ex){
							ex.printStackTrace();
							System.out.println("Erro ao buscar Usuario");
						}
					}
					
				}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao fazer busca no BD");
		}
				
				try{
					
					String consulta = "select count(u) from Usuario u where u.pessoaf.id = :pf_id";
					TypedQuery<Number> query2 = em.createQuery(consulta, Number.class);
					query2.setParameter("pf_id", pf_id);
					n = query2.getSingleResult();
					
					if(n.intValue()<1){
						
						System.out.println("Pessoa Fisica NÃO Encontrado! Cadastre antes de Atualizar!");
						return 404;
					}else{
						
						pf = user.getPessoaFisica();
					}
					
				}catch(Exception ex){
					ex.printStackTrace();
					System.out.println("Erro ao buscar Pessoa Fisica");
				}
			
			try{
				
				Date datan = JavaUtil.ConvertStringToDate(datanasc);
				Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
				
				if((sexo.equals("Masculino")) || (sexo.equals("masculino"))){
					
					pf.setSexo(masc);
				}
				if((sexo.equals("Feminino")) || (sexo.equals("feminino"))){
					
					pf.setSexo(fem);
				}
				
				a = JavaUtil.ConvertStringToAtor(ator);
				
				if(cpf.validaCPF(numero_cpf) == true){
					
					cpf.setDataEmissao(datacpf);
					cpf.setNumero(numero_cpf);
				}else{
					
					System.out.println("CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!");
					return 400;
				}
				
				e.setRua(rua);
				e.setNumero(numero);
				e.setComplemento(complemento);
				e.setCidade(cidade);
				e.setBairro(bairro);
				e.setPais(pais);
				e.setUf(uf);
				e.setCep(cep);
				
				pf.setNome(nome);
				pf.setDatanasc(datan);
				pf.setCpf(cpf);
				pf.setEmail(email);
				pf.setTel(tel);
				pf.setEndereco(e);
				
				user.setLogin(login);
				user.setSenha(senha);
				user.setAtor(a);
				user.setPessoaFisica(pf);
			
			}catch(Exception ex){
				
				ex.printStackTrace();
				System.out.println("Erro ao setar os dados!");
			}
			

		try{
			em.getTransaction().begin();
			em.merge(pf);
			em.merge(user);
			em.getTransaction().commit();

		}catch(Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Usuário Atualizado Com Sucesso!!!");
		return 200;
	}
	
	public int AtualizarUsuario(String user_id, String login, String senha, String ator){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Usuario user = new Usuario();
		List<Usuario> user_list = new ArrayList<Usuario>();
		Ator a = null;
		long id = JavaUtil.ConvertStringToLong(user_id);
		
		try{
			
			TypedQuery<Usuario> query = em.createNamedQuery(Usuario.BuscarPorID, Usuario.class);
			query.setParameter("id", id);
			user_list = query.getResultList();
			
			if(user_list.isEmpty()){
				
				System.out.println("Usuario NÃO Encontrado! Não é possivel Atualizar!");
				return 404;
				
			}else{
				
				Iterator<Usuario> itr = user_list.iterator();
				
				for(int i=0; i<user_list.size(); i++){
					while(itr.hasNext()){
						user = em.find(Usuario.class, itr.next().getId());
					}
				}
				
				a = JavaUtil.ConvertStringToAtor(ator);
				
				user.setLogin(login);
				user.setSenha(senha);
				user.setAtor(a);
			}
				
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao setar os dados!");
			}
		
		try{

			em.getTransaction().begin();
			em.merge(user);
			em.getTransaction().commit();
			
		}catch (Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Usuário Atualizado Com Sucesso!!!");
		return 200;
		}

	public int RemoverUsuario(long id) {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Usuario user = null;
		PessoaFisica pf = null;
		List<Usuario> user_list = new ArrayList<Usuario>();
		Number n=0, n1,n2, n3, n4;
		long id_p = 0;
		
		/**
		 * Se count = 1 - NÃO existe pessoa fisica ligada a este Usuario
		 * ...ou seja não é necessario excluir nenhuma "Pessoa Fisica"
		 * 
		 * Se count = 2 - significa que o id da pessoa "deste Usuario" EXISTE, mas não esta sendo usado por mais "ninguem" 
		 * ...ou seja esta "Pessoa Fisica" PODE ser excluida!!
		 * 
		 * Se count = 3 - significa que existe uma "Pessoa Fisica" ligada a este usuario e esta sendo usada em outro 
		 * "Papel"...ou seja, NÃO PODE ser excluida!!
		 */
		int count = 0;
		
		
		try { 

			TypedQuery<Usuario> query = em.createNamedQuery(Usuario.BuscarPorID, Usuario.class);
			query.setParameter("id", id);
			user_list = query.getResultList();
			
			String consulta = "select count(u) from Usuario u where u.pessoaf.id = :pf_id";
			TypedQuery<Number> query2 = em.createQuery(consulta, Number.class);
			query2.setParameter("pf_id", id);
			n = query2.getSingleResult();
			
			if(user_list.isEmpty()){
				
				System.out.println("Usuario Não Encontrado no BD");
				return 404;
			}else{
				
				if(n.intValue()>=1){
				Iterator<Usuario> itr = user_list.iterator();
				for(int i=0; i<user_list.size(); i++){
						while(itr.hasNext()){
							user = itr.next();
							id_p = user.getPessoaFisica().getId();
						}
				}
			}else{
				
				Iterator<Usuario> itr = user_list.iterator();
				for(int i=0; i<user_list.size(); i++){
						while(itr.hasNext()){
							user = itr.next();
							count = 1;
						}
				}
			}

		}
		}catch (Exception ex) { 

			ex.printStackTrace();
			System.out.println("Erro ao fazer busca no BD");
		}
		
		
		if(count == 0){
		try{
			
			String consulta = "select count(p.pessoaf.id) from Paciente p where p.pessoaf.id = :id_pessoa1";
			TypedQuery<Number> query = em.createQuery(consulta, Number.class);
			query.setParameter("id_pessoa1", id_p);
			n1 = query.getSingleResult();
			
			String consulta2 = "select count(m.pessoaf.id) from Medico m where m.pessoaf.id = :id_pessoa2";
			TypedQuery<Number> query2 = em.createQuery(consulta2, Number.class);
			query2.setParameter("id_pessoa2", id_p);
			n2 = query.getSingleResult();
			
			String consulta3 = "select count(e.pessoaf.id) from Enfermeira e where e.pessoaf.id = :id_pessoa3";
			TypedQuery<Number> query3 = em.createQuery(consulta3, Number.class);
			query3.setParameter("id_pessoa3", id_p);
			n3 = query.getSingleResult();
			
			String consulta4 = "select count(u) from Usuario u where u.pessoaf.id = :id_pessoa4";
			TypedQuery<Number> query4 = em.createQuery(consulta4, Number.class);
			query4.setParameter("id_pessoa4", id_p);
			n4 = query4.getSingleResult();
			
			if((n1.intValue()<1) && (n2.intValue()<1) && (n3.intValue()<1) && (n4.intValue()<=1)) {
				
				pf = em.find(PessoaFisica.class, id_p);
				
				count = 2;
				
			}else{
				
				count = 3;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao verificar Pessoa Fisica");
		}
		}
		
		try{
			
			if((count == 1) || (count == 3)){
				
				em.getTransaction().begin();
				em.remove(user);
				em.getTransaction().commit();
			}
			
			if(count == 2){
				
				em.getTransaction().begin();
				em.remove(user);
				em.remove(pf);
				em.getTransaction().commit();
			}
			
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao Removero Usuario");
			
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Usuário Removido Com Sucesso!!!");
		return 200;
	}

	public boolean ValidarUsuario(String login, String senha){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		List<Usuario> user_list = new ArrayList<Usuario>();
		
		try{
		
			TypedQuery<Usuario> query = em.createNamedQuery(Usuario.ValidarUsuario, Usuario.class);
			query.setParameter("usuario", login);
			query.setParameter("senha", senha);
			user_list = query.getResultList();
			
			if(user_list.isEmpty()){
				
				System.out.println("Usuario NÃO está Cadastrado no Sistema!! NÃO é possivel Entrar no Sistema!");
				return false;
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao fazer busca no BD");
		
		}finally{
			
			em.close();
			factory.close();
		}
		
		System.out.println("Usuario Validado com Sucesso!!!");
		return true;
	}
}
