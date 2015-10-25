package br.com.next;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.core.Model.ContratoEstagio;
import br.com.core.Model.Empresa;
import br.com.core.Model.Estagiario;
import br.com.core.Model.Matriz;

public class ListaContrato extends Activity {

	JSONArray jsonArrayContrato;
	JSONObject jsonObjectContrato, jsonObjectEstagiario, jsonObjectEmpresa,
			matrizObject, jsonContrato;
	ArrayList<String> contratoList;
	ArrayList<ContratoEstagio> contratos;
	ListView listViewContrato;
	Estagiario estagiario = null;
	Empresa empresa = null;
	ContratoEstagio contraEstagio = null;
	String valorId;
	ContratoEstagio contrato = new ContratoEstagio();
	GerenciadorConexao novaConexao = new GerenciadorConexao();
	Helpers help = new Helpers();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Context context = getApplicationContext();
		Bundle extras = getIntent().getExtras();

		if (extras != null && extras.containsKey("estagiario")) {
			estagiario = (Estagiario) extras.getSerializable("estagiario");
		}
		CharSequence text = estagiario.getNome().toString()
				+ " você está logado!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		setContentView(R.layout.activity_contrato);
		new ListContratos().execute();

	}

	private class ListContratos extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				contratos = new ArrayList<ContratoEstagio>();
				contratoList = new ArrayList<String>();
				String resposta = null;
				listViewContrato = (ListView) findViewById(android.R.id.list);

				JSONObject json = new JSONObject();
				json.put("cpf", estagiario.getCpf());
				// String param = ;

				resposta = novaConexao.chamarConexaoContratoEstagio("all",
						json.toString());

				jsonArrayContrato = new JSONArray(resposta);
				try {
					// Locate the NodeList name
					// jsonarray = jsonobject.getJSONArray("displina");
					for (int i = 0; i < jsonArrayContrato.length(); i++) {
						jsonObjectContrato = jsonArrayContrato.getJSONObject(i);
						ContratoEstagio contratoEstagio = new ContratoEstagio();
						contratoEstagio.setId(jsonObjectContrato.getLong("id"));
						// contratoEstagio.setData_inicio((Date)
						// jsonObjectContrato.get("data_inicio"));
						// contratoEstagio.setData_fim(jsonObjectContrato.);
						contratoEstagio.setAtivo(jsonObjectContrato
								.optBoolean("ativo"));

						jsonObjectEstagiario = jsonObjectContrato
								.getJSONObject("estagiario");

						estagiario = new Estagiario();
						estagiario.setAtivo(jsonObjectEstagiario
								.getBoolean("ativo"));
						estagiario
								.setCpf(jsonObjectEstagiario.getString("cpf"));
						estagiario.setPeriodo(jsonObjectEstagiario
								.getInt("periodo"));
						estagiario.setEmail(jsonObjectEstagiario
								.getString("email"));
						estagiario.setNome(jsonObjectEstagiario
								.getString("nome"));
						estagiario.setId(jsonObjectEstagiario.getInt("id"));

						matrizObject = jsonObjectEstagiario
								.getJSONObject("matriz");
						Matriz matriz = new Matriz();
						matriz.setId(matrizObject.optInt("id"));
						matriz.setAtivo(matrizObject.optBoolean("ativo"));
						matriz.setNome(matrizObject.optString("nome"));
						matriz.setQtd_periodo(matrizObject
								.optInt("qtd_periodo"));
						estagiario.setMatriz(matriz);
						contratoEstagio.setEstagiario(estagiario);

						jsonObjectEmpresa = jsonObjectContrato
								.getJSONObject("empresa");

						empresa = new Empresa();

						empresa.setId(jsonObjectEmpresa.getLong("id"));
						empresa.setCnpj(jsonObjectEmpresa.optString("cnpj"));
						empresa.setNome(jsonObjectEmpresa.optString("nome"));
						empresa.setAtivo(jsonObjectEmpresa.optBoolean("ativo"));
						contratoEstagio.setEmpresa(empresa);

						contratos.add(contratoEstagio);
						String array = jsonObjectContrato.getLong("id") + "-" + jsonObjectEmpresa.optString("nome");
						contratoList.add(array);

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			contraEstagio = new ContratoEstagio();
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					ListaContrato.this, android.R.layout.simple_list_item_1,
					contratoList);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_list_item_1);
			listViewContrato.setAdapter(dataAdapter);
			listViewContrato
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView,
								View view, int i, long l) {
 
							Intent login = new Intent(getApplicationContext(),
									Welcome.class);
							valorId = help.pegarId(adapterView.getItemAtPosition(i).toString());
						//	new Contratos().execute();
							contraEstagio.setId(Long.parseLong(valorId));
							login.putExtra("contratoEstagio", contraEstagio);

							login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(login);
							finish();
						}
					});

		}

	}
	
	private class Contratos extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
		
				String resposta = null;


				JSONObject json = new JSONObject();
				json.put("id", valorId);
				// String param = ;

				resposta = novaConexao.chamarConexaoContratoEstagio("id",
						json.toString());

				jsonContrato = new JSONObject(resposta);
				contrato.setId(jsonContrato.getLong("id"));
				contrato.setAtivo(jsonContrato
						.optBoolean("ativo"));

				jsonObjectEstagiario = jsonContrato
						.getJSONObject("estagiario");

				estagiario = new Estagiario();
				estagiario.setAtivo(jsonObjectEstagiario
						.getBoolean("ativo"));
				estagiario
						.setCpf(jsonObjectEstagiario.getString("cpf"));
				estagiario.setPeriodo(jsonObjectEstagiario
						.getInt("periodo"));
				estagiario.setEmail(jsonObjectEstagiario
						.getString("email"));
				estagiario.setNome(jsonObjectEstagiario
						.getString("nome"));
				estagiario.setId(jsonObjectEstagiario.getInt("id"));

				matrizObject = jsonObjectEstagiario
						.getJSONObject("matriz");
				Matriz matriz = new Matriz();
				matriz.setId(matrizObject.optInt("id"));
				matriz.setAtivo(matrizObject.optBoolean("ativo"));
				matriz.setNome(matrizObject.optString("nome"));
				matriz.setQtd_periodo(matrizObject
						.optInt("qtd_periodo"));
				estagiario.setMatriz(matriz);
				contrato.setEstagiario(estagiario);

				jsonObjectEmpresa = jsonObjectContrato
						.getJSONObject("empresa");

				empresa = new Empresa();

				empresa.setId(jsonObjectEmpresa.getLong("id"));
				empresa.setCnpj(jsonObjectEmpresa.optString("cnpj"));
				empresa.setNome(jsonObjectEmpresa.optString("nome"));
				empresa.setAtivo(jsonObjectEmpresa.optBoolean("ativo"));
				contrato.setEmpresa(empresa);

				} catch (Exception e) {
					e.printStackTrace();
				}
			return null;
		}


	}

}