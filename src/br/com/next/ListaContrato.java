package br.com.next;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.core.Model.ContratoEstagio;
import br.com.core.Model.Empresa;
import br.com.core.Model.Estagiario;
import br.com.core.Model.Matriz;

public class ListaContrato extends Activity {

		JSONArray jsonArrayContrato;
		JSONObject jsonObjectContrato, jsonObjectEstagiario, jsonObjectEmpresa, matrizObject;
		ArrayList<String> contratoList;
		ArrayList<ContratoEstagio> contratos;
		ListView listViewContrato;
		Estagiario estagiario = null;
		Empresa empresa  = null;
		GerenciadorConexao novaConexao = new GerenciadorConexao();
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Context context = getApplicationContext();
	    	Bundle extras = getIntent().getExtras();
			
			if(extras != null && extras.containsKey("estagiario")) {
			   estagiario = (Estagiario) extras.getSerializable("estagiario");
			}
			CharSequence text = estagiario.getNome().toString()+ " você está logado!";
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
					//String param = ;

					resposta = novaConexao.chamarConexaoContratoEstagio("all", json.toString());
					
					jsonArrayContrato = new JSONArray(resposta);
					try {
						// Locate the NodeList name
						// jsonarray = jsonobject.getJSONArray("displina");
						for (int i = 0; i < jsonArrayContrato.length(); i++) {
							jsonObjectContrato = jsonArrayContrato.getJSONObject(i);
							ContratoEstagio contratoEstagio = new ContratoEstagio();
							contratoEstagio.setId(jsonObjectContrato.getLong("id"));
						//	contratoEstagio.setData_inicio((Date) jsonObjectContrato.get("data_inicio"));
						//	contratoEstagio.setData_fim(jsonObjectContrato.);
							contratoEstagio.setAtivo(jsonObjectContrato.optBoolean("ativo"));
							
							jsonObjectEstagiario = jsonObjectContrato.getJSONObject("estagiario");
							
							estagiario = new Estagiario();
						    estagiario.setAtivo(jsonObjectEstagiario.getBoolean("ativo"));
							estagiario.setCpf(jsonObjectEstagiario.getString("cpf"));
							estagiario.setPeriodo(jsonObjectEstagiario.getInt("periodo"));
							estagiario.setEmail(jsonObjectEstagiario.getString("email"));
							estagiario.setNome(jsonObjectEstagiario.getString("nome"));
							estagiario.setId(jsonObjectEstagiario.getInt("id"));
							
							matrizObject = jsonObjectEstagiario.getJSONObject("matriz");
							Matriz matriz = new Matriz();
							matriz.setId(matrizObject.optInt("id"));
							matriz.setAtivo(matrizObject.optBoolean("ativo"));
							matriz.setNome(matrizObject.optString("nome"));
							matriz.setQtd_periodo(matrizObject.optInt("qtd_periodo"));
							estagiario.setMatriz(matriz);
							contratoEstagio.setEstagiario(estagiario);
							
							jsonObjectEmpresa = jsonObjectContrato.getJSONObject("empresa");
							
							empresa = new Empresa();
							
							empresa.setId(jsonObjectEmpresa.getLong("id"));
							empresa.setCnpj(jsonObjectEmpresa.optString("cnpj"));
							empresa.setNome(jsonObjectEmpresa.optString("nome"));
							empresa.setAtivo(jsonObjectEmpresa.optBoolean("ativo"));
							contratoEstagio.setEmpresa(empresa);
							
							contratos.add(contratoEstagio);
							contratoList.add(jsonObjectEmpresa.optString("nome"));

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
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ListaContrato.this, android.R.layout.simple_list_item_1,
						contratoList);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_list_item_1);
				listViewContrato.setAdapter(dataAdapter);
			}

		}
  
}