package sk.upjs.neo4j_repository.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import sk.upjs.nosql_data_source.entity.Download;
import sk.upjs.nosql_data_source.entity.Page;

@NodeEntity(label = "download_dd")
public class NeoDownload {

	@Id
	@GeneratedValue
	private Long id;
	@Property
	private LocalDateTime startTime;
	@Property
	private LocalDateTime endTime;
	@Property
	private boolean finished;
	@Property
	private String url;
	@Property
	private String country;
	@Property
	private String language;
	@Relationship(type="SEED_PAGE_dd")
	private NeoPage seedPage;
	@Relationship(type="CONTAINS_dd")
	private Collection<NeoPage> pages = new HashSet<NeoPage>();

	public NeoDownload() {

	}
	
	public NeoDownload(Download download) {
//		this.id = download.getId();
		this.startTime = download.getStartTime();
		this.endTime = download.getEndTime();
		this.finished = download.isFinished();
		this.url = download.getUrl();
		this.country = download.getCountry();
		this.language = download.getLanguage();
		Map<String, NeoPage> urlToPage = new HashMap<String, NeoPage>();
		for (Page page: download.getPages()) {
			NeoPage neoPage = urlToPage.get(page.getUrl());
			if (neoPage == null) {
				neoPage = new NeoPage(page, this);
				urlToPage.put(page.getUrl(), neoPage);
				pages.add(neoPage);
			}
			if (download.getSeedPage().getUrl().equals(page.getUrl())) {
				this.seedPage = neoPage;
			}
		}
		
		for (Page page: download.getPages()) {
			Set<LinksTo> links = new HashSet<LinksTo>();
			NeoPage startPage = urlToPage.get(page.getUrl());
			for (Entry<String, Page> rel: page.getxPathToChildrenPages().entrySet()) {
				NeoPage endPage = urlToPage.get(rel.getValue().getUrl());
				links.add(new LinksTo(startPage, endPage, rel.getKey()));
			}
			startPage.setxPathToChildrenPages(links);
		}
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public NeoPage getSeedPage() {
		return seedPage;
	}

	public void setSeedPage(NeoPage seedPage) {
		this.seedPage = seedPage;
	}

	public Collection<NeoPage> getPages() {
		return pages;
	}

	public void setPages(Collection<NeoPage> pages) {
		this.pages = pages;
	}

	@Override
	public String toString() {
		return "NeoDownload [id=" + id + ", url=" + url + "]";
	}

}
