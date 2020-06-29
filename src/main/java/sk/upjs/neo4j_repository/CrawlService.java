package sk.upjs.neo4j_repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.upjs.neo4j_repository.entity.LinksTo;
import sk.upjs.neo4j_repository.entity.NeoDownload;
import sk.upjs.neo4j_repository.entity.NeoPage;
import sk.upjs.nosql_data_source.entity.Download;
import sk.upjs.nosql_data_source.persist.DaoFactory;
import sk.upjs.nosql_data_source.persist.DownloadDao;

@Service
public class CrawlService {

	@Autowired
	private SessionFactory sessionFactory;

	public void fillDb() {
		DownloadDao downloadDao = DaoFactory.INSTANCE.getDownloadDao();
		List<Download> allDownloads = downloadDao.getAllDownloads();
		NeoDownload neoDownload = new NeoDownload(allDownloads.get(0));
		Session session = sessionFactory.openSession();
		session.save(neoDownload);
	}

	public void printDetailPages() {
		Session session = sessionFactory.openSession();
		String query = "MATCH (det:page_jdz {is_detail_page: true}) RETURN det";
		Iterable<NeoPage> result = session.query(NeoPage.class, query, new HashMap<>());
		for (NeoPage page : result) {
			System.out.println(page);
		}
	}

	public void printShortestPathsToDetailPages() {
		Session session = sessionFactory.openSession();
		String query = "MATCH (seed:page_jdz)<-[:SEED_PAGE_jdz]-(:download_jdz), (det:page_jdz {is_detail_page: true}), "
				+ "p = shortestPath((seed)-[:LINKS_TO_jdz]-(det)) RETURN nodes(p) AS spath";
		Result result = session.query(query, new HashMap<>());
		for (Map<String, Object> map : result.queryResults()) {
			Collection<NeoPage> pages = (Collection<NeoPage>) map.get("spath");
			System.out.println(pages.size() + ": ");
			for (NeoPage page : pages) {
				System.out.print(page);
			}
			System.out.println();
		}
	}

	public void printAllShortestPathsToDetailPages() {
         Session session = sessionFactory.openSession();
         String cypherQuery = "MATCH (seed:page_jdz)<-[:SEED_PAGE_jdz]-(:download_jdz),(det:page_jdz {is_detail_page: true}), "
                         + "p = allShortestPaths((seed)-[:LINKS_TO_jdz*]-(det)) " +
                         " RETURN nodes(p) as spath";
         Map<String,?> parameters = new HashMap<>();
         Result result = session.query(cypherQuery, parameters);
         int i = 1;
         for (Map<String, Object> map : result.queryResults()) {
                 Collection<NeoPage> path = (Collection<NeoPage>) map.get("spath");
                 StringBuilder sb = new StringBuilder();
                 sb.append(i++).append(": ");
                 for (NeoPage page : path) {
                         sb.append(page.getUrl()).append(" >> ");
                 }
                 System.out.println(sb.toString().substring(0, sb.length()-4));
         }
 }

	public void printAllShortestPathsToDetailPagesWithXPaths() {
         Session session = sessionFactory.openSession();
         String cypherQuery = "MATCH (seed:page_jdz)<-[:SEED_PAGE_jdz]-(:download_jdz),(det:page_jdz {is_detail_page: true}), "
                         + "p = allShortestPaths((seed)-[:LINKS_TO_jdz*]-(det)) " +
                         " RETURN nodes(p) as spath, rels(p) as rel";
         Map<String,?> parameters = new HashMap<>();
         Result result = session.query(cypherQuery, parameters);
         int i = 1;
         for (Map<String, Object> map : result.queryResults()) {
                 Collection<LinksTo> rels = (Collection<LinksTo>)map.get("rel");
                 StringBuilder sb = new StringBuilder();
                 sb.append(i++).append(": ");
                 for (LinksTo rel : rels) {
                         sb.append(rel.getxPath()).append(" - ");
                         sb.append(rel.getStartPage().getUrl()).append(" >> ");
                 }
                 System.out.println(sb.toString().substring(0, sb.length()-4));
         }
 }
}
