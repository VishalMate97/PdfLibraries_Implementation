package com.model;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.lang.Nullable;

public class textFontData {
	private String text;
	private PDFont font;
	private float fontSize;
	private float leading;
	
	
	public textFontData(String text, PDFont font, float fontSize, float leading) {
		super();
		this.text = text;
		this.font = font;
		this.fontSize = fontSize;
		this.leading = leading;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public PDFont getFont() {
		return font;
	}
	public void setFont(PDFont font) {
		this.font = font;
	}
	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	public float getLeading() {
		return leading;
	}
	public void setLeading(float leading) {
		this.leading = leading;
	}

	
	
}
