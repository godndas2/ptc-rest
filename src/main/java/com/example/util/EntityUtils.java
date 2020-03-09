package com.example.util;

import com.example.model.common.BaseEntity;
import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.Collection;

/**
* @author halfdev
* @since 2020-03-10
*  Look up the entity of the given class with the given id in the given collection.
*/
public abstract class EntityUtils {

    public static <T extends BaseEntity> T getById(Collection<T> entities, Class<T> entityClass, int entityId)
            throws ObjectRetrievalFailureException {
        for (T entity : entities) {
            if (entity.getId() == entityId && entityClass.isInstance(entity)) {
                return entity;
            }
        }
        throw new ObjectRetrievalFailureException(entityClass, entityId);
    }

}
