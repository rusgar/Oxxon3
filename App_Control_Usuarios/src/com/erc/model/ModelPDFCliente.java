package com.erc.model;

import com.erc.bdhelpers.BDDAO;
import com.erc.dao.SalidaInfoDAO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.time.format.DateTimeFormatter;


/**
 * CON ESTA CLASE GENEREAMOS UN REPORTE PDF PARA UN CLIENTE ESPECÍFICO  O DETERMINDADO,
 * INCLUYENDO INFORMACIÓN SOBRE LAS SALIDAS ASOCIADAS A DICHO CLIENTE.
 *  UTILIZAMOS  LA BIBLIOTECA APACHE PDFBOX PARA CREAR EL DOCUMENTO PDF,
 *  Y CON EL  BDDAO PARA INTERACTUAR CON LA BBDD PARA OBTENER LOS DATOS NECESARIOS.
 */
public class ModelPDFCliente {
	// ATRIBUTOS A UTILIZAR
	private static final float MARGIN = 50;
	private static final float HEADER_Y_START = 700;  //  AJUSTAMOS EL PUNTO DE INICIO SI ES NECESARIO
	private static final float ROW_HEIGHT = 20;  // AJUSTAMOS LA ALTURA DE CADA FILA
	private static final float COLUMN_WIDTH_ENLACE = 100;
	private static final float COLUMN_WIDTH_TAREA = 70;
	private static final float COLUMN_WIDTH_TRABAJADOR = 70;
	private static final float COLUMN_WIDTH_FECHA = 80; //  ANCHO PARA LA COLUMNA DE LA FECHA
	private static final float COLUMN_WIDTH_DESCRIPCION = 220; //AJUSTAMOS PARA QUE ENTRE TODA LA DESCRIPCION
	private static final PDType1Font FONT = PDType1Font.HELVETICA;
	private static final float FONT_SIZE = 10f;
	private static final float LEADING = 14f; // ESPACIADO ENTRE LINEAS

	/**
	 *  CON ESTE METODO GENERAMOS UN ARCHIVO PDF QUE CONTIENE UN REPORTE DE SALIDAS DE UN CLIENTE EN CUESTION
	 * @param idCliente
	 * @param connection
	 * @param bdDAO
	 */
	@SuppressWarnings("deprecation")
	public void generateClientPDF(int idCliente, Connection connection, BDDAO bdDAO) {
		SalidaInfoDAO salidaInfoDAO = new SalidaInfoDAO(connection, bdDAO);
		List<SalidaInfo> salidaInfos;

		tablaClientes cliente;
		try {
			salidaInfos = salidaInfoDAO.obtenerSalidasPorCliente(idCliente);
			cliente = bdDAO.obtenerClientePorId(connection, idCliente); //  USA EL METODO EXISTENTE PARA OBTENER EL CLIENTE
		} catch (SQLException e) {
			System.err.println("Error al obtener los datos del cliente: " + e.getMessage());
			return;
		}

		if (salidaInfos.isEmpty()) {
			System.out.println("No hay datos para el cliente con ID: " + idCliente);
			return;
		}

		String nombreCliente = cliente != null ? cliente.getNombre() : "Desconocido";

		String rutaArchivo = "C:\\Users\\Propietario\\Desktop\\Control_OXON_3\\PDF_Salidas\\" + idCliente + "_Cliente.pdf";
		String directorio = "C:\\Users\\Propietario\\Desktop\\Control_OXON_3\\PDF_Salidas\\";

		try {
			if (!Files.exists(Paths.get(directorio))) {
				Files.createDirectories(Paths.get(directorio));
			}
		} catch (IOException e) {
			System.err.println("Error al crear el directorio: " + e.getMessage());
			return;
		}

		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage();
			document.addPage(page);

			// CARGAMOS LAS IMAGENES
			PDImageXObject image = PDImageXObject.createFromFile("resources/images/Oxon3.png", document);

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
				contentStream.setNonStrokingColor(255, 0, 0);
				// TAMAÑO Y POSICION DE LA IMAGEN
				float imageWidth = 180;  
				float imageHeight = 70;
				float pageWidth = page.getMediaBox().getWidth();
				float pageHeight = page.getMediaBox().getHeight();
				//  POSICION DE LA IAMGEN EN LA PARTE SUPERIOR DERECHA
				float imageX = pageWidth - MARGIN - imageWidth;
				float titleAndImageY = pageHeight - MARGIN - imageHeight / 2;
				// DIBUJAR LA IMAGEN EN LA ESQUINA SUPERIRO DERECHA
				contentStream.drawImage(image, imageX, titleAndImageY, imageWidth, imageHeight);
				//  DIBUJA EL TITULO EN LA PARTE SUPERIOR IZQUIERDA
				contentStream.setFont(FONT, 18);
				contentStream.beginText();
				//  CENTRAMOS EL TEXTO VERTICALMENTE CON RESPECTO A LA IMAGEN
				contentStream.newLineAtOffset(MARGIN, titleAndImageY + (imageHeight / 2) - 9);  
				contentStream.showText("Reporte del Cliente ID: " + idCliente + " - " + nombreCliente);
				contentStream.endText();
				contentStream.setNonStrokingColor(0, 0, 0);

				//  DIBUJAMOS EL ENCABEZADO DE LA TABLA
				drawTableHeader(contentStream, HEADER_Y_START - imageHeight -20);

				//  DIBUJAMOS LOS DATOS DE SALIDAS DE LOS CLIENTES
				drawClientData(contentStream, HEADER_Y_START- imageHeight -20, salidaInfos, bdDAO, connection);
			}

			document.save(rutaArchivo);
			System.out.println("PDF creado exitosamente en: " + rutaArchivo);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * CON ESTE METODO SACAMOS EL ENCABEZADO DE LA TABLA EN EL PDF
	 * @param contentStream
	 * @param yPosition
	 * @throws IOException
	 */
	private static void drawTableHeader(PDPageContentStream contentStream, float yPosition) throws IOException {
		contentStream.setFont(FONT, FONT_SIZE);

		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + 10, yPosition + 5);
		contentStream.showText("ENLACE");
		contentStream.endText();

		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + COLUMN_WIDTH_ENLACE + 10, yPosition + 5);
		contentStream.showText("TAREA");
		contentStream.endText();

		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + COLUMN_WIDTH_ENLACE + COLUMN_WIDTH_TAREA + 10, yPosition + 5);
		contentStream.showText("TRABAJADOR");
		contentStream.endText();

		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + COLUMN_WIDTH_ENLACE + COLUMN_WIDTH_TAREA + COLUMN_WIDTH_TRABAJADOR + 10, yPosition + 5);
		contentStream.showText("FECHA");
		contentStream.endText();

		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + COLUMN_WIDTH_ENLACE + COLUMN_WIDTH_TAREA + COLUMN_WIDTH_TRABAJADOR + COLUMN_WIDTH_FECHA + 10, yPosition + 5);
		contentStream.showText("DESCRIPCION");
		contentStream.endText();
	}

	/**
	 * CON ESTE METODO SACAMOS LOS DATOS DE LA SALIDA DEL CLIETNE EN EL PDF
	 * @param contentStream
	 * @param yStart
	 * @param salidaInfos
	 * @param bdDAO
	 * @param connection
	 * @throws IOException
	 */
	private static void drawClientData(PDPageContentStream contentStream, float yStart, List<SalidaInfo> salidaInfos, BDDAO bdDAO, Connection connection) throws IOException {
		float currentYPosition = yStart - ROW_HEIGHT - 10; // CREAMOS EL ESPACIO INICIAL

		contentStream.setFont(FONT, FONT_SIZE);

		//  DEFINIMOS EL FORMATO DE LA DFECHA
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		for (SalidaInfo salidaInfo : salidaInfos) {
			String descripcion = salidaInfo.getSalida().getDescripcion();
			String fecha = salidaInfo.getSalida().getFechaTarea().format(formatter); 

			float descriptionHeight = calculateTextHeight(COLUMN_WIDTH_DESCRIPCION, descripcion);
			float rowHeight = Math.max(ROW_HEIGHT, descriptionHeight);

			currentYPosition -= rowHeight; // MOVEMOS LA POSICION "Y" HACIA ABAJO

			//  DIBUJAMOS LOS DATOS EN CADA FILA
			drawRowData(contentStream, currentYPosition, salidaInfo, bdDAO, connection);

			//  DIBUJAMOS EL TEXTO DE LA DECRIPCION, AJUSTANDO LAS LINEAS SI ES NECESARIO
			drawWrappedText(contentStream, MARGIN + COLUMN_WIDTH_ENLACE + COLUMN_WIDTH_TAREA + COLUMN_WIDTH_TRABAJADOR + COLUMN_WIDTH_FECHA + 10,
					        currentYPosition +5 , COLUMN_WIDTH_DESCRIPCION, descripcion);
		    currentYPosition -= 70;        
		}
	}




	/**
	 * CON ESTE MÉTODO UTILIZA UN  PDPAGECONTENTSTREAM  PARA DIBUJAR INFORMACIÓN RELACIONADA CON UNA SALIDA, 
	 * INCLUYENDO LA FECHA DE LA TAREA, EL ENLACE A COORDENADAS DE GOOGLE, LA TAREA EN SÍ Y EL NOMBRE DEL TRABAJADOR ASOCIADO.
	 * @param contentStream
	 * @param yPosition
	 * @param salidaInfo
	 * @param bdDAO
	 * @param connection
	 * @throws IOException
	 */
	private static void drawRowData(PDPageContentStream contentStream, float yPosition, SalidaInfo salidaInfo, BDDAO bdDAO, Connection connection) throws IOException {
		// DEFINIMOS EL FORMATO DE LA FECHA
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		//  OBTENEMOS LA FECHA COMO STRING
		String fecha = salidaInfo.getSalida().getFechaTarea().format(formatter);

		// OBTENERMOS EL ID DEL TRABAJADOR
		int idTrabajador = salidaInfo.getSalida().getIdTrabajador();

		//  OBTENEMOS EL NOMBRE DEL TRABAJADOR DESDE LA BBDD
		String nombreTrabajador;
		try {
			// OBTENEMOS LA INFORMACION DEL TRABAJDOR DESDE LA BBDD USABDI EL BDDO
			tablaTrabajadores trabajador = bdDAO.obtenerTrabajadorPorId(connection, idTrabajador);
			nombreTrabajador = trabajador.getNombre(); //  AJUSTAMOS SEGUN EL METODO SEGUN EL NIMBRE DEL METODO
		} catch (SQLException e) {
			nombreTrabajador = "Desconocido"; // MANEJAMOS EL CASO EN QUE NO SE PUEDE OBTENER EL NOMBNRE DEL TRABAJADOR
			System.err.println("Error al obtener el trabajador: " + e.getMessage());
		}

		//  PINTAMOS EL TEXTO PARA LA FECHA
		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + COLUMN_WIDTH_ENLACE + COLUMN_WIDTH_TAREA + COLUMN_WIDTH_TRABAJADOR + 10, yPosition + 5);
		contentStream.showText(fecha);
		contentStream.endText();

		//  PINTAMOS EL ENLACE DE GOOGLE
		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + 10, yPosition + 5);
		contentStream.showText("COORDENADAS");
		contentStream.endText();

		// PINTAMOS EL TEXTO DE LA TAREA
		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + COLUMN_WIDTH_ENLACE + 10, yPosition + 5);
		contentStream.showText(salidaInfo.getSalida().getTarea());
		contentStream.endText();

		// PINTAMO EL NOMBRE DEL TRABAJADOR
		contentStream.beginText();
		contentStream.newLineAtOffset(MARGIN + COLUMN_WIDTH_ENLACE + COLUMN_WIDTH_TAREA + 10, yPosition + 5);
		contentStream.showText(nombreTrabajador);
		contentStream.endText();
	}

	/**
	 * CON ESTE MÉTODO PERMITIMOS ESCRIBIR TEXTO EN MÚLTIPLES LÍNEAS,ES DEICR,  SI EL TEXTO SUPERA EL  ANCHO DADO, 
	 * UTILIZANDO UN MÉTODO DE DIVISIÓN PARA ASEGURARSE DE QUE EL TEXTO  SE AJUSTE DENTRO DE LOS MÁRGENES ESPECIFICADOS.
	 * @param contentStream
	 * @param x
	 * @param y
	 * @param width
	 * @param text
	 * @throws IOException
	 */
	private static void drawWrappedText(PDPageContentStream contentStream, float x, float y, float width, String text) throws IOException {
		contentStream.setFont(FONT, FONT_SIZE);
		contentStream.beginText();
		contentStream.newLineAtOffset(x, y);

		String[] lines = splitTextToFitWidth(text, width);
		for (String line : lines) {
			contentStream.showText(line);
			contentStream.newLineAtOffset(0, -LEADING);
		}
		contentStream.endText();
	}


	/**
	 * CON ESTE MÉTODO TOMAMOS UN TEXTO Y LO SEPARA EN PALABRAS, CREANDO NUEVAS LÍNEAS  SEGÚN SEA NECESARIO
	 * PARA ASEGURARSE DE QUE CADA LÍNEA SALGA DEL  ANCHO PERMITIDO. UTILIZAMOS LA FUENTE Y EL TAMAÑO DE FUENTE
	 * ACTUALES PARA  CALCULAR EL ANCHO DEL TEXTO.
	 * @param text
	 * @param width
	 * @return
	 * @throws IOException
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
	 * CON ESTE MÉTODO UTILIZAMOS EL ANCHO DE COLUMNA PROPORCIONADO PARA DIVIDIR EL TEXTO  EN LÍNEAS 
	 * PARA QUE SE AJUSTEN A DICHO ANCHO. LUEGO, CALCULAMOS LA ALTURA TOTAL  DEL TEXTO MULTIPLICANDO 
	 * EL NÚMERO DE LÍNEAS POR EL ESPACIADO ENTRE LÍNEAS.
	 * @param columnWidth
	 * @param text
	 * @return
	 * @throws IOException
	 */
	private static float calculateTextHeight(float columnWidth, String text) throws IOException {
		return splitTextToFitWidth(text, columnWidth).length * LEADING;
	}
}
