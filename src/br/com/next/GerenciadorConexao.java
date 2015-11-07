package br.com.next;

public class GerenciadorConexao {

	public static final String ENDERECO_SERVER = "http://192.169.2.225:8080/habilis-server";
	public static final String METODO_POST = "POST"; 
	public static final String METODO_GET = "GET"; 
	
	String resposta = null;
	
	public String chamarConexaoDisciplinas(String method, String param) {
		String metodoReq = null;
		String classeServico = "/disciplina/";

		if (method == "all") {
			metodoReq = METODO_POST;
		}
		resposta = JSONParser.makePOST(
				ENDERECO_SERVER + classeServico + method, metodoReq, param);
		return resposta;
	}

	public String chamarConexaoContratoEstagio(String method, String param) {
		String classeServico = "/contrato/";
		String metodoReq = null;
		
		if (method == "all") {
			metodoReq = METODO_POST;
		}
		if (method == "id") {
			metodoReq = METODO_GET;
		}

		resposta = JSONParser.makePOST(ENDERECO_SERVER + classeServico
				+ method, metodoReq, param);
		
		return resposta;
	}

	public String chamarConexaoCategorias(String method, String param) {
		String classeServico = "/categoria/";
		String metodoReq = null;
		
		if (method == "all") {
			metodoReq = METODO_POST;
		}

		resposta = JSONParser.makePOST(ENDERECO_SERVER + classeServico
				+ method, metodoReq, param);
		
		return resposta;
	}

	public String chamarConexaoConteudo(String method, String param) {
		String metodoReq = null;
		String classeServico = "/conteudo/";

		if (method == "all") {
			metodoReq = METODO_POST;
		}
		resposta = JSONParser.makePOST(ENDERECO_SERVER + classeServico
				+ method, metodoReq, param);
		
		return resposta;
	}

	public String chamarConexaoAtividades(String method, String param) {
		String metodoReq = null;
		String classeServico = "/atividade/";

		if (method == "all") {
			metodoReq = METODO_POST;
		}
		resposta = JSONParser.makePOST(ENDERECO_SERVER + classeServico
				+ method, metodoReq, param);
		
		return resposta;
	}

	public String chamarConexaoEstagiario(String method, String param) {
		String classeServico = "/estagiarios/";
		String metodoReq = null;
		
		if (method == "all") {
			metodoReq = METODO_POST;
		}
		if (method == "login") {
			metodoReq = METODO_POST;
		}
		resposta = JSONParser.makePOST(ENDERECO_SERVER + classeServico
				+ method, metodoReq, param);

		return resposta;
	}

	public String chamarConexaoAtividadesRealizadas(String method, String param) {
		String classeServico = "/atividadesrealizadas/";
		String metodoReq = null;
		
		
		if (method == "salvar") {
			metodoReq = METODO_POST;
		}
		resposta = JSONParser.makePOST(ENDERECO_SERVER + classeServico
				+ method, metodoReq, param);
		
		return resposta;
	}

}
