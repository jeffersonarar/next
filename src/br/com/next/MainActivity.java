package br.com.next;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.core.Model.ContratoEstagio;
import br.com.core.Model.Estagiario;
import br.com.core.Model.Matriz;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	String resposta = null;
	EditText uname, password;
	Button submit;
	Estagiario estagiario = null;
	Matriz matriz = null;
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	GerenciadorConexao novaConexao = new GerenciadorConexao();

	JSONObject json;
//	private static String url_login = "http://192.168.0.114:8080/habilis-server/estagiarios/login";

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

	private class Login extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... params) {
			String cpf = uname.getText().toString();
			String senha = password.getText().toString();

			try {

				JSONObject json = new JSONObject();
				json.put("senha", senha);
				json.put("cpf", cpf);

				resposta = novaConexao.chamarConexaoEstagiario("login", json.toString());
				System.out.println(resposta);
				if (resposta != null && !resposta.equals("")) {
					JSONObject estagiarioObject = new JSONObject(resposta);
					JSONObject matrizObject = new JSONObject();
					estagiario = new Estagiario();
					// estagiario.setAcesso(estagiarioObject.getBoolean("acesso"));
					estagiario.setAtivo(estagiarioObject.getBoolean("ativo"));
					estagiario.setCpf(estagiarioObject.getString("cpf"));
					estagiario.setPeriodo(estagiarioObject.getInt("periodo"));
					estagiario.setEmail(estagiarioObject.getString("email"));
					estagiario.setNome(estagiarioObject.getString("nome"));
					estagiario.setId(estagiarioObject.getInt("id"));
					matrizObject = estagiarioObject.getJSONObject("matriz");
					Matriz matriz = new Matriz();
					matriz.setId(matrizObject.optInt("id"));
					matriz.setAtivo(matrizObject.optBoolean("ativo"));
					matriz.setNome(matrizObject.optString("nome"));
					matriz.setQtd_periodo(matrizObject.optInt("qtd_periodo"));
					estagiario.setMatriz(matriz);
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (resposta != null && !resposta.equals("")) {
				Intent login = new Intent(getApplicationContext(),
						Welcome.class);
				login.putExtra("estagiario", estagiario);
				login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(login);
				finish();
			}
		}

	}
}
