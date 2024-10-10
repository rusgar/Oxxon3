package com.erc.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import com.erc.bdhelpers.BDDAO;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;



import com.erc.dao.URLShortener;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * CON ESTA CLASE PARA GENERAMOS UN DOCUMENTO PDF QUE CONTIENE UN INFORME DIARIO DE SALIDAS,
 * INCLUYENDO TABLAS QUE MUESTRAN  LOS DETALLES COMO FECHAS, PUESTOS, LOCALIDADES, 
 * Y ENLACES A GOOGLE MAPS. UTILIZAMOS LA BIBLIOTECA APACHE PDFBOX PARA LA CREACIÓN Y MANIPULACIÓN DEL  DOCUMENTO PDF.
 * LOS DATOS LOS OBTENEMOS A TRAVÉS DE LA CLASE SALIDAINFO.
 */
public class modelPDFDiario {
	private Connection conexion;
	private BDDAO bdDAO;
	private static final float MARGIN = 50;
	private static final float TABLE_WIDTH = 500; //  ANCHO TOTAL DE LAS TABLAS
	private static final float ROW_HEIGHT = 15;
	private static final float COLUMN_WIDTH_TAREA = 50;
	private static final float COLUMN_WIDTH_FECHA = 100; 
	private static final float COLUMN_WIDTH_NOMBRE = 100;
	private static final float COLUMN_WIDTH_LOCALIDAD = 100;
	private static final float COLUMN_WIDTH_NOMBRE_USUARIO = 100; 
	private static final float COLUMN_WIDTH_ENLACE = 130; 
	private static final float COLUMN_WIDTH_DESCRIPCION = 350; 
	private static final float COLUMN_WIDTH_INSTALACION = 80;
	private static final float COLUMN_WIDTH_INCIDENCIA = 80;
	private static final float TITLE_Y_POSITION = 700; // POSICION INICIAL PARA EL TITULO
	private static final float TABLE1_Y_START = 620; // POSICIONES DE LAS TABLAS
	private static final float TABLE3_Y_START = 500; 
	private static final float TABLE2_Y_START = 400; 
	private static final PDType1Font FONT = PDType1Font.HELVETICA;
	private static final PDType1Font FONT_HEADER = PDType1Font.HELVETICA_BOLD;
	private static final float FONT_SIZE = 10;// TAMAÑO INICIAL DE LA FUENTE
	private static final float FONT_SIZE_HEADER = 12;

	/**
	 * CON ESTE MODELO GENERAMOS UN ARCHIVO PDF QUE CONTIENE UN INFORME DIARIO CON LOS DETALLES DE LAS SALIDAS
	 * @param salidaInfos
	 * @param fechaSeleccionada
	 */
	@SuppressWarnings("deprecation")
	public static void generarPDF(List<SalidaInfo> salidaInfos, LocalDate fechaSeleccionada) {
		String rutaArchivo = "C:\\Users\\Propietario\\Desktop\\Control_OXON_3\\PDF_Salidas\\" + fechaSeleccionada.toString() + "_Diario.pdf";
		String directorio = "C:\\Users\\Propietario\\Desktop\\Control_OXON_3\\PDF_Salidas\\";

		if (!Files.exists(Paths.get(directorio))) {
			System.out.println("El directorio no existe: " + directorio);
			return;
		}
		// OBTENEMOS LA FECHA Y DIA DE LA SEMANA
		Date fecha = java.sql.Date.valueOf(fechaSeleccionada);
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
		String diaSemana = dayFormat.format(fecha).toLowerCase();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		String diaSemanaCapitalizado = diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1);
		String fechaCompleta = dateFormat.format(fecha);

		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage(); 
			document.addPage(page);
			// AGREGRAMOS LA IMAGEN
			PDImageXObject pdImage = PDImageXObject.createFromFile("resources/images/Oxon3.png", document);
			float imageWidth = 180; // AJUSTAMOS EL ANCHO DE LA IMAGEN
			float imageHeight = 70; // AJUSTAMOS EL ALTO DE LA IMAGEN
			float imageXPosition = page.getMediaBox().getWidth() - MARGIN - imageWidth; // POSICINAMOS LA IMAGEN A LA DERECGHA
			float imageYPosition = page.getMediaBox().getHeight() - MARGIN - imageHeight; 


			try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
				contentStream.setNonStrokingColor(255, 0, 0);
				// AGREGAMOS EL TITULO				
				contentStream.setFont(FONT_HEADER, 18);
				contentStream.beginText();
				contentStream.newLineAtOffset(MARGIN, page.getMediaBox().getHeight() - MARGIN - 30); //AJUSTAMOS LA POSICION DEL TITULO
				contentStream.showText("Programación: " + diaSemanaCapitalizado + " - " + fechaCompleta); // Día de la semana + fecha completa
				contentStream.endText();
				
				
				// DIBUJAMOS LA IMAGEN EN LA PARTE SUPERIOR DERECHA
				contentStream.drawImage(pdImage, imageXPosition, imageYPosition, imageWidth, imageHeight);
				contentStream.setNonStrokingColor(0, 0, 0);

				//DIBUJAMOS LAS TABLAS
				drawTable1(contentStream, TABLE1_Y_START, salidaInfos);
				drawTable3(contentStream, page, TABLE3_Y_START, salidaInfos);
				drawTable2(contentStream, TABLE2_Y_START, salidaInfos, document);

			}

			document.save(rutaArchivo);
			System.out.println("PDF creado exitosamente en: " + rutaArchivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * CON ESTE METODO TENEMOS Y DIBUJAMOS LA PRIMERA TABLA
	 * @param contentStream
	 * @param yStart
	 * @param salidaInfos
	 * @throws IOException
	 */
	private static void drawTable1(PDPageContentStream contentStream, float yStart, List<SalidaInfo> salidaInfos) throws IOException {
		final float yPosition = yStart;
		final float tableHeight = ROW_HEIGHT * (2 + salidaInfos.size()); //  AGREGAMOS LAS FILAS NECESARIAS PARA LAS TABLAS

		// DIBUJAMOS LAS FILAS
		contentStream.setLineWidth(1f);
		contentStream.moveTo(MARGIN, yPosition);
		contentStream.lineTo(MARGIN + COLUMN_WIDTH_TAREA + COLUMN_WIDTH_FECHA + COLUMN_WIDTH_NOMBRE+ COLUMN_WIDTH_LOCALIDAD + COLUMN_WIDTH_NOMBRE_USUARIO, yPosition);
		contentStream.stroke();

		// DIBUJAMOS LAS COLUMNAS
		float xPosition = MARGIN;
		contentStream.moveTo(xPosition, yPosition);
		contentStream.lineTo(xPosition, yPosition - tableHeight);
		contentStream.stroke();
		xPosition += COLUMN_WIDTH_TAREA;
		contentStream.moveTo(xPosition, yPosition);
		contentStream.lineTo(xPosition, yPosition - tableHeight);
		contentStream.stroke();
		xPosition += COLUMN_WIDTH_FECHA;
		contentStream.moveTo(xPosition, yPosition);
		contentStream.lineTo(xPosition, yPosition - tableHeight);
		contentStream.stroke();
		xPosition += COLUMN_WIDTH_NOMBRE;
		contentStream.moveTo(xPosition, yPosition);
		contentStream.lineTo(xPosition, yPosition - tableHeight);
		contentStream.stroke();
		xPosition += COLUMN_WIDTH_LOCALIDAD;
		contentStream.moveTo(xPosition, yPosition);
		contentStream.lineTo(xPosition, yPosition - tableHeight);
		contentStream.stroke();
		xPosition += COLUMN_WIDTH_NOMBRE_USUARIO;
		contentStream.moveTo(xPosition, yPosition);
		contentStream.lineTo(xPosition, yPosition - tableHeight);
		contentStream.stroke();


		contentStream.moveTo(MARGIN, yPosition - tableHeight);
		contentStream.lineTo(MARGIN + COLUMN_WIDTH_TAREA + COLUMN_WIDTH_FECHA + COLUMN_WIDTH_NOMBRE+ COLUMN_WIDTH_LOCALIDAD + COLUMN_WIDTH_NOMBRE_USUARIO, yPosition - tableHeight);
		contentStream.stroke();

		// DIBUJAMOS EL ENCABEZADO DE LA PRIMERA TABLA
		contentStream.setFont(FONT_HEADER,  FONT_SIZE_HEADER);
		float headerYPosition = yStart;
		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN+10, headerYPosition +10);
		contentStream.showText("Tarea");
		contentStream.newLineAtOffset(COLUMN_WIDTH_TAREA, 0);
		contentStream.showText("Fecha Tarea");
		contentStream.newLineAtOffset(COLUMN_WIDTH_FECHA, 0);
		contentStream.showText("Trabajador");
		contentStream.newLineAtOffset(COLUMN_WIDTH_NOMBRE, 0);
		contentStream.showText("Localidad");
		contentStream.newLineAtOffset(COLUMN_WIDTH_LOCALIDAD, 0);
		contentStream.showText("Cliente");
		contentStream.newLineAtOffset(COLUMN_WIDTH_NOMBRE_USUARIO, 0);       
		contentStream.endText();

		// DIBUJAMOS LOS DATOS DE LA COLUMNA DE LA PRIMNERA TABLA
		contentStream.setFont(FONT, FONT_SIZE);
		float rowYPosition = headerYPosition - ROW_HEIGHT;
		for (SalidaInfo salidaInfo : salidaInfos) {
			 tablaSalidas salida = salidaInfo.getSalida();
			contentStream.beginText();
			contentStream.newLineAtOffset(MARGIN +15, rowYPosition -5);
			contentStream.showText(String.valueOf(salida.getTarea()));
			contentStream.newLineAtOffset(COLUMN_WIDTH_TAREA, 0);
			contentStream.showText(salida.getFechaTarea().toString());
			contentStream.newLineAtOffset(COLUMN_WIDTH_FECHA, 0);
			contentStream.showText(salidaInfo.getNombre());
			contentStream.newLineAtOffset(COLUMN_WIDTH_NOMBRE, 0);
			contentStream.showText(salidaInfo.getLocalidad());
			contentStream.newLineAtOffset(COLUMN_WIDTH_LOCALIDAD, 0);
			contentStream.showText(salidaInfo.getNombreUsuario());
			contentStream.newLineAtOffset(COLUMN_WIDTH_NOMBRE_USUARIO, 0);           
			contentStream.endText();
			rowYPosition -= ROW_HEIGHT;
		}
	}

	/**
	 * CON ESTE METODO TENEMOS Y DIBUJAMOS LA SEGUNDA TABLA
	 * @param contentStream
	 * @param yStart
	 * @param salidaInfos
	 * @param document 
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private static void drawTable2(PDPageContentStream contentStream, float yStart, List<SalidaInfo> salidaInfos, PDDocument document) throws IOException {
		float yPosition = yStart;		
		float pageWidth = PDRectangle.A4.getWidth() - 2 * MARGIN;

		 //DIBUJAMOS LOS ENCABEZADOD
		contentStream.setNonStrokingColor(115, 91, 162);
		contentStream.setFont(FONT_HEADER,  FONT_SIZE_HEADER);
		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN, yPosition);
		contentStream.showText("DESCRIPCION");		
		contentStream.endText();
		contentStream.setNonStrokingColor(0, 0, 0);

		// AJUSTAMOS EL CONTENIDO DE LA MISMA
		yPosition -= ROW_HEIGHT;
		contentStream.setFont(FONT, FONT_SIZE);

		for (SalidaInfo salidaInfo : salidaInfos) {
			tablaSalidas salida = salidaInfo.getSalida();
			String descripcion = salida.getDescripcion();
			String[] lines = splitTextToFitWidth(descripcion, pageWidth);

			for (String line : lines) {				
				if (yPosition - ROW_HEIGHT < MARGIN + 5) {
					PDPage newPage = new PDPage();
					contentStream = new PDPageContentStream(document, newPage);
					document.addPage(newPage);
					yPosition = PDRectangle.A4.getHeight() - MARGIN; // REINICIAMOS LA POSICION INICIAL
				}
				
				contentStream.beginText();
				contentStream.newLineAtOffset(MARGIN, yPosition);
				contentStream.showText(line);
				contentStream.endText();
				yPosition -= ROW_HEIGHT;
			}
			yPosition -= ROW_HEIGHT; 
		}
	}

	/**
	 * MÉTODO PARA DIVIDIR EL TEXTO EN LÍNEAS DE LONGITUD FIJA.
	 * @param text
	 * @param maxLength
	 * @return
	 */
	private static String[] splitTextToFitWidth(String text, float width) throws IOException {
		List<String> lines = new ArrayList<>();
		String[] words = text.split(" ");
		StringBuilder currentLine = new StringBuilder();

		for (String word : words) {
			String testLine = currentLine.toString() + (currentLine.length() > 0 ? " " : "") + word;
			float testWidth = FONT.getStringWidth(testLine) / 1000 * FONT_SIZE;

			if (testWidth > width) {
				lines.add(currentLine.toString());
				currentLine = new StringBuilder(word);
			} else {
				currentLine.append((currentLine.length() > 0 ? " " : "")).append(word);
			}
		}
		lines.add(currentLine.toString());

		return lines.toArray(new String[0]);
	}


	/**
	 * CON ESTE METODO TENEMOS Y DIBUJAMOS LA TERCERA TABLA
	 * @param contentStream
	 * @param page
	 * @param yStart
	 * @param salidaInfos
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private static void drawTable3(PDPageContentStream contentStream, PDPage page, float yStart, List<SalidaInfo> salidaInfos) throws IOException {
		final float tableWidth = COLUMN_WIDTH_ENLACE + COLUMN_WIDTH_INSTALACION + COLUMN_WIDTH_INCIDENCIA;
		final float rowHeight = ROW_HEIGHT;
		final float tableHeight = rowHeight * (1 + salidaInfos.size()); //  AGREGAMOS LAS FILAS NECESARIAS PARA LAS TABLAS

		// DIBUJAMOS LAS FILAS
		contentStream.setLineWidth(1f);
		contentStream.moveTo(MARGIN, yStart);
		contentStream.lineTo(MARGIN + tableWidth, yStart);
		contentStream.stroke();

		// DIBUJAMOS LAS COLUMNAS
		float xPosition = MARGIN;
		contentStream.moveTo(xPosition, yStart);
		contentStream.lineTo(xPosition, yStart - tableHeight);
		contentStream.stroke();
		xPosition += COLUMN_WIDTH_ENLACE;
		contentStream.moveTo(xPosition, yStart);
		contentStream.lineTo(xPosition, yStart - tableHeight);
		contentStream.stroke();
		xPosition += COLUMN_WIDTH_INSTALACION;
		contentStream.moveTo(xPosition, yStart);
		contentStream.lineTo(xPosition, yStart - tableHeight);
		contentStream.stroke();
		xPosition += COLUMN_WIDTH_INCIDENCIA;
		contentStream.moveTo(xPosition, yStart);
		contentStream.lineTo(xPosition, yStart - tableHeight);
		contentStream.stroke();

		contentStream.moveTo(MARGIN, yStart - tableHeight);
		contentStream.lineTo(MARGIN + tableWidth, yStart - tableHeight);
		contentStream.stroke();

		// DIBUJAMOS EL ENCABEZADO DE LA TERCERA TABLA
		contentStream.setFont(FONT_HEADER,  FONT_SIZE_HEADER);
		float headerYPosition = yStart + 5; 

		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + 10, headerYPosition); 
		contentStream.showText("Enlace Google Maps");
		contentStream.endText();

		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + COLUMN_WIDTH_ENLACE + 10, headerYPosition); 
		contentStream.showText("Instalación");
		contentStream.endText();

		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + COLUMN_WIDTH_ENLACE + COLUMN_WIDTH_INSTALACION + 10, headerYPosition);
		contentStream.showText("Incidencia");
		contentStream.endText();

		// CREAMOS LA INSTANCIA DE URLSHORNERT
		URLShortener shortener = new URLShortener();

		// DIBUJAMOS LOS DATOS DE LA COLUMNA DE LA TERCERA TABLA
		contentStream.setFont(FONT, FONT_SIZE);
		float rowYPosition = yStart - rowHeight; 
		for (SalidaInfo salidaInfo : salidaInfos) {
			// Shorten the URL
			String originalURL = salidaInfo.getEnlaceGoogleMaps();

			// Add the link annotation
			PDAnnotationLink link = new PDAnnotationLink();
			PDRectangle position = new PDRectangle(MARGIN, rowYPosition, COLUMN_WIDTH_ENLACE, rowHeight);
			link.setRectangle(position);

			PDActionURI action = new PDActionURI(); 
			action.setURI(originalURL); 
			link.setAction(action);			
			page.getAnnotations().add(link);

			// Add text for the link
			tablaSalidas salida = salidaInfo.getSalida();
			contentStream.setNonStrokingColor(0, 150, 64);
			contentStream.beginText();
			contentStream.newLineAtOffset(MARGIN + 15, rowYPosition + 5); 
			contentStream.showText("COORDENADAS");
			contentStream.endText();
			contentStream.setNonStrokingColor(0, 0, 0);
		    contentStream.beginText();
			contentStream.newLineAtOffset(COLUMN_WIDTH_ENLACE +10, 0); 
			contentStream.showText(salida.isInstalaciones() ? "Sí" : "No");		
			contentStream.newLineAtOffset(COLUMN_WIDTH_INSTALACION +10, 0); 
			contentStream.showText(salida.isIncidencias() ? "Sí" : "No");
			contentStream.endText();

			rowYPosition -= rowHeight; 
		}
	}





}
