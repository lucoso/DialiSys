package br.com.DialiSys.Model;


import java.util.Date;
import java.util.InputMismatchException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class CPF {

	@Column(name="Pessoa_CPF", length=15, unique=true, nullable=true)
	private String numero;
	@Column(name="Pessoa_CPF_DataEmissao", nullable=true)
	@Temporal(value=TemporalType.DATE)
	private Date dataEmissao;

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;	
	}
	public Date getDataEmissao() {
		return dataEmissao;
	}
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public static boolean isCPF(String CPF) {

		if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
				CPF.equals("22222222222") || CPF.equals("33333333333") ||
				CPF.equals("44444444444") || CPF.equals("55555555555") ||
				CPF.equals("66666666666") || CPF.equals("77777777777") ||
				CPF.equals("88888888888") || CPF.equals("99999999999") ||
				(CPF.length() != 11))
			return(false);

		char dig10, dig11;
		int sm, i, r, num, peso;


		try {

			sm = 0;
			peso = 10;
			for (i=0; i<9; i++) {              

				num = (int)(CPF.charAt(i) - 48); 
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)){
				dig10 = '0';

			}else{

				dig10 = (char)(r + 48); 
			}


			sm = 0;
			peso = 11;

			for(i=0; i<10; i++) {
				num = (int)(CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);

			if ((r == 10) || (r == 11)){
				dig11 = '0';

			}else{

				dig11 = (char)(r + 48);
			}


			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))){

				return(true);

			}else{
				return(false);
			}
		}

		catch (InputMismatchException erro) {
			return(false);
		}
	}

	public void ImprimeCPF() {
		System.out.println (this.numero.substring(0, 3) + "." + this.numero.substring(3, 6) + "." +
				this.numero.substring(6, 9) + "-" + this.numero.substring(9, 11));
	}


	public boolean validaCPF(String numero){
		if ((numero != null) && (isCPF(numero))){
			System.out.println("valido");
			return true;
		}else{
			System.out.println("não valido");
			return false;	
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CPF other = (CPF) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
	
	


}
