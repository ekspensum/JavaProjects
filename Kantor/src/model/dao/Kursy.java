package model.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Kursy implements Runnable {

	private double pln_usd;
	private double pln_eur;
	private double pln_chf;
	private double eur_usd;
	private double chf_usd;
	private double eur_chf;
	
	private double pln_usd_Ask;
	private double pln_usd_Bid;
	private double pln_eur_Ask;
	private double pln_eur_Bid;
	private double pln_chf_Ask;
	private double pln_chf_Bid;
	
	private ObslugaBD bd;
	private KoszykDaneWalut kdw;

	public Kursy() {
		bd = new ObslugaBD();
		kdw = bd.daneBidAskWalut();
	}

	public double getPln_usd() {
		return pln_usd;
	}

	public double getPln_eur() {
		return pln_eur;
	}

	public double getPln_chf() {
		return pln_chf;
	}

	public double getEur_usd() {
		return eur_usd;
	}

	public double getChf_usd() {
		return chf_usd;
	}

	public double getEur_chf() {
		return eur_chf;
	}

	public double getPln_usd_Ask() {
		return pln_usd_Ask;
	}

	public double getPln_usd_Bid() {
		return pln_usd_Bid;
	}

	public double getPln_eur_Ask() {
		return pln_eur_Ask;
	}


	public double getPln_eur_Bid() {
		return pln_eur_Bid;
	}

	public double getPln_chf_Ask() {
		return pln_chf_Ask;
	}

	public double getPln_chf_Bid() {
		return pln_chf_Bid;
	}

	private Document getKursyNBP() {
		try {
			URL url = new URL("http://api.nbp.pl/api/exchangerates/tables/a/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbFactory.newDocumentBuilder();
			Document doc = db.parse(conn.getInputStream());
			return doc;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject getKursyECB() {
		try {
			URL url = new URL("https://api.exchangeratesapi.io/latest");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String sj = br.readLine();
			JSONObject ob = new JSONObject(sj);
			JSONObject rates = ob.getJSONObject("rates");
			return rates;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//	Aktulizacja danych odbywa si� co 60 min. Dost�pnych jest wiele walut w tym PLN
	private JSONObject getKursyFreeApi() {
		try {
			URL url = new URL("http://free.currencyconverterapi.com/api/v5/convert?q=CHF_USD&compact=y");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String sj = br.readLine();
			JSONObject ob = new JSONObject(sj);
			JSONObject rates = ob.getJSONObject("CHF_USD");
			return rates;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

//Przekazanie do osobnego wątku
@Override
public void run() {
	try {
//		JSONObject obECB = getKursyECB();
//		JSONObject obFreeApi = getKursyFreeApi();
		this.pln_usd = 1 / Double.valueOf(getKursyNBP().getElementsByTagName("Mid").item(1).getTextContent());
//		this.pln_usd = 1/3.7;
		this.pln_eur = 1 / Double.valueOf(getKursyNBP().getElementsByTagName("Mid").item(7).getTextContent());
//		this.pln_eur = 1/4.3;
		this.pln_chf = 1 / Double.valueOf(getKursyNBP().getElementsByTagName("Mid").item(9).getTextContent());
//		this.pln_chf = 1/3.67;
		
//		for asynchronous transfer
		this.pln_usd_Ask = Math.round(Double.valueOf(getKursyNBP().getElementsByTagName("Mid").item(1).getTextContent()) * kdw.getDolarAsk() * 10000) * 0.0001;
		this.pln_usd_Bid = Math.round(Double.valueOf(getKursyNBP().getElementsByTagName("Mid").item(1).getTextContent()) * kdw.getDolarBid() * 10000) * 0.0001;
		this.pln_eur_Ask = Math.round(Double.valueOf(getKursyNBP().getElementsByTagName("Mid").item(7).getTextContent()) * kdw.getEuroAsk() * 10000) * 0.0001;
		this.pln_eur_Bid = Math.round(Double.valueOf(getKursyNBP().getElementsByTagName("Mid").item(7).getTextContent()) * kdw.getEuroBid() * 10000) * 0.0001;
		this.pln_chf_Ask = Math.round(Double.valueOf(getKursyNBP().getElementsByTagName("Mid").item(9).getTextContent()) * kdw.getFrankAsk() * 10000) * 0.0001;
		this.pln_chf_Bid = Math.round(Double.valueOf(getKursyNBP().getElementsByTagName("Mid").item(9).getTextContent()) * kdw.getFrankBid() * 10000) * 0.0001;
		
//		if (obECB != null)
//			this.eur_usd = obECB.getDouble("USD");
//		this.eur_usd = 1.2234;
//		if (obFreeApi != null)
//			this.chf_usd = obFreeApi.getDouble("val");
//		this.chf_usd = 0.99;
//		if (obECB != null)
//			this.eur_chf = obECB.getDouble("CHF");
//		this.eur_chf = 1.1798;
	} catch (NumberFormatException | DOMException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

}
