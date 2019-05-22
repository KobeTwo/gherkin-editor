package de.gherkineditor.controller;

import io.cucumber.messages.Messages;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class SuggestionRestController {


    @Autowired
    Client client;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;


    @RequestMapping(path = "/rest/api/suggest", method = RequestMethod.GET)
    public List<String> suggestSteps(@RequestParam(name = "text", required = true) String text) throws IOException {

        SuggestionBuilder completionSuggestionFuzzyBuilder = SuggestBuilders.completionSuggestion("suggest").prefix(text, Fuzziness.ZERO).skipDuplicates(false).size(10);
        final SearchResponse suggestResponse  =  elasticsearchTemplate.suggest(new SuggestBuilder().addSuggestion("suggest", completionSuggestionFuzzyBuilder), "scenario");

        CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("suggest");
        List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();

        List<String> suggestions = new ArrayList<>();
        for(CompletionSuggestion.Entry.Option option : options){
            suggestions.add(option.getText().string());
        }

        return suggestions;

    }
}
