package com.hupu.ad.domain.serializer;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public abstract class AbstractSerializer<T> extends Serializer<T> implements RedisSerializer<T> {

	@Override
	public byte[] serialize(T t) throws SerializationException {
		if (t == null) {
			return null;
		}
		Output output = new Output(1024);
		this.write(null, output, t);
		return output.toBytes();
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if(ArrayUtils.isEmpty(bytes)){
			return null;
		}
		return this.read(null, new Input(bytes), null);
	}

	

}
