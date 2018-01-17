import java.io.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

public class Html2Text extends HTMLEditorKit.ParserCallback {
	StringBuffer s;

	public Html2Text() {
	}

	public void parse(Reader in) throws IOException {
		s = new StringBuffer();
		ParserDelegator delegator = new ParserDelegator();
		// the third parameter is TRUE to ignore charset directive
		delegator.parse(in, this, Boolean.TRUE);
	}

	public void handleText(char[] text, int pos) {
		s.append(text);
	}

	public String getText() {
		return s.toString();
	}

	public String getTextFromHTML(String almText) {
		String returnText = almText;
		try {
			// the HTML to convert
			StringReader in = new StringReader(almText);
			Html2Text parser = new Html2Text();
			parser.parse(in);
			in.close();
			returnText = parser.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnText;
	}

	public static void main(String[] args) {
		
		Html2Text parser = new Html2Text();
		parser.getTextFromHTML("<html><body>Verify that the physical address field<b><i> </i></b>in Lawson contains the same information as the data address from MDM.<br /><br /><font color=\"#FF0000\"><b>**Note: Validate about 50 random &quot;Active&quot; addressed to pass this test.</b></font></body></html>");
	}
}