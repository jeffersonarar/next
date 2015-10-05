package br.com.next;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.core.Model.Disciplina;
import br.com.core.Model.Matriz;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Welcome extends Activity{
	private Spinner spinnerDisciplina, spinnerConteudo, spinnerCategoria,
			spinnerAtividade;
	private static final String TAG = "Welcome";
	
	Button btnLogout;
	String nomeDisciplina;
	JSONObject jsonobject;
	JSONArray jsonarray, jsonarraydisciplina, jsonarrayconteudo;
	JSONObject jsonobjectMatriz, jsonobjectdisciplina;
	JSONArray jsonarrayMatriz;
	ProgressDialog mProgressDialog;
	ArrayList<String> disciplinalist;
	ArrayList<Disciplina> disciplinas;
	ArrayList<String> matrizlist;
	ArrayList<Matriz> matrizes;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Context context = getApplicationContext();
		CharSequence text = "Você está logado!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		setContentView(R.layout.activity_home);
		new ListDisciplinas().execute();
		spinnerDisciplina.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				int idd = parent.getId();
				Toast.makeText(parent.getContext(), 
						"Disciplina selecionada : " + parent.getItemAtPosition(position).toString(),
						Toast.LENGTH_SHORT).show();
				nomeDisciplina = parent.getItemAtPosition(position).toString();
				Log.i(TAG, "Item: "+nomeDisciplina);
				
				
				new ListConteudo().execute();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_ajuda) {
			return true;
		}
		if (id == R.id.action_atividades) {
			return true;
		}
		if (id == R.id.action_dados) {
			return true;
		}
		if (id == R.id.action_sair) {
			Intent login = new Intent(getApplicationContext(),
					MainActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class ListDisciplinas extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
				disciplinas = new ArrayList<Disciplina>();
				disciplinalist = new ArrayList<String>();
				String resposta = null;
				spinnerDisciplina = (Spinner) findViewById(R.id.spinnerDisciplina);
				spinnerConteudo = (Spinner) findViewById(R.id.spinnerConteudo);
				@SuppressWarnings("unused")
				JSONParser jsonParser = new JSONParser();

				resposta = JSONParser.makePOST(
						"http://192.168.0.107:8080/habilis-server/disciplina/all",
						"POST", null);
				jsonarraydisciplina = new JSONArray(resposta);

				// List<Disciplina> list = new ArrayList<Disciplina>();

				try {
					// Locate the NodeList name
					// jsonarray = jsonobject.getJSONArray("displina");
					for (int i = 0; i < jsonarraydisciplina.length(); i++) {
						jsonobject = jsonarraydisciplina.getJSONObject(i);
						Disciplina disciplina = new Disciplina();
						disciplina.setId(jsonobject.optInt("id"));
						disciplina.setNome(jsonobject.optString("nome"));
						disciplina.setPeriodo(jsonobject.optInt("periodo"));
						jsonobjectMatriz = jsonobject.getJSONObject("matriz");

						Matriz matriz = new Matriz();
						matriz.setId(jsonobjectMatriz.optInt("id"));
						matriz.setAtivo(jsonobjectMatriz.optBoolean("ativo"));
						matriz.setNome(jsonobjectMatriz.optString("nome"));
						matriz.setQtd_periodo(jsonobjectMatriz.optInt("qtd_periodo"));
						disciplina.setMatriz(matriz);
						disciplina.setCarga_horaria_total(jsonobject
								.optDouble("carga_horaria_total"));
						disciplina.setAtivo(jsonobject.optBoolean("ativo"));
						disciplinas.add(disciplina);
						disciplinalist.add(jsonobject.optString("nome"));
						}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			return null;
		}
		
		protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Welcome.this,
    				android.R.layout.simple_list_item_1, disciplinalist);
    		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		spinnerDisciplina.setAdapter(dataAdapter);
        }



	}
	
	private class ListConteudo extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			Log.i(TAG,"Item json: "+nomeDisciplina);
			String resposta2 = null;
			try {
				jsonobjectdisciplina.put("nome", nomeDisciplina);
				JSONParser jsonParser = new JSONParser();

				resposta2 = JSONParser.makePOST(
						"http://192.168.0.107:8080/habilis-server/disciplina/all",
						"POST", null);
				jsonarrayconteudo = new JSONArray(resposta2);
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			return null;
		}
		
		protected void onPostExecute(Void result) {
            super.onPostExecute(result);
    	
        }
		
	}
	
	


}