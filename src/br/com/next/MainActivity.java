package br.com.next;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.core.Model.Estagiario;
import br.com.core.Model.Matriz;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class MainActivity extends Activity {
	String resposta = null;
	EditText uname, password;
	Button submit;
	Estagiario estagiario = null;
	Matriz matriz = null;
	JSONParser jParser = new JSONParser();
	GerenciadorConexao novaConexao = new GerenciadorConexao();
	Helpers helpers = new Helpers();
	JSONObject json;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewsById();

		/*
		 * MaskEditTextChangedListener maskCPF = new
		 * MaskEditTextChangedListener( "###.###.###-##", uname);
		 * uname.addTextChangedListener(maskCPF);
		 */

		context = getApplicationContext();
		if (!helpers.verificaConexao(context)) {
			try {
				Toast.makeText(context,
						"Falha ao conectar com banco de dados.",
						Toast.LENGTH_SHORT).show();

				finish();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		submit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Validator.validateNotNull(uname, "Campo usuario é obrigatório!");
				Validator.validateNotNull(password, "Campo senha é obrigatório!");

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

				resposta = novaConexao.chamarConexaoEstagiario("login",
						json.toString());
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
						ListaContrato.class);
				login.putExtra("estagiario", estagiario);
				login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(login);
				finish();
			} else {
				Toast.makeText(context, "Dados de login estão incorretos!",
						Toast.LENGTH_SHORT).show();

			}
		}

	}
}
