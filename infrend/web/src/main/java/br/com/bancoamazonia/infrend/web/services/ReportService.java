package br.com.bancoamazonia.infrend.web.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import br.com.bancoamazonia.infrend.web.util.Util;

public class ReportService {
	
	public class ReportException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;

		public ReportException() {
			super();
		}
		
		public ReportException(String message) {
			super(message);
		}
		
		public ReportException(Throwable e) {
			super(e);
		}
	}
	public InputStream create(Map<String, Object> params) throws ReportException {
		File generatedFile = null;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			
			//ApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext((ServletContext)context.getExternalContext().getContext());
			params.put("image_path", context.getExternalContext().getRealPath("resources/images"));
			
			JasperReport report = JasperCompileManager.compileReport(context.getExternalContext().getResourceAsStream("reports/"+params.get("tipoCliente")+".jrxml"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
			
			generatedFile = new File(context.getExternalContext().getRealPath("reports")
					+File.separator
					+"ir"
					+Util.rawCode((String)params.get("codigo"))
					+".pdf");
			JRExporter arqExport = new JRPdfExporter();
			arqExport.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			arqExport.setParameter(JRExporterParameter.OUTPUT_FILE, generatedFile);
			arqExport.exportReport();
			
			return new FileInputStream(generatedFile);
		} 
		catch (ReportException e) {
			e.printStackTrace();  
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();  
			throw new ReportException(e.getMessage());
		}
		finally {
			if(generatedFile != null)
				generatedFile.deleteOnExit();
		}
	}
}
