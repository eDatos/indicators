package es.gobcan.istac.indicators.web.shared.db;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AnalysisDimension extends IndicatorSystemContent implements IsSerializable {

	private Long id; 
	private List<IndicatorSystemContent> content;

	public AnalysisDimension() {
	}
	
	public AnalysisDimension(Long id, String name) {
		this.id = id;
		this.name = name;
		this.content = new ArrayList<IndicatorSystemContent>();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public List<IndicatorSystemContent> getContent() {
		return content;
	}
	
	public void addElement(IndicatorSystemContent element) {
		content.add(element);
	}
	
}
