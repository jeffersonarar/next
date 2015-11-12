package br.com.next;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class Validator {

	public static boolean validateNotNull(View pView, String pMessage) {
		if (pView instanceof EditText) {
			EditText edText = (EditText) pView;
			Editable text = edText.getText();
			if (text != null) {
				String strText = text.toString();
				if (!TextUtils.isEmpty(strText)) {
					return true;
				}
			}
			// em qualquer outra condição é gerado um erro
			edText.setError(pMessage);
			edText.setFocusable(true);
			edText.requestFocus();
			return false;
		}
		return false;
	}

	public final static boolean validateEmail(String txtEmail) {
		if (TextUtils.isEmpty(txtEmail)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(txtEmail)
					.matches();
		}
	}
	
	public static boolean validarDatas(String data){
		boolean retorno = false;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");  
	    try {  
	        System.out.println(df.parse (data));  
	        retorno = true;
	    } catch (ParseException ex) {
	    	retorno = false;
	       System.out.println(ex);  
	    }
		return retorno;  
	}
}