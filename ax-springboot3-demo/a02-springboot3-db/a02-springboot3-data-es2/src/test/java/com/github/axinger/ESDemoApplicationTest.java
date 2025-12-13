package com.github.axinger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.github.axinger.model.Book;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ESDemoApplicationTest {

    @Autowired
    private ElasticsearchClient client;

    @Test
    public void test07() throws IOException {
        Book book = new Book();
        book.setId(890);
        book.setName("深入理解Java虚拟机");
        book.setAuthor("xxx");
        // 添加一个文档
        // 这是一个同步请求，请求会卡在这里
        IndexResponse response = client.index(i -> i.index("books").document(book).id("890"));
        System.out.println("response.result() = " + response.result());
        System.out.println("response.id() = " + response.id());
        System.out.println("response.seqNo() = " + response.seqNo());
        System.out.println("response.index() = " + response.index());
        System.out.println("response.shards() = " + response.shards());
    }

    @SneakyThrows
    @Test
    void test1() {

        CreateIndexResponse createIndexResponse = client.indices().create(
                c ->
                        c.index("javaboy_books")
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
