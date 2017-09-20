package com.stephen.zhihu.dao;

import com.stephen.zhihu.domain_jpa.Question;
import com.stephen.zhihu.domain_jpa.Topic;
import com.stephen.zhihu.domain_jpa.User;
import com.stephen.zhihu.dto.QuestionRequestBody;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class QuestionRepositoryImpl extends BaseRepository implements QuestionRepository {

    @Autowired
    public QuestionRepositoryImpl(SessionFactory sf) {
        super(sf);
    }

    @Override
    public Question getQuestion(Long id) {
        /*Session session = getCurrentSession();
        return session.get(Question.class, id);*/
        return null;
    }

    @Override
    public Long addQuestion(Long userId, QuestionRequestBody body) {
        Session session = getCurrentSession();

        Question question = new Question();
        question.setTitle(body.getTitle());
        question.setDescription(body.getDescription());

        User user = session.getReference(User.class, userId);
        question.setUser(user);

        for (Long topicId : body.getTopics()) {
            Topic topic = session.getReference(Topic.class, topicId);
            question.getTopics().add(topic);
        }

        session.persist(question);

        return question.getId();
    }

    @Override
    public Long getQuestionerId(Long questionId) {
        Session session = getCurrentSession();
        TypedQuery<Long> query = session.createQuery("select q.user.id from Question q where q.id = :id").setParameter("id", questionId);
        return query.getSingleResult();
    }
}
