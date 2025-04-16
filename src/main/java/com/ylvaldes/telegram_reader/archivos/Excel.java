package com.ylvaldes.telegram_reader.archivos;

import com.ylvaldes.leerpdf.dao.Registro;
import com.ylvaldes.leerpdf.utiles.LoadResourceConfLeerPDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Excel {
	private static final Logger log =  LogManager.getLogger(Excel.class);
	private static final LoadResourceConfLeerPDF recurso = new LoadResourceConfLeerPDF();

	public Excel() {
		try {
			recurso.loadResourceConf();
		}  catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	@SuppressWarnings("resource")
	public void crearExcel(List<Registro> reg, String salida, String mercado, Date fecha) {
		SimpleDateFormat format = new SimpleDateFormat(recurso.getPatternFormatH());
		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
		Workbook libro = new HSSFWorkbook();
		Sheet hoja = libro.createSheet();
		int rowIndex = 0;
		for (Registro registro : reg) {
			Row row = hoja.createRow(rowIndex++);
			Cell cell = row.createCell(0);
			cell.setCellValue((String) registro.getMoneda());

			Cell cell1 = row.createCell(1);
			cell1.setCellValue((String) registro.getCategoria());

			Cell cell2 = row.createCell(2);
			cell2.setCellValue((String) registro.getDescripcion());

			Cell cell3 = row.createCell(3);
			cell3.setCellValue((String) registro.getBeneficiario());

			Cell cell4 = row.createCell(4);
			cell4.setCellValue((String) registro.getDireccion());

			Cell cell5 = row.createCell(5);
			cell5.setCellValue((String) format.format(registro.getFecha()));

			Cell cell6 = row.createCell(6);
			cell6.setCellValue((Double) registro.getImporte());

		}

		try {
			FileOutputStream out = new FileOutputStream(salida + "/" + format2.format(fecha) + "-" + mercado + ".xls");
			libro.write(out);
			out.close();
		} catch (Exception e) {
			log.error("Error al crear el Libro Excel");
			log.error(e.getMessage());
		}

	}
}
