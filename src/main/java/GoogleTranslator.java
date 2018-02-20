import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.language.LanguageProfile;
import org.apache.tika.language.translate.Translator;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

public class GoogleTranslator implements Translator {
  private static final String GOOGLE_TRANSLATE_URL_BASE = "https://www.googleapis.com/language/translate/v2";
  private static final Logger LOG = Logger.getLogger(org.apache.tika.language.translate.GoogleTranslator.class.getName());
  private WebClient client = WebClient.create("https://www.googleapis.com/language/translate/v2");
  private String apiKey="AIzaSyBVkaFQ3qVgxOTlOM3hAJ2swE77W0LQAFo";
  private boolean isAvailable = true;

  public GoogleTranslator() {
    Properties config = new Properties();

    try {
      config.load(org.apache.tika.language.translate.GoogleTranslator.class.getResourceAsStream("translator.google.properties"));
      if(this.apiKey.equals("dummy-secret")) {
        this.isAvailable = false;
      }
    } catch (Exception var3) {
      var3.printStackTrace();
      this.isAvailable = false;
    }

  }

  public String translate(String text, String sourceLanguage, String targetLanguage) throws TikaException, IOException {
    if(!this.isAvailable) {
      return text;
    } else {
      Response response = this.client.accept(new String[]{"application/json"}).query("key", new Object[]{this.apiKey}).query("source", new Object[]{sourceLanguage}).query("target", new Object[]{targetLanguage}).query("q", new Object[]{text}).get();
      BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)response.getEntity(), IOUtils.UTF_8));
      String line = null;
      StringBuffer responseText = new StringBuffer();

      while((line = reader.readLine()) != null) {
        responseText.append(line);
      }

      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonResp = mapper.readTree(responseText.toString());
      return (String)jsonResp.findValuesAsText("translatedText").get(0);
    }
  }

  public String translate(String text, String targetLanguage) throws TikaException, IOException {
    if(!this.isAvailable) {
      return text;
    } else {
      LanguageIdentifier language = new LanguageIdentifier(new LanguageProfile(text));
      String sourceLanguage = language.getLanguage();
      return this.translate(text, sourceLanguage, targetLanguage);
    }
  }

  public boolean isAvailable() {
    return this.isAvailable;
  }
}
