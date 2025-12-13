package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.config.ESConfig;
import com.github.axinger.model.Account;
import com.github.axinger.model.User;
import lombok.SneakyThrows;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    @SuppressWarnings("deprecation")
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {

        System.out.println("restHighLevelClient = " + restHighLevelClient);
    }


    @SneakyThrows
    @Test
    void indexData() {

//        Map map = new HashMap();
//        map.put("userName","jim");
//        map.put("age",10);
//        IndexRequest request = new IndexRequest("users")
//                .id("1")
//                .source(map);


        // 放入对象,转JSON
        User user = User.builder().userName("jim").gender("F").age(20).build();

        final String jsonString = JSON.toJSONString(user);
        IndexRequest request = new IndexRequest("users")
                .id("1")
                .source(jsonString, XContentType.JSON);

        // 同步处理,保存或更新
        final IndexResponse indexResponse = restHighLevelClient.index(request, ESConfig.COMMON_OPTIONS);
        System.out.println("indexResponse = " + indexResponse);

    }

    /**
     * 聚合复杂
     */
    @SneakyThrows
    @Test
    void searchData() {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 检索条件
        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));

        sourceBuilder.from(0);
        sourceBuilder.size(100);
        // 聚合

        // term 年龄分布
        final TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);

        // avg 薪资 平均值
        final AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);


        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        searchRequest.source(sourceBuilder);


        final SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ESConfig.COMMON_OPTIONS);
//        System.out.println("searchResponse = " + searchResponse.toString());
//        System.out.println("searchResponse.getHits().getHits() = " + searchResponse.getHits().getHits().toString());
//        Arrays.stream(searchResponse.getHits().getHits()).forEach(val ->{
//            final String string = val.getSourceAsString();
//            final Account account = JSON.parseObject(string, Account.class);
//            System.out.println("val = " + val);
//        });

        final List<Account> list = Arrays.stream(searchResponse.getHits().getHits()).map(val -> {

            // 获得 _source
            final String string = val.getSourceAsString();
            final Account account = JSON.parseObject(string, Account.class);
            System.out.println("account = " + account);
            return account;
        }).collect(Collectors.toList());
        System.out.println("list = " + list);

        // 获得聚合信息
        final Aggregations aggregations = searchResponse.getAggregations();
        final Terms ageAgg1 = aggregations.get("ageAgg");
        ageAgg1.getBuckets().forEach(val -> {
            System.out.println("年龄分布 = " + val.getKeyAsString() + "人数 = " + val.getDocCount());
        });


        final Avg balanceAvg1 = aggregations.get("balanceAvg");
        System.out.println("平均薪资 = " + balanceAvg1.getValue());
    }
}
