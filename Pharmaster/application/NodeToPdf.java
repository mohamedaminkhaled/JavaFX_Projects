package application;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;


 public class NodeToPdf {
			
	   public void createPDF(Node pane) {
	            WritableImage nodeshot = pane.snapshot(new SnapshotParameters(), null);
	            File file = new File("chart.png");
	
			try {
			    ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
			} catch (IOException e) {
			
			}
	
	            PDDocument doc    = new PDDocument();
	            PDPage page = new PDPage();
	            PDImageXObject pdimage;
	            PDPageContentStream content;
	            try {
	                pdimage = PDImageXObject.createFromFile("chart.png",doc);
	                content = new PDPageContentStream(doc, page);
	                content.drawImage(pdimage, 100, 100);
	                content.close();
	                doc.addPage(page);
	                doc.save("pdf_file.pdf");
	                doc.close();
	                file.delete();
	            } catch (IOException ex) {
	                Logger.getLogger(NodeToPdf.class.getName()).log(Level.SEVERE, null, ex);
	            }
	
	        }
	}

