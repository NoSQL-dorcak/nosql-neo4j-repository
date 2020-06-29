package sk.upjs.neo4j_repository.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import sk.upjs.nosql_data_source.entity.Download;
import sk.upjs.nosql_data_source.entity.Page;

@NodeEntity(label = "page_dd")
public class NeoPage {

	@Id
	@GeneratedValue
	private Long id;
	@Property
	private String url;
	@Relationship(type = "PART_OF_dd")
	private NeoDownload download;
	@Relationship(type = "LINKS_TO_dd")
	private Set<LinksTo> xPathToChildrenPages = new HashSet<>();
	@Property(name = "is_detail_page")
	private boolean isDetailPage;

	public NeoPage() {
	}

	public NeoPage(Page page, NeoDownload neoDownload) {
//		this.id = page.getId();
		this.url = page.getUrl();
		this.download = neoDownload;
		this.isDetailPage = page.isDetailPage();
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public NeoDownload getDownload() {
		return download;
	}

	public void setDownload(NeoDownload download) {
		this.download = download;
	}

	public Set<LinksTo> getxPathToChildrenPages() {
		return xPathToChildrenPages;
	}

	public void setxPathToChildrenPages(Set<LinksTo> xPathToChildrenPages) {
		this.xPathToChildrenPages = xPathToChildrenPages;
	}

	public boolean isDetailPage() {
		return isDetailPage;
	}

	public void setDetailPage(boolean isDetailPage) {
		this.isDetailPage = isDetailPage;
	}

	@Override
	public String toString() {
		return "NeoPage [id=" + id + ", url=" + url + ", isDetailPage=" + isDetailPage + "]";
	}

}
