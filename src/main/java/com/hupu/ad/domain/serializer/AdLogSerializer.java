package com.hupu.ad.domain.serializer;

import org.springframework.stereotype.Component;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.hupu.ad.domain.AdLog;

@Component
public class AdLogSerializer extends AbstractSerializer<AdLog> {
	
	@Override
	public void write(Kryo kryo, Output output, AdLog adLog) {
		output.writeInt(adLog.getId());
		output.writeInt(adLog.getCid());
		output.writeInt(adLog.getAdId());
		output.writeInt(adLog.getEventType());
		output.writeString(adLog.getUrl());
		output.writeString(adLog.getIp());
		output.writeInt(adLog.getDateline());
		output.writeInt(adLog.getIsUser());
		output.writeInt(adLog.getDkcode());
		output.writeInt(adLog.getActionDkcode());
	}

	@Override
	public AdLog read(Kryo kryo, Input input, Class<AdLog> type) {
		AdLog adLog = new AdLog();
		adLog.setId(input.readInt());
		adLog.setCid(input.readInt());
		adLog.setAdId(input.readInt());
		adLog.setEventType(input.readInt());
		adLog.setUrl(input.readString());
		adLog.setIp(input.readString());
		adLog.setDateline(input.readInt());
		adLog.setIsUser(input.readInt());
		adLog.setDkcode(input.readInt());
		adLog.setActionDkcode(input.readInt());
		return adLog;
	}
}
