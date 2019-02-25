package de.gherkineditor.controller;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class SuggestionRestController {


    @Autowired
    Client client;


    @RequestMapping(path = "/rest/api/suggest", method = RequestMethod.GET)
    public List<String> suggestSteps(@RequestParam(name = "text", required = true) String text) throws IOException {


        AggregationBuilder aggregation = AggregationBuilders.nested("nested", "steps")
                .subAggregation(AggregationBuilders
                        .filter("filter", QueryBuilders
                                .boolQuery()
                                .filter(QueryBuilders.prefixQuery("steps.text.raw", text)))
                        .subAggregation(AggregationBuilders.terms("top_steps")
                                .field("steps.text.raw")
                                .order(Terms.Order.count(false))));


        SearchResponse response = this.client.prepareSearch("scenario")
                .setTypes("scenario")
                .addAggregation(aggregation)
                .execute().actionGet();

        Nested nested = response.getAggregations().get("nested");
        Filter filter = nested.getAggregations().get("filter");
        StringTerms topSteps = filter.getAggregations().get("top_steps");

        List<String> suggestions = topSteps.getBuckets()
                .stream()
                .map(b -> b.getKeyAsString())
                .collect(Collectors.toList());

        return suggestions;

    }
}