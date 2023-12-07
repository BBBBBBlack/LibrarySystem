package com.bbbbbblack.utils;

import cn.hutool.json.JSONObject;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Like;
import co.elastic.clients.elasticsearch._types.query_dsl.LikeBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.MoreLikeThisQuery;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import com.bbbbbblack.configuration.esConfiguration.ESClientPool;
import com.bbbbbblack.domain.entity.Book;
import com.bbbbbblack.domain.entity.Search;
import com.bbbbbblack.domain.vo.BookVo;
import com.bbbbbblack.domain.vo.PageVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
public class ESUtil {
    /**
     * bulk批量插入 指定id
     *
     * @return List<BulkResponseItem>
     * @throws Exception
     */
    public List<BulkResponseItem> bulkInsert(String indexName, List<JSONObject> bookList) throws Exception {
        ElasticsearchClient client = ESClientPool.getClient();
        //创建BulkOperation列表准备批量插入doc
        List<BulkOperation> bulkOperations = new ArrayList<>();
        //将id作为es id
        for (int i = 0; i < bookList.size(); i++) {
            int finalI = i;
            bulkOperations.add(BulkOperation
                    .of(b -> b
                            .index(c -> c
                                    .id(bookList.get(finalI).get("id").toString())
                                    .document(bookList.get(finalI)
                                    )
                            )
                    )
            );
        }
        BulkResponse bulk = client
                .bulk(x -> x
                        .index(indexName)
                        .operations(bulkOperations)
                );
        List<BulkResponseItem> items = bulk.items();
        ESClientPool.returnClient(client);
        return items;
    }

    /**
     * bulk批量删除文档记录
     *
     * @return List<BulkResponseItem>
     * @throws Exception
     */
    public List<BulkResponseItem> delDocByIds(String indexName, List<JSONObject> bookList) throws Exception {
        ElasticsearchClient client = ESClientPool.getClient();
        // 构建批量操作对象BulkOperation的集合
        List<BulkOperation> bulkOperations = new ArrayList<>();
        // 向集合中添加需要删除的文档id信息
        for (int i = 0; i < bookList.size(); i++) {
            int finalI = i;
            bulkOperations.add(BulkOperation
                    .of(b -> b
                            .delete((d -> d
                                    .index(indexName)
                                    .id(bookList.get(finalI).get("id").toString()
                                    )
                            ))
                    ));
        }
        // 调用客户端的bulk方法，并获取批量操作响应结果
        BulkResponse response = client
                .bulk(e -> e
                        .index(indexName)
                        .operations(bulkOperations));
        return response.items();
    }

    /**
     * bluk批量更新数据
     *
     * @return List<BulkResponseItem> items
     * @throws Exception
     */
    public List<BulkResponseItem> bulkUpdate(String indexName, List<JSONObject> bookList) throws Exception {
        ElasticsearchClient client = ESClientPool.getClient();
        //创建BulkOperation列表准备批量插入doc
        List<BulkOperation> bulkOperations = new ArrayList<>();
        //将id作为es id，也可不指定id es会自动生成id
        for (int i = 0; i < bookList.size(); i++) {
            int finalI = i;
            bulkOperations.add(BulkOperation.of(b -> b
                    .update(u -> u
                            .index(indexName)
                            .id(bookList.get(finalI).get("id").toString())
                            .action(a -> a
                                    .doc(bookList.get(finalI))
                            ))
            ));
        }
        BulkResponse bulk = client
                .bulk(x -> x
                        .index(indexName)
                        .operations(bulkOperations));
        List<BulkResponseItem> items = bulk.items();
        ESClientPool.returnClient(client);
        return items;
    }

    public static List<BookVo> queryBoolKeywords(String indexName, Search search) throws Exception {
        ElasticsearchClient client = ESClientPool.getClient();
        SearchResponse<Book> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .bool(bool -> bool
                                        .must(must -> {
                                            Integer type = search.getType();
                                            if (type != null) {
                                                if (1 == type) {
                                                    //书名
                                                    return must.match((match -> match
                                                            .field("title")
                                                            .query(search.getKeywords())
                                                    ));
                                                } else if (2 == type) {
                                                    //作者
                                                    return must.match((match -> match
                                                            .field("author")
                                                            .query(search.getKeywords())
                                                    ));
                                                } else {
                                                    //内容
                                                    return must.match((match -> match
                                                            .field("introduction")
                                                            .query(search.getKeywords())
                                                    ));
                                                }
                                            } else {
                                                return must.matchAll(m -> m);
                                            }
                                        })
                                        .must(must -> {
                                            if (search.getBookType() != null) {
                                                return must.term(t -> t
                                                        .field("type")
                                                        .value(search.getBookType())
                                                );
                                            } else {
                                                return must.matchAll(m -> m);
                                            }
                                        })
                                        .filter(f -> f
                                                .range(r -> r
                                                        .field("price")
                                                        .gt((Objects.isNull(search.getLowPrice()) ? null : JsonData.of(Double.parseDouble(search.getLowPrice().toString()))))
                                                        .lt((Objects.isNull(search.getHighPrice()) ? null : JsonData.of(Double.parseDouble(search.getHighPrice().toString()))))
                                                )
                                        )
                                ))
//                        .highlight(h -> h
//                                .fields(String.valueOf(query), f -> f
//                                        .preTags("<font color='yellow'>")
//                                        .postTags("</font>")
//                                )
//                        )
                        .from(search.getFrom())
                        .size(PageVo.pageSize.intValue())
                , Book.class);
        List<Hit<Book>> hits = searchResponse.hits().hits();
        List<BookVo> bookVoList = new ArrayList<>();
        for (Hit<Book> hit : hits) {
            Book source = hit.source();
            assert source != null;
            bookVoList.add(new BookVo(source));
        }
        ESClientPool.returnClient(client);
        return bookVoList;
    }

    public static List<BookVo> getMoreLikeThis(String indexName, List<String> keywords, Integer from) throws Exception {
        List<Like> likeList=new ArrayList<>();
        for (String keyword : keywords) {
            Like.Builder builder=new Like.Builder();
            Like like = builder.text(keyword).build();
            likeList.add(like);
        }
        ElasticsearchClient client = ESClientPool.getClient();
        SearchResponse<Book> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .moreLikeThis(new Function<MoreLikeThisQuery.Builder, ObjectBuilder<MoreLikeThisQuery>>() {
                                                  @Override
                                                  public ObjectBuilder<MoreLikeThisQuery> apply(MoreLikeThisQuery.Builder m) {
                                                      return m
                                                              .fields("title", "introduction")
                                                              .like(likeList)
                                                              .minDocFreq(0)
                                                              .minTermFreq(0);
                                                  }
                                              }
                                ))
                        .from(from)
                        .size(PageVo.pageSize.intValue())
                , Book.class);
        List<Hit<Book>> hits = searchResponse.hits().hits();
        List<BookVo> bookVoList = new ArrayList<>();
        for (Hit<Book> hit : hits) {
            Book source = hit.source();
            assert source != null;
            source.setId(null);
            bookVoList.add(new BookVo(source));
        }
        ESClientPool.returnClient(client);
        return bookVoList;
    }

    //自动补全标题
    public static List<String> autoTitle(String indexName, String keywords) throws Exception {
        ElasticsearchClient client = ESClientPool.getClient();
        SearchResponse<Book> searchResponse = client.search(e -> e
                        .index(indexName)
                        .suggest(s -> s.suggesters("title_suggest", t -> t
                                        .text(keywords)
                                        .completion(f -> f
                                                .field("title_suggest")
                                                .skipDuplicates(true)
                                                .size(PageVo.pageSize.intValue())
                                        )
                                )
                        )
                , Book.class);
        List<String> titles = new ArrayList<>();
        List<CompletionSuggestOption<Book>> op = searchResponse.suggest().get("title_suggest").get(0).completion().options();
        for (CompletionSuggestOption<Book> option : op) {
            Book book = option.source();
            assert book != null;
            titles.add(book.getTitle());
        }
        return titles;
    }

    //自动补全作者
    public static List<String> autoAuthor(String indexName, String keywords) throws Exception {
        ElasticsearchClient client = ESClientPool.getClient();
        SearchResponse<Book> searchResponse = client.search(e -> e
                        .index(indexName)
                        .suggest(s -> s.suggesters("author_suggest", t -> t
                                        .text(keywords)
                                        .completion(f -> f
                                                .field("author_suggest")
                                                .skipDuplicates(true)
                                                .size(PageVo.pageSize.intValue()))
                                )
                        )
                , Book.class);
        List<String> authors = new ArrayList<>();
        List<CompletionSuggestOption<Book>> op = searchResponse.suggest().get("author_suggest").get(0).completion().options();
        for (CompletionSuggestOption<Book> option : op) {
            Book book = option.source();
            assert book != null;
            authors.add(book.getAuthor());
        }
        return authors;
    }


}
