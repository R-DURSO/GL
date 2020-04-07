package GUI.components.menu.preference_elements;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Simple class that ensures that a {@code JTextField} have a limited length
 * 
 * @author Aldric Vitali Silvestre
 *
 */
class DocumentFieldLimit extends PlainDocument {
	private int limit;

	/**
	 * Specifies the limited length of the JTextField
	 * 
	 * @param limit the limit length desired
	 */
	public DocumentFieldLimit(int limit) {
		super();
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;
		
		int length = getLength() + str.length();
		if (length <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}
