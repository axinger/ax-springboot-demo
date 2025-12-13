package com.github.axinger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.github.axinger.model.BookEntity;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class A21EsApplicationTest {

    @Autowired
    private ElasticsearchClient client;

    @Test
    public void test02() throws IOException {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(1);
        bookEntity.setName("海底两万里");
        bookEntity.setAuthor("xxx");

        IndexResponse response = client.index(e -> e.index("books")
                .document(bookEntity)
                .id("1")
        );
        System.out.println("response.result() = " + response.result());
        System.out.println("response.id() = " + response.id());
        System.out.println("response.seqNo() = " + response.seqNo());
        System.out.println("response.index() = " + response.index());
        System.out.println("response.shards() = " + response.shards());

    }

    @Test
    public void test03() throws IOException {

        // 构建查询请求
        SearchRequest request = new SearchRequest.Builder()
                .index("books")
                .query(q -> q.match(m -> m.field("name").query("海底"))) // 模糊搜索
                .build();

        // 执行查询并获取响应
        SearchResponse<BookEntity> response = client.search(request, BookEntity.class);
        // 解析响应结果
        List<Hit<BookEntity>> hits = response.hits().hits();
        List<BookEntity> books = hits.stream()
                .map(Hit::source)
                .toList();

        System.out.println("books = " + books);

    }

    @SneakyThrows
    @Test
    void test1() {

        CreateIndexResponse createIndexResponse = client.indices().create(
                e ->
                        e.index("javaboy_books")
                                .settings(s ->
                                        s.numberOfShards("3")
                                                .numberOfReplicas("1"))
                                .mappings(m ->
                                        m.properties("name", p -> p.text(f -> f.analyzer("ik_max_word")))
                                                .properties("birthday", p -> p.date(d -> d.format("yyyy-MM-dd"))))
                                .aliases("books_alias", f -> f.isWriteIndex(true)));
        System.out.println("createResponse.acknowledged() = " + createIndexResponse.acknowledged());
        System.out.println("createResponse.index() = " + createIndexResponse.index());
        System.out.println("createResponse.shardsAcknowledged() = " + createIndexResponse.shardsAcknowledged());
    }
}
