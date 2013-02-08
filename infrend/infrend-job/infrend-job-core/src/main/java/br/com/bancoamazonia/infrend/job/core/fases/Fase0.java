package br.com.bancoamazonia.infrend.job.core.fases;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import br.com.bancoamazonia.infrend.job.core.Core;
import br.com.bancoamazonia.infrend.job.core.context.InfrendContext;
import br.com.bancoamazonia.infrend.job.core.util.Util;
import br.com.bancoamazonia.integracao.base.Conexao;
import br.com.bancoamazonia.integracao.opcon.FaseOpcon;
import br.com.bancoamazonia.integracao.opcon.OpconException;
import br.com.bancoamazonia.integracao.util.JobErros;
import br.com.bancoamazonia.integracao.util.MensagensErros;


public class Fase0 extends FaseOpcon {
	
	private Logger log = Logger.getLogger(getClass());

	@Override
	public void executar() throws OpconException {
		try {
			// especificacao da conexao oriunda do arquivo Connection-INFREND.xml 
			Conexao conexao =  getOpconExec().getConexaoFactories().get(0).getConexao("INFRENDCON01");
			// especificacao da conexao oriunda do arquivo modelContext.xml 
			BasicDataSource bds = InfrendContext.getBean(BasicDataSource.class);
			//Connection-INFREND.xml -> modelContext.xml
			bds.setDriverClassName(conexao.getClasseDriver());
			bds.setUrl(conexao.getUrl());
			bds.setUsername(conexao.getUsuario());
			bds.setPassword(conexao.getSenha());
			// nucleo da aplicacao
        	Core core = InfrendContext.getBean(Core.class);
        	core.execute(bds.getConnection());
    	}
		catch(Exception e) {
			e.printStackTrace();
			log.fatal(e);
			int errorCode = JobErros.ERRO_INESPERADO_EXECUCAO_FASE.valor();
			if(Util.isLostConnectionException(e))
				errorCode = JobErros.ERRO_CONEXAO_NAO_ENCONTRADA.valor();
			else if(Util.isConfigurationException(e))
				errorCode = JobErros.ERRO_PROPRIEDADE_SISTEMA_INDEFINIDA.valor();
			throw new OpconException(errorCode, MensagensErros.getInstance().getMensagemErro(errorCode));
		}
	}


}
