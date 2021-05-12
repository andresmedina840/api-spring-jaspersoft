package com.javahowtos.jasperdemo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.javahowtos.jasperdemo.beans.SampleBean;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@CrossOrigin("*")
@RestController
@RequestMapping("api/document")
public class JasperDemoController {
	
	@GetMapping(value ="/docx")
    public void getDocument(HttpServletResponse response) throws IOException, JRException {

        String sourceFileName = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "SampleJasperReport.jasper")
            .getAbsolutePath();
        List<SampleBean> dataList = new ArrayList<SampleBean>();
        SampleBean sampleBean = new SampleBean();
        sampleBean.setName("some name");
        sampleBean.setColor("green");
        dataList.add(sampleBean);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        Map<String, Object> parameters = new HashMap<String, Object>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        response.setHeader("Content-Disposition", "attachment;filename=jasperfile.docx");
        response.setContentType("application/octet-stream");
        exporter.exportReport();
    }

	@GetMapping(value = "/pdf")
	public void getDocumentPdf(HttpServletResponse response) throws IOException, JRException {

		String sourceFileName = ResourceUtils
				.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "SampleJasperTemplate.jasper").getAbsolutePath();
		List<SampleBean> dataList = new ArrayList<SampleBean>();
		SampleBean sampleBean = new SampleBean();
		sampleBean.setName("some name");
		sampleBean.setColor("red");
		dataList.add(sampleBean);
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");
	}
	

	
	@GetMapping("/xlsx")
    public void getDocumentExcel(HttpServletResponse response) throws IOException, JRException {

        String sourceFileName = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "SampleJasperReport.jasper")
            .getAbsolutePath();
        List<SampleBean> dataList = new ArrayList<SampleBean>();
        SampleBean sampleBean = new SampleBean();
        sampleBean.setName("some name");
        sampleBean.setColor("red");
        dataList.add(sampleBean);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        Map<String, Object> parameters = new HashMap<String, Object>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
        JRXlsxExporter exporter = new JRXlsxExporter();
        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setSheetNames(new String[] { "Hoja 1" });
        exporter.setConfiguration(reportConfigXLS);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        response.setHeader("Content-Disposition", "attachment;filename=jasperfile.xlsx");
        response.setContentType("application/octet-stream");
        exporter.exportReport();
    }
}
