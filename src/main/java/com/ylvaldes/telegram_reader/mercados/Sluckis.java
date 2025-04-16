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

public class Sluckis implements IMercados {
	private static final Logger log =  LogManager.getLogger(Sluckis.class);
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
	double totalPagarPDF;
	double totalSuma;
	double ley;
	String mercado;

	public Sluckis() {
		try {
			// Inicializando Listas
			lineasPDF = new ArrayList<String>();
			elementos = new ArrayList<Compra>();
			registros = new ArrayList<Registro>();

			// Inicializando Variables
			moneda = "";
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
			mercado="";

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
		log.debug("Entrada: " + filename);
		txt = new Txt(filename, output);
		mercado=output.substring(11, output.length()-4);

		lineasPDF = Arrays.asList(txt.crearTxt().split("\r\n"));

		rut = lineasPDF.get(0).substring(0,12);
		eTicket = lineasPDF.get(2).split(" / ")[0];
		serie = lineasPDF.get(2).split(" / ")[1].substring(0, lineasPDF.get(2).split(" / ")[1].length()-10);

		log.info("Moneda: " + "UYU");
		moneda = "UYU";
		log.debug("Fecha: " + lineasPDF.get(7));

		log.debug("Direccion: " + lineasPDF.get(1).substring(8, lineasPDF.get(1).length()));
		direccion = lineasPDF.get(1).substring(8, lineasPDF.get(1).length());

		int posInicio = utilString.buscarString("Artículo", lineasPDF);
		int postFin = utilString.buscarString("Adenda", lineasPDF);
		int posTotal = utilString.buscarString("Fecha de Vencimiento", lineasPDF);
		int postLey19 = utilString.buscarString("Desc. Ley   19210", lineasPDF);
		int postFechaHora = utilString.buscarString("Fecha de emitido", lineasPDF);
		int postCodSeg = utilString.buscarString("Código de seguridad:", lineasPDF);

		codSeguridad = lineasPDF.get(postCodSeg+1).trim();

		log.info((lineasPDF.get(postFechaHora).split("   ")[0]).split(":")[1]);
		try {
			if (lineasPDF.get(postFechaHora + 1).split("   ")[1] == " ") {
				fecha = new SimpleDateFormat(recurso.getPatternFormatH()).parse(lineasPDF.get(postFechaHora + 1).split("   ")[1]);
			} else {
				fecha = new SimpleDateFormat(recurso.getPatternFormatH()).parse(lineasPDF.get(postFechaHora + 1).split("   ")[2]);
			}
		} catch (ParseException e) {
			log.error(e.getMessage());
		}

		log.info("TOTAL A PAGAR: " + lineasPDF.get(posTotal).substring(13).trim());
		totalPagarPDF = Double.valueOf(lineasPDF.get(posTotal).substring(13).trim().substring(1, lineasPDF.get(posTotal).substring(13).trim().length()).trim());

		datosExtra = utilString.datosExtra(rut, eTicket, serie, codSeguridad, totalPagarPDF);

		procesarCompra(lineasPDF.subList(posInicio + 1, postFin), datosExtra);

		if (postLey19 > 0) {
			log.info("Desc. Ley 19210: " + lineasPDF.get(postLey19).substring(29).trim());
			int posSPeso = lineasPDF.get(postLey19).indexOf("$");
			ley = (Double.valueOf(lineasPDF.get(postLey19).substring(posSPeso + 1, lineasPDF.get(postLey19).length()).trim()) * -1);
		}

		if (totalPagarPDF == totalSuma) {
			log.info("Información de la Compra es Correcta");
			if (ley > 0) {
				registros.add(new Registro(ley, moneda, "IVA Ley 19.210", fecha, mercado + " Devolución Ley 19.210 compra " + datosExtra, "Yasmani", direccion));
			}
			excel.crearExcel(registros, recurso.getOutput(), mercado, fecha);
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
			// se obtiene el total del producto (Si hay algun descuento Precio* Cant no aplica el
			// descuento)
			log.info("Compra: " + string);
			String[] a = string.split(" ");

			// Se obtiene la Cantidad de elementos comprados
			patternCantP = Pattern.compile("[0-9]{1,2}(\\,[0-9]{3})", Pattern.CASE_INSENSITIVE);
			Matcher matcherCantP = patternCantP.matcher("0" + a[a.length - 2]);
			if (matcherCantP.find()) {
				// Coincidió => obtener el valor del grupo 1
				cantidad = matcherCantP.group(0);
				cantidad = cantidad.replace(",", ".");
				log.info("Cantidad de Producto: " + cantidad);
			}
			// Obtiene el precio Original del producto
			patternCantP = Pattern.compile("([0-9]{1,3})*(\\.)*[0-9]{1,3}(\\,[0-9]{2})", Pattern.CASE_INSENSITIVE);
			matcherCantP = patternCantP.matcher(string);
			if (matcherCantP.find()) {
				// Coincidió => obtener el valor del grupo 1
				precioOriginal = matcherCantP.group(0);
				precioOriginal=precioOriginal.replace(".", "");
				precioOriginal = precioOriginal.replace(",", ".");
				log.info("Precio Original: " + precioOriginal);
			}
			// Obtiene el precio con descuento del producto
			patternCantP = Pattern.compile("([0-9]{1,3})*(\\.)*[0-9]{1,3}(\\,[0-9]{2})", Pattern.CASE_INSENSITIVE);
			matcherCantP = patternCantP.matcher(a[a.length - 1]);
			if (matcherCantP.find()) {
				// Coincidió => obtener el valor del grupo 1
				precioDescuento = matcherCantP.group(0);
				precioDescuento=precioDescuento.replace(".", "");
				precioDescuento = precioDescuento.replace(",", ".");
				log.info("Precio con Descuento: " + precioDescuento);
			}
			String[] des = string.split("([0-9]{1,3})*(\\.)*[0-9]{1,3}(\\,[0-9]{2})");
			descripcion = des[1].toString().trim().substring(0, des[1].toString().length() - 4);
			log.info("Descripcion: " + descripcion);
			elementos.add(new Compra(Double.valueOf(cantidad), Double.valueOf(cantidad) * Double.valueOf(precioOriginal), Double.valueOf(precioDescuento), descripcion, mercado));

		}
		for (Compra compra : elementos) {
			Registro registro = new Registro((compra.getPrecioCDescuento() * -1), moneda, "Aceite", fecha, compra.toString() + " " + datosExtra, "Yasmani", direccion);
			totalSuma += compra.getPrecioCDescuento();
			log.info(registro.toString());
			registros.add(registro);
		}
		totalSuma = Double.valueOf(df2.format(totalSuma).replace(",", "."));

	}

	public List<Registro> detectarCategoria(List<Registro> regs) {

		List<String> categorias = Arrays.asList("Aceite", "Agua", "Arroz", "Azucar", "Bolsa", "Cafe", "Chorizo", "Dulces", "Fiambre", "Hamburguesas", "Harina", "Huevos", "Jugos", "Pizza", "Quesos",
				"Sal", "Vinagre");

		for (Registro registro : regs) {
			if (registro.getDescripcion().contains(moneda)) {

			}
		}
		return regs;

	}
}
