package com.couchbase.grocerysync;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;

import android.text.format.DateFormat;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown=true)
public class GroceryItem extends CouchDbDocument {
	
	private String text;
	private Boolean check;
	private String createdAt;
	
	public GroceryItem() {
		
	}
	
	//eventually this constructor should go away
	//need to patch changes feed to return GroceryItem
	public GroceryItem(JsonNode document) {
		//internals
		JsonNode idNode = document.get("_id");
		if(idNode != null) {
			setId(idNode.getTextValue());
		}
		
		JsonNode revNode = document.get("_rev");
		if(revNode != null) {
			setRevision(revNode.getTextValue());
		}
		
		
		//text
        JsonNode textNode = document.get("text");
        if(textNode != null) {
        	setText(textNode.getTextValue());
        }
        else {
        	setText("");
        }
        
        //check
        JsonNode checkNode = document.get("check");
        if(checkNode != null) {
	        if(checkNode.getBooleanValue()) {
	        	setCheck(Boolean.TRUE);
	        }
	        else {
	        	setCheck(Boolean.FALSE);
	        }
        } 
        
        JsonNode createdNode = document.get("created_at");
        if(createdNode != null) {
        	setCreatedAt(createdNode.getTextValue());
        }         
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Boolean getCheck() {
		return check;
	}
	
	public void setCheck(Boolean check) {
		this.check = check;
	}
	
	@JsonProperty("created_at")
	public String getCreatedAt() {
		return createdAt;
	}
	
	@JsonProperty("created_at")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public void toggleCheck() {
		check = !check;
	}
	
	public static GroceryItem createWithText(String text) {
    	UUID uuid = UUID.randomUUID();
    	Calendar calendar = GregorianCalendar.getInstance();
    	long currentTime = calendar.getTimeInMillis();
    	String currentTimeString = DateFormat.format("EEEE-MM-dd'T'HH:mm:ss.SSS'Z'", calendar).toString();
    	
    	String id = currentTime + "-" + uuid.toString();
    	
    	GroceryItem result = new GroceryItem();
    	
    	result.setId(id);
    	result.setText(text);
    	result.setCheck(Boolean.FALSE);
    	result.setCreatedAt(currentTimeString);
    	
    	return result;
	}

}
