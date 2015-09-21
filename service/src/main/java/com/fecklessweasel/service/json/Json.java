package com.fecklessweasel.service.json;

import flexjson.JSON;
import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;
import flexjson.JSONException;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * JSON Request/Response parent class.
 * @author Christian Gunderman
 */
public abstract class Json {

    /**
     * Serializes this object into JSON.
     * @return Serialized object as JSON.
     */
    public String serialize() {
        JSONSerializer serializer = new JSONSerializer();
        return serializer.exclude("*.class").serialize(this);
    }

    /**
     * Deserializes given JSON String into this object.
     * @param json Json serialized instance of a class.
     */
    public void deserializeFrom(String json) throws ServiceException {
        try {
            new JSONDeserializer<Json>().use(null, 
                this.getClass()).deserializeInto(json, this);
        } catch (JSONException ex) {
            throw new ServiceException(ServiceStatus.JSON_DS_ERROR, ex);
        }
    }
}
