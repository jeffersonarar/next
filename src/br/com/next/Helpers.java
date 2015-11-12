package br.com.next;

import android.content.Context;
import android.net.ConnectivityManager;

public class Helpers {

	public String pegarId(String s){
		String valor = (String) s.subSequence(0, s.indexOf("-"));
		return valor;
	}
	
	public  boolean verificaConexao(Context contexto) {  
	    boolean conectado;  
	    ConnectivityManager conectivtyManager = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);;  
	    if (conectivtyManager.getActiveNetworkInfo() != null  
	            && conectivtyManager.getActiveNetworkInfo().isAvailable()  
	            && conectivtyManager.getActiveNetworkInfo().isConnected()) {  
	        conectado = true;  
	    } else {  
	        conectado = false;  
	    }  
	    return conectado;  
	}  
}
