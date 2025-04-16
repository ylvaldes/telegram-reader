package com.ylvaldes.telegram_reader.archivos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Txt {
	private static final Logger log =  LogManager.getLogger(Txt.class);
		
	String filename;
	String output;

	public Txt(String filename, String output) {
		this.filename = filename;
		this.output = output;
	}

	public String crearTxt()  {
		File f = new File(filename);
		String parsedText="";
		PDFParser parser;
		try {
			if (f.exists() && f.isFile()) {
				parser = new PDFParser(new RandomAccessFile(f, "r"));
				parser.parse();
				COSDocument cosDoc = parser.getDocument();

				PDFTextStripper pdfStripper = new PDFTextStripper();
				PDDocument pdDoc = new PDDocument(cosDoc);

				parsedText = pdfStripper.getText(pdDoc);
				if (cosDoc != null)
					cosDoc.close();
				if (pdDoc != null)
					pdDoc.close();

				PrintWriter pw = new PrintWriter(output, "utf-8");
				pw.print(parsedText);
				pw.close();
			}
			
		} catch (IOException e) {
			log.error("Error al crear el txt");
			log.error(e.getMessage());
		}
		return parsedText;
	}
}
