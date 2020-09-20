package com.milchstrabe.rainbow.biz.repository;

import com.milchstrabe.rainbow.server.domain.po.AddContactMessage;
import com.milchstrabe.rainbow.server.domain.po.Message;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContactRequestMessageRepository {

    private static final String COLLECTION = "col_contact_request_message";

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *    private String id;
     *     private Integer msgType;
     *     private T content;
     *     private String sender;
     *     private String receiver;
     *     private Short status;
     *     private Long date;
     * @return
     */
    public List<Message> getAddContactMessage(String userId){
        Query query = new Query();
        query.addCriteria(Criteria.where("receiver").is(userId));
        List<Message> messages = mongoTemplate.find(query, Message.class,COLLECTION);
        return messages;
    }

    public boolean updateAddContactContent(Message<AddContactMessage> message){
        String sender = message.getSender();
        String receiver = message.getReceiver();
        Query query = new Query();
        query.addCriteria(Criteria.where("receiver").is(receiver).and("sender").is(sender));

        Update update = new Update();
        update.set("content.status",message.getContent().getStatus());
        update.set("content.note",message.getContent().getNote());
        update.set("content.receiver",message.getContent().getReceiver());
        update.set("content.sender",message.getContent().getSender());
        UpdateResult upsert = mongoTemplate.upsert(query, update, Message.class,COLLECTION);
        if(upsert.getModifiedCount()>0){
            return true;
        }
        return false;
    }

    public Message getAddContactMessage(String sender,String receiver){
        Query query = new Query();
        query.addCriteria(Criteria.where("receiver").is(receiver).and("sender").is(sender));
        Message messages = mongoTemplate.findOne(query, Message.class,COLLECTION);
        return messages;
    }

    public boolean addContactMessage(Message<AddContactMessage> message){
        mongoTemplate.save(message,COLLECTION);
        return true;
    }

    public boolean handleAddContact(String userId,String sender,Short handle){
        Query query = new Query();
        query.addCriteria(Criteria.where("receiver").is(userId).and("sender").is(sender).and("content.status").is(0));
        Update update = new Update();
        update.set("content.status",handle);
        update.set("status",2);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Message.class,COLLECTION);
        if(updateResult.getModifiedCount() > 0){
            return true;
        }else{
            return false;
        }
    }
}
