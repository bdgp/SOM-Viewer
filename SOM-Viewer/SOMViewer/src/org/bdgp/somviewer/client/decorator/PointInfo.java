package org.bdgp.somviewer.client.decorator;

import java.util.HashMap;
import java.util.Vector;

import org.bdgp.somviewer.client.DialogBoxClosable;
import org.bdgp.somviewer.client.CategoryComposite.SomOverlayUpdater;
import org.bdgp.somviewer.client.OverlayDrawMap;
import org.bdgp.somviewer.rpc.AbstractLoggingAsyncHandler;
import org.bdgp.somviewer.rpc.ServerService;
import org.bdgp.somviewer.rpc.data.SOMDataOverlay;
import org.bdgp.somviewer.rpc.data.SOMPtInfo;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PointInfo implements PointDecorator {

	protected final static int uuid = 12347;
	int x,y;
	int view_h, view_w;
	int dialog_w = -1;
	int click_x, click_y; // Last click position
	String title;

	protected HashMap<Integer,String> contents;
	protected String label;
	protected Integer id;

	HashMap<Integer,String> infoCache = new HashMap<Integer,String>();
	
	DialogBox dialogBox = null;
	VerticalPanel dialogVPanel = null;
	
	public PointInfo() {
		// TODO Auto-generated constructor stub
	}

	public void setPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setViewPortSize(int w, int h) {
		this.view_h = h;
		this.view_w = w;
	}

	
	public void setInfo(Integer id, String name, HashMap<Integer,String> others) {
		this.id = id;
		this.label = name;
		this.contents = others;
	}
	
	
	public VectorObject drawLabel(String label, ClickHandler onclick) {
		return null;
	}

	public VectorObject drawMarker(boolean showMarker, Vector<String> colors, OverlayDrawMap overlay_map, ClickHandler onclick) {
		return null;
	}

	public void infoQuick(String title, Integer id, int variant, int x, int y) {
		
		click_x = x; click_y = y;
		this.title = title;
		
		if ( infoCache.containsKey(id) )
			infoDialog(infoCache.get(id));
	
		getSInfo(id.intValue(), variant);
	}

	public void infoLong(Integer id) {
	}

	public int uuid() {
		return uuid;
	}


	protected void infoDialog(String html) {
		// Create the popup dialog box
		
		int pos_x, pos_y;
		
		pos_x = click_x; pos_y = click_y;
		
		int win_w = Window.getClientWidth();
		
		if ( dialogBox == null ) {
			dialogBox = new DialogBoxClosable();
			//dialogBox.setText("Information");
			// dialogBox.setText(label);
			dialogBox.setAnimationEnabled(true);
			dialogVPanel = new VerticalPanel();
		} else {
			dialogVPanel.clear();
		}

		dialogBox.setText(title);

		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML(html));
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		// dialogBox.setPopupPosition(Window.getClientWidth()/2, Window.getClientHeight()/2);
		
		if ( pos_x > win_w / 2 ) {
			//TODO
			// 
			if ( dialog_w < 0 ) {
				if ( dialog_w == - 1 ) {
					String dw = dialogBox.getElement().getStyle().getProperty("width");
					// String dw = dialogBox.getElement().getStyle().getWidth();
					if ( dw.length() > 2 && dw.substring(dw.length()-2, dw.length()).compareTo("px") == 0 ) {
						dialog_w = Integer.valueOf( dw.substring(0,dw.length()-2) );
					} else {
						dialog_w = -2;
					}
				}
			} else {
				pos_x -= dialog_w;
			}
		}
		
		dialogBox.setPopupPosition(pos_x, pos_y);
		
		dialogBox.show();

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

	}
	
	
	protected void getSInfo(int id, int variant) {
		ServerService.getInstance().getPtInfo(id, variant, new SomPtInfoUpdater());
	}
	
	/**
	 * @author erwin
	 * RPC incoming data for the overlays
	 */
	public class SomPtInfoUpdater extends AbstractLoggingAsyncHandler {
		
		public void handleFailure(Throwable caught) {
			
		}
		
		public void handleSuccess(Object result) {
			SOMPtInfo data = (SOMPtInfo) result;
			
			if ( data.queryResult != null ) {
				setLogEntry("Pt Info: ERROR " + data.queryResult);
			} else {
				setLogEntry("Pt Info: Received " + data.req_id);
				infoCache.put(data.req_id, data.html_Sinfo);
				infoDialog(data.html_Sinfo);
			}
		}
		
	}

	public boolean isDraw() {
		return false;
	}

	public boolean isInfo() {
		return true;
	}

	
	
}
