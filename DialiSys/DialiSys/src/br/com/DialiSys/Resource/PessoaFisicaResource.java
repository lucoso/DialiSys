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
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.DialiSys.Manager.PessoaFisicaManager;

import com.google.gson.Gson;

@Path("/pessoas")
public class PessoaFisicaResource {

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response CadastrarPessoaFisica(String dados) throws JSONException{
		
		PessoaFisicaManager pfm = new PessoaFisicaManager();
		JSONObject obj = new JSONObject(dados);
		int cod_http;
		long id = 0;
		
		try{
			
			cod_http = pfm.CadastrarPessoaFisica(obj.optString("nome"), obj.optString("tel"), obj.optString("email"), 
					obj.optString("sexo"), obj.optString("datanasc"), obj.optString("numero_cpf"), obj.optString("data_emissao_cpf"), 
					obj.optString("rua"), obj.optString("numero"), obj.optString("complemento"), obj.optString("bairro"), 
					obj.optString("cidade"), obj.optString("uf"), obj.optString("pais"), obj.optString("cep"));
			
			if(cod_http == 409){
				
				String erro = "Status: 409 Conflict - Pessoa Fisica ja existe!";
				return Response.status(409).entity(erro).build();
			}
			
			if(cod_http == 400){
				String msg_erro = "Status: 400 Bad Request - CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!";
				return Response.status(400).entity(msg_erro).build();
			}
			
			if(cod_http == 201){
				
				id = PessoaFisicaManager.IDGerado;
				
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao gravar os dados");
		}
		
		System.out.println("Status: 201 CREATED - Pessoa Fisica Cadastrada Com Sucesso!");
		try{
			return Response.created(new URI(""+id)).build();
			}catch(URISyntaxException e){
				throw new RuntimeException(e);
			}
	}
	
	@GET
	@Produces("application/json")
	public Response BuscarTodos(){
		
		Gson gson = new Gson();
		PessoaFisicaManager pfm = new PessoaFisicaManager();
		String pessoas;
		
		try{
			
			pessoas = gson.toJson(pfm.BuscarTodosPessoasFisicas());
		
		}catch(Exception ex){
			
			String msgErro = "Status: 404 Not Found - NÃO existe nenhuma Pessoa cadastrada no BD!";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		
		String msg = "Status: 200 OK - Busca encontrada com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(pessoas).build();
		}
	
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response BuscarPorID(@PathParam("id") long id){
		
		Gson gson = new Gson();
		PessoaFisicaManager pfm = new PessoaFisicaManager();
		String pessoa;
		
		try{
			
			pessoa = gson.toJson(pfm.BuscarPorID(id));
		
		}catch(Exception ex){
			
			String msgErro = "Status: 404 Not Found - NÃO existe nenhuma Pessoa cadastrada no BD!";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		
		String msg = "Status: 200 OK - Busca encontrada com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(pessoa).build();
		}

	
	@GET
	@Path("cpf/{cpf}")
	@Produces("application/json")
	public Response BuscarPorCPF(@PathParam("cpf") String cpf_numero){
		
		Gson gson = new Gson();
		PessoaFisicaManager pfm = new PessoaFisicaManager();
		String pessoa;
		
		try{
			
			pessoa = gson.toJson(pfm.BuscarPorCPF(cpf_numero));
		
		}catch(Exception ex){
			
			String msgErro = "Status: 404 Not Found - NÃO existe nenhuma Pessoa cadastrada no BD!";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		
		String msg = "Status: 200 OK - Busca encontrada com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(pessoa).build();
		}
	
	@PUT
	@Path("{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response AtualizarPessoaFisica(@PathParam("id") long id, String dados) throws JSONException{
		
		Gson gson = new Gson();
		PessoaFisicaManager pfm = new PessoaFisicaManager();
		JSONObject obj = new JSONObject(dados);
		int cod_http;
		String json = null;
		
		try{
			
			cod_http = pfm.AtualizarPessoaFisica(obj.optString("id"), obj.optString("nome"), obj.optString("tel"), 
					obj.optString("email"), obj.optString("sexo"), obj.optString("datanasc"), obj.optString("numero_cpf"), 
					obj.optString("data_emissao_cpf"), obj.optString("rua"), obj.optString("numero"), 
					obj.optString("complemento"), obj.optString("bairro"), obj.optString("cidade"), obj.optString("uf"), 
					obj.optString("pais"), obj.optString("cep"));
			
			if(cod_http == 400){
				String msg_erro = "Status: 400 Bad Request - CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!";
				return Response.status(400).entity(msg_erro).build();
			}
			
			if(cod_http == 200){
				
				json = gson.toJson(obj);
				
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao gravar os dados");
		}
		
		System.out.println("Status: 200 OK - Pessoa Fisica Atualizada Com Sucesso!");
		return Response.status(200).entity(json).build();
	}
	
	
	@DELETE
	@Path("{id}")
	public Response RemoverPessoaFisicaPorID(@PathParam("id") long id){
		
		PessoaFisicaManager pfm = new PessoaFisicaManager();
		int cod_http;
		
		try{
			
			cod_http = pfm.RemoverPessoaFisica(id);
			
			if(cod_http == 404){
				String msgErro = "Status: 404 Not Found - Pessoa NÃO Encontrada no BD! NÃO é possivel Remover!";
				return Response.status(404).entity(msgErro).build();
			}
			
			if(cod_http == 400){
				String msg_erro = "Status: 400 Bad Request - NÃO é possivel Remover a Pessoa! "
						+ "Os dados estão sendo usados em outro lugar";
				return Response.status(400).entity(msg_erro).build();
			}
		
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao Remover a Pessoa");
		}
		
		String msg = "Status: 200 OK - Pessoa Removida com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(msg).build();
	}
}
