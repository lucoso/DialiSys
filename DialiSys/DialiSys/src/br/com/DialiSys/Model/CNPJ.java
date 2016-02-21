package br.com.DialiSys.Model;
	
import java.util.Date;
import java.util.InputMismatchException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class CNPJ {

	@Column(name="Empresa_CNPJ", length=17, unique=true, nullable=true)
	private String numero;
	
	@Column(name="Empresa_CNPJ_DataEmisssao", nullable=true)
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


	public static boolean isCNPJ(String CNPJ) {

		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
				CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
				CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
				CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
				CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
				(CNPJ.length() != 14))
			return(false);

		char dig13, dig14;
		int sm, i, r, num, peso;


		try {

			sm = 0;
			peso = 2;

			for (i=11; i>=0; i--) {

				num = (int)(CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;

				if (peso == 10){
					peso = 2;
				}
			}

			r = sm % 11;

			if ((r == 0) || (r == 1)){
				dig13 = '0';

			}else{ 

				dig13 = (char)((11-r) + 48);
			}


			sm = 0;
			peso = 2;

			for (i=12; i>=0; i--) {
				num = (int)(CNPJ.charAt(i)- 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;

			if ((r == 0) || (r == 1)){
				dig14 = '0';

			}else{

				dig14 = (char)((11-r) + 48);

			}


			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13))){
				return(true);

			}else{ 
				return(false);
			}

		} catch (InputMismatchException erro) {
			return(false);
		}
	}


	public void ImprimeCNPJ(){

		System.out.println(this.numero.substring(0, 2) + "." + this.numero.substring(2, 5) + "." +
				this.numero.substring(5, 8) + "/" + this.numero.substring(8, 12) + "-" +
				this.numero.substring(12, 14));
	}



	public boolean validaCNPJ(String numero){
		if ((numero != null) && (isCNPJ(numero))){
			System.out.println("Válido");
			return true;
		}else{
			System.out.println("NÃO Válido");
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
		CNPJ other = (CNPJ) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	

}
