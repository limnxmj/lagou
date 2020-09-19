package cn.xmj.service;

import cn.xmj.bean.Position;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PositionSolrService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionSolrService.class);

    @Autowired
    private SolrClient solrClient;

    public List<Position> queryPositionsFromSolr(String queryStr, int start, int rows) {
        try {
            SolrQuery query = new SolrQuery();
            query.setQuery(queryStr);
            query.setStart(start);
            query.setRows(rows);
            QueryResponse queryResponse = solrClient.query(query);
            if (queryResponse == null) {
                return new ArrayList<>();
            }
            return queryResponse.getBeans(Position.class);
        } catch (Exception e) {
            LOGGER.error("queryPositionsFromSolr异常", e);
        }
        return new ArrayList<>();
    }

}
