package application;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class PDFDocument {
	
    void createPDF(MouseEvent event) {
 	   
 	   ObservableList<Node> arr ;
 	   
 	   PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        File file = null;
 	   
 	   FlowPane flowPaneContent = new FlowPane();
 	   arr = flowPaneContent.getChildren();
 	   int size = arr.size();
 	   System.out.println(size);
 	   int begin = 0;
 	   int max = 15;
 	   int mark = 0;
 	   int step = max;
 	   int check = 1;
 	   if(size < max) {
 		   step = size;
 		   check ++;
 	   }
 	   
 	   try {
 		   while(size >= step && check <=2) {
 			   FlowPane subFlowPane = new FlowPane();
 			   System.out.println(size);
 			   subFlowPane.getChildren().addAll(arr.subList(0, step));
	    		   begin = step;
	    		   step += max;
	    		   mark +=15;
	    		   if(step > size) {
	    			   step = size - mark;
	    			   check ++;
	    		   }
	    		   WritableImage nodeshot = subFlowPane.snapshot(new SnapshotParameters(), null);
	    		   file = new File("chart.png");
	    		   
		           try {
		        	   ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
					} catch (IOException e) {
					
					}
		           
		           PDImageXObject pdimage;
		           PDPageContentStream content;		        	           	   
		           pdimage = PDImageXObject.createFromFile("chart.png",doc);
		           content = new PDPageContentStream(doc, page);
		           final PDRectangle mediaBox = page.getMediaBox();
		               
			       content.drawImage(pdimage, 20, 20, mediaBox.getWidth()-40, mediaBox.getHeight()-40);
			       PDFont font = PDType1Font.SYMBOL;
			       content.setFont(font, 12);
			       content.close();
			       doc.addPage(page);
		    	}
 	
            doc.save("pdf_file.pdf");
            doc.close();
            file.delete();
        } catch (IOException ex) {
            //Logger.getLogger(NodeToPdf.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
