package br.com.bancoamazonia.infrend.job.core;

import java.io.File;
import java.io.FileFilter;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.log4j.Logger;

import br.com.bancoamazonia.infrend.job.core.util.Util;
import br.com.bancoamazonia.infrend.modelo.ArquivoProcessado;
import br.com.bancoamazonia.infrend.modelo.Propriedade;
import br.com.bancoamazonia.infrend.modelo.SpecArquivo;
import br.com.bancoamazonia.infrend.service.ArquivoProcessadoService;
import br.com.bancoamazonia.infrend.service.PropriedadeService;
import br.com.bancoamazonia.infrend.service.SpecArquivoService;

public class Core {
	
	private Logger log = Logger.getLogger(getClass());
	
	private SpecArquivoService specArquivoService;
	private ArquivoProcessadoService arqProcService;
	private PropriedadeService propriedadeService;
	public void setSpecArquivoService(SpecArquivoService specArquivoService) {
		this.specArquivoService = specArquivoService;
	}
	public void setArqProcService(ArquivoProcessadoService arqProcService) {
		this.arqProcService = arqProcService;
	}
	public void setPropriedadeService(PropriedadeService propriedadeService) {
		this.propriedadeService = propriedadeService;
	}
	
	public void execute(Connection conn) throws Exception {
		log.info("Iniciando processamento dos arquivos...");
		try {
			CallableStatement fileReaderCall = conn.prepareCall("EXEC usp_FileReader(?)");
			CallableStatement procRegArqCall = conn.prepareCall("EXEC usp_ProcessaRegistroArquivo(?, ?, ?)");
			CallableStatement deleteRegistroCall = conn.prepareCall("EXEC usp_DeleteRegistroArquivo()");
			
			for(SpecArquivo specArq : specArquivoService.list()) {
				// lista os arquivos do diretorio pelo padrao no nome
				File dir = new File(specArq.getDirOrigem());
				FileFilter fileFilter = new RegexFileFilter(specArq.getPattern());
				File[] files = dir.listFiles(fileFilter);
				
				if(files == null || files.length == 0) 
					log.warn("Arquivo nao localizado em:" + specArq.getDirOrigem() + 
							"; ID_SPEC_ARQUIVO: " + specArq.getId());
				// reprocessa
				else {
					for(File file : files) {
						// carrega propriedades relativas aos registros
						Map<String, List<Propriedade>> props = propriedadeService.toLayoutMap(specArq);
						Map<String, String> fileProperty = propriedadeService.toMap(file.getName(), props.get("nome_arquivo"));
						Integer ano = Integer.parseInt(fileProperty.get("ano"));
						Integer mesFim = Integer.parseInt(fileProperty.get("mes_fim"));
						
						// instancia um calendar com a data calendario do arquivo.
						Calendar calendar = Calendar.getInstance();
						calendar.set(ano, mesFim-1, 31);
						
						// PROCESSADO_OK
						BigInteger statusId = new BigInteger("2");
						ArquivoProcessado arqProc = arqProcService.findByNomeAndSpecArquivo(file.getName(), specArq, calendar.getTime());
						if (arqProc == null) {
							arqProc = arqProcService.insert(file.getName(), specArq, calendar.getTime());
							log.info("Processando arquivo " + file.getAbsolutePath() + "...");
						}
						// deleta ocorrenias para reprocessar arquivo
						else {
							deleteFromOcorrencia(conn, ano, specArq);
							log.info("Reprocessando arquivo " + file.getAbsolutePath() + "...");
						}
						
						// le o arquivo
						fileReaderCall.setString(1, file.getAbsolutePath());
						fileReaderCall.execute();
						
						// processa o arquivo
						procRegArqCall.setInt(1, specArq.getId().intValue());
						procRegArqCall.setInt(2, ano);
						procRegArqCall.setInt(3, mesFim);
						procRegArqCall.execute();
						
						// deleta registros temporarios processados
						deleteRegistroCall.execute();
						
						// atualiza arquivo apos alterar status
						arqProcService.update(arqProc, statusId);
						// upload e delecao somente realizados caso nao ocorra nenhuma exception
						FileUtils.copyFile(file, new File(specArq.getDirDestino()+File.separator+file.getName()+"."+mesFim + "" +ano), false);
						FileUtils.deleteQuietly(file);
						log.info(file.getAbsolutePath() + " processado com status " + arqProc.getStatus().getDescricao());
					}
				}
			}
			fileReaderCall.close();
			procRegArqCall.close();
			deleteRegistroCall.close();
		} 
		catch(Exception e) {
			// exceptions de banco da dados
			if(Util.isLostConnectionException(e)) {
				log.fatal("Erro na conexao: ", e);
				throw e;
			}
			else if(Util.isConfigurationException(e)) {
				log.fatal("Erro na configuracao: ", e);
				throw e;
			}
			log.fatal("Erro Fatal", e);
		}
		log.info("Processamento dos arquivos finalizado!");
	}
	
	private void deleteFromOcorrencia(Connection conn, Integer ano, SpecArquivo specArq) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("delete from ocorrencia_" + specArq.getTipo().getDescricao() + " where an_ocorrencia = ? and id_operacao = ?");
		stmt.setInt(1, ano);
		stmt.setInt(2, specArq.getOperacao().getId().intValue());
		
		stmt.executeUpdate();
	}
	
}
