import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.SAXException;


public class testParseConvert {
  public static void main(final String[] args) throws IOException,TikaException {

    BodyContentHandler handler = new BodyContentHandler();
    ToXMLContentHandler handler1 = new ToXMLContentHandler();
    Metadata metadata = new Metadata();
    FileInputStream inputstream = new FileInputStream(new File("/Users/charana/Documents/finitude/tikexample/src/main/java/Example.pdf"));
    ParseContext pcontext = new ParseContext();

    //parsing the document using PDF parser
    PDFParser pdfparser = new PDFParser();
    pdfparser.setSortByPosition(true);
    try {
//      pdfparser.parse(inputstream, handler, metadata, pcontext);
      pdfparser.parse(inputstream, handler1, metadata, pcontext);
    }
    catch (SAXException e){
      System.out.println(e);
    }


    //getting the content of the document
    GoogleTranslator translator = new GoogleTranslator();
//    System.out.println("Contents of the PDF :" + translator.translate(handler.toString(),"de","en"));
    System.out.println("Content:\n"+handler1.toString());

    //getting metadata of the document
    System.out.println("Metadata of the PDF:");
    String[] metadataNames = metadata.names();

    for(String name : metadataNames) {
      System.out.println(name+ " : " + metadata.get(name));
    }
  }
}
