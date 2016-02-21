package br.com.DialiSys.Util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.DialiSys.Model.Ator;

public class JavaUtil {
	
	public static Date ConvertStringToDate(String data) throws ParseException{
		
		if((data == null) || (data.equals(""))){
			return null;
		}
		
		Date data_convertida = null;
		
		try{
			
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		data_convertida = (Date)formatter.parse(data);
		
		}catch(ParseException pe){
			
			throw pe;
		}
		
		return data_convertida;
	}
	
	public static Boolean ConvertStringToBoolean(String bool){
		
		if((bool == null) || (bool.equals(""))){
			return null;
		}
		
		Boolean bool_convertido = null;
		
		try{
			
			bool_convertido = Boolean.parseBoolean(bool);
		
		}catch(Exception ex){
			
			throw ex;
		}
		
		return bool_convertido;
	}
	
	public static long ConvertStringToLong(String l){
		
		long l_convertido = 0;
		
		if((l == null) || (l.equals(""))){
			return 0;
		}
		
		try{
		
			l_convertido = Long.parseLong(l);
		
		}catch(Exception ex){
			
			ex.printStackTrace();
		}
		
		return l_convertido;
	}
	
	public static Ator ConvertStringToAtor(String a){
		
		Ator ator_convertido = null;
		
		if((a == null) || (a.equals(""))){
			return null;
		}
		
		try{
			
			ator_convertido = Ator.valueOf(a);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return ator_convertido;
		
	}
	
	public static BigDecimal ConvertStringToBigDecimal(String b){
		
		BigDecimal big_convertido = null;
		
		if((b == null) || (b.equals(""))){
			return null;
		}
		
		try{
			
			big_convertido = new BigDecimal(b);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return big_convertido;
	}
	
	public static Date ConvertStringToHora(String h){
		
		Date hora_convertida = null;
		
		if((h == null) || (h.equals(""))){
			return null;
		}
		
		try{
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  
		    hora_convertida = sdf.parse(h);
		    
		} catch (ParseException e) {  
		    e.printStackTrace();  
		}  

		return hora_convertida;
	}
	
	public static int ConvertStringToInt(String i){
		
		int inteiro_convertido = 0;
		
		if((i == null) || (i.equals(""))){
			return 0;
		}
		
		try{
			
			inteiro_convertido = Integer.parseInt(i);
		    
		} catch (Exception ex) {  
		    ex.printStackTrace();  
		}  

		return inteiro_convertido;
	}
	
	public static int CalcularIdade(Date datanascimento){
		
		int idade = 0;
		Calendar dataAtual = Calendar.getInstance();
		Calendar dataNasc = new GregorianCalendar();
		dataNasc.setTime(datanascimento);
		
		try{
		
			idade = dataAtual.get(Calendar.YEAR) - dataNasc.get(Calendar.YEAR);
			
			dataNasc.add(Calendar.YEAR, idade);
			if(dataAtual.before(dataNasc)){
				
				idade--;
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao calcular a Idade");
		}
		
		return idade;
	}

}
