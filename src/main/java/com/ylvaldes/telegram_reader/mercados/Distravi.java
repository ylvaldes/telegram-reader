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
import java.util.regex.Pattern;

public class Distravi implements IMercados {
	private static final Logger log =  LogManager.getLogger(Distravi.class);
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

	public Distravi() {
		try {
			// Inicializando Listas
			lineasPDF = new ArrayList<String>();
			elementos = new ArrayList<Compra>();
			registros = new ArrayList<Registro>();

			// Inicializando Variables
			moneda = "";
			mercado="";
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
			mercado=output.substring(11, output.length()-4);
			
			lineasPDF = Arrays.asList(txt.crearTxt().split("\r\n"));
			rut = lineasPDF.get(1).split(":")[1].trim();
			eTicket = lineasPDF.get(5).split(" ")[2];
			serie = lineasPDF.get(5).split(" ")[1];
			moneda = "UYU";

			try {
				fecha = new SimpleDateFormat(recurso.getPatternFormatH()).parse(lineasPDF.get(8));
			} catch (ParseException e) {
				log.error(e.getMessage());
			}
			log.debug("Fecha: " + lineasPDF.get(8));

			log.debug("Direccion: " + lineasPDF.get(2));
			direccion = lineasPDF.get(2);

			int posInicio = utilString.buscarString("Cajero:", lineasPDF);
			int postFin = utilString.buscarString("TOTAL:", lineasPDF);
			int posTotal = utilString.buscarString("TOTAL:", lineasPDF);
			int postLey19 = utilString.buscarString("Desc. Ley   19210", lineasPDF);
			int postCodSeg = utilString.buscarString("Codigo de seguridad:", lineasPDF);

			codSeguridad = lineasPDF.get(postCodSeg).trim().split(":")[1];

			log.info("TOTAL A PAGAR: " + lineasPDF.get(posTotal).substring(13).trim());

			totalPagarPDF = Double.valueOf(lineasPDF.get(posTotal).substring(13).trim().replace(",", ""));

			datosExtra = utilString.datosExtra(rut, eTicket, serie, codSeguridad, totalPagarPDF);

			procesarCompra(lineasPDF.subList(posInicio + 1, postFin), datosExtra);

			if (postLey19 > 0) {
				log.info("Desc. Ley 19210: " + lineasPDF.get(postLey19).substring(29).trim());
				ley = (Double.valueOf(lineasPDF.get(postLey19).substring(29).trim()) * -1);
			}

			if (totalPagarPDF == totalSuma) {
				log.info("Información de la Compra es Correcta");
				if (ley > 0) {
					registros.add(new Registro(ley, moneda, "IVA Ley 19.210", fecha,
							mercado + " Devolución Ley 19.210 compra " + datosExtra, "Yasmani",
							direccion));
				}
				excel.crearExcel(registros, recurso.getOutput(), mercado, fecha);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	public void procesarCompra(List<String> compras, String datosExtra) {
		Pattern patternCantP = Pattern.compile("[0-9]{1,2}(\\,[0-9]{3})", Pattern.CASE_INSENSITIVE);

		for (int i = 0; i < compras.size(); i += 2) {

			String string = compras.get(i);
			String string1 = compras.get(i + 1);
			String cantidad = "";
			String precioOriginal = "";
			String precioDescuento = "";
			String descripcion = "";

			// Sobre el String entrado por parametro hacer un Split por espacio
			// se obtiene el total del producto (Si hay algun descuento Precio* Cant no aplica el
			// descuento)
			log.info("Compra: " + string);
			String[] a = string.split(" ");

			// Descripcion del Producto
			descripcion = string.substring(0, string.length() - a[a.length - 1].length());
			log.info("Descripcion: " + descripcion);

			// Obtiene el precio Original del producto
			precioOriginal = a[a.length - 1];
			precioOriginal = precioOriginal.replace(",", ".");
			log.info("Precio Original: " + precioOriginal);

			// Obtiene el precio con descuento del producto
			precioDescuento = a[a.length - 1];
			precioDescuento = precioDescuento.replace(",", ".");
			log.info("Precio con Descuento: " + precioDescuento);

			// Se obtiene la Cantidad de elementos comprados
			cantidad = string1.substring(1).split("X")[0].trim();
			cantidad = cantidad.replace(",", ".");
			log.info("Cantidad de Producto: " + cantidad);

			elementos.add(new Compra(Double.valueOf(cantidad),
					Double.valueOf(precioOriginal) + Double.valueOf(precioDescuento), Double.valueOf(precioOriginal),
					descripcion,mercado));

		}
		for (Compra compra : elementos) {
			Registro registro = new Registro((compra.getPrecioCDescuento() * -1), moneda, "Aceite", fecha,
					compra.toString() + " " + datosExtra, "Yasmani", direccion);
			totalSuma += compra.getPrecioCDescuento();
			log.info(registro.toString());
			registros.add(registro);
		}
		totalSuma = Double.valueOf(df2.format(totalSuma).replace(",", "."));
	}
}
