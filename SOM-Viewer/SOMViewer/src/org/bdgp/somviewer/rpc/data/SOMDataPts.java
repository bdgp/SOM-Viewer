package org.bdgp.somviewer.rpc.data;

import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SOMDataPts implements IsSerializable {

	public String queryResult = null;
	public String requested = null;
	public String map = null;
	
	public int [] id;
	public float [] x, y;
	public String [] names;
		
	public Vector<SOMOverlaysAvailable> available;
	
	public SOMOverlaysAvailable CreateAvailable(String name, int variant, String color) {
		SOMOverlaysAvailable oa = new SOMOverlaysAvailable();
		oa.name = name;
		oa.variant = variant;
		oa.color = color;
		return oa;
	}
//	public HashMap<String,String> shortinfo = null;
//	public HashMap<String,String> longinfo = null;
		
}