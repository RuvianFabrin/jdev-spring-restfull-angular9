package com.ru.jdevrestapi.service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceRelatorio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public byte[] gerarRelatorio(String nomeRelatorio, Map<String,Object> params, ServletContext servletContext) throws Exception {
        /* Obter Conexão com o Banco de dados */
        Connection connection = jdbcTemplate.getDataSource().getConnection();

        /* Carregar caminho do arquivo Jasper */
         String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + nomeRelatorio + ".jasper";
        //String caminhoJasper = "C:\\relatorio" + File.separator + nomeRelatorio + ".jasper";

        /* Gerar relatório com os dados e conexão */
        JasperPrint print = JasperFillManager.fillReport(caminhoJasper, params, connection);

        /* Exporta para byte o PDF para fazer o download */
        byte[] retorno = JasperExportManager.exportReportToPdf(print);
        connection.close();
        return retorno;
    }


}
