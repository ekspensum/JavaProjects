package model.encje;

import java.time.LocalDateTime;

public class DaneDolar {
	
	private int idDolar;
	private String znak;
	private double bid;
	private double ask;
	private LocalDateTime dataDodania;
	private int idOperator;
	private String imieOperatora;
	private String nazwiskoOperatora;
	
	public int getIdDolar() {
		return idDolar;
	}
	public void setIdDolar(int idDolar) {
		this.idDolar = idDolar;
	}
	public String getZnak() {
		return znak;
	}
	public void setZnak(String znak) {
		this.znak = znak;
	}
	public double getBid() {
		return bid;
	}
	public void setBid(double bid) {
		this.bid = bid;
	}
	public double getAsk() {
		return ask;
	}
	public void setAsk(double ask) {
		this.ask = ask;
	}
	public LocalDateTime getDataDodania() {
		return dataDodania;
	}
	public void setDataDodania(LocalDateTime dataDodania) {
		this.dataDodania = dataDodania;
	}
	public int getIdOperator() {
		return idOperator;
	}
	public void setIdOperator(int idOperator) {
		this.idOperator = idOperator;
	}
	public String getImieOperatora() {
		return imieOperatora;
	}
	public void setImieOperatora(String imieOperatora) {
		this.imieOperatora = imieOperatora;
	}
	public String getNazwiskoOperatora() {
		return nazwiskoOperatora;
	}
	public void setNazwiskoOperatora(String nazwiskoOperatora) {
		this.nazwiskoOperatora = nazwiskoOperatora;
	}
		
}
