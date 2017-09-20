package com.stephen.zhihu.service;

import com.stephen.zhihu.Constants;
import com.stephen.zhihu.base.UpdateMessageInfo;
import com.stephen.zhihu.domain_elasticsearch.QuestionDoc;
import com.stephen.zhihu.domain_elasticsearch.TopicDoc;
import com.stephen.zhihu.domain_elasticsearch.UserDoc;
import com.stephen.zhihu.elasticsearch_repository.QuestionDocRepository;
import com.stephen.zhihu.elasticsearch_repository.TopicDocRepository;
import com.stephen.zhihu.elasticsearch_repository.UserDocRepository;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private QuestionDocRepository questionDocRepository;
    private UserDocRepository userDocRepository;
    private TopicDocRepository topicDocRepository;
    private ElasticsearchOperations operations;

    @Autowired
    public ElasticsearchServiceImpl(QuestionDocRepository questionDocRepository,
                                    UserDocRepository userDocRepository,
                                    TopicDocRepository topicDocRepository,
                                    ElasticsearchOperations operations) {
        this.questionDocRepository = questionDocRepository;
        this.userDocRepository = userDocRepository;
        this.topicDocRepository = topicDocRepository;
        this.operations = operations;
    }

    @Override
    @Async(Constants.ASYNC_EXECUTOR)
    public void saveQuestionDoc(QuestionDoc doc) {
        questionDocRepository.save(doc);
    }

    @Override
    @Async(Constants.ASYNC_EXECUTOR)
    public void saveUserDoc(UserDoc doc) {
        userDocRepository.save(doc);
    }

    @Override
    @Async(Constants.ASYNC_EXECUTOR)
    public void saveTopicDoc(TopicDoc doc) {
        topicDocRepository.save(doc);
    }

    @Override
    @Async(Constants.ASYNC_EXECUTOR)
    public void updateDoc(Class clazz, String type, String docId, UpdateMessageInfo... messages) {
        try {
            UpdateRequest ur = new UpdateRequest();
            ur.index("zhihu");
            ur.type(type);
            ur.id(docId);
            ur.doc(getBuilderByUpdateMessages(messages));

            UpdateQuery query = new UpdateQueryBuilder().withId(docId).withClass(clazz).withUpdateRequest(ur).build();
            operations.update(query);
        } catch (IOException e) {
            // 打log
        }
    }

    private XContentBuilder getBuilderByUpdateMessages(UpdateMessageInfo[] infos) throws IOException {
        XContentBuilder builder = jsonBuilder().startObject();

        for (UpdateMessageInfo info : infos) {
            builder.field(info.getField(), info.getValue());
        }

        return builder.endObject();
    }
}
