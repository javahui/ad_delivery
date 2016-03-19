package com.hupu.ad.domain.serializer;

import org.springframework.stereotype.Component;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.hupu.ad.domain.AdList;

@Component
public class AdListSerializer extends AbstractSerializer<AdList> {
	
	@Override
	public void write(Kryo kryo, Output output, AdList adList) {
		output.writeInt(adList.getAdId());
		output.writeString(adList.getTitle());
		output.writeString(adList.getName());
		output.writeString(adList.getIntroduce());
		output.writeInt(adList.getCid());
		output.writeString(adList.getMediaUri());
		output.writeString(adList.getUrl());
		output.writeInt(adList.getClassify());
		output.writeInt(adList.getClassifyId());
		output.writeInt(adList.getIsPersonal()); 
		output.writeInt(adList.getIsFree());
	}

	@Override
	public AdList read(Kryo kryo, Input input, Class<AdList> type) {
		AdList adList = new AdList();
		adList.setAdId(input.readInt());
		adList.setTitle(input.readString());
		adList.setName(input.readString());
		adList.setIntroduce(input.readString());
		adList.setCid(input.readInt());
		adList.setMediaUri(input.readString());
		adList.setUrl(input.readString());
		adList.setClassify(input.readInt());
		adList.setClassifyId(input.readInt());
		adList.setIsPersonal(input.readInt());
		adList.setIsFree(input.readInt());
		return adList;
	}
}
