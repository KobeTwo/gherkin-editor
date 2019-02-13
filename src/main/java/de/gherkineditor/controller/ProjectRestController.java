package de.gherkineditor.controller;

import de.gherkineditor.facade.ProjectFacade;
import de.gherkineditor.model.Project;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class ProjectRestController {

    @Autowired
    ProjectFacade projectFacade;

    @Autowired
    Client client;

    @RequestMapping(value = "/rest/api/project/import", method = RequestMethod.POST, produces = "application/json")
    public Project importProject(@RequestParam(name = "file", required = true) MultipartFile file, @RequestParam(name = "projectId", required = true) String projectId) {
        return this.projectFacade.importProject(projectId, file);
    }

    @RequestMapping(path = "/rest/api/project/export", method = RequestMethod.GET)
    public ResponseEntity<Resource> exportProject(@RequestParam(name = "projectId", required = true) String projectId) throws IOException {

        OutputStream out = this.projectFacade.exportProject(projectId);

        String fileName = projectId + "_export.zip";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        ByteArrayResource resource = new ByteArrayResource(((ByteArrayOutputStream) out).toByteArray());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @RequestMapping(path = "/rest/api/project/suggest", method = RequestMethod.GET)
    public List<String> suggestSteps(@RequestParam(name = "projectId", required = true) String projectId, @RequestParam(name = "text", required = true) String text) throws IOException {


        AggregationBuilder aggregation = AggregationBuilders.nested("nested", "steps")
                .subAggregation(AggregationBuilders
                        .filter("filter", QueryBuilders.prefixQuery("steps.text.raw", text))
                        .subAggregation(AggregationBuilders.terms("top_steps")
                                .field("steps.text.raw")
                                .order(Terms.Order.count(false))));


        QueryBuilder query = QueryBuilders.boolQuery()
                .filter(QueryBuilders
                        .termQuery("projectId.raw", projectId))
                .must(QueryBuilders.nestedQuery("steps", QueryBuilders.termQuery("steps.text", text), ScoreMode.Avg));

        QueryBuilder query2 = QueryBuilders.nestedQuery("steps", QueryBuilders
                .termQuery("steps.text", text), ScoreMode.Avg)
                .innerHit(new InnerHitBuilder());

        SearchResponse response = this.client.prepareSearch("scenario")
                .setTypes("scenario")
                //.setQuery(query2)
                .addAggregation(aggregation)
                .execute().actionGet();

        Nested nested = response.getAggregations().get("nested");
        StringTerms topSteps = nested.getAggregations().get("top_steps");

        List<String> suggestions = topSteps.getBuckets()
                .stream()
                .map(b -> b.getKeyAsString())
                .collect(Collectors.toList());

        return suggestions;

    }
}
