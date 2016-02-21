package br.com.DialiSys.Resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;

import br.com.DialiSys.Manager.UsuarioManager;
import br.com.DialiSys.Util.JavaUtil;

@Path("/usuarios")
public class UsuarioResource {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response CadastrarUsuario(String dados) throws JSONException{
		
		JSONObject obj = new JSONObject(dados);
		UsuarioManager um = new UsuarioManager();
		int cod_http = 0;
		long id = 0;
		
		
		try{
			if(obj.has("numero_cpf")){
				
				cod_http = um.CriarUsuarioComoPessoa(obj.optString("nome"), obj.optString("tel"), obj.optString("email"), 
						obj.optString("sexo"), obj.optString("datanasc"), obj.optString("numero_cpf"), 
						obj.optString("data_emissao_cpf"), obj.optString("rua"), obj.optString("numero"), 
						obj.optString("complemento"), obj.optString("bairro"), obj.optString("cidade"), obj.optString("uf"), 
						obj.optString("pais"), obj.optString("cep"), obj.optString("login"), obj.optString("senha"), 
						obj.optString("ator"));
			}else{
				
				cod_http =  um.CriarUsuario(obj.optString("login"), obj.optString("senha"), obj.optString("ator"));
			}
			
		if(cod_http == 201){
			
			id = UsuarioManager.idGerado;
			
		}
		
		if(cod_http == 409){
			String msg_erro = "Status: 409 Conflict - Usuario ja Existe! Não é possivel Criar um Novo";
			return Response.status(409).entity(msg_erro).build();
		}
		
		if(cod_http == 400){
			String msg_erro = "Status: 400 Bad Request - CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!";
			return Response.status(400).entity(msg_erro).build();
		}
		
			}catch(Exception e){
			e.printStackTrace();
			System.out.println("Erro ao gravar os dados");
			
		}
		
			String msg = "Status: 201 CREATED - Novo Usuario Criado Com Sucesso!";
			System.out.println(msg);
			try{
				return Response.created(new URI(""+id)).build();
				}catch(URISyntaxException e){
					throw new RuntimeException(e);
				}
	}
	
	@GET
	@Produces("application/json")
	public Response BuscarTodos(){
		
		UsuarioManager um = new UsuarioManager();
		String usuarios = null;
		Gson gson = new Gson();
		
		try{
			
			usuarios = gson.toJson(um.BuscarTodosUsuarios());
			
		}catch(Exception ex){
			String erro = "Status: 404 Not Found - Usuarios Não Encontrados no BD";
			System.out.println(erro);
			return Response.status(404).entity(erro).build();
		}
		
		String msg = "Status: 200 OK - Usuario encontrado com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(usuarios).build();
	}
	
	/*@GET
	@Path("{id}")
	@Produces("application/json")
	public Response BuscarUsuarioPorID(@PathParam("id") long id){
		
		UsuarioManager um = new UsuarioManager();
		String usuario = null;
		Gson gson = new Gson();
		
		
		try{
			
			usuario = gson.toJson(um.BuscarUsuarioPorID(id));
		
		}catch(Exception ex){
			String erro = "Status: 404 Not Found - Usuario NÃO encontrado";
			System.out.println(erro);
			return Response.status(404).entity(erro).build();
		}
		
		String msg = "Status: 200 OK - Usuario encontrado com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(usuario).build();
		
	}
	
	@GET
	@Path("login/{login}")
	@Produces("application/json")
	public Response BuscarPorLogin(@PathParam("login") String login){
	
		UsuarioManager um = new UsuarioManager();
		Gson gson = new Gson();
		String usuario = null;
		
		try{
			
			usuario = gson.toJson(um.BuscarUsuarioPorLogin(login));
		
		}catch(Exception ex){
			String erro = "Status: 404 Not Found - Usuario NÃO encontrado";
			System.out.println(erro);
			return Response.status(404).entity(erro).build();
		}
		
		String msg = "Status: 200 OK - Usuario encontrado com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(usuario).build();
	}*/
	
	@GET
	@Path("{param}")
	@Produces("application/json")
	public Response BuscarPorLoginOuID(@PathParam("param") String param){
	
		UsuarioManager um = new UsuarioManager();
		Gson gson = new Gson();
		String usuario = null;
		char[] c = param.toCharArray();  
	    boolean d = true;  
	  
	    for(int i = 0; i < c.length; i++ ){  
	        //verifica se o char não é um dígito  
	        if ( !Character.isDigit( c[ i ] ) ) {  
	            d = false;  
	            break;  
	        }  
	    }
		
		try{
			
			 if(d == true){
				 long id = JavaUtil.ConvertStringToLong(param);
				 usuario = gson.toJson(um.BuscarUsuarioPorID(id));
			    }
			    
			    if(d == false){
			    	usuario = gson.toJson(um.BuscarUsuarioPorLogin(param));
			    }
			
			
		
		}catch(Exception ex){
			String erro = "Status: 404 Not Found - Usuario NÃO encontrado";
			System.out.println(erro);
			return Response.status(404).entity(erro).build();
		}
		
		String msg = "Status: 200 OK - Usuario encontrado com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(usuario).build();
	}
	
	@PUT
	@Path("{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response AtualizarUsuario(@PathParam("id") long id, String dados) throws JSONException{
		
		UsuarioManager um = new UsuarioManager();
		JSONObject obj = new JSONObject(dados);
		int cod_http = 0;
		String json = null;
		
		
		try{
			if(obj.has("numero_cpf")){
				
				cod_http = um.AtualizarUsuarioComoPessoa(obj.optString("user_id"), obj.optString("nome"), 
						obj.optString("tel"), obj.optString("email"), obj.optString("sexo"), obj.optString("datanasc"), 
						obj.optString("numero_cpf"), obj.optString("data_emissao_cpf"), obj.optString("rua"), 
						obj.optString("numero"), obj.optString("complemento"), obj.optString("bairro"), 
						obj.optString("cidade"), obj.optString("uf"), obj.optString("pais"), obj.optString("cep"), 
						obj.optString("login"), obj.optString("senha"), obj.optString("ator"));
			}else{
				
				cod_http = um.AtualizarUsuario(obj.optString("user_id"), obj.optString("login"), obj.optString("senha"), 
						obj.optString("ator"));
			}
				if(cod_http == 200){
					
					Gson gson = new Gson();
					json = gson.toJson(obj);
					
				}
				
				if(cod_http == 404){
					String msg_erro = "Status: 404 Not Found - Usuario Não esta cadastrado no BD!"
							+ "Cadastre antes de Atualizar";
					return Response.status(404).entity(msg_erro).build();
				}
				
				if(cod_http == 400){
					String msg_erro = "Status: 400 Bad Request - CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!";
					return Response.status(400).entity(msg_erro).build();
				}
				
				}catch(Exception ex){
					
					ex.printStackTrace();
					System.out.println("Erro ao atualizar os dados");
					
				}
				
				String msg = "Status: 200 OK - Usuario Atualizado Com Sucesso!";
				System.out.println(msg);
				return Response.status(200).entity(json).build();
	}
	
	
	@DELETE
	@Path("{id}")
	@Produces("application/json")
	public Response RemoverUsuario(@PathParam("id") long id){
		
		UsuarioManager um = new UsuarioManager();
		int cod_http = 0;
		
		try{
			
			cod_http = um.RemoverUsuario(id);
			
			if(cod_http == 404){
				
				String erro = "Status: 404 Not Found - Usuario NÃO pode ser removido pois NÃO foi encontrado";
				System.out.println(erro);
				return Response.status(404).entity(erro).build();
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao Remover Usuario");
		}
		
		String msg = "Status: 200 OK - Usuario Removido com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(msg).build();
	}
	
	@GET
	@Path("entrar")
	@Produces("application/json")
	public Response ValidarUsuario(@QueryParam("login") String login, @QueryParam("senha") String senha) throws JSONException{
		
		UsuarioManager um = new UsuarioManager();
		JSONObject obj = new JSONObject();
		boolean b = false;
		
		try{
			
			b = um.ValidarUsuario(login, senha);
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Status: 500 - Erro ao Validar Usuario");
		}
		
		if(b==false){
			
			String inv = "Inválido";
			String log = "Incorretos";
			String msg = "Não Foi Possível Entrar No Sistema";
			
			obj.putOpt("Usuario", inv);
			obj.putOpt("Login e/ou Senha", log);
			obj.putOpt("Erro", msg);
			
			String erro = "Status: 401 Unauthorized - Usuario Inválido! Login e/ou Senha Incorretos";
			System.out.println(erro);
			return Response.status(401).entity(obj).build();
		}
		
		if(b == true){
			
			String valido = "Validado Com Sucesso";
			String log = "Corretos";
			String msg = "Bem-Vindo ao DialiSys";
			
			obj.putOpt("Usuario", valido);
			obj.putOpt("Login e/ou Senha", log);
			obj.putOpt("OK", msg);
		}
		
		String msg = "Usuario Valido! Bem-Vindo ao DialiSys";
		System.out.println(msg);
		return Response.status(200).entity(obj).build();
	}
}

