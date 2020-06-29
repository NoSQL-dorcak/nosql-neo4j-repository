package sk.upjs.neo4j_repository.entity;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "LINKS_TO_dd")
public class LinksTo {

	@Id
	@GeneratedValue
	private Long id;
	@StartNode
	private NeoPage startPage;
	@EndNode
	private NeoPage endPage;
	@Property(name = "xpath")
	private String xPath;

	public LinksTo() {

	}

	public LinksTo(NeoPage startPage, NeoPage endPage, String xPath) {
		super();
		this.startPage = startPage;
		this.endPage = endPage;
		this.xPath = xPath;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NeoPage getStartPage() {
		return startPage;
	}

	public void setStartPage(NeoPage startPage) {
		this.startPage = startPage;
	}

	public NeoPage getEndPage() {
		return endPage;
	}

	public void setEndPage(NeoPage endPage) {
		this.endPage = endPage;
	}

	public String getxPath() {
		return xPath;
	}

	public void setxPath(String xPath) {
		this.xPath = xPath;
	}

	@Override
	public String toString() {
		return "LinksTo [xPath=" + xPath + "]";
	}

}
