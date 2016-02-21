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

import br.com.DialiSys.Manager.MedicoManager;

import com.google.gson.Gson;

@Path("/medicos")
public class MedicoResource {

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response CadastrarMedico(String dados) throws JSONException{
		
		MedicoManager mm = new MedicoManager();
		int cod_http = 0;
		JSONObject obj = new JSONObject(dados);
		long id = 0;
		
		try{
			
			cod_http = mm.CadastrarMedico(obj.optString("nome"), obj.optString("tel"), obj.optString("email"), obj.optString("sexo"), 
					obj.optString("datanasc"), obj.optString("numero_cpf"), obj.optString("data_emissao_cpf"), obj.optString("rua"), 
					obj.optString("numero"), obj.optString("complemento"), obj.optString("bairro"), obj.optString("cidade"), 
					obj.optString("uf"), obj.optString("pais"), obj.optString("cep"), obj.optJSONArray("clinicas_ids"), 
					obj.optString("numero_crm"), obj.optString("data_emissao_crm"));
			
			if(cod_http == 201){
				
				id = MedicoManager.idGerado;
			}
		
		if(cod_http == 409){
			String msg_erro = "Status: 409 Conflito! Medico ja EXISTE!";
			return Response.status(409).entity(msg_erro).build();
		}
		
		if(cod_http == 400){
			String msg_erro = "Status: 400 Bad Request - CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!";
			return Response.status(400).entity(msg_erro).build();
		}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Erro ao Gravar os Dados no BD");
			
		}
		
		String msg = "Status: 201 CREATED - Medico Cadastrada Com Sucesso!";
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
		
		Gson gson = new Gson();
		MedicoManager mm = new MedicoManager();
		String json = null;
		
		try{
			json = gson.toJson(mm.BuscarTodosMedicos());
		}catch(Exception ex){
			
			String msgErro = "Status: 404 Not Found - NÃO encontrado no BD!";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		String msg = "Medicos Encontrados com Sucesso";
		System.out.println(msg);
		return Response.status(200).entity(json).build();
	}
	
	@GET
	@Path("{id}/clinicas")
	@Produces("application/json")
	public Response BuscarClinicasDoMedico(@PathParam("id") long id){
		
		Gson gson = new Gson();
		MedicoManager mm = new MedicoManager();
		String clinicas;
		
		try{
			
			clinicas = gson.toJson(mm.BuscarClinicasDosMedicos(id));
		
		}catch(Exception ex){
			
			String msgErro = "Status: 404 Not Found - NÃO encontrado no BD!";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		
		String msg = "Status: 200 OK - Busca encontrada com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(clinicas).build();
	}
	
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response BuscarPorPessoa(@PathParam("id") long id){
		
		Gson gson = new Gson();
		MedicoManager mm = new MedicoManager();
		String medico;
		
		try{
			
			medico = gson.toJson(mm.BuscarMedicoPorPessoa(id));
		
		}catch(Exception ex){
			
			String msgErro = "Status: 404 Not Found - Medico NÃO encontrado no BD!";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		
		String msg = "Status: 200 OK - Busca encontrada com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(medico).build();
	}
	

	@GET
	@Path("crm/{crm}")
	@Produces("application/json")
	public Response BuscarPorCRM(@PathParam("crm") String crm){
		
		Gson gson = new Gson();
		MedicoManager mm = new MedicoManager();
		String medico = null;
		
		try{
			
			medico = gson.toJson(mm.BuscarMedicoPorCRM(crm));
		
		}catch(Exception ex){
			
			String erro = "Status: 404 Not Found - Médico NÃO Encontrado";
			System.out.println(erro);
			return Response.status(404).entity(erro).build();
		}
		
		String msg = "Status: 200 OK - Médico Encontrado Com Sucesso";
		System.out.println(msg);
		return Response.status(200).entity(medico).build();
		
	}
	
	@GET
	@Path("{id}/pacientes")
	@Produces("application/json")
	public Response BuscarPacientesDoMedico(@PathParam("id") long id){
		
		MedicoManager mm = new MedicoManager();
		String pacientes = null;
		Gson gson = new Gson();
		
		try{
		
			pacientes = gson.toJson(mm.BuscarPacientesPorMedico(id)); 
			
		}catch(Exception ex){
			
			String erro = "Status: 404 Not Found - Pacientes Não Encontrados";
			System.out.println(erro);
			return Response.status(404).entity(erro).build();
		}
		
		System.out.println("Status: 200 OK - Pacientes Encontrados Com Sucesso");
		return Response.status(200).entity(pacientes).build();
	}
		
		
	
	@PUT
	@Path("{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response AtualizarMedico(@PathParam("id") long id, String dados) throws JSONException{
		
		MedicoManager mm = new MedicoManager();
		int cod_http = 0;
		JSONObject obj = new JSONObject(dados);
		String json = null;
		Gson gson = new Gson();
	

		try{
			
			cod_http = mm.AtualizarMedico(obj.optString("pessoa_id"), obj.optString("nome"), obj.optString("tel"), 
					obj.optString("email"), obj.optString("sexo"), obj.optString("datanasc"), obj.optString("numero_cpf"), 
					obj.optString("data_emissao_cpf"), obj.optString("rua"), obj.optString("numero"), 
					obj.optString("complemento"), obj.optString("bairro"), obj.optString("cidade"), obj.optString("uf"), 
					obj.optString("pais"), obj.optString("cep"), obj.optJSONArray("clinicas_ids"), 
					obj.optString("numero_crm"), obj.optString("data_emissao_crm"));
			

			if(cod_http == 200){
				
				json = gson.toJson(obj);
				
			}
			
			if(cod_http == 404){
				
				String erro = "Status: 404 Not Found - Medico Não Encontrado no BD! Cadastre antes de Atualizar";
				return Response.status(404).entity(erro).build();
			}
			
			if(cod_http == 400){
				String msg_erro = "Status: 400 Bad Request - CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!";
				return Response.status(400).entity(msg_erro).build();
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao Atualizar os dados");
		}
		
		System.out.println("Medico Atualizado Com Sucesso");
		return Response.status(200).entity(json).build();
	}
	
	
	@DELETE
	@Path("{id}")
	public Response RemoverMedico(@PathParam("id") long id_pessoa){
		
		MedicoManager mm =  new MedicoManager();
		int cod_http = 0;
		
		try{
			
			cod_http = mm.RemoverMeidco(id_pessoa);
			
			if(cod_http == 404){
				
				String erro = "Status: 404 Not Found - Médico NÃO Encontrado! Não é possivel Remover!";
				System.out.println(erro);
				return Response.status(404).entity(erro).build();
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao Remover");
		}
		
		String msg = "Status: 200 OK - Medico Removido Com Sucesso";
		System.out.println(msg);
		return Response.status(200).entity(msg).build();
	}
}

