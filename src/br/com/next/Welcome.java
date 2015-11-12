package br.com.next;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.MaskFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import br.com.core.Model.Atividade;
import br.com.core.Model.Categoria;
import br.com.core.Model.Conteudo;
import br.com.core.Model.ContratoEstagio;
import br.com.core.Model.Disciplina;
import br.com.core.Model.Matriz;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class Welcome extends Activity {
	private Spinner spinnerDisciplina, spinnerConteudo, spinnerCategoria,
			spinnerAtividade;
	private static final String TAG = "Welcome";
	GerenciadorConexao novaConexao = new GerenciadorConexao();
	EditText data_inicio, data_fim;
	Button btnLogout, salvar;
	String nomeDisciplina, nomeCategoria, nomeConteudo, nomeAtividade;
	JSONObject jsonobject;
	JSONArray jsonarray, jsonarrayMatriz, jsonarraydisciplina,
			jsonarrayconteudo, jsonarraycategoria, jsonarrayatividade;
	JSONObject jsonobjectMatriz, jsonobjectdisciplina, jsonobjectcategoria,
			jsonobjectatividade;
	ProgressDialog mProgressDialog;
	ArrayList<String> disciplinalist, categorialist, conteudolist,
			atividadelist;
	ArrayList<Disciplina> disciplinas;
	ArrayList<Categoria> categorias;
	ArrayList<Conteudo> conteudos;
	ArrayList<Atividade> atividades;
	ArrayList<String> matrizlist;
	ArrayList<Matriz> matrizes;
	ContratoEstagio contratoEstagio = null;
	Context context;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		context = getApplicationContext();
		
		if (extras != null && extras.containsKey("contratoEstagio")) {
			contratoEstagio = (ContratoEstagio) extras
					.getSerializable("contratoEstagio");
		}
		
		setContentView(R.layout.activity_home);
		data_inicio = (EditText) findViewById(R.id.editTextDataInicio);
		data_fim = (EditText) findViewById(R.id.editTextDataFim);
		MaskEditTextChangedListener maskDataInicio = new MaskEditTextChangedListener(
				"##/##/####", data_inicio);
		MaskEditTextChangedListener maskDataFim = new MaskEditTextChangedListener(
				"##/##/####", data_fim);
		data_inicio.addTextChangedListener(maskDataInicio);
		data_fim.addTextChangedListener(maskDataFim);

		new ListDisciplinas().execute();
		salvar = (Button) findViewById(R.id.btnSalvar);
		salvar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean data_inicio_valido = false;
				boolean data_fim_valido = false;
				
				if(data_inicio.getText().toString() != null && !data_inicio.getText().toString().equals("")){
					data_inicio_valido = Validator.validarDatas(data_inicio.getText().toString());
					if (!data_inicio_valido) {
						data_inicio.setError("Data inicio inválida!");
						data_inicio.setFocusable(true);
						data_inicio.requestFocus();
					}
				}
				if(data_fim.getText().toString() != null && !data_fim.getText().toString().equals("")){
					 data_fim_valido = Validator.validarDatas(data_fim.getText().toString());
					if (!data_fim_valido) {
						data_fim.setError("Data fim inválida!");
						data_fim.setFocusable(true);
						data_fim.requestFocus();
					}
				}
				new AssociarAtividades().execute();
		

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
			Uri uri = Uri.parse("http://192.168.0.104:8080/habilis-server/atividades.zul?id="+contratoEstagio.getId());
			 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			 startActivity(intent);
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

	private class ListDisciplinas extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				disciplinas = new ArrayList<Disciplina>();
				disciplinalist = new ArrayList<String>();
				String resposta = null;
				spinnerDisciplina = (Spinner) findViewById(R.id.spinnerDisciplina);
				spinnerConteudo = (Spinner) findViewById(R.id.spinnerConteudo);
				resposta = novaConexao.chamarConexaoDisciplinas("all", null);
				jsonarraydisciplina = new JSONArray(resposta);

				try {
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
						matriz.setQtd_periodo(jsonobjectMatriz
								.optInt("qtd_periodo"));
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
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					Welcome.this, android.R.layout.simple_list_item_1,
					disciplinalist);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerDisciplina.setAdapter(dataAdapter);
			new ListCategorias().execute();
			spinnerDisciplina
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {

							/*
							 * Toast.makeText( parent.getContext(),
							 * "Disciplina selecionada : " +
							 * parent.getItemAtPosition(position) .toString(),
							 * Toast.LENGTH_SHORT).show();
							 */
							nomeDisciplina = parent.getItemAtPosition(position)
									.toString();
							Log.i(TAG, "Item: " + nomeDisciplina);
							new ListConteudo().execute();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}
					});
		}

	}

	private class ListConteudo extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Log.i(TAG, "Item json: " + nomeDisciplina);
			String resposta2 = null;
			try {
				jsonobjectdisciplina = new JSONObject();
				conteudolist = new ArrayList<String>();
				conteudos = new ArrayList<Conteudo>();
				jsonobjectdisciplina.put("nome", nomeDisciplina);

				resposta2 = novaConexao.chamarConexaoConteudo("all",
						jsonobjectdisciplina.toString());

				jsonarrayconteudo = new JSONArray(resposta2);
				try {

					for (int i = 0; i < jsonarrayconteudo.length(); i++) {
						jsonobject = jsonarrayconteudo.getJSONObject(i);
						Conteudo conteudo = new Conteudo();
						conteudo.setNome(jsonobject.getString("nome"));
						conteudo.setAtivo(jsonobject.getBoolean("ativo"));
						conteudo.setId(jsonobject.getLong("id"));
						JSONObject jsondisciplinas = jsonobject
								.getJSONObject("disciplina");

						Disciplina disciplina = new Disciplina();
						disciplina.setId(jsondisciplinas.optInt("id"));
						disciplina.setNome(jsondisciplinas.optString("nome"));
						disciplina
								.setPeriodo(jsondisciplinas.optInt("periodo"));
						disciplina.setCarga_horaria_total(jsondisciplinas
								.optDouble("carga_horaria_total"));
						jsonobjectMatriz = jsondisciplinas
								.getJSONObject("matriz");

						Matriz matriz = new Matriz();
						matriz.setId(jsonobjectMatriz.optInt("id"));
						matriz.setAtivo(jsonobjectMatriz.optBoolean("ativo"));
						matriz.setNome(jsonobjectMatriz.optString("nome"));
						matriz.setQtd_periodo(jsonobjectMatriz
								.optInt("qtd_periodo"));
						disciplina.setMatriz(matriz);

						conteudo.setDisciplina(disciplina);

						conteudos.add(conteudo);
						conteudolist.add(jsonobject.optString("nome"));
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
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					Welcome.this, android.R.layout.simple_list_item_1,
					conteudolist);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerConteudo.setAdapter(dataAdapter);
			spinnerConteudo
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {

							/*
							 * Toast.makeText( parent.getContext(),
							 * "Conteudo selecionado: " +
							 * parent.getItemAtPosition(position) .toString(),
							 * Toast.LENGTH_SHORT).show();
							 */
							nomeConteudo = parent.getItemAtPosition(position)
									.toString();
							/* Log.i(TAG, "Item: " + nomeConteudo); */
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}
					});
		}

	}

	private class ListCategorias extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				categorias = new ArrayList<Categoria>();
				categorialist = new ArrayList<String>();
				String resposta = null;
				spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);

				resposta = novaConexao.chamarConexaoCategorias("all", null);

				jsonarraycategoria = new JSONArray(resposta);

				try {
					for (int i = 0; i < jsonarraycategoria.length(); i++) {
						jsonobjectcategoria = jsonarraycategoria
								.getJSONObject(i);
						Categoria categoria = new Categoria();
						categoria.setId(jsonobjectcategoria.optInt("id"));
						categoria
								.setNome(jsonobjectcategoria.optString("nome"));
						categoria.setDescricao(jsonobjectcategoria
								.optString("descricao"));
						categoria.setAtivo(jsonobjectcategoria
								.optBoolean("ativo"));
						categorias.add(categoria);
						categorialist
								.add(jsonobjectcategoria.optString("nome"));
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
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					Welcome.this, android.R.layout.simple_list_item_1,
					categorialist);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerCategoria.setAdapter(dataAdapter);
			spinnerCategoria
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							int idd = parent.getId();
							/*
							 * Toast.makeText( parent.getContext(),
							 * "Categoria selecionada : " +
							 * parent.getItemAtPosition(position) .toString(),
							 * Toast.LENGTH_SHORT).show();
							 */
							nomeCategoria = parent.getItemAtPosition(position)
									.toString();
							// Log.i(TAG, "Item: " + nomeCategoria);
							new ListAtividade().execute();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}
					});
		}

	}

	private class ListAtividade extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Log.i(TAG, "Item json: " + nomeDisciplina);
			String resposta2 = null;
			try {
				spinnerAtividade = (Spinner) findViewById(R.id.spinnerAtividade);
				jsonobjectatividade = new JSONObject();
				atividadelist = new ArrayList<String>();
				atividades = new ArrayList<Atividade>();
				jsonobjectatividade.put("nome", nomeCategoria);

				resposta2 = novaConexao.chamarConexaoAtividades("all",
						jsonobjectatividade.toString());
				jsonarrayatividade = new JSONArray(resposta2);
				try {

					for (int i = 0; i < jsonarrayatividade.length(); i++) {
						jsonobject = jsonarrayatividade.getJSONObject(i);

						Atividade atividade = new Atividade();
						atividade.setNome(jsonobject.getString("nome"));
						atividade.setAtivo(jsonobject.getBoolean("ativo"));
						atividade.setId(jsonobject.getLong("id"));
						atividade.setNome(jsonobject.getString("nome"));
						atividade
								.setAprovado(jsonobject.getBoolean("aprovado"));
						JSONObject jsoncategorias = jsonobject
								.getJSONObject("categoria");

						Categoria categoria = new Categoria();
						categoria.setId(jsoncategorias.optInt("id"));
						categoria.setNome(jsoncategorias.optString("nome"));
						categoria.setDescricao(jsoncategorias
								.optString("descricao"));
						categoria.setAtivo(jsoncategorias.optBoolean("ativo"));

						atividade.setCategoria(categoria);

						atividades.add(atividade);
						atividadelist.add(jsonobject.optString("nome"));
						// atividadelist.add(jsonobject.opt);
						// atividadelist.add(jsonobject.optInt("id"));
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
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					Welcome.this, android.R.layout.simple_list_item_1,
					atividadelist);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerAtividade.setAdapter(dataAdapter);
			spinnerAtividade
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							/*
							 * Toast.makeText( parent.getContext(),
							 * "Atividade selecionada: " +
							 * parent.getItemAtPosition(position) .toString(),
							 * Toast.LENGTH_SHORT).show();
							 */
							nomeAtividade = parent.getItemAtPosition(position)
									.toString();
							// Log.i(TAG, "Item: " + nomeAtividade);

						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}
					});

		}
	}

	private class AssociarAtividades extends AsyncTask<Void, Void, Void> {
		String respostaSalvar = null;
		@Override
		protected Void doInBackground(Void... params) {
			

			String valorCategoria = (String) spinnerCategoria.getSelectedItem();
			String valorAtividade = (String) spinnerAtividade.getSelectedItem();
			String valorConteudo = (String) spinnerConteudo.getSelectedItem();
			String valorDisciplina = (String) spinnerDisciplina
					.getSelectedItem();

			JSONObject json = new JSONObject();
			try {

				json.put("disciplina", valorDisciplina);
				json.put("atividade", valorAtividade);
				json.put("conteudo", valorConteudo);
				json.put("categoria", valorCategoria);
				json.put("contrato", contratoEstagio.getId());
				json.put("data_inicio", data_inicio.getText().toString());
				json.put("data_fim", data_fim.getText().toString());
				
				respostaSalvar = novaConexao.chamarConexaoAtividadesRealizadas(
						"salvar", json.toString());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			if(respostaSalvar != null){
				Toast.makeText(Welcome.this, respostaSalvar,
						Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(Welcome.this, "Erro ao salvar!",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

}