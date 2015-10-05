package br.com.next;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
 
public class MainActivity extends Activity {
    EditText uname, password;
    Button submit;
 // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    JSONObject json;
    private static String url_login = "http://192.168.0.107:8080/habilis-server/estagiarios/login";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewsById();
        submit.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View arg0) {
                new Login().execute();
                 
            }
        });
    }
    private void findViewsById() {
 
        uname = (EditText) findViewById(R.id.txtUser);
        password = (EditText) findViewById(R.id.txtPass);
        submit = (Button) findViewById(R.id.button1);
    }
    private class Login extends AsyncTask<String, String, String>{
 
   
		@Override
        protected String doInBackground(String... args) {
			String resposta = null;
            String cpf = uname.getText().toString();
            String senha = password.getText().toString();                
 
             try {
				
				 JSONObject json = new JSONObject();
				 json.put("senha", senha);
	             json.put("cpf", cpf);
	             String param = json.toString();
	             resposta = JSONParser.makePOST(url_login, "POST", param);
	             System.out.println(resposta);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
            
            String s=null;      

			if(resposta != null && !resposta.equals("")){
			Intent login = new Intent(getApplicationContext(), Welcome.class);
			                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			                startActivity(login);                    
			                finish();
			} 
              
            return null;
        }
         
    }
 
   
}