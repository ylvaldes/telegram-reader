package com.ylvaldes.telegram_reader.mercados;

import com.ylvaldes.leerpdf.archivos.Excel;
import com.ylvaldes.leerpdf.archivos.Txt;
import com.ylvaldes.leerpdf.dao.Compra;
import com.ylvaldes.leerpdf.dao.Registro;
import com.ylvaldes.leerpdf.mercados.IMercados;
import com.ylvaldes.leerpdf.utiles.LoadResourceConfLeerPDF;
import com.ylvaldes.leerpdf.utiles.UtilesString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class McDonals implements IMercados {
	private static final Logger log =  LogManager.getLogger(McDonals.class);
	private final static LoadResourceConfLeerPDF recurso = new LoadResourceConfLeerPDF();
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	// Objetos
	Excel excel;
	UtilesString utilString;
	Txt txt;

	// Listas
	List<String> lineasPDF;
	List<Compra> elementos;
	List<Registro> registros;

	// Variables
	String moneda;
	String direccion;
	String rut;
	String eTicket;
	String serie;
	String codSeguridad;
	String datosExtra;
	Date fecha;
	String mercado;
	double totalPagarPDF;
	double totalSuma;
	double ley;

	public McDonals() {
		try {
			// Inicializando Listas
			lineasPDF = new ArrayList<String>();
			elementos = new ArrayList<Compra>();
			registros = new ArrayList<Registro>();

			// Inicializando Variables
			moneda = "";
			mercado = "";
			direccion = "";
			rut = "";
			eTicket = "";
			serie = "";
			codSeguridad = "";
			datosExtra = "";
			totalPagarPDF = 0.0;
			totalSuma = 0.0;
			ley = 0.0;
			fecha = new Date();

			// Inicializando Objetos de Clases
			excel = new Excel();
			utilString = new UtilesString();

			// Carga de Recursos
			recurso.loadResourceConf();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void leerDatos(String filename, String output) {
		try {
			log.debug("Entrada: " + filename);
			txt = new Txt(filename, output);
			mercado = output.substring(11, output.length() - 4);

			lineasPDF = Arrays.asList(txt.crearTxt().split("\r\n"));
			lineasPDF = lineasPDF.subList(1, lineasPDF.size());
			rut = lineasPDF.get(3);
			eTicket = lineasPDF.get(1).trim();
			serie = lineasPDF.get(10).trim();
			log.info("Moneda: " + lineasPDF.get(5).substring(lineasPDF.get(5).length() - 3));
			moneda = lineasPDF.get(5).substring(lineasPDF.get(5).length() - 3);

			Pattern patternCantP = Pattern.compile(
					"^([0-2][0-9]|3[0-1])(\\/|-)(0[1-9]|1[0-2])\\2(\\d{4})(\\s)([0-1][0-9]|2[0-3])(:)([0-5][0-9])(:)([0-5][0-9])$",
					Pattern.CASE_INSENSITIVE);
			Matcher matcherCantP;
			int postFechaHora2 = -1;
			for (int i = 0; i < lineasPDF.size(); i++) {
				matcherCantP = patternCantP.matcher(lineasPDF.get(i).trim());
				if (matcherCantP.find()) {
					postFechaHora2 = i;
					break;
				}
			}
			direccion = lineasPDF.get(3).substring(0, lineasPDF.get(3).length() - 8);
			int posInicio = utilString.buscarString("IMPORTECONCEPTO", lineasPDF);
			int postFin = utilString.buscarString("Monto IVA 22%", lineasPDF) - 1;
			int posTotal = utilString.buscarString("TOTAL A PAGAR", lineasPDF);
			int postFechaHora = utilString.buscarString("ContadoMoneda", lineasPDF) + 1;
			int postCodSeg = utilString.buscarString("3390/2013", lineasPDF) + 1;
			int postLey17 = utilString.buscarString("Desc. Ley 17.934", lineasPDF);
			codSeguridad = lineasPDF.get(postCodSeg).substring(0, 6);
			log.info(codSeguridad);
			try {
				fecha = new SimpleDateFormat(recurso.getPatternFormatH()).parse(lineasPDF.get(postFechaHora2));
			} catch (ParseException e) {
				log.error(e.getMessage());
			}

			log.info("TOTAL A PAGAR: " + lineasPDF.get(posTotal).substring(13).trim());

			totalPagarPDF = Double.valueOf(lineasPDF.get(posTotal).substring(13).trim().replace(',', '.'));
			// .substring(1, lineasPDF.get(posTotal).substring(13).trim().length()).trim());

			log.info("+++++++" + totalPagarPDF);
			datosExtra = utilString.datosExtra(rut, eTicket, serie, codSeguridad, totalPagarPDF);

			procesarCompra(lineasPDF.subList(posInicio + 1, postFin), datosExtra);

			if (postLey17 > 0) {
				log.info("Desc. Ley 17.934: " + lineasPDF.get(postLey17).substring(29).trim());
				int posSPeso = lineasPDF.get(postLey17).indexOf("$");
				ley = (Double.valueOf(
						lineasPDF.get(postLey17).substring(posSPeso + 1, lineasPDF.get(postLey17).length()).trim())
						* -1);
			}

			if (totalPagarPDF == totalSuma) {
				log.info("Información de la Compra es Correcta");
				if (ley > 0) {
					registros.add(new Registro(ley, moneda, "IVA Ley 19.210", fecha,
							mercado + " Devolución Ley 19.210 compra " + datosExtra, "Yasmani", direccion));
				}
				excel.crearExcel(registros, recurso.getOutput(), mercado, fecha);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	public void procesarCompra(List<String> compras, String datosExtra) {
		Pattern patternCantP = Pattern.compile("[0-9]{1,2}(\\,[0-9]{3})", Pattern.CASE_INSENSITIVE);

		for (String string : compras) {
			String cantidad = "";
			String precioOriginal = "";
			String precioDescuento = "";
			String descripcion = "";

			// Sobre el String entrado por parametro hacer un Split por espacio
			// se obtiene el total del producto (Si hay algun descuento Precio* Cant no
			// aplica el
			// descuento)
			log.info("Compra: " + string);
			String[] listacompra = string.split(" ");

			// Se obtiene la Cantidad de elementos comprados
			Matcher matcherCantP;
			cantidad = "1";

			// Obtiene el precio Original del producto
			patternCantP = Pattern.compile("[0-9]{1,3}(\\,[0-9]{2})", Pattern.CASE_INSENSITIVE);
			matcherCantP = patternCantP.matcher(listacompra[listacompra.length - 1]);
			if (matcherCantP.find()) {
				// Coincidió => obtener el valor del grupo 1
				precioOriginal = matcherCantP.group(0);
				precioOriginal = precioOriginal.replace(",", ".");
				log.info("Precio Original: " + precioOriginal);
			}
			// Obtiene el precio con descuento del producto
			/*
			 * patternCantP = Pattern.compile("[0-9]{1,3}(\\,[0-9]{2})",
			 * Pattern.CASE_INSENSITIVE); matcherCantP =
			 * patternCantP.matcher(listacompra[listacompra.length - 1]); if
			 * (matcherCantP.find()) { // Coincidió => obtener el valor del grupo 1
			 * precioDescuento = matcherCantP.group(0); precioDescuento =
			 * precioDescuento.replace(",", "."); log.info("Precio con Descuento: " +
			 * precioDescuento); }
			 */

			precioDescuento = precioOriginal;

			// String[] des = string.split("[0-9]{1,3}(\\,[0-9]{2})");
			for (int i = 0; i > listacompra.length - 1; i++) {
				descripcion += listacompra[i];
			}
			log.info("Descripcion: " + descripcion);
			elementos.add(new Compra(Double.valueOf(cantidad), Double.valueOf(precioOriginal),
					Double.valueOf(precioOriginal), descripcion, mercado));

		}
		for (Compra compra : elementos) {
			Registro registro = new Registro((compra.getPrecioSDescuento() * -1), moneda, "Hamburguesa", fecha,
					compra.toString() + " " + datosExtra, "YO", direccion);
			totalSuma += compra.getPrecioSDescuento();
			log.info(registro.toString());
			registros.add(registro);
		}
		totalSuma = Double.valueOf(df2.format(totalSuma).replace(",", "."));
	}
}
