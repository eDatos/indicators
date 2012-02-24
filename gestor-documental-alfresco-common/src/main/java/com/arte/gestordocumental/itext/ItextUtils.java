package com.arte.gestordocumental.itext;

import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class ItextUtils {

	public static void anadirTextoADocumento(byte[] documento, String texto, OutputStream outputStream) throws Exception {

		Document nuevoDocumentoPdf = new Document(PageSize.A4);
		try {
			PdfReader pdfReader = new PdfReader(documento);
			
			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(nuevoDocumentoPdf, outputStream);
			nuevoDocumentoPdf.open();
			
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_OBLIQUE, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF

			int pageOfCurrentReaderPDF = 0;
			while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
				nuevoDocumentoPdf.newPage();
				pageOfCurrentReaderPDF++;
				PdfImportedPage page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
				cb.addTemplate(page, 0, 0);

//				cb.setLineWidth(new Float(0.5));
//	            cb.moveTo(0, 30);
//	            cb.lineTo(document.right(10000), 30);
//	            cb.stroke();
				
				cb.beginText();
				cb.setFontAndSize(bf, 9);	
				cb.showTextAligned(PdfContentByte.ALIGN_CENTER, texto, 297, 15, 0);				
				cb.endText();
				cb.lineTo(0, 0);
			}			
			outputStream.flush();
			nuevoDocumentoPdf.close();
			outputStream.close();
		} finally {
			if (nuevoDocumentoPdf.isOpen()) {
				nuevoDocumentoPdf.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
}
